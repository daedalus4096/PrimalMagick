package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.common.attunements.AttunementManager;
import com.verdantartifice.primalmagick.common.attunements.AttunementType;
import com.verdantartifice.primalmagick.common.sources.Source;
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
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.awt.Color;

public class AttunementMeterWidget extends AbstractWidget {
    private static final Identifier FOREGROUND = ResourceUtils.loc("grimoire/attunement_meter_foreground");
    private static final Identifier BAR = ResourceUtils.loc("grimoire/attunement_meter_bar");

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
    public void extractWidgetRenderState(@NotNull GuiGraphicsExtractor pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        // Render attunement meter
        Minecraft mc = Minecraft.getInstance();

        int p = AttunementManager.getAttunement(mc.player, this.source, AttunementType.PERMANENT);
        int i = AttunementManager.getAttunement(mc.player, this.source, AttunementType.INDUCED);
        int t = AttunementManager.getAttunement(mc.player, this.source, AttunementType.TEMPORARY);

        // Render permanent meter bar
        pGuiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, BAR, this.getX() + 1, this.getY() + 1 + (100 - Mth.clamp(p, 0, 100)), 10, Mth.clamp(p, 0, 100), this.permanentColor.getRGB());
        
        // Render induced meter bar
        pGuiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, BAR, this.getX() + 1, this.getY() + 1 + (100 - Mth.clamp(p + i, 0, 100)), 10, Mth.clamp(i, 0, 100 - p), this.inducedColor.getRGB());
        
        // Render temporary meter bar
        pGuiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, BAR, this.getX() + 1, this.getY() + 1 + (100 - Mth.clamp(p + i + t, 0, 100)), 10, Mth.clamp(t, 0, 100 - p - i), this.temporaryColor.getRGB());

        // Render meter foreground
        pGuiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, FOREGROUND, this.getX(), this.getY(), 12, 102);
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
