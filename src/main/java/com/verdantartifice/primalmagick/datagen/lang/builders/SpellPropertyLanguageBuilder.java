package com.verdantartifice.primalmagick.datagen.lang.builders;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.resources.ResourceLocation;

/**
 * Helper for specifying spell property-related localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public class SpellPropertyLanguageBuilder extends AbstractLanguageBuilder<String, SpellPropertyLanguageBuilder> {
    public SpellPropertyLanguageBuilder(String id, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(id, () -> String.join(".", "spells", PrimalMagick.MODID, "property", id), untracker, adder);
    }

    @Override
    public String getBuilderKey() {
        return this.getBaseRegistryKey().withPrefix("spell_property/").toString();
    }

    @Override
    protected ResourceLocation getBaseRegistryKey(String base) {
        return PrimalMagick.resource(base.toLowerCase());
    }
}
