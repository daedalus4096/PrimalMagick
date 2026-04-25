package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

/**
 * Display widget for summarizing recipe mana costs.  Used on arcane recipe pages.
 * 
 * @author Daedalus4096
 */
public class ManaCostSummaryWidget extends AbstractWidget {
    protected static final Identifier TEXTURE = ResourceUtils.loc("mana_cost_summary_widget");
    protected static final DecimalFormat MANA_FORMATTER = new DecimalFormat("#######.##");

    protected SourceList manaCosts;
    
    public ManaCostSummaryWidget(SourceList manaCosts, int x, int y) {
        super(x, y, 16, 16, Component.empty());
        this.manaCosts = manaCosts;
        
        Minecraft mc = Minecraft.getInstance();
        MutableComponent tooltip = Component.empty();
        if (this.manaCosts.isEmpty()) {
            tooltip.append(Component.translatable("label.primalmagick.crafting.no_mana"));
        } else {
            tooltip.append(Component.translatable("label.primalmagick.crafting.mana_cost_header"));
            for (Source source : this.manaCosts.getSourcesSorted()) {
                boolean discovered = source.isDiscovered(mc.player);
                Component sourceText = discovered ? 
                        source.getNameText() :
                        Component.translatable(Source.getUnknownTranslationKey());
                tooltip.append(CommonComponents.NEW_LINE).append(Component.translatable("label.primalmagick.crafting.mana",
                        MANA_FORMATTER.format(this.manaCosts.getAmount(source) / 100D), sourceText));
            }
        }
        this.setTooltip(Tooltip.create(tooltip));
    }
    
    @Override
    public void extractWidgetRenderState(@NotNull GuiGraphicsExtractor pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        // Render the base widget
        pGuiGraphics.pose().pushMatrix();
        pGuiGraphics.pose().translate(this.getX(), this.getY());
        pGuiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, TEXTURE, 0, 0, 16, 16);
        pGuiGraphics.pose().popMatrix();
    }
    
    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent event, boolean isDoubleClick) {
        // Disable click behavior
        return false;
    }

    @Override
    public void updateWidgetNarration(@NotNull NarrationElementOutput output) {
    }
}
