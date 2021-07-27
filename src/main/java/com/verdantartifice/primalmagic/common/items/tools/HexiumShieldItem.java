package com.verdantartifice.primalmagic.common.items.tools;

import java.util.function.Consumer;

import com.verdantartifice.primalmagic.client.renderers.itemstack.HexiumShieldISTER;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.IItemRenderProperties;

/**
 * Definition of a shield item made of the magical metal hexium.
 * 
 * @author Daedalus4096
 */
public class HexiumShieldItem extends AbstractTieredShieldItem {
    public HexiumShieldItem(Item.Properties properties) {
        super(ItemTierPM.HEXIUM, properties);
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            final BlockEntityWithoutLevelRenderer renderer = new HexiumShieldISTER();

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return renderer;
            }
        });
    }
}
