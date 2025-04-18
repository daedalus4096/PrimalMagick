package com.verdantartifice.primalmagick.client.gui;

import com.verdantartifice.primalmagick.client.gui.recipe_book.ArcaneRecipeBookComponent;
import com.verdantartifice.primalmagick.client.gui.recipe_book.ArcaneRecipeUpdateListener;
import com.verdantartifice.primalmagick.client.gui.widgets.ManaGaugeWidget;
import com.verdantartifice.primalmagick.common.menus.DissolutionChamberMenu;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * GUI screen for the dissolution chamber block.
 * 
 * @author Daedalus4096
 */
public class DissolutionChamberScreen extends AbstractContainerScreenPM<DissolutionChamberMenu> implements ArcaneRecipeUpdateListener {
    protected static final Logger LOGGER = LogManager.getLogger();
    protected static final ResourceLocation TEXTURE = ResourceUtils.loc("textures/gui/dissolution_chamber.png");
    
    protected final ArcaneRecipeBookComponent recipeBookComponent = new ArcaneRecipeBookComponent();
    protected boolean widthTooNarrow;
    protected ManaGaugeWidget manaGauge;

    public DissolutionChamberScreen(DissolutionChamberMenu screenMenu, Inventory inv, Component titleIn) {
        super(screenMenu, inv, titleIn);
        this.titleLabelX = 27;
        this.inventoryLabelX = 27;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.manaGauge.setCurrentMana(this.menu.getCurrentMana());
        this.manaGauge.setMaxMana(this.menu.getMaxMana());
        if (this.recipeBookComponent.isVisible() && this.widthTooNarrow) {
            this.renderBg(guiGraphics, partialTicks, mouseX, mouseY);
            this.recipeBookComponent.render(guiGraphics, mouseX, mouseY, partialTicks);
        } else {
            super.render(guiGraphics, mouseX, mouseY, partialTicks);
            this.recipeBookComponent.render(guiGraphics, mouseX, mouseY, partialTicks);
            this.recipeBookComponent.renderGhostRecipe(guiGraphics, this.leftPos, this.topPos, true, partialTicks);
        }
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        this.recipeBookComponent.renderTooltip(guiGraphics, this.leftPos, this.topPos, mouseX, mouseY);
    }
    
    @Override
    protected void init() {
        super.init();
        this.widthTooNarrow = this.width < 379;
        this.recipeBookComponent.init(this.width, this.height, this.minecraft, this.widthTooNarrow, true, this.menu);
        this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
        
        this.manaGauge = this.addRenderableWidget(new ManaGaugeWidget(this.leftPos + 10, this.topPos + 6, Sources.EARTH, this.menu.getCurrentMana(), this.menu.getMaxMana()));
        
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
    protected void containerTick() {
        super.containerTick();
        this.recipeBookComponent.tick();
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int x, int y) {
        // Render background texture
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        
        // Animate progress indicator
        int cook = this.menu.getDissolutionProgressionScaled();
        guiGraphics.blit(TEXTURE, this.leftPos + 78, this.topPos + 34, 176, 0, cook + 1, 16);
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
    protected void slotClicked(Slot p_97778_, int p_97779_, int p_97780_, ClickType p_97781_) {
        super.slotClicked(p_97778_, p_97779_, p_97780_, p_97781_);
        this.recipeBookComponent.slotClicked(p_97778_);
    }

    @Override
    public void recipesUpdated() {
        this.recipeBookComponent.recipesUpdated();
    }

    @Override
    public ArcaneRecipeBookComponent getRecipeBookComponent() {
        return this.recipeBookComponent;
    }
}
