package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.tags.ITagValue;
import com.verdantartifice.primalmagick.common.tags.TagValueNeoforge;
import com.verdantartifice.primalmagick.platform.services.ITagService;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class TagServiceNeoforge implements ITagService {
    @Override
    public ITagValue<Block> block(TagKey<Block> key) {
        return new TagValueNeoforge<>(BuiltInRegistries.BLOCK.getOrCreateTag(key));
    }

    @Override
    public ITagValue<Item> item(TagKey<Item> key) {
        return new TagValueNeoforge<>(BuiltInRegistries.ITEM.getOrCreateTag(key));
    }

    @Override
    public boolean itemTagExists(TagKey<Item> key) {
        return BuiltInRegistries.ITEM.getTagNames().anyMatch(key::equals);
    }
}
