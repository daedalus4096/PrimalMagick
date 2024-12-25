package com.verdantartifice.primalmagick.datagen.linguistics;

import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;

import java.util.OptionalInt;

public interface IFinishedGridNodeReward {
    JsonElement serialize();

    default OptionalInt getComprehensionPoints(ResourceLocation bookLanguageId) {
        return OptionalInt.empty();
    }
}
