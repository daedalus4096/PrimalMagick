package com.verdantartifice.primalmagick.client.gui.grimoire;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.AffinityButton;
import com.verdantartifice.primalmagick.common.research.topics.OtherResearchTopic;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.Sources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class AffinityIndexPage extends AbstractPage {
    public static final OtherResearchTopic TOPIC = new OtherResearchTopic("affinities", 0);

    protected boolean firstPage;

    public AffinityIndexPage() {
        this(false);
    }

    public AffinityIndexPage(boolean first) {
        this.firstPage = first;
    }

    public boolean isFirstPage() {
        return this.firstPage;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int side, int x, int y, int mouseX, int mouseY) {
        // Just render the title; buttons have already been added
        if (this.isFirstPage() && side == 0) {
            this.renderTitle(guiGraphics, side, x, y, mouseX, mouseY, null);
        }
    }

    @Override
    protected Component getTitleText() {
        return Component.translatable("grimoire.primalmagick.affinity_index_header");
    }

    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        // Add a button to the screen for each discovered source
        Minecraft mc = Minecraft.getInstance();
        for (Source source : Sources.getAllSorted()) {
            if (source.isDiscovered(mc.player)) {
                Component text = Component.translatable(source.getNameTranslationKey());
                screen.addWidgetToScreen(new AffinityButton(x + 12 + (side * 140), y, text, screen, source));
                y += 12;
            }
        }
    }
}
