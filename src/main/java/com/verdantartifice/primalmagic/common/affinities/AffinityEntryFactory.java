package com.verdantartifice.primalmagic.common.affinities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.verdantartifice.primalmagic.PrimalMagic;

/**
 * Factory for affinity entry objects based on their given type.
 * 
 * @author Daedalus4096
 */
public class AffinityEntryFactory {
    @Nullable
    public static AffinityEntry create(@Nonnull JsonObject obj) {
        String typeStr = obj.getAsJsonPrimitive("type").getAsString();
        try {
            switch (typeStr) {
            case "block":
                return BlockAffinityEntry.parse(obj);
            default:
                PrimalMagic.LOGGER.warn("Unknown affinity entry type {}", typeStr);
                return null;
            }
        } catch (Exception e) {
            PrimalMagic.LOGGER.warn("Failed to parse affinity entry of type {}", typeStr);
            return null;
        }
    }
}
