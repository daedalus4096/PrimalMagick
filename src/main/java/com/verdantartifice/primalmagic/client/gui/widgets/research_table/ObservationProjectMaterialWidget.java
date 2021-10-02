package com.verdantartifice.primalmagic.client.gui.widgets.research_table;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.theorycrafting.ObservationProjectMaterial;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.Block;

/**
 * Display widget for an observation research project material.  Used on the research table screen.
 * 
 * @author Daedalus4096
 */
public class ObservationProjectMaterialWidget extends AbstractProjectMaterialWidget {
    protected ObservationProjectMaterial material;
    
    public ObservationProjectMaterialWidget(ObservationProjectMaterial material, int x, int y, Set<Block> surroundings) {
        super(material, x, y, surroundings);
        this.material = material;
    }
    
    @Override
    public void renderButton(PoseStack matrixStack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        // Draw observation icon
        RenderSystem.setShaderTexture(0, IPlayerKnowledge.KnowledgeType.OBSERVATION.getIconLocation());
        matrixStack.pushPose();
        matrixStack.translate(this.x, this.y, 0.0F);
        matrixStack.scale(0.0625F, 0.0625F, 0.0625F);
        this.blit(matrixStack, 0, 0, 0, 0, 255, 255);
        matrixStack.popPose();
        
        // Draw base class stuff
        super.renderButton(matrixStack, p_renderButton_1_, p_renderButton_2_, p_renderButton_3_);
    }
    
    @Override
    protected List<Component> getHoverText() {
        return Collections.singletonList(new TranslatableComponent(IPlayerKnowledge.KnowledgeType.OBSERVATION.getNameTranslationKey()));
    }
}
