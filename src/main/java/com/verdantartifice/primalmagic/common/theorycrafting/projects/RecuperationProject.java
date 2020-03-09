package com.verdantartifice.primalmagic.common.theorycrafting.projects;

import com.verdantartifice.primalmagic.common.theorycrafting.AbstractProject;
import com.verdantartifice.primalmagic.common.theorycrafting.AbstractProjectMaterial;
import com.verdantartifice.primalmagic.common.theorycrafting.ItemProjectMaterial;
import com.verdantartifice.primalmagic.common.util.WeightedRandomBag;

import net.minecraft.item.Items;

/**
 * Definition of a research project option.
 * 
 * @author Daedalus4096
 */
public class RecuperationProject extends AbstractProject {
    public static final String TYPE = "recuperation";
    
    protected static final WeightedRandomBag<AbstractProjectMaterial> OPTIONS = new WeightedRandomBag<>();
    
    static {
        OPTIONS.add(new ItemProjectMaterial(Items.RED_BED, false), 2);
        OPTIONS.add(new ItemProjectMaterial(Items.JUKEBOX, false), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.BOOK, false), 2);
        OPTIONS.add(new ItemProjectMaterial(Items.COOKED_BEEF, true), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.BAKED_POTATO, true), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.MILK_BUCKET, true), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.CAKE, true), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.ROSE_BUSH, true), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.TNT, true), 0.5D);
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
