package com.verdantartifice.primalmagick.common.capabilities;

import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncWardPacket;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;

import java.util.List;

/**
 * Default implementation of the player ward capability.
 * 
 * @author Daedalus4096
 */
public class PlayerWard implements IPlayerWard {
    protected static final List<EquipmentSlot> APPLICABLE_SLOTS = List.of(EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.HEAD, EquipmentSlot.FEET);
    protected static final int PAUSE_DURATION_MILLIS = 10000;
    
    private float current = 0;
    private float max = 0;
    private long lastPaused = 0;        // Last timestamp at which regeneration was paused
    private long syncTimestamp = 0L;    // Last timestamp at which this capability received a sync from the server

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider registries) {
        CompoundTag rootTag = new CompoundTag();
        rootTag.putFloat("Current", this.current);
        rootTag.putFloat("Max", this.max);
        rootTag.putLong("LastPaused", this.lastPaused);
        rootTag.putLong("SyncTimestamp", System.currentTimeMillis());
        return rootTag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider registries, CompoundTag nbt) {
        if (nbt == null || nbt.getLong("SyncTimestamp") <= this.syncTimestamp) {
            return;
        }
        this.syncTimestamp = nbt.getLong("SyncTimestamp");
        this.clear();
        this.current = nbt.getFloat("Current");
        this.max = nbt.getFloat("Max");
        this.lastPaused = nbt.getLong("LastPaused");
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
    public void sync(ServerPlayer player) {
        if (player != null) {
            PacketHandler.sendToPlayer(new SyncWardPacket(player), player);
        }
    }
}
