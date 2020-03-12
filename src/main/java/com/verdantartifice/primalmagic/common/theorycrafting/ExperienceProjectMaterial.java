package com.verdantartifice.primalmagic.common.theorycrafting;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

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
        return retVal;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.levels;
        result = prime * result + (this.selected ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ExperienceProjectMaterial other = (ExperienceProjectMaterial) obj;
        if (this.levels != other.levels) {
            return false;
        }
        if (this.selected != other.selected) {
            return false;
        }
        return true;
    }
}
