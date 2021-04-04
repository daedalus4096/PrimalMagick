package com.verdantartifice.primalmagic.common.theorycrafting;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.ResearchManager;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

/**
 * Definition of a project material that requires one or more observations, which are always consumed as part
 * of the research project.
 * 
 * @author Daedalus4096
 */
public class ObservationProjectMaterial extends AbstractProjectMaterial {
    public static final String TYPE = "observation";
    
    protected int count;

    public ObservationProjectMaterial() {
        super();
        this.count = 0;
    }
    
    public ObservationProjectMaterial(int count) {
        super();
        this.count = count;
    }

    @Override
    protected String getMaterialType() {
        return TYPE;
    }

    @Override
    public boolean isSatisfied(PlayerEntity player) {
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(player);
        return (knowledge != null && knowledge.getKnowledge(IPlayerKnowledge.KnowledgeType.OBSERVATION) >= this.count);
    }

    @Override
    public boolean consume(PlayerEntity player) {
        // Deduct observation level(s) from the player's knowledge pool
        return ResearchManager.addKnowledge(player, IPlayerKnowledge.KnowledgeType.OBSERVATION, -1 * this.count * IPlayerKnowledge.KnowledgeType.OBSERVATION.getProgression());
    }
    
    @Override
    public boolean isConsumed() {
        return true;
    }
    
    @Override
    public AbstractProjectMaterial copy() {
        ObservationProjectMaterial material = new ObservationProjectMaterial();
        material.count = this.count;
        material.selected = this.selected;
        material.weight = this.weight;
        if (this.requiredResearch != null) {
            material.requiredResearch = this.requiredResearch.copy();
        }
        return material;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + count;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        ObservationProjectMaterial other = (ObservationProjectMaterial) obj;
        if (count != other.count)
            return false;
        return true;
    }
    
    public static class Serializer implements IProjectMaterialSerializer<ObservationProjectMaterial> {
        @Override
        public ObservationProjectMaterial read(ResourceLocation projectId, JsonObject json) {
            int count = json.getAsJsonPrimitive("count").getAsInt();
            if (count <= 0) {
                throw new JsonSyntaxException("Invalid observation count in material JSON for project " + projectId.toString());
            }
            
            ObservationProjectMaterial retVal = new ObservationProjectMaterial(count);
            
            retVal.setWeight(json.getAsJsonPrimitive("weight").getAsDouble());
            if (json.has("required_research")) {
                retVal.setRequiredResearch(CompoundResearchKey.parse(json.getAsJsonPrimitive("required_research").getAsString()));
            }
            
            return retVal;
        }
    }
}
