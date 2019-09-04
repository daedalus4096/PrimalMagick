package com.verdantartifice.primalmagic.client.gui.grimoire.buttons;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.gui.grimoire.GrimoireScreen;
import com.verdantartifice.primalmagic.common.research.ResearchEntry;

import net.minecraft.client.gui.widget.button.Button;

public class GrimoireEntryButton extends Button {
    protected GrimoireScreen screen;
    protected ResearchEntry entry;

    public GrimoireEntryButton(int widthIn, int heightIn, String text, GrimoireScreen screen, ResearchEntry entry) {
        super(widthIn, heightIn, 135, 18, text, new Handler());
        this.screen = screen;
        this.entry = entry;
    }
    
    public GrimoireScreen getScreen() {
        return this.screen;
    }
    
    public ResearchEntry getEntry() {
        return this.entry;
    }

    private static class Handler implements IPressable {
        @Override
        public void onPress(Button button) {
            if (button instanceof GrimoireEntryButton) {
                GrimoireEntryButton geb = (GrimoireEntryButton)button;
                PrimalMagic.LOGGER.info("Pressed button for {}", geb.getMessage());
                GrimoireScreen.HISTORY.add(geb.getScreen().getContainer().getTopic());
                geb.getScreen().getContainer().setTopic(geb.getEntry());
                geb.getScreen().getMinecraft().displayGuiScreen(new GrimoireScreen(
                    geb.getScreen().getContainer(),
                    geb.getScreen().getPlayerInventory(),
                    geb.getScreen().getTitle()
                ));
            }
        }
    }

}
