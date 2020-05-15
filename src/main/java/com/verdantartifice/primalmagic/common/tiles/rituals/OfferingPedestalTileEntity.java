package com.verdantartifice.primalmagic.common.tiles.rituals;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.verdantartifice.primalmagic.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagic.common.tiles.base.TileInventoryPM;

/**
 * Definition of an offering pedestal tile entity.  Holds the pedestal's inventory.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagic.common.blocks.rituals.OfferingPedestalBlock}
 */
public class OfferingPedestalTileEntity extends TileInventoryPM {
    public OfferingPedestalTileEntity() {
        super(TileEntityTypesPM.OFFERING_PEDESTAL.get(), 1);
    }
    
    @Override
    protected Set<Integer> getSyncedSlotIndices() {
        // Sync the pedestal's item stack for client rendering use
        return ImmutableSet.of(Integer.valueOf(0));
    }
}
