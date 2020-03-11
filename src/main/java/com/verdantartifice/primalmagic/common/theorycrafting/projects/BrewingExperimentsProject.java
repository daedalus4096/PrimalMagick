package com.verdantartifice.primalmagic.common.theorycrafting.projects;

import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.theorycrafting.AbstractProject;
import com.verdantartifice.primalmagic.common.theorycrafting.AbstractProjectMaterial;
import com.verdantartifice.primalmagic.common.theorycrafting.ItemProjectMaterial;
import com.verdantartifice.primalmagic.common.theorycrafting.ItemTagProjectMaterial;
import com.verdantartifice.primalmagic.common.theorycrafting.ObservationProjectMaterial;
import com.verdantartifice.primalmagic.common.util.WeightedRandomBag;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

/**
 * Definition of a research project option.
 * 
 * @author Daedalus4096
 */
public class BrewingExperimentsProject extends AbstractProject {
    public static final String TYPE = "brewing_experiments";
    
    protected static final WeightedRandomBag<AbstractProjectMaterial> OPTIONS = new WeightedRandomBag<>();
    protected static final SimpleResearchKey RESEARCH = Source.INFERNAL.getDiscoverKey();
    
    static {
        OPTIONS.add(new ItemProjectMaterial(Items.BREWING_STAND, false), 3);
        OPTIONS.add(new ItemTagProjectMaterial(new ResourceLocation("forge", "crops/nether_wart"), true), 3);
        OPTIONS.add(new ItemProjectMaterial(Items.FERMENTED_SPIDER_EYE, true), 1);
        OPTIONS.add(new ItemTagProjectMaterial(new ResourceLocation("forge", "dusts/glowstone"), true), 1);
        OPTIONS.add(new ItemTagProjectMaterial(new ResourceLocation("forge", "dusts/redstone"), true), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.SUGAR, true), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.RABBIT_FOOT, true), 0.5D);
        OPTIONS.add(new ItemProjectMaterial(Items.BLAZE_POWDER, true), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.GLISTERING_MELON_SLICE, true), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.SPIDER_EYE, true), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.GHAST_TEAR, true), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.MAGMA_CREAM, true), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.PUFFERFISH, true), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.GOLDEN_CARROT, true), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.TURTLE_HELMET, true), 0.5D);
        OPTIONS.add(new ItemProjectMaterial(Items.PHANTOM_MEMBRANE, true), 0.5D);
        OPTIONS.add(new ObservationProjectMaterial(), 3);
    }
    
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
