package com.verdantartifice.primalmagick.common.tiles.devices;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.verdantartifice.primalmagick.common.items.essence.EssenceItem;
import com.verdantartifice.primalmagick.common.items.essence.EssenceType;
import com.verdantartifice.primalmagick.common.misc.DeviceTier;
import com.verdantartifice.primalmagick.common.misc.ITieredDevice;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.TilePM;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Definition of an essence cask tile entity.
 * 
 * @author Daedalus4096
 */
public class EssenceCaskTileEntity extends TilePM implements MenuProvider, WorldlyContainer {
    protected static final int NUM_ROWS = EssenceType.values().length;
    protected static final int NUM_COLS = Source.SORTED_SOURCES.size();
    protected static final int NUM_SLOTS = NUM_ROWS * NUM_COLS;
    
    protected static final Map<DeviceTier, Integer> CAPACITY = Util.make(new HashMap<>(), map -> {
        map.put(DeviceTier.ENCHANTED, 4096);
        map.put(DeviceTier.FORBIDDEN, 16384);
        map.put(DeviceTier.HEAVENLY, 65536);
    });
    
    protected final Table<EssenceType, Source, Integer> contents = HashBasedTable.create(NUM_ROWS, NUM_COLS);
    
    public EssenceCaskTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.ESSENCE_CASK.get(), pos, state);
        for (EssenceType row : EssenceType.values()) {
            for (Source col : Source.SORTED_SOURCES) {
                this.contents.put(row, col, 0);
            }
        }
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player) {
        // TODO Auto-generated method stub
        return null;
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
        CompoundTag contentsTag = new CompoundTag();
        for (EssenceType type : EssenceType.values()) {
            CompoundTag typeContents = new CompoundTag();
            for (Source source : Source.SORTED_SOURCES) {
                int count = this.contents.contains(type, source) ? this.contents.get(type, source) : 0;
                typeContents.put(source.getTag(), IntTag.valueOf(count));
            }
            contentsTag.put(type.getSerializedName(), typeContents);
        }
        compound.put("CaskContents", contentsTag);
    }

    @Override
    public int getContainerSize() {
        return NUM_SLOTS;
    }

    @Override
    public int getMaxStackSize() {
        return 65536;
    }

    @Override
    public boolean isEmpty() {
        return this.getTotalEssenceCount() <= 0;
    }

    @Override
    public ItemStack getItem(int index) {
        int count = this.getEssenceCountAtSlot(index);
        return count > 0 ? EssenceItem.getEssence(this.getEssenceTypeForIndex(index), this.getEssenceSourceForIndex(index), count) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        int currentCount = this.getEssenceCountAtSlot(index);
        int numToRemove = Math.min(count, currentCount);
        this.setEssenceCountAtSlot(index, currentCount - numToRemove);
        this.setChanged();
        return numToRemove > 0 ? EssenceItem.getEssence(this.getEssenceTypeForIndex(index), this.getEssenceSourceForIndex(index), numToRemove) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        int count = this.getEssenceCountAtSlot(index);
        this.setEssenceCountAtSlot(index, 0);
        this.setChanged();
        return count > 0 ? EssenceItem.getEssence(this.getEssenceTypeForIndex(index), this.getEssenceSourceForIndex(index), count) : ItemStack.EMPTY;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        if ( stack.getItem() instanceof EssenceItem essenceItem && 
             essenceItem.getEssenceType().equals(this.getEssenceTypeForIndex(index)) && 
             essenceItem.getSource().equals(this.getEssenceSourceForIndex(index)) ) {
            int numToSet = Math.min(stack.getCount(), this.getMaxStackSize());
            this.setEssenceCountAtSlot(index, numToSet);
            this.setChanged();
        }
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void clearContent() {
        this.contents.clear();
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, Direction direction) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        // TODO Auto-generated method stub
        return false;
    }
}
