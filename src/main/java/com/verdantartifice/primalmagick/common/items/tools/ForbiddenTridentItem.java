package com.verdantartifice.primalmagick.common.items.tools;

import java.util.function.Consumer;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagick.client.renderers.itemstack.ForbiddenTridentISTER;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.entities.projectiles.AbstractTridentEntity;
import com.verdantartifice.primalmagick.common.entities.projectiles.ForbiddenTridentEntity;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

/**
 * Definition for a trident made of the magickal metal hexium which comes pre-enchanted with Rending.
 * 
 * @author Daedalus4096
 */
public class ForbiddenTridentItem extends AbstractTieredTridentItem {
    protected IClientItemExtensions renderProps;
    
    public ForbiddenTridentItem(Item.Properties properties) {
        super(ItemTierPM.HEXIUM, properties);
    }

    @Override
    protected AbstractTridentEntity getThrownEntity(Level world, LivingEntity thrower, ItemStack stack) {
        return new ForbiddenTridentEntity(world, thrower, stack);
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        EnchantmentHelper.setEnchantments(ImmutableMap.of(EnchantmentsPM.RENDING.get(), 2), stack);
        return stack;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(this.getRenderProperties());
    }
    
    public IClientItemExtensions getRenderProperties() {
        if (this.renderProps == null) {
            this.renderProps = new IClientItemExtensions() {
                final BlockEntityWithoutLevelRenderer renderer = new ForbiddenTridentISTER();

                @Override
                public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                    return renderer;
                }
            };
        }
        return this.renderProps;
    }
}
