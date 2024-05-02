package com.verdantartifice.primalmagick.common.research.keys;

import com.mojang.serialization.Codec;

public class ResearchEntryKey extends AbstractResearchKey {
    public static final Codec<ResearchEntryKey> CODEC = Codec.STRING.fieldOf("rootKey").xmap(ResearchEntryKey::new, key -> key.rootKey).codec();
    
    protected final String rootKey; // TODO Replace with a ResourceKey once the research system refactor is complete
    
    public ResearchEntryKey(String rootKey) {
        this.rootKey = rootKey;
    }
    
    public String getRootKey() {
        return this.rootKey;
    }

    @Override
    public String toString() {
        return this.rootKey;
    }

    @Override
    protected ResearchKeyType<?> getType() {
        return ResearchKeyTypesPM.RESEARCH_ENTRY.get();
    }
}
