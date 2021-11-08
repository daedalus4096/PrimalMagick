package com.verdantartifice.primalmagick.common.enchantments;

import com.verdantartifice.primalmagick.PrimalMagic;
import com.verdantartifice.primalmagick.common.wands.IStaff;
import com.verdantartifice.primalmagick.common.wands.IWand;

import net.minecraft.world.item.HoeItem;
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
    public static final EnchantmentCategory HOE = EnchantmentCategory.create(PrimalMagic.MODID + ":hoe", i -> i instanceof HoeItem);
}
