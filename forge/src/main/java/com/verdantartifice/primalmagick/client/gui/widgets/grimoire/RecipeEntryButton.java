package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.common.research.topics.OtherResearchTopic;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

/**
 * GUI button to view the grimoire page for a given recipe.
 * 
 * @author Daedalus4096
 */
public class RecipeEntryButton extends AbstractTopicButton {
    protected String recipeName;
    
    public RecipeEntryButton(int x, int y, Component text, GrimoireScreen screen, String recipeName, ItemStack outputStack) {
        super(x, y, 123, 12, text, screen, ItemIndexIcon.of(outputStack, false), new Handler());
        this.recipeName = recipeName;
    }
    
    public String getRecipeName() {
        return this.recipeName;
    }
    
    private static class Handler implements OnPress {
        @Override
        public void onPress(Button button) {
            if (button instanceof RecipeEntryButton greb) {
                // Set the new grimoire topic and open a new screen for it
                greb.getScreen().gotoTopic(new OtherResearchTopic(greb.getRecipeName(), 0));
            }
        }
    }
}
