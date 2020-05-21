package com.verdantartifice.primalmagic.common.tiles.rituals;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.tiles.base.TilePM;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

/**
 * Base class for a ritual prop tile entity.  Holds information about the altar it interacts with.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractRitualPropTileEntity extends TilePM {
    protected BlockPos altarPos = null;
    
    public AbstractRitualPropTileEntity(TileEntityType<?> type) {
        super(type);
    }
    
    @Nullable
    public BlockPos getAltarPos() {
        return this.altarPos;
    }
    
    public void setAltarPos(@Nullable BlockPos pos) {
        this.altarPos = pos;
        this.markDirty();
    }
    
    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        this.altarPos = compound.contains("AltarPos", Constants.NBT.TAG_LONG) ? BlockPos.fromLong(compound.getLong("AltarPos")) : null;
    }
    
    @Override
    public CompoundNBT write(CompoundNBT compound) {
        if (this.altarPos != null) {
            compound.putLong("AltarPos", this.altarPos.toLong());
        }
        return super.write(compound);
    }
    
    public void notifyAltarOfPropActivation() {
        if (this.altarPos != null) {
            TileEntity tile = this.world.getTileEntity(this.altarPos);
            if (tile instanceof RitualAltarTileEntity) {
                ((RitualAltarTileEntity)tile).onPropActivation(this.pos);
            }
        }
    }
}
