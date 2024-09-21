package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

/**
 * Display widget for summarizing recipe mana costs.  Used on arcane recipe pages.
 * 
 * @author Daedalus4096
 */
public class ManaCostSummaryWidget extends AbstractWidget {
    protected static final ResourceLocation TEXTURE = PrimalMagick.resource("textures/gui/mana_cost_summary_widget.png");

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
                tooltip.append(CommonComponents.NEW_LINE).append(Component.translatable("label.primalmagick.crafting.mana", this.manaCosts.getAmount(source), sourceText));
            }
        }
        this.setTooltip(Tooltip.create(tooltip));
    }
    
    @Override
    public void renderWidget(GuiGraphics guiGraphics, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        // Render the base widget
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(this.getX(), this.getY(), 0.0F);
        guiGraphics.pose().scale(0.0625F, 0.0625F, 0.0625F);
        guiGraphics.blit(TEXTURE, 0, 0, 0, 0, 255, 255);
        guiGraphics.pose().popPose();
    }
    
    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput output) {
    }
}
