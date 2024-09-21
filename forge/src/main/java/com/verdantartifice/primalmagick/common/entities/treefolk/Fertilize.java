package com.verdantartifice.primalmagick.common.entities.treefolk;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagick.common.entities.ai.memory.MemoryModuleTypesPM;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class Fertilize<E extends TreefolkEntity> extends Behavior<E> {
    private final float maxDistanceSqr;
    private final IntProvider cooldownRange;
    
    public Fertilize(float maxDistance, IntProvider cooldownRange) {
        super(ImmutableMap.of(MemoryModuleTypesPM.FERTILIZE_LOCATION.get(), MemoryStatus.VALUE_PRESENT, MemoryModuleTypesPM.FERTILIZED_RECENTLY.get(), MemoryStatus.VALUE_ABSENT, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED));
        this.maxDistanceSqr = maxDistance * maxDistance;
        this.cooldownRange = cooldownRange;
    }

    @Override
    protected void start(ServerLevel pLevel, E pEntity, long pGameTime) {
        Brain<TreefolkEntity> brain = pEntity.getBrain();
        BlockPos targetPos = brain.getMemory(MemoryModuleTypesPM.FERTILIZE_LOCATION.get()).orElse(BlockPos.ZERO);
        brain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(targetPos));
        
        BlockState state = pLevel.getBlockState(targetPos);
        if (state.getBlock() instanceof BonemealableBlock bonemealable && bonemealable.isValidBonemealTarget(pLevel, targetPos, state)) {
            if (bonemealable.isBonemealSuccess(pLevel, pLevel.random, targetPos, state)) {
                bonemealable.performBonemeal(pLevel, pLevel.random, targetPos, state);
                pLevel.levelEvent(1505, targetPos, 0);
            }
            brain.setMemoryWithExpiry(MemoryModuleTypesPM.FERTILIZED_RECENTLY.get(), true, this.cooldownRange.sample(pLevel.random));
            brain.eraseMemory(MemoryModuleTypesPM.FERTILIZE_LOCATION.get());
        }
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel pLevel, E pOwner) {
        Brain<TreefolkEntity> brain = pOwner.getBrain();
        Vec3 targetCenter = Vec3.atCenterOf(brain.getMemory(MemoryModuleTypesPM.FERTILIZE_LOCATION.get()).orElse(BlockPos.ZERO));
        return pOwner.position().distanceToSqr(targetCenter) <= this.maxDistanceSqr;
    }
}
