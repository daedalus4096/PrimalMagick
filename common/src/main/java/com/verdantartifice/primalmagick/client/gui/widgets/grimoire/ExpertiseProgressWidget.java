package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.client.util.GuiUtils;
import com.verdantartifice.primalmagick.common.research.ResearchTier;
import com.verdantartifice.primalmagick.common.research.keys.ResearchDisciplineKey;
import com.verdantartifice.primalmagick.common.stats.ExpertiseManager;
import com.verdantartifice.primalmagick.common.stats.Stat;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
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

import java.util.Optional;

public class ExpertiseProgressWidget extends AbstractWidget {
    private static final Identifier COMPLETE = ResourceUtils.loc("grimoire/complete");
    private static final Identifier PROGRESS_FG = ResourceUtils.loc("grimoire/progress_foreground");
    private static final Identifier PROGRESS_BG = ResourceUtils.loc("grimoire/progress_background");
    
    protected final ResearchDisciplineKey disciplineKey;
    protected final ResearchTier tier;
    protected final int maxValue;
    protected final int currentValue;
    protected final boolean isComplete;
    protected MutableComponent lastTooltip = Component.empty();
    protected MutableComponent tooltip = Component.empty();
    
    public ExpertiseProgressWidget(ResearchDisciplineKey disciplineKey, ResearchTier tier, int maxProgressValue, int x, int y, boolean isComplete) {
        super(x, y, 16, 18, Component.empty());
        Minecraft mc = Minecraft.getInstance();
        this.disciplineKey = disciplineKey;
        this.tier = tier;
        this.maxValue = maxProgressValue;
        this.currentValue = ExpertiseManager.getValue(mc.player, disciplineKey).orElse(0);
        this.isComplete = isComplete;
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }

        // Render the icon
        this.tier.getIconDefinition().ifPresent(iconDef -> GuiUtils.renderIconFromDefinition(pGuiGraphics, iconDef, this.getX(), this.getY()));
        
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
        ExpertiseManager.getStat(mc.player.registryAccess(), this.disciplineKey).flatMap(Stat::getHintTranslationKey).ifPresentOrElse(hintTranslationKey -> {
            if (mc.hasShiftDown()) {
                this.tooltip.append(Component.translatable(hintTranslationKey));
            } else {
                this.tooltip.append(this.getStatDescription());
                this.tooltip.append(CommonComponents.NEW_LINE);
                this.tooltip.append(Component.translatable("tooltip.primalmagick.more_info").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            }
        }, () -> this.tooltip.append(this.getStatDescription()));
        if (!this.lastTooltip.equals(this.tooltip)) {
            this.setTooltip(Tooltip.create(this.tooltip));
        }
    }

    protected Component getStatDescription() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            Optional<Stat> statOpt = ExpertiseManager.getStat(mc.player.registryAccess(), this.disciplineKey);
            if (statOpt.isPresent()) {
                Stat stat = statOpt.get();
                Component baseDescription = Component.translatable(stat.getTranslationKey());
                String currentValue = stat.formatter().format(Math.min(this.currentValue, this.maxValue));
                String maxValue = stat.formatter().format(this.maxValue);
                return Component.translatable("tooltip.primalmagick.stat_progress", baseDescription, currentValue, maxValue);
            }
        }
        return Component.empty();
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
