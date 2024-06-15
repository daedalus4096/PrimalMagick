package com.verdantartifice.primalmagick.common.stats;

import java.util.Optional;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.capabilities.IPlayerStats;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.crafting.IHasExpertise;
import com.verdantartifice.primalmagick.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagick.common.research.ResearchDisciplines;
import com.verdantartifice.primalmagick.common.research.ResearchTier;
import com.verdantartifice.primalmagick.common.research.keys.ResearchDisciplineKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.runes.RuneManager;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Wrapper around {@link StatsManager} specifically for dealing with expertise stats.
 * 
 * @author Daedalus4096
 */
public class ExpertiseManager {
    protected static Optional<Stat> getStat(RegistryAccess registryAccess, ResearchDisciplineKey disciplineKey) {
        ResearchDiscipline discipline = ResearchDisciplines.getDiscipline(registryAccess, disciplineKey);
        return discipline == null ? Optional.empty() : discipline.expertiseStat();
    }
    
    public static Optional<Integer> getThreshold(ResearchDisciplineKey disciplineKey, ResearchTier tier) {
        // TODO Stub
        return Optional.empty();
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
                // Determine the highest research tier represented by any of the runes in this enchantment's definition
                Optional<ResearchTier> maxTierOpt = Optional.empty();
                for (AbstractRequirement<?> req : runeEnchDef.getRunes().stream().map(r -> r.getRequirement()).toList()) {
                    Optional<ResearchTier> tierOpt = req.getResearchTier(player.level().registryAccess());
                    if (maxTierOpt.isEmpty() || (tierOpt.isPresent() && tierOpt.get().compareTo(maxTierOpt.get()) > 0)) {
                        maxTierOpt = tierOpt;
                    }
                }
                
                // Award the expertise based on the research tier of the enchantment, then mark it as having been crafted
                maxTierOpt.ifPresent(tier -> {
                    incrementValue(player, discKey, tier.getDefaultExpertise());
                    if (isBonusEligible(player, enchantment)) {
                        incrementValue(player, discKey, tier.getDefaultBonusExpertise());
                        markCrafted(player, enchantment);
                    }
                });
            });
        }
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
