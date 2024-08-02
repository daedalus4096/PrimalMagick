package com.verdantartifice.primalmagick.common.items.tools;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.items.IEnchantedByDefault;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * Definition of a sword that comes pre-enchanted with Essence Thief.
 * 
 * @author Daedalus4096
 */
public class ForbiddenSwordItem extends SwordItem implements IEnchantedByDefault {
    public ForbiddenSwordItem(Tier tier, Item.Properties builderIn) {
        super(tier, builderIn);
    }

    @Override
    public Map<ResourceKey<Enchantment>, Integer> getDefaultEnchantments() {
        return ImmutableMap.of(EnchantmentsPM.ESSENCE_THIEF, 2);
    }
}
