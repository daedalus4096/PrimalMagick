package com.verdantartifice.primalmagick.common.research.topics;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.sources.Source;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class AffinityResearchTopic extends AbstractResearchTopic<AffinityResearchTopic> {
    public static final MapCodec<AffinityResearchTopic> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Source.CODEC.fieldOf("source").forGetter(AffinityResearchTopic::getSource),
            Codec.INT.fieldOf("page").forGetter(AffinityResearchTopic::getPage)
    ).apply(instance, AffinityResearchTopic::new));

    public static final StreamCodec<ByteBuf, AffinityResearchTopic> STREAM_CODEC = StreamCodec.composite(
            Source.STREAM_CODEC, AffinityResearchTopic::getSource,
            ByteBufCodecs.VAR_INT, AffinityResearchTopic::getPage,
            AffinityResearchTopic::new);

    protected final Source source;

    public AffinityResearchTopic(Source source, int page) {
        super(page);
        this.source = source;
    }

    public Source getSource() {
        return this.source;
    }

    @Override
    public ResearchTopicType<AffinityResearchTopic> getType() {
        return ResearchTopicTypesPM.AFFINITY.get();
    }

    @Override
    public AffinityResearchTopic withPage(int newPage) {
        return new AffinityResearchTopic(this.source, newPage);
    }
}
