package com.verdantartifice.primalmagick.common.rewards;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;

/**
 * Base class for a tangible reward provided by knowledge work (e.g. theorycrafting project, research stage) upon
 * successful completion.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractReward<T extends AbstractReward<T>> {
    public static Codec<AbstractReward<?>> dispatchCodec() {
        return Services.REWARD_TYPES_REGISTRY.codec().dispatch("reward_type", AbstractReward::getType, RewardType::codec);
    }
    
    public static StreamCodec<RegistryFriendlyByteBuf, AbstractReward<?>> dispatchStreamCodec() {
        return Services.REWARD_TYPES_REGISTRY.registryFriendlyStreamCodec().dispatch(AbstractReward::getType, RewardType::streamCodec);
    }
    
    public abstract void grant(ServerPlayer player);
    
    public abstract Component getDescription();
    
    protected abstract RewardType<T> getType();

    public static AbstractReward<?> fromNetwork(RegistryFriendlyByteBuf buf) {
        return AbstractReward.dispatchStreamCodec().decode(buf);
    }
    
    public void toNetwork(RegistryFriendlyByteBuf buf) {
        AbstractReward.dispatchStreamCodec().encode(buf, this);
    }

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object obj);
}
