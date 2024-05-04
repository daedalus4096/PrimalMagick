package com.verdantartifice.primalmagick.common.research.keys;

import java.util.Objects;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.research.requirements.RequirementCategory;

import net.minecraft.network.FriendlyByteBuf;

public class ResearchEntryKey extends AbstractResearchKey<ResearchEntryKey> {
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
    public RequirementCategory getRequirementCategory() {
        return RequirementCategory.RESEARCH;
    }

    @Override
    protected ResearchKeyType<ResearchEntryKey> getType() {
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
    
    @Nonnull
    public static ResearchEntryKey fromNetwork(FriendlyByteBuf buf) {
        return new ResearchEntryKey(buf.readUtf());
    }
    
    @Override
    public void toNetworkInner(FriendlyByteBuf buf) {
        buf.writeUtf(this.rootKey);
    }
}
