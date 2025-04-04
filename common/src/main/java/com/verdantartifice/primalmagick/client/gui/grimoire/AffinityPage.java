package com.verdantartifice.primalmagick.client.gui.grimoire;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.AffinityRecordWidget;
import com.verdantartifice.primalmagick.common.affinities.AffinityIndexEntry;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class AffinityPage extends AbstractPage {
    protected final Source source;
    protected final CompletableFuture<Void> loadedFuture;
    protected final List<AffinityIndexEntry> contents = new ArrayList<>();
    protected final boolean firstPage;
    protected Optional<List<CompletableFuture<SourceList>>> progressFutures = Optional.empty();

    public AffinityPage(@NotNull Source source, CompletableFuture<Void> loadedFuture) {
        this(source, loadedFuture, false);
    }

    public AffinityPage(@NotNull Source source, CompletableFuture<Void> loadedFuture, boolean firstPage) {
        this.source = source;
        this.loadedFuture = loadedFuture;
        this.firstPage = firstPage;
    }

    @NotNull
    public List<AffinityIndexEntry> getElements() {
        return Collections.unmodifiableList(this.contents);
    }

    public boolean addElement(AffinityIndexEntry element) {
        return this.contents.add(element);
    }

    public boolean isFirstPage() {
        return this.firstPage;
    }

    protected boolean isLoaded() {
        return this.loadedFuture.isDone();
    }

    public void setProgressFutures(List<CompletableFuture<SourceList>> progressFutures) {
        this.progressFutures = Optional.of(progressFutures);
    }

    protected int getTotalProgress() {
        return this.progressFutures.map(List::size).orElse(0);
    }

    protected int getCurrentProgress() {
        return this.progressFutures.map(futures -> futures.stream().mapToInt(f -> f.isDone() ? 1 : 0).sum()).orElse(0);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int side, int x, int y, int mouseX, int mouseY) {
        // Draw title page if applicable
        final int startY = y;
        if (this.isFirstPage() && side == 0) {
            this.renderTitle(guiGraphics, side, x, y, mouseX, mouseY, null);
            y += 53;
        } else {
            y += 25;
        }

        Minecraft mc = Minecraft.getInstance();
        if (this.isFirstPage() && !this.isLoaded()) {
            // If the page contents aren't yet loaded, render a label saying so
            Component text = Component.translatable("grimoire.primalmagick.affinity_data.calculating");
            int width = mc.font.width(text.getString());
            int indent = 124;
            guiGraphics.drawString(mc.font, text, x - 3 + (side * 140) + (indent / 2) - (width / 2), y + 25, Color.BLACK.getRGB(), false);

            // Display calculation progress if applicable
            this.progressFutures.ifPresent($ -> {
                Component progressText = Component.literal(this.getCurrentProgress() + " / " + this.getTotalProgress());
                int progressWidth = mc.font.width(progressText.getString());
                guiGraphics.drawString(mc.font, progressText, x - 3 + (side * 140) + (indent / 2) - (progressWidth / 2), startY + 78 + mc.font.lineHeight, Color.BLACK.getRGB(), false);
            });
        } else if (this.isFirstPage() && this.getElements().isEmpty()) {
            // If the page has no contents, render a label saying so
            Component text = Component.translatable("grimoire.primalmagick.affinity_data.no_entries");
            int width = mc.font.width(text.getString());
            int indent = 124;
            guiGraphics.drawString(mc.font, text, x - 3 + (side * 140) + (indent / 2) - (width / 2), y + 25, Color.BLACK.getRGB(), false);
        }
    }

    @Override
    protected Component getTitleText() {
        return Component.translatable(this.source.getNameTranslationKey());
    }

    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        for (AffinityIndexEntry entry : this.getElements()) {
            AffinityRecordWidget widget = new AffinityRecordWidget(x + 12 + (side * 140), y, entry, this.source, screen);
            screen.addWidgetToScreen(widget);
            y += widget.getHeight();
        }
    }
}
