package com.verdantartifice.primalmagick.common.rituals.steps;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.registries.RegistryCodecs;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

/**
 * Class identifying a single step in a ritual's process.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractRitualStep<T extends AbstractRitualStep<T>> {
    public static Codec<AbstractRitualStep<?>> dispatchCodec() {
        return RegistryCodecs.codec(RitualStepTypesPM.TYPES).dispatch("topic_type", AbstractRitualStep::getType, RitualStepType::codec);
    }
    
    public static StreamCodec<RegistryFriendlyByteBuf, AbstractRitualStep<?>> dispatchStreamCodec() {
        return RegistryCodecs.streamCodec(RitualStepTypesPM.TYPES).dispatch(AbstractRitualStep::getType, RitualStepType::streamCodec);
    }
    
    public abstract boolean isValid();
    public abstract RitualStepType<T> getType();
}
