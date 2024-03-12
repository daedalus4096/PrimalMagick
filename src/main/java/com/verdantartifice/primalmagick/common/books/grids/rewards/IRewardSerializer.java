package com.verdantartifice.primalmagick.common.books.grids.rewards;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

/**
 * Primary interface for the serializer of a data-defined linguistics grid node's reward.
 * 
 * @author Daedalus4096
 */
public interface IRewardSerializer<T extends IReward> {
    /**
     * Read a reward definition from JSON
     */
    T read(ResourceLocation gridId, JsonObject json);
    
    /**
     * Read a reward definition from the network
     */
    T fromNetwork(FriendlyByteBuf buf);

    /**
     * Write a reward definition to the network
     */
    void toNetwork(FriendlyByteBuf buf, T reward);
}
