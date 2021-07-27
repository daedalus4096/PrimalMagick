package com.verdantartifice.primalmagic.common.items.tools;

import java.util.function.Consumer;

import com.verdantartifice.primalmagic.client.renderers.itemstack.HallowsteelShieldISTER;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.IItemRenderProperties;

/**
 * Definition of a shield item made of the magical metal hallowsteel.
 * 
 * @author Daedalus4096
 */
public class HallowsteelShieldItem extends AbstractTieredShieldItem {
    public HallowsteelShieldItem(Item.Properties properties) {
        super(ItemTierPM.HALLOWSTEEL, properties);
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            final BlockEntityWithoutLevelRenderer renderer = new HallowsteelShieldISTER();

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return renderer;
            }
        });
    }
}
