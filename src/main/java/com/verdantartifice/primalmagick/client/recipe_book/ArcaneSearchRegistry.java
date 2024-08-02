package com.verdantartifice.primalmagick.client.recipe_book;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import com.verdantartifice.primalmagick.client.gui.recipe_book.ArcaneRecipeCollection;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.SessionSearchTrees;
import net.minecraft.client.searchtree.FullTextSearchTree;
import net.minecraft.client.searchtree.SearchTree;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

/**
 * Helper class for managing the mod's search tree for arcane recipes.
 * 
 * @author Daedalus4096
 */
public class ArcaneSearchRegistry {
    protected static final SessionSearchTrees.Key ARCANE_RECIPE_COLLECTIONS = new SessionSearchTrees.Key();

    private static CompletableFuture<SearchTree<ArcaneRecipeCollection>> recipeSearch = CompletableFuture.completedFuture(SearchTree.empty());

    private static void updateRecipes(ClientArcaneRecipeBook pArcaneRecipeBook, RegistryAccess pRegistries) {
        Minecraft.getInstance().getConnection().searchTrees().register(ARCANE_RECIPE_COLLECTIONS, () -> {
            List<ArcaneRecipeCollection> contents = pArcaneRecipeBook.getCollections();
            Registry<Item> registry = pRegistries.registryOrThrow(Registries.ITEM);
            Item.TooltipContext tooltipContext = Item.TooltipContext.of(pRegistries);
            TooltipFlag tooltipFlag = TooltipFlag.Default.NORMAL;
            CompletableFuture<?> oldFuture = ArcaneSearchRegistry.recipeSearch;

            ArcaneSearchRegistry.recipeSearch = CompletableFuture.supplyAsync(
                    () -> new FullTextSearchTree<>(
                            arc -> getTooltipLines(arc.getRecipes().stream().map(recipe -> recipe.value().getResultItem(pRegistries)), tooltipContext, tooltipFlag),
                            arc -> arc.getRecipes().stream().map(recipe -> registry.getKey(recipe.value().getResultItem(pRegistries).getItem())),
                            contents
                        ),
                    Util.backgroundExecutor());
            
            oldFuture.cancel(true);
        });
    }
    
    public static SearchTree<ArcaneRecipeCollection> getSearchTree() {
        return ArcaneSearchRegistry.recipeSearch.join();
    }
    
    private static Stream<String> getTooltipLines(Stream<ItemStack> pItems, Item.TooltipContext pContext, TooltipFlag pTooltipFlag) {
        return pItems.<Component>flatMap(stack -> stack.getTooltipLines(pContext, null, pTooltipFlag).stream())
            .map(component -> ChatFormatting.stripFormatting(component.getString()).trim())
            .filter(str -> !str.isEmpty());
    }

    public static void populate() {
        Minecraft mc = Minecraft.getInstance();
        ClientArcaneRecipeBook book = new ClientArcaneRecipeBook(PrimalMagickCapabilities.getArcaneRecipeBook(mc.player).orElseThrow(() -> new IllegalArgumentException("No arcane recipe book for player")).get());
        book.setupCollections(mc.level.getRecipeManager().getRecipes(), mc.level.registryAccess());
        updateRecipes(book, mc.level.registryAccess());
    }
}
