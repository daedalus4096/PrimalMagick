package com.verdantartifice.primalmagick.common.theorycrafting.rewards;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

/**
 * Primary interface for the serializer of a data-defined theorycrafting project template's
 * non-theory reward.
 * 
 * @author Daedalus4096
 */
public interface IRewardSerializer<T extends AbstractReward> {
    /**
     * Read a reward definition from JSON
     */
    T read(ResourceLocation templateId, JsonObject json);
    
    /**
     * Read a reward definition from the network
     */
    T fromNetwork(FriendlyByteBuf buf);

    /**
     * Write a reward definition to the network
     */
    void toNetwork(FriendlyByteBuf buf, T reward);
}
