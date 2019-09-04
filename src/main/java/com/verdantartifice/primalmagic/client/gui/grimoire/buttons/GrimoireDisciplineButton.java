package com.verdantartifice.primalmagic.client.gui.grimoire.buttons;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.gui.grimoire.GrimoireScreen;
import com.verdantartifice.primalmagic.common.research.ResearchDiscipline;

import net.minecraft.client.gui.widget.button.Button;

public class GrimoireDisciplineButton extends Button {
    protected GrimoireScreen screen;
    protected ResearchDiscipline discipline;

    public GrimoireDisciplineButton(int widthIn, int heightIn, String text, GrimoireScreen screen, ResearchDiscipline discipline) {
        super(widthIn, heightIn, 135, 18, text, new Handler());
        this.screen = screen;
        this.discipline = discipline;
    }
    
    public GrimoireScreen getScreen() {
        return this.screen;
    }
    
    public ResearchDiscipline getDiscipline() {
        return this.discipline;
    }
    
    private static class Handler implements IPressable {
        @Override
        public void onPress(Button button) {
            if (button instanceof GrimoireDisciplineButton) {
                GrimoireDisciplineButton gdb = (GrimoireDisciplineButton)button;
                PrimalMagic.LOGGER.info("Pressed button for {}", gdb.getMessage());
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
