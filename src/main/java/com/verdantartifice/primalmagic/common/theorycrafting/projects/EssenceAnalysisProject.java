package com.verdantartifice.primalmagic.common.theorycrafting.projects;

import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.research.ResearchManager;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.theorycrafting.AbstractProject;
import com.verdantartifice.primalmagic.common.theorycrafting.AbstractProjectMaterial;
import com.verdantartifice.primalmagic.common.theorycrafting.ItemProjectMaterial;
import com.verdantartifice.primalmagic.common.theorycrafting.ObservationProjectMaterial;
import com.verdantartifice.primalmagic.common.util.WeightedRandomBag;

import net.minecraft.entity.player.PlayerEntity;

/**
 * Definition of a research project option.
 * 
 * @author Daedalus4096
 */
public class EssenceAnalysisProject extends AbstractProject {
    public static final String TYPE = "essence_analysis";
    
    protected static final SimpleResearchKey RESEARCH = SimpleResearchKey.parse("BASIC_ALCHEMY");
    protected static final SimpleResearchKey SHARD_RESEARCH = SimpleResearchKey.parse("SHARD_SYNTHESIS");
    
    @Override
    protected String getProjectType() {
        return TYPE;
    }

    @Override
    protected WeightedRandomBag<AbstractProjectMaterial> getMaterialOptions(PlayerEntity player) {
        WeightedRandomBag<AbstractProjectMaterial> retVal = new WeightedRandomBag<>();
        
        // Add basic dust essences and observation
        retVal.add(new ItemProjectMaterial(ItemsPM.ESSENCE_DUST_EARTH.get(), true), 3);
        retVal.add(new ItemProjectMaterial(ItemsPM.ESSENCE_DUST_SEA.get(), true), 3);
        retVal.add(new ItemProjectMaterial(ItemsPM.ESSENCE_DUST_SKY.get(), true), 3);
        retVal.add(new ItemProjectMaterial(ItemsPM.ESSENCE_DUST_SUN.get(), true), 3);
        retVal.add(new ItemProjectMaterial(ItemsPM.ESSENCE_DUST_MOON.get(), true), 3);
        retVal.add(new ObservationProjectMaterial(), 3);
        
        boolean hasBlood = ResearchManager.isResearchComplete(player, Source.BLOOD.getDiscoverKey());
        boolean hasInfernal = ResearchManager.isResearchComplete(player, Source.INFERNAL.getDiscoverKey());
        boolean hasVoid = ResearchManager.isResearchComplete(player, Source.VOID.getDiscoverKey());
        boolean hasHallowed = ResearchManager.isResearchComplete(player, Source.HALLOWED.getDiscoverKey());

        // Add advanced dust essences, if unlocked
        if (hasBlood) {
            retVal.add(new ItemProjectMaterial(ItemsPM.ESSENCE_DUST_BLOOD.get(), true), 3);
        }
        if (hasInfernal) {
            retVal.add(new ItemProjectMaterial(ItemsPM.ESSENCE_DUST_INFERNAL.get(), true), 3);
        }
        if (hasVoid) {
            retVal.add(new ItemProjectMaterial(ItemsPM.ESSENCE_DUST_VOID.get(), true), 3);
        }
        if (hasHallowed) {
            retVal.add(new ItemProjectMaterial(ItemsPM.ESSENCE_DUST_HALLOWED.get(), true), 3);
        }
        
        // Add shards, if unlocked
        if (ResearchManager.isResearchComplete(player, SHARD_RESEARCH)) {
            retVal.add(new ItemProjectMaterial(ItemsPM.ESSENCE_SHARD_EARTH.get(), true), 1);
            retVal.add(new ItemProjectMaterial(ItemsPM.ESSENCE_SHARD_SEA.get(), true), 1);
            retVal.add(new ItemProjectMaterial(ItemsPM.ESSENCE_SHARD_SKY.get(), true), 1);
            retVal.add(new ItemProjectMaterial(ItemsPM.ESSENCE_SHARD_SUN.get(), true), 1);
            retVal.add(new ItemProjectMaterial(ItemsPM.ESSENCE_SHARD_MOON.get(), true), 1);

            if (hasBlood) {
                retVal.add(new ItemProjectMaterial(ItemsPM.ESSENCE_SHARD_BLOOD.get(), true), 1);
            }
            if (hasInfernal) {
                retVal.add(new ItemProjectMaterial(ItemsPM.ESSENCE_SHARD_INFERNAL.get(), true), 1);
            }
            if (hasVoid) {
                retVal.add(new ItemProjectMaterial(ItemsPM.ESSENCE_SHARD_VOID.get(), true), 1);
            }
            if (hasHallowed) {
                retVal.add(new ItemProjectMaterial(ItemsPM.ESSENCE_SHARD_HALLOWED.get(), true), 1);
            }
        }

        return retVal;
    }
    
    @Override
    public SimpleResearchKey getRequiredResearch() {
        return RESEARCH;
    }
}
