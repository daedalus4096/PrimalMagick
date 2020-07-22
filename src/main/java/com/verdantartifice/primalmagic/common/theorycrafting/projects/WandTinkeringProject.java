package com.verdantartifice.primalmagic.common.theorycrafting.projects;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.theorycrafting.AbstractProject;
import com.verdantartifice.primalmagic.common.theorycrafting.AbstractProjectMaterial;
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
public class WandTinkeringProject extends AbstractProject {
    public static final String TYPE = "wand_tinkering";
    
    protected static final WeightedRandomBag<AbstractProjectMaterial> OPTIONS = Util.make(new WeightedRandomBag<>(), bag -> {
        bag.add(new ItemProjectMaterial(ItemsPM.WAND_ASSEMBLY_TABLE.get(), false), 3);
        bag.add(new ItemProjectMaterial(ItemsPM.HEARTWOOD.get(), true), 1);
        bag.add(new ItemTagProjectMaterial(new ResourceLocation("forge", "obsidian"), true), 1);
        bag.add(new ItemTagProjectMaterial(new ResourceLocation(PrimalMagic.MODID, "coral_blocks"), true), 1);
        bag.add(new ItemProjectMaterial(Items.BAMBOO, true), 1);
        bag.add(new ItemTagProjectMaterial(new ResourceLocation(PrimalMagic.MODID, "sunwood_logs"), true), 1);
        bag.add(new ItemTagProjectMaterial(new ResourceLocation(PrimalMagic.MODID, "moonwood_logs"), true), 1);
        bag.add(new ItemTagProjectMaterial(new ResourceLocation("forge", "ingots/iron"), true), 1);
        bag.add(new ItemTagProjectMaterial(new ResourceLocation("forge", "ingots/gold"), true), 1);
        bag.add(new ItemTagProjectMaterial(new ResourceLocation("forge", "gems/diamond"), true), 1);
        bag.add(new ItemTagProjectMaterial(new ResourceLocation(PrimalMagic.MODID, "essences/terrestrial_dusts"), true), 1);
        bag.add(new ObservationProjectMaterial(), 5);
    });
    protected static final SimpleResearchKey RESEARCH = SimpleResearchKey.parse("BASIC_SORCERY");
    
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
