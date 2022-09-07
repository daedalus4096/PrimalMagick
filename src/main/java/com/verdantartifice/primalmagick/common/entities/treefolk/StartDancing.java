package com.verdantartifice.primalmagick.common.entities.treefolk;

import com.google.common.collect.ImmutableMap;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class StartDancing<E extends TreefolkEntity> extends Behavior<E> {
    private final int danceDuration;
    
    public StartDancing(int danceDuration) {
        super(ImmutableMap.of(MemoryModuleType.CELEBRATE_LOCATION, MemoryStatus.VALUE_ABSENT, MemoryModuleType.DANCING, MemoryStatus.REGISTERED));
        this.danceDuration = danceDuration;
    }

    @Override
    protected void start(ServerLevel pLevel, E pEntity, long pGameTime) {
        pEntity.getBrain().setMemoryWithExpiry(MemoryModuleType.DANCING, true, this.danceDuration);
        pEntity.getBrain().setMemoryWithExpiry(MemoryModuleType.CELEBRATE_LOCATION, pEntity.blockPosition(), this.danceDuration);
    }
}
