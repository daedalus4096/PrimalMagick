package com.verdantartifice.primalmagick.common.entities.ai.sensing;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.verdantartifice.primalmagick.common.entities.ai.memory.MemoryModuleTypesPM;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * AI sensor that detects nearby blocks which are valid targets for bone meal fertilization.
 * 
 * @author Daedalus4096
 */
public class NearestValidBonemealableBlockSensor extends Sensor<PathfinderMob> {
    private static final int SCAN_RATE = 40;
    private static final int SCAN_XZ_RADIUS = 4;
    private static final int SCAN_Y_RADIUS = 2;

    public NearestValidBonemealableBlockSensor() {
        super(SCAN_RATE);
    }

    @Override
    protected void doTick(ServerLevel pLevel, PathfinderMob pEntity) {
        ResourceKey<Level> resourceKey = pLevel.dimension();
        BlockPos blockPos = pEntity.blockPosition();
        List<BlockPos> nearby = new ArrayList<>();
        
        for (int x = -SCAN_XZ_RADIUS; x <= SCAN_XZ_RADIUS; x++) {
            for (int y = -SCAN_Y_RADIUS; y <= SCAN_Y_RADIUS; y++) {
                for (int z = -SCAN_XZ_RADIUS; z <= SCAN_XZ_RADIUS; z++) {
                    BlockPos scanPos = blockPos.offset(x, y, z);
                    BlockState scanState = pLevel.getBlockState(scanPos);
                    if (scanState.getBlock() instanceof BonemealableBlock bonemealable && bonemealable.isValidBonemealTarget(pLevel, scanPos, scanState, pLevel.isClientSide)) {
                        nearby.add(scanPos);
                    }
                }
            }
        }
        nearby.sort(Comparator.comparingDouble(blockPos::distSqr));
        
        List<GlobalPos> result = nearby.stream().map(p -> GlobalPos.of(resourceKey, p)).toList();
        Brain<?> brain = pEntity.getBrain();
        brain.setMemory(MemoryModuleTypesPM.NEAREST_VALID_BONEMEALABLE_BLOCKS.get(), result);
    }

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleTypesPM.NEAREST_VALID_BONEMEALABLE_BLOCKS.get());
    }
}
