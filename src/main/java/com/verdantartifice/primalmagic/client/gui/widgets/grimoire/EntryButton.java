package com.verdantartifice.primalmagic.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagic.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.data.SyncProgressPacket;
import com.verdantartifice.primalmagic.common.network.packets.data.SyncResearchFlagsPacket;
import com.verdantartifice.primalmagic.common.research.ResearchEntry;

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

    public EntryButton(int x, int y, Component text, GrimoireScreen screen, ResearchEntry entry) {
        super(x, y, 123, 12, text, screen, new Handler());
        this.entry = entry;
    }
    
    public ResearchEntry getEntry() {
        return this.entry;
    }
    
    private static class Handler implements OnPress {
        @Override
        public void onPress(Button button) {
            if (button instanceof EntryButton) {
                Minecraft mc = Minecraft.getInstance();
                EntryButton geb = (EntryButton)button;
                
                // Push the current grimoire topic onto the history stack
                GrimoireScreen.HISTORY.add(geb.getScreen().getMenu().getTopic());
                geb.getScreen().getMenu().setTopic(geb.getEntry());
                if (geb.getEntry().getKey().isKnownBy(mc.player)) {
                    // If the research entry has been flagged as new or updated, clear those flags
                    IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(mc.player);
                    if (knowledge != null) {
                        knowledge.removeResearchFlag(geb.getEntry().getKey(), IPlayerKnowledge.ResearchFlag.NEW);
                        knowledge.removeResearchFlag(geb.getEntry().getKey(), IPlayerKnowledge.ResearchFlag.UPDATED);
                        PacketHandler.sendToServer(new SyncResearchFlagsPacket(mc.player, geb.getEntry().getKey()));
                    }
                } else {
                    PacketHandler.sendToServer(new SyncProgressPacket(geb.getEntry().getKey(), true, false, true));  // Advance research from unknown to stage 1
                }
                
                // Set the new grimoire topic and open a new screen for it
                geb.getScreen().getMinecraft().setScreen(new GrimoireScreen(
                    geb.getScreen().getMenu(),
                    geb.getScreen().getPlayerInventory(),
                    geb.getScreen().getTitle()
                ));
            }
        }
    }

}
