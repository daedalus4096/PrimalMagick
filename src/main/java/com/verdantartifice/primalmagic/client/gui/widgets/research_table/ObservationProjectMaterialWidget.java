package com.verdantartifice.primalmagic.client.gui.widgets.research_table;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.theorycrafting.ObservationProjectMaterial;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Display widget for an observation research project material.  Used on the research table screen.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class ObservationProjectMaterialWidget extends AbstractProjectMaterialWidget {
    protected ObservationProjectMaterial material;
    
    public ObservationProjectMaterialWidget(ObservationProjectMaterial material, int x, int y) {
        super(material, x, y);
        this.material = material;
    }
    
    @Override
    public void renderButton(MatrixStack matrixStack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        // Draw observation icon
        Minecraft.getInstance().getTextureManager().bindTexture(IPlayerKnowledge.KnowledgeType.OBSERVATION.getIconLocation());
        RenderSystem.pushMatrix();
        RenderSystem.translatef(this.x, this.y, 0.0F);
        RenderSystem.scaled(0.0625D, 0.0625D, 0.0625D);
        this.blit(matrixStack, 0, 0, 0, 0, 255, 255);
        RenderSystem.popMatrix();
        
        // Draw base class stuff
        super.renderButton(matrixStack, p_renderButton_1_, p_renderButton_2_, p_renderButton_3_);
    }
    
    @Override
    protected ITextComponent getHoverText() {
        return new TranslationTextComponent(IPlayerKnowledge.KnowledgeType.OBSERVATION.getNameTranslationKey());
    }
}
