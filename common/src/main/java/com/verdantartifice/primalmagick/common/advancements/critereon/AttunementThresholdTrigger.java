package com.verdantartifice.primalmagick.common.advancements.critereon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.attunements.AttunementThreshold;
import com.verdantartifice.primalmagick.common.sources.Source;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

/**
 * Advancement criterion that is triggered when the player reaches a given threshold of attunement, either lasting
 * (i.e. permanent and induced only) or ephemeral (i.e. total attunement), optionally for a specific source.
 * 
 * @author Daedalus4096
 */
public class AttunementThresholdTrigger extends SimpleCriterionTrigger<AttunementThresholdTrigger.TriggerInstance> {
    public Codec<AttunementThresholdTrigger.TriggerInstance> codec() {
        return AttunementThresholdTrigger.TriggerInstance.CODEC;
    }
    
    public void trigger(ServerPlayer player, Source source, int p, int i, int t) {
        this.trigger(player, triggerInstance -> triggerInstance.matches(source, p, i, t));
    }
    
    public static record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<Source> source, Optional<AttunementThreshold> lasting, Optional<AttunementThreshold> ephemeral) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<AttunementThresholdTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(AttunementThresholdTrigger.TriggerInstance::player),
                Source.CODEC.optionalFieldOf("source").forGetter(AttunementThresholdTrigger.TriggerInstance::source),
                AttunementThreshold.CODEC.optionalFieldOf("lasting").forGetter(AttunementThresholdTrigger.TriggerInstance::lasting),
                AttunementThreshold.CODEC.optionalFieldOf("ephemeral").forGetter(AttunementThresholdTrigger.TriggerInstance::ephemeral)
            ).apply(instance, AttunementThresholdTrigger.TriggerInstance::new));
        
        public static Criterion<AttunementThresholdTrigger.TriggerInstance> anyLasting(AttunementThreshold threshold) {
            return CriteriaTriggersPM.ATTUNEMENT_THRESHOLD.get().createCriterion(new AttunementThresholdTrigger.TriggerInstance(Optional.empty(), Optional.empty(), Optional.of(threshold), Optional.empty()));
        }
        
        public static Criterion<AttunementThresholdTrigger.TriggerInstance> anyEphemeral(AttunementThreshold threshold) {
            return CriteriaTriggersPM.ATTUNEMENT_THRESHOLD.get().createCriterion(new AttunementThresholdTrigger.TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(threshold)));
        }
        
        public static Criterion<AttunementThresholdTrigger.TriggerInstance> lasting(Source source, AttunementThreshold threshold) {
            return CriteriaTriggersPM.ATTUNEMENT_THRESHOLD.get().createCriterion(new AttunementThresholdTrigger.TriggerInstance(Optional.empty(), Optional.of(source), Optional.of(threshold), Optional.empty()));
        }
        
        public static Criterion<AttunementThresholdTrigger.TriggerInstance> ephemeral(Source source, AttunementThreshold threshold) {
            return CriteriaTriggersPM.ATTUNEMENT_THRESHOLD.get().createCriterion(new AttunementThresholdTrigger.TriggerInstance(Optional.empty(), Optional.of(source), Optional.empty(), Optional.of(threshold)));
        }
        
        public boolean matches(Source source, int permanent, int induced, int temporary) {
            if (this.source.isPresent() && !this.source.get().equals(source)) {
                return false;
            } else {
                if (this.lasting.isPresent()) {
                    return (permanent + induced) >= this.lasting.get().getValue();
                } else if (this.ephemeral.isPresent()) {
                    return (permanent + induced + temporary) >= this.ephemeral.get().getValue();
                } else {
                    return true;
                }
            }
        }

        public Optional<ContextAwarePredicate> player() {
           return this.player;
        }
    }
}
