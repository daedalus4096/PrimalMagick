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
public class SourceLanguageBuilder extends AbstractLanguageBuilder<Source, SourceLanguageBuilder> {
    protected final BiConsumer<Source, String> saver;
    
    public SourceLanguageBuilder(Source source, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder, BiConsumer<Source, String> saver) {
        super(source, source::getNameTranslationKey, untracker, adder);
        this.saver = saver;
    }

    @Override
    public String getBuilderKey() {
        return this.getBaseRegistryKey().withPrefix("source/").toString();
    }

    @Override
    protected ResourceLocation getBaseRegistryKey(Source base) {
        return PrimalMagick.resource(base.getTag());
    }
    
    @Override
    public SourceLanguageBuilder name(String value) {
        this.saver.accept(this.base, value);
        return super.name(value);
    }

    public SourceLanguageBuilder attunement(String value) {
        this.add(this.getKey("attunement", "text"), value);
        return this;
    }
}
