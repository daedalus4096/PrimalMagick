package com.verdantartifice.primalmagick.common.items.tools;

import com.verdantartifice.primalmagick.client.renderers.itemstack.PrimaliteShieldISTER;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

/**
 * Definition of a shield item made of the magickal metal primalite.
 * 
 * @author Daedalus4096
 */
public class PrimaliteShieldItem extends AbstractTieredShieldItem {
    public PrimaliteShieldItem(Item.Properties properties) {
        super(ItemTierPM.PRIMALITE, properties);
    }

    public Supplier<BlockEntityWithoutLevelRenderer> getCustomRendererSupplier() {
        return PrimaliteShieldISTER::new;
    }
}
