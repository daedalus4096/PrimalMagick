package com.verdantartifice.primalmagic.common.items.misc;

import java.util.function.Consumer;

import com.verdantartifice.primalmagic.client.renderers.itemstack.ManaFontISTER;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.IItemRenderProperties;

/**
 * Definition of a block item for a mana font.
 * 
 * @author Daedalus4096
 */
public class ManaFontBlockItem extends BlockItem {
    public ManaFontBlockItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            final BlockEntityWithoutLevelRenderer renderer = new ManaFontISTER();

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return renderer;
            }
        });
    }
}
