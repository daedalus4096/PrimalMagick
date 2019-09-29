package com.verdantartifice.primalmagic.client.gui.grimoire.widgets;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.gui.grimoire.GrimoireScreen;
import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.data.SyncProgressPacket;
import com.verdantartifice.primalmagic.common.research.ResearchStage;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ProgressButton extends Button {
    protected ResearchStage stage;
    protected GrimoireScreen screen;
    
    public ProgressButton(ResearchStage stage, int widthIn, int heightIn, String text, GrimoireScreen screen) {
        super(widthIn, heightIn, 119, 18, text, new Handler());
        this.stage = stage;
        this.screen = screen;
    }
    
    public ResearchStage getStage() {
        return this.stage;
    }
    
    public GrimoireScreen getScreen() {
        return this.screen;
    }
    
    private static class Handler implements IPressable {
        @Override
        public void onPress(Button button) {
            if (button instanceof ProgressButton) {
                ProgressButton pb = (ProgressButton)button;
                PrimalMagic.LOGGER.info("Progressing research");
                PacketHandler.sendToServer(new SyncProgressPacket(pb.getStage().getResearchEntry().getKey(), false, true, true));
                pb.getScreen().setProgressing();
            }
        }
    }
}
