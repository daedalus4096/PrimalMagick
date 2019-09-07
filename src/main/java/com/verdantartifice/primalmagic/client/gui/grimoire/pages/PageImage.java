package com.verdantartifice.primalmagic.client.gui.grimoire.pages;

import javax.annotation.Nullable;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PageImage extends AbstractGui implements IPageElement {
    public int x, y, width, height, adjustedWidth, adjustedHeight;
    public float scale;
    public ResourceLocation location;
    
    @Nullable
    public static PageImage parse(String str) {
        String[] tokens = str.split(":");
        if (tokens.length != 7) {
            return null;
        }
        try {
            PageImage image = new PageImage();
            image.location = new ResourceLocation(tokens[0], tokens[1]);
            image.x = Integer.parseInt(tokens[2]);
            image.y = Integer.parseInt(tokens[3]);
            image.width = Integer.parseInt(tokens[4]);
            image.height = Integer.parseInt(tokens[5]);
            image.scale = Float.parseFloat(tokens[6]);
            image.adjustedWidth = (int)(image.width * image.scale);
            image.adjustedHeight = (int)(image.height * image.scale);
            if (image.adjustedWidth > 208 || image.adjustedHeight > 140) {
                return null;
            }
            return image;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void render(int side, int x, int y) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.pushMatrix();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bindTexture(this.location);
        GlStateManager.translatef(x - 15 + (side * 152) + ((140 - this.adjustedWidth) / 2), y - 5, 0.0F);
        GlStateManager.scalef(this.scale, this.scale, this.scale);
        this.blit(0, 0, this.x, this.y, this.width, this.height);
        GlStateManager.popMatrix();
    }

    @Override
    public int getNextY(int y) {
        return (y + this.adjustedHeight + 2);
    }
}
