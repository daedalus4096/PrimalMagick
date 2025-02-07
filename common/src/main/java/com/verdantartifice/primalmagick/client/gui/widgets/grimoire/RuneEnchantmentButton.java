package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.common.research.ResearchDisciplines;
import com.verdantartifice.primalmagick.common.research.topics.EnchantmentResearchTopic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * GUI button to view the grimoire page for a given rune enchantment.
 * 
 * @author Daedalus4096
 */
public class RuneEnchantmentButton extends AbstractTopicButton {
    protected Holder<Enchantment> enchant;
    
    @SuppressWarnings("resource")
    public RuneEnchantmentButton(int widthIn, int heightIn, Component text, GrimoireScreen screen, Holder<Enchantment> enchant) {
        super(widthIn, heightIn, 123, 12, text, screen, 
                GenericIndexIcon.of(ResearchDisciplines.getDiscipline(Minecraft.getInstance().level.registryAccess(), ResearchDisciplines.RUNEWORKING).iconLocation(), false), new Handler());
        this.enchant = enchant;
    }
    
    public Holder<Enchantment> getEnchantment() {
        return this.enchant;
    }
    
    private static class Handler implements OnPress {
        @Override
        public void onPress(Button button) {
            if (button instanceof RuneEnchantmentButton greb) {
                // Set the new grimoire topic and open a new screen for it
                greb.getScreen().gotoTopic(new EnchantmentResearchTopic(greb.getEnchantment(), 0));
            }
        }
    }
}
