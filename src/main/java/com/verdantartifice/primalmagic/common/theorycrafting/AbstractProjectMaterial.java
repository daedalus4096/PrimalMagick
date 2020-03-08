package com.verdantartifice.primalmagic.common.theorycrafting;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Base class for a material required by a theorycrafting research project.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractProjectMaterial implements INBTSerializable<CompoundNBT> {
    protected boolean selected;
    
    protected AbstractProjectMaterial() {
        this.selected = false;
    }
    
    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT retVal = new CompoundNBT();
        retVal.putString("MaterialType", this.getMaterialType());
        retVal.putBoolean("Selected", this.isSelected());
        return retVal;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.selected = nbt.getBoolean("Selected");
    }
    
    /**
     * Get the type name for this project material.
     * 
     * @return the type name for this project material
     */
    protected abstract String getMaterialType();
    
    /**
     * Collect the requirements of this material into the given satisfaction criteria
     * 
     * @param criteria the satisfaction criteria to receive this material's requirements
     */
    public abstract void gatherRequirements(AbstractProject.SatisfactionCritera criteria);
    
    /**
     * Consume this project material's requirements from the given player.
     * 
     * @param player the player doing the research project
     * @return true if the consumption succeeded, false otherwise
     */
    public abstract boolean consume(PlayerEntity player);
    
    /**
     * Determine whether this material should be consumed upon project completion.
     * 
     * @return whether this material should be consumed upon project completion
     */
    public abstract boolean isConsumed();
    
    public boolean isSelected() {
        return this.selected;
    }
    
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    public abstract AbstractProjectMaterial copy();
    
    public abstract int hashCode();
    
    public abstract boolean equals(Object obj);
}
