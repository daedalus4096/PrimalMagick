package com.verdantartifice.primalmagick.common.entities.ai.behavior;

import com.google.common.collect.ImmutableMap;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.monster.RangedAttackMob;

public class LongDistanceRangedAttack<E extends Mob & RangedAttackMob> extends Behavior<E> {
    protected final int cooldownBetweenAttacks;
    protected final float minDistanceSq;
    protected final float maxDistanceSq;
    protected final float maxDistance;

    public LongDistanceRangedAttack(int cooldownBetweenAttacks, float minDistance, float maxDistance) {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryStatus.VALUE_ABSENT));
        this.cooldownBetweenAttacks = cooldownBetweenAttacks;
        this.minDistanceSq = minDistance * minDistance;
        this.maxDistanceSq = maxDistance * maxDistance;
        this.maxDistance = maxDistance;
    }

    @Override
    protected void start(ServerLevel pLevel, E pEntity, long pGameTime) {
        LivingEntity livingentity = this.getAttackTarget(pEntity);
        BehaviorUtils.lookAtEntity(pEntity, livingentity);
        float distSqr = (float)pEntity.distanceToSqr(livingentity);
        float f = Mth.sqrt(distSqr) / this.maxDistance;
        pEntity.performRangedAttack(livingentity, Mth.clamp(f, 0.1F, 1.0F));
        pEntity.getBrain().setMemoryWithExpiry(MemoryModuleType.ATTACK_COOLING_DOWN, true, (long)this.cooldownBetweenAttacks);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel pLevel, E pOwner) {
        LivingEntity livingentity = this.getAttackTarget(pOwner);
        double distSqr = pOwner.distanceToSqr(livingentity);
        return BehaviorUtils.canSee(pOwner, livingentity) && distSqr > this.minDistanceSq && distSqr <= this.maxDistanceSq;
    }

    private LivingEntity getAttackTarget(Mob mob) {
        return mob.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
    }
}
