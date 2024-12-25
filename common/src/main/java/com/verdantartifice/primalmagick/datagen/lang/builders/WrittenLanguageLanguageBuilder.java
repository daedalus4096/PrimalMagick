package com.verdantartifice.primalmagick.datagen.lang.builders;

import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import net.minecraft.Util;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Helper for specifying written book language definition localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public class WrittenLanguageLanguageBuilder extends AbstractLanguageBuilder<ResourceKey<BookLanguage>, WrittenLanguageLanguageBuilder> {
    public WrittenLanguageLanguageBuilder(ResourceKey<BookLanguage> langKey, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(langKey, () -> Util.makeDescriptionId("written_language", langKey.location()), untracker, adder);
    }

    @Override
    public String getBuilderKey() {
        return ResourceKey.create(RegistryKeysPM.BOOK_LANGUAGES, this.getBaseRegistryKey()).toString();
    }

    @Override
    protected ResourceLocation getBaseRegistryKey(ResourceKey<BookLanguage> base) {
        return Objects.requireNonNull(base).location();
    }
    
    public WrittenLanguageLanguageBuilder description(String value) {
        this.add(this.getKey("description"), value);
        return this;
    }
}
