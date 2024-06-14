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

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeHolder;

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
}
