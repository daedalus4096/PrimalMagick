package com.verdantartifice.primalmagic.client.gui.widgets;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.util.GuiUtils;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.theorycrafting.AbstractProjectMaterial;
import com.verdantartifice.primalmagic.common.theorycrafting.ItemProjectMaterial;
import com.verdantartifice.primalmagic.common.theorycrafting.ObservationProjectMaterial;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Display widget for a research project material.  Used on the research table screen.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class ProjectMaterialWidget extends Widget {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/research_table_overlay.png");

    protected AbstractProjectMaterial material;
    protected boolean complete;
    
    public ProjectMaterialWidget(AbstractProjectMaterial material, int x, int y, boolean complete) {
        super(x, y, 16, 16, "");
        this.material = material;
        this.complete = complete;
    }
    
    @Override
    public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft mc = Minecraft.getInstance();
        
        ITextComponent displayNameText = new StringTextComponent("");
        if (this.material instanceof ItemProjectMaterial) {
            // Draw stack icon and, if applicable, amount string
            ItemProjectMaterial ipm = (ItemProjectMaterial)this.material;
            GuiUtils.renderItemStack(ipm.getItemStack(), this.x, this.y, this.getMessage(), false);
            if (ipm.getItemStack().getCount() > 1) {
                ITextComponent amountText = new StringTextComponent(Integer.toString(ipm.getItemStack().getCount()));
                int width = mc.fontRenderer.getStringWidth(amountText.getFormattedText());
                RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.pushMatrix();
                RenderSystem.translatef(this.x + 16 - width / 2, this.y + 12, 500.0F);
                RenderSystem.scaled(0.5D, 0.5D, 0.5D);
                mc.fontRenderer.drawStringWithShadow(amountText.getFormattedText(), 0.0F, 0.0F, Color.WHITE.getRGB());
                RenderSystem.popMatrix();
            }
            displayNameText = ipm.getItemStack().getDisplayName();
        } else if (this.material instanceof ObservationProjectMaterial) {
            // Draw observation icon
            mc.getTextureManager().bindTexture(IPlayerKnowledge.KnowledgeType.OBSERVATION.getIconLocation());
            RenderSystem.pushMatrix();
            RenderSystem.translatef(this.x, this.y, 0.0F);
            RenderSystem.scaled(0.0625D, 0.0625D, 0.0625D);
            this.blit(0, 0, 0, 0, 255, 255);
            RenderSystem.popMatrix();
            displayNameText = new TranslationTextComponent(IPlayerKnowledge.KnowledgeType.OBSERVATION.getNameTranslationKey());
        }
        
        if (this.complete) {
            // Render completion checkmark if appropriate
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.pushMatrix();
            RenderSystem.translatef(this.x + 8, this.y, 200.0F);
            Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE);
            this.blit(0, 0, 162, 0, 10, 10);
            RenderSystem.popMatrix();
        }
        if (this.material.isConsumed()) {
            // Render consumption exclamation point if appropriate
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.pushMatrix();
            RenderSystem.translatef(this.x - 3, this.y, 200.0F);
            Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE);
            this.blit(0, 0, 172, 0, 10, 10);
            RenderSystem.popMatrix();
        }
        if (this.isHovered()) {
            // Render tooltip
            List<ITextComponent> textList = Collections.singletonList(displayNameText);
            GuiUtils.renderCustomTooltip(textList, this.x, this.y);
        }
    }
    
    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }
}
