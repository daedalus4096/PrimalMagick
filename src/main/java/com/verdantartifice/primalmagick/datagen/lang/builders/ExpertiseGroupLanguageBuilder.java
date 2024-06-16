package com.verdantartifice.primalmagick.datagen.lang.builders;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Helper for specifying expertise group-related localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public class ExpertiseGroupLanguageBuilder extends AbstractTokenizedLanguageBuilder {
    public ExpertiseGroupLanguageBuilder(String cmd, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(cmd, "expertise_group", untracker, adder);
    }
}
