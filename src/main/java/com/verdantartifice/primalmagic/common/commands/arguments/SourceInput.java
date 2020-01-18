package com.verdantartifice.primalmagic.common.commands.arguments;

import java.util.function.Predicate;

import javax.annotation.Nonnull;

/**
 * Input predicate for a source.  Determines if a given string represents a valid argument.
 * 
 * @author Daedalus4096
 */
public class SourceInput implements Predicate<String> {
    protected final String sourceTag;
    
    public SourceInput(@Nonnull String sourceTag) {
        this.sourceTag = sourceTag;
    }
    
    @Nonnull
    public String getSourceTag() {
        return this.sourceTag;
    }
    
    @Override
    public boolean test(String arg0) {
        return this.sourceTag.equals(arg0);
    }

    @Nonnull
    public String serialize() {
        return this.sourceTag;
    }
}
