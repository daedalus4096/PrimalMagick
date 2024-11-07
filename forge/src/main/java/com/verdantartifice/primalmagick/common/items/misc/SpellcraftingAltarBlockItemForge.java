package com.verdantartifice.primalmagick.common.items.misc;

import com.verdantartifice.primalmagick.common.items.IHasCustomRendererForge;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class SpellcraftingAltarBlockItemForge extends SpellcraftingAltarBlockItem implements IHasCustomRendererForge {
    public SpellcraftingAltarBlockItemForge(Block block, Item.Properties properties) {
        super(block, properties);
    }
}
