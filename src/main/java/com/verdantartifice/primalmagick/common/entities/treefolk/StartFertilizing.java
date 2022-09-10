package com.verdantartifice.primalmagick.common.entities.treefolk;

import java.util.Collections;
import java.util.function.Predicate;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagick.common.entities.ai.memory.MemoryModuleTypesPM;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class StartFertilizing<E extends TreefolkEntity> extends Behavior<E> {
    private final Predicate<E> canWorkPredicate;
    
    public StartFertilizing(Predicate<E> canWorkPredicate) {
        this(canWorkPredicate, 60);
    }
    
    public StartFertilizing(Predicate<E> canWorkPredicate, int duration) {
        super(ImmutableMap.of(MemoryModuleTypesPM.NEAREST_VALID_FERTILIZABLE_BLOCKS.get(), MemoryStatus.VALUE_PRESENT, MemoryModuleTypesPM.FERTILIZE_LOCATION.get(), MemoryStatus.VALUE_ABSENT, MemoryModuleTypesPM.FERTILIZED_RECENTLY.get(), MemoryStatus.VALUE_ABSENT, MemoryModuleTypesPM.DISABLE_WALK_TO_FERTILIZE_BLOCK.get(), MemoryStatus.VALUE_ABSENT), duration);
        this.canWorkPredicate = canWorkPredicate;
    }

    @Override
    protected void start(ServerLevel pLevel, E pEntity, long pGameTime) {
        pEntity.getBrain().getMemory(MemoryModuleTypesPM.NEAREST_VALID_FERTILIZABLE_BLOCKS.get()).ifPresent(list -> {
            pEntity.getBrain().setMemory(MemoryModuleTypesPM.FERTILIZE_LOCATION.get(), list.get(0));
        });
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel pLevel, E pOwner) {
        return this.canWorkPredicate.test(pOwner) && !pOwner.getBrain().getMemory(MemoryModuleTypesPM.NEAREST_VALID_FERTILIZABLE_BLOCKS.get()).orElse(Collections.emptyList()).isEmpty();
    }
}
