package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * GUI button to navigate to the next or previous page of the current grimoire entry.
 * 
 * @author Daedalus4096
 */
public class PageButton extends Button {
    private static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagick.MODID, "textures/gui/grimoire.png");

    protected GrimoireScreen screen;
    protected boolean isNext;

    public PageButton(int widthIn, int heightIn, GrimoireScreen screen, boolean isNext) {
        super(widthIn, heightIn, 12, 5, Component.empty(), new Handler());
        this.screen = screen;
        this.isNext = isNext;
    }
    
    public GrimoireScreen getScreen() {
        return this.screen;
    }
    
    public boolean isNext() {
        return this.isNext;
    }
    
    @Override
    public void renderButton(PoseStack matrixStack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft mc = Minecraft.getInstance();
        RenderSystem.setShaderTexture(0, TEXTURE);
        matrixStack.pushPose();

        // When hovered, scale the button up and down to create a pulsing effect
        float scaleMod = this.isHoveredOrFocused() ? Mth.sin(mc.player.tickCount / 3.0F) * 0.2F + 0.1F : 0.0F;
        int dx = this.width / 2;
        int dy = this.height / 2;
        matrixStack.translate(this.getX() + dx, this.getY() + dy, 0.0F);
        matrixStack.scale(1.5F + scaleMod, 1.5F + scaleMod, 1.0F);
        this.blit(matrixStack, -dx, -dy, this.isNext ? 12 : 0, 185, this.width, this.height);

        matrixStack.popPose();
}
    
    @Override
    public void playDownSound(SoundManager handler) {
        handler.play(SimpleSoundInstance.forUI(SoundsPM.PAGE.get(), 1.0F, 1.0F));
    }

    private static class Handler implements OnPress {
        @Override
        public void onPress(Button button) {
            if (button instanceof PageButton) {
                PageButton gpb = (PageButton)button;
                if (gpb.isNext()) {
                    gpb.getScreen().nextPage();
                } else {
                    gpb.getScreen().prevPage();
                }
            }
        }
    }
}
