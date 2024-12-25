package com.verdantartifice.primalmagick.common.theorycrafting;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

/**
 * Primary interface for the serializer of a data-defined theorycrafting project template. 
 * @author Daedalus4096
 */
public interface IProjectTemplateSerializer {
    /**
     * Read a project template from JSON
     */
    ProjectTemplate read(ResourceLocation templateId, JsonObject json);
    
    /**
     * Read a project template from the network
     */
    ProjectTemplate fromNetwork(FriendlyByteBuf buf);

    /**
     * Write a project template to the network
     */
    void toNetwork(FriendlyByteBuf buf, ProjectTemplate template);
}
