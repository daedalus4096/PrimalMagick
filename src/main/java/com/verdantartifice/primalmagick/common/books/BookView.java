package com.verdantartifice.primalmagick.common.books;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * Record containing all the parameters needed to render a player's view of an encoded static book.
 * 
 * @author Daedalus4096
 */
public record BookView(Either<Holder<BookDefinition>, Holder<Enchantment>> bookDef, Holder<BookLanguage> language, int comprehension) {
    public static final Codec<BookView> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.either(BookDefinition.HOLDER_CODEC, Enchantment.CODEC).fieldOf("bookDef").forGetter(BookView::bookDef),
            BookLanguage.HOLDER_CODEC.fieldOf("language").forGetter(BookView::language),
            Codec.INT.fieldOf("comprehension").forGetter(BookView::comprehension)
        ).apply(instance, BookView::new));
    
    public static final StreamCodec<RegistryFriendlyByteBuf, BookView> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.either(BookDefinition.STREAM_CODEC, Enchantment.STREAM_CODEC), BookView::bookDef,
            BookLanguage.STREAM_CODEC, BookView::language,
            ByteBufCodecs.VAR_INT, BookView::comprehension,
            BookView::new);
    
    public BookView withComprehension(int newComprehension) {
        return new BookView(this.bookDef, this.language, newComprehension);
    }
    
    public ResourceKey<?> unwrapBookKey() {
        return this.bookDef.map(bookHolder -> bookHolder.unwrapKey().get(), enchHolder -> enchHolder.unwrapKey().get());
    }
}
