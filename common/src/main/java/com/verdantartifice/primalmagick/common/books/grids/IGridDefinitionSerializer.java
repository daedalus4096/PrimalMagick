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
     * Read a grid definition from JSON
     */
    GridDefinition read(ResourceLocation gridId, JsonObject json);
    
    /**
     * Read a grid definition from the network
     */
    GridDefinition fromNetwork(FriendlyByteBuf buf);

    /**
     * Write a grid definition to the network
     */
    void toNetwork(FriendlyByteBuf buf, GridDefinition gridDef);
}
