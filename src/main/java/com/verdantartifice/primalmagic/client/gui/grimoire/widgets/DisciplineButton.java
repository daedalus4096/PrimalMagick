package com.verdantartifice.primalmagic.client.gui.grimoire.widgets;

import com.verdantartifice.primalmagic.client.gui.grimoire.GrimoireScreen;
import com.verdantartifice.primalmagic.common.research.ResearchDiscipline;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DisciplineButton extends AbstractTopicButton {
    protected ResearchDiscipline discipline;

    public DisciplineButton(int widthIn, int heightIn, String text, GrimoireScreen screen, ResearchDiscipline discipline) {
        super(widthIn, heightIn, 123, 12, text, screen, new Handler());
        this.discipline = discipline;
    }
    
    public ResearchDiscipline getDiscipline() {
        return this.discipline;
    }
    
    private static class Handler implements IPressable {
        @Override
        public void onPress(Button button) {
            if (button instanceof DisciplineButton) {
                DisciplineButton gdb = (DisciplineButton)button;
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
