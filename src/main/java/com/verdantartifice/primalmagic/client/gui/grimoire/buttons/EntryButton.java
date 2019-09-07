package com.verdantartifice.primalmagic.client.gui.grimoire.buttons;

import com.verdantartifice.primalmagic.client.gui.grimoire.GrimoireScreen;
import com.verdantartifice.primalmagic.common.research.ResearchEntry;

import net.minecraft.client.gui.widget.button.Button;

public class EntryButton extends AbstractTopicButton {
    protected ResearchEntry entry;

    public EntryButton(int widthIn, int heightIn, String text, GrimoireScreen screen, ResearchEntry entry) {
        super(widthIn, heightIn, 135, 18, text, screen, new Handler());
        this.entry = entry;
    }
    
    public ResearchEntry getEntry() {
        return this.entry;
    }
    
    private static class Handler implements IPressable {
        @Override
        public void onPress(Button button) {
            if (button instanceof EntryButton) {
                EntryButton geb = (EntryButton)button;
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
