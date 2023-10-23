package com.verdantartifice.primalmagick.common.research;

import javax.annotation.Nonnull;

/**
 * Factory class which generates a research key of the appropriate type, if possible, for a given string.
 * 
 * @author Daedalus4096
 */
public class ResearchKeyFactory {
    public static IResearchKey parse(@Nonnull String keyStr) {
        if (keyStr == null) {
            throw new IllegalArgumentException("Key string may not be null");
        } else if (keyStr.isEmpty()) {
            return SimpleResearchKey.EMPTY;
        } else if (keyStr.startsWith("?") && keyStr.contains(":")) {
            return QuorumResearchKey.parse(keyStr);
        } else if (keyStr.contains("&&") || keyStr.contains("||")) {
            return CompoundResearchKey.parse(keyStr);
        } else {
            return SimpleResearchKey.parse(keyStr);
        }
    }
}
