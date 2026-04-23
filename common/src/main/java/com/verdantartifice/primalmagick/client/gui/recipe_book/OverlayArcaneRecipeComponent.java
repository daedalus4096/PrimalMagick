package com.verdantartifice.primalmagick.client.gui.recipe_book;

import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagick.common.crafting.recipe_book.ArcaneRecipeBook;
import com.verdantartifice.primalmagick.common.menus.base.IArcaneRecipeBookMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.recipebook.PlaceRecipe;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OverlayArcaneRecipeComponent implements Renderable, GuiEventListener {
    private static final Identifier OVERLAY_RECIPE_SPRITE = Identifier.withDefaultNamespace("recipe_book/overlay_recipe");
    protected static final WidgetSprites CRAFTING_OVERLAY_SPRITES = new WidgetSprites(Identifier.withDefaultNamespace("recipe_book/crafting_overlay"), Identifier.withDefaultNamespace("recipe_book/crafting_overlay_disabled"), Identifier.withDefaultNamespace("recipe_book/crafting_overlay_highlighted"), Identifier.withDefaultNamespace("recipe_book/crafting_overlay_disabled_highlighted"));
    protected static final WidgetSprites FURNACE_OVERLAY_SPRITES = new WidgetSprites(Identifier.withDefaultNamespace("recipe_book/furnace_overlay"), Identifier.withDefaultNamespace("recipe_book/furnace_overlay_disabled"), Identifier.withDefaultNamespace("recipe_book/furnace_overlay_highlighted"), Identifier.withDefaultNamespace("recipe_book/furnace_overlay_disabled_highlighted"));
    protected static final int MAX_ROW = 4;
    protected static final int MAX_ROW_LARGE = 5;
    protected static final float ITEM_RENDER_SCALE = 0.375F;
    
    protected final List<OverlayArcaneRecipeComponent.OverlayArcaneRecipeButton> recipeButtons = new ArrayList<>();
    protected boolean isVisible;
    protected int x;
    protected int y;
    protected Minecraft mc;
    protected ArcaneRecipeCollection collection;
    @Nullable
    protected RecipeHolder<?> lastRecipeClicked;
    protected float time;
    protected boolean useFurnaceStyle;
    
    public void init(Minecraft mc, ArcaneRecipeCollection recipeCollection, ArcaneRecipeBook book, int xPos, int yPos, int dx, int dy, float parentWidth) {
        this.mc = mc;
        this.collection = recipeCollection;
        boolean isFiltering = false;
        if (mc.player != null && mc.player.containerMenu instanceof IArcaneRecipeBookMenu<?, ?> arcaneMenu) {
            this.useFurnaceStyle = arcaneMenu.isSingleIngredientMenu();
            isFiltering = book.isFiltering(arcaneMenu.getRecipeBookType());
        } else {
            this.useFurnaceStyle = false;
        }
        
        List<RecipeHolder<?>> visibleRecipes = this.collection.getDisplayRecipes(true);
        List<RecipeHolder<?>> invisibleRecipes = isFiltering ? Collections.emptyList() : this.collection.getDisplayRecipes(false);
        int visibleSize = visibleRecipes.size();
        int totalSize = visibleSize + invisibleRecipes.size();
        int maxRowSize = (totalSize <= 16) ? MAX_ROW : MAX_ROW_LARGE;
        int rowCount = (int)Mth.ceil((double)((float)totalSize / (float)maxRowSize));
        this.x = xPos;
        this.y = yPos;
        
        float f = (float)(this.x + Math.min(totalSize, maxRowSize) * 25);
        float f1 = (float)(dx + 50);
        if (f > f1) {
            this.x = (int)((float)this.x - parentWidth * (float)((int)((f - f1) / parentWidth)));
        }
        
        float f2 = (float)(this.y + rowCount * 25);
        float f3 = (float)(dy + 50);
        if (f2 > f3) {
            this.y = (int)((float)this.y - parentWidth * (float)Mth.ceil((f2 - f3) / parentWidth));
        }
        
        float f4 = (float)this.y;
        float f5 = (float)(dy - 100);
        if (f4 < f5) {
            this.y = (int)((float)this.y - parentWidth * (float)Mth.ceil((f4 - f5) / parentWidth));
        }
        
        this.isVisible = true;
        this.recipeButtons.clear();
        
        for (int index = 0; index < totalSize; index++) {
            boolean isVisible = index < visibleSize;
            RecipeHolder<?> recipe = isVisible ? visibleRecipes.get(index) : invisibleRecipes.get(index - visibleSize);
            int slotX = this.x + 4 + 25 * (index % maxRowSize);
            int slotY = this.y + 5 + 25 * (index / maxRowSize);
            if (this.useFurnaceStyle) {
                this.recipeButtons.add(new OverlayArcaneRecipeComponent.OverlayArcaneSingleIngredientRecipeButton(slotX, slotY, recipe, isVisible));
            } else {
                this.recipeButtons.add(new OverlayArcaneRecipeComponent.OverlayArcaneRecipeButton(slotX, slotY, recipe, isVisible));
            }
        }
        
        this.lastRecipeClicked = null;
    }

    public ArcaneRecipeCollection getRecipeCollection() {
        return this.collection;
    }

    @Nullable
    public RecipeHolder<?> getLastRecipeClicked() {
        return this.lastRecipeClicked;
    }

    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent event, boolean isDoubleClick) {
        if (event.button() != 0) {
            return false;
        } else {
            for (OverlayArcaneRecipeComponent.OverlayArcaneRecipeButton recipeButton : this.recipeButtons) {
                if (recipeButton.mouseClicked(mouseX, mouseY, button)) {
                    this.lastRecipeClicked = recipeButton.recipe;
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return false;
    }

    @Override
    public void render(@NotNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (this.isVisible) {
            this.time += partialTicks;
            guiGraphics.pose().pushMatrix();

            int maxRowSize = (this.recipeButtons.size() <= 16) ? MAX_ROW : MAX_ROW_LARGE;
            int colCount = Math.min(this.recipeButtons.size(), maxRowSize);
            int rowCount = Mth.ceil((float)this.recipeButtons.size() / (float)maxRowSize);
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, OVERLAY_RECIPE_SPRITE, this.x, this.y, colCount * 25 + 8, rowCount * 25 + 8);

            for (OverlayArcaneRecipeComponent.OverlayArcaneRecipeButton recipeButton : this.recipeButtons) {
                recipeButton.render(guiGraphics, mouseX, mouseY, partialTicks);
            }
            
            guiGraphics.pose().popMatrix();
        }
    }

    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }
    
    public boolean isVisible() {
        return this.isVisible;
    }

    @Override
    public void setFocused(boolean p_265728_) {
        // Do nothing
    }

    @Override
    public boolean isFocused() {
        return false;
    }

    protected class OverlayArcaneRecipeButton extends AbstractWidget implements PlaceRecipe<Ingredient> {
        protected final RecipeHolder<?> recipe;
        protected final boolean isCraftable;
        protected final List<OverlayArcaneRecipeComponent.OverlayArcaneRecipeButton.Pos> ingredientPos = new ArrayList<>();
        
        public OverlayArcaneRecipeButton(int xPos, int yPos, RecipeHolder<?> recipe, boolean isCraftable) {
            super(xPos, yPos, 200, 20, Component.empty());
            this.width = 24;
            this.height = 24;
            this.recipe = recipe;
            this.isCraftable = isCraftable;
            this.calculateIngredientsPositions(recipe);
        }
        
        protected void calculateIngredientsPositions(RecipeHolder<?> recipe) {
            this.placeRecipe(3, 3, -1, recipe, recipe.value().getIngredients().iterator(), 0);
        }
        
        @Override
        public void updateWidgetNarration(NarrationElementOutput output) {
            this.defaultButtonNarrationText(output);
        }

        @Override
        public void addItemToSlot(Ingredient ingredient, int pSlot, int pMaxAmount, int pX, int pY) {
            ItemStack[] stackArray = ingredient.getItems();
            if (stackArray.length != 0) {
                this.ingredientPos.add(new Pos(3 + pY * 7, 3 + pX * 7, stackArray));
            }
        }

        @Override
        public void renderWidget(GuiGraphicsExtractor guiGraphics, int p_93677_, int p_93678_, float p_93679_) {
            WidgetSprites sprites = OverlayArcaneRecipeComponent.this.useFurnaceStyle ? OverlayArcaneRecipeComponent.FURNACE_OVERLAY_SPRITES : OverlayArcaneRecipeComponent.CRAFTING_OVERLAY_SPRITES;
            Identifier spriteLoc = sprites.get(this.isCraftable, this.isHoveredOrFocused());
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, spriteLoc, this.getX(), this.getY(), this.width, this.height);
            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().translate((this.getX() + 2), (this.getY() + 2));

            for (OverlayArcaneRecipeComponent.OverlayArcaneRecipeButton.Pos pos : this.ingredientPos) {
                guiGraphics.pose().pushMatrix();
                guiGraphics.pose().translate(pos.x, pos.y);
                guiGraphics.pose().scale(0.375F, 0.375F);
                guiGraphics.pose().translate(-8.0F, -8.0F);
                RenderSystem.applyModelViewMatrix();
                guiGraphics.renderItem(pos.ingredients[Mth.floor(OverlayArcaneRecipeComponent.this.time / 30.0F) % pos.ingredients.length], 0, 0);
                guiGraphics.pose().popMatrix();
            }
            
            guiGraphics.pose().popMatrix();
        }

        protected static class Pos {
            public final ItemStack[] ingredients;
            public final int x;
            public final int y;

            public Pos(int x, int y, ItemStack[] ingredients) {
                this.ingredients = ingredients;
                this.x = x;
                this.y = y;
            }
        }
    }
    
    protected class OverlayArcaneSingleIngredientRecipeButton extends OverlayArcaneRecipeComponent.OverlayArcaneRecipeButton {
        public OverlayArcaneSingleIngredientRecipeButton(int xPos, int yPos, RecipeHolder<?> recipe, boolean isCraftable) {
            super(xPos, yPos, recipe, isCraftable);
        }

        protected void calculateIngredientsPositions(Recipe<?> recipe) {
            ItemStack[] aitemstack = recipe.placementInfo().ingredients().getFirst().getItems();
            this.ingredientPos.add(new Pos(10, 10, aitemstack));
        }
    }
}
