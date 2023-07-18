package com.verdantartifice.primalmagick.client.gui.widgets.research_table;

import java.util.Set;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.theorycrafting.AbstractProjectMaterial;
import com.verdantartifice.primalmagick.common.theorycrafting.ExperienceProjectMaterial;
import com.verdantartifice.primalmagick.common.theorycrafting.ItemProjectMaterial;
import com.verdantartifice.primalmagick.common.theorycrafting.ItemTagProjectMaterial;
import com.verdantartifice.primalmagick.common.theorycrafting.ObservationProjectMaterial;

import net.minecraft.world.level.block.Block;

/**
 * Factory for creating widgets to display research project materials.
 * 
 * @author Daedalus4096
 */
public class ProjectMaterialWidgetFactory {
    @Nullable
    public static AbstractProjectMaterialWidget<?> create(AbstractProjectMaterial material, int x, int y, Set<Block> surroundings) {
        if (material instanceof ItemProjectMaterial itemMaterial) {
            return new ItemProjectMaterialWidget(itemMaterial, x, y, surroundings);
        } else if (material instanceof ItemTagProjectMaterial itemTagMaterial) {
            return new ItemTagProjectMaterialWidget(itemTagMaterial, x, y, surroundings);
        } else if (material instanceof ObservationProjectMaterial observationMaterial) {
            return new ObservationProjectMaterialWidget(observationMaterial, x, y, surroundings);
        } else if (material instanceof ExperienceProjectMaterial expMaterial) {
            return new ExperienceProjectMaterialWidget(expMaterial, x, y, surroundings);
        } else {
            return null;
        }
    }
}
