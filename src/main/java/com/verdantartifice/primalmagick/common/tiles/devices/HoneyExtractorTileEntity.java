package com.verdantartifice.primalmagick.common.tiles.devices;

import com.verdantartifice.primalmagick.common.capabilities.IManaStorage;
import com.verdantartifice.primalmagick.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.containers.HoneyExtractorContainer;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.sources.IManaContainer;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.TileInventoryPM;
import com.verdantartifice.primalmagick.common.wands.IWand;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

/**
 * Definition of a honey extractor tile entity.  Performs the extraction for the corresponding block.
 * 
 * @see {@link com.verdantartifice.primalmagick.common.blocks.devices.HoneyExtractorBlock}
 * @author Daedalus4096
 */
public class HoneyExtractorTileEntity extends TileInventoryPM implements MenuProvider, IManaContainer {
    protected static final int[] SLOTS_FOR_UP = new int[] { 0, 1 };
    protected static final int[] SLOTS_FOR_DOWN = new int[] { 2, 3 };
    protected static final int[] SLOTS_FOR_SIDES = new int[] { 4 };
    
    protected int spinTime;
    protected int spinTimeTotal;
    protected ManaStorage manaStorage;
    
    protected LazyOptional<IManaStorage> manaStorageOpt = LazyOptional.of(() -> this.manaStorage);
    
    // Define a container-trackable representation of this tile's relevant data
    protected final ContainerData extractorData = new ContainerData() {
        @Override
        public int get(int index) {
            switch (index) {
            case 0:
                return HoneyExtractorTileEntity.this.spinTime;
            case 1:
                return HoneyExtractorTileEntity.this.spinTimeTotal;
            case 2:
                return HoneyExtractorTileEntity.this.manaStorage.getManaStored(Source.SKY);
            case 3:
                return HoneyExtractorTileEntity.this.manaStorage.getMaxManaStored(Source.SKY);
            default:
                return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            // Don't set mana storage values
            switch (index) {
            case 0:
                HoneyExtractorTileEntity.this.spinTime = value;
                break;
            case 1:
                HoneyExtractorTileEntity.this.spinTimeTotal = value;
                break;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };
    
    public HoneyExtractorTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.HONEY_EXTRACTOR.get(), pos, state, 5);
        this.manaStorage = new ManaStorage(10000, 100, 100, Source.SKY);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.spinTime = compound.getInt("SpinTime");
        this.spinTimeTotal = compound.getInt("SpinTimeTotal");
        this.manaStorage.deserializeNBT(compound.getCompound("ManaStorage"));
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        compound.putInt("SpinTime", this.spinTime);
        compound.putInt("SpinTimeTotal", this.spinTimeTotal);
        compound.put("ManaStorage", this.manaStorage.serializeNBT());
        return super.save(compound);
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player) {
        return new HoneyExtractorContainer(windowId, playerInv, this, this.extractorData);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent(this.getBlockState().getBlock().getDescriptionId());
    }
    
    protected int getSpinTimeTotal() {
        return 200;
    }
    
    protected int getManaCost() {
        return 10;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, HoneyExtractorTileEntity entity) {
        boolean shouldMarkDirty = false;

        if (!level.isClientSide) {
            // Fill up internal mana storage with that from any inserted wands
            ItemStack wandStack = entity.items.get(4);
            if (!wandStack.isEmpty() && wandStack.getItem() instanceof IWand) {
                IWand wand = (IWand)wandStack.getItem();
                int centimanaMissing = entity.manaStorage.getMaxManaStored(Source.SKY) - entity.manaStorage.getManaStored(Source.SKY);
                int centimanaToTransfer = Mth.clamp(centimanaMissing, 0, 100);
                if (wand.consumeMana(wandStack, null, Source.SKY, centimanaToTransfer)) {
                    entity.manaStorage.receiveMana(Source.SKY, centimanaToTransfer, false);
                    shouldMarkDirty = true;
                }
            }
            
            // Process ingredients
            ItemStack honeycombStack = entity.items.get(0);
            ItemStack bottleStack = entity.items.get(1);
            if (!honeycombStack.isEmpty() && !bottleStack.isEmpty() && entity.manaStorage.getManaStored(Source.SKY) >= entity.getManaCost()) {
                // If spinnable input is in place, process it
                if (entity.canSpin()) {
                    entity.spinTime++;
                    if (entity.spinTime == entity.spinTimeTotal) {
                        entity.spinTime = 0;
                        entity.spinTimeTotal = entity.getSpinTimeTotal();
                        entity.doExtraction();
                        shouldMarkDirty = true;
                    }
                } else {
                    entity.spinTime = 0;
                }
            } else if (entity.spinTime > 0) {
                // Decay any spin progress
                entity.spinTime = Mth.clamp(entity.spinTime - 2, 0, entity.spinTimeTotal);
            }
        }
        if (shouldMarkDirty) {
            entity.setChanged();
            entity.syncTile(true);
        }
    }

    protected boolean canSpin() {
        ItemStack honeyOutput = this.items.get(2);
        ItemStack beeswaxOutput = this.items.get(3);
        return (honeyOutput.getCount() < this.getMaxStackSize() &&
                honeyOutput.getCount() < honeyOutput.getMaxStackSize() &&
                beeswaxOutput.getCount() < this.getMaxStackSize() &&
                beeswaxOutput.getCount() < beeswaxOutput.getMaxStackSize());
    }
    
    protected void doExtraction() {
        ItemStack honeycombStack = this.items.get(0);
        ItemStack bottleStack = this.items.get(1);
        if (!honeycombStack.isEmpty() && !bottleStack.isEmpty() && this.canSpin() && this.manaStorage.getManaStored(Source.SKY) >= this.getManaCost()) {
            ItemStack honeyStack = this.items.get(2);
            if (honeyStack.isEmpty()) {
                this.items.set(2, new ItemStack(Items.HONEY_BOTTLE));
            } else {
                honeyStack.grow(1);
            }
            
            ItemStack beeswaxStack = this.items.get(3);
            if (beeswaxStack.isEmpty()) {
                this.items.set(3, new ItemStack(ItemsPM.BEESWAX.get()));
            } else {
                beeswaxStack.grow(1);
            }
            
            honeycombStack.shrink(1);
            bottleStack.shrink(1);
            this.manaStorage.extractMana(Source.SKY, this.getManaCost(), false);
        }
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        ItemStack slotStack = this.items.get(index);
        super.setItem(index, stack);
        if ((index == 0 || index == 1) && (stack.isEmpty() || !stack.sameItem(slotStack) || !ItemStack.tagMatches(stack, slotStack))) {
            this.spinTimeTotal = this.getSpinTimeTotal();
            this.spinTime = 0;
            this.setChanged();
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.remove && cap == PrimalMagickCapabilities.MANA_STORAGE) {
            return this.manaStorageOpt.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.manaStorageOpt.invalidate();
    }

    @Override
    public int getMana(Source source) {
        return this.manaStorage.getManaStored(source);
    }

    @Override
    public SourceList getAllMana() {
        SourceList mana = new SourceList();
        for (Source source : Source.SORTED_SOURCES) {
            int amount = this.manaStorage.getManaStored(source);
            if (amount > 0) {
                mana.add(source, amount);
            }
        }
        return mana;
    }

    @Override
    public int getMaxMana() {
        // TODO Fix up
        return this.manaStorage.getMaxManaStored(Source.SKY);
    }

    @Override
    public void setMana(Source source, int amount) {
        this.manaStorage.setMana(source, amount);
        this.setChanged();
        this.syncTile(true);
    }

    @Override
    public void setMana(SourceList mana) {
        this.manaStorage.setMana(mana);
        this.setChanged();
        this.syncTile(true);
    }

    @Override
    public boolean canPlaceItem(int slotIndex, ItemStack stack) {
        if (slotIndex == 2 || slotIndex == 3) {
            return false;
        } else if (slotIndex == 4) {
            return stack.getItem() instanceof IWand;
        } else if (slotIndex == 1) {
            return stack.is(Items.GLASS_BOTTLE);
        } else {
            return stack.is(Items.HONEYCOMB);
        }
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.UP) {
            return SLOTS_FOR_UP;
        } else if (side == Direction.DOWN) {
            return SLOTS_FOR_DOWN;
        } else {
            return SLOTS_FOR_SIDES;
        }
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, Direction direction) {
        return this.canPlaceItem(index, itemStackIn);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return true;
    }
}
