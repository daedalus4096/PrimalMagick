package com.verdantartifice.primalmagick.common.items.tools;

import com.verdantartifice.primalmagick.client.renderers.itemstack.HallowsteelShieldISTER;
import com.verdantartifice.primalmagick.common.items.IHasCustomRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

/**
 * Definition of a shield item made of the magickal metal hallowsteel.
 * 
 * @author Daedalus4096
 */
public class HallowsteelShieldItem extends AbstractTieredShieldItem implements IHasCustomRenderer {
    public HallowsteelShieldItem(Item.Properties properties) {
        super(ItemTierPM.HALLOWSTEEL, properties);
    }

    @Override
    public Supplier<BlockEntityWithoutLevelRenderer> getCustomRendererSupplier() {
        return HallowsteelShieldISTER::new;
    }
}
