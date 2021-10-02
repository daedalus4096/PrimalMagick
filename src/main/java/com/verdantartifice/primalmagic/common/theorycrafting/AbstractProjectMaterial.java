package com.verdantartifice.primalmagic.common.theorycrafting;

import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Base class for a material required by a theorycrafting research project.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractProjectMaterial implements INBTSerializable<CompoundTag> {
    protected boolean selected;
    protected double weight;
    protected CompoundResearchKey requiredResearch;
    
    protected AbstractProjectMaterial() {
        this.selected = false;
    }
    
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag retVal = new CompoundTag();
        retVal.putString("MaterialType", this.getMaterialType());
        retVal.putBoolean("Selected", this.isSelected());
        retVal.putDouble("Weight", this.getWeight());
        if (this.requiredResearch != null) {
            retVal.putString("RequiredResearch", this.getRequiredResearch().toString());
        }
        return retVal;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.selected = nbt.getBoolean("Selected");
        this.weight = nbt.getDouble("Weight");
        this.requiredResearch = nbt.contains("RequiredResearch") ? CompoundResearchKey.parse(nbt.getString("RequiredResearch")) : null;
    }
    
    /**
     * Get the type name for this project material.
     * 
     * @return the type name for this project material
     */
    protected abstract String getMaterialType();
    
    /**
     * Determine if this material's requirements are satisfied by the given player.
     * 
     * @param player the player doing the research project
     * @param surroundings TODO
     * @return true if the requirement is satisfied, false otherwise
     */
    public abstract boolean isSatisfied(Player player, Set<Block> surroundings);
    
    /**
     * Consume this project material's requirements from the given player.
     * 
     * @param player the player doing the research project
     * @return true if the consumption succeeded, false otherwise
     */
    public abstract boolean consume(Player player);
    
    /**
     * Determine whether this material should be consumed upon project completion.
     * 
     * @return whether this material should be consumed upon project completion
     */
    public abstract boolean isConsumed();
    
    /**
     * Write this material to the network using its defined serializer.
     * 
     * @param buf the network buffer to be written to
     */
    public abstract void toNetwork(FriendlyByteBuf buf);
    
    public boolean isSelected() {
        return this.selected;
    }
    
    public double getWeight() {
        return this.weight;
    }
    
    @Nullable
    public CompoundResearchKey getRequiredResearch() {
        return this.requiredResearch;
    }
    
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    public void setWeight(double weight) {
        this.weight = weight;
    }
    
    public void setRequiredResearch(@Nonnull CompoundResearchKey key) {
        this.requiredResearch = key.copy();
    }
    
    public boolean hasRequiredResearch(Player player) {
        if (this.requiredResearch == null) {
            return true;
        } else {
            return this.requiredResearch.isKnownByStrict(player);
        }
    }
    
    public abstract AbstractProjectMaterial copy();

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((requiredResearch == null) ? 0 : requiredResearch.hashCode());
        result = prime * result + (selected ? 1231 : 1237);
        long temp;
        temp = Double.doubleToLongBits(weight);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AbstractProjectMaterial other = (AbstractProjectMaterial) obj;
        if (requiredResearch == null) {
            if (other.requiredResearch != null)
                return false;
        } else if (!requiredResearch.equals(other.requiredResearch))
            return false;
        if (selected != other.selected)
            return false;
        if (Double.doubleToLongBits(weight) != Double.doubleToLongBits(other.weight))
            return false;
        return true;
    }
}
