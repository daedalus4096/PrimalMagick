package com.verdantartifice.primalmagick.common.commands.arguments;

import java.util.function.Predicate;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.common.research.KnowledgeType;

/**
 * Input predicate for a knowledge type.  Determines if a given string represents a valid argument.
 * 
 * @author Daedalus4096
 */
public class KnowledgeTypeInput implements Predicate<KnowledgeType> {
    protected final KnowledgeType type;
    
    public KnowledgeTypeInput(@Nonnull KnowledgeType type) {
        this.type = type;
    }
    
    @Nonnull
    public KnowledgeType getType() {
        return this.type;
    }

    @Override
    public boolean test(KnowledgeType t) {
        return this.type.equals(t);
    }

    @Nonnull
    public String serialize() {
        return this.type.name();
    }
}
