package com.verdantartifice.primalmagick.common.research.keys;

import java.util.Objects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.ExtraCodecs;

public class ResearchStageKey extends ResearchEntryKey {
    public static final Codec<ResearchStageKey> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("rootKey").forGetter(ResearchStageKey::getRootKey), 
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("stage").forGetter(ResearchStageKey::getStage)
        ).apply(instance, ResearchStageKey::new));
    
    protected final int stage;
    
    public ResearchStageKey(String rootKey, int stage) {
        super(rootKey);
        this.stage = stage;
    }
    
    public int getStage() {
        return this.stage;
    }

    @Override
    public String toString() {
        return this.rootKey + "@" + this.stage;
    }

    @Override
    protected ResearchKeyType<?> getType() {
        return ResearchKeyTypesPM.RESEARCH_STAGE.get();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(stage);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        ResearchStageKey other = (ResearchStageKey) obj;
        return stage == other.stage;
    }
}
