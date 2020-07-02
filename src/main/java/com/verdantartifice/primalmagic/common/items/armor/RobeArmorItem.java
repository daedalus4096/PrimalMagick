package com.verdantartifice.primalmagic.common.items.armor;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Item definition for armor that gives a mana discount.  Intended for wizard robes.
 * 
 * @author Daedalus4096
 */
public class RobeArmorItem extends ArmorItem implements IManaDiscountGear {
    protected final int manaDiscount;
    
    public RobeArmorItem(IArmorMaterial material, EquipmentSlotType slot, int manaDiscount, Item.Properties properties) {
        super(material, slot, properties);
        this.manaDiscount = manaDiscount;
    }
    
    @Override
    public int getManaDiscount(ItemStack stack, PlayerEntity player) {
        return this.manaDiscount;
    }
}
