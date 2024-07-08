package com.verdantartifice.primalmagick.common.advancements.critereon;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Advancement criterion that is triggered when the player runescribes an enchantment onto an item.
 * 
 * @author Daedalus4096
 */
public class RunescribingTrigger extends SimpleCriterionTrigger<RunescribingTrigger.TriggerInstance> {
    public Codec<RunescribingTrigger.TriggerInstance> codec() {
        return RunescribingTrigger.TriggerInstance.codec();
    }
    
    public void trigger(ServerPlayer player, Enchantment ench) {
        this.trigger(player, triggerInstance -> triggerInstance.matches(ench));
    }
    
    public static record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ResourceLocation> enchantmentIdOpt) implements SimpleCriterionTrigger.SimpleInstance {
        public static Codec<RunescribingTrigger.TriggerInstance> codec() {
            return RecordCodecBuilder.create(instance -> instance.group(
                    EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(RunescribingTrigger.TriggerInstance::player), 
                    ResourceLocation.CODEC.optionalFieldOf("enchantmentId").forGetter(RunescribingTrigger.TriggerInstance::enchantmentIdOpt)
                ).apply(instance, RunescribingTrigger.TriggerInstance::new));
        }
        
        public static Criterion<RunescribingTrigger.TriggerInstance> enchantment(Enchantment ench) {
            Optional<ResourceLocation> enchIdOpt = Optional.ofNullable(ForgeRegistries.ENCHANTMENTS.getKey(ench));
            return CriteriaTriggersPM.RUNESCRIBING.createCriterion(new RunescribingTrigger.TriggerInstance(Optional.empty(), enchIdOpt));
        }
        
        public boolean matches(Enchantment other) {
            Optional<ResourceLocation> otherIdOpt = Optional.ofNullable(ForgeRegistries.ENCHANTMENTS.getKey(other));
            return this.enchantmentIdOpt.isPresent() && this.enchantmentIdOpt.equals(otherIdOpt);
        }

        public Optional<ContextAwarePredicate> player() {
           return this.player;
        }
    }
}
