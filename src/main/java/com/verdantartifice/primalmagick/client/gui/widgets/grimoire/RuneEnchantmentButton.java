package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.common.research.topics.EnchantmentResearchTopic;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * GUI button to view the grimoire page for a given rune enchantment.
 * 
 * @author Daedalus4096
 */
public class RuneEnchantmentButton extends AbstractTopicButton {
    protected Enchantment enchant;
    
    public RuneEnchantmentButton(int widthIn, int heightIn, Component text, GrimoireScreen screen, Enchantment enchant) {
        super(widthIn, heightIn, 123, 12, text, screen, new Handler());
        this.enchant = enchant;
    }
    
    public Enchantment getEnchantment() {
        return this.enchant;
    }
    
    private static class Handler implements OnPress {
        @Override
        public void onPress(Button button) {
            if (button instanceof RuneEnchantmentButton) {
                RuneEnchantmentButton greb = (RuneEnchantmentButton)button;
                
                // Push the current grimoire topic onto the history stack
                GrimoireScreen.HISTORY.add(greb.getScreen().getMenu().getTopic());
                
                // Set the new grimoire topic and open a new screen for it
                greb.getScreen().getMenu().setTopic(new EnchantmentResearchTopic(greb.getEnchantment()));
                greb.getScreen().getMinecraft().setScreen(new GrimoireScreen(
                    greb.getScreen().getMenu(),
                    greb.getScreen().getPlayerInventory(),
                    greb.getScreen().getTitle()
                ));
            }
        }
    }
}
