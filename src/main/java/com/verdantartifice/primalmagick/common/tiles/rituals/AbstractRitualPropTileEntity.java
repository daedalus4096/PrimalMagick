package com.verdantartifice.primalmagick.common.tiles.rituals;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.rituals.IRitualPropTileEntity;
import com.verdantartifice.primalmagick.common.tiles.base.TilePM;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.Constants;

/**
 * Base class for a ritual prop tile entity.  Holds information about the altar it interacts with.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractRitualPropTileEntity extends TilePM implements IRitualPropTileEntity {
    protected BlockPos altarPos = null;
    protected boolean isOpen = false;
    
    public AbstractRitualPropTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    @Override
    public boolean isPropOpen() {
        return this.isOpen;
    }

    @Override
    public void setPropOpen(boolean open) {
        this.isOpen = open;
        this.setChanged();
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
    public void load(CompoundTag compound) {
        super.load(compound);
        this.altarPos = compound.contains("AltarPos", Constants.NBT.TAG_LONG) ? BlockPos.of(compound.getLong("AltarPos")) : null;
        this.isOpen = compound.contains("PropOpen", Constants.NBT.TAG_BYTE) ? compound.getBoolean("PropOpen") : false;
    }
    
    @Override
    public CompoundTag save(CompoundTag compound) {
        if (this.altarPos != null) {
            compound.putLong("AltarPos", this.altarPos.asLong());
        }
        compound.putBoolean("PropOpen", this.isOpen);
        return super.save(compound);
    }
    
    @Override
    public void notifyAltarOfPropActivation(float stabilityBonus) {
        if (this.altarPos != null) {
            BlockEntity tile = this.level.getBlockEntity(this.altarPos);
            if (tile instanceof RitualAltarTileEntity altarTile) {
                altarTile.onPropActivation(this.worldPosition, stabilityBonus);
            }
        }
    }
}
