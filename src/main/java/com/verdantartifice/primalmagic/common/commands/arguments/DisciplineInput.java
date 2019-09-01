package com.verdantartifice.primalmagic.common.commands.arguments;

import java.util.function.Predicate;

import javax.annotation.Nonnull;

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
