package com.verdantartifice.primalmagick.common.research;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

public record IconDefinition(boolean isItem, boolean isTag, ResourceLocation location) {
    public static final Codec<IconDefinition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("isItem").forGetter(IconDefinition::isItem),
            Codec.BOOL.fieldOf("isTag").forGetter(IconDefinition::isTag),
            ResourceLocation.CODEC.fieldOf("location").forGetter(IconDefinition::location)
        ).apply(instance, IconDefinition::new));
    
    public static IconDefinition of(ItemLike item) {
        return new IconDefinition(true, false, ForgeRegistries.ITEMS.getKey(item.asItem()));
    }
    
    public static IconDefinition of(TagKey<Item> tagKey) {
        return new IconDefinition(false, true, tagKey.location());
    }
    
    public static IconDefinition of(ResourceLocation loc) {
        return new IconDefinition(false, false, Preconditions.checkNotNull(loc));
    }
    
    @Nullable
    public Item asItem() {
        return this.isItem ? ForgeRegistries.ITEMS.getValue(this.location) : null;
    }
    
    @Nullable
    public TagKey<Item> asTagKey() {
        return this.isTag ? TagKey.create(Registries.ITEM, location) : null;
    }
    
    @Nullable
    public static IconDefinition fromNetwork(FriendlyByteBuf buf) {
        return new IconDefinition(buf.readBoolean(), buf.readBoolean(), buf.readResourceLocation());
    }
    
    public static void toNetwork(FriendlyByteBuf buf, @Nullable IconDefinition icon) {
        buf.writeBoolean(icon.isItem);
        buf.writeBoolean(icon.isTag);
        buf.writeResourceLocation(icon.location);
    }
}
