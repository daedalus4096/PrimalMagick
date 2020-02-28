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
     * Determine whether the given player can currently satisfy the requirements of this project material.
     * 
     * @param player the player doing the research project
     * @return true if the player can currently satisfy this material's requirements, false otherwise
     */
    public abstract boolean isSatisfied(PlayerEntity player);
    
    /**
     * Consume this project material's requirements from the given player.
     * 
     * @param player the player doing the research project
     * @return true if the consumption succeeded, false otherwise
     */
    public abstract boolean consume(PlayerEntity player);
    
    public boolean isConsumed() {
        return true;
    }
    
    public boolean isSelected() {
        return this.selected;
    }
    
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
