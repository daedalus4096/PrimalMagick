package com.verdantartifice.primalmagic.common.tiles.crafting;

import com.verdantartifice.primalmagic.common.items.essence.EssenceType;

public class EssenceFurnaceTileEntity extends AbstractCalcinatorTileEntity {
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
        return affinityAmount / type.getAffinity();
    }
}
