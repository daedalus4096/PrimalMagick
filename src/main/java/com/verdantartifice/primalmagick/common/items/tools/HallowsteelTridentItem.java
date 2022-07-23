package com.verdantartifice.primalmagick.common.items.tools;

import java.util.function.Consumer;

import com.verdantartifice.primalmagick.client.renderers.itemstack.HallowsteelTridentISTER;
import com.verdantartifice.primalmagick.common.entities.projectiles.AbstractTridentEntity;
import com.verdantartifice.primalmagick.common.entities.projectiles.HallowsteelTridentEntity;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

/**
 * Definition for a trident made of the magickal metal hallowsteel.
 * 
 * @author Daedalus4096
 */
public class HallowsteelTridentItem extends AbstractTieredTridentItem {
    protected IClientItemExtensions renderProps;
    
    public HallowsteelTridentItem(Item.Properties properties) {
        super(ItemTierPM.HALLOWSTEEL, properties);
    }

    @Override
    protected AbstractTridentEntity getThrownEntity(Level world, LivingEntity thrower, ItemStack stack) {
        return new HallowsteelTridentEntity(world, thrower, stack);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(this.getRenderProperties());
    }
    
    public IClientItemExtensions getRenderProperties() {
        if (this.renderProps == null) {
            this.renderProps = new IClientItemExtensions() {
                final BlockEntityWithoutLevelRenderer renderer = new HallowsteelTridentISTER();

                @Override
                public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                    return renderer;
                }
            };
        }
        return this.renderProps;
    }
}
