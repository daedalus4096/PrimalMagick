package com.verdantartifice.primalmagick.common.theorycrafting.materials;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

/**
 * Primary interface for the serializer of a data-defined theorycrafting project material entry.
 * 
 * @author Daedalus4096
 */
public interface IProjectMaterialSerializer<T extends AbstractProjectMaterial> {
    /**
     * Read a project material object from JSON
     */
    T read(ResourceLocation projectId, JsonObject json);
    
    /**
     * Read a project material object from the network
     */
    T fromNetwork(FriendlyByteBuf buf);

    /**
     * Write a project material object to the network
     */
    void toNetwork(FriendlyByteBuf buf, T material);
}
