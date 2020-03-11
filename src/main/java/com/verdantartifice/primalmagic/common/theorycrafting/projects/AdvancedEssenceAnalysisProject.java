package com.verdantartifice.primalmagic.common.theorycrafting.projects;

import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
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
public class AdvancedEssenceAnalysisProject extends AbstractProject {
    public static final String TYPE = "advanced_essence_analysis";
    
    protected static final SimpleResearchKey RESEARCH = SimpleResearchKey.parse("CRYSTAL_SYNTHESIS");
    
    @Override
    protected String getProjectType() {
        return TYPE;
    }

    @Override
    protected WeightedRandomBag<AbstractProjectMaterial> getMaterialOptions(PlayerEntity player) {
        WeightedRandomBag<AbstractProjectMaterial> retVal = new WeightedRandomBag<>();
        
        // Add basic shard and crystal essences, and observation
        retVal.add(new ItemProjectMaterial(ItemsPM.ESSENCE_SHARD_EARTH.get(), true), 3);
        retVal.add(new ItemProjectMaterial(ItemsPM.ESSENCE_SHARD_SEA.get(), true), 3);
        retVal.add(new ItemProjectMaterial(ItemsPM.ESSENCE_SHARD_SKY.get(), true), 3);
        retVal.add(new ItemProjectMaterial(ItemsPM.ESSENCE_SHARD_SUN.get(), true), 3);
        retVal.add(new ItemProjectMaterial(ItemsPM.ESSENCE_SHARD_MOON.get(), true), 3);
        retVal.add(new ItemProjectMaterial(ItemsPM.ESSENCE_CRYSTAL_EARTH.get(), true), 1);
        retVal.add(new ItemProjectMaterial(ItemsPM.ESSENCE_CRYSTAL_SEA.get(), true), 1);
        retVal.add(new ItemProjectMaterial(ItemsPM.ESSENCE_CRYSTAL_SKY.get(), true), 1);
        retVal.add(new ItemProjectMaterial(ItemsPM.ESSENCE_CRYSTAL_SUN.get(), true), 1);
        retVal.add(new ItemProjectMaterial(ItemsPM.ESSENCE_CRYSTAL_MOON.get(), true), 1);
        retVal.add(new ObservationProjectMaterial(), 3);
        
        // Add advanced shard and crystal essences, if unlocked
        if (ResearchManager.isResearchComplete(player, Source.BLOOD.getDiscoverKey())) {
            retVal.add(new ItemProjectMaterial(ItemsPM.ESSENCE_SHARD_BLOOD.get(), true), 3);
            retVal.add(new ItemProjectMaterial(ItemsPM.ESSENCE_CRYSTAL_BLOOD.get(), true), 1);
        }
        if (ResearchManager.isResearchComplete(player, Source.INFERNAL.getDiscoverKey())) {
            retVal.add(new ItemProjectMaterial(ItemsPM.ESSENCE_SHARD_INFERNAL.get(), true), 3);
            retVal.add(new ItemProjectMaterial(ItemsPM.ESSENCE_CRYSTAL_INFERNAL.get(), true), 1);
        }
        if (ResearchManager.isResearchComplete(player, Source.VOID.getDiscoverKey())) {
            retVal.add(new ItemProjectMaterial(ItemsPM.ESSENCE_SHARD_VOID.get(), true), 3);
            retVal.add(new ItemProjectMaterial(ItemsPM.ESSENCE_CRYSTAL_VOID.get(), true), 1);
        }
        if (ResearchManager.isResearchComplete(player, Source.HALLOWED.getDiscoverKey())) {
            retVal.add(new ItemProjectMaterial(ItemsPM.ESSENCE_SHARD_HALLOWED.get(), true), 3);
            retVal.add(new ItemProjectMaterial(ItemsPM.ESSENCE_CRYSTAL_HALLOWED.get(), true), 1);
        }
        
        return retVal;
    }
    
    @Override
    public SimpleResearchKey getRequiredResearch() {
        return RESEARCH;
    }
    
    @Override
    public int getTheoryPointReward() {
        // Return double the normal reward
        return (IPlayerKnowledge.KnowledgeType.THEORY.getProgression() / 2);
    }
}
