package com.verdantartifice.primalmagick.common.research.topics;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

/**
 * Research topic that points to a specific page in the Grimoire.
 * 
 * @author Daedalus4096
 */
public class OtherResearchTopic extends AbstractResearchTopic<OtherResearchTopic> {
    public static final MapCodec<OtherResearchTopic> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("data").forGetter(OtherResearchTopic::getData),
            Codec.INT.fieldOf("page").forGetter(OtherResearchTopic::getPage)
        ).apply(instance, OtherResearchTopic::new));
    
    public static final StreamCodec<ByteBuf, OtherResearchTopic> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, OtherResearchTopic::getData,
            ByteBufCodecs.VAR_INT, OtherResearchTopic::getPage,
            OtherResearchTopic::new);
    
    protected final String data;
    
    public OtherResearchTopic(String data, int page) {
        super(page);
        this.data = data;
    }
    
    public String getData() {
        return this.data;
    }

    @Override
    public ResearchTopicType<OtherResearchTopic> getType() {
        return ResearchTopicTypesPM.OTHER.get();
    }

    @Override
    public OtherResearchTopic withPage(int newPage) {
        return new OtherResearchTopic(this.data, newPage);
    }
}
