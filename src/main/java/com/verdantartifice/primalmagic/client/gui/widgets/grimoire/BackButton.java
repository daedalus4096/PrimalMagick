package com.verdantartifice.primalmagic.client.gui.widgets.grimoire;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagic.common.sounds.SoundsPM;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * GUI button to go back to the last-viewed topic in the grimoire.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
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
        mc.getTextureManager().bindForSetup(TEXTURE);
        if (this.isHovered()) {
            // When hovered, scale the button up and down to create a pulsing effect
            float scaleMod = Mth.sin(mc.player.tickCount / 3.0F) * 0.2F + 0.1F;
            matrixStack.pushPose();
            int dx = this.width / 2;
            int dy = this.height / 2;
            matrixStack.translate(this.x + dx, this.y + dy, 0.0F);
            matrixStack.scale(1.0F + scaleMod, 1.0F + scaleMod, 1.0F);
            this.blit(matrixStack, -dx, -dy, 40, 204, this.width, this.height);
            matrixStack.popPose();
        } else {
            this.blit(matrixStack, this.x, this.y, 40, 204, this.width, this.height);
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
