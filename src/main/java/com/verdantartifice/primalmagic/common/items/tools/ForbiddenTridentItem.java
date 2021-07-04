package com.verdantartifice.primalmagic.common.items.tools;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagic.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagic.common.entities.projectiles.AbstractTridentEntity;
import com.verdantartifice.primalmagic.common.entities.projectiles.ForbiddenTridentEntity;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

/**
 * Definition for a trident made of the magical metal hexium which comes pre-enchanted with Rending.
 * 
 * @author Daedalus4096
 */
public class ForbiddenTridentItem extends AbstractTridentItem {
    public ForbiddenTridentItem(Item.Properties properties) {
        super(ItemTierPM.HEXIUM, properties);
    }

    @Override
    protected AbstractTridentEntity getThrownEntity(World world, LivingEntity thrower, ItemStack stack) {
        return new ForbiddenTridentEntity(world, thrower, stack);
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        EnchantmentHelper.setEnchantments(ImmutableMap.of(EnchantmentsPM.RENDING.get(), 2), stack);
        return stack;
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        // Populate the creative pane with a pre-enchanted trident
        if (this.isInGroup(group)) {
            items.add(this.getDefaultInstance());
        }
    }
}
