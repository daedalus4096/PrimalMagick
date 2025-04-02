package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.common.affinities.AffinityIndexEntry;
import com.verdantartifice.primalmagick.common.sources.Source;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.sounds.SoundManager;
import org.jetbrains.annotations.NotNull;

public class AffinityRecordWidget extends AbstractTopicButton {
    protected final AffinityIndexEntry entry;
    protected final Source source;

    public AffinityRecordWidget(int x, int y, @NotNull AffinityIndexEntry entry, Source source, GrimoireScreen screen) {
        super(x, y, 123, 12, entry.stack().getDisplayName(), screen, ItemIndexIcon.of(entry.stack().getItem(), false), button -> {});
        this.entry = entry;
        this.source = source;
        // TODO Set tooltip data to be full affinity source list
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        super.renderWidget(guiGraphics, p_renderButton_1_, p_renderButton_2_, p_renderButton_3_);

        // TODO Draw the relevant affinity rating where the stack count would normally be
    }

    @Override
    public void playDownSound(SoundManager handler) {
        // Don't play a sound on click
    }
}
