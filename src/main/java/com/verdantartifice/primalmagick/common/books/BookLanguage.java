package com.verdantartifice.primalmagick.common.books;

import java.util.function.Function;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceLocation;

/**
 * Definition for a language in which a static book can be written/encoded.
 * 
 * @author Daedalus4096
 */
public record BookLanguage(ResourceLocation languageId, Style style, int complexity, boolean autoTranslate) {
    public static final Codec<BookLanguage> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("languageId").forGetter(BookLanguage::languageId),
            Style.Serializer.CODEC.fieldOf("style").forGetter(BookLanguage::style),
            Codec.INT.fieldOf("complexity").forGetter(BookLanguage::complexity),
            Codec.BOOL.fieldOf("autoTranslate").forGetter(BookLanguage::autoTranslate)
        ).apply(instance, BookLanguage::new));
    public static final Codec<BookLanguage> NETWORK_CODEC = DIRECT_CODEC;   // TODO Modify if some language data is not necessary on the client
    
    public static final Codec<Holder<BookLanguage>> HOLDER_CODEC = RegistryFixedCodec.create(RegistryKeysPM.BOOK_LANGUAGES);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<BookLanguage>> STREAM_CODEC = ByteBufCodecs.holderRegistry(RegistryKeysPM.BOOK_LANGUAGES);
    
    private static final Function<BookLanguage, String> MEMOIZED_NAME_ID = Util.memoize(BookLanguage::getNameIdInner);
    private static final Function<BookLanguage, String> MEMOIZED_DESCRIPTION_ID = Util.memoize(BookLanguage::getDescriptionIdInner);
    
    public boolean isComplex() {
        return this.complexity() > 0 && !this.autoTranslate();
    }
    
    public boolean isTranslatable() {
        return this.complexity() >= 0;
    }
    
    public String getNameId() {
        return MEMOIZED_NAME_ID.apply(this);
    }
    
    private static String getNameIdInner(BookLanguage lang) {
        return Util.makeDescriptionId("written_language", lang.languageId());
    }
    
    public MutableComponent getName() {
        return Component.translatable(this.getNameId());
    }
    
    public String getDescriptionId() {
        return MEMOIZED_DESCRIPTION_ID.apply(this);
    }
    
    private static String getDescriptionIdInner(BookLanguage lang) {
        return getNameIdInner(lang) + ".description";
    }
    
    public MutableComponent getDescription() {
        return Component.translatable(this.getDescriptionId());
    }
    
    public ResourceLocation getGlyphSprite() {
        return this.languageId().withPrefix("books/language_glyphs/");
    }
}
