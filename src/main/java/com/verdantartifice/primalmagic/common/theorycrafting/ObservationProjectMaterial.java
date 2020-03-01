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
        // The material is satisfied if the player has at least one observation known
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(player);
        if (knowledge == null) {
            return false;
        } else {
            return knowledge.getKnowledge(IPlayerKnowledge.KnowledgeType.OBSERVATION) >= 1;
        }
    }

    @Override
    public boolean consume(PlayerEntity player) {
        // Deduct one observation level from the player's knowledge pool
        return ResearchManager.addKnowledge(player, IPlayerKnowledge.KnowledgeType.OBSERVATION, -1 * IPlayerKnowledge.KnowledgeType.OBSERVATION.getProgression());
    }
    
    @Override
    public boolean isConsumed() {
        return true;
    }
    
    @Override
    public AbstractProjectMaterial copy() {
        ObservationProjectMaterial material = new ObservationProjectMaterial();
        material.selected = this.selected;
        return material;
    }
}
