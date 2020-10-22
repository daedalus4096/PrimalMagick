package com.verdantartifice.primalmagic.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagic.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagic.common.research.ResearchDiscipline;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * GUI button to view the grimoire page for a given research discipline.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class DisciplineButton extends AbstractTopicButton {
    protected ResearchDiscipline discipline;

    public DisciplineButton(int widthIn, int heightIn, ITextComponent text, GrimoireScreen screen, ResearchDiscipline discipline) {
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
                
                // Push the current grimoire topic onto the history stack
                GrimoireScreen.HISTORY.add(gdb.getScreen().getContainer().getTopic());
                
                // Set the new grimoire topic and open a new screen for it
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
