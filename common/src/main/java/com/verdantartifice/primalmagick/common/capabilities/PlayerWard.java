package com.verdantartifice.primalmagick.common.capabilities;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncWardPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.List;

/**
 * Default implementation of the player ward capability.
 * 
 * @author Daedalus4096
 */
public class PlayerWard extends AbstractCapability<PlayerWard> implements IPlayerWard {
    protected static final List<EquipmentSlot> APPLICABLE_SLOTS = List.of(EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.HEAD, EquipmentSlot.FEET);
    protected static final int PAUSE_DURATION_MILLIS = 10000;

    public static final Codec<PlayerWard> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("current").forGetter(w -> w.current),
            Codec.FLOAT.fieldOf("max").forGetter(w -> w.max),
            Codec.LONG.fieldOf("lastPaused").forGetter(w -> w.lastPaused),
            Codec.LONG.fieldOf("syncTimestamp").forGetter(AbstractCapability::getSyncTimestamp)
        ).apply(instance, PlayerWard::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerWard> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, w -> w.current,
            ByteBufCodecs.FLOAT, w -> w.max,
            ByteBufCodecs.VAR_LONG, w -> w.lastPaused,
            ByteBufCodecs.VAR_LONG, AbstractCapability::getSyncTimestamp,
            PlayerWard::new);
    
    private float current;
    private float max;
    private long lastPaused;    // Last timestamp at which regeneration was paused

    public PlayerWard() {
        this(0F, 0F, 0L, 0L);
    }

    protected PlayerWard(float current, float max, long lastPaused, long syncTimestamp) {
        super(syncTimestamp);
        this.current = current;
        this.max = max;
        this.lastPaused = lastPaused;
    }

    @Override
    public Codec<PlayerWard> codec() {
        return CODEC;
    }

    @Override
    protected void copyFromInner(@NonNull PlayerWard other) {
        this.clear();
        this.current = other.current;
        this.max = other.max;
        this.lastPaused = other.lastPaused;
    }

    @Override
    public List<EquipmentSlot> getApplicableSlots() {
        return APPLICABLE_SLOTS;
    }

    @Override
    public float getCurrentWard() {
        return this.current;
    }

    @Override
    public float getMaxWard() {
        return this.max;
    }

    @Override
    public void setCurrentWard(float ward) {
        this.current = Mth.clamp(ward, 0, this.getMaxWard());
    }

    @Override
    public void setMaxWard(float ward) {
        this.max = ward;
        this.current = Mth.clamp(this.current, 0, this.getMaxWard());
    }

    @Override
    public boolean isRegenerating() {
        return this.getCurrentWard() < this.getMaxWard() && System.currentTimeMillis() >= this.lastPaused + PAUSE_DURATION_MILLIS;
    }

    @Override
    public void pauseRegeneration() {
        this.lastPaused = System.currentTimeMillis();
    }

    @Override
    public void clear() {
        this.current = 0;
        this.max = 0;
    }

    @Override
    public void sync(@NotNull ServerPlayer player) {
        this.sync(player, SyncWardPacket::new);
    }
}
