package com.verdantartifice.primalmagick.datagen.lang.builders;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Helper for specifying arbitrary tip-related localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public class TipLanguageBuilder extends AbstractTokenizedLanguageBuilder {
    public TipLanguageBuilder(String tipId, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(tipId, "tip", untracker, adder);
    }
}
