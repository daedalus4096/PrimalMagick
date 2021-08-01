package com.verdantartifice.primalmagic.common.enchantments;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.wands.IStaff;
import com.verdantartifice.primalmagic.common.wands.IWand;

import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

/**
 * Definition of extended enchantment types for the mod.
 * 
 * @author Daedalus4096
 */
public class EnchantmentTypesPM {
    public static final EnchantmentCategory WAND = EnchantmentCategory.create(PrimalMagic.MODID + ":wand", i -> i instanceof IWand);
    public static final EnchantmentCategory STAFF = EnchantmentCategory.create(PrimalMagic.MODID + ":staff", i -> i instanceof IStaff);
    public static final EnchantmentCategory SHIELD = EnchantmentCategory.create(PrimalMagic.MODID + ":shield", i -> i instanceof ShieldItem);
}
