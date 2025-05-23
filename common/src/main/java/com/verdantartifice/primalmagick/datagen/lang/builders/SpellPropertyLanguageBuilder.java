package com.verdantartifice.primalmagick.datagen.lang.builders;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Helper for specifying spell property-related localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public class SpellPropertyLanguageBuilder extends AbstractLanguageBuilder<String, SpellPropertyLanguageBuilder> {
    public SpellPropertyLanguageBuilder(String id, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(id, () -> String.join(".", "spells", Constants.MOD_ID, "property", id), untracker, adder);
    }

    @Override
    public String getBuilderKey() {
        return this.getBaseRegistryKey().withPrefix("spell_property/").toString();
    }

    @Override
    protected ResourceLocation getBaseRegistryKey(String base) {
        return ResourceUtils.loc(base.toLowerCase());
    }
}
