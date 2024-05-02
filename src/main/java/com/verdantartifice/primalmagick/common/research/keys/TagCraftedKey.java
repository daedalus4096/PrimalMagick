package com.verdantartifice.primalmagick.common.research.keys;

import java.util.Objects;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class TagCraftedKey extends AbstractResearchKey {
    public static final Codec<TagCraftedKey> CODEC = TagKey.codec(Registries.ITEM).fieldOf("tagKey").xmap(TagCraftedKey::new, key -> key.tagKey).codec();
    private static final String PREFIX = "[#]";
    
    protected final TagKey<Item> tagKey;
    
    public TagCraftedKey(TagKey<Item> tagKey) {
        this.tagKey = Preconditions.checkNotNull(tagKey);
    }

    @Override
    public String toString() {
        return PREFIX + this.tagKey.hashCode();
    }

    @Override
    protected ResearchKeyType<?> getType() {
        return ResearchKeyTypesPM.TAG_CRAFTED.get();
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagKey);
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
        return Objects.equals(tagKey, other.tagKey);
    }
}
