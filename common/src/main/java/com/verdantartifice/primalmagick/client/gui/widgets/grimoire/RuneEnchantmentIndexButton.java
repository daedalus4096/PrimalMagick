package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.client.gui.grimoire.RuneEnchantmentIndexPage;
import com.verdantartifice.primalmagick.common.research.ResearchDisciplines;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

/**
 * GUI button to view the grimoire rune enchantment index page.
 * 
 * @author Daedalus4096
 */
public class RuneEnchantmentIndexButton extends AbstractTopicButton {
    @SuppressWarnings("resource")
    public RuneEnchantmentIndexButton(int widthIn, int heightIn, Component text, GrimoireScreen screen) {
        super(widthIn, heightIn, 123, 17, text, screen,
                GenericIndexIcon.of(ResearchDisciplines.getDiscipline(Minecraft.getInstance().level.registryAccess(), ResearchDisciplines.RUNEWORKING).iconLocation(), true), new Handler());
    }
    
    private static class Handler implements OnPress {
        @Override
        public void onPress(Button button) {
            if (button instanceof RuneEnchantmentIndexButton greb) {
                // Set the new grimoire topic and open a new screen for it
                greb.getScreen().gotoTopic(RuneEnchantmentIndexPage.TOPIC);
            }
        }
    }
}
