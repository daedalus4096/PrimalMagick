package com.verdantartifice.primalmagic.common.theorycrafting;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

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
        return this.consumed;
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
    }
}
