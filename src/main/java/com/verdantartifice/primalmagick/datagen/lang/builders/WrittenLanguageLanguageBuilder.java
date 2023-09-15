package com.verdantartifice.primalmagick.datagen.lang.builders;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

/**
 * Helper for specifying written book language definition localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public class WrittenLanguageLanguageBuilder extends AbstractLanguageBuilder<BookLanguage, WrittenLanguageLanguageBuilder> {
    public WrittenLanguageLanguageBuilder(BookLanguage lang, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(lang, lang::getDescriptionId, untracker, adder);
    }

    @Override
    public String getBuilderKey() {
        return ResourceKey.create(RegistryKeysPM.BOOK_LANGUAGES, this.getBaseRegistryKey()).toString();
    }

    @Override
    protected ResourceLocation getBaseRegistryKey(BookLanguage base) {
        return Objects.requireNonNull(BookLanguagesPM.LANGUAGES.get().getKey(base));
    }
}
