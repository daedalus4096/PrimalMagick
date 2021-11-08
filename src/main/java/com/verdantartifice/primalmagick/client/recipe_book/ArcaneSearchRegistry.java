package com.verdantartifice.primalmagick.client.recipe_book;

import com.verdantartifice.primalmagick.client.gui.recipe_book.ArcaneRecipeCollection;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.searchtree.MutableSearchTree;
import net.minecraft.client.searchtree.ReloadableSearchTree;
import net.minecraft.core.Registry;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.TooltipFlag;

@SuppressWarnings("deprecation")
public class ArcaneSearchRegistry implements ResourceManagerReloadListener {
    protected static ArcaneSearchRegistry INSTANCE = null;
    
    protected final MutableSearchTree<ArcaneRecipeCollection> arcaneSearchTree;
    
    public static ArcaneSearchRegistry getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ArcaneSearchRegistry();
        }
        return INSTANCE;
    }
    
    protected ArcaneSearchRegistry() {
        this.arcaneSearchTree = new ReloadableSearchTree<>((arc) -> {
            return arc.getRecipes().stream().flatMap((r) -> {
                return r.getResultItem().getTooltipLines(null, TooltipFlag.Default.NORMAL).stream();
            }).map((c) -> {
                return ChatFormatting.stripFormatting(c.getString()).trim();
            }).filter((s) -> {
                return !s.isEmpty();
            });
        }, (arc) -> {
            return arc.getRecipes().stream().map((r) -> {
                return Registry.ITEM.getKey(r.getResultItem().getItem());
            });
        });
    }

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        if (this.arcaneSearchTree != null) {
            this.arcaneSearchTree.refresh();
        }
    }

    public MutableSearchTree<ArcaneRecipeCollection> getSearchTree() {
        return this.arcaneSearchTree;
    }
    
    public void populate() {
        Minecraft mc = Minecraft.getInstance();
        ClientArcaneRecipeBook book = new ClientArcaneRecipeBook(PrimalMagickCapabilities.getArcaneRecipeBook(mc.player).orElseThrow(() -> new IllegalArgumentException("No arcane recipe book for player")).get());
        this.arcaneSearchTree.clear();
        book.setupCollections(mc.level.getRecipeManager().getRecipes());
        book.getCollections().forEach(this.arcaneSearchTree::add);
        this.arcaneSearchTree.refresh();
    }
}
