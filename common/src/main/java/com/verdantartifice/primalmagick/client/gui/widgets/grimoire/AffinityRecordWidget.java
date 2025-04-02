package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.common.affinities.AffinityIndexEntry;
import com.verdantartifice.primalmagick.common.sources.Source;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class AffinityRecordWidget extends AbstractTopicButton {
    public static final int WIDGET_HEIGHT = 17;

    protected final AffinityIndexEntry entry;
    protected final Source source;
    protected final Component amountText;

    public AffinityRecordWidget(int x, int y, @NotNull AffinityIndexEntry entry, Source source, GrimoireScreen screen) {
        super(x, y, 123, WIDGET_HEIGHT, entry.stack().getHoverName(), screen, ItemIndexIcon.of(entry.stack().getItem(), true), button -> {});
        this.entry = entry;
        this.source = source;
        this.amountText = Component.literal(Integer.toString(this.entry.affinities().join().getAmount(this.source)));
        // TODO Set tooltip data to be full affinity source list
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        super.renderWidget(guiGraphics, p_renderButton_1_, p_renderButton_2_, p_renderButton_3_);

        // Draw the relevant affinity rating where the stack count would normally be
        Minecraft mc = Minecraft.getInstance();
        int width = mc.font.width(this.amountText);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(this.getX() + 16 - width / 2, this.getY() + 12, 200.0F);
        guiGraphics.pose().scale(0.5F, 0.5F, 0.5F);
        guiGraphics.drawString(mc.font, this.amountText, 0, 0, Color.WHITE.getRGB());
        guiGraphics.pose().popPose();
    }

    @Override
    public void playDownSound(SoundManager handler) {
        // Don't play a sound on click
    }
}
