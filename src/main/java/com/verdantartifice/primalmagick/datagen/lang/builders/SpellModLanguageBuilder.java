package com.verdantartifice.primalmagick.datagen.lang.builders;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Helper for specifying spell mod-related localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public class SpellModLanguageBuilder extends AbstractSpellComponentLanguageBuilder {
    public SpellModLanguageBuilder(String vehicleName, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(vehicleName, "mod", untracker, adder);
    }
}
