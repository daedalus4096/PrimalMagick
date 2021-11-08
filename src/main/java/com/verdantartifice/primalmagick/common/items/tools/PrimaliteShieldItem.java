package com.verdantartifice.primalmagick.common.items.tools;

import java.util.function.Consumer;

import com.verdantartifice.primalmagick.client.renderers.itemstack.PrimaliteShieldISTER;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.IItemRenderProperties;

/**
 * Definition of a shield item made of the magical metal primalite.
 * 
 * @author Daedalus4096
 */
public class PrimaliteShieldItem extends AbstractTieredShieldItem {
    protected IItemRenderProperties renderProps;
    
    public PrimaliteShieldItem(Item.Properties properties) {
        super(ItemTierPM.PRIMALITE, properties);
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(this.getRenderProperties());
    }
    
    public IItemRenderProperties getRenderProperties() {
        if (this.renderProps == null) {
            this.renderProps = new IItemRenderProperties() {
                final BlockEntityWithoutLevelRenderer renderer = new PrimaliteShieldISTER();

                @Override
                public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                    return renderer;
                }
            };
        }
        return this.renderProps;
    }
}
