package com.verdantartifice.primalmagick.common.theorycrafting.weights;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;

/**
 * Abstract base class intended for use in calculating weights for theorycrafting projects.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractWeightFunction<T extends AbstractWeightFunction<T>> {
    public static Codec<AbstractWeightFunction<?>> dispatchCodec() {
        return Services.WEIGHT_FUNCTION_TYPES_REGISTRY.codec().dispatch("reward_type", AbstractWeightFunction::getType, WeightFunctionType::codec);
    }
    
    public static StreamCodec<RegistryFriendlyByteBuf, AbstractWeightFunction<?>> dispatchStreamCodec() {
        return Services.WEIGHT_FUNCTION_TYPES_REGISTRY.registryFriendlyStreamCodec().dispatch(AbstractWeightFunction::getType, WeightFunctionType::streamCodec);
    }
    
    public abstract double getWeight(Player player);
    
    protected abstract WeightFunctionType<T> getType();
    
    public static AbstractWeightFunction<?> fromNetwork(RegistryFriendlyByteBuf buf) {
        return AbstractWeightFunction.dispatchStreamCodec().decode(buf);
    }
    
    public void toNetwork(RegistryFriendlyByteBuf buf) {
        AbstractWeightFunction.dispatchStreamCodec().encode(buf, this);
    }
}
