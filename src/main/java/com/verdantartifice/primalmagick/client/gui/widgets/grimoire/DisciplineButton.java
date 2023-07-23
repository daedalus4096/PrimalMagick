package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagick.common.research.topics.DisciplineResearchTopic;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

/**
 * GUI button to view the grimoire page for a given research discipline.
 * 
 * @author Daedalus4096
 */
public class DisciplineButton extends AbstractTopicButton {
    protected final ResearchDiscipline discipline;

    public DisciplineButton(int widthIn, int heightIn, Component text, GrimoireScreen screen, ResearchDiscipline discipline, boolean showIcon, boolean enlarge) {
        super(widthIn, heightIn, 123, enlarge ? 18 : 12, text, screen, showIcon ? GenericIndexIcon.of(discipline.getIconLocation(), true) : null, new Handler());
        this.discipline = discipline;
    }
    
    public ResearchDiscipline getDiscipline() {
        return this.discipline;
    }
    
    private static class Handler implements OnPress {
        @Override
        public void onPress(Button button) {
            if (button instanceof DisciplineButton gdb) {
                // Set the new grimoire topic and open a new screen for it
                gdb.getScreen().gotoTopic(new DisciplineResearchTopic(gdb.getDiscipline(), 0));
            }
        }
    }
}
