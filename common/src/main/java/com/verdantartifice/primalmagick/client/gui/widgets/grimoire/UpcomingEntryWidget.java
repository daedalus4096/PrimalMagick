package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagick.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagick.common.research.ResearchDisciplines;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.awt.Color;

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
        this.icon = showIcon ? IndexIconFactory.fromEntryIcon(entry.iconOpt().orElse(null), false) : null;
        
        Minecraft mc = Minecraft.getInstance();
        MutableComponent tooltip = Component.empty();
        if (this.entry.key().getRootKey().equals(ResearchEntries.UNKNOWN_RESEARCH)) {
            // If the upcoming research entry is the unknown research placeholder, render a special message
            tooltip.append(Component.translatable("grimoire.primalmagick.unknown_upcoming"));
        } else {
            // Otherwise, list the prerequisites for the upcoming research entry
            tooltip.append(Component.translatable("grimoire.primalmagick.upcoming_tooltip_header"));
            for (ResearchEntryKey parent : this.entry.parents()) {
                ResearchEntry parentEntry = ResearchEntries.getEntry(mc.level.registryAccess(), parent);
                if (parentEntry == null && !parent.isKnownBy(mc.player)) {
                    tooltip.append(CommonComponents.NEW_LINE).append(Component.translatable("research.primalmagick." + parent.getRootKey() + ".text"));
                } else if (parentEntry != null && !parentEntry.key().isKnownBy(mc.player)) {
                    MutableComponent comp = Component.translatable(parentEntry.getNameTranslationKey());
                    if (!this.entry.disciplineKeyOpt().equals(parentEntry.disciplineKeyOpt()) && parentEntry.disciplineKeyOpt().isPresent()) {
                        ResearchDiscipline disc = ResearchDisciplines.getDiscipline(mc.level.registryAccess(), parentEntry.disciplineKeyOpt().get());
                        if (disc != null) {
                            comp.append(Component.literal(" ("));
                            comp.append(Component.translatable(disc.getNameTranslationKey()));
                            comp.append(Component.literal(")"));
                        }
                    }
                    tooltip.append(CommonComponents.NEW_LINE).append(comp);
                }
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
