package com.verdantartifice.primalmagick.common.theorycrafting;

import java.util.Set;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.research.KnowledgeType;
import com.verdantartifice.primalmagick.common.research.ResearchManager;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;

/**
 * Definition of a project material that requires one or more observations, which may or may not be consumed as part
 * of the research project.
 * 
 * @author Daedalus4096
 */
public class ObservationProjectMaterial extends AbstractProjectMaterial {
    public static final String TYPE = "observation";
    public static final IProjectMaterialSerializer<ObservationProjectMaterial> SERIALIZER = new ObservationProjectMaterial.Serializer();

    protected int count;
    protected boolean consumed;

    public ObservationProjectMaterial() {
        this(0, true);
    }
    
    public ObservationProjectMaterial(int count) {
        this(count, true);
    }
    
    public ObservationProjectMaterial(int count, boolean consumed) {
        super();
        this.count = count;
        this.consumed = consumed;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = super.serializeNBT();
        tag.putInt("Count", this.count);
        tag.putBoolean("Consumed", this.consumed);
        return tag;
    }
    
    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        this.count = nbt.getInt("Count");
        this.consumed = nbt.getBoolean("Consumed");
    }

    @Override
    protected String getMaterialType() {
        return TYPE;
    }

    @Override
    public boolean isSatisfied(Player player, Set<Block> surroundings) {
        IPlayerKnowledge knowledge = PrimalMagickCapabilities.getKnowledge(player).orElse(null);
        return (knowledge != null && knowledge.getKnowledge(KnowledgeType.OBSERVATION) >= this.count);
    }

    @Override
    public boolean consume(Player player) {
        // Deduct observation level(s) from the player's knowledge pool
        return ResearchManager.addKnowledge(player, KnowledgeType.OBSERVATION, -1 * this.count * KnowledgeType.OBSERVATION.getProgression());
    }
    
    @Override
    public boolean isConsumed() {
        return this.consumed;
    }
    
    @Override
    public void toNetwork(FriendlyByteBuf buf) {
        SERIALIZER.toNetwork(buf, this);
    }

    @Override
    public AbstractProjectMaterial copy() {
        ObservationProjectMaterial material = new ObservationProjectMaterial();
        material.count = this.count;
        material.consumed = this.consumed;
        material.selected = this.selected;
        material.weight = this.weight;
        material.bonusReward = this.bonusReward;
        if (this.requiredResearch != null) {
            material.requiredResearch = this.requiredResearch.copy();
        }
        return material;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (consumed ? 1231 : 1237);
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
        if (consumed != other.consumed)
            return false;
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
            
            boolean consumed = json.getAsJsonPrimitive("consumed").getAsBoolean();
            
            ObservationProjectMaterial retVal = new ObservationProjectMaterial(count, consumed);
            
            retVal.setWeight(json.getAsJsonPrimitive("weight").getAsDouble());
            if (json.has("bonus_reward")) {
                retVal.setBonusReward(json.getAsJsonPrimitive("bonus_reward").getAsDouble());
            }
            if (json.has("required_research")) {
                retVal.setRequiredResearch(CompoundResearchKey.parse(json.getAsJsonPrimitive("required_research").getAsString()));
            }
            
            return retVal;
        }

        @Override
        public ObservationProjectMaterial fromNetwork(FriendlyByteBuf buf) {
            ObservationProjectMaterial material = new ObservationProjectMaterial(buf.readVarInt(), buf.readBoolean());
            material.setWeight(buf.readDouble());
            material.setBonusReward(buf.readDouble());
            CompoundResearchKey research = CompoundResearchKey.parse(buf.readUtf());
            if (research != null) {
                material.setRequiredResearch(research);
            }
            return material;
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ObservationProjectMaterial material) {
            buf.writeVarInt(material.count);
            buf.writeBoolean(material.consumed);
            buf.writeDouble(material.weight);
            buf.writeDouble(material.bonusReward);
            buf.writeUtf(material.requiredResearch == null ? "" : material.requiredResearch.toString());
        }
    }
}
