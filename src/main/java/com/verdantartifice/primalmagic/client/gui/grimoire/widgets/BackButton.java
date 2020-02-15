package com.verdantartifice.primalmagic.client.gui.grimoire.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.gui.grimoire.GrimoireScreen;
import com.verdantartifice.primalmagic.common.sounds.SoundsPM;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
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
        super(widthIn, heightIn, 16, 8, "", new Handler());
        this.screen = screen;
    }
    
    public GrimoireScreen getScreen() {
        return this.screen;
    }
    
    @Override
    public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft mc = Minecraft.getInstance();
        mc.getTextureManager().bindTexture(TEXTURE);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.isHovered()) {
            // When hovered, scale the button up and down to create a pulsing effect
            float scaleMod = MathHelper.sin(mc.player.ticksExisted / 3.0F) * 0.2F + 0.1F;
            RenderSystem.pushMatrix();
            int dx = this.width / 2;
            int dy = this.height / 2;
            RenderSystem.translatef(this.x + dx, this.y + dy, 0.0F);
            RenderSystem.scalef(1.0F + scaleMod, 1.0F + scaleMod, 1.0F);
            this.blit(-dx, -dy, 40, 204, this.width, this.height);
            RenderSystem.popMatrix();
        } else {
            this.blit(this.x, this.y, 40, 204, this.width, this.height);
        }
    }

    @Override
    public void playDownSound(SoundHandler handler) {
        handler.play(SimpleSound.master(SoundsPM.PAGE, 1.0F, 1.0F));
    }

    private static class Handler implements IPressable {
        @Override
        public void onPress(Button button) {
            if (button instanceof BackButton) {
                BackButton gbb = (BackButton)button;
                gbb.getScreen().goBack();
            }
        }
    }
}
