package com.verdantartifice.primalmagick.common.research.keys;

import java.util.Objects;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.research.IconDefinition;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.research.requirements.RequirementCategory;

import net.minecraft.core.RegistryAccess;
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

    @Nonnull
    static TagCraftedKey fromNetworkInner(FriendlyByteBuf buf) {
        return new TagCraftedKey(ItemTags.create(buf.readResourceLocation()));
    }
    
    @Override
    protected void toNetworkInner(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.tagKey.location());
    }
}
