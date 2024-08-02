package com.verdantartifice.primalmagick.common.items.tools;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.items.IEnchantedByDefault;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * Definition of a pickaxe that comes pre-enchanted with Lucky Strike.
 * 
 * @author Daedalus4096
 */
public class PrimalPickaxeItem extends PickaxeItem implements IEnchantedByDefault {
    public PrimalPickaxeItem(Tier tier, Item.Properties builder) {
        super(tier, builder);
    }

    @Override
    public Map<ResourceKey<Enchantment>, Integer> getDefaultEnchantments() {
        return ImmutableMap.of(EnchantmentsPM.LUCKY_STRIKE, 2);
    }
}
