package com.verdantartifice.primalmagick.common.items.tools;

import java.util.function.Consumer;

import com.verdantartifice.primalmagick.client.renderers.itemstack.PrimaliteShieldISTER;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

/**
 * Definition of a shield item made of the magickal metal primalite.
 * 
 * @author Daedalus4096
 */
public class PrimaliteShieldItem extends AbstractTieredShieldItem {
    protected IClientItemExtensions renderProps;
    
    public PrimaliteShieldItem(Item.Properties properties) {
        super(ItemTierPM.PRIMALITE, properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(this.getRenderProperties());
    }
    
    public IClientItemExtensions getRenderProperties() {
        if (this.renderProps == null) {
            this.renderProps = new IClientItemExtensions() {
                final BlockEntityWithoutLevelRenderer renderer = new PrimaliteShieldISTER();

                @Override
                public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                    return renderer;
                }
            };
        }
        return this.renderProps;
    }
}
