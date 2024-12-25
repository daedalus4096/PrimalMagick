package com.verdantartifice.primalmagick.common.research.topics;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

/**
 * Research topic that points to the main index of the Grimoire.
 * 
 * @author Daedalus4096
 */
public class MainIndexResearchTopic extends AbstractResearchTopic<MainIndexResearchTopic> {
    public static final MainIndexResearchTopic INSTANCE = new MainIndexResearchTopic();
    
    public static final MapCodec<MainIndexResearchTopic> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<ByteBuf, MainIndexResearchTopic> STREAM_CODEC = StreamCodec.unit(INSTANCE);
    
    protected MainIndexResearchTopic() {
        super(0);
    }

    @Override
    public ResearchTopicType<MainIndexResearchTopic> getType() {
        return ResearchTopicTypesPM.MAIN_INDEX.get();
    }

    @Override
    public MainIndexResearchTopic withPage(int newPage) {
        return INSTANCE;
    }
}
