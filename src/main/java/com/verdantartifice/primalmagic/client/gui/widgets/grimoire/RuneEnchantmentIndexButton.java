package com.verdantartifice.primalmagic.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagic.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagic.client.gui.grimoire.RuneEnchantmentIndexPage;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * GUI button to view the grimoire rune enchantment index page.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class RuneEnchantmentIndexButton extends AbstractTopicButton {
    public RuneEnchantmentIndexButton(int widthIn, int heightIn, Component text, GrimoireScreen screen) {
        super(widthIn, heightIn, 123, 12, text, screen, new Handler());
    }
    
    private static class Handler implements OnPress {
        @Override
        public void onPress(Button button) {
            if (button instanceof RuneEnchantmentIndexButton) {
                RuneEnchantmentIndexButton greb = (RuneEnchantmentIndexButton)button;
                
                // Push the current grimoire topic onto the history stack
                GrimoireScreen.HISTORY.add(greb.getScreen().getMenu().getTopic());
                
                // Set the new grimoire topic and open a new screen for it
                greb.getScreen().getMenu().setTopic(RuneEnchantmentIndexPage.TOPIC);
                greb.getScreen().getMinecraft().setScreen(new GrimoireScreen(
                    greb.getScreen().getMenu(),
                    greb.getScreen().getPlayerInventory(),
                    greb.getScreen().getTitle()
                ));
            }
        }
    }
}
