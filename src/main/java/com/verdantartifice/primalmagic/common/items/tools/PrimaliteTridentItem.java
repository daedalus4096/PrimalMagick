package com.verdantartifice.primalmagic.common.items.tools;

import java.util.function.Consumer;

import com.verdantartifice.primalmagic.client.renderers.itemstack.PrimaliteTridentISTER;
import com.verdantartifice.primalmagic.common.entities.projectiles.AbstractTridentEntity;
import com.verdantartifice.primalmagic.common.entities.projectiles.PrimaliteTridentEntity;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.IItemRenderProperties;

/**
 * Definition for a trident made of the magical metal primalite.
 * 
 * @author Daedalus4096
 */
public class PrimaliteTridentItem extends AbstractTieredTridentItem {
    public PrimaliteTridentItem(Item.Properties properties) {
        super(ItemTierPM.PRIMALITE, properties);
    }

    @Override
    protected AbstractTridentEntity getThrownEntity(Level world, LivingEntity thrower, ItemStack stack) {
        return new PrimaliteTridentEntity(world, thrower, stack);
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            final BlockEntityWithoutLevelRenderer renderer = new PrimaliteTridentISTER();

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return renderer;
            }
        });
    }
}
