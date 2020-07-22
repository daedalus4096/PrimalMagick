package com.verdantartifice.primalmagic.common.theorycrafting.projects;

import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.theorycrafting.AbstractProject;
import com.verdantartifice.primalmagic.common.theorycrafting.AbstractProjectMaterial;
import com.verdantartifice.primalmagic.common.theorycrafting.ItemProjectMaterial;
import com.verdantartifice.primalmagic.common.theorycrafting.ObservationProjectMaterial;
import com.verdantartifice.primalmagic.common.util.WeightedRandomBag;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Util;

/**
 * Definition of a research project option.
 * 
 * @author Daedalus4096
 */
public class RedstoneTinkeringProject extends AbstractProject {
    public static final String TYPE = "redstone_tinkering";
    
    protected static final WeightedRandomBag<AbstractProjectMaterial> OPTIONS = Util.make(new WeightedRandomBag<>(), bag -> {
        bag.add(new ItemProjectMaterial(Items.DETECTOR_RAIL, false), 1);
        bag.add(new ItemProjectMaterial(Items.ACTIVATOR_RAIL, false), 1);
        bag.add(new ItemProjectMaterial(Items.DISPENSER, false), 1);
        bag.add(new ItemProjectMaterial(Items.DROPPER, false), 1);
        bag.add(new ItemProjectMaterial(Items.DAYLIGHT_DETECTOR, false), 1);
        bag.add(new ItemProjectMaterial(Items.PISTON, false), 1);
        bag.add(new ItemProjectMaterial(Items.HOPPER, false), 1);
        bag.add(new ItemProjectMaterial(Items.REDSTONE_LAMP, false), 1);
        bag.add(new ItemProjectMaterial(Items.STICKY_PISTON, false), 1);
        bag.add(new ItemProjectMaterial(Items.COMPARATOR, false), 1);
        bag.add(new ItemProjectMaterial(Items.OBSERVER, false), 1);
        bag.add(new ObservationProjectMaterial(), 5);
    });
    protected static final SimpleResearchKey RESEARCH = SimpleResearchKey.parse("BASIC_MAGITECH");
    
    @Override
    protected String getProjectType() {
        return TYPE;
    }

    @Override
    protected WeightedRandomBag<AbstractProjectMaterial> getMaterialOptions(PlayerEntity player) {
        return OPTIONS;
    }
    
    @Override
    public SimpleResearchKey getRequiredResearch() {
        return RESEARCH;
    }
}
