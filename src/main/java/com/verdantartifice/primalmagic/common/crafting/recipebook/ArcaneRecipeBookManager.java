package com.verdantartifice.primalmagic.common.crafting.recipebook;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.crafting.IArcaneRecipeBookItem;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Recipe;

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
    
    public static void addRecipes(Collection<Recipe<?>> recipes, ServerPlayer serverPlayer) {
        PrimalMagicCapabilities.getArcaneRecipeBook(serverPlayer).ifPresent(recipeBook -> {
            for (Recipe<?> recipe : recipes) {
                if (recipe instanceof IArcaneRecipeBookItem arbi && !arbi.isArcaneSpecial()) {
                    recipeBook.get().add(recipe);
                    recipeBook.get().addHighlight(recipe);
                }
            }
        });
        scheduleSync(serverPlayer);
    }
    
    public static void removeRecipes(Collection<Recipe<?>> recipes, ServerPlayer serverPlayer) {
        PrimalMagicCapabilities.getArcaneRecipeBook(serverPlayer).ifPresent(recipeBook -> {
            for (Recipe<?> recipe : recipes) {
                recipeBook.get().remove(recipe);
            }
        });
        scheduleSync(serverPlayer);
    }
}
