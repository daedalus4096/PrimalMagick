package com.verdantartifice.primalmagic.common.theorycrafting.projects;

import com.verdantartifice.primalmagic.common.theorycrafting.AbstractProject;
import com.verdantartifice.primalmagic.common.theorycrafting.AbstractProjectMaterial;
import com.verdantartifice.primalmagic.common.theorycrafting.ItemProjectMaterial;
import com.verdantartifice.primalmagic.common.util.WeightedRandomBag;

import net.minecraft.entity.player.PlayerEntity;
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
        OPTIONS.add(new ItemProjectMaterial(Items.EMERALD, true), 10);
        OPTIONS.add(new ItemProjectMaterial(Items.DIAMOND, true), 2);
        OPTIONS.add(new ItemProjectMaterial(Items.COAL, true), 2);
        OPTIONS.add(new ItemProjectMaterial(Items.COMPASS, true), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.GOLD_INGOT, true), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.PUMPKIN, true), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.STRING, true), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.LEATHER, true), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.BOOK, true), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.QUARTZ, true), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.WHITE_WOOL, true), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.IRON_INGOT, true), 1);
    }

    @Override
    protected String getProjectType() {
        return TYPE;
    }

    @Override
    protected WeightedRandomBag<AbstractProjectMaterial> getMaterialOptions(PlayerEntity player) {
        return OPTIONS;
    }
}
