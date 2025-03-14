package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncProgressPacket;
import com.verdantartifice.primalmagick.common.research.ResearchStage;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

/**
 * GUI button to tell the server to attempt to progress to the next stage of the current research entry in the grimoire.
 * 
 * @author Daedalus4096
 */
public class ProgressButton extends Button {
    protected ResearchStage stage;
    protected GrimoireScreen screen;
    
    public ProgressButton(ResearchStage stage, int x, int y, Component text, GrimoireScreen screen) {
        super(x, y, 119, 20, text, new Handler(), Button.DEFAULT_NARRATION);
        this.stage = stage;
        this.screen = screen;
    }
    
    public ResearchStage getStage() {
        return this.stage;
    }
    
    public GrimoireScreen getScreen() {
        return this.screen;
    }
    
    private static class Handler implements OnPress {
        @Override
        public void onPress(Button button) {
            if (button instanceof ProgressButton pb) {
                // Send a packet to the server and tell the screen to update more frequently until resolved
                PacketHandler.sendToServer(new SyncProgressPacket(pb.getStage().parentKey(), false, true, true, false));
                pb.getScreen().setProgressing();
            }
        }
    }
}
