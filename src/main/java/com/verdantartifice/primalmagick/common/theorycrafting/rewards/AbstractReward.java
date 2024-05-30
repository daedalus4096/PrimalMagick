package com.verdantartifice.primalmagick.common.theorycrafting.rewards;

import com.mojang.serialization.Codec;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/**
 * Base class for a non-theory reward provided by a theorycrafting research project upon successful completion.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractReward<T extends AbstractReward<T>> {
    public static Codec<AbstractReward<?>> dispatchCodec() {
        return RewardTypesPM.TYPES.get().getCodec().dispatch("reward_type", AbstractReward::getType, RewardType::codec);
    }
    
    public abstract void grant(ServerPlayer player);
    
    public abstract Component getDescription();
    
    protected abstract RewardType<T> getType();

    public static AbstractReward<?> fromNetwork(FriendlyByteBuf buf) {
        ResourceLocation typeId = buf.readResourceLocation();
        return RewardTypesPM.TYPES.get().getValue(typeId).networkReader().apply(buf);
    }
    
    public void toNetwork(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.getType().id());
        this.toNetworkInner(buf);
    }
    
    protected abstract void toNetworkInner(FriendlyByteBuf buf);
}
