package com.verdantartifice.primalmagick.client.gui.widgets.research_table;

import java.awt.Color;
import java.util.Collections;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.util.GuiUtils;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.LazyOptional;

/**
 * Display widget for showing accumulated knowledge (e.g. observations) in the research table GUI.
 * 
 * @author Daedalus4096
 */
public class KnowledgeTotalWidget extends AbstractWidget {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagick.MODID, "textures/gui/research_table_overlay.png");

    protected IPlayerKnowledge.KnowledgeType type;
    protected LazyOptional<IPlayerKnowledge> knowledgeOpt;
    
    public KnowledgeTotalWidget(int x, int y, IPlayerKnowledge.KnowledgeType type) {
        super(x, y, 16, 19, Component.empty());
        Minecraft mc = Minecraft.getInstance();
        this.type = type;
        this.knowledgeOpt = PrimalMagickCapabilities.getKnowledge(mc.player);
    }
    
    @Override
    public void renderButton(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        
        // Draw knowledge type icon
        matrixStack.pushPose();
        RenderSystem.setShaderTexture(0, this.type.getIconLocation());
        matrixStack.translate(this.getX(), this.getY(), 0.0F);
        matrixStack.scale(0.0625F, 0.0625F, 0.0625F);
        this.blit(matrixStack, 0, 0, 0, 0, 255, 255);        
        matrixStack.popPose();
        
        // Draw progress bar background
        matrixStack.pushPose();
        RenderSystem.setShaderTexture(0, TEXTURE);
        matrixStack.translate(this.getX(), this.getY() + 17, 0.0F);
        this.blit(matrixStack, 0, 0, 182, 2, 16, 2);
        matrixStack.popPose();
        
        this.knowledgeOpt.ifPresent(knowledge -> {
            // Draw amount str
            int levels = knowledge.getKnowledge(this.type);
            Component amountText = Component.literal(Integer.toString(levels));
            int width = mc.font.width(amountText);
            matrixStack.pushPose();
            matrixStack.translate(this.getX() + 16 - width / 2, this.getY() + 12, 5.0F);
            matrixStack.scale(0.5F, 0.5F, 0.5F);
            mc.font.drawShadow(matrixStack, amountText, 0.0F, 0.0F, Color.WHITE.getRGB());
            matrixStack.popPose();
            
            // Draw progress bar foreground
            int rawPoints = knowledge.getKnowledgeRaw(this.type);
            int levelPoints = rawPoints % this.type.getProgression();
            int px = (int)(16.0D * ((double)levelPoints / (double)this.type.getProgression()));
            matrixStack.pushPose();
            RenderSystem.setShaderTexture(0, TEXTURE);
            matrixStack.translate(this.getX(), this.getY() + 17, 1.0F);
            this.blit(matrixStack, 0, 0, 182, 0, px, 2);
            matrixStack.popPose();
        });
    }
    
    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput output) {
    }

    @Override
    public void renderToolTip(PoseStack matrixStack, int mouseX, int mouseY) {
        // Render tooltip
        Component knowledgeText = Component.translatable(this.type.getNameTranslationKey());
        GuiUtils.renderCustomTooltip(matrixStack, Collections.singletonList(knowledgeText), mouseX, mouseY);
    }
}
