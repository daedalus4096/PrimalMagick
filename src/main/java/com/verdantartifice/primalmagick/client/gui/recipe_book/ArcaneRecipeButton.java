package com.verdantartifice.primalmagick.client.gui.recipe_book;

import java.util.ArrayList;
import java.util.List;

import com.verdantartifice.primalmagick.common.crafting.recipe_book.ArcaneRecipeBook;
import com.verdantartifice.primalmagick.common.menus.base.IArcaneRecipeBookMenu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

/**
 * GUI button for a recipe collection in the arcane recipe book.
 * 
 * @author Daedalus4096
 */
public class ArcaneRecipeButton extends AbstractWidget {
    protected static final WidgetSprites SLOT_SPRITES = new WidgetSprites(new ResourceLocation("recipe_book/slot_craftable"), new ResourceLocation("recipe_book/slot_uncraftable"), new ResourceLocation("recipe_book/slot_many_craftable"), new ResourceLocation("recipe_book/slot_many_uncraftable"));
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
        List<RecipeHolder<?>> list = this.collection.getRecipes(this.book.isFiltering(this.menu.getRecipeBookType()));
        
        for (RecipeHolder<?> recipe : list) {
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

        boolean animating = this.animationTime > 0.0F;
        if (animating) {
            float scale = 1.0F + 0.1F * (float)Math.sin((double)(this.animationTime / 15.0F * (float)Math.PI));
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate((double)(this.getX() + 8), (double)(this.getY() + 12), 0.0D);
            guiGraphics.pose().scale(scale, scale, 1.0F);
            guiGraphics.pose().translate((double)(-(this.getX() + 8)), (double)(-(this.getY() + 12)), 0.0D);
            this.animationTime -= p_93679_;
        }
        
        ResourceLocation spriteLoc = SLOT_SPRITES.get(this.collection.hasCraftable(), this.collection.getRecipes(this.book.isFiltering(this.menu.getRecipeBookType())).size() > 1);
        guiGraphics.blitSprite(spriteLoc, this.getX(), this.getY(), this.width, this.height);
        List<RecipeHolder<?>> recipeList = this.getOrderedRecipes();
        this.currentIndex = Mth.floor(this.time / (float)TICKS_TO_SWAP) % recipeList.size();
        ItemStack stack = recipeList.get(this.currentIndex).value().getResultItem(mc.level.registryAccess());
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
    
    protected List<RecipeHolder<?>> getOrderedRecipes() {
        List<RecipeHolder<?>> retVal = this.collection.getDisplayRecipes(true);
        if (!this.book.isFiltering(this.menu.getRecipeBookType())) {
            retVal.addAll(this.collection.getDisplayRecipes(false));
        }
        return retVal;
    }
    
    public boolean isOnlyOption() {
        return this.getOrderedRecipes().size() == 1;
    }

    public RecipeHolder<?> getRecipe() {
        return this.getOrderedRecipes().get(this.currentIndex);
    }
    
    public List<Component> getTooltipText(Screen screen) {
        Minecraft mc = screen.getMinecraft();
        ItemStack stack = this.getRecipe().value().getResultItem(mc.level.registryAccess());
        List<Component> retVal = new ArrayList<>(Screen.getTooltipFromItem(mc, stack));
        if (this.collection.getRecipes(this.book.isFiltering(this.menu.getRecipeBookType())).size() > 1) {
            retVal.add(MORE_RECIPES_TOOLTIP);
        }
        return retVal;
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput output) {
        Minecraft mc = Minecraft.getInstance();
        ItemStack stack = this.getRecipe().value().getResultItem(mc.level.registryAccess());
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
