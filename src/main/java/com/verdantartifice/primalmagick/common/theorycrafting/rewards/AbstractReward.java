package com.verdantartifice.primalmagick.common.theorycrafting.rewards;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.registries.RegistryCodecs;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;

/**
 * Base class for a non-theory reward provided by a theorycrafting research project upon successful completion.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractReward<T extends AbstractReward<T>> {
    public static Codec<AbstractReward<?>> dispatchCodec() {
        return RegistryCodecs.codec(RewardTypesPM.TYPES).dispatch("reward_type", AbstractReward::getType, RewardType::codec);
    }
    
    public static StreamCodec<RegistryFriendlyByteBuf, AbstractReward<?>> dispatchStreamCodec() {
        return RegistryCodecs.registryFriendlyStreamCodec(RewardTypesPM.TYPES).dispatch(AbstractReward::getType, RewardType::streamCodec);
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
}
