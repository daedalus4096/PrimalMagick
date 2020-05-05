package com.verdantartifice.primalmagic.common.tiles.devices;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagic.common.tiles.base.TilePM;

import net.minecraft.tileentity.ITickableTileEntity;

/**
 * Definition of a sunlamp tile entity.  Periodically attempts to spawn glow fields in dark air
 * near the sunlamp.
 * 
 * @author Daedalus4096
 */
public class SunlampTileEntity extends TilePM implements ITickableTileEntity {
    protected int ticksExisted = 0;
    
    public SunlampTileEntity() {
        super(TileEntityTypesPM.SUNLAMP.get());
    }

    @Override
    public void tick() {
        this.ticksExisted++;
        if (!this.world.isRemote && this.ticksExisted % 5 == 0) {
            // TODO pick a random location within 15 blocks
            // TODO if location is ordinary air and dark enough and in line-of-sight, spawn a glow field there
            PrimalMagic.LOGGER.debug("Trying to spawn glow field");
        }
    }
}
