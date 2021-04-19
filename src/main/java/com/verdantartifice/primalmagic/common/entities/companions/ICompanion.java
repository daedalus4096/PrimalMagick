package com.verdantartifice.primalmagic.common.entities.companions;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.IEntityReader;

/**
 * Interface defining a companion entity, such as an enchanted golem.
 * 
 * @author Daedalus4096
 */
public interface ICompanion {
    /**
     * Get the unique ID of this companion entity.
     * 
     * @return the unique ID of this companion entity
     */
    public UUID getCompanionId();
    
    /**
     * Get the unique ID of this companion entity's owner, if any.
     * 
     * @return the unique ID of this companion entity's owner, if any
     */
    @Nullable
    public UUID getCompanionOwnerId();
    
    /**
     * Set the unique ID of this companion entity's owner.
     * 
     * @param ownerId the unique ID of this companion entity's new owner
     */
    public void setCompanionOwnerId(@Nullable UUID ownerId);
    
    /**
     * Get this companion entity's owner entity.
     * 
     * @param world a valid entity reader
     * @return this companion entity's owner entity
     */
    public default PlayerEntity getCompanionOwner(IEntityReader world) {
        try {
            UUID ownerId = this.getCompanionOwnerId();
            return ownerId == null ? null : world.getPlayerByUuid(ownerId);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    /**
     * Get whether this companion entity has an owner.
     * 
     * @return whether this companion entity has an owner
     */
    public default boolean hasCompanionOwner() {
        return this.getCompanionOwnerId() != null;
    }
    
    /**
     * Get whether this companion entity has been ordered to stay put.
     * 
     * @return whether this companion entity has been ordered to stay put
     */
    public boolean isCompanionStaying();
    
    /**
     * Set whether this companion entity has been ordered to stay put.
     * 
     * @param stay whether this companion entity should stay put
     */
    public void setCompanionStaying(boolean stay);
    
    /**
     * Write this entity's companion data to the given NBT structure.
     * 
     * @param nbt the NBT structure to be written to
     */
    public default void writeCompanionNBT(CompoundNBT nbt) {
        nbt.putBoolean("CompanionStaying", this.isCompanionStaying());
        if (this.hasCompanionOwner()) {
            nbt.putUniqueId("CompanionOwner", this.getCompanionOwnerId());
        }
    }
    
    /**
     * Read the companion data from the given NBT structure.
     * 
     * @param nbt the NBT structure to be read from
     */
    public default void readCompanionNBT(CompoundNBT nbt) {
        this.setCompanionStaying(nbt.getBoolean("CompanionStaying"));
        if (nbt.hasUniqueId("CompanionOwner")) {
            this.setCompanionOwnerId(nbt.getUniqueId("CompanionOwner"));
        } else {
            this.setCompanionOwnerId(null);
        }
    }
}
