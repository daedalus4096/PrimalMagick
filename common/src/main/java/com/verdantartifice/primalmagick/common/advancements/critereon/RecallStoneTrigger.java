package com.verdantartifice.primalmagick.common.advancements.critereon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.Optional;

/**
 * Advancement criterion that is triggered when the player uses a recall stone, possibly testing if the
 * stone was used in a particular dimension.
 * 
 * @author Daedalus4096
 */
public class RecallStoneTrigger extends SimpleCriterionTrigger<RecallStoneTrigger.TriggerInstance> {
    public Codec<RecallStoneTrigger.TriggerInstance> codec() {
        return RecallStoneTrigger.TriggerInstance.CODEC;
    }
    
    public void trigger(ServerPlayer player, ResourceKey<Level> dim) {
        this.trigger(player, triggerInstance -> triggerInstance.matches(dim));
    }
    
    public static record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ResourceKey<Level>> dimension) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<RecallStoneTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(RecallStoneTrigger.TriggerInstance::player), 
                ResourceKey.codec(Registries.DIMENSION).optionalFieldOf("dimension").forGetter(RecallStoneTrigger.TriggerInstance::dimension)
            ).apply(instance, RecallStoneTrigger.TriggerInstance::new));
        
        public static Criterion<RecallStoneTrigger.TriggerInstance> anywhere() {
            return CriteriaTriggersPM.RECALL_STONE.get().createCriterion(new RecallStoneTrigger.TriggerInstance(Optional.empty(), Optional.empty()));
        }
        
        public static Criterion<RecallStoneTrigger.TriggerInstance> inDimension(ResourceKey<Level> dimension) {
            return CriteriaTriggersPM.RECALL_STONE.get().createCriterion(new RecallStoneTrigger.TriggerInstance(Optional.empty(), Optional.ofNullable(dimension)));
        }
        
        public boolean matches(ResourceKey<Level> dim) {
            return this.dimension.isEmpty() || this.dimension.get().equals(dim);
        }

        public Optional<ContextAwarePredicate> player() {
           return this.player;
        }
    }
}
