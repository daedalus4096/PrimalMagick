package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.common.research.topics.AbstractResearchTopic;
import com.verdantartifice.primalmagick.common.research.topics.TopicLink;
import net.minecraft.client.gui.components.Button;

/**
 * GUI button to navigate the Grimoire to the given topic link target.
 *
 * @author Daedalus4096
 */
public class TopicLinkButton extends Button {
    public static final int WIDTH = 119;
    public static final int HEIGHT = 20;

    protected final AbstractResearchTopic<?> target;
    protected final GrimoireScreen screen;

    public TopicLinkButton(TopicLink link, int x, int y, GrimoireScreen screen) {
        super(x, y, WIDTH, HEIGHT, link.getDisplayText(), new Handler(), Button.DEFAULT_NARRATION);
        this.target = link.target();
        this.screen = screen;
    }

    public AbstractResearchTopic<?> getTarget() {
        return this.target;
    }

    public GrimoireScreen getScreen() {
        return this.screen;
    }

    private static class Handler implements OnPress {
        @Override
        public void onPress(Button button) {
            if (button instanceof TopicLinkButton tlb) {
                tlb.getScreen().gotoTopic(tlb.getTarget());
            }
        }
    }
}
