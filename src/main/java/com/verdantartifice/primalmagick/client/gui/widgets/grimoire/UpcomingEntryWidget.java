package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.client.util.GuiUtils;
import com.verdantartifice.primalmagick.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagick.common.research.ResearchDisciplines;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

/**
 * GUI widget to announce the presence of future grimoire entries and their requirements.
 * 
 * @author Daedalus4096
 */
public class UpcomingEntryWidget extends AbstractWidget {
    protected ResearchEntry entry;

    public UpcomingEntryWidget(int x, int y, Component text, ResearchEntry entry) {
        super(x, y, 123, 12, text);
        this.entry = entry;
    }

    @Override
    public void renderButton(PoseStack matrixStack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft mc = Minecraft.getInstance();
        matrixStack.pushPose();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        int strWidth = mc.font.width(this.getMessage().getString());
        int dy = (this.height - mc.font.lineHeight) / 2;
        if (strWidth <= this.width) {
            mc.font.draw(matrixStack, this.getMessage(), this.x, this.y + dy, Color.GRAY.getRGB());
        } else {
            // If the button text is too long, scale it down to fit on one line
            float scale = (float)this.width / (float)strWidth;
            matrixStack.pushPose();
            matrixStack.translate(this.x, this.y + dy + (1.0F * scale), 0.0F);
            matrixStack.scale(scale, scale, scale);
            mc.font.draw(matrixStack, this.getMessage(), 0, 0, Color.GRAY.getRGB());
            matrixStack.popPose();
        }
        matrixStack.popPose();

        // When hovering, show a tooltip with the missing requirements
        if (this.isHoveredOrFocused()) {
            List<Component> tooltip = new ArrayList<>();
            tooltip.add(new TranslatableComponent("primalmagick.grimoire.upcoming_tooltip_header"));
            
            for (SimpleResearchKey parent : this.entry.getParentResearch().getKeys()) {
                ResearchEntry parentEntry = ResearchEntries.getEntry(parent);
                if (parentEntry == null) {
                    tooltip.add(new TranslatableComponent("primalmagick.research." + parent.getRootKey() + ".text"));
                } else if (!parentEntry.getKey().isKnownByStrict(mc.player)) {
                    MutableComponent comp = new TranslatableComponent(parentEntry.getNameTranslationKey());
                    if (!this.entry.getDisciplineKey().equals(parentEntry.getDisciplineKey())) {
                        ResearchDiscipline disc = ResearchDisciplines.getDiscipline(parentEntry.getDisciplineKey());
                        if (disc != null) {
                            comp.append(new TextComponent(" ("));
                            comp.append(new TranslatableComponent(disc.getNameTranslationKey()));
                            comp.append(new TextComponent(")"));
                        }
                    }
                    tooltip.add(comp);
                }
            }
            GuiUtils.renderCustomTooltip(matrixStack, tooltip, this.x, this.y);
        }
    }

    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }

    @Override
    public void updateNarration(NarrationElementOutput p_169152_) {
    }
}
