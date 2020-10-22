package com.verdantartifice.primalmagic.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagic.client.gui.GrimoireScreen;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * GUI button to view the grimoire page for a given rune enchantment.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class RuneEnchantmentButton extends AbstractTopicButton {
    protected Enchantment enchant;
    
    public RuneEnchantmentButton(int widthIn, int heightIn, ITextComponent text, GrimoireScreen screen, Enchantment enchant) {
        super(widthIn, heightIn, 123, 12, text, screen, new Handler());
        this.enchant = enchant;
    }
    
    public Enchantment getEnchantment() {
        return this.enchant;
    }
    
    private static class Handler implements IPressable {
        @Override
        public void onPress(Button button) {
            if (button instanceof RuneEnchantmentButton) {
                RuneEnchantmentButton greb = (RuneEnchantmentButton)button;
                
                // Push the current grimoire topic onto the history stack
                GrimoireScreen.HISTORY.add(greb.getScreen().getContainer().getTopic());
                
                // Set the new grimoire topic and open a new screen for it
                greb.getScreen().getContainer().setTopic(greb.getEnchantment());
                greb.getScreen().getMinecraft().displayGuiScreen(new GrimoireScreen(
                    greb.getScreen().getContainer(),
                    greb.getScreen().getPlayerInventory(),
                    greb.getScreen().getTitle()
                ));
            }
        }
    }
}
