package com.verdantartifice.primalmagick.common.entities.treefolk;

import java.util.Optional;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;

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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;

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

    public static void stopHoldingOffHandItem(TreefolkEntity entity, boolean shouldBarter) {
        // TODO Auto-generated method stub
        
    }
}
