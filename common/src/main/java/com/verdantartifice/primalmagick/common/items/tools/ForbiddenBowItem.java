package com.verdantartifice.primalmagick.common.items.tools;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.items.IEnchantedByDefault;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Map;

/**
 * Definition for a bow made of the magickal metal hexium which comes pre-enchanted with Soulpiercing.
 * 
 * @author Daedalus4096
 */
public class ForbiddenBowItem extends TieredBowItem implements IEnchantedByDefault {
    public ForbiddenBowItem(Item.Properties properties) {
        super(ItemTierPM.HEXIUM, properties);
    }

    @Override
    public Map<ResourceKey<Enchantment>, Integer> getDefaultEnchantments() {
        return ImmutableMap.of(EnchantmentsPM.SOULPIERCING, 2);
    }
}
