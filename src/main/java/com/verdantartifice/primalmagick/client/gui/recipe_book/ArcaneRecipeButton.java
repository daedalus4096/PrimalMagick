package com.verdantartifice.primalmagick.client.gui.recipe_book;

import java.util.ArrayList;
import java.util.List;

import com.verdantartifice.primalmagick.common.crafting.recipe_book.ArcaneRecipeBook;
import com.verdantartifice.primalmagick.common.menus.AbstractArcaneRecipeBookMenu;
import com.verdantartifice.primalmagick.common.menus.IArcaneRecipeBookMenu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

/**
 * GUI button for a recipe collection in the arcane recipe book.
 * 
 * @author Daedalus4096
 */
public class ArcaneRecipeButton extends AbstractWidget {
    protected static final ResourceLocation RECIPE_BOOK_LOCATION = new ResourceLocation("textures/gui/recipe_book.png");
    protected static final float ANIMATION_TIME = 15.0F;
    protected static final int BACKGROUND_SIZE = 25;
    public static final int TICKS_TO_SWAP = 30;
    protected static final Component MORE_RECIPES_TOOLTIP = Component.translatable("gui.recipebook.moreRecipes");
    
    protected IArcaneRecipeBookMenu<?> menu;
    protected ArcaneRecipeBook book;
    protected ArcaneRecipeCollection collection;
    protected float time;
    protected float animationTime;
    protected int currentIndex;
    
    public ArcaneRecipeButton() {
        super(0, 0, BACKGROUND_SIZE, BACKGROUND_SIZE, Component.empty());
    }
    
    public void init(ArcaneRecipeCollection recipeCollection, ArcaneRecipeBookPage page) {
        Minecraft mc = page.getMinecraft();
        this.collection = recipeCollection;
        this.menu = mc.player.containerMenu instanceof IArcaneRecipeBookMenu<?> recipeBookMenu ? recipeBookMenu : null;
        this.book = page.getArcaneRecipeBook();
        List<Recipe<?>> list = this.collection.getRecipes(this.book.isFiltering(this.menu.getRecipeBookType()));
        
        for (Recipe<?> recipe : list) {
            if (this.book.willHighlight(recipe)) {
                page.recipesShown(list);
                this.animationTime = ANIMATION_TIME;
                break;
            }
        }
    }
    
    public ArcaneRecipeCollection getCollection() {
        return this.collection;
    }
    
    public void setPosition(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int p_93677_, int p_93678_, float p_93679_) {
        if (!Screen.hasControlDown()) {
            this.time += p_93679_;
        }
        
        Minecraft mc = Minecraft.getInstance();

        int texX = 29;
        if (!this.collection.hasCraftable()) {
            texX += BACKGROUND_SIZE;
        }
        
        int texY = 206;
        if (this.collection.getRecipes(this.book.isFiltering(this.menu.getRecipeBookType())).size() > 1) {
            texY += BACKGROUND_SIZE;
        }
        
        boolean animating = this.animationTime > 0.0F;
        if (animating) {
            float scale = 1.0F + 0.1F * (float)Math.sin((double)(this.animationTime / 15.0F * (float)Math.PI));
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate((double)(this.getX() + 8), (double)(this.getY() + 12), 0.0D);
            guiGraphics.pose().scale(scale, scale, 1.0F);
            guiGraphics.pose().translate((double)(-(this.getX() + 8)), (double)(-(this.getY() + 12)), 0.0D);
            this.animationTime -= p_93679_;
        }
        
        guiGraphics.blit(RECIPE_BOOK_LOCATION, this.getX(), this.getY(), texX, texY, this.width, this.height);
        List<Recipe<?>> recipeList = this.getOrderedRecipes();
        this.currentIndex = Mth.floor(this.time / (float)TICKS_TO_SWAP) % recipeList.size();
        ItemStack stack = recipeList.get(this.currentIndex).getResultItem(mc.level.registryAccess());
        int k = 4;
        if (this.collection.hasSingleResultItem() && this.getOrderedRecipes().size() > 1) {
            guiGraphics.renderItem(stack, this.getX() + k + 1, this.getY() + k + 1, 0, 10);
            k--;
        }
        guiGraphics.renderFakeItem(stack, this.getX() + k, this.getY() + k);
        
        if (animating) {
            guiGraphics.pose().popPose();
        }
    }
    
    protected List<Recipe<?>> getOrderedRecipes() {
        List<Recipe<?>> retVal = this.collection.getDisplayRecipes(true);
        if (!this.book.isFiltering(this.menu.getRecipeBookType())) {
            retVal.addAll(this.collection.getDisplayRecipes(false));
        }
        return retVal;
    }
    
    public boolean isOnlyOption() {
        return this.getOrderedRecipes().size() == 1;
    }

    public Recipe<?> getRecipe() {
        return this.getOrderedRecipes().get(this.currentIndex);
    }
    
    public List<Component> getTooltipText(Screen screen) {
        Minecraft mc = screen.getMinecraft();
        ItemStack stack = this.getRecipe().getResultItem(mc.level.registryAccess());
        List<Component> retVal = new ArrayList<>(Screen.getTooltipFromItem(mc, stack));
        if (this.collection.getRecipes(this.book.isFiltering(this.menu.getRecipeBookType())).size() > 1) {
            retVal.add(MORE_RECIPES_TOOLTIP);
        }
        return retVal;
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput output) {
        Minecraft mc = Minecraft.getInstance();
        ItemStack stack = this.getRecipe().getResultItem(mc.level.registryAccess());
        output.add(NarratedElementType.TITLE, Component.translatable("narration.recipe", stack.getHoverName()));
        if (this.collection.getRecipes(this.book.isFiltering(this.menu.getRecipeBookType())).size() > 1) {
            output.add(NarratedElementType.USAGE, Component.translatable("narration.button.usage.hovered"), Component.translatable("narration.recipe.usage.more"));
        } else {
            output.add(NarratedElementType.USAGE, Component.translatable("narration.button.usage.hovered"));
        }
    }

    @Override
    public int getWidth() {
        return BACKGROUND_SIZE;
    }

    @Override
    protected boolean isValidClickButton(int value) {
        return value == 0 || value == 1;
    }
}
