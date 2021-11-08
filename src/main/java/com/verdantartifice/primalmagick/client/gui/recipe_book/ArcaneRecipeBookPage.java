package com.verdantartifice.primalmagick.client.gui.recipe_book;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.common.crafting.recipe_book.ArcaneRecipeBook;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.StateSwitchingButton;
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
    protected List<ArcaneRecipeCollection> recipeCollections = ImmutableList.of();
    protected StateSwitchingButton forwardButton;
    protected StateSwitchingButton backButton;
    protected int totalPages;
    protected int currentPage;
    protected RecipeBook vanillaBook;
    protected ArcaneRecipeBook arcaneBook;
    @Nullable
    protected Recipe<?> lastClickedRecipe;
    @Nullable
    protected ArcaneRecipeCollection lastClickedRecipeCollection;

    public ArcaneRecipeBookPage() {
        for (int index = 0; index < ITEMS_PER_PAGE; index++) {
            this.buttons.add(new ArcaneRecipeButton());
        }
    }
    
    public void init(Minecraft mc, int xPos, int yPos, ArcaneRecipeBook arcaneBook) {
        this.mc = mc;
        this.vanillaBook = mc.player.getRecipeBook();
        this.arcaneBook = arcaneBook;
        
        for (int index = 0; index < this.buttons.size(); index++) {
            this.buttons.get(index).setPosition(xPos + 11 + 25 * (index % 5), yPos + 31 + 25 * (index / 5));
        }
        
        this.forwardButton = new StateSwitchingButton(xPos + 93, yPos + 137, 12, 17, false);
        this.forwardButton.initTextureValues(1, 208, 13, 18, ArcaneRecipeBookComponent.RECIPE_BOOK_LOCATION);
        this.backButton = new StateSwitchingButton(xPos + 38, yPos + 137, 12, 17, true);
        this.backButton.initTextureValues(1, 208, 13, 18, ArcaneRecipeBookComponent.RECIPE_BOOK_LOCATION);
    }
    
    public void addListener(ArcaneRecipeBookComponent component) {
        this.showListeners.remove(component);
        this.showListeners.add(component);
    }
    
    public void updateCollections(List<ArcaneRecipeCollection> collectionList, boolean resetPage) {
        this.recipeCollections = collectionList;
        this.totalPages = (int)Math.ceil((double)collectionList.size() / (double)ITEMS_PER_PAGE);
        if (this.totalPages <= this.currentPage || resetPage) {
            this.currentPage = 0;
        }
        this.updateButtonsForPage();
    }
    
    protected void updateButtonsForPage() {
        int recipesPast = ITEMS_PER_PAGE * this.currentPage;
        for (int index = 0; index < this.buttons.size(); index++) {
            ArcaneRecipeButton button = this.buttons.get(index);
            if (recipesPast + index < this.recipeCollections.size()) {
                ArcaneRecipeCollection collection = this.recipeCollections.get(recipesPast + index);
                button.init(collection, this);
                button.visible = true;
            } else {
                button.visible = false;
            }
        }
        this.updateArrowButtons();
    }
    
    protected void updateArrowButtons() {
        this.forwardButton.visible = this.totalPages > 1 && this.currentPage < this.totalPages - 1;
        this.backButton.visible = this.totalPages > 1 && this.currentPage > 0;
    }
    
    public void render(PoseStack poseStack, int parentX, int parentY, int mouseX, int mouseY, float partialTicks) {
        if (this.totalPages > 1) {
            String str = (this.currentPage + 1) + "/" + this.totalPages;
            int width = this.mc.font.width(str);
            this.mc.font.draw(poseStack, str, (float)(parentX - width / 2 + 73), (float)(parentY + 141), -1);
        }
        
        this.hoveredButton = null;
        
        for (ArcaneRecipeButton button : this.buttons) {
            button.render(poseStack, mouseX, mouseY, partialTicks);
            if (button.visible && button.isHovered()) {
                this.hoveredButton = button;
            }
        }
        
        this.backButton.render(poseStack, mouseX, mouseY, partialTicks);
        this.forwardButton.render(poseStack, mouseX, mouseY, partialTicks);
        this.overlay.render(poseStack, mouseX, mouseY, partialTicks);
    }
    
    public void renderTooltip(PoseStack poseStack, int mouseX, int mouseY) {
        if (this.mc.screen != null && this.hoveredButton != null && !this.overlay.isVisible()) {
            this.mc.screen.renderComponentTooltip(poseStack, this.hoveredButton.getTooltipText(this.mc.screen), mouseX, mouseY, this.hoveredButton.getRecipe().getResultItem());
        }
    }
    
    @Nullable
    public Recipe<?> getLastClickedRecipe() {
        return this.lastClickedRecipe;
    }
    
    @Nullable
    public ArcaneRecipeCollection getLastClickedRecipeCollection() {
        return this.lastClickedRecipeCollection;
    }
    
    public void setInvisible() {
        this.overlay.setVisible(false);
    }
    
    public boolean mouseClicked(double mouseX, double mouseY, int button, int xPos, int yPos, int width, int height) {
        this.lastClickedRecipe = null;
        this.lastClickedRecipeCollection = null;
        if (this.overlay.isVisible()) {
            if (this.overlay.mouseClicked(mouseX, mouseY, button)) {
                this.lastClickedRecipe = this.overlay.lastRecipeClicked;
                this.lastClickedRecipeCollection = this.overlay.getRecipeCollection();
            } else {
                this.overlay.setVisible(false);
            }
            return true;
        } else if (this.forwardButton.mouseClicked(mouseX, mouseY, button)) {
            this.currentPage++;
            this.updateButtonsForPage();
            return true;
        } else if (this.backButton.mouseClicked(mouseX, mouseY, button)) {
            this.currentPage--;
            this.updateButtonsForPage();
            return true;
        } else {
            for (ArcaneRecipeButton recipeButton : this.buttons) {
                if (recipeButton.mouseClicked(mouseX, mouseY, button)) {
                    if (button == 0) {
                        this.lastClickedRecipe = recipeButton.getRecipe();
                        this.lastClickedRecipeCollection = recipeButton.getCollection();
                    } else if (button == 1 && !this.overlay.isVisible() && !recipeButton.isOnlyOption()) {
                        this.overlay.init(this.mc, recipeButton.getCollection(), this.arcaneBook, recipeButton.x, recipeButton.y, xPos + width / 2, yPos + 13 + height / 2, (float)recipeButton.getWidth());
                    }
                    return true;
                }
            }
            return false;
        }
    }
    
    public void recipesShown(List<Recipe<?>> recipes) {
        for (RecipeShownListener listener : this.showListeners) {
            listener.recipesShown(recipes);
        }
    }
    
    public Minecraft getMinecraft() {
        return this.mc;
    }
    
    public RecipeBook getVanillaRecipeBook() {
        return this.vanillaBook;
    }
    
    public ArcaneRecipeBook getArcaneRecipeBook() {
        return this.arcaneBook;
    }
    
    public void listButtons(Consumer<AbstractWidget> consumer) {
        consumer.accept(this.forwardButton);
        consumer.accept(this.backButton);
        this.buttons.forEach(consumer);
    }
}
