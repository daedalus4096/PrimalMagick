package com.verdantartifice.primalmagick.common.research.keys;

import com.google.common.base.Preconditions;
import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.research.IconDefinition;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.research.requirements.RequirementCategory;
import com.verdantartifice.primalmagick.common.util.StreamCodecUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.Objects;

public class TagCraftedKey extends AbstractResearchKey<TagCraftedKey> {
    public static final MapCodec<TagCraftedKey> CODEC = TagKey.codec(Registries.ITEM).fieldOf("tagKey").xmap(TagCraftedKey::new, key -> key.tagKey);
    public static final StreamCodec<ByteBuf, TagCraftedKey> STREAM_CODEC = StreamCodecUtils.tagKey(Registries.ITEM).map(TagCraftedKey::new, key -> key.tagKey);
    
    private static final String PREFIX = "[#]";
    
    protected final TagKey<Item> tagKey;
    
    public TagCraftedKey(TagKey<Item> tagKey) {
        this.tagKey = Preconditions.checkNotNull(tagKey);
        ResearchManager.addCraftingReference(this.tagKey.hashCode());
    }

    @Override
    public String toString() {
        return PREFIX + this.tagKey.hashCode();
    }

    @Override
    public RequirementCategory getRequirementCategory() {
        return RequirementCategory.MUST_CRAFT;
    }

    @Override
    protected ResearchKeyType<TagCraftedKey> getType() {
        return ResearchKeyTypesPM.TAG_CRAFTED.get();
    }

    @Override
    public IconDefinition getIcon(RegistryAccess registryAccess) {
        return IconDefinition.of(this.tagKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.tagKey);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TagCraftedKey other = (TagCraftedKey) obj;
        return Objects.equals(this.tagKey, other.tagKey);
    }
}
