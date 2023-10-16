package com.verdantartifice.primalmagick.datagen.runes;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.runes.Rune;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.registries.ForgeRegistries;

public class RuneEnchantmentProvider implements DataProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    protected final PackOutput packOutput;
    protected final Set<ResourceLocation> registeredEnchantments = new HashSet<>();

    public RuneEnchantmentProvider(PackOutput packOutput) {
        this.packOutput = packOutput;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        ImmutableList.Builder<CompletableFuture<?>> futuresBuilder = new ImmutableList.Builder<>();
        Map<ResourceLocation, IFinishedRuneEnchantment> map = new HashMap<>();
        this.registerEnchantments((enchantment) -> {
            if (map.put(enchantment.getId(), enchantment) != null) {
                LOGGER.debug("Duplicate rune enchantment definition in data generation: {}", enchantment.getId().toString());
            }
            this.registeredEnchantments.add(enchantment.getId());
        });
        map.entrySet().forEach(entry -> {
            futuresBuilder.add(DataProvider.saveStable(cache, entry.getValue().getEnchantmentJson(), this.getPath(this.packOutput, entry.getKey())));
        });
        this.checkExpectations();
        return CompletableFuture.allOf(futuresBuilder.build().toArray(CompletableFuture[]::new));
    }

    private Path getPath(PackOutput output, ResourceLocation entryLoc) {
        return output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(entryLoc.getNamespace()).resolve("rune_enchantments").resolve(entryLoc.getPath() + ".json");
    }
    
    private void registerEmptyEnchantment(Enchantment ench) {
        this.registeredEnchantments.add(ForgeRegistries.ENCHANTMENTS.getKey(ench));
    }
    
    private void checkExpectations() {
        // Collect all the resource locations for the enchantments defined
        Set<ResourceLocation> enchantments = new HashSet<>(ForgeRegistries.ENCHANTMENTS.getKeys());
        
        // Warn for each enchantment that didn't have a rune combinations (or lack thereof) registered
        enchantments.removeAll(this.registeredEnchantments);
        enchantments.forEach(key -> LOGGER.warn("Missing enchantment rune combination for {}", key.toString()));
    }
    
    protected void registerEnchantments(Consumer<IFinishedRuneEnchantment> consumer) {
        // Register rune combinations for vanilla enchantments
        RuneEnchantmentBuilder.enchantment(Enchantments.ALL_DAMAGE_PROTECTION).verb(Rune.PROTECT).noun(Rune.SELF).source(Rune.EARTH).build(consumer);
        RuneEnchantmentBuilder.enchantment(Enchantments.FIRE_PROTECTION).verb(Rune.PROTECT).noun(Rune.SELF).source(Rune.INFERNAL).build(consumer);
        RuneEnchantmentBuilder.enchantment(Enchantments.FALL_PROTECTION).verb(Rune.PROJECT).noun(Rune.ITEM).source(Rune.SKY).build(consumer);
        RuneEnchantmentBuilder.enchantment(Enchantments.BLAST_PROTECTION).verb(Rune.PROTECT).noun(Rune.SELF).source(Rune.MOON).build(consumer);
        RuneEnchantmentBuilder.enchantment(Enchantments.PROJECTILE_PROTECTION).verb(Rune.PROTECT).noun(Rune.SELF).source(Rune.SKY).build(consumer);
        RuneEnchantmentBuilder.enchantment(Enchantments.RESPIRATION).verb(Rune.PROJECT).noun(Rune.ITEM).source(Rune.SKY).build(consumer);
        RuneEnchantmentBuilder.enchantment(Enchantments.AQUA_AFFINITY).verb(Rune.PROJECT).noun(Rune.ITEM).source(Rune.SEA).build(consumer);
        RuneEnchantmentBuilder.enchantment(Enchantments.THORNS).verb(Rune.PROJECT).noun(Rune.CREATURE).source(Rune.BLOOD).build(consumer);
        RuneEnchantmentBuilder.enchantment(Enchantments.DEPTH_STRIDER).verb(Rune.PROJECT).noun(Rune.ITEM).source(Rune.SEA).build(consumer);
        RuneEnchantmentBuilder.enchantment(Enchantments.FROST_WALKER).verb(Rune.PROJECT).noun(Rune.AREA).source(Rune.SEA).build(consumer);
        RuneEnchantmentBuilder.enchantment(Enchantments.SHARPNESS).verb(Rune.PROJECT).noun(Rune.ITEM).source(Rune.EARTH).build(consumer);
        RuneEnchantmentBuilder.enchantment(Enchantments.KNOCKBACK).verb(Rune.PROJECT).noun(Rune.CREATURE).source(Rune.EARTH).build(consumer);
        RuneEnchantmentBuilder.enchantment(Enchantments.FIRE_ASPECT).verb(Rune.PROJECT).noun(Rune.ITEM).source(Rune.INFERNAL).build(consumer);
        RuneEnchantmentBuilder.enchantment(Enchantments.MOB_LOOTING).verb(Rune.PROJECT).noun(Rune.ITEM).source(Rune.MOON).build(consumer);
        RuneEnchantmentBuilder.enchantment(Enchantments.SWEEPING_EDGE).verb(Rune.PROJECT).noun(Rune.AREA).source(Rune.SKY).build(consumer);
        RuneEnchantmentBuilder.enchantment(Enchantments.BLOCK_EFFICIENCY).verb(Rune.PROJECT).noun(Rune.ITEM).source(Rune.SKY).build(consumer);
        RuneEnchantmentBuilder.enchantment(Enchantments.SILK_TOUCH).verb(Rune.PROJECT).noun(Rune.ITEM).source(Rune.SEA).build(consumer);
        RuneEnchantmentBuilder.enchantment(Enchantments.UNBREAKING).verb(Rune.PROTECT).noun(Rune.ITEM).source(Rune.EARTH).build(consumer);
        RuneEnchantmentBuilder.enchantment(Enchantments.BLOCK_FORTUNE).verb(Rune.PROJECT).noun(Rune.ITEM).source(Rune.MOON).build(consumer);
        RuneEnchantmentBuilder.enchantment(Enchantments.POWER_ARROWS).verb(Rune.PROJECT).noun(Rune.ITEM).source(Rune.SKY).build(consumer);
        RuneEnchantmentBuilder.enchantment(Enchantments.PUNCH_ARROWS).verb(Rune.PROJECT).noun(Rune.CREATURE).source(Rune.EARTH).build(consumer);
        RuneEnchantmentBuilder.enchantment(Enchantments.FLAMING_ARROWS).verb(Rune.PROJECT).noun(Rune.ITEM).source(Rune.INFERNAL).build(consumer);
        RuneEnchantmentBuilder.enchantment(Enchantments.INFINITY_ARROWS).verb(Rune.SUMMON).noun(Rune.ITEM).source(Rune.SKY).build(consumer);
        RuneEnchantmentBuilder.enchantment(Enchantments.FISHING_LUCK).verb(Rune.PROJECT).noun(Rune.ITEM).source(Rune.MOON).build(consumer);
        RuneEnchantmentBuilder.enchantment(Enchantments.FISHING_SPEED).verb(Rune.SUMMON).noun(Rune.CREATURE).source(Rune.SEA).build(consumer);
        RuneEnchantmentBuilder.enchantment(Enchantments.LOYALTY).verb(Rune.SUMMON).noun(Rune.ITEM).source(Rune.SEA).build(consumer);
        RuneEnchantmentBuilder.enchantment(Enchantments.IMPALING).verb(Rune.PROJECT).noun(Rune.ITEM).source(Rune.SEA).build(consumer);
        RuneEnchantmentBuilder.enchantment(Enchantments.RIPTIDE).verb(Rune.SUMMON).noun(Rune.SELF).source(Rune.SEA).build(consumer);
        RuneEnchantmentBuilder.enchantment(Enchantments.CHANNELING).verb(Rune.SUMMON).noun(Rune.AREA).source(Rune.SKY).build(consumer);
        RuneEnchantmentBuilder.enchantment(Enchantments.MULTISHOT).verb(Rune.SUMMON).noun(Rune.ITEM).source(Rune.SKY).build(consumer);
        RuneEnchantmentBuilder.enchantment(Enchantments.QUICK_CHARGE).verb(Rune.PROJECT).noun(Rune.ITEM).source(Rune.SKY).build(consumer);
        RuneEnchantmentBuilder.enchantment(Enchantments.PIERCING).verb(Rune.PROJECT).noun(Rune.ITEM).source(Rune.EARTH).build(consumer);
        RuneEnchantmentBuilder.enchantment(Enchantments.MENDING).verb(Rune.ABSORB).noun(Rune.ITEM).source(Rune.SUN).build(consumer);
        RuneEnchantmentBuilder.enchantment(Enchantments.BANE_OF_ARTHROPODS).verb(Rune.PROJECT).noun(Rune.ITEM).source(Rune.BLOOD).build(consumer);
        RuneEnchantmentBuilder.enchantment(Enchantments.SMITE).verb(Rune.PROJECT).noun(Rune.CREATURE).source(Rune.SUN).build(consumer);
        RuneEnchantmentBuilder.enchantment(Enchantments.SOUL_SPEED).verb(Rune.PROJECT).noun(Rune.SELF).source(Rune.INFERNAL).build(consumer);
        RuneEnchantmentBuilder.enchantment(Enchantments.SWIFT_SNEAK).verb(Rune.PROJECT).noun(Rune.SELF).source(Rune.SKY).build(consumer);
        
        // Register rune combinations for mod enchantments
        RuneEnchantmentBuilder.enchantment(EnchantmentsPM.LIFESTEAL.get()).verb(Rune.ABSORB).noun(Rune.SELF).source(Rune.BLOOD).build(consumer);
        RuneEnchantmentBuilder.enchantment(EnchantmentsPM.ENDERLOCK.get()).verb(Rune.DISPEL).noun(Rune.CREATURE).source(Rune.VOID).build(consumer);
        RuneEnchantmentBuilder.enchantment(EnchantmentsPM.JUDGMENT.get()).verb(Rune.PROJECT).noun(Rune.ITEM).source(Rune.HALLOWED).build(consumer);
        RuneEnchantmentBuilder.enchantment(EnchantmentsPM.ENDERPORT.get()).verb(Rune.SUMMON).noun(Rune.SELF).source(Rune.VOID).build(consumer);
        RuneEnchantmentBuilder.enchantment(EnchantmentsPM.REGROWTH.get()).verb(Rune.ABSORB).noun(Rune.ITEM).source(Rune.HALLOWED).build(consumer);
        RuneEnchantmentBuilder.enchantment(EnchantmentsPM.AEGIS.get()).verb(Rune.PROTECT).noun(Rune.SELF).source(Rune.HALLOWED).build(consumer);
        RuneEnchantmentBuilder.enchantment(EnchantmentsPM.MANA_EFFICIENCY.get()).verb(Rune.DISPEL).noun(Rune.ITEM).source(Rune.VOID).build(consumer);
        RuneEnchantmentBuilder.enchantment(EnchantmentsPM.SPELL_POWER.get()).verb(Rune.PROJECT).noun(Rune.ITEM).source(Rune.VOID).build(consumer);
        RuneEnchantmentBuilder.enchantment(EnchantmentsPM.TREASURE.get()).verb(Rune.PROJECT).noun(Rune.ITEM).source(Rune.MOON).build(consumer);
        RuneEnchantmentBuilder.enchantment(EnchantmentsPM.BLUDGEONING.get()).verb(Rune.PROJECT).noun(Rune.ITEM).source(Rune.EARTH).build(consumer);
        RuneEnchantmentBuilder.enchantment(EnchantmentsPM.REVERBERATION.get()).verb(Rune.PROJECT).noun(Rune.AREA).source(Rune.EARTH)
            .requiredResearch(CompoundResearchKey.from(true, "MASTER_RUNEWORKING", "PRIMAL_SHOVEL", "RUNE_PROJECT", "RUNE_AREA", "RUNE_EARTH")).build(consumer);
        RuneEnchantmentBuilder.enchantment(EnchantmentsPM.BOUNTY.get()).verb(Rune.SUMMON).noun(Rune.AREA).source(Rune.SEA)
            .requiredResearch(CompoundResearchKey.from(true, "MASTER_RUNEWORKING", "PRIMAL_FISHING_ROD", "RUNE_SUMMON", "RUNE_AREA", "RUNE_SEA")).build(consumer);
        RuneEnchantmentBuilder.enchantment(EnchantmentsPM.DISINTEGRATION.get()).verb(Rune.PROJECT).noun(Rune.AREA).source(Rune.SKY)
            .requiredResearch(CompoundResearchKey.from(true, "MASTER_RUNEWORKING", "PRIMAL_AXE", "RUNE_PROJECT", "RUNE_AREA", "RUNE_SKY")).build(consumer);
        RuneEnchantmentBuilder.enchantment(EnchantmentsPM.VERDANT.get()).verb(Rune.SUMMON).noun(Rune.CREATURE).source(Rune.SUN)
            .requiredResearch(CompoundResearchKey.from(true, "MASTER_RUNEWORKING", "PRIMAL_HOE", "RUNE_SUMMON", "RUNE_CREATURE", "RUNE_SUN")).build(consumer);
        RuneEnchantmentBuilder.enchantment(EnchantmentsPM.LUCKY_STRIKE.get()).verb(Rune.SUMMON).noun(Rune.ITEM).source(Rune.MOON)
            .requiredResearch(CompoundResearchKey.from(true, "MASTER_RUNEWORKING", "PRIMAL_PICKAXE", "RUNE_SUMMON", "RUNE_ITEM", "RUNE_MOON")).build(consumer);
        RuneEnchantmentBuilder.enchantment(EnchantmentsPM.RENDING.get()).verb(Rune.PROJECT).noun(Rune.CREATURE).source(Rune.BLOOD)
            .requiredResearch(CompoundResearchKey.from(true, "MASTER_RUNEWORKING", "FORBIDDEN_TRIDENT", "RUNE_PROJECT", "RUNE_CREATURE", "RUNE_BLOOD")).build(consumer);
        RuneEnchantmentBuilder.enchantment(EnchantmentsPM.SOULPIERCING.get()).verb(Rune.ABSORB).noun(Rune.CREATURE).source(Rune.INFERNAL)
            .requiredResearch(CompoundResearchKey.from(true, "MASTER_RUNEWORKING", "FORBIDDEN_BOW", "RUNE_ABSORB", "RUNE_CREATURE", "RUNE_INFERNAL")).build(consumer);
        RuneEnchantmentBuilder.enchantment(EnchantmentsPM.ESSENCE_THIEF.get()).verb(Rune.SUMMON).noun(Rune.ITEM).source(Rune.VOID)
            .requiredResearch(CompoundResearchKey.from(true, "MASTER_RUNEWORKING", "FORBIDDEN_SWORD", "RUNE_SUMMON", "RUNE_ITEM", "RUNE_VOID")).build(consumer);
        RuneEnchantmentBuilder.enchantment(EnchantmentsPM.BULWARK.get()).verb(Rune.PROTECT).noun(Rune.SELF).source(Rune.HALLOWED)
            .requiredResearch(CompoundResearchKey.from(true, "MASTER_RUNEWORKING", "SACRED_SHIELD", "RUNE_PROTECT", "RUNE_SELF", "RUNE_HALLOWED")).build(consumer);
        RuneEnchantmentBuilder.enchantment(EnchantmentsPM.MAGICK_PROTECTION.get()).verb(Rune.PROTECT).noun(Rune.SELF).source(Rune.VOID).build(consumer);
        RuneEnchantmentBuilder.enchantment(EnchantmentsPM.GUILLOTINE.get()).verb(Rune.DISPEL).noun(Rune.CREATURE).source(Rune.BLOOD).build(consumer);
        
        // Register lack of rune combinations for unsupported enchantments
        this.registerEmptyEnchantment(Enchantments.BINDING_CURSE);
        this.registerEmptyEnchantment(Enchantments.VANISHING_CURSE);
    }

    @Override
    public String getName() {
        return "Primal Magick Rune Enchantment Definitions";
    }
}
