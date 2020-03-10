package com.verdantartifice.primalmagic.client.gui.widgets.research_table;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.theorycrafting.AbstractProjectMaterial;
import com.verdantartifice.primalmagic.common.theorycrafting.ItemProjectMaterial;
import com.verdantartifice.primalmagic.common.theorycrafting.ObservationProjectMaterial;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Factory for creating widgets to display research project materials.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class ProjectMaterialWidgetFactory {
    @Nullable
    public static AbstractProjectMaterialWidget create(AbstractProjectMaterial material, int x, int y) {
        if (material instanceof ItemProjectMaterial) {
            return new ItemProjectMaterialWidget((ItemProjectMaterial)material, x, y);
        } else if (material instanceof ObservationProjectMaterial) {
            return new ObservationProjectMaterialWidget((ObservationProjectMaterial)material, x, y);
        } else {
            return null;
        }
    }
}
