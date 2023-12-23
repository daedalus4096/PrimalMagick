package com.verdantartifice.primalmagick.client.recipe_book;

import com.verdantartifice.primalmagick.client.gui.recipe_book.ArcaneRecipeCollection;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.searchtree.FullTextSearchTree;
import net.minecraft.client.searchtree.SearchRegistry;
import net.minecraft.client.searchtree.SearchTree;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;

/**
 * Helper class for managing the mod's search tree for arcane recipes.
 * 
 * @author Daedalus4096
 */
public class ArcaneSearchRegistry {
    protected static final SearchRegistry.Key<ArcaneRecipeCollection> ARCANE_RECIPE_COLLECTIONS = new SearchRegistry.Key<>();
    
    @SuppressWarnings("deprecation")
    public static void registerSearchTree() {
        Minecraft.getInstance().getSearchTreeManager().register(ArcaneSearchRegistry.ARCANE_RECIPE_COLLECTIONS, (contents) -> {
            return new FullTextSearchTree<>((arc) -> {
                return arc.getRecipes().stream().flatMap((r) -> {
                    return r.value().getResultItem(arc.registryAccess()).getTooltipLines((Player)null, TooltipFlag.Default.NORMAL).stream();
                }).map((c) -> {
                    return ChatFormatting.stripFormatting(c.getString()).trim();
                }).filter((s) -> {
                    return !s.isEmpty();
                });
            }, (arc) -> {
                return arc.getRecipes().stream().map((r) -> {
                    return BuiltInRegistries.ITEM.getKey(r.value().getResultItem(arc.registryAccess()).getItem());
                });
            }, contents);
        });
    }
    
    public static SearchTree<ArcaneRecipeCollection> getSearchTree() {
        return Minecraft.getInstance().getSearchTree(ARCANE_RECIPE_COLLECTIONS);
    }
    
    public static void populate() {
        Minecraft mc = Minecraft.getInstance();
        ClientArcaneRecipeBook book = new ClientArcaneRecipeBook(PrimalMagickCapabilities.getArcaneRecipeBook(mc.player).orElseThrow(() -> new IllegalArgumentException("No arcane recipe book for player")).get());
        book.setupCollections(mc.level.getRecipeManager().getRecipes(), mc.level.registryAccess());
        mc.getSearchTreeManager().populate(ARCANE_RECIPE_COLLECTIONS, book.getCollections());
    }
}
