package com.verdantartifice.primalmagic.common.tiles.crafting;

import com.verdantartifice.primalmagic.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagic.common.tiles.base.TilePM;

import net.minecraft.tileentity.ITickableTileEntity;

public class CalcinatorTileEntity extends TilePM implements ITickableTileEntity {
    public int counter = 0;
    
    public CalcinatorTileEntity() {
        super(TileEntityTypesPM.CALCINATOR);
    }

    @Override
    public void tick() {
        // TODO Auto-generated method stub
        this.counter++;
    }

}
