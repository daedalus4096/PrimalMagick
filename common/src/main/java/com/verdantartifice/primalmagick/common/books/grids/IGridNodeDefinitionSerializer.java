package com.verdantartifice.primalmagick.common.books.grids;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

/**
 * Primary interface for the serializer of a data-defined linguistics grid node definition.
 *  
 * @author Daedalus4096
 */
public interface IGridNodeDefinitionSerializer {
    /**
     * Read a grid node definition from JSON
     */
    GridNodeDefinition read(ResourceLocation gridId, JsonObject json);
    
    /**
     * Read a grid node definition from the network
     */
    GridNodeDefinition fromNetwork(FriendlyByteBuf buf);

    /**
     * Write a grid node definition to the network
     */
    void toNetwork(FriendlyByteBuf buf, GridNodeDefinition nodeDef);
}
