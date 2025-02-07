package com.verdantartifice.primalmagick.common.advancements.critereon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Optional;

/**
 * Advancement criterion that is triggered when the player runescribes an enchantment onto an item.
 * 
 * @author Daedalus4096
 */
public class RunescribingTrigger extends SimpleCriterionTrigger<RunescribingTrigger.TriggerInstance> {
    public Codec<RunescribingTrigger.TriggerInstance> codec() {
        return RunescribingTrigger.TriggerInstance.codec();
    }
    
    public void trigger(ServerPlayer player, Holder<Enchantment> ench) {
        this.trigger(player, triggerInstance -> triggerInstance.matches(ench));
    }
    
    public static record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<Holder<Enchantment>> enchantmentIdOpt) implements SimpleCriterionTrigger.SimpleInstance {
        public static Codec<RunescribingTrigger.TriggerInstance> codec() {
            return RecordCodecBuilder.create(instance -> instance.group(
                    EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(RunescribingTrigger.TriggerInstance::player), 
                    Enchantment.CODEC.optionalFieldOf("enchantment").forGetter(RunescribingTrigger.TriggerInstance::enchantmentIdOpt)
                ).apply(instance, RunescribingTrigger.TriggerInstance::new));
        }
        
        public static Criterion<RunescribingTrigger.TriggerInstance> enchantment(Holder<Enchantment> ench) {
            return CriteriaTriggersPM.RUNESCRIBING.get().createCriterion(new RunescribingTrigger.TriggerInstance(Optional.empty(), Optional.of(ench)));
        }
        
        public boolean matches(Holder<Enchantment> other) {
            return this.enchantmentIdOpt.isPresent() && this.enchantmentIdOpt.get().equals(other);
        }

        public Optional<ContextAwarePredicate> player() {
           return this.player;
        }
    }
}
