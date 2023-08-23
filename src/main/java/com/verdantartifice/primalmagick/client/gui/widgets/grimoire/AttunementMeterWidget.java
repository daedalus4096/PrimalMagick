package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import java.awt.Color;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.attunements.AttunementManager;
import com.verdantartifice.primalmagick.common.attunements.AttunementType;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class AttunementMeterWidget extends AbstractWidget {
    private static final ResourceLocation TEXTURE = PrimalMagick.resource("textures/gui/attunement_meter.png");

    protected final Source source;
    protected final Color permanentColor;
    protected final Color inducedColor;
    protected final Color temporaryColor;
    
    public AttunementMeterWidget(@Nonnull Source source, int x, int y) {
        super(x, y, 12, 102, Component.empty());
        this.source = source;
        
        Color baseColor = new Color(this.source.getColor());
        this.permanentColor = baseColor.darker();
        this.inducedColor = baseColor;
        this.temporaryColor = baseColor.brighter();
        
        Minecraft mc = Minecraft.getInstance();
        int p = AttunementManager.getAttunement(mc.player, this.source, AttunementType.PERMANENT);
        int i = AttunementManager.getAttunement(mc.player, this.source, AttunementType.INDUCED);
        int t = AttunementManager.getAttunement(mc.player, this.source, AttunementType.TEMPORARY);
        MutableComponent tooltip = Component.translatable("grimoire.primalmagick.attunement_meter.tooltip.header", this.source.getNameText()).append(CommonComponents.NEW_LINE);
        tooltip.append(Component.translatable("grimoire.primalmagick.attunement_meter.tooltip.permanent", p)).append(CommonComponents.NEW_LINE);
        if (i > 0) {
            tooltip.append(Component.translatable("grimoire.primalmagick.attunement_meter.tooltip.induced", i)).append(CommonComponents.NEW_LINE);
        }
        tooltip.append(Component.translatable("grimoire.primalmagick.attunement_meter.tooltip.temporary", t));
        this.setTooltip(Tooltip.create(tooltip));
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        // Render attunement meter
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        Minecraft mc = Minecraft.getInstance();
        
        int p = AttunementManager.getAttunement(mc.player, this.source, AttunementType.PERMANENT);
        int i = AttunementManager.getAttunement(mc.player, this.source, AttunementType.INDUCED);
        int t = AttunementManager.getAttunement(mc.player, this.source, AttunementType.TEMPORARY);

        // Render permanent meter bar
        guiGraphics.setColor(this.permanentColor.getRed() / 255.0F, this.permanentColor.getGreen() / 255.0F, this.permanentColor.getBlue() / 255.0F, 1.0F);
        guiGraphics.blit(TEXTURE, this.getX() + 1, this.getY() + 1 + (100 - Mth.clamp(p, 0, 100)), 0, 10, 10, Mth.clamp(p, 0, 100));
        
        // Render induced meter bar
        guiGraphics.setColor(this.inducedColor.getRed() / 255.0F, this.inducedColor.getGreen() / 255.0F, this.inducedColor.getBlue() / 255.0F, 1.0F);
        guiGraphics.blit(TEXTURE, this.getX() + 1, this.getY() + 1 + (100 - Mth.clamp(p + i, 0, 100)), 0, 10, 10, Mth.clamp(i, 0, 100 - p));
        
        // Render temporary meter bar
        guiGraphics.setColor(this.temporaryColor.getRed() / 255.0F, this.temporaryColor.getGreen() / 255.0F, this.temporaryColor.getBlue() / 255.0F, 1.0F);
        guiGraphics.blit(TEXTURE, this.getX() + 1, this.getY() + 1 + (100 - Mth.clamp(p + i + t, 0, 100)), 0, 10, 10, Mth.clamp(t, 0, 100 - p - i));

        // Render meter foreground
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.blit(TEXTURE, this.getX(), this.getY(), 29, 9, 12, 102);
    }

    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput p_169152_) {
    }
}
