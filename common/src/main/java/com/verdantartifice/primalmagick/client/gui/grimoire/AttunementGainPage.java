package com.verdantartifice.primalmagick.client.gui.grimoire;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.common.rewards.AttunementReward;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Grimoire page showing the attunements gained from a research stage.
 * 
 * @author Daedalus4096
 */
public class AttunementGainPage extends AbstractPage {
    protected final List<AttunementReward> rewards;

    public AttunementGainPage(@Nonnull List<AttunementReward> rewards) {
        this.rewards = rewards;
    }
    
    @Override
    protected boolean renderTopTitleBar() {
        return false;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int side, int x, int y, int mouseX, int mouseY) {
        // Render page title
        this.renderTitle(guiGraphics, side, x, y, mouseX, mouseY, null);
        y += 53;

        // Render attunement gain list
        Minecraft mc = Minecraft.getInstance();
        for (AttunementReward reward : this.rewards) {
            Component fullText = reward.getDescription(mc.player);
            guiGraphics.drawString(mc.font, fullText, x - 3 + (side * 140), y - 6, 0, false);
            y += mc.font.lineHeight;
        }
    }

    @Override
    protected Component getTitleText() {
        return Component.translatable("grimoire.primalmagick.attunement_gain_header");
    }

    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {}
}
