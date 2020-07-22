package com.verdantartifice.primalmagic.common.theorycrafting.projects;

import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.theorycrafting.AbstractProject;
import com.verdantartifice.primalmagic.common.theorycrafting.AbstractProjectMaterial;
import com.verdantartifice.primalmagic.common.theorycrafting.ExperienceProjectMaterial;
import com.verdantartifice.primalmagic.common.theorycrafting.ItemProjectMaterial;
import com.verdantartifice.primalmagic.common.theorycrafting.ItemTagProjectMaterial;
import com.verdantartifice.primalmagic.common.theorycrafting.ObservationProjectMaterial;
import com.verdantartifice.primalmagic.common.util.WeightedRandomBag;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;

/**
 * Definition of a research project option.
 * 
 * @author Daedalus4096
 */
public class EnchantingStudiesProject extends AbstractProject {
    public static final String TYPE = "enchanting_studies";
    
    protected static final WeightedRandomBag<AbstractProjectMaterial> OPTIONS = Util.make(new WeightedRandomBag<>(), bag -> {
        bag.add(new ItemProjectMaterial(Items.ENCHANTING_TABLE, false), 5);
        bag.add(new ItemTagProjectMaterial(new ResourceLocation("forge", "gems/lapis"), true), 5);
        bag.add(new ExperienceProjectMaterial(3), 5);
        bag.add(new ItemProjectMaterial(Items.BOOK, false), 1);
        bag.add(new ItemProjectMaterial(Items.GOLDEN_SWORD, false), 1);
        bag.add(new ItemProjectMaterial(Items.GOLDEN_PICKAXE, false), 1);
        bag.add(new ItemProjectMaterial(Items.GOLDEN_SHOVEL, false), 1);
        bag.add(new ItemProjectMaterial(Items.GOLDEN_HOE, false), 1);
        bag.add(new ItemProjectMaterial(Items.GOLDEN_AXE, false), 1);
        bag.add(new ItemProjectMaterial(Items.GOLDEN_CHESTPLATE, false), 1);
        bag.add(new ItemProjectMaterial(Items.GOLDEN_LEGGINGS, false), 1);
        bag.add(new ItemProjectMaterial(Items.GOLDEN_BOOTS, false), 1);
        bag.add(new ItemProjectMaterial(Items.GOLDEN_HELMET, false), 1);
        bag.add(new ObservationProjectMaterial(), 5);
    });
    protected static final SimpleResearchKey RESEARCH = SimpleResearchKey.parse("BASIC_MANAWEAVING");
    
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
