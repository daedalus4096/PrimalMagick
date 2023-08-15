package com.verdantartifice.primalmagick.common.tiles.devices;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.verdantartifice.primalmagick.common.blocks.devices.EssenceCaskBlock;
import com.verdantartifice.primalmagick.common.items.essence.EssenceItem;
import com.verdantartifice.primalmagick.common.items.essence.EssenceType;
import com.verdantartifice.primalmagick.common.menus.EssenceCaskContainer;
import com.verdantartifice.primalmagick.common.misc.DeviceTier;
import com.verdantartifice.primalmagick.common.misc.ITieredDevice;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.TileInventoryPM;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Definition of an essence cask tile entity.
 * 
 * @author Daedalus4096
 */
public class EssenceCaskTileEntity extends TileInventoryPM implements MenuProvider {
    public static final int NUM_ROWS = EssenceType.values().length;
    public static final int NUM_COLS = Source.SORTED_SOURCES.size();
    public static final int NUM_SLOTS = NUM_ROWS * NUM_COLS;
    protected static final int INPUT_SLOT_INDEX = 0;
    protected static final int[] SLOTS_FOR_UP = new int[] { INPUT_SLOT_INDEX };
    protected static final int[] SLOTS_FOR_OTHER = new int[] {};

    protected static final Map<DeviceTier, Integer> CAPACITY = Util.make(new HashMap<>(), map -> {
        map.put(DeviceTier.ENCHANTED, 4096);
        map.put(DeviceTier.FORBIDDEN, 8192);
        map.put(DeviceTier.HEAVENLY, 16384);
    });
    
    protected final Table<EssenceType, Source, Integer> contents = HashBasedTable.create(NUM_ROWS, NUM_COLS);
    
    protected final ContainerData caskData = new ContainerData() {
        @Override
        public int get(int index) {
            return EssenceCaskTileEntity.this.getEssenceCountAtSlot(index);
        }

        @Override
        public void set(int index, int value) {
            EssenceCaskTileEntity.this.setEssenceCountAtSlot(index, value);
        }

        @Override
        public int getCount() {
            return NUM_SLOTS;
        }
    };
    
    protected final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
        @Override
        protected void onOpen(Level level, BlockPos pos, BlockState state) {
            level.playSound(null, pos, SoundEvents.BARREL_OPEN, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
            level.setBlock(pos, state.setValue(EssenceCaskBlock.OPEN, true), Block.UPDATE_ALL);
        }

        @Override
        protected void onClose(Level level, BlockPos pos, BlockState state) {
            level.playSound(null, pos, SoundEvents.BARREL_CLOSE, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
            level.setBlock(pos, state.setValue(EssenceCaskBlock.OPEN, false), Block.UPDATE_ALL);
        }

        @Override
        protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int previousCount, int currentCount) {
        }

        @Override
        protected boolean isOwnContainer(Player player) {
            return player.containerMenu instanceof EssenceCaskContainer caskMenu && caskMenu.getContainer() == EssenceCaskTileEntity.this;  // Reference comparison intended
        }
    };
    
    public EssenceCaskTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.ESSENCE_CASK.get(), pos, state, 1);
        for (EssenceType row : EssenceType.values()) {
            for (Source col : Source.SORTED_SOURCES) {
                this.contents.put(row, col, 0);
            }
        }
    }
    
    public static void tick(Level level, BlockPos pos, BlockState state, EssenceCaskTileEntity entity) {
        if (!level.isClientSide && !entity.items.isEmpty()) {
            ItemStack stack = entity.items.get(0);
            if (stack.getItem() instanceof EssenceItem essenceItem) {
                EssenceType essenceType = essenceItem.getEssenceType();
                Source essenceSource = essenceItem.getSource();
                int inputCount = stack.getCount();
                int currentCount = entity.contents.contains(essenceType, essenceSource) ? entity.contents.get(essenceType, essenceSource) : 0;
                int totalCount = entity.getTotalEssenceCount();
                int capacity = entity.getTotalEssenceCapacity();
                if (totalCount + inputCount <= capacity) {
                    entity.contents.put(essenceType, essenceSource, currentCount + inputCount);
                    entity.items.set(0, ItemStack.EMPTY);
                } else {
                    int addable = capacity - totalCount;
                    entity.contents.put(essenceType, essenceSource, currentCount + addable);
                    stack.shrink(addable);
                }
            }
        }
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player) {
        return new EssenceCaskContainer(windowId, playerInv, this, this.caskData, this.worldPosition);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
    }
    
    @Nullable
    protected DeviceTier getDeviceTier() {
        return this.getBlockState().getBlock() instanceof ITieredDevice device ? device.getDeviceTier() : null;
    }
    
    public int getTotalEssenceCapacity() {
        return CAPACITY.getOrDefault(this.getDeviceTier(), 0);
    }
    
    public int getTotalEssenceCount() {
        return this.contents.values().stream().mapToInt(i -> i).sum();
    }
    
    public int getEssenceCountForType(EssenceType type) {
        return this.contents.row(type).entrySet().stream().mapToInt(e -> e.getValue()).sum();
    }
    
    public int getEssenceCountForSource(Source source) {
        return this.contents.column(source).entrySet().stream().mapToInt(e -> e.getValue()).sum();
    }
    
    protected EssenceType getEssenceTypeForIndex(int index) {
        return (index < 0 || index >= NUM_SLOTS) ? null : EssenceType.values()[index / NUM_COLS];
    }
    
    protected Source getEssenceSourceForIndex(int index) {
        return (index < 0 || index >= NUM_SLOTS) ? null : Source.SORTED_SOURCES.get(index % NUM_COLS);
    }
    
    public int getEssenceCount(EssenceType essenceType, Source source) {
        return this.contents.contains(essenceType, source) ? this.contents.get(essenceType, source) : 0;
    }
    
    public int setEssenceCount(EssenceType type, Source source, int amount) {
        if (amount < 0) {
            return 0;
        }
        int capacity = this.getTotalEssenceCapacity();
        if (amount < capacity) {
            this.contents.put(type, source, amount);
            return 0;
        } else {
            this.contents.put(type, source, capacity);
            return amount - capacity;
        }
    }
    
    public int getEssenceCountAtSlot(int index) {
        if (index < 0 || index >= NUM_SLOTS) {
            return 0;
        }
        EssenceType row = this.getEssenceTypeForIndex(index);
        Source col = this.getEssenceSourceForIndex(index);
        return this.contents.contains(row, col) ? this.contents.get(row, col) : 0;
    }
    
    public void setEssenceCountAtSlot(int index, int count) {
        if (index < 0 || index >= NUM_SLOTS) {
            return;
        }
        EssenceType row = this.getEssenceTypeForIndex(index);
        Source col = this.getEssenceSourceForIndex(index);
        this.contents.put(row, col, count);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.loadContentsNbt(compound);
    }
    
    protected void loadContentsNbt(CompoundTag compound) {
        this.contents.clear();
        CompoundTag contentsTag = compound.getCompound("CaskContents");
        for (EssenceType type : EssenceType.values()) {
            CompoundTag typeContents = contentsTag.getCompound(type.getSerializedName());
            for (Source source : Source.SORTED_SOURCES) {
                int count = typeContents.getInt(source.getTag());
                this.contents.put(type, source, count);
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.put("CaskContents", this.getContentsNbt());
    }
    
    protected CompoundTag getContentsNbt() {
        CompoundTag contentsTag = new CompoundTag();
        for (EssenceType type : EssenceType.values()) {
            CompoundTag typeContents = new CompoundTag();
            for (Source source : Source.SORTED_SOURCES) {
                int count = this.contents.contains(type, source) ? this.contents.get(type, source) : 0;
                typeContents.put(source.getTag(), IntTag.valueOf(count));
            }
            contentsTag.put(type.getSerializedName(), typeContents);
        }
        return contentsTag;
    }

    @Override
    public void clearContent() {
        this.items.clear();
        this.contents.clear();
        this.syncSlots(null);
    }

    public void dropContents() {
        Containers.dropContents(this.level, this.worldPosition, this);
        for (Table.Cell<EssenceType, Source, Integer> cell : this.contents.cellSet()) {
            ItemStack tempStack = EssenceItem.getEssence(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
            this.contents.put(cell.getRowKey(), cell.getColumnKey(), 0);
            Containers.dropItemStack(this.level, this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ(), tempStack);
        }
    }

    @Override
    public void startOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.openersCounter.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    @Override
    public void stopOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.openersCounter.decrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }
    
    public void recheckOpen() {
        if (!this.remove) {
            this.openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return direction == Direction.UP ? SLOTS_FOR_UP : SLOTS_FOR_OTHER;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index == INPUT_SLOT_INDEX && stack.getItem() instanceof EssenceItem && direction == Direction.UP;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return false;
    }
}
