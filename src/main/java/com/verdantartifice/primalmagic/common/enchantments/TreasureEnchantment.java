package com.verdantartifice.primalmagic.common.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.LootBonusEnchantment;

/**
 * Definition of a loot bonus enchantment that can be applied to wands and staves.
 * 
 * @author Daedalus4096
 */
public class TreasureEnchantment extends LootBonusEnchantment {
    public TreasureEnchantment(Enchantment.Rarity rarity, EquipmentSlot... slots) {
        super(rarity, EnchantmentTypesPM.WAND, slots);
    }
}
