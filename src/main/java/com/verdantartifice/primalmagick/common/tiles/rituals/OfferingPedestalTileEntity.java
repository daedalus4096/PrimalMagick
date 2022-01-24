package com.verdantartifice.primalmagick.common.tiles.rituals;

import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;
import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.TileInventoryPM;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Definition of an offering pedestal tile entity.  Holds the pedestal's inventory.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagick.common.blocks.rituals.OfferingPedestalBlock}
 */
public class OfferingPedestalTileEntity extends TileInventoryPM {
    protected static final int[] SLOTS = new int[] { 0 };
    
    protected BlockPos altarPos = null;
    
    public OfferingPedestalTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.OFFERING_PEDESTAL.get(), pos, state, 1);
    }
    
    @Override
    protected Set<Integer> getSyncedSlotIndices() {
        // Sync the pedestal's item stack for client rendering use
        return ImmutableSet.of(Integer.valueOf(0));
    }
    
    @Nullable
    public BlockPos getAltarPos() {
        return this.altarPos;
    }
    
    public void setAltarPos(@Nullable BlockPos pos) {
        this.altarPos = pos;
        this.setChanged();
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.altarPos = compound.contains("AltarPos", Tag.TAG_LONG) ? BlockPos.of(compound.getLong("AltarPos")) : null;
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        if (this.altarPos != null) {
            compound.putLong("AltarPos", this.altarPos.asLong());
        }
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return SLOTS;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, Direction direction) {
        return this.canPlaceItem(index, itemStackIn);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return true;
    }
}
