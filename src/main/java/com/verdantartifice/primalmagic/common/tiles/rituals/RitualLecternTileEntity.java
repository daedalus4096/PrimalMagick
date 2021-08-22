package com.verdantartifice.primalmagic.common.tiles.rituals;

import com.verdantartifice.primalmagic.common.rituals.IRitualPropTileEntity;
import com.verdantartifice.primalmagic.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagic.common.tiles.base.TileInventoryPM;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.Constants;

/**
 * Definition of a ritual lectern tile entity.  Holds the lectern's inventory.
 * 
 * @author Daedalus4096
 */
public class RitualLecternTileEntity extends TileInventoryPM implements IRitualPropTileEntity {
    protected BlockPos altarPos = null;
    
    public RitualLecternTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.RITUAL_LECTERN.get(), pos, state, 1);
    }
    
    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
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
    public boolean canPlaceItem(int index, ItemStack stack) {
        // Don't allow automation to add items
        return false;
    }
    
    @Override
    public int getMaxStackSize() {
        return 1;
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
