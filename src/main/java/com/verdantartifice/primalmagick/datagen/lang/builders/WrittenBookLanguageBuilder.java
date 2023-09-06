package com.verdantartifice.primalmagick.datagen.lang.builders;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.verdantartifice.primalmagick.common.books.BookDefinition;
import com.verdantartifice.primalmagick.common.books.BooksPM;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

/**
 * Helper for specifying written book localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public class WrittenBookLanguageBuilder extends AbstractLanguageBuilder<BookDefinition, WrittenBookLanguageBuilder> {
    public WrittenBookLanguageBuilder(BookDefinition bookDef, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(bookDef, bookDef::getDescriptionId, untracker, adder);
    }

    @Override
    public String getBuilderKey() {
        return ResourceKey.create(RegistryKeysPM.BOOKS, this.getBaseRegistryKey()).toString();
    }

    @Override
    protected ResourceLocation getBaseRegistryKey(BookDefinition base) {
        return Objects.requireNonNull(BooksPM.BOOKS.get().getKey(base));
    }

    @Override
    public WrittenBookLanguageBuilder name(String value) {
        this.add(this.getKey("title"), value);
        return this;
    }
    
    public WrittenBookLanguageBuilder author(String value) {
        this.add(this.getKey("author"), value);
        return this;
    }
    
    public WrittenBookLanguageBuilder text(String value) {
        this.add(this.getKey("text"), value);
        return this;
    }
    
    public WrittenBookLanguageBuilder foreword(String value) {
        this.add(this.getKey("foreword"), value);
        return this;
    }
    
    public WrittenBookLanguageBuilder afterword(String value) {
        this.add(this.getKey("afterword"), value);
        return this;
    }
}
