package com.verdantartifice.primalmagic.common.tiles.crafting;

import com.verdantartifice.primalmagic.common.items.essence.EssenceType;
import com.verdantartifice.primalmagic.common.tiles.TileEntityTypesPM;

/**
 * Definition of a proper calcinator tile entity.  Provides the melting functionality for the corresponding
 * device.  Works quickly and generates essence based on item affinity, with a chance for dregs on low-affinity
 * items.
 * 
 * @author Daedalus4096
 */
public class CalcinatorTileEntity extends AbstractCalcinatorTileEntity {
    public CalcinatorTileEntity() {
        super(TileEntityTypesPM.CALCINATOR.get());
    }
    
    @Override
    protected int getCookTimeTotal() {
        return 100;
    }

    @Override
    protected boolean canGenerateDregs() {
        return true;
    }

    @Override
    protected int getOutputEssenceCount(int affinityAmount, EssenceType type) {
        return affinityAmount / type.getAffinity();
    }
}
