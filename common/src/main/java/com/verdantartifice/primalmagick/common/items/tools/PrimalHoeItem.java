package com.verdantartifice.primalmagick.common.items.tools;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.items.IEnchantedByDefault;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Map;

/**
 * Definition of a hoe that comes pre-enchanted with Verdant.
 * 
 * @author Daedalus4096
 */
public class PrimalHoeItem extends HoeItem implements IEnchantedByDefault {
    public PrimalHoeItem(Tier tier, Item.Properties builder) {
        super(tier, builder);
    }

    @Override
    public Map<ResourceKey<Enchantment>, Integer> getDefaultEnchantments() {
        return ImmutableMap.of(EnchantmentsPM.VERDANT, 2);
    }
}
