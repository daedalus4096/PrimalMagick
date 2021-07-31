package com.verdantartifice.primalmagic.client.gui.widgets.research_table;

import java.awt.Color;
import java.util.Collections;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.util.GuiUtils;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Display widget for showing accumulated knowledge (e.g. observations) in the research table GUI.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class KnowledgeTotalWidget extends AbstractWidget {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/research_table_overlay.png");

    protected IPlayerKnowledge.KnowledgeType type;
    protected IPlayerKnowledge knowledge;
    
    public KnowledgeTotalWidget(int x, int y, IPlayerKnowledge.KnowledgeType type) {
        super(x, y, 16, 19, TextComponent.EMPTY);
        Minecraft mc = Minecraft.getInstance();
        this.type = type;
        this.knowledge = PrimalMagicCapabilities.getKnowledge(mc.player);
    }
    
    @Override
    public void renderButton(PoseStack matrixStack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft mc = Minecraft.getInstance();
        
        // Draw knowledge type icon
        matrixStack.pushPose();
        mc.getTextureManager().bindForSetup(this.type.getIconLocation());
        matrixStack.translate(this.x, this.y, 0.0F);
        matrixStack.scale(0.0625F, 0.0625F, 0.0625F);
        this.blit(matrixStack, 0, 0, 0, 0, 255, 255);        
        matrixStack.popPose();
        
        // Draw progress bar background
        matrixStack.pushPose();
        mc.getTextureManager().bindForSetup(TEXTURE);
        matrixStack.translate(this.x, this.y + 17, 0.0F);
        this.blit(matrixStack, 0, 0, 182, 2, 16, 2);
        matrixStack.popPose();
        
        if (this.knowledge != null) {
            // Draw amount str
            int levels = this.knowledge.getKnowledge(this.type);
            Component amountText = new TextComponent(Integer.toString(levels));
            int width = mc.font.width(amountText);
            matrixStack.pushPose();
            matrixStack.translate(this.x + 16 - width / 2, this.y + 12, 5.0F);
            matrixStack.scale(0.5F, 0.5F, 0.5F);
            mc.font.drawShadow(matrixStack, amountText, 0.0F, 0.0F, Color.WHITE.getRGB());
            matrixStack.popPose();
            
            // Draw progress bar foreground
            int rawPoints = this.knowledge.getKnowledgeRaw(this.type);
            int levelPoints = rawPoints % this.type.getProgression();
            int px = (int)(16.0D * ((double)levelPoints / (double)this.type.getProgression()));
            matrixStack.pushPose();
            mc.getTextureManager().bindForSetup(TEXTURE);
            matrixStack.translate(this.x, this.y + 17, 1.0F);
            this.blit(matrixStack, 0, 0, 182, 0, px, 2);
            matrixStack.popPose();
        }
        
        if (this.isHovered()) {
            // Render tooltip
            Component knowledgeText = new TranslatableComponent(this.type.getNameTranslationKey());
            GuiUtils.renderCustomTooltip(matrixStack, Collections.singletonList(knowledgeText), this.x, this.y);
        }
    }
    
    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }

    @Override
    public void updateNarration(NarrationElementOutput output) {
    }
}
