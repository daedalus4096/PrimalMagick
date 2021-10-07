package com.verdantartifice.primalmagic.client.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.gui.widgets.ManaCostWidget;
import com.verdantartifice.primalmagic.common.containers.ArcaneWorkbenchContainer;
import com.verdantartifice.primalmagic.common.crafting.IArcaneRecipe;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;

import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * GUI screen for arcane workbench block.
 * 
 * @author Daedalus4096
 */
public class ArcaneWorkbenchScreen extends AbstractContainerScreen<ArcaneWorkbenchContainer> {
    protected static final Logger LOGGER = LogManager.getLogger();
    private static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/arcane_workbench.png");
    private static final ResourceLocation RECIPE_BUTTON_LOCATION = new ResourceLocation("textures/gui/recipe_button.png");
    
    protected List<ManaCostWidget> costWidgets = new ArrayList<>();

    public ArcaneWorkbenchScreen(ArcaneWorkbenchContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageHeight = 183;
    }
    
    @Override
    protected void init() {
        super.init();
        this.initCostWidgets();
        
        // Add arcane recipe book button
        this.addRenderableWidget(new ImageButton(this.leftPos + 105, this.topPos + 69, 20, 18, 0, 0, 19, RECIPE_BUTTON_LOCATION, (button) -> {
            LOGGER.info("Toggling arcane recipe book");
        }));
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.adjustCostWidgets();
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }
    
    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
        // Generate text in the case that the current recipe, or lack there of, does not have a mana cost
        IArcaneRecipe activeArcaneRecipe = this.menu.getActiveArcaneRecipe();
        if (activeArcaneRecipe == null || activeArcaneRecipe.getManaCosts() == null || activeArcaneRecipe.getManaCosts().isEmpty()) {
            Component text = new TranslatableComponent("primalmagic.crafting.no_mana");
            int width = this.font.width(text.getString());
            int x = 1 + (this.getXSize() - width) / 2;
            int y = 10 + (16 - this.font.lineHeight) / 2;
            this.font.draw(matrixStack, text, x, y, Color.BLACK.getRGB());
        }
    }
    
    protected void initCostWidgets() {
        this.costWidgets.clear();
        int widgetSetWidth = Source.SORTED_SOURCES.size() * 18;
        int x = this.leftPos + 1 + (this.getXSize() - widgetSetWidth) / 2;
        int y = this.topPos + 10;
        for (Source source : Source.SORTED_SOURCES) {
            this.costWidgets.add(this.addRenderableWidget(new ManaCostWidget(source, 0, x, y)));
            x += 18;
        }
    }
    
    protected void adjustCostWidgets() {
        IArcaneRecipe activeArcaneRecipe = this.menu.getActiveArcaneRecipe();
        if (activeArcaneRecipe != null) {
            SourceList manaCosts = activeArcaneRecipe.getManaCosts();
            int widgetSetWidth = manaCosts.getSourcesSorted().size() * 18;
            for (ManaCostWidget widget : this.costWidgets) {
                int amount = manaCosts.getAmount(widget.getSource());
                widget.visible = (amount > 0);
                widget.setAmount(amount);
                widget.x = this.leftPos + 1 + (this.getXSize() - widgetSetWidth) / 2;
            }
        } else {
            this.costWidgets.forEach(widget -> {
                widget.visible = false;
            });
        }
    }
}
