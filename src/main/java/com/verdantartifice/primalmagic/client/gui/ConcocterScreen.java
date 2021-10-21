package com.verdantartifice.primalmagic.client.gui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.gui.widgets.ManaGaugeWidget;
import com.verdantartifice.primalmagic.common.containers.ConcocterContainer;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * GUI screen for concocter block.
 * 
 * @author Daedalus4096
 */
public class ConcocterScreen extends AbstractContainerScreen<ConcocterContainer> {
    protected static final Logger LOGGER = LogManager.getLogger();
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/concocter.png");
    protected static final ResourceLocation RECIPE_BUTTON_LOCATION = new ResourceLocation("textures/gui/recipe_button.png");
    
    protected ManaGaugeWidget manaGauge;

    public ConcocterScreen(ConcocterContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.titleLabelX = 44;
        this.inventoryLabelX = 27;
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.manaGauge.setCurrentMana(this.menu.getCurrentMana());
        this.manaGauge.setMaxMana(this.menu.getMaxMana());
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }
    
    @Override
    protected void init() {
        super.init();
        this.manaGauge = this.addRenderableWidget(new ManaGaugeWidget(this.leftPos + 10, this.topPos + 6, Source.INFERNAL, this.menu.getCurrentMana(), this.menu.getMaxMana()));
        
        // Add arcane recipe book button
        this.addRenderableWidget(new ImageButton(this.leftPos + 105, this.topPos + 52, 20, 18, 0, 0, 19, RECIPE_BUTTON_LOCATION, (button) -> {
            LOGGER.info("Toggling concoter recipe book");
//            this.recipeBookComponent.toggleVisibility();
//            this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
//            ((ImageButton)button).setPosition(this.leftPos + 105, this.topPos + 52);
        }));
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        // Render background texture
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        
        // Animate spin progress indicator
        int cook = this.menu.getCookProgressionScaled();
        this.blit(matrixStack, this.leftPos + 103, this.topPos + 34, 176, 0, cook + 1, 16);
    }
}
