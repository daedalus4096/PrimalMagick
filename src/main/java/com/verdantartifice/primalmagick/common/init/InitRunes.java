package com.verdantartifice.primalmagick.common.init;

import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.runes.Rune;
import com.verdantartifice.primalmagick.common.runes.RuneManager;

import net.minecraft.world.item.enchantment.Enchantments;

/**
 * Point of registration for mod rune enchantment combinations.
 * 
 * @author Daedalus4096
 */
public class InitRunes {
    public static void initRuneEnchantments() {
        // Register rune combinations for vanilla enchantments
        RuneManager.registerRuneEnchantment(Enchantments.ALL_DAMAGE_PROTECTION, Rune.PROTECT, Rune.SELF, Rune.EARTH);
        RuneManager.registerRuneEnchantment(Enchantments.FIRE_PROTECTION, Rune.PROTECT, Rune.SELF, Rune.INFERNAL);
        RuneManager.registerRuneEnchantment(Enchantments.FALL_PROTECTION, Rune.PROJECT, Rune.ITEM, Rune.SKY);
        RuneManager.registerRuneEnchantment(Enchantments.BLAST_PROTECTION, Rune.PROTECT, Rune.SELF, Rune.MOON);
        RuneManager.registerRuneEnchantment(Enchantments.PROJECTILE_PROTECTION, Rune.PROTECT, Rune.SELF, Rune.SKY);
        RuneManager.registerRuneEnchantment(Enchantments.RESPIRATION, Rune.PROJECT, Rune.ITEM, Rune.SKY);
        RuneManager.registerRuneEnchantment(Enchantments.AQUA_AFFINITY, Rune.PROJECT, Rune.ITEM, Rune.SEA);
        RuneManager.registerRuneEnchantment(Enchantments.THORNS, Rune.PROJECT, Rune.CREATURE, Rune.BLOOD);
        RuneManager.registerRuneEnchantment(Enchantments.DEPTH_STRIDER, Rune.PROJECT, Rune.ITEM, Rune.SEA);
        RuneManager.registerRuneEnchantment(Enchantments.FROST_WALKER, Rune.PROJECT, Rune.AREA, Rune.SEA);
        RuneManager.registerRuneEnchantment(Enchantments.SHARPNESS, Rune.PROJECT, Rune.ITEM, Rune.EARTH);
        RuneManager.registerRuneEnchantment(Enchantments.KNOCKBACK, Rune.PROJECT, Rune.CREATURE, Rune.EARTH);
        RuneManager.registerRuneEnchantment(Enchantments.FIRE_ASPECT, Rune.PROJECT, Rune.ITEM, Rune.INFERNAL);
        RuneManager.registerRuneEnchantment(Enchantments.MOB_LOOTING, Rune.PROJECT, Rune.ITEM, Rune.MOON);
        RuneManager.registerRuneEnchantment(Enchantments.SWEEPING_EDGE, Rune.PROJECT, Rune.AREA, Rune.SKY);
        RuneManager.registerRuneEnchantment(Enchantments.BLOCK_EFFICIENCY, Rune.PROJECT, Rune.ITEM, Rune.SKY);
        RuneManager.registerRuneEnchantment(Enchantments.SILK_TOUCH, Rune.PROJECT, Rune.ITEM, Rune.SEA);
        RuneManager.registerRuneEnchantment(Enchantments.UNBREAKING, Rune.PROTECT, Rune.ITEM, Rune.EARTH);
        RuneManager.registerRuneEnchantment(Enchantments.BLOCK_FORTUNE, Rune.PROJECT, Rune.ITEM, Rune.MOON);
        RuneManager.registerRuneEnchantment(Enchantments.POWER_ARROWS, Rune.PROJECT, Rune.ITEM, Rune.SKY);
        RuneManager.registerRuneEnchantment(Enchantments.PUNCH_ARROWS, Rune.PROJECT, Rune.CREATURE, Rune.EARTH);
        RuneManager.registerRuneEnchantment(Enchantments.FLAMING_ARROWS, Rune.PROJECT, Rune.ITEM, Rune.INFERNAL);
        RuneManager.registerRuneEnchantment(Enchantments.INFINITY_ARROWS, Rune.SUMMON, Rune.ITEM, Rune.SKY);
        RuneManager.registerRuneEnchantment(Enchantments.FISHING_LUCK, Rune.PROJECT, Rune.ITEM, Rune.MOON);
        RuneManager.registerRuneEnchantment(Enchantments.FISHING_SPEED, Rune.SUMMON, Rune.CREATURE, Rune.SEA);
        RuneManager.registerRuneEnchantment(Enchantments.LOYALTY, Rune.SUMMON, Rune.ITEM, Rune.SEA);
        RuneManager.registerRuneEnchantment(Enchantments.IMPALING, Rune.PROJECT, Rune.ITEM, Rune.SEA);
        RuneManager.registerRuneEnchantment(Enchantments.RIPTIDE, Rune.SUMMON, Rune.SELF, Rune.SEA);
        RuneManager.registerRuneEnchantment(Enchantments.CHANNELING, Rune.SUMMON, Rune.AREA, Rune.SKY);
        RuneManager.registerRuneEnchantment(Enchantments.MULTISHOT, Rune.SUMMON, Rune.ITEM, Rune.SKY);
        RuneManager.registerRuneEnchantment(Enchantments.QUICK_CHARGE, Rune.PROJECT, Rune.ITEM, Rune.SKY);
        RuneManager.registerRuneEnchantment(Enchantments.PIERCING, Rune.PROJECT, Rune.ITEM, Rune.EARTH);
        RuneManager.registerRuneEnchantment(Enchantments.MENDING, Rune.ABSORB, Rune.ITEM, Rune.SUN);
        
        // Register rune combinations for mod enchantments
        RuneManager.registerRuneEnchantment(EnchantmentsPM.LIFESTEAL.get(), Rune.ABSORB, Rune.SELF, Rune.BLOOD);
        RuneManager.registerRuneEnchantment(EnchantmentsPM.ENDERLOCK.get(), Rune.DISPEL, Rune.CREATURE, Rune.VOID);
        RuneManager.registerRuneEnchantment(EnchantmentsPM.JUDGMENT.get(), Rune.PROJECT, Rune.ITEM, Rune.HALLOWED);
        RuneManager.registerRuneEnchantment(EnchantmentsPM.ENDERPORT.get(), Rune.SUMMON, Rune.SELF, Rune.VOID);
        RuneManager.registerRuneEnchantment(EnchantmentsPM.REGROWTH.get(), Rune.ABSORB, Rune.ITEM, Rune.HALLOWED);
        RuneManager.registerRuneEnchantment(EnchantmentsPM.AEGIS.get(), Rune.PROTECT, Rune.SELF, Rune.HALLOWED);
        RuneManager.registerRuneEnchantment(EnchantmentsPM.MANA_EFFICIENCY.get(), Rune.DISPEL, Rune.ITEM, Rune.VOID);
        RuneManager.registerRuneEnchantment(EnchantmentsPM.SPELL_POWER.get(), Rune.PROJECT, Rune.ITEM, Rune.VOID);
        RuneManager.registerRuneEnchantment(EnchantmentsPM.TREASURE.get(), Rune.PROJECT, Rune.ITEM, Rune.MOON);
        RuneManager.registerRuneEnchantment(EnchantmentsPM.BLUDGEONING.get(), Rune.PROJECT, Rune.ITEM, Rune.EARTH);
        RuneManager.registerRuneEnchantment(EnchantmentsPM.REVERBERATION.get(), Rune.PROJECT, Rune.AREA, Rune.EARTH, CompoundResearchKey.from(true, "MASTER_RUNEWORKING", "PRIMAL_SHOVEL", "RUNE_PROJECT", "RUNE_AREA", "RUNE_EARTH"));
        RuneManager.registerRuneEnchantment(EnchantmentsPM.BOUNTY.get(), Rune.SUMMON, Rune.AREA, Rune.SEA, CompoundResearchKey.from(true, "MASTER_RUNEWORKING", "PRIMAL_FISHING_ROD", "RUNE_SUMMON", "RUNE_AREA", "RUNE_SEA"));
        RuneManager.registerRuneEnchantment(EnchantmentsPM.DISINTEGRATION.get(), Rune.PROJECT, Rune.AREA, Rune.SKY, CompoundResearchKey.from(true, "MASTER_RUNEWORKING", "PRIMAL_AXE", "RUNE_PROJECT", "RUNE_AREA", "RUNE_SKY"));
        RuneManager.registerRuneEnchantment(EnchantmentsPM.VERDANT.get(), Rune.SUMMON, Rune.CREATURE, Rune.SUN, CompoundResearchKey.from(true, "MASTER_RUNEWORKING", "PRIMAL_HOE", "RUNE_SUMMON", "RUNE_CREATURE", "RUNE_SUN"));
        RuneManager.registerRuneEnchantment(EnchantmentsPM.LUCKY_STRIKE.get(), Rune.SUMMON, Rune.ITEM, Rune.MOON, CompoundResearchKey.from(true, "MASTER_RUNEWORKING", "PRIMAL_PICKAXE", "RUNE_SUMMON", "RUNE_ITEM", "RUNE_MOON"));
        RuneManager.registerRuneEnchantment(EnchantmentsPM.RENDING.get(), Rune.PROJECT, Rune.CREATURE, Rune.BLOOD, CompoundResearchKey.from(true, "MASTER_RUNEWORKING", "FORBIDDEN_TRIDENT", "RUNE_PROJECT", "RUNE_CREATURE", "RUNE_BLOOD"));
        RuneManager.registerRuneEnchantment(EnchantmentsPM.SOULPIERCING.get(), Rune.ABSORB, Rune.CREATURE, Rune.INFERNAL, CompoundResearchKey.from(true, "MASTER_RUNEWORKING", "FORBIDDEN_BOW", "RUNE_ABSORB", "RUNE_CREATURE", "RUNE_INFERNAL"));
        RuneManager.registerRuneEnchantment(EnchantmentsPM.ESSENCE_THIEF.get(), Rune.SUMMON, Rune.ITEM, Rune.VOID, CompoundResearchKey.from(true, "MASTER_RUNEWORKING", "FORBIDDEN_SWORD", "RUNE_SUMMON", "RUNE_ITEM", "RUNE_VOID"));
        RuneManager.registerRuneEnchantment(EnchantmentsPM.BULWARK.get(), Rune.PROTECT, Rune.SELF, Rune.HALLOWED, CompoundResearchKey.from(true, "MASTER_RUNEWORKING", "SACRED_SHIELD", "RUNE_PROTECT", "RUNE_SELF", "RUNE_HALLOWED"));
        RuneManager.registerRuneEnchantment(EnchantmentsPM.MAGICK_PROTECTION.get(), Rune.PROTECT, Rune.SELF, Rune.VOID);
    }
}
