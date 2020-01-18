package com.verdantartifice.primalmagic.common.commands.arguments;

import java.util.function.Predicate;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge.KnowledgeType;

/**
 * Input predicate for a knowledge type.  Determines if a given string represents a valid argument.
 * 
 * @author Daedalus4096
 */
public class KnowledgeTypeInput implements Predicate<IPlayerKnowledge.KnowledgeType> {
    protected final IPlayerKnowledge.KnowledgeType type;
    
    public KnowledgeTypeInput(@Nonnull IPlayerKnowledge.KnowledgeType type) {
        this.type = type;
    }
    
    @Nonnull
    public IPlayerKnowledge.KnowledgeType getType() {
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
