package com.verdantartifice.primalmagick.datagen.lang.builders;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Helper for specifying concoction-related localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public class ConcoctionLanguageBuilder extends AbstractTokenizedLanguageBuilder {
    public ConcoctionLanguageBuilder(String id, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(id, "concoctions", untracker, adder);
    }
}
