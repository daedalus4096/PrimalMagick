package com.verdantartifice.primalmagick.common.items.tools;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.items.IEnchantedByDefault;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * Definition of a shovel that comes pre-enchanted with Reverberation.
 * 
 * @author Daedalus4096
 */
public class PrimalShovelItem extends ShovelItem implements IEnchantedByDefault {
    public PrimalShovelItem(Tier tier, Item.Properties builder) {
        super(tier, builder);
    }

    @Override
    public Map<ResourceKey<Enchantment>, Integer> getDefaultEnchantments() {
        return ImmutableMap.of(EnchantmentsPM.REVERBERATION, 2);
    }
}
