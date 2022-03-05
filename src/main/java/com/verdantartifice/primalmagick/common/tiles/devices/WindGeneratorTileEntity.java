package com.verdantartifice.primalmagick.common.tiles.devices;

import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.TilePM;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Definition of a wind generator tile entity.  Works with either a Zephyr Engine (wind pushes) or a
 * Void Turbine (wind pulls).
 * 
 * @author Daedalus4096
 */
public class WindGeneratorTileEntity extends TilePM {
    public WindGeneratorTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.WIND_GENERATOR.get(), pos, state);
    }
    
    public static void tick(Level level, BlockPos pos, BlockState state, WindGeneratorTileEntity entity) {
        // TODO Move entities in the direction of this device's generated wind
    }
}
