package com.verdantartifice.primalmagick.client.gui.grimoire;

import javax.annotation.Nullable;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;

/**
 * An image element to be rendered on a grimoire page.
 * 
 * @author Daedalus4096
 */
public class PageImage extends GuiComponent implements IPageElement {
    public int x, y, width, height, adjustedWidth, adjustedHeight;
    public float scale;
    public ResourceLocation location;
    
    @Nullable
    public static PageImage parse(String str) {
        // Parse the string representation of a page image into an element
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
                // If the adjusted size of the image is too big, abort
                return null;
            }
            return image;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void render(PoseStack matrixStack, int side, int x, int y) {
        // Render the image at this element's resource location to the screen
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        matrixStack.pushPose();
        RenderSystem.setShaderTexture(0, this.location);
        matrixStack.translate(x - 15 + (side * 152) + ((124 - this.adjustedWidth) / 2), y - 5, 0.0F);
        matrixStack.scale(this.scale, this.scale, this.scale);
        this.blit(matrixStack, 0, 0, this.x, this.y, this.width, this.height);
        matrixStack.popPose();
    }

    @Override
    public int getNextY(int y) {
        return (y + this.adjustedHeight + 2);
    }
}
