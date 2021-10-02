package com.verdantartifice.primalmagic.common.theorycrafting;

import java.util.Set;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;

/**
 * Definition of a project material that requires experience levels, which may or may not be consumed as part
 * of the research project.
 * 
 * @author Daedalus4096
 */
public class ExperienceProjectMaterial extends AbstractProjectMaterial {
    public static final String TYPE = "experience";
    public static final IProjectMaterialSerializer<ExperienceProjectMaterial> SERIALIZER = new ExperienceProjectMaterial.Serializer();

    protected int levels;
    protected boolean consumed;
    
    public ExperienceProjectMaterial() {
        this(0, true);
    }
    
    public ExperienceProjectMaterial(int levels) {
        this(levels, true);
    }
    
    public ExperienceProjectMaterial(int levels, boolean consumed) {
        super();
        this.levels = levels;
        this.consumed = consumed;
    }
    
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = super.serializeNBT();
        tag.putInt("Levels", this.levels);
        tag.putBoolean("Consumed", this.consumed);
        return tag;
    }
    
    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        this.levels = nbt.getInt("Levels");
        this.consumed = nbt.getBoolean("Consumed");
    }

    @Override
    protected String getMaterialType() {
        return TYPE;
    }

    @Override
    public boolean isSatisfied(Player player, Set<Block> surroundings) {
        return player.experienceLevel >= this.levels;
    }

    @Override
    public boolean consume(Player player) {
        player.giveExperienceLevels(-1 * this.levels);
        return true;
    }
    
    public int getLevels() {
        return this.levels;
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
        ExperienceProjectMaterial retVal = new ExperienceProjectMaterial();
        retVal.levels = this.levels;
        retVal.consumed = this.consumed;
        retVal.selected = this.selected;
        retVal.weight = this.weight;
        if (this.requiredResearch != null) {
            retVal.requiredResearch = this.requiredResearch.copy();
        }
        return retVal;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (consumed ? 1231 : 1237);
        result = prime * result + levels;
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
        ExperienceProjectMaterial other = (ExperienceProjectMaterial) obj;
        if (consumed != other.consumed)
            return false;
        if (levels != other.levels)
            return false;
        return true;
    }

    public static class Serializer implements IProjectMaterialSerializer<ExperienceProjectMaterial> {
        @Override
        public ExperienceProjectMaterial read(ResourceLocation projectId, JsonObject json) {
            int levels = json.getAsJsonPrimitive("levels").getAsInt();
            if (levels <= 0) {
                throw new JsonSyntaxException("Invalid experience levels in material JSON for project " + projectId.toString());
            }
            
            boolean consumed = json.getAsJsonPrimitive("consumed").getAsBoolean();
            
            ExperienceProjectMaterial retVal = new ExperienceProjectMaterial(levels, consumed);
            
            retVal.setWeight(json.getAsJsonPrimitive("weight").getAsDouble());
            if (json.has("required_research")) {
                retVal.setRequiredResearch(CompoundResearchKey.parse(json.getAsJsonPrimitive("required_research").getAsString()));
            }
            
            return retVal;
        }

        @Override
        public ExperienceProjectMaterial fromNetwork(FriendlyByteBuf buf) {
            ExperienceProjectMaterial material = new ExperienceProjectMaterial(buf.readVarInt(), buf.readBoolean());
            material.setWeight(buf.readDouble());
            CompoundResearchKey research = CompoundResearchKey.parse(buf.readUtf());
            if (research != null) {
                material.setRequiredResearch(research);
            }
            return material;
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ExperienceProjectMaterial material) {
            buf.writeVarInt(material.levels);
            buf.writeBoolean(material.consumed);
            buf.writeDouble(material.weight);
            buf.writeUtf(material.requiredResearch == null ? "" : material.requiredResearch.toString());
        }
    }
}
