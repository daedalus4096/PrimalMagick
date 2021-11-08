package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.PrimalMagic;
import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * GUI button to go back to the last-viewed topic in the grimoire.
 * 
 * @author Daedalus4096
 */
public class BackButton extends Button {
    private static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/grimoire.png");

    protected GrimoireScreen screen;
    
    public BackButton(int widthIn, int heightIn, GrimoireScreen screen) {
        super(widthIn, heightIn, 16, 8, TextComponent.EMPTY, new Handler());
        this.screen = screen;
    }
    
    public GrimoireScreen getScreen() {
        return this.screen;
    }
    
    @Override
    public void renderButton(PoseStack matrixStack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft mc = Minecraft.getInstance();
        RenderSystem.setShaderTexture(0, TEXTURE);
        matrixStack.pushPose();
        
        // When hovered, scale the button up and down to create a pulsing effect
        float scaleMod = this.isHovered() ? Mth.sin(mc.player.tickCount / 3.0F) * 0.2F + 0.1F : 0.0F;
        int dx = this.width / 2;
        int dy = this.height / 2;
        matrixStack.translate(this.x + dx, this.y + dy, 0.0F);
        matrixStack.scale(1.5F + scaleMod, 1.5F + scaleMod, 1.0F);
        this.blit(matrixStack, -dx, -dy, 40, 204, this.width, this.height);

        matrixStack.popPose();
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
