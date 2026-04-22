package com.verdantartifice.primalmagick.common.capabilities;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.util.INBTSerializablePM;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.Tag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.IntFunction;

/**
 * Capability interface for storing player companion IDs.  Attached to player entities.
 * 
 * @author Daedalus4096
 */
public interface IPlayerCompanions extends INBTSerializablePM<Tag> {
    /**
     * Adds the given entity ID for the given companion type to the player's data.  Removes and returns
     * the oldest companion ID for the given type if the new ID would put the player over the limit for
     * that companion type.
     * 
     * @param type the type of companion to add
     * @param id the companion ID to be added
     * @return the oldest companion ID if this would exceed the companion limit, or null 
     */
    EntityReference<LivingEntity> add(CompanionType type, EntityReference<LivingEntity> id);
    
    /**
     * Gets whether the given entity ID of the given companion type exists in this player's companion set.
     * 
     * @param type the type of companion to query
     * @param id the companion ID to be queried
     * @return true if the given ID represents one of the player's active companions, false otherwise
     */
    boolean contains(CompanionType type, EntityReference<LivingEntity> id);
    
    /**
     * Gets all the companion IDs of the given companion type for this player.
     * 
     * @param type the type of companion to query
     * @return the list of all active companion IDs of the given type for this player
     */
    List<EntityReference<LivingEntity>> get(CompanionType type);
    
    /**
     * Removes the given companion ID of the given companion type from the player's data, if present.
     * 
     * @param type the type of companion to remove
     * @param id the companion ID to be removed
     * @return true if the given ID was present for the player, false otherwise
     */
    boolean remove(CompanionType type, EntityReference<LivingEntity> id);
    
    /**
     * Removes all companions from this player.
     */
    void clear();
    
    /**
     * Sync the given player's companion data to their client.
     * 
     * @param player the player whose client should receive the data
     */
    void sync(@NotNull ServerPlayer player);
    
    enum CompanionType implements StringRepresentable {
        GOLEM(0, "golem", 1),
        PIXIE(1, "pixie", 3);

        private static final IntFunction<CompanionType> BY_ID = ByIdMap.continuous(CompanionType::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
        public static final Codec<CompanionType> CODEC = StringRepresentable.fromValues(CompanionType::values);
        public static final StreamCodec<ByteBuf, CompanionType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, CompanionType::getId);

        private final int id;
        private final String name;
        private final int limit;
        
        CompanionType(int id, @NotNull String name, int limit) {
            this.id = id;
            this.name = name;
            this.limit = limit;
        }

        public int getId() {
            return this.id;
        }

        @Override
        @NotNull
        public String getSerializedName() {
            return this.name;
        }
        
        public int getLimit() {
            return this.limit;
        }
    }
}
