package com.verdantartifice.primalmagic.common.theorycrafting;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

/**
 * Definition of a project material that requires experience levels, which are always consumed as part
 * of the research project.
 * 
 * @author Daedalus4096
 */
public class ExperienceProjectMaterial extends AbstractProjectMaterial {
    public static final String TYPE = "experience";
    
    protected int levels;
    
    public ExperienceProjectMaterial() {
        super();
        this.levels = 0;
    }
    
    public ExperienceProjectMaterial(int levels) {
        super();
        this.levels = levels;
    }
    
    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = super.serializeNBT();
        tag.putInt("Levels", this.levels);
        return tag;
    }
    
    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        this.levels = nbt.getInt("Levels");
    }

    @Override
    protected String getMaterialType() {
        return TYPE;
    }

    @Override
    public boolean isSatisfied(PlayerEntity player) {
        return player.experienceLevel >= this.levels;
    }

    @Override
    public boolean consume(PlayerEntity player) {
        player.addExperienceLevel(-1 * this.levels);
        return true;
    }
    
    public int getLevels() {
        return this.levels;
    }

    @Override
    public boolean isConsumed() {
        return true;
    }

    @Override
    public AbstractProjectMaterial copy() {
        ExperienceProjectMaterial retVal = new ExperienceProjectMaterial();
        retVal.levels = this.levels;
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
            
            ExperienceProjectMaterial retVal = new ExperienceProjectMaterial(levels);
            
            retVal.setWeight(json.getAsJsonPrimitive("weight").getAsDouble());
            if (json.has("required_research")) {
                retVal.setRequiredResearch(CompoundResearchKey.parse(json.getAsJsonPrimitive("required_research").getAsString()));
            }
            
            return retVal;
        }
    }
}
