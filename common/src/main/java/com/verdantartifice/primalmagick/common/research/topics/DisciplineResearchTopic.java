package com.verdantartifice.primalmagick.common.research.topics;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.research.keys.ResearchDisciplineKey;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Objects;

/**
 * Research topic that points to a mod research discipline in the Grimoire.
 * 
 * @author Daedalus4096
 */
public class DisciplineResearchTopic extends AbstractResearchTopic<DisciplineResearchTopic> {
    public static final MapCodec<DisciplineResearchTopic> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResearchDisciplineKey.CODEC.fieldOf("data").forGetter(DisciplineResearchTopic::getDiscipline),
            Codec.INT.fieldOf("page").forGetter(DisciplineResearchTopic::getPage)
        ).apply(instance, DisciplineResearchTopic::new));
    
    public static final StreamCodec<ByteBuf, DisciplineResearchTopic> STREAM_CODEC = StreamCodec.composite(
            ResearchDisciplineKey.STREAM_CODEC, DisciplineResearchTopic::getDiscipline,
            ByteBufCodecs.VAR_INT, DisciplineResearchTopic::getPage,
            DisciplineResearchTopic::new);
    
    protected final ResearchDisciplineKey discipline;
    
    public DisciplineResearchTopic(ResearchDisciplineKey disciplineKey, int page) {
        super(page);
        this.discipline = disciplineKey;
    }
    
    public ResearchDisciplineKey getDiscipline() {
        return this.discipline;
    }

    @Override
    public ResearchTopicType<DisciplineResearchTopic> getType() {
        return ResearchTopicTypesPM.DISCIPLINE.get();
    }

    @Override
    public DisciplineResearchTopic withPage(int newPage) {
        return new DisciplineResearchTopic(this.discipline, newPage);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DisciplineResearchTopic that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(discipline, that.discipline);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), discipline);
    }
}
