package com.verdantartifice.primalmagick.platform.services;

import com.verdantartifice.primalmagick.common.tags.ITagValue;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public interface ITagService {
    ITagValue<Block> block(TagKey<Block> key);
    ITagValue<Item> item(TagKey<Item> key);
    boolean itemTagExists(TagKey<Item> key);
}
