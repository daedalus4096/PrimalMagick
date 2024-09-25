package com.verdantartifice.primalmagick.common.items.tools;

import java.util.function.Consumer;

import com.verdantartifice.primalmagick.client.renderers.itemstack.HexiumShieldISTER;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

/**
 * Definition of a shield item made of the magickal metal hexium.
 * 
 * @author Daedalus4096
 */
public class HexiumShieldItem extends AbstractTieredShieldItem {
    protected IClientItemExtensions renderProps;
    
    public HexiumShieldItem(Item.Properties properties) {
        super(ItemTierPM.HEXIUM, properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(this.getRenderProperties());
    }
    
    public IClientItemExtensions getRenderProperties() {
        if (this.renderProps == null) {
            this.renderProps = new IClientItemExtensions() {
                final BlockEntityWithoutLevelRenderer renderer = new HexiumShieldISTER();

                @Override
                public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                    return renderer;
                }
            };
        }
        return this.renderProps;
    }
}
