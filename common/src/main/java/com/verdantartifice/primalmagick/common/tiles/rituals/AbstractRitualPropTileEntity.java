package com.verdantartifice.primalmagick.common.tiles.rituals;

import com.verdantartifice.primalmagick.common.rituals.IRitualPropTileEntity;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTilePM;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

/**
 * Base class for a ritual prop tile entity.  Holds information about the altar it interacts with.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractRitualPropTileEntity extends AbstractTilePM implements IRitualPropTileEntity {
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
    public void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        this.altarPos = compound.contains("AltarPos", Tag.TAG_LONG) ? BlockPos.of(compound.getLong("AltarPos")) : null;
        this.isOpen = compound.contains("PropOpen", Tag.TAG_BYTE) ? compound.getBoolean("PropOpen") : false;
    }
    
    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        if (this.altarPos != null) {
            compound.putLong("AltarPos", this.altarPos.asLong());
        }
        compound.putBoolean("PropOpen", this.isOpen);
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
