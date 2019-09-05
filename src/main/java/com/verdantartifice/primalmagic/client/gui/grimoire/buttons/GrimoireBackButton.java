package com.verdantartifice.primalmagic.client.gui.grimoire.buttons;

import com.mojang.blaze3d.platform.GlStateManager;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.gui.grimoire.GrimoireScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class GrimoireBackButton extends Button {
    private static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/grimoire.png");

    protected GrimoireScreen screen;
    
    public GrimoireBackButton(int widthIn, int heightIn, GrimoireScreen screen) {
        super(widthIn, heightIn, 20, 12, "", new Handler());
        this.screen = screen;
    }
    
    public GrimoireScreen getScreen() {
        return this.screen;
    }
    
    @Override
    public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft mc = Minecraft.getInstance();
        mc.getTextureManager().bindTexture(TEXTURE);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.isHovered()) {
            float scaleMod = MathHelper.sin(mc.player.ticksExisted / 3.0F) * 0.2F + 0.1F;
            GlStateManager.pushMatrix();
            int dx = this.width / 2;
            int dy = this.height / 2;
            GlStateManager.translatef(this.x + dx, this.y + dy, 0.0F);
            GlStateManager.scalef(1.0F + scaleMod, 1.0F + scaleMod, 1.0F);
            this.blit(-dx, -dy, 40, 204, this.width, this.height);
            GlStateManager.popMatrix();
        } else {
            this.blit(this.x, this.y, 40, 204, this.width, this.height);
        }
    }

    private static class Handler implements IPressable {
        @Override
        public void onPress(Button button) {
            if (button instanceof GrimoireBackButton) {
                GrimoireBackButton gbb = (GrimoireBackButton)button;
                gbb.getScreen().goBack();
            }
        }
    }
}
