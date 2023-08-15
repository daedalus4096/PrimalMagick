package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
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
    private static final ResourceLocation TEXTURE = PrimalMagick.resource("textures/gui/grimoire.png");

    protected GrimoireScreen screen;
    
    public BackButton(int x, int y, GrimoireScreen screen) {
        super(Button.builder(Component.empty(), new Handler()).bounds(x, y, 16, 8));
        this.screen = screen;
    }
    
    public GrimoireScreen getScreen() {
        return this.screen;
    }
    
    @Override
    public void renderWidget(GuiGraphics guiGraphics, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft mc = Minecraft.getInstance();
        guiGraphics.pose().pushPose();
        
        // When hovered, scale the button up and down to create a pulsing effect
        float scaleMod = this.isHoveredOrFocused() ? Mth.sin(mc.player.tickCount / 3.0F) * 0.2F + 0.1F : 0.0F;
        int dx = this.width / 2;
        int dy = this.height / 2;
        guiGraphics.pose().translate(this.getX() + dx, this.getY() + dy, 0.0F);
        guiGraphics.pose().scale(1.5F + scaleMod, 1.5F + scaleMod, 1.0F);
        guiGraphics.blit(TEXTURE, -dx, -dy, 40, 204, this.width, this.height);

        guiGraphics.pose().popPose();
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
