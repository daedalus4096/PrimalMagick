package com.verdantartifice.primalmagick.common.entities.treefolk;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagick.common.entities.ai.memory.MemoryModuleTypesPM;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class StartDancing<E extends TreefolkEntity> extends Behavior<E> {
    private final int danceDuration;
    private final int danceCooldown;
    
    public StartDancing(int danceDuration, int danceCooldown) {
        super(ImmutableMap.of(MemoryModuleType.CELEBRATE_LOCATION, MemoryStatus.VALUE_ABSENT, MemoryModuleType.DANCING, MemoryStatus.VALUE_ABSENT, MemoryModuleTypesPM.DANCED_RECENTLY.get(), MemoryStatus.VALUE_ABSENT, MemoryModuleTypesPM.NEARBY_ADULT_TREEFOLK.get(), MemoryStatus.REGISTERED));
        this.danceDuration = danceDuration;
        this.danceCooldown = danceCooldown;
    }

    @Override
    protected void start(ServerLevel pLevel, E pEntity, long pGameTime) {
        TreefolkAi.startDanceParty(pEntity, this.danceDuration, this.danceCooldown);
    }
}
