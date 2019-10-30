package com.verdantartifice.primalmagic.common.tiles.misc;

import com.verdantartifice.primalmagic.common.containers.AnalysisTableContainer;
import com.verdantartifice.primalmagic.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagic.common.tiles.base.TilePM;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class AnalysisTableTileEntity extends TilePM implements INamedContainerProvider, ITickableTileEntity, ISidedInventory {
    protected static final int[] SLOTS_UP = new int[] {0};
    protected static final int[] SLOTS_OTHER = new int[] {};

    protected NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);
    protected int scanTime;
    protected int scanTimeTotal;
    
    protected final IIntArray analysisData = new IIntArray() {
        @Override
        public int get(int index) {
            switch (index) {
            case 0:
                return AnalysisTableTileEntity.this.scanTime;
            case 1:
                return AnalysisTableTileEntity.this.scanTimeTotal;
            default:
                return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
            case 0:
                AnalysisTableTileEntity.this.scanTime = value;
            case 1:
                AnalysisTableTileEntity.this.scanTimeTotal = value;
            }
        }

        @Override
        public int size() {
            return 2;
        }
    };
    
    public AnalysisTableTileEntity() {
        super(TileEntityTypesPM.ANALYSIS_TABLE);
    }
    
    @Override
    protected void readFromTileNBT(CompoundNBT compound) {
        this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.items);
        this.scanTime = compound.getInt("ScanTime");
        this.scanTimeTotal = compound.getInt("ScanTimeTotal");
    }
    
    @Override
    protected CompoundNBT writeToTileNBT(CompoundNBT compound) {
        ItemStackHelper.saveAllItems(compound, this.items);
        compound.putInt("ScanTime", this.scanTime);
        compound.putInt("ScanTimeTotal", this.scanTimeTotal);
        return compound;
    }
    
    @Override
    public void tick() {
        // TODO Auto-generated method stub
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(this.getBlockState().getBlock().getTranslationKey());
    }

    @Override
    public Container createMenu(int windowId, PlayerInventory inv, PlayerEntity player) {
        return new AnalysisTableContainer(windowId, inv, this, this.analysisData);
    }
    
    protected int getTotalScanTime(ItemStack stack) {
        // TODO make dynamic based on affinities?
        return 200;
    }

    @Override
    public int getSizeInventory() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.items) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.items.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.items, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.items, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack itemstack = this.items.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.items.set(index, stack);
        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }
        if (index == 0 && !flag) {
            this.scanTimeTotal = this.getTotalScanTime(stack);
            this.scanTime = 0;
            this.markDirty();
        }
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        if (this.world.getTileEntity(this.pos) != this) {
            return false;
        } else {
            return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void clear() {
        this.items.clear();
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return (side == Direction.UP) ? SLOTS_UP : SLOTS_OTHER;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, Direction direction) {
        return this.isItemValidForSlot(index, itemStackIn);
    }
    
    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return (index == 0);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return (index == 0);
    }
}
