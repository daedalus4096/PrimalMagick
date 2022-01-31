package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.resources.ResourceLocation;

/**
 * Icon to show a generic texture on a grimoire topic button.
 * 
 * @author Daedalus4096
 */
public class GenericIndexIcon extends AbstractIndexIcon {
    protected final ResourceLocation iconLocation;
    
    protected GenericIndexIcon(ResourceLocation loc, boolean large) {
        super(large);
        this.iconLocation = loc;
    }
    
    public static GenericIndexIcon of(ResourceLocation loc, boolean large) {
        return new GenericIndexIcon(loc, large);
    }

    @Override
    public void render(PoseStack poseStack, double x, double y) {
        if (this.iconLocation != null) {
            float scale = this.large ? 0.06F : 0.04F;
            poseStack.pushPose();
            poseStack.translate(x, y, 0d);
            poseStack.scale(scale, scale, scale);
            RenderSystem.setShaderTexture(0, this.iconLocation);
            this.blit(poseStack, 0, 0, 0, 0, 255, 255);
            poseStack.popPose();
        }
    }
}
