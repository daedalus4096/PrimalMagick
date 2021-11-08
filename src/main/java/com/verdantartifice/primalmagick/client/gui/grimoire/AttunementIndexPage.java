package com.verdantartifice.primalmagick.client.gui.grimoire;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.AttunementButton;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

/**
 * Grimoire page showing the list of discovered attunements.
 * 
 * @author Daedalus4096
 */
public class AttunementIndexPage extends AbstractPage {
    public static final String TOPIC = "attunements";

    protected boolean firstPage;

    public AttunementIndexPage() {
        this(false);
    }
    
    public AttunementIndexPage(boolean first) {
        this.firstPage = first;
    }
    
    @Override
    public void render(PoseStack matrixStack, int side, int x, int y, int mouseX, int mouseY) {
        // Just render the title; buttons have already been added
        if (this.isFirstPage() && side == 0) {
            this.renderTitle(matrixStack, side, x, y, mouseX, mouseY, null);
        }
    }

    public boolean isFirstPage() {
        return this.firstPage;
    }
    
    @Override
    protected String getTitleTranslationKey() {
        return "primalmagic.grimoire.attunement_header";
    }

    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        // Add a button to the screen for each discovered source
        Minecraft mc = Minecraft.getInstance();
        for (Source source : Source.SORTED_SOURCES) {
            if (source.isDiscovered(mc.player)) {
                Component text = new TranslatableComponent(source.getNameTranslationKey());
                screen.addWidgetToScreen(new AttunementButton(x + 12 + (side * 140), y, text, screen, source));
                y += 12;
            }
        }
    }

}
