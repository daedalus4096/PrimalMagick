package com.verdantartifice.primalmagic.client.gui.recipe_book;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagic.common.containers.AbstractArcaneRecipeBookMenu;
import com.verdantartifice.primalmagic.common.crafting.recipe_book.ArcaneRecipeBook;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
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
    protected static final Component MORE_RECIPES_TOOLTIP = new TranslatableComponent("gui.recipebook.moreRecipes");
    
    protected AbstractArcaneRecipeBookMenu<?> menu;
    protected ArcaneRecipeBook book;
    protected ArcaneRecipeCollection collection;
    protected float time;
    protected float animationTime;
    protected int currentIndex;
    
    public ArcaneRecipeButton() {
        super(0, 0, BACKGROUND_SIZE, BACKGROUND_SIZE, TextComponent.EMPTY);
    }
    
    public void init(ArcaneRecipeCollection recipeCollection, ArcaneRecipeBookPage page) {
        Minecraft mc = page.getMinecraft();
        this.collection = recipeCollection;
        this.menu = (AbstractArcaneRecipeBookMenu<?>)mc.player.containerMenu;
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
        this.x = x;
        this.y = y;
    }

    @Override
    public void renderButton(PoseStack poseStack, int p_93677_, int p_93678_, float p_93679_) {
        if (!Screen.hasControlDown()) {
            this.time += p_93679_;
        }
        
        Minecraft mc = Minecraft.getInstance();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, RECIPE_BOOK_LOCATION);

        int texX = 29;
        if (!this.collection.hasCraftable()) {
            texX += BACKGROUND_SIZE;
        }
        
        int texY = 206;
        if (this.collection.getRecipes(this.book.isFiltering(this.menu.getRecipeBookType())).size() > 1) {
            texY += BACKGROUND_SIZE;
        }
        
        boolean animating = this.animationTime > 0.0F;
        PoseStack modelViewStack = RenderSystem.getModelViewStack();
        if (animating) {
            float scale = 1.0F + 0.1F * (float)Math.sin((double)(this.animationTime / 15.0F * (float)Math.PI));
            modelViewStack.pushPose();
            modelViewStack.translate((double)(this.x + 8), (double)(this.y + 12), 0.0D);
            modelViewStack.scale(scale, scale, 1.0F);
            modelViewStack.translate((double)(-(this.x + 8)), (double)(-(this.y + 12)), 0.0D);
            RenderSystem.applyModelViewMatrix();
            this.animationTime -= p_93679_;
        }
        
        this.blit(poseStack, this.x, this.y, texX, texY, this.width, this.height);
        List<Recipe<?>> recipeList = this.getOrderedRecipes();
        this.currentIndex = Mth.floor(this.time / (float)TICKS_TO_SWAP) % recipeList.size();
        ItemStack stack = recipeList.get(this.currentIndex).getResultItem();
        int k = 4;
        if (this.collection.hasSingleResultItem() && this.getOrderedRecipes().size() > 1) {
            mc.getItemRenderer().renderAndDecorateItem(stack, this.x + k + 1, this.y + k + 1, 0, 10);
            k--;
        }
        mc.getItemRenderer().renderAndDecorateFakeItem(stack, this.x + k, this.y + k);
        
        if (animating) {
            modelViewStack.popPose();
            RenderSystem.applyModelViewMatrix();
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
        ItemStack stack = this.getRecipe().getResultItem();
        List<Component> retVal = new ArrayList<>(screen.getTooltipFromItem(stack));
        if (this.collection.getRecipes(this.book.isFiltering(this.menu.getRecipeBookType())).size() > 1) {
            retVal.add(MORE_RECIPES_TOOLTIP);
        }
        return retVal;
    }

    @Override
    public void updateNarration(NarrationElementOutput output) {
        ItemStack stack = this.getRecipe().getResultItem();
        output.add(NarratedElementType.TITLE, new TranslatableComponent("narration.recipe", stack.getHoverName()));
        if (this.collection.getRecipes(this.book.isFiltering(this.menu.getRecipeBookType())).size() > 1) {
            output.add(NarratedElementType.USAGE, new TranslatableComponent("narration.button.usage.hovered"), new TranslatableComponent("narration.recipe.usage.more"));
        } else {
            output.add(NarratedElementType.USAGE, new TranslatableComponent("narration.button.usage.hovered"));
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
