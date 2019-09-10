package com.verdantartifice.primalmagic.client.gui.grimoire.widgets;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.data.SyncProgressPacket;
import com.verdantartifice.primalmagic.common.research.ResearchStage;

import net.minecraft.client.gui.widget.button.Button;

public class ProgressButton extends Button {
    protected ResearchStage stage;
    
    public ProgressButton(ResearchStage stage, int widthIn, int heightIn, String text) {
        super(widthIn, heightIn, 135, 18, text, new Handler());
        this.stage = stage;
    }
    
    public ResearchStage getStage() {
        return this.stage;
    }
    
    private static class Handler implements IPressable {
        @Override
        public void onPress(Button button) {
            if (button instanceof ProgressButton) {
                ProgressButton pb = (ProgressButton)button;
                PrimalMagic.LOGGER.info("Progressing research");
                PacketHandler.sendToServer(new SyncProgressPacket(pb.getStage().getResearchEntry().getKey()));
            }
        }
    }
}
