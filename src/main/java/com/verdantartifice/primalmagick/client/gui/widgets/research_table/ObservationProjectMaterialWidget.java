package com.verdantartifice.primalmagick.client.gui.widgets.research_table;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.verdantartifice.primalmagick.common.research.KnowledgeType;
import com.verdantartifice.primalmagick.common.theorycrafting.ObservationProjectMaterial;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;

/**
 * Display widget for an observation research project material.  Used on the research table screen.
 * 
 * @author Daedalus4096
 */
public class ObservationProjectMaterialWidget extends AbstractProjectMaterialWidget<ObservationProjectMaterial> {
    public ObservationProjectMaterialWidget(ObservationProjectMaterial material, int x, int y, Set<Block> surroundings) {
        super(material, x, y, surroundings);
    }
    
    @Override
    public void renderWidget(GuiGraphics guiGraphics, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        // Draw observation icon
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(this.getX(), this.getY(), 0.0F);
        guiGraphics.pose().scale(0.0625F, 0.0625F, 0.0625F);
        guiGraphics.blit(KnowledgeType.OBSERVATION.getIconLocation(), 0, 0, 0, 0, 255, 255);
        guiGraphics.pose().popPose();
        
        // Draw base class stuff
        super.renderWidget(guiGraphics, p_renderButton_1_, p_renderButton_2_, p_renderButton_3_);
    }
    
    @Override
    protected List<Component> getHoverText() {
        return Collections.singletonList(Component.translatable(KnowledgeType.OBSERVATION.getNameTranslationKey()));
    }
}
