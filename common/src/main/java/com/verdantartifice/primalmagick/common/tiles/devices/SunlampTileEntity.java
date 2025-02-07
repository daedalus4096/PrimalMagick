package com.verdantartifice.primalmagick.common.tiles.devices;

import com.verdantartifice.primalmagick.common.blocks.devices.SunlampBlock;
import com.verdantartifice.primalmagick.common.tiles.BlockEntityTypesPM;
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
    private static final int LIGHT_THRESHOLD = 11;
    
    protected int ticksExisted = 0;
    
    public SunlampTileEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypesPM.SUNLAMP.get(), pos, state);
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
            final BlockPos finalPos = new BlockPos(bp);
            if (state.getBlock() instanceof SunlampBlock lampBlock) {
                lampBlock.getGlowField(level.registryAccess()).ifPresent(glow -> {
                    if ( level.isEmptyBlock(finalPos) &&
                         !level.getBlockState(finalPos).is(glow) &&
                         level.getBrightness(LightLayer.BLOCK, finalPos) < LIGHT_THRESHOLD &&
                         RayTraceUtils.hasLineOfSight(level, pos, finalPos) ) {
                        level.setBlock(finalPos, glow.defaultBlockState(), Block.UPDATE_ALL);
                    }
                });
            }
        }
    }
}
