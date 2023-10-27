package com.verdantartifice.primalmagick.client.gui.widgets.research_table;

import java.awt.Color;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagick.common.theorycrafting.ExperienceProjectMaterial;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

/**
 * Display widget for an experience research project material.  Used on the research table screen.
 * 
 * @author Daedalus4096
 */
public class ExperienceProjectMaterialWidget extends AbstractProjectMaterialWidget<ExperienceProjectMaterial> {
    private static final ResourceLocation EXPERIENCE_ORB_TEXTURES = new ResourceLocation("textures/entity/experience_orb.png");

    public ExperienceProjectMaterialWidget(ExperienceProjectMaterial material, int x, int y, Set<Block> surroundings) {
        super(material, x, y, surroundings);
    }
    
    @Override
    public void renderWidget(GuiGraphics guiGraphics, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft mc = Minecraft.getInstance();

        // Draw experience orb
        int textureIndex = this.getTextureIndexByXP(7 * this.material.getLevels());
        int uMin = (textureIndex % 4 * 16) * 4;
        int vMin = (textureIndex / 4 * 16) * 4;
        double approxTicks = (System.currentTimeMillis() / 50.0D);
        float r = (float)(Math.sin(approxTicks) + 1.0F) * 0.5F;
        float g = 1.0F;
        float b = (float)(Math.sin(approxTicks + 4.1887903F) + 1.0F) * 0.1F;
        float a = 0.5F;
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(this.getX(), this.getY(), 0.0F);
        guiGraphics.pose().scale(0.25F, 0.25F, 0.25F);
        RenderSystem.setShaderColor(r, g, b, a);
        guiGraphics.blit(EXPERIENCE_ORB_TEXTURES, 0, 0, uMin, vMin, 63, 63);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.pose().popPose();

        // If applicable, draw level count string
        if (this.material.getLevels() > 1) {
            Component amountText = Component.literal(Integer.toString(this.material.getLevels()));
            int width = mc.font.width(amountText);
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(this.getX() + 16 - width / 2, this.getY() + 12, 200.0F);
            guiGraphics.pose().scale(0.5F, 0.5F, 0.5F);
            guiGraphics.drawString(mc.font, amountText, 0, 0, Color.WHITE.getRGB());
            guiGraphics.pose().popPose();
        }

        // Draw base class stuff
        super.renderWidget(guiGraphics, p_renderButton_1_, p_renderButton_2_, p_renderButton_3_);
    }
    
    protected int getTextureIndexByXP(int xpValue) {
        if (xpValue >= 2477) {
            return 10;
         } else if (xpValue >= 1237) {
            return 9;
         } else if (xpValue >= 617) {
            return 8;
         } else if (xpValue >= 307) {
            return 7;
         } else if (xpValue >= 149) {
            return 6;
         } else if (xpValue >= 73) {
            return 5;
         } else if (xpValue >= 37) {
            return 4;
         } else if (xpValue >= 17) {
            return 3;
         } else if (xpValue >= 7) {
            return 2;
         } else {
            return xpValue >= 3 ? 1 : 0;
         }
    }
    
    @Override
    protected List<Component> getHoverText() {
        return Collections.singletonList(Component.translatable("argument.entity.options.level.description"));
    }
}
