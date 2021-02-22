package com.verdantartifice.primalmagic.client.gui;

import java.awt.Color;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.gui.widgets.AffinityWidget;
import com.verdantartifice.primalmagic.client.gui.widgets.research_table.KnowledgeTotalWidget;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.containers.AnalysisTableContainer;
import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.misc.AnalysisActionPacket;
import com.verdantartifice.primalmagic.common.sources.AffinityManager;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * GUI screen for analysis table block.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class AnalysisTableScreen extends ContainerScreen<AnalysisTableContainer> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/analysis_table.png");
    private static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/analysis_button.png");
    
    protected World world;

    public AnalysisTableScreen(AnalysisTableContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.world = inv.player.world;
    }
    
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.initWidgets();
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
        ITextComponent text = null;
        ItemStack lastScannedStack = this.container.getLastScannedStack();
        
        // Generate text in the case that no item has been analyzed, or the item has no affinities
        if (lastScannedStack == null || lastScannedStack.isEmpty()) {
            text = new TranslationTextComponent("primalmagic.analysis.no_item");
        } else {
            SourceList sources = AffinityManager.getAffinities(lastScannedStack, this.world);
            if (sources == null || sources.isEmpty()) {
                text = new TranslationTextComponent("primalmagic.analysis.no_affinities");
            }
        }
        
        // Render any generated text
        if (text != null) {
            int width = this.font.getStringWidth(text.getString());
            int x = 1 + (this.getXSize() - width) / 2;
            int y = 10 + (16 - this.font.FONT_HEIGHT) / 2;
            this.font.drawString(matrixStack, text.getString(), x, y, Color.BLACK.getRGB());
        }
    }

    protected void initWidgets() {
        this.buttons.clear();
        this.children.clear();
        
        this.addButton(new ImageButton(this.guiLeft + 78, this.guiTop + 34, 20, 18, 0, 0, 19, BUTTON_TEXTURE, (button) -> {
            PacketHandler.sendToServer(new AnalysisActionPacket(this.container.windowId));
        }));
        
        // Render observation tracker widget
        this.addButton(new KnowledgeTotalWidget(this.guiLeft + 8, this.guiTop + 60, IPlayerKnowledge.KnowledgeType.OBSERVATION));
        
        // Show affinity widgets, if the last scanned stack has affinities
        ItemStack lastScannedStack = this.container.getLastScannedStack();
        if (lastScannedStack != null && !lastScannedStack.isEmpty()) {
            SourceList sources = AffinityManager.getAffinities(lastScannedStack, this.world);
            if (!sources.isEmpty()) {
                int widgetSetWidth = sources.getSourcesSorted().size() * 18;
                int x = this.guiLeft + 1 + (this.getXSize() - widgetSetWidth) / 2;
                int y = this.guiTop + 10;
                for (Source source : sources.getSourcesSorted()) {
                    this.addButton(new AffinityWidget(source, sources.getAmount(source), x, y));
                    x += 18;
                }
            }
        }
    }
}
