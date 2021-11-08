package com.verdantartifice.primalmagick.common.commands.arguments;

import java.util.function.Predicate;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;

/**
 * Input predicate for a research entry.  Determines if a given string represents a valid argument.
 * 
 * @author Daedalus4096
 */
public class ResearchInput implements Predicate<SimpleResearchKey> {
    protected final SimpleResearchKey key;
    
    public ResearchInput(@Nonnull SimpleResearchKey key) {
        this.key = key;
    }
    
    @Nonnull
    public SimpleResearchKey getKey() {
        return this.key;
    }

    @Override
    public boolean test(SimpleResearchKey t) {
        return this.key.equals(t);
    }

    @Nonnull
    public String serialize() {
        return this.key.getRootKey();
    }
}
