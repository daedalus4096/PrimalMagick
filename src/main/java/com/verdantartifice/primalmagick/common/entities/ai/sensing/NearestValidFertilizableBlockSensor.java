package com.verdantartifice.primalmagick.common.entities.ai.sensing;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.verdantartifice.primalmagick.common.entities.ai.memory.MemoryModuleTypesPM;
import com.verdantartifice.primalmagick.common.tags.BlockTagsPM;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * AI sensor that detects nearby blocks which are valid targets for bone meal fertilization.
 * 
 * @author Daedalus4096
 */
public class NearestValidFertilizableBlockSensor extends Sensor<PathfinderMob> {
    private static final int SCAN_RATE = 40;
    private static final int SCAN_XZ_RADIUS = 4;
    private static final int SCAN_Y_RADIUS = 2;

    public NearestValidFertilizableBlockSensor() {
        super(SCAN_RATE);
    }

    @Override
    protected void doTick(ServerLevel pLevel, PathfinderMob pEntity) {
        BlockPos blockPos = pEntity.blockPosition();
        List<BlockPos> nearby = new ArrayList<>();
        
        for (int x = -SCAN_XZ_RADIUS; x <= SCAN_XZ_RADIUS; x++) {
            for (int y = -SCAN_Y_RADIUS; y <= SCAN_Y_RADIUS; y++) {
                for (int z = -SCAN_XZ_RADIUS; z <= SCAN_XZ_RADIUS; z++) {
                    BlockPos scanPos = blockPos.offset(x, y, z);
                    BlockState scanState = pLevel.getBlockState(scanPos);
                    if (scanState.getBlock() instanceof BonemealableBlock bonemealable && 
                            bonemealable.isValidBonemealTarget(pLevel, scanPos, scanState) &&
                            !scanState.is(BlockTagsPM.TREEFOLK_FERTILIZE_EXEMPT)) {
                        nearby.add(scanPos);
                    }
                }
            }
        }
        nearby.sort(Comparator.comparingDouble(blockPos::distSqr));
        
        Brain<?> brain = pEntity.getBrain();
        if (!nearby.isEmpty()) {
            brain.setMemory(MemoryModuleTypesPM.NEAREST_VALID_FERTILIZABLE_BLOCKS.get(), nearby);
        } else {
            brain.eraseMemory(MemoryModuleTypesPM.NEAREST_VALID_FERTILIZABLE_BLOCKS.get());
        }
    }

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleTypesPM.NEAREST_VALID_FERTILIZABLE_BLOCKS.get());
    }
}
