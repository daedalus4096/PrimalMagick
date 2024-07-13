package com.verdantartifice.primalmagick.common.capabilities;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Capability interface for storing player companion IDs.  Attached to player entities.
 * 
 * @author Daedalus4096
 */
@SuppressWarnings("deprecation")
public interface IPlayerCompanions extends INBTSerializable<CompoundTag> {
    /**
     * Adds the given entity ID for the given companion type to the player's data.  Removes and returns
     * the oldest companion ID for the given type if the new ID would put the player over the limit for
     * that companion type.
     * 
     * @param type the type of comapanion to add
     * @param id the companion ID to be added
     * @return the oldest companion ID if this would exceed the companion limit, or null 
     */
    public UUID add(CompanionType type, UUID id);
    
    /**
     * Gets whether the given entity ID of the given companion type exists in this player's companion set.
     * 
     * @param type the type of companion to query
     * @param id the companion ID to be queried
     * @return true if the given ID represents one of the player's active companions, false otherwise
     */
    public boolean contains(CompanionType type, UUID id);
    
    /**
     * Gets all of the companion IDs of the given companion type for this player.
     * 
     * @param type the type of companion to query
     * @return the list of all active companion IDs of the given type for this player
     */
    public List<UUID> get(CompanionType type);
    
    /**
     * Removes the given companion ID of the given companion type from the player's data, if present.
     * 
     * @param type the type of companion to remove
     * @param id the companion ID to be removed
     * @return true if the given ID was present for the player, false otherwise
     */
    public boolean remove(CompanionType type, UUID id);
    
    /**
     * Removes all companions from this player.
     */
    public void clear();
    
    /**
     * Sync the given player's companion data to the their client.
     * 
     * @param player the player whose client should receive the data
     */
    public void sync(@Nullable ServerPlayer player);
    
    public enum CompanionType implements StringRepresentable {
        GOLEM("golem", 1),
        PIXIE("pixie", 3);
        
        private String name;
        private int limit;
        
        private CompanionType(@Nonnull String name, int limit) {
            this.name = name;
            this.limit = limit;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
        
        public int getLimit() {
            return this.limit;
        }
    }
}
