package com.verdantartifice.primalmagick.client.gui.recipe_book;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.common.containers.AbstractArcaneRecipeBookMenu;
import com.verdantartifice.primalmagick.common.crafting.recipe_book.ArcaneRecipeBook;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.recipebook.PlaceRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

public class OverlayArcaneRecipeComponent extends GuiComponent implements Widget, GuiEventListener {
    protected static final ResourceLocation RECIPE_BOOK_LOCATION = new ResourceLocation("textures/gui/recipe_book.png");
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
    protected Recipe<?> lastRecipeClicked;
    protected float time;
    protected boolean useFurnaceStyle;
    
    public void init(Minecraft mc, ArcaneRecipeCollection recipeCollection, ArcaneRecipeBook book, int xPos, int yPos, int dx, int dy, float parentWidth) {
        this.mc = mc;
        this.collection = recipeCollection;
        boolean isFiltering = false;
        if (mc.player.containerMenu instanceof AbstractArcaneRecipeBookMenu<?> arcaneMenu) {
            this.useFurnaceStyle = arcaneMenu.isSingleIngredientMenu();
            isFiltering = book.isFiltering(arcaneMenu.getRecipeBookType());
        } else {
            this.useFurnaceStyle = false;
        }
        
        List<Recipe<?>> visibleRecipes = this.collection.getDisplayRecipes(true);
        List<Recipe<?>> invisibleRecipes = isFiltering ? Collections.emptyList() : this.collection.getDisplayRecipes(false);
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
            Recipe<?> recipe = isVisible ? visibleRecipes.get(index) : invisibleRecipes.get(index - visibleSize);
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

    @Override
    public boolean changeFocus(boolean focus) {
        return false;
    }

    public ArcaneRecipeCollection getRecipeCollection() {
        return this.collection;
    }

    @Nullable
    public Recipe<?> getLastRecipeClicked() {
        return this.lastRecipeClicked;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) {
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
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        if (this.isVisible) {
            this.time += partialTicks;
            RenderSystem.enableBlend();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, RECIPE_BOOK_LOCATION);
            poseStack.pushPose();
            poseStack.translate(0.0D, 0.0D, 170.0D);
            
            int maxRowSize = (this.recipeButtons.size() <= 16) ? MAX_ROW : MAX_ROW_LARGE;
            int colCount = Math.min(this.recipeButtons.size(), maxRowSize);
            int rowCount = Mth.ceil((float)this.recipeButtons.size() / (float)maxRowSize);
            this.nineInchSprite(poseStack, colCount, rowCount, 24, 4, 82, 208);
            RenderSystem.disableBlend();

            for (OverlayArcaneRecipeComponent.OverlayArcaneRecipeButton recipeButton : this.recipeButtons) {
                recipeButton.render(poseStack, mouseX, mouseY, partialTicks);
            }
            
            poseStack.popPose();
        }
    }

    private void nineInchSprite(PoseStack p_100214_, int p_100215_, int p_100216_, int p_100217_, int p_100218_, int p_100219_, int p_100220_) {
        this.blit(p_100214_, this.x, this.y, p_100219_, p_100220_, p_100218_, p_100218_);
        this.blit(p_100214_, this.x + p_100218_ * 2 + p_100215_ * p_100217_, this.y, p_100219_ + p_100217_ + p_100218_, p_100220_, p_100218_, p_100218_);
        this.blit(p_100214_, this.x, this.y + p_100218_ * 2 + p_100216_ * p_100217_, p_100219_, p_100220_ + p_100217_ + p_100218_, p_100218_, p_100218_);
        this.blit(p_100214_, this.x + p_100218_ * 2 + p_100215_ * p_100217_, this.y + p_100218_ * 2 + p_100216_ * p_100217_, p_100219_ + p_100217_ + p_100218_, p_100220_ + p_100217_ + p_100218_, p_100218_, p_100218_);

        for(int i = 0; i < p_100215_; ++i) {
            this.blit(p_100214_, this.x + p_100218_ + i * p_100217_, this.y, p_100219_ + p_100218_, p_100220_, p_100217_, p_100218_);
            this.blit(p_100214_, this.x + p_100218_ + (i + 1) * p_100217_, this.y, p_100219_ + p_100218_, p_100220_, p_100218_, p_100218_);

            for(int j = 0; j < p_100216_; ++j) {
                if (i == 0) {
                    this.blit(p_100214_, this.x, this.y + p_100218_ + j * p_100217_, p_100219_, p_100220_ + p_100218_, p_100218_, p_100217_);
                    this.blit(p_100214_, this.x, this.y + p_100218_ + (j + 1) * p_100217_, p_100219_, p_100220_ + p_100218_, p_100218_, p_100218_);
                }

                this.blit(p_100214_, this.x + p_100218_ + i * p_100217_, this.y + p_100218_ + j * p_100217_, p_100219_ + p_100218_, p_100220_ + p_100218_, p_100217_, p_100217_);
                this.blit(p_100214_, this.x + p_100218_ + (i + 1) * p_100217_, this.y + p_100218_ + j * p_100217_, p_100219_ + p_100218_, p_100220_ + p_100218_, p_100218_, p_100217_);
                this.blit(p_100214_, this.x + p_100218_ + i * p_100217_, this.y + p_100218_ + (j + 1) * p_100217_, p_100219_ + p_100218_, p_100220_ + p_100218_, p_100217_, p_100218_);
                this.blit(p_100214_, this.x + p_100218_ + (i + 1) * p_100217_ - 1, this.y + p_100218_ + (j + 1) * p_100217_ - 1, p_100219_ + p_100218_, p_100220_ + p_100218_, p_100218_ + 1, p_100218_ + 1);
                if (i == p_100215_ - 1) {
                    this.blit(p_100214_, this.x + p_100218_ * 2 + p_100215_ * p_100217_, this.y + p_100218_ + j * p_100217_, p_100219_ + p_100217_ + p_100218_, p_100220_ + p_100218_, p_100218_, p_100217_);
                    this.blit(p_100214_, this.x + p_100218_ * 2 + p_100215_ * p_100217_, this.y + p_100218_ + (j + 1) * p_100217_, p_100219_ + p_100217_ + p_100218_, p_100220_ + p_100218_, p_100218_, p_100218_);
                }
            }

            this.blit(p_100214_, this.x + p_100218_ + i * p_100217_, this.y + p_100218_ * 2 + p_100216_ * p_100217_, p_100219_ + p_100218_, p_100220_ + p_100217_ + p_100218_, p_100217_, p_100218_);
            this.blit(p_100214_, this.x + p_100218_ + (i + 1) * p_100217_, this.y + p_100218_ * 2 + p_100216_ * p_100217_, p_100219_ + p_100218_, p_100220_ + p_100217_ + p_100218_, p_100218_, p_100218_);
        }
    }
    
    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }
    
    public boolean isVisible() {
        return this.isVisible;
    }

    protected class OverlayArcaneRecipeButton extends AbstractWidget implements PlaceRecipe<Ingredient> {
        protected final Recipe<?> recipe;
        protected final boolean isCraftable;
        protected final List<OverlayArcaneRecipeComponent.OverlayArcaneRecipeButton.Pos> ingredientPos = new ArrayList<>();
        
        public OverlayArcaneRecipeButton(int xPos, int yPos, Recipe<?> recipe, boolean isCraftable) {
            super(xPos, yPos, 200, 20, TextComponent.EMPTY);
            this.width = 24;
            this.height = 24;
            this.recipe = recipe;
            this.isCraftable = isCraftable;
            this.calculateIngredientsPositions(recipe);
        }
        
        protected void calculateIngredientsPositions(Recipe<?> recipe) {
            this.placeRecipe(3, 3, -1, recipe, recipe.getIngredients().iterator(), 0);
        }
        
        @Override
        public void updateNarration(NarrationElementOutput output) {
            this.defaultButtonNarrationText(output);
        }

        @Override
        public void addItemToSlot(Iterator<Ingredient> ingredientIterator, int p_135416_, int p_135417_, int p_135418_, int p_135419_) {
            ItemStack[] stackArray = ingredientIterator.next().getItems();
            if (stackArray.length != 0) {
                this.ingredientPos.add(new OverlayArcaneRecipeComponent.OverlayArcaneRecipeButton.Pos(3 + p_135419_ * 7, 3 + p_135418_ * 7, stackArray));
            }
        }

        @Override
        public void renderButton(PoseStack poseStack, int p_93677_, int p_93678_, float p_93679_) {
            RenderSystem.setShaderTexture(0, OverlayArcaneRecipeComponent.RECIPE_BOOK_LOCATION);
            
            int texX = 152;
            if (!this.isCraftable) {
                texX += 26;
            }
            
            int texY = OverlayArcaneRecipeComponent.this.useFurnaceStyle ? 130 : 78;
            if (this.isHoveredOrFocused()) {
                texY += 26;
            }
            
            this.blit(poseStack, this.x, this.y, texX, texY, this.width, this.height);
            PoseStack modelViewStack = RenderSystem.getModelViewStack();
            modelViewStack.pushPose();
            modelViewStack.translate((double)(this.x + 2), (double)(this.y + 2), 125.0D);

            for (OverlayArcaneRecipeComponent.OverlayArcaneRecipeButton.Pos pos : this.ingredientPos) {
                modelViewStack.pushPose();
                modelViewStack.translate(pos.x, pos.y, 0.0D);
                modelViewStack.scale(0.375F, 0.375F, 1.0F);
                modelViewStack.translate(-8.0D, -8.0D, 0.0D);
                RenderSystem.applyModelViewMatrix();
                OverlayArcaneRecipeComponent.this.mc.getItemRenderer().renderAndDecorateItem(pos.ingredients[Mth.floor(OverlayArcaneRecipeComponent.this.time / 30.0F) % pos.ingredients.length], 0, 0);
                modelViewStack.popPose();
            }
            
            modelViewStack.popPose();
            RenderSystem.applyModelViewMatrix();
        }

        protected class Pos {
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
        public OverlayArcaneSingleIngredientRecipeButton(int xPos, int yPos, Recipe<?> recipe, boolean isCraftable) {
            super(xPos, yPos, recipe, isCraftable);
        }

        protected void calculateIngredientsPositions(Recipe<?> recipe) {
            ItemStack[] aitemstack = recipe.getIngredients().get(0).getItems();
            this.ingredientPos.add(new OverlayArcaneRecipeComponent.OverlayArcaneRecipeButton.Pos(10, 10, aitemstack));
        }
    }
}
