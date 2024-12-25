package com.verdantartifice.primalmagick.common.commands.arguments;

import com.verdantartifice.primalmagick.common.attunements.AttunementType;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

/**
 * Input predicate for an attunement type.  Determines if a given string represents a valid argument.
 * 
 * @author Daedalus4096
 */
public class AttunementTypeInput implements Predicate<AttunementType> {
    protected final AttunementType type;
    
    public AttunementTypeInput(@Nonnull AttunementType type) {
        this.type = type;
    }
    
    @Nonnull
    public AttunementType getType() {
        return this.type;
    }

    @Override
    public boolean test(AttunementType t) {
        return this.type.equals(t);
    }

    @Nonnull
    public String serialize() {
        return this.type.name();
    }
}
