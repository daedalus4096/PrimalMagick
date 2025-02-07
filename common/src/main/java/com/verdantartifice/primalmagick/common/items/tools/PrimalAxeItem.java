package com.verdantartifice.primalmagick.common.items.tools;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.items.IEnchantedByDefault;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Map;

/**
 * Definition of an axe that comes pre-enchanted with Disintegration.
 * 
 * @author Daedalus4096
 */
public class PrimalAxeItem extends AxeItem implements IEnchantedByDefault {
    public PrimalAxeItem(Tier tier, Item.Properties builder) {
        super(tier, builder);
    }

    @Override
    public Map<ResourceKey<Enchantment>, Integer> getDefaultEnchantments() {
        return ImmutableMap.of(EnchantmentsPM.DISINTEGRATION, 2);
    }
}
