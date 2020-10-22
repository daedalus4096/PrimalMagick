package com.verdantartifice.primalmagic.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagic.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagic.client.gui.grimoire.StatisticsPage;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * GUI button to view the grimoire statistics page.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class StatisticsButton extends AbstractTopicButton {
    public StatisticsButton(int widthIn, int heightIn, ITextComponent text, GrimoireScreen screen) {
        super(widthIn, heightIn, 123, 12, text, screen, new Handler());
    }
    
    private static class Handler implements IPressable {
        @Override
        public void onPress(Button button) {
            if (button instanceof StatisticsButton) {
                StatisticsButton gsb = (StatisticsButton)button;
                
                // Push the current grimoire topic onto the history stack
                GrimoireScreen.HISTORY.add(gsb.getScreen().getContainer().getTopic());
                
                // Set the new grimoire topic and open a new screen for it
                gsb.getScreen().getContainer().setTopic(StatisticsPage.TOPIC);
                gsb.getScreen().getMinecraft().displayGuiScreen(new GrimoireScreen(
                    gsb.getScreen().getContainer(),
                    gsb.getScreen().getPlayerInventory(),
                    gsb.getScreen().getTitle()
                ));
            }
        }
    }
}
