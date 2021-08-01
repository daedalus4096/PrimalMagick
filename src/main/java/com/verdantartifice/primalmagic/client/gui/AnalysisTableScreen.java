package com.verdantartifice.primalmagic.client.gui;

import java.awt.Color;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.gui.widgets.AffinityWidget;
import com.verdantartifice.primalmagic.client.gui.widgets.research_table.KnowledgeTotalWidget;
import com.verdantartifice.primalmagic.common.affinities.AffinityManager;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.containers.AnalysisTableContainer;
import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.misc.AnalysisActionPacket;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;

import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * GUI screen for analysis table block.
 * 
 * @author Daedalus4096
 */
public class AnalysisTableScreen extends AbstractContainerScreen<AnalysisTableContainer> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/analysis_table.png");
    private static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/analysis_button.png");
    
    protected Level world;

    public AnalysisTableScreen(AnalysisTableContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.world = inv.player.level;
    }
    
    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.initWidgets();
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        this.minecraft.getTextureManager().bindForSetup(TEXTURE);
        this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }
    
    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
        Component text = null;
        ItemStack lastScannedStack = this.menu.getLastScannedStack();
        
        // Generate text in the case that no item has been analyzed, or the item has no affinities
        if (lastScannedStack == null || lastScannedStack.isEmpty()) {
            text = new TranslatableComponent("primalmagic.analysis.no_item");
        } else {
            SourceList sources = AffinityManager.getInstance().getAffinityValues(lastScannedStack, this.world);
            if (sources == null || sources.isEmpty()) {
                text = new TranslatableComponent("primalmagic.analysis.no_affinities");
            }
        }
        
        // Render any generated text
        if (text != null) {
            int width = this.font.width(text.getString());
            int x = 1 + (this.getXSize() - width) / 2;
            int y = 10 + (16 - this.font.lineHeight) / 2;
            this.font.draw(matrixStack, text, x, y, Color.BLACK.getRGB());
        }
    }

    protected void initWidgets() {
        this.clearWidgets();
        
        this.addRenderableWidget(new ImageButton(this.leftPos + 78, this.topPos + 34, 20, 18, 0, 0, 19, BUTTON_TEXTURE, (button) -> {
            PacketHandler.sendToServer(new AnalysisActionPacket(this.menu.containerId));
        }));
        
        // Render observation tracker widget
        this.addRenderableWidget(new KnowledgeTotalWidget(this.leftPos + 8, this.topPos + 60, IPlayerKnowledge.KnowledgeType.OBSERVATION));
        
        // Show affinity widgets, if the last scanned stack has affinities
        ItemStack lastScannedStack = this.menu.getLastScannedStack();
        if (lastScannedStack != null && !lastScannedStack.isEmpty()) {
            SourceList sources = AffinityManager.getInstance().getAffinityValues(lastScannedStack, this.world);
            if (!sources.isEmpty()) {
                int widgetSetWidth = sources.getSourcesSorted().size() * 18;
                int x = this.leftPos + 1 + (this.getXSize() - widgetSetWidth) / 2;
                int y = this.topPos + 10;
                for (Source source : sources.getSourcesSorted()) {
                    this.addRenderableWidget(new AffinityWidget(source, sources.getAmount(source), x, y));
                    x += 18;
                }
            }
        }
    }
}
