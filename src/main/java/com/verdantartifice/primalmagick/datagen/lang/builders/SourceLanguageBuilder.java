package com.verdantartifice.primalmagick.datagen.lang.builders;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.resources.ResourceLocation;

/**
 * Helper for specifying magickal source-related localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public class SourceLanguageBuilder extends AbstractLanguageBuilder<Source> {
    public SourceLanguageBuilder(Source source, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(source, source::getNameTranslationKey, untracker, adder);
    }

    @Override
    public String getBuilderKey() {
        return this.getBaseRegistryKey().withPrefix("source/").toString();
    }

    @Override
    protected ResourceLocation getBaseRegistryKey(Source base) {
        return PrimalMagick.resource(base.getTag());
    }

    public SourceLanguageBuilder name(String value) {
        this.add(this.getKey(), value);
        return this;
    }
}
