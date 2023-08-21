package com.verdantartifice.primalmagick.datagen.lang.builders;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Helper for specifying spell payload-related localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public class SpellPayloadLanguageBuilder extends AbstractSpellComponentLanguageBuilder {
    public SpellPayloadLanguageBuilder(String vehicleName, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(vehicleName, "payload", untracker, adder);
    }
}
