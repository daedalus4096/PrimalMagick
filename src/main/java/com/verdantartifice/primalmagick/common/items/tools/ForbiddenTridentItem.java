package com.verdantartifice.primalmagick.common.items.tools;

import java.util.function.Consumer;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagick.client.renderers.itemstack.ForbiddenTridentISTER;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.entities.projectiles.AbstractTridentEntity;
import com.verdantartifice.primalmagick.common.entities.projectiles.ForbiddenTridentEntity;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.IItemRenderProperties;

/**
 * Definition for a trident made of the magickal metal hexium which comes pre-enchanted with Rending.
 * 
 * @author Daedalus4096
 */
public class ForbiddenTridentItem extends AbstractTieredTridentItem {
    protected IItemRenderProperties renderProps;
    
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
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        // Populate the creative pane with a pre-enchanted trident
        if (this.allowedIn(group)) {
            items.add(this.getDefaultInstance());
        }
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(this.getRenderProperties());
    }
    
    public IItemRenderProperties getRenderProperties() {
        if (this.renderProps == null) {
            this.renderProps = new IItemRenderProperties() {
                final BlockEntityWithoutLevelRenderer renderer = new ForbiddenTridentISTER();

                @Override
                public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                    return renderer;
                }
            };
        }
        return this.renderProps;
    }
}
