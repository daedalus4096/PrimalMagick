package com.verdantartifice.primalmagick.common.books.grids;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

/**
 * Primary interface for the serializer of a data-defined linguistics grid definition.
 *  
 * @author Daedalus4096
 */
public interface IGridDefinitionSerializer {
    /**
     * Read a project template from JSON
     */
    GridDefinition read(ResourceLocation templateId, JsonObject json);
    
    /**
     * Read a project template from the network
     */
    GridDefinition fromNetwork(FriendlyByteBuf buf);

    /**
     * Write a project template to the network
     */
    void toNetwork(FriendlyByteBuf buf, GridDefinition template);
}
