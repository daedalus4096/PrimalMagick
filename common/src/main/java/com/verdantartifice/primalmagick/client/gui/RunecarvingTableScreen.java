package com.verdantartifice.primalmagick.client.gui;

import com.verdantartifice.primalmagick.common.crafting.IRunecarvingRecipe;
import com.verdantartifice.primalmagick.common.menus.RunecarvingTableMenu;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.SelectableRecipe;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;
import org.jetbrains.annotations.NotNull;

/**
 * GUI screen for runecarving table block.
 * 
 * @author Daedalus4096
 */
public class RunecarvingTableScreen extends AbstractContainerScreenPM<RunecarvingTableMenu> {
    protected static final Identifier TEXTURE = ResourceUtils.loc("textures/gui/runecarving_table.png");
    protected static final Identifier SCROLL_HANDLE = ResourceUtils.loc("runecarving_table/scroll_handle");
    protected static final Identifier SCROLL_HANDLE_DISABLED = ResourceUtils.loc("runecarving_table/scroll_handle_disabled");
    protected static final Identifier RECIPE_NORMAL = ResourceUtils.loc("runecarving_table/recipe_normal");
    protected static final Identifier RECIPE_SELECTED = ResourceUtils.loc("runecarving_table/recipe_selected");
    protected static final Identifier RECIPE_HOVERED = ResourceUtils.loc("runecarving_table/recipe_hovered");

    protected float sliderProgress;
    protected boolean clickedOnSroll;
    
    /**
     * The index of the first recipe to display.
     * The number of recipes displayed at any time is 12 (4 recipes per row, and 3 rows). If the player scrolled down one
     * row, this value would be 4 (representing the index of the first slot on the second row).
     */
    protected int recipeIndexOffset;
    protected boolean hasItemsInInputSlot;

    public RunecarvingTableScreen(RunecarvingTableMenu screenMenu, Inventory inv, Component titleIn) {
        super(screenMenu, inv, titleIn);
        screenMenu.setInventoryUpdateListener(this::onInventoryUpdate);
    }
    
    @Override
    public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
        int i = this.leftPos;
        int j = this.topPos;
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
        int k = (int)(41.0F * this.sliderProgress);
        Identifier scrollHandleTexture = this.canScroll() ? SCROLL_HANDLE : SCROLL_HANDLE_DISABLED;
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, scrollHandleTexture, i + 119, j + 15 + k, 12, 15);
        int l = this.leftPos + 52;
        int i1 = this.topPos + 14;
        int j1 = this.recipeIndexOffset + 12;
        this.drawRecipesBackground(guiGraphics, mouseX, mouseY, l, i1, j1);
        this.drawRecipesItems(guiGraphics, mouseX, mouseY, l, i1, j1);
    }
    
    protected void drawRecipesBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, int left, int top, int recipeIndexOffsetMax) {
        for (int i = this.recipeIndexOffset; i < recipeIndexOffsetMax && i < this.menu.getRecipeListSize(); ++i) {
            int j = i - this.recipeIndexOffset;
            int k = left + j % 4 * 16;
            int l = j / 4;
            int i1 = top + l * 18 + 2;

            Identifier recipeBgTexture;
            if (i == this.menu.getSelectedRecipe()) {
                recipeBgTexture = RECIPE_SELECTED;
            } else if (mouseX >= k && mouseY >= i1 && mouseX < k + 16 && mouseY < i1 + 18) {
                recipeBgTexture = RECIPE_HOVERED;
            } else {
                recipeBgTexture = RECIPE_NORMAL;
            }

            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, recipeBgTexture, k, i1 - 1, 16, 18);
        }
    }
    
    protected void drawRecipesItems(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, int left, int top, int recipeIndexOffsetMax) {
        SelectableRecipe.SingleInputSet<IRunecarvingRecipe> visibleRecipes = this.menu.getRecipeList();
        ContextMap context = SlotDisplayContext.fromLevel(this.minecraft.level);
        for (int i = this.recipeIndexOffset; i < recipeIndexOffsetMax && i < this.menu.getRecipeListSize(); ++i) {
            int j = i - this.recipeIndexOffset;
            int k = left + j % 4 * 16;
            int l = j / 4;
            int i1 = top + l * 18 + 2;
            SlotDisplay buttonIcon = visibleRecipes.entries().get(i).recipe().optionDisplay();
            guiGraphics.setTooltipForNextFrame(this.font, buttonIcon.resolveForFirstStack(context), k, i1);
        }
    }
    
    @Override
    protected void extractTooltip(@NotNull GuiGraphicsExtractor pGuiGraphics, int pX, int pY) {
        super.extractTooltip(pGuiGraphics, pX, pY);
        SelectableRecipe.SingleInputSet<IRunecarvingRecipe> visibleRecipes = this.menu.getRecipeList();
        for (int i = this.recipeIndexOffset; i < this.recipeIndexOffset + 12 && i < this.menu.getRecipeListSize(); ++i) {
            int j = i - this.recipeIndexOffset;
            int k = this.leftPos + 52 + j % 4 * 16;
            int l = j / 4;
            int i1 = this.topPos + 14 + l * 18 + 2;
            if (pX >= k && pX < k + 16 && pY >= i1 && pY < i1 + 18) {
                ContextMap context = SlotDisplayContext.fromLevel(this.minecraft.level);
                SlotDisplay buttonIcon = visibleRecipes.entries().get(i).recipe().optionDisplay();
                pGuiGraphics.setTooltipForNextFrame(this.font, buttonIcon.resolveForFirstStack(context), pX, pY);
            }
        }
    }

    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent event, boolean doubleClick) {
        this.clickedOnSroll = false;
        if (this.hasItemsInInputSlot) {
            int i = this.leftPos + 52;
            int j = this.topPos + 14;
            int k = this.recipeIndexOffset + 12;

            for(int l = this.recipeIndexOffset; l < k; ++l) {
                int i1 = l - this.recipeIndexOffset;
                double d0 = event.x() - (double)(i + i1 % 4 * 16);
                double d1 = event.y() - (double)(j + i1 / 4 * 18);
                if (d0 >= 0.0D && d1 >= 0.0D && d0 < 16.0D && d1 < 18.0D && this.menu.clickMenuButton(this.minecraft.player, l)) {
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
                    this.minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, l);
                    return true;
                }
            }

            i = this.leftPos + 119;
            j = this.topPos + 9;
            if (event.x() >= (double)i && event.x() < (double)(i + 12) && event.y() >= (double)j && event.y() < (double)(j + 54)) {
                this.clickedOnSroll = true;
            }
        }
        return super.mouseClicked(event, doubleClick);
    }
    
    @Override
    public boolean mouseDragged(@NotNull MouseButtonEvent event, double pMouseX, double pMouseY) {
        if (this.clickedOnSroll && this.canScroll()) {
            int i = this.topPos + 14;
            int j = i + 54;
            this.sliderProgress = ((float)pMouseY - (float)i - 7.5F) / ((float)(j - i) - 15.0F);
            this.sliderProgress = Mth.clamp(this.sliderProgress, 0.0F, 1.0F);
            this.recipeIndexOffset = (int)((double)(this.sliderProgress * (float)this.getHiddenRows()) + 0.5D) * 4;
            return true;
        } else {
            return super.mouseDragged(event, pMouseX, pMouseY);
        }
    }
    
    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pScrollX, double pScrollY) {
        if (this.canScroll()) {
            int i = this.getHiddenRows();
            this.sliderProgress = (float)((double)this.sliderProgress - pScrollY / (double)i);
            this.sliderProgress = Mth.clamp(this.sliderProgress, 0.0F, 1.0F);
            this.recipeIndexOffset = (int)((double)(this.sliderProgress * (float)i) + 0.5D) * 4;
        }
        return true;
    }
    
    protected boolean canScroll() {
        return this.hasItemsInInputSlot && this.menu.getRecipeListSize() > 12;
    }
    
    protected int getHiddenRows() {
        return (this.menu.getRecipeListSize() + 4 - 1) / 4 - 3;
    }
    
    /**
     * Called every time this screen's container is changed (is marked as dirty).
     */
    protected void onInventoryUpdate() {
        this.hasItemsInInputSlot = this.menu.hasItemsInInputSlot();
        if (!this.hasItemsInInputSlot) {
            this.sliderProgress = 0.0F;
            this.recipeIndexOffset = 0;
        }
    }
}
