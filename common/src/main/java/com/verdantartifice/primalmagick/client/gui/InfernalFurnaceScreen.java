package com.verdantartifice.primalmagick.client.gui;

import com.verdantartifice.primalmagick.client.gui.recipe_book.ArcaneRecipeBookComponent;
import com.verdantartifice.primalmagick.client.gui.recipe_book.ArcaneRecipeUpdateListener;
import com.verdantartifice.primalmagick.client.gui.widgets.ManaGaugeWidget;
import com.verdantartifice.primalmagick.common.menus.InfernalFurnaceMenu;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * GUI screen for the infernal furnace block.
 * 
 * @author Daedalus4096
 */
public class InfernalFurnaceScreen extends AbstractContainerScreenPM<InfernalFurnaceMenu> implements ArcaneRecipeUpdateListener {
    protected static final Logger LOGGER = LogManager.getLogger();
    protected static final Identifier TEXTURE = ResourceUtils.loc("textures/gui/infernal_furnace.png");
    protected static final Identifier PROGRESS_SPRITE = ResourceUtils.loc("progress_arrow");
    protected static final Identifier BURN_SPRITE = ResourceUtils.loc("burn_indicator");

    protected final ArcaneRecipeBookComponent recipeBookComponent = new ArcaneRecipeBookComponent();
    protected boolean widthTooNarrow;
    protected ManaGaugeWidget manaGauge;

    public InfernalFurnaceScreen(InfernalFurnaceMenu screenMenu, Inventory inv, Component titleIn) {
        super(screenMenu, inv, titleIn);
        this.titleLabelX = 27;
        this.inventoryLabelX = 27;
    }

    @Override
    protected void init() {
        super.init();
        this.widthTooNarrow = this.width < 379;
        this.recipeBookComponent.init(this.width, this.height, this.minecraft, this.widthTooNarrow, true, this.menu);
        this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
        
        this.manaGauge = this.addRenderableWidget(new ManaGaugeWidget(this.leftPos + 10, this.topPos + 6, Sources.INFERNAL, this.menu.getCurrentMana(), this.menu.getMaxMana()));
        
        this.addRenderableWidget(new ImageButton(this.leftPos + 80, this.topPos + 52, 20, 18, RecipeBookComponent.RECIPE_BUTTON_SPRITES, (button) -> {
            this.recipeBookComponent.toggleVisibility();
            this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
            ((ImageButton)button).setPosition(this.leftPos + 80, this.topPos + 52);
            this.manaGauge.setPosition(this.leftPos + 10, this.topPos + 6);
        }));
        this.addWidget(this.recipeBookComponent);
        this.setInitialFocus(this.recipeBookComponent);
    }

    @Override
    public void extractRenderState(@NotNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.manaGauge.setCurrentMana(this.menu.getCurrentMana());
        this.manaGauge.setMaxMana(this.menu.getMaxMana());
        if (this.recipeBookComponent.isVisible() && this.widthTooNarrow) {
            this.extractBackground(guiGraphics, mouseX, mouseY, partialTicks);
        } else {
            super.extractContents(guiGraphics, mouseX, mouseY, partialTicks);
        }
        guiGraphics.nextStratum();
        this.recipeBookComponent.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.recipeBookComponent.renderGhostRecipe(guiGraphics, this.leftPos, this.topPos, true, partialTicks); // FIXME Is this still a thing?
        guiGraphics.nextStratum();
        this.extractCarriedItem(guiGraphics, mouseX, mouseY);
        this.extractSnapbackItem(guiGraphics);
        this.extractTooltip(guiGraphics, mouseX, mouseY);
        this.recipeBookComponent.renderTooltip(guiGraphics, this.leftPos, this.topPos, mouseX, mouseY); // FIXME Conform to new naming scheme
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.recipeBookComponent.tick();
    }

    @Override
    public void recipesUpdated() {
        this.recipeBookComponent.recipesUpdated();
    }

    @Override
    public ArcaneRecipeBookComponent getRecipeBookComponent() {
        return this.recipeBookComponent;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor guiGraphics, int x, int y, float partialTicks) {
        // Render background texture
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
        
        // Animate supercharge burn indicator
        if (this.menu.isSupercharged()) {
            int burn = this.menu.getSuperchargeProgressionScaled();
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, BURN_SPRITE, 14, 14, 0, 14 - burn, this.leftPos + 44, this.topPos + 48 - burn, 14, burn);
        }

        // Animate progress indicator
        int cook = this.menu.getProcessProgressionScaled();
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, PROGRESS_SPRITE, 24, 16, 0, 0, this.leftPos + 78, this.topPos + 34, cook, 16);
    }

    @Override
    protected boolean isHovering(int p_97768_, int p_97769_, int p_97770_, int p_97771_, double p_97772_, double p_97773_) {
        return (!this.widthTooNarrow || !this.recipeBookComponent.isVisible()) && super.isHovering(p_97768_, p_97769_, p_97770_, p_97771_, p_97772_, p_97773_);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int buttonIndex) {
        if (this.recipeBookComponent.mouseClicked(mouseX, mouseY, buttonIndex)) {
            this.setFocused(this.recipeBookComponent);
            return true;
        } else {
            return this.widthTooNarrow && this.recipeBookComponent.isVisible() ? true : super.mouseClicked(mouseX, mouseY, buttonIndex);
        }
    }

    @Override
    protected boolean hasClickedOutside(double mouseX, double mouseY, int leftPos, int topPos, int buttonIndex) {
        boolean flag = mouseX < (double)leftPos || mouseY < (double)topPos || mouseX >= (double)(leftPos + this.imageWidth) || mouseY >= (double)(topPos + this.imageHeight);
        return this.recipeBookComponent.hasClickedOutside(mouseX, mouseY, this.leftPos, this.topPos, this.imageWidth, this.imageHeight, buttonIndex) && flag;
    }

    @Override
    protected void slotClicked(@NotNull Slot slot, int slotId, int mouseButton, @NotNull ClickType type) {
        super.slotClicked(slot, slotId, mouseButton, type);
        this.recipeBookComponent.slotClicked(slot);
    }
}
