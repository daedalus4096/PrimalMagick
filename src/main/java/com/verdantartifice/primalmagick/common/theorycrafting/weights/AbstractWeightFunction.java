package com.verdantartifice.primalmagick.common.theorycrafting.weights;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.registries.RegistryCodecs;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

/**
 * Abstract base class intended for use in calculating weights for theorycrafting projects.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractWeightFunction<T extends AbstractWeightFunction<T>> {
    public static Codec<AbstractWeightFunction<?>> dispatchCodec() {
        return RegistryCodecs.codec(WeightFunctionTypesPM.TYPES).dispatch("reward_type", AbstractWeightFunction::getType, WeightFunctionType::codec);
    }
    
    public abstract double getWeight(Player player);
    
    protected abstract WeightFunctionType<T> getType();
    
    public static AbstractWeightFunction<?> fromNetwork(FriendlyByteBuf buf) {
        ResourceLocation typeId = buf.readResourceLocation();
        return WeightFunctionTypesPM.TYPES.get().getValue(typeId).networkReader().apply(buf);
    }
    
    public void toNetwork(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.getType().id());
        this.toNetworkInner(buf);
    }
    
    protected abstract void toNetworkInner(FriendlyByteBuf buf);
}
