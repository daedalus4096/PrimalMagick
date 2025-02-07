package com.verdantartifice.primalmagick.common.affinities;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

/**
 * Primary interface for the serializer of a data-defined affinity entry
 * 
 * @author Daedalus4096
 */
public interface IAffinitySerializer<T extends IAffinity> {
    /**
     * Read an affinity entry object from JSON
     */
    T read(ResourceLocation affinityId, JsonObject json);
    
    /**
     * Read an affinity entry object from the network
     */
    T fromNetwork(FriendlyByteBuf buf);

    /**
     * Write an affinity entry object to the network
     */
    void toNetwork(FriendlyByteBuf buf, T affinity);
}
