package com.verdantartifice.primalmagic.common.tiles.devices;

import com.verdantartifice.primalmagic.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagic.common.containers.HoneyExtractorContainer;
import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagic.common.tiles.base.TileInventoryPM;
import com.verdantartifice.primalmagic.common.wands.IWand;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Definition of a honey extractor tile entity.  Performs the extraction for the corresponding block.
 * 
 * @see {@link com.verdantartifice.primalmagic.common.blocks.devices.HoneyExtractorBlock}
 * @author Daedalus4096
 */
public class HoneyExtractorTileEntity extends TileInventoryPM implements ITickableTileEntity, INamedContainerProvider {
    protected int spinTime;
    protected int spinTimeTotal;
    protected ManaStorage manaStorage;
    
    // Define a container-trackable representation of this tile's relevant data
    protected final IIntArray extractorData = new IIntArray() {
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
        public int size() {
            return 4;
        }
    };
    
    public HoneyExtractorTileEntity() {
        super(TileEntityTypesPM.HONEY_EXTRACTOR.get(), 5);
        this.manaStorage = new ManaStorage(10000, 100, 100, Source.SKY);
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        this.spinTime = compound.getInt("SpinTime");
        this.spinTimeTotal = compound.getInt("SpinTimeTotal");
        this.manaStorage.deserializeNBT(compound.getCompound("ManaStorage"));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("SpinTime", this.spinTime);
        compound.putInt("SpinTimeTotal", this.spinTimeTotal);
        compound.put("ManaStorage", this.manaStorage.serializeNBT());
        return super.write(compound);
    }

    @Override
    public Container createMenu(int windowId, PlayerInventory playerInv, PlayerEntity player) {
        return new HoneyExtractorContainer(windowId, playerInv, this, this.extractorData);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(this.getBlockState().getBlock().getTranslationKey());
    }
    
    protected int getSpinTimeTotal() {
        return 200;
    }

    @Override
    public void tick() {
        boolean shouldMarkDirty = false;

        if (!this.world.isRemote) {
            // Fill up internal mana storage with that from any inserted wands
            ItemStack wandStack = this.items.get(4);
            if (!wandStack.isEmpty() && wandStack.getItem() instanceof IWand) {
                IWand wand = (IWand)wandStack.getItem();
                int centimanaMissing = this.manaStorage.getMaxManaStored(Source.SKY) - this.manaStorage.getManaStored(Source.SKY);
                int centimanaToTransfer = MathHelper.clamp(centimanaMissing, 0, 100);
                if (wand.consumeMana(wandStack, null, Source.SKY, centimanaToTransfer)) {
                    this.manaStorage.receiveMana(Source.SKY, centimanaToTransfer, false);
                    shouldMarkDirty = true;
                }
            }
            
            // Process ingredients
            // TODO require and consume sky mana per extraction
            ItemStack honeycombStack = this.items.get(0);
            ItemStack bottleStack = this.items.get(1);
            if (!honeycombStack.isEmpty() && !bottleStack.isEmpty()) {
                // If spinnable input is in place, process it
                if (this.canSpin()) {
                    this.spinTime++;
                    if (this.spinTime == this.spinTimeTotal) {
                        this.spinTime = 0;
                        this.spinTimeTotal = this.getSpinTimeTotal();
                        this.doExtraction();
                        shouldMarkDirty = true;
                    }
                } else {
                    this.spinTime = 0;
                }
            } else if (this.spinTime > 0) {
                // Decay any spin progress
                this.spinTime = MathHelper.clamp(this.spinTime - 2, 0, this.spinTimeTotal);
            }
        }
        if (shouldMarkDirty) {
            this.markDirty();
            this.syncTile(true);
        }
    }

    protected boolean canSpin() {
        ItemStack honeyOutput = this.items.get(2);
        ItemStack beeswaxOutput = this.items.get(3);
        return (honeyOutput.getCount() < this.getInventoryStackLimit() &&
                honeyOutput.getCount() < honeyOutput.getMaxStackSize() &&
                beeswaxOutput.getCount() < this.getInventoryStackLimit() &&
                beeswaxOutput.getCount() < beeswaxOutput.getMaxStackSize());
    }
    
    protected void doExtraction() {
        ItemStack honeycombStack = this.items.get(0);
        ItemStack bottleStack = this.items.get(1);
        if (!honeycombStack.isEmpty() && !bottleStack.isEmpty() && this.canSpin()) {
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
        }
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack slotStack = this.items.get(index);
        super.setInventorySlotContents(index, stack);
        if ((index == 0 || index == 1) && (stack.isEmpty() || !stack.isItemEqual(slotStack) || !ItemStack.areItemStackTagsEqual(stack, slotStack))) {
            this.spinTimeTotal = this.getSpinTimeTotal();
            this.spinTime = 0;
            this.markDirty();
        }
    }
}
