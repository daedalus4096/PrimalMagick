package com.verdantartifice.primalmagic.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagic.client.gui.GrimoireScreen;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * GUI button to view the grimoire page for a given recipe.
 * 
 * @author Daedalus4096
 */
public class RecipeEntryButton extends AbstractTopicButton {
    protected ResourceLocation recipeLoc;
    
    public RecipeEntryButton(int x, int y, Component text, GrimoireScreen screen, ResourceLocation recipeLoc) {
        super(x, y, 123, 12, text, screen, new Handler());
        this.recipeLoc = recipeLoc;
    }
    
    public ResourceLocation getRecipeLocation() {
        return this.recipeLoc;
    }
    
    private static class Handler implements OnPress {
        @Override
        public void onPress(Button button) {
            if (button instanceof RecipeEntryButton greb) {
                // Push the current grimoire topic onto the history stack
                GrimoireScreen.HISTORY.add(greb.getScreen().getMenu().getTopic());
                
                // Set the new grimoire topic and open a new screen for it
                greb.getScreen().getMenu().setTopic(greb.getRecipeLocation());
                greb.getScreen().getMinecraft().setScreen(new GrimoireScreen(
                    greb.getScreen().getMenu(),
                    greb.getScreen().getPlayerInventory(),
                    greb.getScreen().getTitle()
                ));
            }
        }
    }
}
