package com.verdantartifice.primalmagick.common.items.tools;

import com.verdantartifice.primalmagick.client.renderers.itemstack.HexiumShieldISTER;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

/**
 * Definition of a shield item made of the magickal metal hexium.
 * 
 * @author Daedalus4096
 */
public class HexiumShieldItem extends AbstractTieredShieldItem {
    public HexiumShieldItem(Item.Properties properties) {
        super(ItemTierPM.HEXIUM, properties);
    }

    public Supplier<BlockEntityWithoutLevelRenderer> getCustomRendererSupplier() {
        return HexiumShieldISTER::new;
    }
}
