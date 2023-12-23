package com.verdantartifice.primalmagick.common.crafting.recipe_book;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.capabilities.IPlayerArcaneRecipeBook;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.crafting.IArcaneRecipeBookItem;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

/**
 * Primary access point for methods related to the arcane recipe book.  This book is like the vanilla
 * recipe book, but it also supports arcane recipes in combination with vanilla ones.
 * 
 * @author Daedalus4096
 */
public class ArcaneRecipeBookManager {
    // Set of unique IDs of players that need their companions synced to their client
    private static final Set<UUID> SYNC_SET = ConcurrentHashMap.newKeySet();

    public static boolean isSyncScheduled(@Nullable Player player) {
        if (player == null) {
            return false;
        } else {
            return SYNC_SET.remove(player.getUUID());
        }
    }
    
    public static void scheduleSync(@Nullable Player player) {
        if (player != null) {
            SYNC_SET.add(player.getUUID());
        }
    }
    
    public static void addRecipes(Collection<RecipeHolder<?>> recipes, ServerPlayer serverPlayer) {
        PrimalMagickCapabilities.getArcaneRecipeBook(serverPlayer).ifPresent(recipeBook -> {
            for (RecipeHolder<?> recipe : recipes) {
                if (recipe.value() instanceof IArcaneRecipeBookItem arbi && !arbi.isArcaneSpecial()) {
                    recipeBook.get().add(recipe);
                    recipeBook.get().addHighlight(recipe);
                }
            }
        });
        scheduleSync(serverPlayer);
    }
    
    public static void removeRecipes(Collection<RecipeHolder<?>> recipes, ServerPlayer serverPlayer) {
        PrimalMagickCapabilities.getArcaneRecipeBook(serverPlayer).ifPresent(recipeBook -> {
            for (RecipeHolder<?> recipe : recipes) {
                recipeBook.get().remove(recipe);
            }
        });
        scheduleSync(serverPlayer);
    }
    
    public static boolean containsRecipe(Player player, RecipeHolder<?> recipe) {
        IPlayerArcaneRecipeBook book = PrimalMagickCapabilities.getArcaneRecipeBook(player).orElse(null);
        return book != null && book.get().contains(recipe);
    }
    
    public static boolean syncRecipesWithResearch(ServerPlayer player) {
        IPlayerArcaneRecipeBook recipeBook = PrimalMagickCapabilities.getArcaneRecipeBook(player).orElse(null);
        IPlayerKnowledge knowledge = PrimalMagickCapabilities.getKnowledge(player).orElse(null);
        if (recipeBook == null || knowledge == null) {
            return false;
        } else {
            recipeBook.get().clear();
            RecipeManager recipeManager = player.level().getRecipeManager();
            Set<ResourceLocation> idsToAdd = new HashSet<>();
            for (ResearchEntry entry : ResearchEntries.getAllEntries()) {
                if (knowledge.isResearchKnown(entry.getKey())) {
                    idsToAdd.addAll(entry.getKnownRecipeIds(player));
                }
            }
            ArcaneRecipeBookManager.addRecipes(idsToAdd.stream().map(id -> recipeManager.byKey(id).orElse(null)).filter(Objects::nonNull).collect(Collectors.toSet()), player);
            return true;
        }
    }
}
