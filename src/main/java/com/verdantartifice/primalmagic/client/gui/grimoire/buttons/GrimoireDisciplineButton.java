package com.verdantartifice.primalmagic.client.gui.grimoire.buttons;

import com.verdantartifice.primalmagic.client.gui.grimoire.GrimoireScreen;
import com.verdantartifice.primalmagic.common.research.ResearchDiscipline;

import net.minecraft.client.gui.widget.button.Button;

public class GrimoireDisciplineButton extends GrimoireTopicButton {
    protected ResearchDiscipline discipline;

    public GrimoireDisciplineButton(int widthIn, int heightIn, String text, GrimoireScreen screen, ResearchDiscipline discipline) {
        super(widthIn, heightIn, 135, 18, text, screen, new Handler());
        this.discipline = discipline;
    }
    
    public ResearchDiscipline getDiscipline() {
        return this.discipline;
    }
    
    private static class Handler implements IPressable {
        @Override
        public void onPress(Button button) {
            if (button instanceof GrimoireDisciplineButton) {
                GrimoireDisciplineButton gdb = (GrimoireDisciplineButton)button;
                GrimoireScreen.HISTORY.add(gdb.getScreen().getContainer().getTopic());
                gdb.getScreen().getContainer().setTopic(gdb.getDiscipline());
                gdb.getScreen().getMinecraft().displayGuiScreen(new GrimoireScreen(
                    gdb.getScreen().getContainer(),
                    gdb.getScreen().getPlayerInventory(),
                    gdb.getScreen().getTitle()
                ));
            }
        }
    }
}
