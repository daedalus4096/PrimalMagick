package com.verdantartifice.primalmagick.common.items.misc;

import java.util.function.Consumer;

import com.verdantartifice.primalmagick.client.renderers.itemstack.SpellcraftingAltarISTER;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

/**
 * Definition of a block item for a spellcrafting altar.
 * 
 * @author Daedalus4096
 */
public class SpellcraftingAltarBlockItem extends BlockItem {
    protected IClientItemExtensions renderProps;
    
    public SpellcraftingAltarBlockItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(this.getRenderProperties());
    }
    
    public IClientItemExtensions getRenderProperties() {
        if (this.renderProps == null) {
            this.renderProps = new IClientItemExtensions() {
                final BlockEntityWithoutLevelRenderer renderer = new SpellcraftingAltarISTER();

                @Override
                public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                    return renderer;
                }
            };
        }
        return this.renderProps;
    }
}
