package com.verdantartifice.primalmagic.client.gui.widgets.research_table;

import java.util.Set;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.theorycrafting.AbstractProjectMaterial;
import com.verdantartifice.primalmagic.common.theorycrafting.ExperienceProjectMaterial;
import com.verdantartifice.primalmagic.common.theorycrafting.ItemProjectMaterial;
import com.verdantartifice.primalmagic.common.theorycrafting.ItemTagProjectMaterial;
import com.verdantartifice.primalmagic.common.theorycrafting.ObservationProjectMaterial;

import net.minecraft.world.level.block.Block;

/**
 * Factory for creating widgets to display research project materials.
 * 
 * @author Daedalus4096
 */
public class ProjectMaterialWidgetFactory {
    @Nullable
    public static AbstractProjectMaterialWidget create(AbstractProjectMaterial material, int x, int y, Set<Block> surroundings) {
        if (material instanceof ItemProjectMaterial) {
            return new ItemProjectMaterialWidget((ItemProjectMaterial)material, x, y, surroundings);
        } else if (material instanceof ItemTagProjectMaterial) {
            return new ItemTagProjectMaterialWidget((ItemTagProjectMaterial)material, x, y, surroundings);
        } else if (material instanceof ObservationProjectMaterial) {
            return new ObservationProjectMaterialWidget((ObservationProjectMaterial)material, x, y, surroundings);
        } else if (material instanceof ExperienceProjectMaterial) {
            return new ExperienceProjectMaterialWidget((ExperienceProjectMaterial)material, x, y, surroundings);
        } else {
            return null;
        }
    }
}
