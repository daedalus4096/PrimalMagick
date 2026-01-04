package com.verdantartifice.primalmagick.datagen.linguistics;

import com.google.gson.JsonElement;
import net.minecraft.resources.Identifier;

import java.util.OptionalInt;

public interface IFinishedGridNodeReward {
    JsonElement serialize();

    default OptionalInt getComprehensionPoints(Identifier bookLanguageId) {
        return OptionalInt.empty();
    }
}
