package com.verdantartifice.primalmagick.common.research.keys;

import java.util.Objects;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;

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

    @Override
    public int hashCode() {
        return Objects.hash(rootKey);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ResearchEntryKey other = (ResearchEntryKey) obj;
        return Objects.equals(rootKey, other.rootKey);
    }

    @Override
    public ResearchEntry getResearchEntry() {
        return ResearchEntries.getEntry(this.rootKey);
    }
}
