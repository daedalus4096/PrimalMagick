package com.verdantartifice.primalmagic.client.gui.widgets.research_table;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagic.common.theorycrafting.ExperienceProjectMaterial;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Display widget for an experience research project material.  Used on the research table screen.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class ExperienceProjectMaterialWidget extends AbstractProjectMaterialWidget {
    private static final ResourceLocation EXPERIENCE_ORB_TEXTURES = new ResourceLocation("textures/entity/experience_orb.png");

    protected ExperienceProjectMaterial material;
    
    public ExperienceProjectMaterialWidget(ExperienceProjectMaterial material, int x, int y) {
        super(material, x, y);
        this.material = material;
    }
    
    @Override
    public void renderButton(PoseStack matrixStack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
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
        Minecraft.getInstance().getTextureManager().bindForSetup(EXPERIENCE_ORB_TEXTURES);
        matrixStack.pushPose();
        matrixStack.translate(this.x, this.y, 0.0F);
        matrixStack.scale(0.25F, 0.25F, 0.25F);
        RenderSystem.setShaderColor(r, g, b, a);
        this.blit(matrixStack, 0, 0, uMin, vMin, 63, 63);
        matrixStack.popPose();

        // If applicable, draw level count string
        if (this.material.getLevels() > 1) {
            Component amountText = new TextComponent(Integer.toString(this.material.getLevels()));
            int width = mc.font.width(amountText);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.pushPose();
            matrixStack.translate(this.x + 16 - width / 2, this.y + 12, 500.0F);
            matrixStack.scale(0.5F, 0.5F, 0.5F);
            mc.font.drawShadow(matrixStack, amountText, 0.0F, 0.0F, Color.WHITE.getRGB());
            matrixStack.popPose();
        }

        // Draw base class stuff
        super.renderButton(matrixStack, p_renderButton_1_, p_renderButton_2_, p_renderButton_3_);
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
        return Collections.singletonList(new TranslatableComponent("argument.entity.options.level.description"));
    }
}
