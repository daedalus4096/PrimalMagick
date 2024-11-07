package com.verdantartifice.primalmagick.common.items.misc;

import com.verdantartifice.primalmagick.common.items.IHasCustomRendererForge;
import net.minecraft.world.level.block.Block;

public class ManaFontBlockItemForge extends ManaFontBlockItem implements IHasCustomRendererForge {
    public ManaFontBlockItemForge(Block block, Properties properties) {
        super(block, properties);
    }
}
