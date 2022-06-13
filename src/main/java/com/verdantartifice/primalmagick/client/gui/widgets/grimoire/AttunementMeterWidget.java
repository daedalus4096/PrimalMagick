package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.util.GuiUtils;
import com.verdantartifice.primalmagick.common.attunements.AttunementManager;
import com.verdantartifice.primalmagick.common.attunements.AttunementType;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class AttunementMeterWidget extends AbstractWidget {
    private static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagick.MODID, "textures/gui/attunement_meter.png");

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
    }

    @Override
    public void renderButton(PoseStack matrixStack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        // Render attunement meter
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        Minecraft mc = Minecraft.getInstance();
        RenderSystem.setShaderTexture(0, TEXTURE);
        
        int p = AttunementManager.getAttunement(mc.player, this.source, AttunementType.PERMANENT);
        int i = AttunementManager.getAttunement(mc.player, this.source, AttunementType.INDUCED);
        int t = AttunementManager.getAttunement(mc.player, this.source, AttunementType.TEMPORARY);

        // Render permanent meter bar
        RenderSystem.setShaderColor(this.permanentColor.getRed() / 255.0F, this.permanentColor.getGreen() / 255.0F, this.permanentColor.getBlue() / 255.0F, 1.0F);
        this.blit(matrixStack, this.x + 1, this.y + 1 + (100 - Mth.clamp(p, 0, 100)), 0, 10, 10, Mth.clamp(p, 0, 100));
        
        // Render induced meter bar
        RenderSystem.setShaderColor(this.inducedColor.getRed() / 255.0F, this.inducedColor.getGreen() / 255.0F, this.inducedColor.getBlue() / 255.0F, 1.0F);
        this.blit(matrixStack, this.x + 1, this.y + 1 + (100 - Mth.clamp(p + i, 0, 100)), 0, 10, 10, Mth.clamp(i, 0, 100 - p));
        
        // Render temporary meter bar
        RenderSystem.setShaderColor(this.temporaryColor.getRed() / 255.0F, this.temporaryColor.getGreen() / 255.0F, this.temporaryColor.getBlue() / 255.0F, 1.0F);
        this.blit(matrixStack, this.x + 1, this.y + 1 + (100 - Mth.clamp(p + i + t, 0, 100)), 0, 10, 10, Mth.clamp(t, 0, 100 - p - i));

        // Render meter foreground
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        this.blit(matrixStack, this.x, this.y, 29, 9, 12, 102);
        
        if (this.isHoveredOrFocused()) {
            // Render tooltip
            List<Component> tooltip = new ArrayList<>();
            tooltip.add(Component.translatable("primalmagick.grimoire.attunement_meter.tooltip.header", this.source.getNameText()));
            tooltip.add(Component.translatable("primalmagick.grimoire.attunement_meter.tooltip.permanent", p));
            if (i > 0) {
                tooltip.add(Component.translatable("primalmagick.grimoire.attunement_meter.tooltip.induced", i));
            }
            tooltip.add(Component.translatable("primalmagick.grimoire.attunement_meter.tooltip.temporary", t));
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
