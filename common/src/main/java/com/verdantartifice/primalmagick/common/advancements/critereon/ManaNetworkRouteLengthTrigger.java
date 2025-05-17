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
 * Advancement criteria that is triggered when the player creates a mana network route of at least a certain length.
 *
 * @author Daedalus4096
 */
public class ManaNetworkRouteLengthTrigger extends SimpleCriterionTrigger<ManaNetworkRouteLengthTrigger.TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return ManaNetworkRouteLengthTrigger.TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, double dist) {
        this.trigger(player, triggerInstance -> triggerInstance.matches(dist));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, double threshold) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<ManaNetworkRouteLengthTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(ManaNetworkRouteLengthTrigger.TriggerInstance::player),
                Codec.DOUBLE.fieldOf("threshold").forGetter(ManaNetworkRouteLengthTrigger.TriggerInstance::threshold)
            ).apply(instance, ManaNetworkRouteLengthTrigger.TriggerInstance::new));

        public static Criterion<ManaNetworkRouteLengthTrigger.TriggerInstance> atLeast(int threshold) {
            return CriteriaTriggersPM.MANA_NETWORK_ROUTE_LENGTH.get().createCriterion(new ManaNetworkRouteLengthTrigger.TriggerInstance(Optional.empty(), threshold));
        }

        public boolean matches(double value) {
            return value >= this.threshold;
        }

        public Optional<ContextAwarePredicate> player() {
            return this.player;
        }
    }
}
