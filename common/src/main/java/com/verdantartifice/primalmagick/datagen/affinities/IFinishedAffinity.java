package com.verdantartifice.primalmagick.datagen.affinities;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.common.affinities.AffinityType;
import net.minecraft.resources.Identifier;

public interface IFinishedAffinity {
    AffinityType<?> getType();
    
    Identifier getId();
    
    JsonElement serialize();
}
