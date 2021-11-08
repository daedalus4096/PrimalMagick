package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.client.gui.grimoire.RecipeIndexPage;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

/**
 * GUI button to view the grimoire recipe index page.
 * 
 * @author Daedalus4096
 */
public class RecipeIndexButton extends AbstractTopicButton {
    public RecipeIndexButton(int widthIn, int heightIn, Component text, GrimoireScreen screen) {
        super(widthIn, heightIn, 123, 12, text, screen, new Handler());
    }
    
    private static class Handler implements OnPress {
        @Override
        public void onPress(Button button) {
            if (button instanceof RecipeIndexButton grib) {
                // Push the current grimoire topic onto the history stack
                GrimoireScreen.HISTORY.add(grib.getScreen().getMenu().getTopic());
                
                // Set the new grimoire topic and open a new screen for it
                grib.getScreen().getMenu().setTopic(RecipeIndexPage.TOPIC);
                grib.getScreen().getMinecraft().setScreen(new GrimoireScreen(
                    grib.getScreen().getMenu(),
                    grib.getScreen().getPlayerInventory(),
                    grib.getScreen().getTitle()
                ));
            }
        }
    }
}
