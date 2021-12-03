package com.verdantartifice.primalmagick.common.tiles.rituals;

import com.verdantartifice.primalmagick.common.rituals.IRitualPropTileEntity;
import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.TileInventoryPM;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Definition of a ritual lectern tile entity.  Holds the lectern's inventory.
 * 
 * @author Daedalus4096
 */
public class RitualLecternTileEntity extends TileInventoryPM implements IRitualPropTileEntity {
    protected BlockPos altarPos = null;
    protected boolean isOpen = false;
    
    public RitualLecternTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.RITUAL_LECTERN.get(), pos, state, 1);
    }
    
    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.altarPos = compound.contains("AltarPos", Tag.TAG_LONG) ? BlockPos.of(compound.getLong("AltarPos")) : null;
        this.isOpen = compound.contains("PropOpen", Tag.TAG_BYTE) ? compound.getBoolean("PropOpen") : false;
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
    public boolean canPlaceItem(int index, ItemStack stack) {
        // Don't allow automation to add items
        return false;
    }
    
    @Override
    public int getMaxStackSize() {
        return 1;
    }
    
    @Override
    public boolean isPropOpen() {
        return this.isOpen;
    }

    @Override
    public void setPropOpen(boolean open) {
        this.isOpen = open;
    }

    @Override
    public BlockPos getAltarPos() {
        return this.altarPos;
    }

    @Override
    public void setAltarPos(BlockPos pos) {
        this.altarPos = pos;
        this.setChanged();
    }

    @Override
    public void notifyAltarOfPropActivation(float stabilityBonus) {
        if (this.altarPos != null) {
            BlockEntity tile = this.level.getBlockEntity(this.altarPos);
            if (tile instanceof RitualAltarTileEntity) {
                ((RitualAltarTileEntity)tile).onPropActivation(this.worldPosition, stabilityBonus);
            }
        }
    }
}
