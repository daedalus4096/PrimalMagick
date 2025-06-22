package com.verdantartifice.primalmagick.common.misc;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.platform.Services;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class IconDefinition {
    public static final Codec<IconDefinition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.optionalFieldOf("isItem", false).forGetter(IconDefinition::isItem),
            Codec.BOOL.optionalFieldOf("isTag", false).forGetter(IconDefinition::isTag),
            ResourceLocation.CODEC.fieldOf("location").forGetter(IconDefinition::getLocation),
            Codec.STRING.optionalFieldOf("tooltipOverride").forGetter(def -> def.tooltipOverrideOpt)
        ).apply(instance, IconDefinition::new));
    public static final StreamCodec<ByteBuf, IconDefinition> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            IconDefinition::isItem,
            ByteBufCodecs.BOOL,
            IconDefinition::isTag,
            ResourceLocation.STREAM_CODEC,
            IconDefinition::getLocation,
            ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8),
            def -> def.tooltipOverrideOpt,
            IconDefinition::new);
    
    private final boolean isItem;
    private final boolean isTag;
    private final ResourceLocation location;
    private final Optional<String> tooltipOverrideOpt;
    
    private IconDefinition(boolean isItem, boolean isTag, ResourceLocation loc, Optional<String> tooltipOverrideOpt) {
        this.isItem = isItem;
        this.isTag = isTag;
        this.location = loc;
        this.tooltipOverrideOpt = tooltipOverrideOpt;
    }
    
    public static IconDefinition of(ItemLike item) {
        return new IconDefinition(true, false, Services.ITEMS_REGISTRY.getKey(item.asItem()), Optional.empty());
    }
    
    public static IconDefinition of(TagKey<Item> tagKey) {
        return new IconDefinition(false, true, tagKey.location(), Optional.empty());
    }
    
    public static IconDefinition of(ResourceLocation loc) {
        return of(loc, null);
    }
    
    public static IconDefinition of(ResourceLocation loc, String tooltipKey) {
        return new IconDefinition(false, false, Preconditions.checkNotNull(loc), Optional.ofNullable(tooltipKey));
    }
    
    public boolean isItem() {
        return this.isItem;
    }
    
    public boolean isTag() {
        return this.isTag;
    }
    
    public ResourceLocation getLocation() {
        return this.location;
    }
    
    @Nullable
    public Item asItem() {
        return this.isItem ? Services.ITEMS_REGISTRY.get(this.location) : null;
    }
    
    @Nullable
    public TagKey<Item> asTagKey() {
        return this.isTag ? TagKey.create(Registries.ITEM, this.location) : null;
    }
    
    public List<Component> getTooltipLines() {
        if (this.tooltipOverrideOpt.isPresent()) {
            return ImmutableList.of(Component.translatable(this.tooltipOverrideOpt.get()));
        } else if (this.isItem) {
            return ImmutableList.of(this.asItem().getDescription());
        } else if (this.isTag) {
            return Services.ITEMS_REGISTRY.getTag(this.asTagKey()).stream().map(Item::getDescription).toList();
        } else {
            return ImmutableList.of();
        }
    }
}
