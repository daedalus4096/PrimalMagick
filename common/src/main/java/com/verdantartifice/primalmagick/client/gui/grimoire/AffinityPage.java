package com.verdantartifice.primalmagick.client.gui.grimoire;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.common.sources.Source;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AffinityPage extends AbstractPage {
    protected Source source;
    protected List<ItemStack> contents = new ArrayList<>();
    protected boolean firstPage;

    public AffinityPage(@NotNull Source source) {
        this(source, false);
    }

    public AffinityPage(@NotNull Source source, boolean firstPage) {
        this.source = source;
        this.firstPage = firstPage;
    }

    @NotNull
    public List<ItemStack> getElements() {
        return Collections.unmodifiableList(this.contents);
    }

    public boolean addElement(ItemStack element) {
        return this.contents.add(element);
    }

    public boolean isFirstPage() {
        return this.firstPage;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int side, int x, int y, int mouseX, int mouseY) {
        // Draw title page if applicable
        if (this.isFirstPage() && side == 0) {
            this.renderTitle(guiGraphics, side, x, y, mouseX, mouseY, null);
            y += 53;
        } else {
            y += 25;
        }

        // If the page has no contents, render a label saying so
        if (this.isFirstPage() && this.getElements().isEmpty()) {
            Minecraft mc = Minecraft.getInstance();
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

    }
}
