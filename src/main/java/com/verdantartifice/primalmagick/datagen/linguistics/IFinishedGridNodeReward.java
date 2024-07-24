package com.verdantartifice.primalmagick.datagen.linguistics;

import java.util.OptionalInt;

import com.google.gson.JsonElement;

import net.minecraft.resources.ResourceLocation;

public interface IFinishedGridNodeReward {
    JsonElement serialize();

    default OptionalInt getComprehensionPoints(ResourceLocation bookLanguageId) {
        return OptionalInt.empty();
    }
}
