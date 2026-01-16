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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.IntFunction;

/**
 * Capability interface for storing custom cooldowns.  Attached to player entities.
 * 
 * @author Daedalus4096
 */
public interface IPlayerCooldowns extends INBTSerializablePM<Tag> {
    /**
     * Clears all active cooldowns from this player.
     */
    void clear();

    /**
     * Determine if the given cooldown type is active, rendering that ability unusable.
     * 
     * @param type the type of cooldown
     * @return true if the ability is on cooldown and unusable, false otherwise
     */
    boolean isOnCooldown(@Nullable CooldownType type);
    
    /**
     * Determine how much time remains for the given cooldown type.
     * 
     * @param type the type of cooldown
     * @return the remaining cooldown time in milliseconds, or zero if not on cooldown
     */
    long getRemainingCooldown(@Nullable CooldownType type);
    
    /**
     * Set a cooldown of the given type, rendering an ability temporarily unusable.
     * 
     * @param type the type of cooldown
     * @param durationTicks the length of the cooldown to set, in ticks
     */
    void setCooldown(@Nullable CooldownType type, int durationTicks);
    
    /**
     * Reset all active cooldowns.
     */
    void clearCooldowns();
    
    /**
     * Sync the given player's cooldown data to their client.
     * 
     * @param player the player whose client should receive the data
     */
    void sync(@Nullable ServerPlayer player);
    
    enum CooldownType implements StringRepresentable {
        SPELL(0, "spell"),
        DEATH_SAVE(1, "death_save");

        private static final IntFunction<CooldownType> BY_ID = ByIdMap.continuous(CooldownType::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
        public static final Codec<CooldownType> CODEC = StringRepresentable.fromValues(CooldownType::values);
        public static final StreamCodec<ByteBuf, CooldownType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, CooldownType::getId);

        private final int id;
        private final String name;

        CooldownType(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return this.id;
        }

        @Override
        @NotNull
        public String getSerializedName() {
            return this.name;
        }

        @Nullable
        public static CooldownType fromName(@Nullable String name) {
            for (CooldownType type : CooldownType.values()) {
                if (type.getSerializedName().equals(name)) {
                    return type;
                }
            }
            return null;
        }
    }
}
