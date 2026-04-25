package com.verdantartifice.primalmagick.client.gui.widgets.research_table;

import com.verdantartifice.primalmagick.common.research.KnowledgeType;
import com.verdantartifice.primalmagick.common.theorycrafting.materials.ObservationProjectMaterial;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Set;

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
    public void extractWidgetRenderState(@NotNull GuiGraphicsExtractor pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        // Draw observation icon
        pGuiGraphics.pose().pushMatrix();
        pGuiGraphics.pose().translate(this.getX(), this.getY());
        pGuiGraphics.pose().scale(0.0625F, 0.0625F);
        pGuiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, KnowledgeType.OBSERVATION.getIconLocation(), 0, 0, 16, 16);
        pGuiGraphics.pose().popMatrix();
        
        // Draw base class stuff
        super.extractWidgetRenderState(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }
    
    @Override
    protected List<Component> getHoverText() {
        return Collections.singletonList(Component.translatable(KnowledgeType.OBSERVATION.getNameTranslationKey()));
    }
}
