package com.verdantartifice.primalmagic.client.gui.widgets;

import java.awt.Color;
import java.util.Collections;

import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.util.GuiUtils;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Display widget for showing accumulated knowledge (e.g. observations) in the research table GUI.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class KnowledgeTotalWidget extends Widget {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/research_table_overlay.png");

    protected IPlayerKnowledge.KnowledgeType type;
    protected IPlayerKnowledge knowledge;
    
    public KnowledgeTotalWidget(int x, int y, IPlayerKnowledge.KnowledgeType type) {
        super(x, y, 16, 19, "");
        this.type = type;
        this.knowledge = PrimalMagicCapabilities.getKnowledge(Minecraft.getInstance().player);
    }
    
    @Override
    public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft mc = Minecraft.getInstance();
        
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        
        // Draw knowledge type icon
        RenderSystem.pushMatrix();
        mc.getTextureManager().bindTexture(this.type.getIconLocation());
        RenderSystem.translatef(this.x, this.y, 0.0F);
        RenderSystem.scaled(0.0625D, 0.0625D, 0.0625D);
        this.blit(0, 0, 0, 0, 255, 255);        
        RenderSystem.popMatrix();
        
        // Draw progress bar background
        RenderSystem.pushMatrix();
        mc.getTextureManager().bindTexture(TEXTURE);
        RenderSystem.translatef(this.x, this.y + 17, 0.0F);
        this.blit(0, 0, 182, 2, 16, 2);
        RenderSystem.popMatrix();
        
        if (this.knowledge != null) {
            // Draw amount str
            int levels = this.knowledge.getKnowledge(this.type);
            ITextComponent amountText = new StringTextComponent(Integer.toString(levels));
            int width = mc.fontRenderer.getStringWidth(amountText.getFormattedText());
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.pushMatrix();
            RenderSystem.translatef(this.x + 16 - width / 2, this.y + 12, 5.0F);
            RenderSystem.scaled(0.5D, 0.5D, 0.5D);
            mc.fontRenderer.drawStringWithShadow(amountText.getFormattedText(), 0.0F, 0.0F, Color.WHITE.getRGB());
            RenderSystem.popMatrix();
            
            // Draw progress bar foreground
            int rawPoints = this.knowledge.getKnowledgeRaw(this.type);
            int levelPoints = rawPoints % this.type.getProgression();
            int px = (int)(16.0D * ((double)levelPoints / (double)this.type.getProgression()));
            RenderSystem.pushMatrix();
            mc.getTextureManager().bindTexture(TEXTURE);
            RenderSystem.translatef(this.x, this.y + 17, 1.0F);
            this.blit(0, 0, 182, 0, px, 2);
            RenderSystem.popMatrix();
        }
        
        if (this.isHovered()) {
            // Render tooltip
            ITextComponent knowledgeText = new TranslationTextComponent(this.type.getNameTranslationKey());
            GuiUtils.renderCustomTooltip(Collections.singletonList(knowledgeText), this.x, this.y);
        }
    }
    
    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }
}
