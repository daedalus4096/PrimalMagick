package com.verdantartifice.primalmagick.datagen.lang.builders;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Helper for specifying grimoire GUI-related localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public class GrimoireLanguageBuilder extends AbstractTokenizedLanguageBuilder {
    public GrimoireLanguageBuilder(String cmd, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(cmd, "grimoire", untracker, adder);
    }
}
