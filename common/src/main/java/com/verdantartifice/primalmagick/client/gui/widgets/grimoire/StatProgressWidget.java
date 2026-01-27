package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagick.common.stats.Stat;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

/**
 * Display widget for showing progress towards a statistic threshold on the requirements page.
 * 
 * @author Daedalus4096
 */
public class StatProgressWidget extends AbstractWidget {
    private static final Identifier UNKNOWN_TEXTURE = ResourceUtils.loc("research/research_unknown");
    private static final Identifier COMPLETE = ResourceUtils.loc("grimoire/complete");
    private static final Identifier PROGRESS_FG = ResourceUtils.loc("grimoire/progress_foreground");
    private static final Identifier PROGRESS_BG = ResourceUtils.loc("grimoire/progress_background");

    protected final Stat stat;
    protected final int maxValue;
    protected final int currentValue;
    protected final boolean isComplete;
    protected final Identifier iconLoc;
    protected MutableComponent lastTooltip = Component.empty();
    protected MutableComponent tooltip = Component.empty();

    public StatProgressWidget(Stat stat, int maxProgressValue, int x, int y, boolean isComplete) {
        super(x, y, 16, 18, Component.empty());
        Minecraft mc = Minecraft.getInstance();
        this.stat = stat;
        this.maxValue = maxProgressValue;
        this.currentValue = StatsManager.getValue(mc.player, stat);
        this.isComplete = isComplete;
        this.iconLoc = stat.iconLocationOpt().orElse(UNKNOWN_TEXTURE);
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        Minecraft mc = Minecraft.getInstance();

        // Render the icon
        pGuiGraphics.pose().pushMatrix();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        pGuiGraphics.pose().translate(this.getX(), this.getY());
        pGuiGraphics.pose().scale(0.0625F, 0.0625F);
        pGuiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.iconLoc, 0, 0, 32, 32);
        pGuiGraphics.pose().popMatrix();
        
        if (this.isComplete) {
            // Render completion checkmark if appropriate
            pGuiGraphics.pose().pushMatrix();
            pGuiGraphics.pose().translate(this.getX() + 8, this.getY());
            pGuiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, COMPLETE, 0, 0, 10, 10);
            pGuiGraphics.pose().popMatrix();
        }
        
        // Draw progress bar background
        pGuiGraphics.pose().pushMatrix();
        pGuiGraphics.pose().translate(this.getX(), this.getY() + 17);
        pGuiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, PROGRESS_BG, 0, 0, 16, 2);
        pGuiGraphics.pose().popMatrix();
        
        // Draw progress bar foreground
        int px = this.getProgressionScaled();
        pGuiGraphics.pose().pushMatrix();
        pGuiGraphics.pose().translate(this.getX(), this.getY() + 17);
        pGuiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, PROGRESS_FG, 0, 0, px, 2);
        pGuiGraphics.pose().popMatrix();
        
        // Prepare the tooltip
        this.lastTooltip = this.tooltip;
        this.tooltip = Component.empty();
        this.stat.getHintTranslationKey().ifPresentOrElse(hintTranslationKey -> {
            if (mc.hasShiftDown()) {
                this.tooltip.append(Component.translatable(hintTranslationKey));
            } else {
                this.tooltip.append(this.getStatDescription());
                this.tooltip.append(CommonComponents.NEW_LINE);
                this.tooltip.append(Component.translatable("tooltip.primalmagick.more_info").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            }
        }, () -> {
            this.tooltip.append(this.getStatDescription());
        });
        if (!this.lastTooltip.equals(this.tooltip)) {
            this.setTooltip(Tooltip.create(this.tooltip));
        }
    }
    
    protected Component getStatDescription() {
        Minecraft mc = Minecraft.getInstance();
        Component baseDescription = Component.translatable(this.stat.getTranslationKey());
        String currentValue = this.stat.formatter().format(Math.min(StatsManager.getValue(mc.player, this.stat), this.maxValue));
        String maxValue = this.stat.formatter().format(this.maxValue);
        return Component.translatable("tooltip.primalmagick.stat_progress", baseDescription, currentValue, maxValue);
    }
    
    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent event, boolean isDoubleClick) {
        // Disable click behavior
        return false;
    }

    @Override
    public void updateWidgetNarration(@NotNull NarrationElementOutput output) {
    }

    protected int getProgressionScaled() {
        // Determine how much of the progress meter to show
        int i = this.currentValue;
        int j = this.maxValue;
        return j != 0 && i != 0 ? (int)(16.0D * ((double)i / (double)j)) : 0;
    }
}
