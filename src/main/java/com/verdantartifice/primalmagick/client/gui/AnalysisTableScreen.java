package com.verdantartifice.primalmagick.client.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.mutable.MutableObject;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.gui.widgets.AffinityWidget;
import com.verdantartifice.primalmagick.client.gui.widgets.research_table.KnowledgeTotalWidget;
import com.verdantartifice.primalmagick.common.affinities.AffinityManager;
import com.verdantartifice.primalmagick.common.menus.AnalysisTableMenu;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.misc.AnalysisActionPacket;
import com.verdantartifice.primalmagick.common.research.KnowledgeType;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * GUI screen for analysis table block.
 * 
 * @author Daedalus4096
 */
public class AnalysisTableScreen extends AbstractContainerScreen<AnalysisTableMenu> {
    private static final ResourceLocation TEXTURE = PrimalMagick.resource("textures/gui/analysis_table.png");
    
    protected Level world;
    protected final List<AffinityWidget> affinityWidgets = new ArrayList<>();

    public AnalysisTableScreen(AnalysisTableMenu screenMenu, Inventory inv, Component titleIn) {
        super(screenMenu, inv, titleIn);
        this.world = inv.player.level();
    }
    
    @Override
    protected void init() {
        super.init();
        this.initControlWidgets();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.initAffinityWidgets();
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }
    
    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        MutableObject<Component> text = new MutableObject<>(null);
        ItemStack lastScannedStack = this.menu.getLastScannedStack();
        
        // Generate text in the case that no item has been analyzed, or the item has no affinities
        if (lastScannedStack == null || lastScannedStack.isEmpty()) {
            text.setValue(Component.translatable("label.primalmagick.analysis.no_item"));
        } else {
            AffinityManager.getInstance().getAffinityValues(lastScannedStack, this.world).ifPresentOrElse(sources -> {
                if (sources == null || sources.isEmpty()) {
                    text.setValue(Component.translatable("label.primalmagick.analysis.no_affinities"));
                }
            }, () -> {
                text.setValue(Component.translatable("label.primalmagick.analysis.calculating"));
            });
        }
        
        // Render any generated text
        if (text.getValue() != null) {
            int width = this.font.width(text.getValue().getString());
            int x = 1 + (this.getXSize() - width) / 2;
            int y = 10 + (16 - this.font.lineHeight) / 2;
            guiGraphics.drawString(this.font, text.getValue(), x, y, Color.BLACK.getRGB(), false);
        }
    }

    protected void initControlWidgets() {
        this.addRenderableWidget(new AnalyzeButton(this.menu, this.leftPos, this.topPos));
        this.addRenderableWidget(new KnowledgeTotalWidget(this.leftPos + 8, this.topPos + 60, KnowledgeType.OBSERVATION));
    }
    
    protected void initAffinityWidgets() {
        // Remove any previously displayed affinity widgets
        this.affinityWidgets.forEach(widget -> this.removeWidget(widget));
        this.affinityWidgets.clear();
        
        // Show affinity widgets, if the last scanned stack has affinities
        ItemStack lastScannedStack = this.menu.getLastScannedStack();
        if (lastScannedStack != null && !lastScannedStack.isEmpty()) {
            AffinityManager.getInstance().getAffinityValues(lastScannedStack, this.world).ifPresent(sources -> {
                if (!sources.isEmpty()) {
                    int widgetSetWidth = sources.getSourcesSorted().size() * 18;
                    int x = this.leftPos + 1 + (this.getXSize() - widgetSetWidth) / 2;
                    int y = this.topPos + 10;
                    for (Source source : sources.getSourcesSorted()) {
                        this.affinityWidgets.add(this.addRenderableWidget(new AffinityWidget(source, sources.getAmount(source), x, y)));
                        x += 18;
                    }
                }
            });
        }
    }
    
    protected static class AnalyzeButton extends ImageButton {
        protected static final WidgetSprites BUTTON_SPRITES = new WidgetSprites(PrimalMagick.resource("analysis_table/button"), PrimalMagick.resource("analysis_table/button_highlighted"));
        protected static final Component ANALYZE_BUTTON_TOOLTIP_1 = Component.translatable("tooltip.primalmagick.analyze_button.1");
        protected static final Component ANALYZE_BUTTON_TOOLTIP_2 = Component.translatable("tooltip.primalmagick.analyze_button.2").withStyle(ChatFormatting.RED);

        public AnalyzeButton(AnalysisTableMenu menu, int leftPos, int topPos) {
            super(leftPos + 78, topPos + 34, 20, 18, BUTTON_SPRITES, button -> {
                PacketHandler.sendToServer(new AnalysisActionPacket(menu.containerId));
            });
            this.setTooltip(Tooltip.create(CommonComponents.joinLines(ANALYZE_BUTTON_TOOLTIP_1, ANALYZE_BUTTON_TOOLTIP_2)));
        }
    }
}
