package com.verdantartifice.primalmagic.common.tiles.rituals;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.rituals.IRitualPropTileEntity;
import com.verdantartifice.primalmagic.common.tiles.base.TilePM;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraftforge.common.util.Constants;

/**
 * Base class for a ritual prop tile entity.  Holds information about the altar it interacts with.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractRitualPropTileEntity extends TilePM implements IRitualPropTileEntity {
    protected BlockPos altarPos = null;
    
    public AbstractRitualPropTileEntity(BlockEntityType<?> type) {
        super(type);
    }
    
    @Override
    @Nullable
    public BlockPos getAltarPos() {
        return this.altarPos;
    }
    
    @Override
    public void setAltarPos(@Nullable BlockPos pos) {
        this.altarPos = pos;
        this.setChanged();
    }
    
    @Override
    public void load(BlockState state, CompoundTag compound) {
        super.load(state, compound);
        this.altarPos = compound.contains("AltarPos", Constants.NBT.TAG_LONG) ? BlockPos.of(compound.getLong("AltarPos")) : null;
    }
    
    @Override
    public CompoundTag save(CompoundTag compound) {
        if (this.altarPos != null) {
            compound.putLong("AltarPos", this.altarPos.asLong());
        }
        return super.save(compound);
    }
    
    @Override
    public void notifyAltarOfPropActivation() {
        if (this.altarPos != null) {
            BlockEntity tile = this.level.getBlockEntity(this.altarPos);
            if (tile instanceof RitualAltarTileEntity) {
                ((RitualAltarTileEntity)tile).onPropActivation(this.worldPosition);
            }
        }
    }
}
