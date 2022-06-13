package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.common.research.topics.MainIndexResearchTopic;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;

/**
 * GUI button to go to the main index in the grimoire.
 * 
 * @author Daedalus4096
 */
public class MainIndexButton extends Button {
    protected GrimoireScreen screen;

    public MainIndexButton(int xPos, int yPos, GrimoireScreen screen) {
        super(xPos, yPos, 10, 26, Component.empty(), new Handler());
        this.screen = screen;
    }
    
    public GrimoireScreen getScreen() {
        return this.screen;
    }
    
    @Override
    public void renderButton(PoseStack matrixStack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        // Do nothing
    }

    @Override
    public void playDownSound(SoundManager handler) {
        handler.play(SimpleSoundInstance.forUI(SoundsPM.PAGE.get(), 1.0F, 1.0F));
    }

    private static class Handler implements OnPress {
        @Override
        public void onPress(Button button) {
            if (button instanceof MainIndexButton indexButton) {
                // Push the current grimoire topic onto the history stack if we're not already on the main menu
                if (!indexButton.getScreen().getMenu().getTopic().equals(MainIndexResearchTopic.INSTANCE)) {
                    indexButton.getScreen().pushCurrentHistoryTopic();
                }
                
                // Set the new grimoire topic and open a new screen for it
                indexButton.getScreen().getMenu().setTopic(MainIndexResearchTopic.INSTANCE);
                indexButton.getScreen().getMinecraft().setScreen(new GrimoireScreen(
                    indexButton.getScreen().getMenu(),
                    indexButton.getScreen().getPlayerInventory(),
                    indexButton.getScreen().getTitle()
                ));
            }
        }
    }
}
