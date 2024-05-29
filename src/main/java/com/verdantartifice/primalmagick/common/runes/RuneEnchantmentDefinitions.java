package com.verdantartifice.primalmagick.common.runes;

import java.util.function.Function;

import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;

import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Datapack registry for the mod's definitions of rune combination enchantments.
 * 
 * @author Daedalus4096
 */
public class RuneEnchantmentDefinitions {
    public static final ResourceKey<RuneEnchantmentDefinition> ALL_DAMAGE_PROTECTION = createKey(Enchantments.ALL_DAMAGE_PROTECTION);
    public static final ResourceKey<RuneEnchantmentDefinition> FIRE_PROTECTION = createKey(Enchantments.FIRE_PROTECTION);
    public static final ResourceKey<RuneEnchantmentDefinition> FALL_PROTECTION = createKey(Enchantments.FALL_PROTECTION);
    public static final ResourceKey<RuneEnchantmentDefinition> BLAST_PROTECTION = createKey(Enchantments.BLAST_PROTECTION);
    public static final ResourceKey<RuneEnchantmentDefinition> PROJECTILE_PROTECTION = createKey(Enchantments.PROJECTILE_PROTECTION);
    public static final ResourceKey<RuneEnchantmentDefinition> RESPIRATION = createKey(Enchantments.RESPIRATION);
    public static final ResourceKey<RuneEnchantmentDefinition> AQUA_AFFINITY = createKey(Enchantments.AQUA_AFFINITY);
    public static final ResourceKey<RuneEnchantmentDefinition> THORNS = createKey(Enchantments.THORNS);
    public static final ResourceKey<RuneEnchantmentDefinition> DEPTH_STRIDER = createKey(Enchantments.DEPTH_STRIDER);
    public static final ResourceKey<RuneEnchantmentDefinition> FROST_WALKER = createKey(Enchantments.FROST_WALKER);
    public static final ResourceKey<RuneEnchantmentDefinition> SHARPNESS = createKey(Enchantments.SHARPNESS);
    public static final ResourceKey<RuneEnchantmentDefinition> KNOCKBACK = createKey(Enchantments.KNOCKBACK);
    public static final ResourceKey<RuneEnchantmentDefinition> FIRE_ASPECT = createKey(Enchantments.FIRE_ASPECT);
    public static final ResourceKey<RuneEnchantmentDefinition> MOB_LOOTING = createKey(Enchantments.MOB_LOOTING);
    public static final ResourceKey<RuneEnchantmentDefinition> SWEEPING_EDGE = createKey(Enchantments.SWEEPING_EDGE);
    public static final ResourceKey<RuneEnchantmentDefinition> BLOCK_EFFICIENCY = createKey(Enchantments.BLOCK_EFFICIENCY);
    public static final ResourceKey<RuneEnchantmentDefinition> SILK_TOUCH = createKey(Enchantments.SILK_TOUCH);
    public static final ResourceKey<RuneEnchantmentDefinition> UNBREAKING = createKey(Enchantments.UNBREAKING);
    public static final ResourceKey<RuneEnchantmentDefinition> BLOCK_FORTUNE = createKey(Enchantments.BLOCK_FORTUNE);
    public static final ResourceKey<RuneEnchantmentDefinition> POWER_ARROWS = createKey(Enchantments.POWER_ARROWS);
    public static final ResourceKey<RuneEnchantmentDefinition> PUNCH_ARROWS = createKey(Enchantments.PUNCH_ARROWS);
    public static final ResourceKey<RuneEnchantmentDefinition> FLAMING_ARROWS = createKey(Enchantments.FLAMING_ARROWS);
    public static final ResourceKey<RuneEnchantmentDefinition> INFINITY_ARROWS = createKey(Enchantments.INFINITY_ARROWS);
    public static final ResourceKey<RuneEnchantmentDefinition> FISHING_LUCK = createKey(Enchantments.FISHING_LUCK);
    public static final ResourceKey<RuneEnchantmentDefinition> FISHING_SPEED = createKey(Enchantments.FISHING_SPEED);
    public static final ResourceKey<RuneEnchantmentDefinition> LOYALTY = createKey(Enchantments.LOYALTY);
    public static final ResourceKey<RuneEnchantmentDefinition> IMPALING = createKey(Enchantments.IMPALING);
    public static final ResourceKey<RuneEnchantmentDefinition> RIPTIDE = createKey(Enchantments.RIPTIDE);
    public static final ResourceKey<RuneEnchantmentDefinition> CHANNELING = createKey(Enchantments.CHANNELING);
    public static final ResourceKey<RuneEnchantmentDefinition> MULTISHOT = createKey(Enchantments.MULTISHOT);
    public static final ResourceKey<RuneEnchantmentDefinition> QUICK_CHARGE = createKey(Enchantments.QUICK_CHARGE);
    public static final ResourceKey<RuneEnchantmentDefinition> PIERCING = createKey(Enchantments.PIERCING);
    public static final ResourceKey<RuneEnchantmentDefinition> MENDING = createKey(Enchantments.MENDING);
    public static final ResourceKey<RuneEnchantmentDefinition> BANE_OF_ARTHROPODS = createKey(Enchantments.BANE_OF_ARTHROPODS);
    public static final ResourceKey<RuneEnchantmentDefinition> SMITE = createKey(Enchantments.SMITE);
    public static final ResourceKey<RuneEnchantmentDefinition> SOUL_SPEED = createKey(Enchantments.SOUL_SPEED);
    public static final ResourceKey<RuneEnchantmentDefinition> SWIFT_SNEAK = createKey(Enchantments.SWIFT_SNEAK);

    public static final ResourceKey<RuneEnchantmentDefinition> LIFESTEAL = createKey(EnchantmentsPM.LIFESTEAL.get());
    public static final ResourceKey<RuneEnchantmentDefinition> ENDERLOCK = createKey(EnchantmentsPM.ENDERLOCK.get());
    public static final ResourceKey<RuneEnchantmentDefinition> JUDGMENT = createKey(EnchantmentsPM.JUDGMENT.get());
    public static final ResourceKey<RuneEnchantmentDefinition> ENDERPORT = createKey(EnchantmentsPM.ENDERPORT.get());
    public static final ResourceKey<RuneEnchantmentDefinition> REGROWTH = createKey(EnchantmentsPM.REGROWTH.get());
    public static final ResourceKey<RuneEnchantmentDefinition> AEGIS = createKey(EnchantmentsPM.AEGIS.get());
    public static final ResourceKey<RuneEnchantmentDefinition> MANA_EFFICIENCY = createKey(EnchantmentsPM.MANA_EFFICIENCY.get());
    public static final ResourceKey<RuneEnchantmentDefinition> SPELL_POWER = createKey(EnchantmentsPM.SPELL_POWER.get());
    public static final ResourceKey<RuneEnchantmentDefinition> TREASURE = createKey(EnchantmentsPM.TREASURE.get());
    public static final ResourceKey<RuneEnchantmentDefinition> BLUDGEONING = createKey(EnchantmentsPM.BLUDGEONING.get());
    public static final ResourceKey<RuneEnchantmentDefinition> REVERBERATION = createKey(EnchantmentsPM.REVERBERATION.get());
    public static final ResourceKey<RuneEnchantmentDefinition> BOUNTY = createKey(EnchantmentsPM.BOUNTY.get());
    public static final ResourceKey<RuneEnchantmentDefinition> DISINTEGRATION = createKey(EnchantmentsPM.DISINTEGRATION.get());
    public static final ResourceKey<RuneEnchantmentDefinition> VERDANT = createKey(EnchantmentsPM.VERDANT.get());
    public static final ResourceKey<RuneEnchantmentDefinition> LUCKY_STRIKE = createKey(EnchantmentsPM.LUCKY_STRIKE.get());
    public static final ResourceKey<RuneEnchantmentDefinition> RENDING = createKey(EnchantmentsPM.RENDING.get());
    public static final ResourceKey<RuneEnchantmentDefinition> SOULPIERCING = createKey(EnchantmentsPM.SOULPIERCING.get());
    public static final ResourceKey<RuneEnchantmentDefinition> ESSENCE_THIEF = createKey(EnchantmentsPM.ESSENCE_THIEF.get());
    public static final ResourceKey<RuneEnchantmentDefinition> BULWARK = createKey(EnchantmentsPM.BULWARK.get());
    public static final ResourceKey<RuneEnchantmentDefinition> MAGICK_PROTECTION = createKey(EnchantmentsPM.MAGICK_PROTECTION.get());
    public static final ResourceKey<RuneEnchantmentDefinition> GUILLOTINE = createKey(EnchantmentsPM.GUILLOTINE.get());

    public static ResourceKey<RuneEnchantmentDefinition> createKey(Enchantment ench) {
        ResourceLocation enchLoc = ForgeRegistries.ENCHANTMENTS.getKey(ench);
        if (enchLoc == null) {
            throw new IllegalArgumentException("Unknown enchantment while creating rune definition key");
        } else {
            return ResourceKey.create(RegistryKeysPM.RUNE_ENCHANTMENT_DEFINITIONS, enchLoc);
        }
    }
    
    public static void bootstrap(BootstapContext<RuneEnchantmentDefinition> context) {
        // Register rune enchantment definitions for vanilla enchantments
        register(context, ALL_DAMAGE_PROTECTION, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.PROTECT).noun(Rune.SELF).source(Rune.EARTH).build());
        register(context, FIRE_PROTECTION, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.PROTECT).noun(Rune.SELF).source(Rune.INFERNAL).build());
        register(context, FALL_PROTECTION, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.PROJECT).noun(Rune.ITEM).source(Rune.SKY).build());
        register(context, BLAST_PROTECTION, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.PROTECT).noun(Rune.SELF).source(Rune.MOON).build());
        register(context, PROJECTILE_PROTECTION, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.PROTECT).noun(Rune.SELF).source(Rune.SKY).build());
        register(context, RESPIRATION, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.PROJECT).noun(Rune.ITEM).source(Rune.SKY).build());
        register(context, AQUA_AFFINITY, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.PROJECT).noun(Rune.ITEM).source(Rune.SEA).build());
        register(context, THORNS, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.PROJECT).noun(Rune.CREATURE).source(Rune.BLOOD).build());
        register(context, DEPTH_STRIDER, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.PROJECT).noun(Rune.ITEM).source(Rune.SEA).build());
        register(context, FROST_WALKER, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.PROJECT).noun(Rune.AREA).source(Rune.SEA).build());
        register(context, SHARPNESS, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.PROJECT).noun(Rune.ITEM).source(Rune.EARTH).build());
        register(context, KNOCKBACK, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.PROJECT).noun(Rune.CREATURE).source(Rune.EARTH).build());
        register(context, FIRE_ASPECT, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.PROJECT).noun(Rune.ITEM).source(Rune.INFERNAL).build());
        register(context, MOB_LOOTING, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.PROJECT).noun(Rune.ITEM).source(Rune.MOON).build());
        register(context, SWEEPING_EDGE, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.PROJECT).noun(Rune.AREA).source(Rune.SKY).build());
        register(context, BLOCK_EFFICIENCY, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.PROJECT).noun(Rune.ITEM).source(Rune.SKY).build());
        register(context, SILK_TOUCH, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.PROJECT).noun(Rune.ITEM).source(Rune.SEA).build());
        register(context, UNBREAKING, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.PROTECT).noun(Rune.ITEM).source(Rune.EARTH).build());
        register(context, BLOCK_FORTUNE, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.PROJECT).noun(Rune.ITEM).source(Rune.MOON).build());
        register(context, POWER_ARROWS, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.PROJECT).noun(Rune.ITEM).source(Rune.SKY).build());
        register(context, PUNCH_ARROWS, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.PROJECT).noun(Rune.CREATURE).source(Rune.EARTH).build());
        register(context, FLAMING_ARROWS, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.PROJECT).noun(Rune.ITEM).source(Rune.INFERNAL).build());
        register(context, INFINITY_ARROWS, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.SUMMON).noun(Rune.ITEM).source(Rune.SKY).build());
        register(context, FISHING_LUCK, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.PROJECT).noun(Rune.ITEM).source(Rune.MOON).build());
        register(context, FISHING_SPEED, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.SUMMON).noun(Rune.CREATURE).source(Rune.SEA).build());
        register(context, LOYALTY, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.SUMMON).noun(Rune.ITEM).source(Rune.SEA).build());
        register(context, IMPALING, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.PROJECT).noun(Rune.ITEM).source(Rune.SEA).build());
        register(context, RIPTIDE, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.SUMMON).noun(Rune.SELF).source(Rune.SEA).build());
        register(context, CHANNELING, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.SUMMON).noun(Rune.AREA).source(Rune.SKY).build());
        register(context, MULTISHOT, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.SUMMON).noun(Rune.ITEM).source(Rune.SKY).build());
        register(context, QUICK_CHARGE, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.PROJECT).noun(Rune.ITEM).source(Rune.SKY).build());
        register(context, PIERCING, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.PROJECT).noun(Rune.ITEM).source(Rune.EARTH).build());
        register(context, MENDING, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.ABSORB).noun(Rune.ITEM).source(Rune.SUN).build());
        register(context, BANE_OF_ARTHROPODS, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.PROJECT).noun(Rune.ITEM).source(Rune.BLOOD).build());
        register(context, SMITE, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.PROJECT).noun(Rune.CREATURE).source(Rune.SUN).build());
        register(context, SOUL_SPEED, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.PROJECT).noun(Rune.SELF).source(Rune.INFERNAL).build());
        register(context, SWIFT_SNEAK, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.PROJECT).noun(Rune.SELF).source(Rune.SKY).build());

        // Register rune combinations for mod enchantments
        register(context, LIFESTEAL, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.ABSORB).noun(Rune.SELF).source(Rune.BLOOD).build());
        register(context, ENDERLOCK, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.DISPEL).noun(Rune.CREATURE).source(Rune.VOID).build());
        register(context, JUDGMENT, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.PROJECT).noun(Rune.ITEM).source(Rune.HALLOWED).build());
        register(context, ENDERPORT, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.SUMMON).noun(Rune.SELF).source(Rune.VOID).build());
        register(context, REGROWTH, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.ABSORB).noun(Rune.ITEM).source(Rune.HALLOWED).build());
        register(context, AEGIS, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.PROTECT).noun(Rune.SELF).source(Rune.HALLOWED).build());
        register(context, MANA_EFFICIENCY, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.DISPEL).noun(Rune.ITEM).source(Rune.VOID).build());
        register(context, SPELL_POWER, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.PROJECT).noun(Rune.ITEM).source(Rune.VOID).build());
        register(context, TREASURE, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.PROJECT).noun(Rune.ITEM).source(Rune.MOON).build());
        register(context, BLUDGEONING, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.PROJECT).noun(Rune.ITEM).source(Rune.EARTH).build());
        register(context, REVERBERATION, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.PROJECT).noun(Rune.AREA).source(Rune.EARTH)
            .requiredResearch(ResearchEntries.MASTER_RUNEWORKING).requiredResearch(ResearchEntries.PRIMAL_SHOVEL).requiredResearch(ResearchEntries.RUNE_PROJECT)
            .requiredResearch(ResearchEntries.RUNE_AREA).requiredResearch(ResearchEntries.RUNE_EARTH).build());
        register(context, BOUNTY, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.SUMMON).noun(Rune.AREA).source(Rune.SEA)
            .requiredResearch(ResearchEntries.MASTER_RUNEWORKING).requiredResearch(ResearchEntries.PRIMAL_FISHING_ROD).requiredResearch(ResearchEntries.RUNE_SUMMON)
            .requiredResearch(ResearchEntries.RUNE_AREA).requiredResearch(ResearchEntries.RUNE_SEA).build());
        register(context, DISINTEGRATION, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.PROJECT).noun(Rune.AREA).source(Rune.SKY)
            .requiredResearch(ResearchEntries.MASTER_RUNEWORKING).requiredResearch(ResearchEntries.PRIMAL_AXE).requiredResearch(ResearchEntries.RUNE_PROJECT)
            .requiredResearch(ResearchEntries.RUNE_AREA).requiredResearch(ResearchEntries.RUNE_SKY).build());
        register(context, VERDANT, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.SUMMON).noun(Rune.CREATURE).source(Rune.SUN)
            .requiredResearch(ResearchEntries.MASTER_RUNEWORKING).requiredResearch(ResearchEntries.PRIMAL_HOE).requiredResearch(ResearchEntries.RUNE_SUMMON)
            .requiredResearch(ResearchEntries.RUNE_CREATURE).requiredResearch(ResearchEntries.RUNE_SUN).build());
        register(context, LUCKY_STRIKE, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.SUMMON).noun(Rune.ITEM).source(Rune.MOON)
            .requiredResearch(ResearchEntries.MASTER_RUNEWORKING).requiredResearch(ResearchEntries.PRIMAL_PICKAXE).requiredResearch(ResearchEntries.RUNE_SUMMON)
            .requiredResearch(ResearchEntries.RUNE_ITEM).requiredResearch(ResearchEntries.RUNE_MOON).build());
        register(context, RENDING, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.PROJECT).noun(Rune.CREATURE).source(Rune.BLOOD)
            .requiredResearch(ResearchEntries.MASTER_RUNEWORKING).requiredResearch(ResearchEntries.FORBIDDEN_TRIDENT).requiredResearch(ResearchEntries.RUNE_PROJECT)
            .requiredResearch(ResearchEntries.RUNE_CREATURE).requiredResearch(ResearchEntries.RUNE_BLOOD).build());
        register(context, SOULPIERCING, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.ABSORB).noun(Rune.CREATURE).source(Rune.INFERNAL)
            .requiredResearch(ResearchEntries.MASTER_RUNEWORKING).requiredResearch(ResearchEntries.FORBIDDEN_BOW).requiredResearch(ResearchEntries.RUNE_ABSORB)
            .requiredResearch(ResearchEntries.RUNE_CREATURE).requiredResearch(ResearchEntries.RUNE_INFERNAL).build());
        register(context, ESSENCE_THIEF, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.SUMMON).noun(Rune.ITEM).source(Rune.VOID)
            .requiredResearch(ResearchEntries.MASTER_RUNEWORKING).requiredResearch(ResearchEntries.FORBIDDEN_SWORD).requiredResearch(ResearchEntries.RUNE_SUMMON)
            .requiredResearch(ResearchEntries.RUNE_ITEM).requiredResearch(ResearchEntries.RUNE_VOID).build());
        register(context, BULWARK, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.PROTECT).noun(Rune.SELF).source(Rune.HALLOWED)
            .requiredResearch(ResearchEntries.MASTER_RUNEWORKING).requiredResearch(ResearchEntries.SACRED_SHIELD).requiredResearch(ResearchEntries.RUNE_PROTECT)
            .requiredResearch(ResearchEntries.RUNE_SELF).requiredResearch(ResearchEntries.RUNE_HALLOWED).build());
        register(context, MAGICK_PROTECTION, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.PROTECT).noun(Rune.SELF).source(Rune.VOID).build());
        register(context, GUILLOTINE, ench -> RuneEnchantmentDefinition.builder(ench).verb(Rune.DISPEL).noun(Rune.CREATURE).source(Rune.BLOOD).build());
    }
    
    private static Holder.Reference<RuneEnchantmentDefinition> register(BootstapContext<RuneEnchantmentDefinition> context, ResourceKey<RuneEnchantmentDefinition> key, 
            Function<Enchantment, RuneEnchantmentDefinition> supplier) {
        Enchantment ench = ForgeRegistries.ENCHANTMENTS.getValue(key.location());
        if (ench == null) {
            throw new IllegalArgumentException("Unknown enchantment while registering rune definition: " + key.toString());
        } else {
            return context.register(key, supplier.apply(ench));
        }
    }
}
