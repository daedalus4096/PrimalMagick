package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncResearchFlagsPacket;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.topics.AbstractResearchTopic;
import com.verdantartifice.primalmagick.common.research.topics.EntryResearchTopic;
import com.verdantartifice.primalmagick.common.research.topics.TopicLink;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;

/**
 * GUI button to navigate the Grimoire to the given topic link target.
 *
 * @author Daedalus4096
 */
public class TopicLinkButton extends Button {
    public static final int WIDTH = 119;
    public static final int HEIGHT = 20;

    protected final AbstractResearchTopic<?> target;
    protected final GrimoireScreen screen;

    public TopicLinkButton(TopicLink link, int x, int y, GrimoireScreen screen) {
        super(x, y, WIDTH, HEIGHT, link.getDisplayText(), new Handler(), Button.DEFAULT_NARRATION);
        this.target = link.target();
        this.screen = screen;
    }

    public AbstractResearchTopic<?> getTarget() {
        return this.target;
    }

    public GrimoireScreen getScreen() {
        return this.screen;
    }

    private static class Handler implements OnPress {
        @Override
        public void onPress(Button button) {
            if (button instanceof TopicLinkButton tlb) {
                // If the target topic is a research entry, update its flags appropriately
                if (tlb.getTarget() instanceof EntryResearchTopic ert) {
                    Minecraft mc = Minecraft.getInstance();
                    Services.CAPABILITIES.knowledge(mc.player).ifPresent(knowledge -> {
                        knowledge.removeResearchFlag(ert.getEntry(), IPlayerKnowledge.ResearchFlag.NEW);
                        knowledge.removeResearchFlag(ert.getEntry(), IPlayerKnowledge.ResearchFlag.UPDATED);
                        knowledge.removeResearchFlag(ert.getEntry(), IPlayerKnowledge.ResearchFlag.HIGHLIGHT);
                        knowledge.addResearchFlag(ert.getEntry(), IPlayerKnowledge.ResearchFlag.READ);
                        PacketHandler.sendToServer(new SyncResearchFlagsPacket(mc.player, ert.getEntry()));
                    });
                }

                // Go to the target topic in the Grimoire
                tlb.getScreen().gotoTopic(tlb.getTarget());
            }
        }
    }
}
