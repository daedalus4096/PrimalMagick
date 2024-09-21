package com.verdantartifice.primalmagick.datagen.lang.builders;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Helper for specifying spell vehicle-related localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public class SpellVehicleLanguageBuilder extends AbstractSpellComponentLanguageBuilder {
    public SpellVehicleLanguageBuilder(String vehicleName, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(vehicleName, "vehicle", untracker, adder);
    }
}
