package com.verdantartifice.primalmagic.client.gui.grimoire.widgets;

import com.verdantartifice.primalmagic.client.gui.grimoire.GrimoireScreen;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.data.SyncProgressPacket;
import com.verdantartifice.primalmagic.common.network.packets.data.SyncResearchFlagsPacket;
import com.verdantartifice.primalmagic.common.research.ResearchEntry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;

public class EntryButton extends AbstractTopicButton {
    protected ResearchEntry entry;

    public EntryButton(int widthIn, int heightIn, String text, GrimoireScreen screen, ResearchEntry entry) {
        super(widthIn, heightIn, 135, 12, text, screen, new Handler());
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
                if (geb.getEntry().getKey().isKnownBy(Minecraft.getInstance().player)) {
                    IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(Minecraft.getInstance().player);
                    if (knowledge != null) {
                        knowledge.removeResearchFlag(geb.getEntry().getKey(), IPlayerKnowledge.ResearchFlag.NEW);
                        knowledge.removeResearchFlag(geb.getEntry().getKey(), IPlayerKnowledge.ResearchFlag.UPDATED);
                        PacketHandler.sendToServer(new SyncResearchFlagsPacket(Minecraft.getInstance().player, geb.getEntry().getKey()));
                    }
                } else {
                    PacketHandler.sendToServer(new SyncProgressPacket(geb.getEntry().getKey(), true, false, true));  // Advance research from unknown to stage 1
                }
                geb.getScreen().getMinecraft().displayGuiScreen(new GrimoireScreen(
                    geb.getScreen().getContainer(),
                    geb.getScreen().getPlayerInventory(),
                    geb.getScreen().getTitle()
                ));
            }
        }
    }

}
