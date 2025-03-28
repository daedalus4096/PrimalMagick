package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagick.common.research.topics.DisciplineResearchTopic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

/**
 * GUI button to view the grimoire page for a given research discipline.
 * 
 * @author Daedalus4096
 */
public class DisciplineButton extends AbstractTopicButton {
    protected final ResearchDiscipline discipline;

    public DisciplineButton(int widthIn, int heightIn, Component text, GrimoireScreen screen, ResearchDiscipline discipline, boolean showIcon, boolean enlarge) {
        super(widthIn, heightIn, 123, enlarge ? 18 : 12, text, screen, showIcon ? GenericIndexIcon.of(discipline.iconLocation(), true) : null, new Handler());
        Minecraft mc = Minecraft.getInstance();
        this.discipline = discipline;
        int unreadCount = this.discipline.getUnreadEntryCount(mc.player);
        if (unreadCount == 1) {
            this.setTooltip(Tooltip.create(Component.translatable("tooltip.primalmagick.unread_count.single")));
        } else if (unreadCount > 0) {
            this.setTooltip(Tooltip.create(Component.translatable("tooltip.primalmagick.unread_count.multiple", unreadCount)));
        }
    }
    
    public ResearchDiscipline getDiscipline() {
        return this.discipline;
    }

    @Override
    protected boolean isHighlighted() {
        Minecraft mc = Minecraft.getInstance();
        return this.discipline.isHighlighted(mc.player);
    }

    @Override
    protected boolean isUnread() {
        Minecraft mc = Minecraft.getInstance();
        return this.discipline.isUnread(mc.player);
    }

    private static class Handler implements OnPress {
        @Override
        public void onPress(Button button) {
            if (button instanceof DisciplineButton gdb) {
                // Set the new grimoire topic and open a new screen for it
                gdb.getScreen().gotoTopic(new DisciplineResearchTopic(gdb.getDiscipline().key(), 0));
            }
        }
    }
}
