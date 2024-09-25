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
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class ExpertiseProgressWidget extends AbstractWidget {
    protected static final ResourceLocation GRIMOIRE_TEXTURE = ResourceUtils.loc("textures/gui/grimoire.png");
    
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
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        // Render the icon
        this.tier.getIconDefinition().ifPresent(iconDef -> GuiUtils.renderIconFromDefinition(pGuiGraphics, iconDef, this.getX(), this.getY()));
        
        if (this.isComplete) {
            // Render completion checkmark if appropriate
            pGuiGraphics.pose().pushPose();
            pGuiGraphics.pose().translate(this.getX() + 8, this.getY(), 100.0F);
            pGuiGraphics.blit(GRIMOIRE_TEXTURE, 0, 0, 159, 207, 10, 10);
            pGuiGraphics.pose().popPose();
        }
        
        // Draw progress bar background
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate(this.getX(), this.getY() + 17, 0.0F);
        pGuiGraphics.blit(GRIMOIRE_TEXTURE, 0, 0, 0, 234, 16, 2);
        pGuiGraphics.pose().popPose();
        
        // Draw progress bar foreground
        int px = this.getProgressionScaled();
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate(this.getX(), this.getY() + 17, 1.0F);
        pGuiGraphics.blit(GRIMOIRE_TEXTURE, 0, 0, 0, 232, px, 2);
        pGuiGraphics.pose().popPose();
        
        // Prepare the tooltip
        Minecraft mc = Minecraft.getInstance();
        this.lastTooltip = this.tooltip;
        this.tooltip = Component.empty();
        ExpertiseManager.getStat(mc.player.level().registryAccess(), this.disciplineKey).flatMap(Stat::getHintTranslationKey).ifPresentOrElse(hintTranslationKey -> {
            if (Screen.hasShiftDown()) {
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
        Optional<Stat> statOpt = ExpertiseManager.getStat(mc.player.level().registryAccess(), this.disciplineKey);
        if (statOpt.isPresent()) {
            Stat stat = statOpt.get();
            Component baseDescription = Component.translatable(stat.getTranslationKey());
            String currentValue = stat.formatter().format(Math.min(this.currentValue, this.maxValue));
            String maxValue = stat.formatter().format(this.maxValue);
            return Component.translatable("tooltip.primalmagick.stat_progress", baseDescription, currentValue, maxValue);
        } else {
            return Component.empty();
        }
    }
    
    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput output) {
    }

    protected int getProgressionScaled() {
        // Determine how much of the progress meter to show
        int i = this.currentValue;
        int j = this.maxValue;
        return j != 0 && i != 0 ? (int)(16.0D * ((double)i / (double)j)) : 0;
    }
}
