package com.verdantartifice.primalmagick.common.research.topics;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;

/**
 * Research topic that points to a linguistics entry in the Grimoire.
 * 
 * @author Daedalus4096
 */
public class LanguageResearchTopic extends AbstractResearchTopic<LanguageResearchTopic> {
    public static final MapCodec<LanguageResearchTopic> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceKey.codec(RegistryKeysPM.BOOK_LANGUAGES).fieldOf("language").forGetter(LanguageResearchTopic::getLanguage),
            Codec.INT.fieldOf("page").forGetter(LanguageResearchTopic::getPage)
        ).apply(instance, LanguageResearchTopic::new));
    
    public static final StreamCodec<ByteBuf, LanguageResearchTopic> STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(RegistryKeysPM.BOOK_LANGUAGES), LanguageResearchTopic::getLanguage,
            ByteBufCodecs.VAR_INT, LanguageResearchTopic::getPage,
            LanguageResearchTopic::new);
    
    protected final ResourceKey<BookLanguage> language;
    
    public LanguageResearchTopic(ResourceKey<BookLanguage> language, int page) {
        super(page);
        this.language = language;
    }
    
    public ResourceKey<BookLanguage> getLanguage() {
        return this.language;
    }

    @Override
    public ResearchTopicType<LanguageResearchTopic> getType() {
        return ResearchTopicTypesPM.LANGUAGE.get();
    }

    @Override
    public LanguageResearchTopic withPage(int newPage) {
        return new LanguageResearchTopic(this.language, newPage);
    }
}
