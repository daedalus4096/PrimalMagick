package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import java.awt.Color;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagick.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagick.common.research.ResearchDisciplines;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/**
 * GUI widget to announce the presence of future grimoire entries and their requirements.
 * 
 * @author Daedalus4096
 */
public class UpcomingEntryWidget extends AbstractWidget {
    protected ResearchEntry entry;
    protected AbstractIndexIcon icon;

    public UpcomingEntryWidget(int x, int y, Component text, ResearchEntry entry, boolean showIcon) {
        super(x, y, 123, 12, text);
        this.entry = entry;
        this.icon = showIcon ? IndexIconFactory.fromEntryIcon(entry.getIcon(), false) : null;
        
        Minecraft mc = Minecraft.getInstance();
        MutableComponent tooltip = Component.empty();
        tooltip.append(Component.translatable("grimoire.primalmagick.upcoming_tooltip_header"));
        
        for (SimpleResearchKey parent : this.entry.getParentResearch().getKeys()) {
            ResearchEntry parentEntry = ResearchEntries.getEntry(parent);
            if (parentEntry == null && !parent.isKnownByStrict(mc.player)) {
                tooltip.append(CommonComponents.NEW_LINE).append(Component.translatable("research.primalmagick." + parent.getRootKey() + ".text"));
            } else if (parentEntry != null && !parentEntry.getKey().isKnownByStrict(mc.player)) {
                MutableComponent comp = Component.translatable(parentEntry.getNameTranslationKey());
                if (!this.entry.getDisciplineKey().equals(parentEntry.getDisciplineKey())) {
                    ResearchDiscipline disc = ResearchDisciplines.getDiscipline(parentEntry.getDisciplineKey());
                    if (disc != null) {
                        comp.append(Component.literal(" ("));
                        comp.append(Component.translatable(disc.getNameTranslationKey()));
                        comp.append(Component.literal(")"));
                    }
                }
                tooltip.append(CommonComponents.NEW_LINE).append(comp);
            }
        }
        this.setTooltip(Tooltip.create(tooltip));
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft mc = Minecraft.getInstance();
        guiGraphics.pose().pushPose();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        int strWidth = mc.font.width(this.getMessage().getString());
        int dx = this.icon == null ? 0 : (this.icon.isLarge() ? 16 : 11);
        int dy = (this.height - mc.font.lineHeight) / 2;
        if (strWidth <= (this.width - dx)) {
            guiGraphics.drawString(mc.font, this.getMessage(), this.getX() + dx, this.getY() + dy, Color.GRAY.getRGB(), false);
            if (this.icon != null) {
                this.icon.render(guiGraphics, this.getX() - 2, this.getY() + dy - (this.icon.isLarge() ? 4 : 1));
            }
        } else {
            // If the button text is too long, scale it down to fit on one line
            float scale = (float)(this.width - dx) / (float)strWidth;
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(this.getX() + dx, this.getY() + dy + (1.0F * scale), 0.0F);
            guiGraphics.pose().scale(scale, scale, scale);
            guiGraphics.drawString(mc.font, this.getMessage(), 0, 0, Color.GRAY.getRGB(), false);
            guiGraphics.pose().popPose();
            if (this.icon != null) {
                this.icon.render(guiGraphics, this.getX() - 2, this.getY() + dy - (this.icon.isLarge() ? 4 : 1));
            }
        }
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
