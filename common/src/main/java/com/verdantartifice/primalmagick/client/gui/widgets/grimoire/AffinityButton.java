package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.mojang.logging.LogUtils;
import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.common.sources.Source;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class AffinityButton extends AbstractTopicButton {
    protected Source source;

    public AffinityButton(int widthIn, int heightIn, Component text, GrimoireScreen screen, Source source) {
        super(widthIn, heightIn, 123, 12, text, screen, GenericIndexIcon.of(source.getImage(), false), new AffinityButton.Handler());
        this.source = source;
    }

    public Source getSource() {
        return this.source;
    }

    private static class Handler implements OnPress {
        @Override
        public void onPress(Button button) {
            if (button instanceof AffinityButton gab) {
                // TODO Set the new grimoire topic and open a new screen for it
                LogUtils.getLogger().info("Affinity button pressed: {}", gab.getSource());
//                gab.getScreen().gotoTopic(new SourceResearchTopic(gab.getSource(), 0));
            }
        }
    }
}
