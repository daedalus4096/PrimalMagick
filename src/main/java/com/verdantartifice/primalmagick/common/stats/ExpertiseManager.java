package com.verdantartifice.primalmagick.common.stats;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nullable;

import org.apache.commons.lang3.mutable.MutableInt;

import com.verdantartifice.primalmagick.common.capabilities.IPlayerStats;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.crafting.IHasExpertise;
import com.verdantartifice.primalmagick.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagick.common.research.ResearchDisciplines;
import com.verdantartifice.primalmagick.common.research.ResearchTier;
import com.verdantartifice.primalmagick.common.research.keys.ResearchDisciplineKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.runes.RuneEnchantmentDefinition;
import com.verdantartifice.primalmagick.common.runes.RuneManager;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Wrapper around {@link StatsManager} specifically for dealing with expertise stats.
 * 
 * @author Daedalus4096
 */
public class ExpertiseManager {
    public static Optional<Stat> getStat(RegistryAccess registryAccess, ResearchDisciplineKey disciplineKey) {
        ResearchDiscipline discipline = ResearchDisciplines.getDiscipline(registryAccess, disciplineKey);
        return discipline == null ? Optional.empty() : discipline.expertiseStat();
    }
    
    public static Optional<Integer> getThreshold(Level level, ResearchDisciplineKey disciplineKey, ResearchTier tier) {
        ResourceKey<ResearchDiscipline> rawKey = disciplineKey.getRootKey();
        if (rawKey.equals(ResearchDisciplines.BASICS) || rawKey.equals(ResearchDisciplines.SCANS)) {
            // These disciplines don't track expertise
            return Optional.empty();
        } else if (rawKey.equals(ResearchDisciplines.SORCERY)) {
            // The Sorcery discipline calculates expertise thresholds arbitrarily, out of necessity
            return Optional.of(getThresholdBySpellsCast(tier));
        } else if (rawKey.equals(ResearchDisciplines.RUNEWORKING)) {
            // The Runeworking discipline calculates thresholds based both on traditional crafting and enchantment runescribing
            return Optional.of(getThresholdByDisciplineRecipes(level.registryAccess(), level.getRecipeManager(), disciplineKey, tier) + getThresholdByEnchantmentsRunescribed(level.registryAccess(), tier));
        } else {
            // All other disciplines calculate thresholds based solely on traditional and/or ritual crafting
            return Optional.of(getThresholdByDisciplineRecipes(level.registryAccess(), level.getRecipeManager(), disciplineKey, tier));
        }
    }
    
    protected static int getThresholdBySpellsCast(ResearchTier tier) {
        return switch (tier) {
            case EXPERT -> 50;      // Assume 10 spells at 5 mana each
            case MASTER -> 500;     // Assume 50 spells at 10 mana each
            case SUPREME -> 5000;   // Assume 250 spells at 20 mana each
            default -> 0;
        };
    }
    
    protected static int getThresholdByEnchantmentsRunescribed(RegistryAccess registryAccess, ResearchTier tier) {
        MutableInt retVal = new MutableInt(0);
        ForgeRegistries.ENCHANTMENTS.getValues().forEach(ench -> {
            RuneManager.getRuneDefinition(registryAccess, ench).ifPresent(runeEnchDef -> {
                // Only consider rune enchantment definitions with a research tier lower than the given one
                getRuneEnchantmentTier(registryAccess, runeEnchDef).filter(enchTier -> enchTier.compareTo(tier) < 0).ifPresent(enchTier -> {
                    retVal.add(enchTier.getDefaultExpertise());
                    retVal.add(enchTier.getDefaultBonusExpertise());
                });
            });
        });
        
        // Only require a fraction of the possible enchantments to be runescribed
        float multiplier = switch (tier) {
            case EXPERT -> 0.2F;
            case MASTER -> 0.4F;
            case SUPREME -> 0.6F;
            default -> 0F;
        };

        return (int)(multiplier * (float)retVal.intValue());
    }
    
    protected static int getThresholdByDisciplineRecipes(RegistryAccess registryAccess, RecipeManager recipeManager, ResearchDisciplineKey discKey, ResearchTier tier) {
        Set<ResourceLocation> foundGroups = new HashSet<>();
        MutableInt retVal = new MutableInt(0);
        for (RecipeHolder<?> recipeHolder : recipeManager.getRecipes()) {
            if (recipeHolder.value() instanceof IHasExpertise expRecipe) {
                // Only consider recipes with a research tier lower than the given one
                expRecipe.getResearchTier(registryAccess).filter(recipeTier -> recipeTier.compareTo(tier) < 0).ifPresent(recipeTier -> {
                    expRecipe.getExpertiseGroup().ifPresentOrElse(groupId -> {
                        // If the recipe is part of an expertise group, only take its values into account if that group has not already been processed
                        if (!foundGroups.contains(groupId)) {
                            retVal.add(expRecipe.getExpertiseReward(registryAccess));
                            retVal.add(expRecipe.getBonusExpertiseReward(registryAccess));
                            foundGroups.add(groupId);
                        }
                    }, () -> {
                        // If the recipe is not part of an expertise group, then always contribute its values to the threshold
                        retVal.add(expRecipe.getExpertiseReward(registryAccess));
                        retVal.add(expRecipe.getBonusExpertiseReward(registryAccess));
                    });
                });
            }
        }
        return retVal.intValue();
    }
    
    public static Optional<Integer> getValue(@Nullable Player player, ResearchDisciplineKey disciplineKey) {
        return player == null ? Optional.empty() : getStat(player.level().registryAccess(), disciplineKey).map(stat -> StatsManager.getValue(player, stat));
    }
    
    public static void incrementValue(@Nullable Player player, ResearchDisciplineKey disciplineKey) {
        if (player != null) {
            getStat(player.level().registryAccess(), disciplineKey).ifPresent(stat -> StatsManager.incrementValue(player, stat));
        }
    }
    
    public static void incrementValue(@Nullable Player player, ResearchDisciplineKey disciplineKey, int delta) {
        if (player != null) {
            getStat(player.level().registryAccess(), disciplineKey).ifPresent(stat -> StatsManager.incrementValue(player, stat, delta));
        }
    }
    
    public static void setValue(@Nullable Player player, ResearchDisciplineKey disciplineKey, int value) {
        if (player != null) {
            getStat(player.level().registryAccess(), disciplineKey).ifPresent(stat -> StatsManager.setValue(player, stat, value));
        }
    }
    
    public static void setValueIfMax(@Nullable Player player, ResearchDisciplineKey disciplineKey, int newVal) {
        if (player != null) {
            getStat(player.level().registryAccess(), disciplineKey).ifPresent(stat -> StatsManager.setValueIfMax(player, stat, newVal));
        }
    }
    
    /**
     * Award expertise to the given player for crafting the given expertise-granting recipe at an arcane workbench or
     * other crafting station.
     * 
     * @param player the player to receive the expertise
     * @param recipeHolder a holder for the expertise-granting recipe
     */
    public static void awardExpertise(@Nullable Player player, @Nullable RecipeHolder<?> recipeHolder) {
        if (player != null && recipeHolder != null && recipeHolder.value() instanceof IHasExpertise expRecipe) {
            expRecipe.getResearchDiscipline(player.level().registryAccess(), recipeHolder.id()).ifPresent(discKey -> {
                // Award base expertise for this recipe to the player's discipline score
                incrementValue(player, discKey, expRecipe.getExpertiseReward(player.level().registryAccess()));
                
                // Award bonus expertise for this recipe to the player's discipline score if eligible, then mark it as having been crafted
                if (isBonusEligible(player, recipeHolder)) {
                    incrementValue(player, discKey, expRecipe.getBonusExpertiseReward(player.level().registryAccess()));
                    markCrafted(player, recipeHolder);
                }
            });
        }
    }
    
    protected static boolean isBonusEligible(Player player, RecipeHolder<?> recipeHolder) {
        if (player != null && recipeHolder != null && recipeHolder.value() instanceof IHasExpertise expRecipe) {
            IPlayerStats stats = PrimalMagickCapabilities.getStats(player);
            if (stats != null) {
                return !stats.isRecipeCrafted(recipeHolder.id()) && (expRecipe.getExpertiseGroup().isEmpty() || !stats.isRecipeGroupCrafted(expRecipe.getExpertiseGroup().get()));
            }
        }
        return false;
    }
    
    protected static void markCrafted(Player player, RecipeHolder<?> recipeHolder) {
        if (player != null && recipeHolder != null && recipeHolder.value() instanceof IHasExpertise expRecipe) {
            IPlayerStats stats = PrimalMagickCapabilities.getStats(player);
            if (stats != null) {
                stats.setRecipeCrafted(recipeHolder.id());
                expRecipe.getExpertiseGroup().ifPresent(groupId -> stats.setRecipeGroupCrafted(groupId));
            }
        }
    }
    
    /**
     * Award expertise to the given player for runescribing the given enchantment onto a piece of gear.
     * 
     * @param player the player to receive the expertise
     * @param enchantment the enchantment that was runescribed
     */
    public static void awardExpertise(@Nullable Player player, @Nullable Enchantment enchantment) {
        if (player != null && enchantment != null) {
            ResearchDisciplineKey discKey = new ResearchDisciplineKey(ResearchDisciplines.RUNEWORKING);
            RuneManager.getRuneDefinition(player.level().registryAccess(), enchantment).ifPresent(runeEnchDef -> {
                // Award the expertise based on the research tier of the enchantment, then mark it as having been crafted
                getRuneEnchantmentTier(player.level().registryAccess(), runeEnchDef).ifPresent(tier -> {
                    incrementValue(player, discKey, tier.getDefaultExpertise());
                    if (isBonusEligible(player, enchantment)) {
                        incrementValue(player, discKey, tier.getDefaultBonusExpertise());
                        markCrafted(player, enchantment);
                    }
                });
            });
        }
    }
    
    protected static Optional<ResearchTier> getRuneEnchantmentTier(RegistryAccess registryAccess, RuneEnchantmentDefinition runeEnchDef) {
        // Determine the highest research tier represented by any of the runes in this enchantment's definition
        Optional<ResearchTier> maxTierOpt = Optional.empty();
        for (AbstractRequirement<?> req : runeEnchDef.getRunes().stream().map(r -> r.getRequirement()).toList()) {
            Optional<ResearchTier> tierOpt = req.getResearchTier(registryAccess);
            if (maxTierOpt.isEmpty() || (tierOpt.isPresent() && tierOpt.get().compareTo(maxTierOpt.get()) > 0)) {
                maxTierOpt = tierOpt;
            }
        }
        return maxTierOpt;
    }
    
    protected static boolean isBonusEligible(Player player, Enchantment enchantment) {
        ResourceLocation enchKey = ForgeRegistries.ENCHANTMENTS.getKey(enchantment);
        if (player != null && enchKey != null) {
            IPlayerStats stats = PrimalMagickCapabilities.getStats(player);
            if (stats != null) {
                return !stats.isRuneEnchantmentCrafted(enchKey);
            }
        }
        return false;
    }
    
    protected static void markCrafted(Player player, Enchantment enchantment) {
        ResourceLocation enchKey = ForgeRegistries.ENCHANTMENTS.getKey(enchantment);
        if (player != null && enchKey != null) {
            IPlayerStats stats = PrimalMagickCapabilities.getStats(player);
            if (stats != null) {
                stats.setRuneEnchantmentCrafted(enchKey);
            }
        }
    }
    
    /**
     * Award expertise to the given player for casting the given spell.  Expertise awarded is based on the total
     * mana cost of the spell.
     * 
     * @param player the player to receive the expertise
     * @param spellPackage the spell that was cast
     */
    public static void awardExpertise(@Nullable Player player, @Nullable SpellPackage spellPackage) {
        if (player != null && spellPackage != null) {
            incrementValue(player, new ResearchDisciplineKey(ResearchDisciplines.SORCERY), spellPackage.getManaCost().getManaSize());
        }
    }
}
