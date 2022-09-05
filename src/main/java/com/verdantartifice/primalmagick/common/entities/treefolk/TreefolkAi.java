package com.verdantartifice.primalmagick.common.entities.treefolk;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.DoNothing;
import net.minecraft.world.entity.ai.behavior.GoToWantedItem;
import net.minecraft.world.entity.ai.behavior.InteractWith;
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
import net.minecraft.world.entity.ai.behavior.RandomStroll;
import net.minecraft.world.entity.ai.behavior.RunIf;
import net.minecraft.world.entity.ai.behavior.RunOne;
import net.minecraft.world.entity.ai.behavior.SetEntityLookTarget;
import net.minecraft.world.entity.ai.behavior.SetLookAndInteract;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
import net.minecraft.world.entity.ai.behavior.StartAttacking;
import net.minecraft.world.entity.ai.behavior.StopBeingAngryIfTargetDead;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

/**
 * Collection of static methods to help with Treefolk entity AI behaviors.
 * 
 * @author Daedalus4096
 */
public class TreefolkAi {
    private static final int ADMIRE_DURATION = 120;
    private static final int MAX_DISTANCE_TO_WALK_TO_ITEM = 9;
    private static final int MAX_TIME_TO_WALK_TO_ITEM = 200;
    private static final int HOW_LONG_TIME_TO_DISABLE_ADMIRE_WALKING_IF_CANT_REACH_ITEM = 200;
    private static final int MAX_LOOK_DIST = 8;
    private static final int MAX_LOOK_DIST_FOR_PLAYER_HOLDING_LOVED_ITEM = 14;
    private static final int INTERACTION_RANGE = 8;
    private static final float SPEED_MULTIPLIER_WHEN_GOING_TO_WANTED_ITEM = 1.0F;
    private static final float SPEED_MULTIPLIER_WHEN_IDLING = 0.6F;

    protected static Brain<?> makeBrain(TreefolkEntity entity, Brain<TreefolkEntity> brain) {
        initCoreActivity(brain);
        initIdleActivity(brain);
        initAdmireItemActivity(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }
    
    private static void initCoreActivity(Brain<TreefolkEntity> brain) {
        brain.addActivity(Activity.CORE, 0, ImmutableList.of(new LookAtTargetSink(45, 90), new MoveToTargetSink(), new StopHoldingItemIfNoLongerAdmiring<>(), new StartAdmiringItemIfSeen<>(ADMIRE_DURATION), new StopBeingAngryIfTargetDead<>()));
    }
    
    private static void initIdleActivity(Brain<TreefolkEntity> brain) {
        brain.addActivity(Activity.IDLE, 10, ImmutableList.of(new SetEntityLookTarget(TreefolkAi::isPlayerHoldingLovedItem, MAX_LOOK_DIST_FOR_PLAYER_HOLDING_LOVED_ITEM), new StartAttacking<>(TreefolkEntity::isAdult, TreefolkAi::findNearestValidAttackTarget), createIdleLookBehaviors(), createIdleMovementBehaviors(), new SetLookAndInteract(EntityType.PLAYER, 4)));
    }
    
    private static void initAdmireItemActivity(Brain<TreefolkEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(Activity.ADMIRE_ITEM, 10, ImmutableList.of(new GoToWantedItem<>(TreefolkAi::isNotHoldingLovedItemInOffHand, SPEED_MULTIPLIER_WHEN_GOING_TO_WANTED_ITEM, true, MAX_DISTANCE_TO_WALK_TO_ITEM), new StopAdmiringIfItemTooFarAway<>(MAX_DISTANCE_TO_WALK_TO_ITEM), new StopAdmiringIfTiredOfTryingToReachItem<>(MAX_TIME_TO_WALK_TO_ITEM, HOW_LONG_TIME_TO_DISABLE_ADMIRE_WALKING_IF_CANT_REACH_ITEM)), MemoryModuleType.ADMIRING_ITEM);
    }
    
    private static RunOne<TreefolkEntity> createIdleLookBehaviors() {
        return new RunOne<>(ImmutableList.of(Pair.of(new SetEntityLookTarget(EntityType.PLAYER, MAX_LOOK_DIST), 1), Pair.of(new SetEntityLookTarget(EntityTypesPM.TREEFOLK.get(), MAX_LOOK_DIST), 1), Pair.of(new SetEntityLookTarget(MAX_LOOK_DIST), 1), Pair.of(new DoNothing(30, 60), 1)));
    }

    private static RunOne<TreefolkEntity> createIdleMovementBehaviors() {
        return new RunOne<>(ImmutableList.of(Pair.of(new RandomStroll(SPEED_MULTIPLIER_WHEN_IDLING), 2), Pair.of(InteractWith.of(EntityTypesPM.TREEFOLK.get(), INTERACTION_RANGE, MemoryModuleType.INTERACTION_TARGET, SPEED_MULTIPLIER_WHEN_IDLING, 2), 2), Pair.of(new RunIf<>(TreefolkAi::doesntSeeAnyPlayerHoldingLovedItem, new SetWalkTargetFromLookTarget(SPEED_MULTIPLIER_WHEN_IDLING, 3)), 2), Pair.of(new DoNothing(30, 60), 1)));
    }

    public static boolean isPlayerHoldingLovedItem(LivingEntity entity) {
        return entity.getType() == EntityType.PLAYER && entity.isHolding(TreefolkAi::isLovedItem);
    }
    
    public static boolean isLovedItem(ItemStack stack) {
        return stack.is(ItemTagsPM.TREEFOLK_LOVED);
    }
    
    private static Optional<? extends LivingEntity> findNearestValidAttackTarget(TreefolkEntity entity) {
        Brain<TreefolkEntity> brain = entity.getBrain();
        Optional<LivingEntity> angryAtOptional = BehaviorUtils.getLivingEntityFromUUIDMemory(entity, MemoryModuleType.ANGRY_AT);
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

    private static boolean seesPlayerHoldingLovedItem(LivingEntity entity) {
       return entity.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM);
    }

    private static boolean doesntSeeAnyPlayerHoldingLovedItem(LivingEntity entity) {
       return !seesPlayerHoldingLovedItem(entity);
    }
    
    protected static boolean isNotHoldingLovedItemInOffHand(TreefolkEntity entity) {
        return entity.getOffhandItem().isEmpty() || !isLovedItem(entity.getOffhandItem());
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
        if (shouldBarter && isLovedItem(stack)) {
            throwItems(entity, getBarterResponseItems(entity));
        } else {
            throwItems(entity, Collections.singletonList(stack));
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
        // TODO Stub
        return Collections.singletonList(new ItemStack(Items.MANGROVE_PROPAGULE));
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
}
