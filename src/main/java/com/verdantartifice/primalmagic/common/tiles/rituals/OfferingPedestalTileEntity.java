package com.verdantartifice.primalmagic.common.tiles.rituals;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.verdantartifice.primalmagic.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagic.common.tiles.base.TileInventoryPM;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Definition of an offering pedestal tile entity.  Holds the pedestal's inventory.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagic.common.blocks.rituals.OfferingPedestalBlock}
 */
public class OfferingPedestalTileEntity extends TileInventoryPM {
    protected static final int[] SLOTS = new int[] { 0 };
    
    public OfferingPedestalTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.OFFERING_PEDESTAL.get(), pos, state, 1);
    }
    
    @Override
    protected Set<Integer> getSyncedSlotIndices() {
        // Sync the pedestal's item stack for client rendering use
        return ImmutableSet.of(Integer.valueOf(0));
    }
    
    public static void tick(Level level, BlockPos pos, BlockState state, OfferingPedestalTileEntity entity) {
        // FIXME Remove when Forge onLoad bug is fixed
        if (entity.ticksExisted == 0) {
            entity.doInventorySync();
        }
        entity.ticksExisted++;
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
