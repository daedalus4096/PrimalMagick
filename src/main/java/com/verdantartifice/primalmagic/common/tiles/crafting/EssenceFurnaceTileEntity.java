package com.verdantartifice.primalmagic.common.tiles.crafting;

import com.verdantartifice.primalmagic.common.items.essence.EssenceType;
import com.verdantartifice.primalmagic.common.tiles.TileEntityTypesPM;

/**
 * Definition of an essence furnace tile entity.  Provides the melting functionality for the corresponding
 * device.  Works slowly and generates little essence, with no dreg chance.
 * 
 * @author Daedalus4096
 */
public class EssenceFurnaceTileEntity extends AbstractCalcinatorTileEntity {
    public EssenceFurnaceTileEntity() {
        super(TileEntityTypesPM.ESSENCE_FURNACE.get());
    }
    
    @Override
    protected int getCookTimeTotal() {
        return 200;
    }

    @Override
    protected boolean canGenerateDregs() {
        return false;
    }

    @Override
    protected int getOutputEssenceCount(int affinityAmount, EssenceType type) {
        return 1;
    }
}
