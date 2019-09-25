package com.verdantartifice.primalmagic.common.tiles.mana;

import com.verdantartifice.primalmagic.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagic.common.tiles.base.TilePM;

import net.minecraft.tileentity.ITickableTileEntity;

public class AncientManaFontTileEntity extends TilePM implements ITickableTileEntity {
    protected int ticksExisted = 0;
    
    public AncientManaFontTileEntity() {
        super(TileEntityTypesPM.ANCIENT_MANA_FONT);
    }

    @Override
    public void tick() {
        this.ticksExisted++;
    }
}
