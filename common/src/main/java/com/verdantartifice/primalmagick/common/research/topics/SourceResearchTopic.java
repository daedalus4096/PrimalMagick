package com.verdantartifice.primalmagick.common.research.topics;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.sources.Source;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

/**
 * Research topic that points to an attunement entry in the Grimoire.
 * 
 * @author Daedalus4096
 */
public class SourceResearchTopic extends AbstractResearchTopic<SourceResearchTopic> {
    public static final MapCodec<SourceResearchTopic> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Source.CODEC.fieldOf("source").forGetter(SourceResearchTopic::getSource),
            Codec.INT.fieldOf("page").forGetter(SourceResearchTopic::getPage)
        ).apply(instance, SourceResearchTopic::new));
    
    public static final StreamCodec<ByteBuf, SourceResearchTopic> STREAM_CODEC = StreamCodec.composite(
            Source.STREAM_CODEC, SourceResearchTopic::getSource,
            ByteBufCodecs.VAR_INT, SourceResearchTopic::getPage,
            SourceResearchTopic::new);
    
    protected final Source source;
    
    public SourceResearchTopic(Source source, int page) {
        super(page);
        this.source = source;
    }
    
    public Source getSource() {
        return this.source;
    }

    @Override
    public ResearchTopicType<SourceResearchTopic> getType() {
        return ResearchTopicTypesPM.SOURCE.get();
    }

    @Override
    public SourceResearchTopic withPage(int newPage) {
        return new SourceResearchTopic(this.source, newPage);
    }
}
