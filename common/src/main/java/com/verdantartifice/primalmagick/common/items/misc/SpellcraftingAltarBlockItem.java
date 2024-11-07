package com.verdantartifice.primalmagick.common.items.misc;

import com.verdantartifice.primalmagick.client.renderers.itemstack.SpellcraftingAltarISTER;
import com.verdantartifice.primalmagick.common.items.IHasCustomRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

/**
 * Definition of a block item for a spellcrafting altar.
 * 
 * @author Daedalus4096
 */
public abstract class SpellcraftingAltarBlockItem extends BlockItem implements IHasCustomRenderer {
    public SpellcraftingAltarBlockItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    @Override
    public Supplier<BlockEntityWithoutLevelRenderer> getCustomRendererSupplier() {
        return SpellcraftingAltarISTER::new;
    }
}
