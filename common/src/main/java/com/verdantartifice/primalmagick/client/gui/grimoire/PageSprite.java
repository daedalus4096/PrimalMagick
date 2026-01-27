package com.verdantartifice.primalmagick.client.gui.grimoire;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

/**
 * An image element to be rendered on a grimoire page.
 * 
 * @author Daedalus4096
 */
public record PageSprite(Identifier location, int width, int height, float scale) implements IPageElement {
    public static final int MAX_WIDTH = 208;
    public static final int MAX_HEIGHT = 140;
    private static final int EXPECTED_TOKENS = 5;

    public PageSprite(Identifier location, int width, int height) {
        this(location, width, height, 1F);
    }

    public int adjustedWidth() {
        return (int)(this.width() * this.scale());
    }

    public int adjustedHeight() {
        return (int)(this.height() * this.scale());
    }
    
    @Nullable
    public static PageSprite parse(String str) {
        // Parse the string representation of a page image into an element
        String[] tokens = str.split(":");
        if (tokens.length != EXPECTED_TOKENS) {
            return null;
        }
        try {
            Identifier location = Identifier.fromNamespaceAndPath(tokens[0], tokens[1]);
            int width = Integer.parseInt(tokens[2]);
            int height = Integer.parseInt(tokens[3]);
            float scale = Float.parseFloat(tokens[4]);
            PageSprite image = new PageSprite(location, width, height, scale);
            if (image.adjustedWidth() > MAX_WIDTH || image.adjustedHeight() > MAX_HEIGHT) {
                // If the adjusted size of the image is too big, abort
                return null;
            }
            return image;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int side, int x, int y) {
        // Render the image at this element's resource location to the screen
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(x - 15 + (side * 152) + ((124 - this.adjustedWidth()) / 2), y - 5);
        guiGraphics.pose().scale(this.scale, this.scale);
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.location, 0, 0, this.width, this.height);
        guiGraphics.pose().popMatrix();
    }

    @Override
    public int getHeight() {
        return this.adjustedHeight() + 2;
    }
}
