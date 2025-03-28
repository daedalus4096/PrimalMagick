package com.verdantartifice.primalmagick.common.rituals.steps;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.tiles.rituals.RitualAltarTileEntity;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

/**
 * Class identifying a single step in a ritual's process.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractRitualStep<T extends AbstractRitualStep<T>> {
    public static Codec<AbstractRitualStep<?>> dispatchCodec() {
        return Services.RITUAL_STEP_TYPES_REGISTRY.codec().dispatch("topic_type", AbstractRitualStep::getType, RitualStepType::codec);
    }
    
    public static StreamCodec<RegistryFriendlyByteBuf, AbstractRitualStep<?>> dispatchStreamCodec() {
        return Services.RITUAL_STEP_TYPES_REGISTRY.registryFriendlyStreamCodec().dispatch(AbstractRitualStep::getType, RitualStepType::streamCodec);
    }
    
    public abstract boolean isValid();
    public abstract RitualStepType<T> getType();
    
    @SuppressWarnings("unchecked")
    public boolean execute(RitualAltarTileEntity altar) {
        return this.getType().action().apply(altar, (T)this);
    }
}
