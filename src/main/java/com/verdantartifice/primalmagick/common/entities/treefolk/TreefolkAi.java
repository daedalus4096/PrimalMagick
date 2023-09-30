package com.verdantartifice.primalmagick.common.entities.treefolk;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.entities.ai.behavior.LongDistanceRangedAttack;
import com.verdantartifice.primalmagick.common.entities.ai.memory.MemoryModuleTypesPM;
import com.verdantartifice.primalmagick.common.loot.LootTablesPM;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.DoNothing;
import net.minecraft.world.entity.ai.behavior.GoToTargetLocation;
import net.minecraft.world.entity.ai.behavior.GoToWantedItem;
import net.minecraft.world.entity.ai.behavior.InteractWith;
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
import net.minecraft.world.entity.ai.behavior.MeleeAttack;
import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
import net.minecraft.world.entity.ai.behavior.RandomStroll;
import net.minecraft.world.entity.ai.behavior.RunOne;
import net.minecraft.world.entity.ai.behavior.SetEntityLookTarget;
import net.minecraft.world.entity.ai.behavior.SetLookAndInteract;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetAwayFrom;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
import net.minecraft.world.entity.ai.behavior.StartAttacking;
import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
import net.minecraft.world.entity.ai.behavior.StopBeingAngryIfTargetDead;
import net.minecraft.world.entity.ai.behavior.Swim;
import net.minecraft.world.entity.ai.behavior.TryFindLand;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

/**
 * Collection of static methods to help with Treefolk entity AI behaviors.
 * 
 * @author Daedalus4096
 */
public class TreefolkAi {
    public static final Logger LOGGER = LogManager.getLogger();
    private static final UniformInt ANGER_DURATION = TimeUtil.rangeOfSeconds(20, 39);
    private static final UniformInt DANCE_COOLDOWN = TimeUtil.rangeOfSeconds(300, 900);
    private static final UniformInt FERTILIZE_COOLDOWN = TimeUtil.rangeOfSeconds(5, 10);
    private static final int RECENTLY_DANCED_DURATION = 1200;
    private static final int DANCE_DURATION = 600;
    private static final int ADMIRE_DURATION = 120;
    private static final int MAX_DISTANCE_TO_WALK_TO_ITEM = 9;
    private static final int MAX_TIME_TO_WALK_TO_ITEM = 200;
    private static final int HOW_LONG_TIME_TO_DISABLE_ADMIRE_WALKING_IF_CANT_REACH_ITEM = 200;
    private static final int HOW_LONG_TIME_TO_DISABLE_FERTILIZING_IF_CANT_REACH_BLOCK = 200;
    private static final int BABY_FLEE_DURATION_AFTER_GETTING_HIT = 100;
    private static final int HIT_BY_PLAYER_MEMORY_TIMEOUT = 400;
    private static final int MELEE_ATTACK_COOLDOWN = 20;
    private static final int RANGED_ATTACK_COOLDOWN = 30;
    private static final float MIN_RANGED_ATTACK_RANGE = 4F;
    private static final float MAX_RANGED_ATTACK_RANGE = 16F;
    private static final float MAX_FERTILIZE_RANGE = 2F;
    private static final int DESIRED_DISTANCE_FROM_ENTITY_WHEN_AVOIDING = 12;
    private static final int MAX_LOOK_DIST = 8;
    private static final int MAX_LOOK_DIST_FOR_PLAYER_HOLDING_LOVED_ITEM = 14;
    private static final int INTERACTION_RANGE = 8;
    private static final float SPEED_MULTIPLIER_WHEN_GOING_TO_WANTED_ITEM = 1.0F;
    private static final float SPEED_MULTIPLIER_WHEN_FIGHTING = 1.0F;
    private static final float SPEED_MULTIPLIER_WHEN_FLEEING = 1.0F;
    private static final float SPEED_MULTIPLIER_WHEN_IDLING = 0.6F;
    private static final float SPEED_MULTIPLIER_WHEN_WORKING = 0.6F;
    private static final float SWIM_CHANCE = 0.8F;

    public static Brain<?> makeBrain(TreefolkEntity entity, Brain<TreefolkEntity> brain) {
        initCoreActivity(brain);
        initIdleActivity(brain);
        initAdmireItemActivity(brain);
        initFightActivity(entity, brain);
        initAvoidActivity(brain);
        initCelebrateActivity(brain);
        initWorkActivity(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }
    
    private static void initCoreActivity(Brain<TreefolkEntity> brain) {
        brain.addActivity(Activity.CORE, 0, ImmutableList.of(new Swim(SWIM_CHANCE), new LookAtTargetSink(45, 90), new MoveToTargetSink(), StopHoldingItemIfNoLongerAdmiring.create(), StartAdmiringItemIfSeen.create(ADMIRE_DURATION), JoinDanceParty.create(DANCE_DURATION, RECENTLY_DANCED_DURATION), StopBeingAngryIfTargetDead.create()));
    }
    
    private static void initIdleActivity(Brain<TreefolkEntity> brain) {
        brain.addActivity(Activity.IDLE, 10, ImmutableList.of(SetEntityLookTarget.create(TreefolkAi::isPlayerHoldingLovedItem, MAX_LOOK_DIST_FOR_PLAYER_HOLDING_LOVED_ITEM), StartAttacking.create(TreefolkEntity::isAdult, TreefolkAi::findNearestValidAttackTarget), new StartFertilizing<>(TreefolkEntity::isAdult), StartDancingSometimes.create(DANCE_DURATION, RECENTLY_DANCED_DURATION, DANCE_COOLDOWN), TryFindLand.create(MAX_LOOK_DIST, 1F), createIdleLookBehaviors(), createIdleMovementBehaviors(), SetLookAndInteract.create(EntityType.PLAYER, 4)));
    }
    
    private static void initAdmireItemActivity(Brain<TreefolkEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(Activity.ADMIRE_ITEM, 10, ImmutableList.of(GoToWantedItem.create(TreefolkAi::isNotHoldingLovedItemInOffhand, SPEED_MULTIPLIER_WHEN_GOING_TO_WANTED_ITEM, true, MAX_DISTANCE_TO_WALK_TO_ITEM), new StopAdmiringIfItemTooFarAway<>(MAX_DISTANCE_TO_WALK_TO_ITEM), new StopAdmiringIfTiredOfTryingToReachItem<>(MAX_TIME_TO_WALK_TO_ITEM, HOW_LONG_TIME_TO_DISABLE_ADMIRE_WALKING_IF_CANT_REACH_ITEM)), MemoryModuleType.ADMIRING_ITEM);
    }
    
    private static void initFightActivity(TreefolkEntity entity, Brain<TreefolkEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10, ImmutableList.of(StopAttackingIfTargetInvalid.create(living -> {
            return !isNearestValidAttackTarget(entity, living);
        }), SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(SPEED_MULTIPLIER_WHEN_FIGHTING), MeleeAttack.create(MELEE_ATTACK_COOLDOWN), new LongDistanceRangedAttack<>(RANGED_ATTACK_COOLDOWN, MIN_RANGED_ATTACK_RANGE, MAX_RANGED_ATTACK_RANGE)), MemoryModuleType.ATTACK_TARGET);
    }
    
    private static void initAvoidActivity(Brain<TreefolkEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(Activity.AVOID, 10, ImmutableList.of(SetWalkTargetAwayFrom.entity(MemoryModuleType.AVOID_TARGET, SPEED_MULTIPLIER_WHEN_FLEEING, DESIRED_DISTANCE_FROM_ENTITY_WHEN_AVOIDING, true), createIdleLookBehaviors(), createIdleMovementBehaviors()), MemoryModuleType.AVOID_TARGET);
    }

    private static void initCelebrateActivity(Brain<TreefolkEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(Activity.CELEBRATE, 10, ImmutableList.of(SetEntityLookTarget.create(TreefolkAi::isPlayerHoldingLovedItem, MAX_LOOK_DIST_FOR_PLAYER_HOLDING_LOVED_ITEM), StartAttacking.create(TreefolkEntity::isAdult, TreefolkAi::findNearestValidAttackTarget), BehaviorBuilder.triggerIf(t -> {
            return !t.isDancing();
        }, GoToTargetLocation.create(MemoryModuleType.CELEBRATE_LOCATION, 2, 1.0F)), BehaviorBuilder.triggerIf(TreefolkEntity::isDancing, GoToTargetLocation.create(MemoryModuleType.CELEBRATE_LOCATION, 4, 0.6F)), new RunOne<>(ImmutableList.of(Pair.of(SetEntityLookTarget.create(EntityTypesPM.TREEFOLK.get(), 8.0F), 1), Pair.of(RandomStroll.stroll(SPEED_MULTIPLIER_WHEN_IDLING, 2, 1), 1), Pair.of(new DoNothing(10, 20), 1)))), MemoryModuleType.CELEBRATE_LOCATION);
    }
    
    private static void initWorkActivity(Brain<TreefolkEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(Activity.WORK, 10, ImmutableList.of(SetEntityLookTarget.create(TreefolkAi::isPlayerHoldingLovedItem, MAX_LOOK_DIST_FOR_PLAYER_HOLDING_LOVED_ITEM), StartAttacking.create(TreefolkEntity::isAdult, TreefolkAi::findNearestValidAttackTarget), GoToTargetLocation.create(MemoryModuleTypesPM.FERTILIZE_LOCATION.get(), 2, SPEED_MULTIPLIER_WHEN_WORKING), new StopFertilizingIfTiredOfTryingToReachBlock<>(MAX_TIME_TO_WALK_TO_ITEM, HOW_LONG_TIME_TO_DISABLE_FERTILIZING_IF_CANT_REACH_BLOCK), new Fertilize<>(MAX_FERTILIZE_RANGE, FERTILIZE_COOLDOWN), new RunOne<>(ImmutableList.of(Pair.of(SetEntityLookTarget.create(EntityTypesPM.TREEFOLK.get(), 8.0F), 1), Pair.of(RandomStroll.stroll(SPEED_MULTIPLIER_WHEN_IDLING, 2, 1), 1), Pair.of(new DoNothing(10, 20), 1)))), MemoryModuleTypesPM.FERTILIZE_LOCATION.get());
    }
    
    private static RunOne<TreefolkEntity> createIdleLookBehaviors() {
        return new RunOne<>(ImmutableList.of(Pair.of(SetEntityLookTarget.create(EntityType.PLAYER, MAX_LOOK_DIST), 1), Pair.of(SetEntityLookTarget.create(EntityTypesPM.TREEFOLK.get(), MAX_LOOK_DIST), 1), Pair.of(SetEntityLookTarget.create(MAX_LOOK_DIST), 1), Pair.of(new DoNothing(30, 60), 1)));
    }

    private static RunOne<TreefolkEntity> createIdleMovementBehaviors() {
        return new RunOne<>(ImmutableList.of(Pair.of(RandomStroll.stroll(SPEED_MULTIPLIER_WHEN_IDLING), 2), Pair.of(InteractWith.of(EntityTypesPM.TREEFOLK.get(), INTERACTION_RANGE, MemoryModuleType.INTERACTION_TARGET, SPEED_MULTIPLIER_WHEN_IDLING, 2), 2), Pair.of(BehaviorBuilder.triggerIf(TreefolkAi::doesntSeeAnyPlayerHoldingLovedItem, SetWalkTargetFromLookTarget.create(SPEED_MULTIPLIER_WHEN_IDLING, 3)), 2), Pair.of(new DoNothing(30, 60), 1)));
    }

    public static boolean isPlayerHoldingLovedItem(LivingEntity entity) {
        return entity.getType() == EntityType.PLAYER && entity.isHolding(TreefolkAi::isLovedItem);
    }
    
    public static boolean isLovedItem(ItemStack stack) {
        return stack.is(ItemTagsPM.TREEFOLK_LOVED);
    }
    
    private static Optional<? extends LivingEntity> findNearestValidAttackTarget(TreefolkEntity entity) {
        Brain<TreefolkEntity> brain = entity.getBrain();
        Optional<LivingEntity> angryAtOptional = getAngerTarget(entity);
        if (angryAtOptional.isPresent() && Sensor.isEntityAttackableIgnoringLineOfSight(entity, angryAtOptional.get())) {
            return angryAtOptional;
        } else {
            if (brain.hasMemoryValue(MemoryModuleType.UNIVERSAL_ANGER)) {
                Optional<Player> attackablePlayerOptional = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER);
                if (attackablePlayerOptional.isPresent()) {
                    return attackablePlayerOptional;
                }
            }
            return Optional.empty();
        }
    }
    
    private static boolean isNearestValidAttackTarget(TreefolkEntity entity, LivingEntity target) {
        return findNearestValidAttackTarget(entity).filter(e -> {
            return e == target;
        }).isPresent();
    }

    private static boolean seesPlayerHoldingLovedItem(LivingEntity entity) {
       return entity.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM);
    }

    private static boolean doesntSeeAnyPlayerHoldingLovedItem(LivingEntity entity) {
       return !seesPlayerHoldingLovedItem(entity);
    }
    
    public static boolean wantsToPickup(TreefolkEntity entity, ItemStack stack) {
        if (isAdmiringDisabled(entity) && entity.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET)) {
            return false;
        } else {
            return isLovedItem(stack) && isNotHoldingLovedItemInOffhand(entity);
        }
    }

    private static boolean isNotHoldingLovedItemInOffhand(TreefolkEntity entity) {
        return entity.getOffhandItem().isEmpty() || !isLovedItem(entity.getOffhandItem());
    }

    private static boolean isAdmiringDisabled(TreefolkEntity entity) {
        return entity.getBrain().hasMemoryValue(MemoryModuleType.ADMIRING_DISABLED);
    }

    public static void stopHoldingOffHandItem(TreefolkEntity entity, boolean shouldBarter) {
        ItemStack stack = entity.getItemInHand(InteractionHand.OFF_HAND);
        entity.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
        if (entity.isAdult()) {
            // Adult treefolk barter for bone meal and throw back other stuff
            if (shouldBarter && isLovedItem(stack)) {
                throwItems(entity, getBarterResponseItems(entity));
            } else {
                throwItems(entity, Collections.singletonList(stack));
            }
        } else {
            // Baby treefolk just keep bone meal and throw back other stuff
            if (!isLovedItem(stack)) {
                throwItems(entity, Collections.singletonList(stack));
            }
        }
    }

    private static void throwItems(TreefolkEntity entity, List<ItemStack> stacks) {
        entity.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER).ifPresentOrElse(player -> {
            throwItemsTowardPlayer(entity, player, stacks);
        }, () -> {
            throwItemsTowardRandomPos(entity, stacks);
        });
    }

    private static void throwItemsTowardPlayer(TreefolkEntity entity, Player player, List<ItemStack> stacks) {
        throwItemsTowardsPos(entity, stacks, player.position());
    }

    private static void throwItemsTowardRandomPos(TreefolkEntity entity, List<ItemStack> stacks) {
        throwItemsTowardsPos(entity, stacks, getRandomNearbyPos(entity));
    }

    private static void throwItemsTowardsPos(TreefolkEntity entity, List<ItemStack> stacks, Vec3 position) {
        if (!stacks.isEmpty()) {
            entity.swing(InteractionHand.OFF_HAND);
            for (ItemStack stack : stacks) {
                BehaviorUtils.throwItem(entity, stack, position.add(0D, 1D, 0D));
            }
        }
    }
    
    private static Vec3 getRandomNearbyPos(TreefolkEntity entity) {
        Vec3 vec3 = LandRandomPos.getPos(entity, 4, 2);
        return vec3 == null ? entity.position() : vec3;
    }

    private static List<ItemStack> getBarterResponseItems(TreefolkEntity entity) {
        if (entity.level() instanceof ServerLevel serverLevel) {
            LootTable table = serverLevel.getServer().getLootData().getLootTable(LootTablesPM.TREEFOLK_BARTERING);
            return table.getRandomItems(new LootParams.Builder(serverLevel).withParameter(LootContextParams.THIS_ENTITY, entity).create(LootContextParamSets.PIGLIN_BARTER));
        } else {
            return Collections.emptyList();
        }
    }

    public static void pickUpItem(TreefolkEntity entity, ItemEntity itemEntity) {
        stopWalking(entity);
        entity.take(itemEntity, 1);
        ItemStack stack = removeOneItemFromItemEntity(itemEntity);
        
        if (isLovedItem(stack)) {
            entity.getBrain().eraseMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM);
            holdInOffhand(entity, stack);
            admireLovedItem(entity);
        } else {
            throwItemsTowardRandomPos(entity, Collections.singletonList(stack));
        }
    }

    private static void stopWalking(TreefolkEntity entity) {
        entity.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        entity.getNavigation().stop();
    }

    private static ItemStack removeOneItemFromItemEntity(ItemEntity itemEntity) {
        ItemStack stack = itemEntity.getItem();
        ItemStack splitStack = stack.split(1);
        if (stack.isEmpty()) {
            itemEntity.discard();
        } else {
            itemEntity.setItem(stack);
        }
        return splitStack;
    }

    private static void holdInOffhand(TreefolkEntity entity, ItemStack stack) {
        if (isHoldingItemInOffHand(entity)) {
            entity.spawnAtLocation(entity.getItemInHand(InteractionHand.OFF_HAND));
        }
        entity.holdInOffHand(stack);
    }

    private static boolean isHoldingItemInOffHand(TreefolkEntity entity) {
        return !entity.getOffhandItem().isEmpty();
    }

    private static void admireLovedItem(TreefolkEntity entity) {
        entity.getBrain().setMemoryWithExpiry(MemoryModuleType.ADMIRING_ITEM, true, ADMIRE_DURATION);
    }

    public static void updateActivity(TreefolkEntity entity) {
        Brain<TreefolkEntity> brain = entity.getBrain();
        Activity activityBefore = brain.getActiveNonCoreActivity().orElse(null);
        brain.setActiveActivityToFirstValid(ImmutableList.of(Activity.ADMIRE_ITEM, Activity.FIGHT, Activity.AVOID, Activity.CELEBRATE, Activity.WORK, Activity.IDLE));
        Activity activityAfter = brain.getActiveNonCoreActivity().orElse(null);
        if (activityBefore != activityAfter) {
            // TODO Play activity transition sound
        }
        
        entity.setAggressive(brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));

        if (!brain.hasMemoryValue(MemoryModuleType.CELEBRATE_LOCATION)) {
            brain.eraseMemory(MemoryModuleType.DANCING);
        }

        entity.setDancing(brain.hasMemoryValue(MemoryModuleType.DANCING));
    }

    public static void wasHurtBy(TreefolkEntity entity, LivingEntity target) {
        if (!(target instanceof TreefolkEntity)) {
            if (isHoldingItemInOffHand(entity)) {
                stopHoldingOffHandItem(entity, false);
            }
            
            Brain<TreefolkEntity> brain = entity.getBrain();
            brain.eraseMemory(MemoryModuleType.CELEBRATE_LOCATION);
            brain.eraseMemory(MemoryModuleType.DANCING);
            brain.eraseMemory(MemoryModuleType.ADMIRING_ITEM);
            if (target instanceof Player) {
                brain.setMemoryWithExpiry(MemoryModuleType.ADMIRING_DISABLED, true, HIT_BY_PLAYER_MEMORY_TIMEOUT);
            }
            
            getAvoidTarget(entity).ifPresent(avoid -> {
                if (avoid.getType() != target.getType()) {
                    brain.eraseMemory(MemoryModuleType.AVOID_TARGET);
                }
            });
            if (entity.isBaby()) {
                brain.setMemoryWithExpiry(MemoryModuleType.AVOID_TARGET, target, BABY_FLEE_DURATION_AFTER_GETTING_HIT);
                if (Sensor.isEntityAttackableIgnoringLineOfSight(entity, target)) {
                    broadcastAngerTarget(entity, target);
                }
            } else {
                maybeRetaliate(entity, target);
            }
        }
    }

    private static Optional<LivingEntity> getAvoidTarget(TreefolkEntity entity) {
        return entity.getBrain().hasMemoryValue(MemoryModuleType.AVOID_TARGET) ? entity.getBrain().getMemory(MemoryModuleType.AVOID_TARGET) : Optional.empty();
    }

    private static void maybeRetaliate(TreefolkEntity entity, LivingEntity target) {
        if (!entity.getBrain().isActive(Activity.AVOID)) {
            if (Sensor.isEntityAttackableIgnoringLineOfSight(entity, target)) {
                if (!BehaviorUtils.isOtherTargetMuchFurtherAwayThanCurrentAttackTarget(entity, target, 4D)) {
                    if (target.getType() == EntityType.PLAYER && entity.level().getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER)) {
                        setAngerTargetToNearestTargetablePlayerIfFound(entity, target);
                        broadcastUniversalAnger(entity);
                    } else {
                        setAngerTarget(entity, target);
                        broadcastAngerTarget(entity, target);
                    }
                }
            }
        }
    }

    private static void setAngerTargetToNearestTargetablePlayerIfFound(TreefolkEntity entity, LivingEntity currentTarget) {
        getNearestVisibleTargetablePlayer(entity).ifPresentOrElse(player -> {
            setAngerTarget(entity, player);
        }, () -> {
            setAngerTarget(entity, currentTarget);
        });
    }

    public static Optional<Player> getNearestVisibleTargetablePlayer(TreefolkEntity entity) {
        return entity.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER) ? entity.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER) : Optional.empty();
    }

    private static void setAngerTarget(TreefolkEntity entity, LivingEntity target) {
        Level level = entity.level();
        if (Sensor.isEntityAttackableIgnoringLineOfSight(entity, target)) {
            int angerDuration = ANGER_DURATION.sample(level.random);
            entity.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
            entity.getBrain().setMemoryWithExpiry(MemoryModuleType.ANGRY_AT, target.getUUID(), angerDuration);
            if (target.getType() == EntityType.PLAYER && level.getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER)) {
                entity.getBrain().setMemoryWithExpiry(MemoryModuleType.UNIVERSAL_ANGER, true, angerDuration);
            }
        }
    }

    private static void broadcastUniversalAnger(TreefolkEntity entity) {
        getNearbyAdultTreefolk(entity).forEach(t -> {
            getNearestVisibleTargetablePlayer(t).ifPresent(p -> {
                setAngerTarget(t, p);
            });
        });
    }

    private static void broadcastAngerTarget(TreefolkEntity entity, LivingEntity target) {
        getNearbyAdultTreefolk(entity).forEach(t -> {
            setAngerTargetIfCloserThanCurrent(t, target);
        });
    }
    
    private static List<TreefolkEntity> getNearbyTreefolk(TreefolkEntity entity) {
        return entity.getBrain().getMemory(MemoryModuleTypesPM.NEARBY_TREEFOLK.get()).orElse(ImmutableList.of());
    }

    private static List<TreefolkEntity> getNearbyAdultTreefolk(TreefolkEntity entity) {
        return entity.getBrain().getMemory(MemoryModuleTypesPM.NEARBY_ADULT_TREEFOLK.get()).orElse(ImmutableList.of());
    }
    
    private static void setAngerTargetIfCloserThanCurrent(TreefolkEntity entity, LivingEntity currentTarget) {
        Optional<LivingEntity> livingOpt = getAngerTarget(entity);
        LivingEntity nearestTarget = BehaviorUtils.getNearestTarget(entity, livingOpt, currentTarget);
        if (!livingOpt.isPresent() || livingOpt.get() != nearestTarget) {
            setAngerTarget(entity, nearestTarget);
        }
    }

    private static Optional<LivingEntity> getAngerTarget(TreefolkEntity entity) {
        return BehaviorUtils.getLivingEntityFromUUIDMemory(entity, MemoryModuleType.ANGRY_AT);
    }
    
    public static void startDanceParty(TreefolkEntity entity, int duration, int cooldown) {
        entity.getBrain().setMemoryWithExpiry(MemoryModuleType.DANCING, true, duration);
        entity.getBrain().setMemoryWithExpiry(MemoryModuleType.CELEBRATE_LOCATION, entity.blockPosition(), duration);
        entity.getBrain().setMemoryWithExpiry(MemoryModuleTypesPM.DANCED_RECENTLY.get(), true, cooldown);
        TreefolkAi.broadcastCelebrateLocation(entity, duration);
    }
    
    public static void broadcastCelebrateLocation(TreefolkEntity entity, int danceDuration) {
        entity.getBrain().getMemory(MemoryModuleType.CELEBRATE_LOCATION).ifPresent(pos -> {
            getNearbyTreefolk(entity).forEach(t -> {
                t.getBrain().setMemoryWithExpiry(MemoryModuleType.CELEBRATE_LOCATION, pos, danceDuration);
            });
        });
    }
    
    public static InteractionResult mobInteract(TreefolkEntity entity, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (isLovedItem(stack)) {
            ItemStack splitStack = stack.split(1);
            holdInOffhand(entity, splitStack);
            admireLovedItem(entity);
            stopWalking(entity);
            return InteractionResult.CONSUME;
        } else {
            return InteractionResult.PASS;
        }
    }
    
    public static boolean canAdmire(TreefolkEntity entity, ItemStack stack) {
        return !isAdmiringDisabled(entity) && !isAdmiringItem(entity) && entity.isAdult() && isLovedItem(stack);
    }

    private static boolean isAdmiringItem(TreefolkEntity entity) {
        return entity.getBrain().hasMemoryValue(MemoryModuleType.ADMIRING_ITEM);
    }
}
