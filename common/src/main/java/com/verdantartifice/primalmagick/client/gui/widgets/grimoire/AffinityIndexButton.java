package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.mojang.logging.LogUtils;
import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Blocks;

public class AffinityIndexButton extends AbstractTopicButton {
    public AffinityIndexButton(int widthIn, int heightIn, Component text, GrimoireScreen screen) {
        super(widthIn, heightIn, 123, 17, text, screen, ItemIndexIcon.of(Blocks.BOOKSHELF, true), new AffinityIndexButton.Handler());
    }

    private static class Handler implements OnPress {
        @Override
        public void onPress(Button button) {
            if (button instanceof AffinityIndexButton aib) {
                // TODO Set the new grimoire topic and open a new screen for it
                LogUtils.getLogger().info("Clicked affinity index button");
                // aib.getScreen().gotoTopic(AttunementIndexPage.TOPIC);
            }
        }
    }
}
