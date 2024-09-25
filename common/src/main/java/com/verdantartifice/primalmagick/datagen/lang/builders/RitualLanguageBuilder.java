package com.verdantartifice.primalmagick.datagen.lang.builders;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Helper for specifying ritual feedback-related localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public class RitualLanguageBuilder extends AbstractTokenizedLanguageBuilder {
    public RitualLanguageBuilder(String cmd, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(cmd, "ritual", untracker, adder);
    }
}
