package com.verdantartifice.primalmagick.client.gui;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.gui.widgets.EssenceCaskWidget;
import com.verdantartifice.primalmagick.common.containers.EssenceCaskContainer;
import com.verdantartifice.primalmagick.common.items.essence.EssenceType;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * GUI screen for the essence cask block.
 * 
 * @author Daedalus4096
 */
public class EssenceCaskScreen extends AbstractContainerScreen<EssenceCaskContainer> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagick.MODID, "textures/gui/essence_cask.png");
    
    protected final List<EssenceCaskWidget> caskWidgets = new ArrayList<>();

    public EssenceCaskScreen(EssenceCaskContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageWidth = 176;
        this.imageHeight = 222;
        this.inventoryLabelY = this.imageHeight - 94;
    }
    
    @Override
    protected void init() {
        super.init();
        for (int row = 0; row < EssenceType.values().length; row++) {
            for (int col = 0; col < Source.SORTED_SOURCES.size(); col++) {
                EssenceType cellType = EssenceType.values()[row];
                Source cellSource = Source.SORTED_SOURCES.get(col);
                // TODO Show correct amounts
                this.caskWidgets.add(this.addRenderableWidget(new EssenceCaskWidget(cellType, cellSource, 0, this.leftPos + 8 + col * 18, this.topPos + 18 + row * 18)));
            }
        }
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
        
        for (EssenceCaskWidget widget : this.caskWidgets) {
            if (widget.isHoveredOrFocused()) {
                renderSlotHighlight(matrixStack, widget.x, widget.y, this.getBlitOffset(), this.slotColor);
                widget.renderToolTip(matrixStack, mouseX, mouseY);
            }
        }
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        // Render background texture
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }
}
