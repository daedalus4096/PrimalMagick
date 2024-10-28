package com.verdantartifice.primalmagick.common.stats;

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
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Wrapper around {@link StatsManager} specifically for dealing with expertise stats.
 * 
 * @author Daedalus4096
 */
public class ExpertiseManager {
    private static final Logger LOGGER = LogManager.getLogger();
    
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
        } else {
            // All other disciplines calculate thresholds based solely on traditional and/or ritual crafting
            return Optional.of(getThresholdByDisciplineRecipes(level.registryAccess(), level.getRecipeManager(), disciplineKey, tier));
        }
    }
    
    protected static int getThresholdBySpellsCast(ResearchTier tier) {
        return switch (tier) {
            case EXPERT -> 50;      // Assume 10 spells at 5 mana each
            case MASTER -> 500;     // Assume 50 spells at 10 mana each
            case SUPREME -> 2500;   // Assume 125 spells at 20 mana each
            default -> 0;
        };
    }
    
    protected static int getThresholdByEnchantmentsRunescribed(RegistryAccess registryAccess, ResearchTier tier) {
        MutableInt retVal = new MutableInt(0);
        registryAccess.registryOrThrow(Registries.ENCHANTMENT).holders().forEach(ench -> {
            RuneManager.getRuneDefinition(registryAccess, ench).ifPresent(runeEnchDef -> {
                // Only consider rune enchantment definitions with a research tier lower than the given one
                getRuneEnchantmentTier(registryAccess, runeEnchDef).filter(enchTier -> enchTier.compareTo(tier) < 0).ifPresent(enchTier -> {
                    int reward = enchTier.getDefaultExpertise() + enchTier.getDefaultBonusExpertise();
                    retVal.add(reward);
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

        int total = (int)(multiplier * (float)retVal.intValue());
        LOGGER.debug("Final expertise enchantment value for runeworking tier {}: {} ({} * {})", tier.getSerializedName(), total, retVal.intValue(), multiplier);
        return total;
    }
    
    protected static int getThresholdByDisciplineRecipes(RegistryAccess registryAccess, RecipeManager recipeManager, ResearchDisciplineKey discKey, ResearchTier tier) {
        Set<ResourceLocation> foundGroups = new HashSet<>();
        MutableInt retVal = new MutableInt(0);
        for (RecipeHolder<?> recipeHolder : recipeManager.getRecipes()) {
            if (recipeHolder.value() instanceof IHasExpertise expRecipe) {
                // Only consider recipes with a discipline that matches the given one
                expRecipe.getResearchDiscipline(registryAccess, recipeHolder.id()).filter(recipeDisc -> recipeDisc.equals(discKey)).ifPresent(recipeDisc -> {
                    // Only consider recipes with a research tier lower than the given one
                    expRecipe.getResearchTier(registryAccess).filter(recipeTier -> recipeTier.compareTo(tier) < 0).ifPresent(recipeTier -> {
                        expRecipe.getExpertiseGroup().ifPresentOrElse(groupId -> {
                            // If the recipe is part of an expertise group, only take its values into account if that group has not already been processed
                            if (!foundGroups.contains(groupId)) {
                                int reward = expRecipe.getExpertiseReward(registryAccess) + expRecipe.getBonusExpertiseReward(registryAccess);
                                retVal.add(reward);
                                foundGroups.add(groupId);
                            }
                        }, () -> {
                            // If the recipe is not part of an expertise group, then always contribute its values to the threshold
                            int reward = expRecipe.getExpertiseReward(registryAccess) + expRecipe.getBonusExpertiseReward(registryAccess);
                            retVal.add(reward);
                        });
                    });
                });
            }
        }
        LOGGER.debug("Final expertise threshold value for {} tier {}: {}", discKey.getRootKey().location(), tier.getSerializedName(), retVal.intValue());
        return retVal.intValue();
    }
    
    public static Optional<Integer> getValue(@Nullable Player player, ResearchDisciplineKey disciplineKey) {
        return player == null ? Optional.empty() : getStat(player.level().registryAccess(), disciplineKey).map(stat -> StatsManager.getValue(player, stat));
    }
    
    public static Optional<Integer> getValue(@Nullable Player player, @Nonnull ResourceKey<ResearchDiscipline> rawKey) {
        return getValue(player, new ResearchDisciplineKey(rawKey));
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
    
    public static boolean isBonusEligible(Player player, RecipeHolder<?> recipeHolder) {
        if (player != null && recipeHolder != null && recipeHolder.value() instanceof IHasExpertise expRecipe) {
            return Services.CAPABILITIES.stats(player).map(stats ->
                    !stats.isRecipeCrafted(recipeHolder.id()) &&
                    (expRecipe.getExpertiseGroup().isEmpty() || !stats.isRecipeGroupCrafted(expRecipe.getExpertiseGroup().get()))
            ).orElse(false);
        }
        return false;
    }
    
    protected static void markCrafted(Player player, RecipeHolder<?> recipeHolder) {
        if (player != null && recipeHolder != null && recipeHolder.value() instanceof IHasExpertise expRecipe) {
            Services.CAPABILITIES.stats(player).ifPresent(stats -> {
                stats.setRecipeCrafted(recipeHolder.id());
                expRecipe.getExpertiseGroup().ifPresent(stats::setRecipeGroupCrafted);
            });
        }
    }
    
    /**
     * Award expertise to the given player for runescribing the given enchantment onto a piece of gear.
     * 
     * @param player the player to receive the expertise
     * @param enchantment the enchantment that was runescribed
     */
    public static void awardExpertise(@Nullable Player player, Holder<Enchantment> enchantment) {
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
    
    public static Optional<ResearchTier> getRuneEnchantmentTier(RegistryAccess registryAccess, RuneEnchantmentDefinition runeEnchDef) {
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
    
    public static boolean isBonusEligible(Player player, Holder<Enchantment> enchantment) {
        ResourceLocation enchKey = player.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getKey(enchantment.value());
        return player != null && enchKey != null && Services.CAPABILITIES.stats(player).map(stats -> !stats.isRuneEnchantmentCrafted(enchKey)).orElse(false);
    }
    
    protected static void markCrafted(Player player, Holder<Enchantment> enchantment) {
        ResourceLocation enchKey = player.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getKey(enchantment.value());
        if (player != null && enchKey != null) {
            Services.CAPABILITIES.stats(player).ifPresent(stats -> stats.setRuneEnchantmentCrafted(enchKey));
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
