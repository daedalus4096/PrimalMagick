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
public class TradeProject extends AbstractProject {
    public static final String TYPE = "trade";
    
    protected static final WeightedRandomBag<AbstractProjectMaterial> OPTIONS = new WeightedRandomBag<>();
    
    static {
        OPTIONS.add(new ItemProjectMaterial(Items.EMERALD, true), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.DIAMOND, true), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.GOLD_INGOT, true), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.IRON_BLOCK, true), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.PUMPKIN, true), 1);
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
