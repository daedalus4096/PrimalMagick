package com.verdantartifice.primalmagick.datagen.lang.builders;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Helper for specifying JEI compatibility-related localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public class JeiLanguageBuilder extends AbstractTokenizedLanguageBuilder {
    public JeiLanguageBuilder(String cmd, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(cmd, "jei", untracker, adder);
    }
}
