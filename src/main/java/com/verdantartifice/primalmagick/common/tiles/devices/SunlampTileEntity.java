package com.verdantartifice.primalmagick.common.tiles.devices;

import com.verdantartifice.primalmagick.common.blocks.devices.SunlampBlock;
import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTilePM;
import com.verdantartifice.primalmagick.common.util.RayTraceUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

/**
 * Definition of a sunlamp tile entity.  Periodically attempts to spawn glow fields in dark air
 * near the sunlamp.
 * 
 * @author Daedalus4096
 */
public class SunlampTileEntity extends AbstractTilePM {
    protected int ticksExisted = 0;
    
    public SunlampTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.SUNLAMP.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SunlampTileEntity entity) {
        entity.ticksExisted++;
        if (!level.isClientSide && entity.ticksExisted % 5 == 0) {
            // Pick a random location within 15 blocks
            int x = level.random.nextInt(16) - level.random.nextInt(16);
            int y = level.random.nextInt(16) - level.random.nextInt(16);
            int z = level.random.nextInt(16) - level.random.nextInt(16);
            BlockPos bp = pos.offset(x, y, z);
            
            // Constrain the selected block pos
            BlockPos rainHeight = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, bp);
            if (bp.getY() > rainHeight.getY() + 4) {
                bp = rainHeight.above(4);
            }
            if (bp.getY() < 1) {
                bp = new BlockPos(bp.getX(), 1, bp.getZ());
            }

            // If location is ordinary air and dark enough and in line-of-sight, spawn a glow field there
            Block block = state.getBlock();
            if (block instanceof SunlampBlock) {
                Block glowBlock = ((SunlampBlock)block).getGlowField();
                if ( level.isEmptyBlock(bp) &&
                     level.getBlockState(bp) != glowBlock.defaultBlockState() &&
                     level.getBrightness(LightLayer.BLOCK, bp) < 11 &&
                     RayTraceUtils.hasLineOfSight(level, pos, bp) ) {
                    level.setBlock(bp, glowBlock.defaultBlockState(), Block.UPDATE_ALL);
                }
            }
        }
    }
}
