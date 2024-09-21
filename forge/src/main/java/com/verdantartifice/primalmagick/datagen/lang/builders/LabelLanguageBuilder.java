package com.verdantartifice.primalmagick.datagen.lang.builders;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Helper for specifying GUI label-related localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public class LabelLanguageBuilder extends AbstractTokenizedLanguageBuilder {
    public LabelLanguageBuilder(String id, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(id, "label", untracker, adder);
    }
}
