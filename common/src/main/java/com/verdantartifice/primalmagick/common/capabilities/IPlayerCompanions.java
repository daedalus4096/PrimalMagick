package com.verdantartifice.primalmagick.common.capabilities;

import com.verdantartifice.primalmagick.common.util.INBTSerializablePM;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

/**
 * Capability interface for storing player companion IDs.  Attached to player entities.
 * 
 * @author Daedalus4096
 */
public interface IPlayerCompanions extends INBTSerializablePM<CompoundTag> {
    /**
     * Adds the given entity ID for the given companion type to the player's data.  Removes and returns
     * the oldest companion ID for the given type if the new ID would put the player over the limit for
     * that companion type.
     * 
     * @param type the type of comapanion to add
     * @param id the companion ID to be added
     * @return the oldest companion ID if this would exceed the companion limit, or null 
     */
    UUID add(CompanionType type, UUID id);
    
    /**
     * Gets whether the given entity ID of the given companion type exists in this player's companion set.
     * 
     * @param type the type of companion to query
     * @param id the companion ID to be queried
     * @return true if the given ID represents one of the player's active companions, false otherwise
     */
    boolean contains(CompanionType type, UUID id);
    
    /**
     * Gets all the companion IDs of the given companion type for this player.
     * 
     * @param type the type of companion to query
     * @return the list of all active companion IDs of the given type for this player
     */
    List<UUID> get(CompanionType type);
    
    /**
     * Removes the given companion ID of the given companion type from the player's data, if present.
     * 
     * @param type the type of companion to remove
     * @param id the companion ID to be removed
     * @return true if the given ID was present for the player, false otherwise
     */
    boolean remove(CompanionType type, UUID id);
    
    /**
     * Removes all companions from this player.
     */
    void clear();
    
    /**
     * Sync the given player's companion data to their client.
     * 
     * @param player the player whose client should receive the data
     */
    void sync(@Nullable ServerPlayer player);
    
    enum CompanionType implements StringRepresentable {
        GOLEM("golem", 1),
        PIXIE("pixie", 3);
        
        private final String name;
        private final int limit;
        
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
