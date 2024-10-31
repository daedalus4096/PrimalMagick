package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.tags.ITagValue;
import com.verdantartifice.primalmagick.common.tags.TagValueForge;
import com.verdantartifice.primalmagick.platform.services.ITagService;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public class TagServiceForge implements ITagService {
    @Override
    public ITagValue<Block> block(TagKey<Block> key) {
        return new TagValueForge<>(ForgeRegistries.BLOCKS.tags().getTag(key));
    }

    @Override
    public ITagValue<Item> item(TagKey<Item> key) {
        return new TagValueForge<>(ForgeRegistries.ITEMS.tags().getTag(key));
    }
}
