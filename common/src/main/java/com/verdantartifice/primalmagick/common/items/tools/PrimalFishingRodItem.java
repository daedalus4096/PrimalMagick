package com.verdantartifice.primalmagick.common.items.tools;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.items.IEnchantedByDefault;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Map;

/**
 * Definition of a fishing rod that comes pre-enchanted with Bounty.
 * 
 * @author Daedalus4096
 */
public class PrimalFishingRodItem extends TieredFishingRodItem implements IEnchantedByDefault {
    public PrimalFishingRodItem(Tier tier, Item.Properties builder) {
        super(tier, builder);
    }

    @Override
    public Map<ResourceKey<Enchantment>, Integer> getDefaultEnchantments() {
        return ImmutableMap.of(EnchantmentsPM.BOUNTY, 2);
    }
}
