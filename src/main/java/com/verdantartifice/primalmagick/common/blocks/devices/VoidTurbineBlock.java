package com.verdantartifice.primalmagick.common.blocks.devices;

import com.verdantartifice.primalmagick.client.fx.particles.ParticleTypesPM;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * Definition of a wind generator block that pulls entities in.
 *  
 * @author Daedalus4096
 */
public class VoidTurbineBlock extends AbstractWindGeneratorBlock {
    @Override
    public Direction getWindDirection(BlockState state) {
        return state.getValue(FACING).getOpposite();
    }

    @Override
    public int getCoreColor() {
        return Source.VOID.getColor();
    }

    @Override
    public ParticleOptions getParticleType() {
        return ParticleTypesPM.VOID_SMOKE.get();
    }

    @Override
    protected Vec3 getParticleStartPoint(BlockState state, Level level, BlockPos pos, RandomSource random) {
        Vec3 startPoint = super.getParticleStartPoint(state, level, pos, random);
        
        // Shift the start point forward along the block's facing vector
        int power = level.getBestNeighborSignal(pos);
        Direction facing = state.getValue(AbstractWindGeneratorBlock.FACING);
        if (power < 1) {
            return startPoint;
        }
        
        // Calculate the distance of the shift, based on redstone power unless obstructed by a solid block
        int lineOfSightPower = 0;
        while (lineOfSightPower < power) {
            if (!level.isEmptyBlock(pos.relative(facing, lineOfSightPower + 1))) {
                break;
            } else {
                lineOfSightPower++;
            }
        }
        
        return startPoint.add(new Vec3(facing.step()).scale(lineOfSightPower));
    }
}
