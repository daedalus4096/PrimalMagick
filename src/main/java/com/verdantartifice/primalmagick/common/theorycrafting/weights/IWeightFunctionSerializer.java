package com.verdantartifice.primalmagick.common.theorycrafting.weights;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

/**
 * Primary interface for the serializer of a data-defined theorycrafting project template's
 * weight function.
 * 
 * @author Daedalus4096
 */
public interface IWeightFunctionSerializer<T extends IWeightFunction> {
    /**
     * Read a weight function from JSON
     */
    T read(ResourceLocation templateId, JsonObject json);
    
    /**
     * Read a weight function from the network
     */
    T fromNetwork(FriendlyByteBuf buf);

    /**
     * Write a weight function to the network
     */
    void toNetwork(FriendlyByteBuf buf, T template);
}
