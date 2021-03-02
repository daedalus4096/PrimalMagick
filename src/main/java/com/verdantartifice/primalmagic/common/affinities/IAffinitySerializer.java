package com.verdantartifice.primalmagic.common.affinities;

import com.google.gson.JsonObject;

import net.minecraft.util.ResourceLocation;

/**
 * Primary interface for the serializer of a data-defined affinity entry
 * @author Daedalus4096
 */
public interface IAffinitySerializer<T extends IAffinity> {
    /**
     * Read an affinity entry object from JSON
     */
    T read(ResourceLocation affinityId, JsonObject json);
    
    /**
     * Write a JSON object from an affinity
     */
    JsonObject write(IAffinity affinity);
}
