package com.verdantartifice.primalmagick.common.items.tools;

import java.util.function.Consumer;

import com.verdantartifice.primalmagick.client.renderers.itemstack.HallowsteelShieldISTER;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

/**
 * Definition of a shield item made of the magickal metal hallowsteel.
 * 
 * @author Daedalus4096
 */
public class HallowsteelShieldItem extends AbstractTieredShieldItem {
    protected IClientItemExtensions renderProps;
    
    public HallowsteelShieldItem(Item.Properties properties) {
        super(ItemTierPM.HALLOWSTEEL, properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(this.getRenderProperties());
    }
    
    public IClientItemExtensions getRenderProperties() {
        if (this.renderProps == null) {
            this.renderProps = new IClientItemExtensions() {
                final BlockEntityWithoutLevelRenderer renderer = new HallowsteelShieldISTER();

                @Override
                public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                    return renderer;
                }
            };
        }
        return this.renderProps;
    }
}
