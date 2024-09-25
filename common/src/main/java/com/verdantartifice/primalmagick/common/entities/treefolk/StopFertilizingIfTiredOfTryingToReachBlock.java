package com.verdantartifice.primalmagick.common.entities.treefolk;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagick.common.entities.ai.memory.MemoryModuleTypesPM;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class StopFertilizingIfTiredOfTryingToReachBlock<E extends TreefolkEntity> extends Behavior<E> {
    private final int maxTimeToReachBlock;
    private final int disableTime;

    public StopFertilizingIfTiredOfTryingToReachBlock(int maxTimeToReachBlock, int disableTime) {
        super(ImmutableMap.of(MemoryModuleTypesPM.FERTILIZE_LOCATION.get(), MemoryStatus.VALUE_PRESENT, MemoryModuleTypesPM.TIME_TRYING_TO_REACH_FERTILIZE_BLOCK.get(), MemoryStatus.REGISTERED, MemoryModuleTypesPM.DISABLE_WALK_TO_FERTILIZE_BLOCK.get(), MemoryStatus.REGISTERED));
        this.maxTimeToReachBlock = maxTimeToReachBlock;
        this.disableTime = disableTime;
    }

    @Override
    protected void start(ServerLevel pLevel, E pEntity, long pGameTime) {
        Brain<TreefolkEntity> brain = pEntity.getBrain();
        brain.getMemory(MemoryModuleTypesPM.TIME_TRYING_TO_REACH_FERTILIZE_BLOCK.get()).ifPresentOrElse(time -> {
            if (time > this.maxTimeToReachBlock) {
                brain.eraseMemory(MemoryModuleTypesPM.FERTILIZE_LOCATION.get());
                brain.eraseMemory(MemoryModuleTypesPM.TIME_TRYING_TO_REACH_FERTILIZE_BLOCK.get());
                brain.setMemoryWithExpiry(MemoryModuleTypesPM.DISABLE_WALK_TO_FERTILIZE_BLOCK.get(), true, this.disableTime);
            } else {
                brain.setMemory(MemoryModuleTypesPM.TIME_TRYING_TO_REACH_FERTILIZE_BLOCK.get(), time + 1);
            }
        }, () -> {
            brain.setMemory(MemoryModuleTypesPM.TIME_TRYING_TO_REACH_FERTILIZE_BLOCK.get(), 0);
        });
    }
}
