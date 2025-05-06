package com.verdantartifice.primalmagick.client.recipe_book;

import com.verdantartifice.primalmagick.client.gui.recipe_book.ArcaneRecipeCollection;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.searchtree.FullTextSearchTree;
import net.minecraft.client.searchtree.SearchRegistry;
import net.minecraft.client.searchtree.SearchTree;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * Helper class for managing the mod's search tree for arcane recipes.
 * 
 * @author Daedalus4096
 */
public class ArcaneSearchRegistry {
    protected static final SearchRegistry.Key<ArcaneRecipeCollection> ARCANE_RECIPE_COLLECTIONS = new SearchRegistry.Key<>();

    private static CompletableFuture<Void> searchPopulationFuture = CompletableFuture.completedFuture(null);

    @SuppressWarnings("deprecation")
    public static void registerSearchTree() {
        Minecraft.getInstance().getSearchTreeManager().register(ArcaneSearchRegistry.ARCANE_RECIPE_COLLECTIONS,
            contents -> new FullTextSearchTree<>(
                arc -> arc.getRecipes().stream().flatMap((r) -> r.getResultItem(arc.registryAccess()).getTooltipLines(null, TooltipFlag.Default.NORMAL).stream())
                    .map(c -> ChatFormatting.stripFormatting(c.getString()).trim())
                    .filter(s -> !s.isEmpty()),
                arc -> arc.getRecipes().stream().map((r) -> BuiltInRegistries.ITEM.getKey(r.getResultItem(arc.registryAccess()).getItem())),
                contents
            )
        );
    }
    
    public static SearchTree<ArcaneRecipeCollection> getSearchTree() {
        // Wait for population to be completed before returning the search tree
        ArcaneSearchRegistry.searchPopulationFuture.join();
        return Minecraft.getInstance().getSearchTree(ARCANE_RECIPE_COLLECTIONS);
    }

    public static void populate() {
        Minecraft mc = Minecraft.getInstance();
        ClientArcaneRecipeBook book = new ClientArcaneRecipeBook(PrimalMagickCapabilities.getArcaneRecipeBook(mc.player).orElseThrow(() -> new IllegalArgumentException("No arcane recipe book for player")).get());
        book.setupCollections(mc.level.getRecipeManager().getRecipes(), mc.level.registryAccess());

        // Populate the search tree asynchronously
        CompletableFuture<Void> oldFuture = ArcaneSearchRegistry.searchPopulationFuture;
        ArcaneSearchRegistry.searchPopulationFuture = CompletableFuture.runAsync(() -> {
            mc.getSearchTreeManager().populate(ARCANE_RECIPE_COLLECTIONS, book.getCollections());
        }, Util.backgroundExecutor());
        oldFuture.cancel(true);
    }
}
