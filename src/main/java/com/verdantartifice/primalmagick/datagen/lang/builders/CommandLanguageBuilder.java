package com.verdantartifice.primalmagick.datagen.lang.builders;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Helper for specifying debug command-related localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public class CommandLanguageBuilder extends AbstractTokenizedLanguageBuilder {
    public CommandLanguageBuilder(String cmd, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(cmd, "commands", untracker, adder);
    }
}
