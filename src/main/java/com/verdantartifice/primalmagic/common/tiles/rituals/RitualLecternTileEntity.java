package com.verdantartifice.primalmagic.common.tiles.rituals;

import com.verdantartifice.primalmagic.common.rituals.IRitualPropTileEntity;
import com.verdantartifice.primalmagic.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagic.common.tiles.base.TileInventoryPM;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

/**
 * Definition of a ritual lectern tile entity.  Holds the lectern's inventory.
 * 
 * @author Daedalus4096
 */
public class RitualLecternTileEntity extends TileInventoryPM implements IRitualPropTileEntity {
    protected BlockPos altarPos = null;
    
    public RitualLecternTileEntity() {
        super(TileEntityTypesPM.RITUAL_LECTERN.get(), 1);
    }
    
    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        this.altarPos = compound.contains("AltarPos", Constants.NBT.TAG_LONG) ? BlockPos.fromLong(compound.getLong("AltarPos")) : null;
    }
    
    @Override
    public CompoundNBT write(CompoundNBT compound) {
        if (this.altarPos != null) {
            compound.putLong("AltarPos", this.altarPos.toLong());
        }
        return super.write(compound);
    }
    
    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        // Don't allow automation to add items
        return false;
    }
    
    @Override
    public int getInventoryStackLimit() {
        return 1;
    }
    
    @Override
    public BlockPos getAltarPos() {
        return this.altarPos;
    }

    @Override
    public void setAltarPos(BlockPos pos) {
        this.altarPos = pos;
        this.markDirty();
    }

    @Override
    public void notifyAltarOfPropActivation() {
        if (this.altarPos != null) {
            TileEntity tile = this.world.getTileEntity(this.altarPos);
            if (tile instanceof RitualAltarTileEntity) {
                ((RitualAltarTileEntity)tile).onPropActivation(this.pos);
            }
        }
    }
}
