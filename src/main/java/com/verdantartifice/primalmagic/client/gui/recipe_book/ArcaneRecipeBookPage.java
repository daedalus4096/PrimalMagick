package com.verdantartifice.primalmagic.client.gui.recipe_book;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.crafting.recipe_book.ArcaneRecipeBook;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.StateSwitchingButton;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.client.gui.screens.recipebook.RecipeShownListener;
import net.minecraft.stats.RecipeBook;
import net.minecraft.world.item.crafting.Recipe;

/**
 * GUI page for the arcane recipe book.
 * 
 * @author Daedalus4096
 */
public class ArcaneRecipeBookPage {
    public static final int ITEMS_PER_PAGE = 20;
    
    protected final List<ArcaneRecipeButton> buttons = new ArrayList<>(ITEMS_PER_PAGE);
    protected final OverlayArcaneRecipeComponent overlay = new OverlayArcaneRecipeComponent();
    protected final List<RecipeShownListener> showListeners = new ArrayList<>();
    @Nullable
    protected ArcaneRecipeButton hoveredButton;
    protected Minecraft mc;
    protected List<RecipeCollection> recipeCollections = ImmutableList.of();
    protected StateSwitchingButton forwardButton;
    protected StateSwitchingButton backButton;
    protected int totalPages;
    protected int currentPage;
    protected RecipeBook vanillaBook;
    protected ArcaneRecipeBook arcaneBook;
    @Nullable
    protected Recipe<?> lastClickedRecipe;
    @Nullable
    protected RecipeCollection lastClickedRecipeCollection;

    public ArcaneRecipeBookPage() {
        for (int index = 0; index < ITEMS_PER_PAGE; index++) {
            this.buttons.add(new ArcaneRecipeButton());
        }
    }
    
    public void init(Minecraft mc, int xPos, int yPos) {
        this.mc = mc;
        this.vanillaBook = mc.player.getRecipeBook();
        this.arcaneBook = PrimalMagicCapabilities.getArcaneRecipeBook(mc.player).orElseThrow(() -> new IllegalArgumentException("No arcane recipe book found for player")).get();
        
        for (int index = 0; index < this.buttons.size(); index++) {
            this.buttons.get(index).setPosition(xPos + 11 + 25 * (index % 5), yPos + 31 + 25 * (index / 5));
        }
        
        this.forwardButton = new StateSwitchingButton(xPos + 93, yPos + 137, 12, 17, false);
        this.forwardButton.initTextureValues(1, 208, 13, 18, ArcaneRecipeBookComponent.RECIPE_BOOK_LOCATION);
        this.backButton = new StateSwitchingButton(xPos + 38, yPos + 137, 12, 17, true);
        this.backButton.initTextureValues(1, 208, 13, 18, ArcaneRecipeBookComponent.RECIPE_BOOK_LOCATION);
    }
}
