package com.verdantartifice.primalmagick.common.research.keys;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.research.IconDefinition;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.requirements.RequirementCategory;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public class ResearchEntryKey extends AbstractResearchKey<ResearchEntryKey> {
    public static final MapCodec<ResearchEntryKey> CODEC = ResourceKey.codec(RegistryKeysPM.RESEARCH_ENTRIES).fieldOf("rootKey").xmap(ResearchEntryKey::new, key -> key.rootKey);
    public static final StreamCodec<ByteBuf, ResearchEntryKey> STREAM_CODEC = ResourceKey.streamCodec(RegistryKeysPM.RESEARCH_ENTRIES).map(ResearchEntryKey::new, key -> key.rootKey);
    
    private static final ResourceLocation ICON_UNKNOWN = ResourceUtils.loc("textures/research/research_unknown.png");

    protected final ResourceKey<ResearchEntry> rootKey;
    
    public ResearchEntryKey(ResourceKey<ResearchEntry> rootKey) {
        this.rootKey = rootKey;
    }
    
    public ResourceKey<ResearchEntry> getRootKey() {
        return this.rootKey;
    }

    @Override
    public String toString() {
        return this.rootKey.location().toString();
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
    public IconDefinition getIcon(RegistryAccess registryAccess) {
        return registryAccess.registryOrThrow(RegistryKeysPM.RESEARCH_ENTRIES).getHolder(this.rootKey).flatMap(ref -> ref.value().iconOpt()).orElse(IconDefinition.of(ICON_UNKNOWN));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.rootKey.registry(), this.rootKey.location());
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
    
    public static ResearchEntryKey fromNetwork(RegistryFriendlyByteBuf buf) {
        return (ResearchEntryKey)AbstractResearchKey.fromNetwork(buf);
    }
}
