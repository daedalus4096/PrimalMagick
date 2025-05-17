package com.verdantartifice.primalmagick.common.advancements.critereon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

/**
 * Advancement criteria that is triggered when the player siphons at least a certain amount of mana through a mana
 * network.
 *
 * @author Daedalus4096
 */
public class ManaNetworkSiphonTrigger extends SimpleCriterionTrigger<ManaNetworkSiphonTrigger.TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return ManaNetworkSiphonTrigger.TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, int value) {
        this.trigger(player, triggerInstance -> triggerInstance.matches(value));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, int threshold) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<ManaNetworkSiphonTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(ManaNetworkSiphonTrigger.TriggerInstance::player),
                Codec.INT.fieldOf("threshold").forGetter(ManaNetworkSiphonTrigger.TriggerInstance::threshold)
        ).apply(instance, ManaNetworkSiphonTrigger.TriggerInstance::new));

        public static Criterion<ManaNetworkSiphonTrigger.TriggerInstance> atLeast(int threshold) {
            return CriteriaTriggersPM.MANA_NETWORK_SIPHON.get().createCriterion(new ManaNetworkSiphonTrigger.TriggerInstance(Optional.empty(), threshold));
        }

        public boolean matches(int value) {
            return value >= this.threshold;
        }

        public Optional<ContextAwarePredicate> player() {
            return this.player;
        }
    }
}
