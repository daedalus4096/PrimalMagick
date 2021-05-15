package com.verdantartifice.primalmagic.common.tiles.devices;

import com.verdantartifice.primalmagic.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagic.common.tiles.base.TileInventoryPM;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;

/**
 * Definition of a sanguine crucible tile entity.  Holds the crucible's core inventory and souls,
 * as well as handling the actual summoning of creatures.
 * 
 * @author Daedalus4096
 */
public class SanguineCrucibleTileEntity extends TileInventoryPM implements ITickableTileEntity {
    protected static final int FLUID_CAPACITY = 1000;
    protected static final int CHARGE_MAX = 100;
    
    protected int souls;
    protected int fluidAmount;
    protected int charge;
    
    public SanguineCrucibleTileEntity() {
        super(TileEntityTypesPM.SANGUINE_CRUCIBLE.get(), 1);
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        this.souls = compound.getInt("Souls");
        this.fluidAmount = compound.getInt("FluidAmount");
        this.charge = compound.getInt("Charge");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("Souls", this.souls);
        compound.putInt("FluidAmount", this.fluidAmount);
        compound.putInt("Charge", this.charge);
        return super.write(compound);
    }

    @Override
    public void tick() {
        if (this.fluidAmount < FLUID_CAPACITY) {
            this.fluidAmount++;
        }
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack slotStack = this.items.get(index);
        super.setInventorySlotContents(index, stack);
        if (index == 0 && (stack.isEmpty() || !stack.isItemEqual(slotStack) || !ItemStack.areItemStackTagsEqual(stack, slotStack))) {
            this.charge = 0;
            this.markDirty();
        }
    }
    
    public int getSouls() {
        return this.souls;
    }
    
    public void addSouls(int count) {
        this.souls += count;
    }
    
    public int getFluidAmount() {
        return this.fluidAmount;
    }
    
    public float getFluidHeight() {
        return 0.3F + (0.5F * ((float)this.getFluidAmount() / (float)FLUID_CAPACITY));
    }
    
    public int getCharge() {
        return this.charge;
    }
    
    public boolean hasCore() {
        return !this.items.get(0).isEmpty();
    }
}
