package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Either;
import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.client.util.GuiUtils;
import com.verdantartifice.primalmagick.common.affinities.AffinityIndexEntry;
import com.verdantartifice.primalmagick.common.affinities.AffinityTooltipComponent;
import com.verdantartifice.primalmagick.common.sources.Source;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AffinityRecordWidget extends AbstractTopicButton {
    public static final int WIDGET_HEIGHT = 17;

    protected final AffinityIndexEntry entry;
    protected final Source source;
    protected final Component amountText;
    protected final List<Either<FormattedText, TooltipComponent>> elements = new ArrayList<>();

    public AffinityRecordWidget(int x, int y, @NotNull AffinityIndexEntry entry, Source source, GrimoireScreen screen) {
        super(x, y, 123, WIDGET_HEIGHT, entry.stack().getHoverName(), screen, ItemIndexIcon.of(entry.stack().getItem(), true), button -> {});
        this.entry = entry;
        this.source = source;
        this.amountText = Component.literal(Integer.toString(this.entry.affinities().join().getAmount(this.source)));
        this.elements.add(Either.left(Component.translatable("tooltip.primalmagick.affinities.label")));
        this.elements.add(Either.right(new AffinityTooltipComponent(this.entry.affinities().join())));
    }

    @Override
    public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.renderWidget(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        // Draw the relevant affinity rating where the stack count would normally be
        Minecraft mc = Minecraft.getInstance();
        int width = mc.font.width(this.amountText);
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate(this.getX() + 16 - width / 2, this.getY() + 12, 200.0F);
        pGuiGraphics.pose().scale(0.5F, 0.5F, 0.5F);
        pGuiGraphics.drawString(mc.font, this.amountText, 0, 0, Color.WHITE.getRGB());
        pGuiGraphics.pose().popPose();

        // Draw the tooltip if applicable
        if (this.isHoveredOrFocused()) {
            GuiUtils.renderComponentTooltipFromElements(pGuiGraphics, this.elements, pMouseX, pMouseY);
        }
    }

    @Override
    public void playDownSound(SoundManager handler) {
        // Don't play a sound on click
    }
}
