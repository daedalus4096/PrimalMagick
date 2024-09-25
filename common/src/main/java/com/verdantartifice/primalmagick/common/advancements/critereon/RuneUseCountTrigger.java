package com.verdantartifice.primalmagick.common.advancements.critereon;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.runes.Rune;

import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

/**
 * Advancement criterion that is triggered when the player uses a rune more than once in a runescribe.
 * 
 * @author Daedalus4096
 */
public class RuneUseCountTrigger extends SimpleCriterionTrigger<RuneUseCountTrigger.TriggerInstance> {
    public Codec<RuneUseCountTrigger.TriggerInstance> codec() {
        return RuneUseCountTrigger.TriggerInstance.CODEC;
    }
    
    public void trigger(ServerPlayer player, Rune rune, int value) {
        this.trigger(player, triggerInstance -> triggerInstance.matches(rune, value));
    }
    
    public static record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<Rune> runeOpt, int threshold) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<RuneUseCountTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(RuneUseCountTrigger.TriggerInstance::player), 
                Rune.CODEC.optionalFieldOf("rune").forGetter(RuneUseCountTrigger.TriggerInstance::runeOpt),
                Codec.INT.fieldOf("threshold").forGetter(RuneUseCountTrigger.TriggerInstance::threshold)
            ).apply(instance, RuneUseCountTrigger.TriggerInstance::new));
        
        public static Criterion<RuneUseCountTrigger.TriggerInstance> atLeast(int threshold) {
            return CriteriaTriggersPM.RUNE_USE_COUNT.createCriterion(new RuneUseCountTrigger.TriggerInstance(Optional.empty(), Optional.empty(), threshold));
        }
        
        public static Criterion<RuneUseCountTrigger.TriggerInstance> atLeast(Rune rune, int threshold) {
            return CriteriaTriggersPM.RUNE_USE_COUNT.createCriterion(new RuneUseCountTrigger.TriggerInstance(Optional.empty(), Optional.of(rune), threshold));
        }
        
        public boolean matches(Rune rune, int value) {
            if (this.runeOpt.isPresent() && !this.runeOpt.get().getId().equals(rune.getId())) {
                return false;
            } else {
                return value >= this.threshold;
            }
        }

        public Optional<ContextAwarePredicate> player() {
           return this.player;
        }
    }
}
