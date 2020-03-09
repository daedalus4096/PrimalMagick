package com.verdantartifice.primalmagic.common.theorycrafting.projects;

import com.verdantartifice.primalmagic.common.theorycrafting.AbstractProject;
import com.verdantartifice.primalmagic.common.theorycrafting.AbstractProjectMaterial;
import com.verdantartifice.primalmagic.common.theorycrafting.ItemProjectMaterial;
import com.verdantartifice.primalmagic.common.util.WeightedRandomBag;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

/**
 * Definition of a research project option.
 * 
 * @author Daedalus4096
 */
public class ExpeditionProject extends AbstractProject {
    public static final String TYPE = "expedition";
    
    protected static final WeightedRandomBag<AbstractProjectMaterial> OPTIONS = new WeightedRandomBag<>();
    
    static {
        OPTIONS.add(new ItemProjectMaterial(Items.IRON_SWORD, false), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.BOW, false), 1);
        OPTIONS.add(new ItemProjectMaterial(new ItemStack(Items.ARROW, 4), true), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.IRON_CHESTPLATE, false), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.MAP, true), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.COMPASS, false), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.CLOCK, false), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.CARTOGRAPHY_TABLE, false), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.CAMPFIRE, true), 1);
        OPTIONS.add(new ItemProjectMaterial(new ItemStack(Items.TORCH, 4), true), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.BREAD, true), 1);
    }

    @Override
    protected String getProjectType() {
        return TYPE;
    }

    @Override
    protected WeightedRandomBag<AbstractProjectMaterial> getMaterialOptions() {
        return OPTIONS;
    }
}
