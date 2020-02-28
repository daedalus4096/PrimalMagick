package com.verdantartifice.primalmagic.common.theorycrafting;

import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.research.ResearchManager;

import net.minecraft.entity.player.PlayerEntity;

/**
 * Definition of a project material that requires an observation, which is always consumed as part
 * of the research project.
 * 
 * @author Daedalus4096
 */
public class ObservationProjectMaterial extends AbstractProjectMaterial {
    public static final String TYPE = "observation";

    public ObservationProjectMaterial() {
        super();
    }

    @Override
    protected String getMaterialType() {
        return TYPE;
    }

    @Override
    public boolean isSatisfied(PlayerEntity player) {
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(player);
        if (knowledge == null) {
            return false;
        } else {
            return knowledge.getKnowledge(IPlayerKnowledge.KnowledgeType.OBSERVATION) >= 1;
        }
    }

    @Override
    public boolean consume(PlayerEntity player) {
        return ResearchManager.addKnowledge(player, IPlayerKnowledge.KnowledgeType.OBSERVATION, -1 * IPlayerKnowledge.KnowledgeType.OBSERVATION.getProgression());
    }
}
