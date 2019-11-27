package com.verdantartifice.primalmagic.common.commands.arguments;

import java.util.function.Predicate;

import javax.annotation.Nonnull;

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
