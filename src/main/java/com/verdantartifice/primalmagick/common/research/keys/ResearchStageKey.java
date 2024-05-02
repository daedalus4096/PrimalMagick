package com.verdantartifice.primalmagick.common.research.keys;

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
        // TODO Auto-generated method stub
        return null;
    }
}
