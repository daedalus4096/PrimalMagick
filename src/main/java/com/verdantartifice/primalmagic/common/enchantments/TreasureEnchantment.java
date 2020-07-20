package com.verdantartifice.primalmagic.common.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.LootBonusEnchantment;
import net.minecraft.inventory.EquipmentSlotType;

/**
 * Definition of a loot bonus enchantment that can be applied to wands and staves.
 * 
 * @author Daedalus4096
 */
public class TreasureEnchantment extends LootBonusEnchantment {
    public TreasureEnchantment(Enchantment.Rarity rarity, EquipmentSlotType... slots) {
        super(rarity, EnchantmentTypesPM.WAND, slots);
    }
}
