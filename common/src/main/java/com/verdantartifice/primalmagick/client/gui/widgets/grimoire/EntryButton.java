package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncProgressPacket;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncResearchFlagsPacket;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.research.topics.EntryResearchTopic;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

/**
 * GUI button to view the grimoire page for a given research entry.
 * 
 * @author Daedalus4096
 */
public class EntryButton extends AbstractTopicButton {
    protected ResearchEntry entry;

    public EntryButton(int x, int y, Component text, GrimoireScreen screen, ResearchEntry entry, boolean showIcon) {
        super(x, y, 123, 12, text, screen, showIcon ? IndexIconFactory.fromEntryIcon(entry.iconOpt().orElse(null), false) : null, new Handler());
        this.entry = entry;
    }
    
    public ResearchEntry getEntry() {
        return this.entry;
    }

    @Override
    protected boolean isHighlighted() {
        Minecraft mc = Minecraft.getInstance();
        return this.entry.isHighlighted(mc.player);
    }

    private static class Handler implements OnPress {
        @Override
        public void onPress(Button button) {
            if (button instanceof EntryButton geb) {
                Minecraft mc = Minecraft.getInstance();
                
                // Push the current grimoire topic onto the history stack
                geb.getScreen().pushCurrentHistoryTopic();
                geb.getScreen().setTopic(new EntryResearchTopic(geb.getEntry().key(), 0));
                if (ResearchManager.isResearchStarted(mc.player, geb.getEntry().key())) {
                    // If the research entry has been flagged as new or updated, clear those flags
                    Services.CAPABILITIES.knowledge(mc.player).ifPresent(knowledge -> {
                        knowledge.removeResearchFlag(geb.getEntry().key(), IPlayerKnowledge.ResearchFlag.NEW);
                        knowledge.removeResearchFlag(geb.getEntry().key(), IPlayerKnowledge.ResearchFlag.UPDATED);
                        knowledge.removeResearchFlag(geb.getEntry().key(), IPlayerKnowledge.ResearchFlag.HIGHLIGHT);
                        PacketHandler.sendToServer(new SyncResearchFlagsPacket(mc.player, geb.getEntry().key()));
                    });
                } else {
                    PacketHandler.sendToServer(new SyncProgressPacket(geb.getEntry().key(), true, false, true, false));  // Advance research from unknown to stage 1
                }
                
                // Set the new grimoire topic and open a new screen for it
                mc.setScreen(new GrimoireScreen());
            }
        }
    }

}
