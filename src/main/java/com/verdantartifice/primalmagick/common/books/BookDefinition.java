package com.verdantartifice.primalmagick.common.books;

import java.util.function.Function;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;

/**
 * Definition for a mod static book.
 * 
 * @author Daedalus4096
 */
public record BookDefinition(ResourceLocation bookId) {
    public static final Codec<BookDefinition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("bookId").forGetter(BookDefinition::bookId)
        ).apply(instance, BookDefinition::new));
    
    private static final Function<BookDefinition, String> MEMOIZED_DESCRIPTION_ID = Util.memoize(BookDefinition::getDescriptionIdInner);
    
    public String getDescriptionId() {
        return MEMOIZED_DESCRIPTION_ID.apply(this);
    }
    
    private static String getDescriptionIdInner(BookDefinition def) {
        return Util.makeDescriptionId("written_book", BooksPM.BOOKS.get().getKey(def));
    }
}
