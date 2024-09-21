package com.verdantartifice.primalmagick.common.entities.treefolk;

import com.google.common.collect.ImmutableMap;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class StopAdmiringIfTiredOfTryingToReachItem<E extends TreefolkEntity> extends Behavior<E> {
    private final int maxTimeToReachItem;
    private final int disableTime;

    public StopAdmiringIfTiredOfTryingToReachItem(int maxTimeToReachItem, int disableTime) {
        super(ImmutableMap.of(MemoryModuleType.ADMIRING_ITEM, MemoryStatus.VALUE_PRESENT, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryStatus.VALUE_PRESENT, MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM, MemoryStatus.REGISTERED, MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM, MemoryStatus.REGISTERED));
        this.maxTimeToReachItem = maxTimeToReachItem;
        this.disableTime = disableTime;
    }

    @Override
    protected void start(ServerLevel pLevel, E pEntity, long pGameTime) {
        Brain<TreefolkEntity> brain = pEntity.getBrain();
        brain.getMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM).ifPresentOrElse(time -> {
            if (time > this.maxTimeToReachItem) {
                brain.eraseMemory(MemoryModuleType.ADMIRING_ITEM);
                brain.eraseMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM);
                brain.setMemoryWithExpiry(MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM, true, (long)this.disableTime);
            } else {
                brain.setMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM, time + 1);
            }
        }, () -> {
            brain.setMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM, 0);
        });
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel pLevel, E pOwner) {
        return pOwner.getOffhandItem().isEmpty();
    }
}
