package com.verdantartifice.primalmagick.common.items.armor;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Item definition for armor that gives a mana discount.  Intended for wizard robes.
 * 
 * @author Daedalus4096
 */
public class RobeArmorItem extends ArmorItem implements IManaDiscountGear {
    protected final int manaDiscount;
    
    public RobeArmorItem(ArmorMaterial material, ArmorItem.Type type, int manaDiscount, Item.Properties properties) {
        super(material, type, properties);
        this.manaDiscount = manaDiscount;
    }
    
    @Override
    public int getManaDiscount(ItemStack stack, Player player) {
        return this.manaDiscount;
    }
}
