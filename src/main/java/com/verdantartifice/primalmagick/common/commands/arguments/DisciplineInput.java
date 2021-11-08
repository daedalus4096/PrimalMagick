package com.verdantartifice.primalmagick.common.commands.arguments;

import java.util.function.Predicate;

import javax.annotation.Nonnull;

/**
 * Input predicate for a research discipline.  Determines if a given string represents a valid argument.
 * 
 * @author Daedalus4096
 */
public class DisciplineInput implements Predicate<String> {
    protected final String disciplineKey;
    
    public DisciplineInput(@Nonnull String disciplineKey) {
        this.disciplineKey = disciplineKey;
    }
    
    @Nonnull
    public String getDisciplineKey() {
        return this.disciplineKey;
    }

    @Override
    public boolean test(String arg0) {
        return this.disciplineKey.equals(arg0);
    }

    @Nonnull
    public String serialize() {
        return this.disciplineKey;
    }
}
