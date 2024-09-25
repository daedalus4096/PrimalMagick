package com.verdantartifice.primalmagick.common.advancements.critereon;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.CriterionValidator;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;

/**
 * Advancement criterion that is triggered when the player scans a given world location with a
 * device such as the arcanometer.
 * 
 * @author Daedalus4096
 */
public class ScanLocationTrigger extends SimpleCriterionTrigger<ScanLocationTrigger.TriggerInstance> {
    public Codec<ScanLocationTrigger.TriggerInstance> codec() {
        return ScanLocationTrigger.TriggerInstance.CODEC;
    }
    
    public void trigger(ServerPlayer pPlayer, BlockPos pPos, ItemStack pStack) {
        ServerLevel serverLevel = pPlayer.serverLevel();
        BlockState blockState = serverLevel.getBlockState(pPos);
        LootParams lootParams = (new LootParams.Builder(serverLevel)).withParameter(LootContextParams.ORIGIN, pPos.getCenter()).withParameter(LootContextParams.THIS_ENTITY, pPlayer).withParameter(LootContextParams.BLOCK_STATE, blockState).withParameter(LootContextParams.TOOL, pStack).create(LootContextParamSets.ADVANCEMENT_LOCATION);
        LootContext lootContext = (new LootContext.Builder(lootParams)).create(Optional.empty());
        this.trigger(pPlayer, triggerInstance -> triggerInstance.matches(lootContext));
    }
    
    public static record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> location) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<ScanLocationTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> {
            return instance.group(
                    EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(ScanLocationTrigger.TriggerInstance::player), 
                    ContextAwarePredicate.CODEC.optionalFieldOf("location").forGetter(ScanLocationTrigger.TriggerInstance::location)
                ).apply(instance, ScanLocationTrigger.TriggerInstance::new);
        });

        private static ScanLocationTrigger.TriggerInstance itemUsedOnLocation(LocationPredicate.Builder pLocation, ItemPredicate.Builder pTool) {
            ContextAwarePredicate predicate = ContextAwarePredicate.create(LocationCheck.checkLocation(pLocation).build(), MatchTool.toolMatches(pTool).build());
            return new ScanLocationTrigger.TriggerInstance(Optional.empty(), Optional.of(predicate));
        }

        public static Criterion<ScanLocationTrigger.TriggerInstance> itemUsedOnBlock(LocationPredicate.Builder pLocation, ItemPredicate.Builder pTool) {
            return CriteriaTriggersPM.SCAN_LOCATION.createCriterion(itemUsedOnLocation(pLocation, pTool));
        }

        public boolean matches(LootContext pContext) {
            return this.location.isEmpty() || this.location.get().matches(pContext);
        }

        public void validate(CriterionValidator pValidator) {
            SimpleCriterionTrigger.SimpleInstance.super.validate(pValidator);
            this.location.ifPresent(loc -> {
                pValidator.validate(loc, LootContextParamSets.ADVANCEMENT_LOCATION, ".location");
            });
        }

        public Optional<ContextAwarePredicate> player() {
            return this.player;
        }
    }
}
