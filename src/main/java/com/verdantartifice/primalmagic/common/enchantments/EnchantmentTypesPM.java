package com.verdantartifice.primalmagic.common.enchantments;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.wands.IStaff;
import com.verdantartifice.primalmagic.common.wands.IWand;

import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.item.ShieldItem;

/**
 * Definition of extended enchantment types for the mod.
 * 
 * @author Daedalus4096
 */
public class EnchantmentTypesPM {
    public static final EnchantmentType WAND = EnchantmentType.create(PrimalMagic.MODID + ":wand", i -> i instanceof IWand);
    public static final EnchantmentType STAFF = EnchantmentType.create(PrimalMagic.MODID + ":staff", i -> i instanceof IStaff);
    public static final EnchantmentType SHIELD = EnchantmentType.create(PrimalMagic.MODID + ":shield", i -> i instanceof ShieldItem);
}
