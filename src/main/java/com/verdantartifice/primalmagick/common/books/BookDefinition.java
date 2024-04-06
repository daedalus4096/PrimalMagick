package com.verdantartifice.primalmagick.common.books;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

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
}
