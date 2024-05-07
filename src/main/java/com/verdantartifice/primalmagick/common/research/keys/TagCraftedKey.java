package com.verdantartifice.primalmagick.common.research.keys;

import java.util.Objects;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.research.requirements.RequirementCategory;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class TagCraftedKey extends AbstractResearchKey<TagCraftedKey> {
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
    public RequirementCategory getRequirementCategory() {
        return RequirementCategory.MUST_CRAFT;
    }

    @Override
    protected ResearchKeyType<TagCraftedKey> getType() {
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

    @Nonnull
    public static TagCraftedKey fromNetwork(FriendlyByteBuf buf) {
        return new TagCraftedKey(ItemTags.create(buf.readResourceLocation()));
    }
    
    @Override
    protected void toNetworkInner(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.tagKey.location());
    }
}