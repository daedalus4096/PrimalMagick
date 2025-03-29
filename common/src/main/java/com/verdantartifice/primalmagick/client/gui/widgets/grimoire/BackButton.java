package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * GUI button to go back to the last-viewed topic in the grimoire.
 * 
 * @author Daedalus4096
 */
public class BackButton extends Button {
    private static final ResourceLocation TEXTURE = ResourceUtils.loc("textures/gui/grimoire.png");
    private static final ResourceLocation UNREAD_SPRITE = ResourceUtils.loc("grimoire/unread");
    private static final int UNREAD_WIDTH = 16;
    private static final int UNREAD_HEIGHT = 16;

    protected GrimoireScreen screen;
    
    public BackButton(int x, int y, GrimoireScreen screen) {
        super(x, y, 16, 8, Component.empty(), new Handler(), Button.DEFAULT_NARRATION);
        this.screen = screen;
        Minecraft mc = Minecraft.getInstance();
        this.screen.getPreviousTopic().flatMap(topic -> topic.getUnreadTooltip(mc.player)).ifPresent(component -> {
            this.setTooltip(Tooltip.create(component));
        });
    }
    
    public GrimoireScreen getScreen() {
        return this.screen;
    }
    
    @Override
    public void renderWidget(GuiGraphics guiGraphics, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft mc = Minecraft.getInstance();

        // When hovered, scale the button up and down to create a pulsing effect
        float scaleMod = this.isHoveredOrFocused() ? Mth.sin(mc.player.tickCount / 3.0F) * 0.2F + 0.1F : 0.0F;
        int dx = this.width / 2;
        int dy = this.height / 2;
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(this.getX() + dx, this.getY() + dy, 0.0F);
        guiGraphics.pose().scale(1.5F + scaleMod, 1.5F + scaleMod, 1.0F);
        guiGraphics.blit(TEXTURE, -dx, -dy, 40, 204, this.width, this.height);
        guiGraphics.pose().popPose();

        // If the previous topic is unread, show the unread icon above the button
        if (this.screen.getPreviousTopic().map(topic -> topic.isUnread(mc.player)).orElse(false)) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(this.getX() + dx + 1, this.getY() - dy, 5F);
            guiGraphics.pose().scale(0.4F, 0.4F, 1F);
            guiGraphics.blitSprite(UNREAD_SPRITE, 0, 0, UNREAD_WIDTH, UNREAD_HEIGHT);
            guiGraphics.pose().popPose();
        }
    }

    @Override
    public void playDownSound(SoundManager handler) {
        handler.play(SimpleSoundInstance.forUI(SoundsPM.PAGE.get(), 1.0F, 1.0F));
    }

    private static class Handler implements OnPress {
        @Override
        public void onPress(Button button) {
            if (button instanceof BackButton) {
                BackButton gbb = (BackButton)button;
                gbb.getScreen().goBack();
            }
        }
    }
}
