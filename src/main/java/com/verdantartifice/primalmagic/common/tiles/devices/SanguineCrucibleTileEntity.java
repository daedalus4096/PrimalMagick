package com.verdantartifice.primalmagic.common.tiles.devices;

import java.awt.Color;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;
import com.verdantartifice.primalmagic.common.blocks.devices.SanguineCrucibleBlock;
import com.verdantartifice.primalmagic.common.items.misc.SanguineCoreItem;
import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.fx.WandPoofPacket;
import com.verdantartifice.primalmagic.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagic.common.tiles.base.TileInventoryPM;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.Constants;

/**
 * Definition of a sanguine crucible tile entity.  Holds the crucible's core inventory and souls,
 * as well as handling the actual summoning of creatures.
 * 
 * @author Daedalus4096
 */
public class SanguineCrucibleTileEntity extends TileInventoryPM implements ITickableTileEntity {
    protected static final int FLUID_CAPACITY = 1000;
    protected static final int FLUID_DRAIN = 100;
    protected static final int CHARGE_MAX = 100;
    
    protected int souls;
    protected int fluidAmount;
    protected int charge;
    
    public SanguineCrucibleTileEntity() {
        super(TileEntityTypesPM.SANGUINE_CRUCIBLE.get(), 1);
    }

    @Override
    protected Set<Integer> getSyncedSlotIndices() {
        // Sync the crucible's core stack for client use
        return ImmutableSet.of(Integer.valueOf(0));
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
        if (this.hasCore() && this.fluidAmount >= FLUID_DRAIN) {
            SanguineCoreItem core = this.getCoreItem();
            if (core != null && this.souls >= core.getSoulsPerSpawn()) {
                this.charge++;
                if (this.charge >= CHARGE_MAX) {
                    this.charge = 0;
                    this.fluidAmount -= FLUID_DRAIN;
                    this.souls -= core.getSoulsPerSpawn();
                    
                    if (!this.world.isRemote) {
                        if (this.getStackInSlot(0).attemptDamageItem(1, this.world.rand, null)) {
                            this.getStackInSlot(0).shrink(1);
                            this.world.setBlockState(this.pos, this.world.getBlockState(pos).with(SanguineCrucibleBlock.LIT, false), Constants.BlockFlags.DEFAULT_AND_RERENDER);
                        }
                        this.doSpawn(core.getEntityType());
                        PacketHandler.sendToAllAround(new WandPoofPacket(this.pos.up(), Color.WHITE.getRGB(), true, Direction.UP), this.world.getDimensionKey(), this.pos, 32.0D);
                    }
                    
                    this.markDirty();
                    this.syncTile(true);
                }
            }
        }
    }
    
    protected void doSpawn(EntityType<?> entityType) {
        // TODO stub
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack slotStack = this.items.get(index);
        super.setInventorySlotContents(index, stack);
        if (index == 0 && (stack.isEmpty() || !stack.isItemEqual(slotStack) || !ItemStack.areItemStackTagsEqual(stack, slotStack))) {
            this.charge = 0;
            this.markDirty();
            this.syncTile(false);
        }
    }
    
    public int getSouls() {
        return this.souls;
    }
    
    public void addSouls(int count) {
        this.souls += count;
        this.markDirty();
        this.syncTile(false);
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
    
    public double getSmokeChance() {
        return (double)this.charge / (double)CHARGE_MAX;
    }
    
    public boolean hasCore() {
        return !this.items.get(0).isEmpty();
    }
    
    @Nullable
    protected SanguineCoreItem getCoreItem() {
        ItemStack stack = this.world.isRemote ? this.getSyncedStackInSlot(0) : this.getStackInSlot(0);
        if (stack.getItem() instanceof SanguineCoreItem) {
            return ((SanguineCoreItem)stack.getItem());
        } else {
            return null;
        }
    }
}
