package com.verdantartifice.primalmagick.common.capabilities;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncCooldownsPacket;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

/**
 * Default implementation of the player cooldown capability.
 * 
 * @author Daedalus4096
 */
public class PlayerCooldowns implements IPlayerCooldowns {
    private final Map<CooldownType, Long> cooldowns = new ConcurrentHashMap<>();    // Map of cooldown types to recovery times, in system milliseconds
    private long syncTimestamp = 0L;    // Last timestamp at which this capability received a sync from the server

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag rootTag = new CompoundTag();
        ListTag cooldownList = new ListTag();
        for (CooldownType type : this.cooldowns.keySet()) {
            if (type != null) {
                Long time = this.cooldowns.get(type);
                if (time != null && time.longValue() > 0) {
                    CompoundTag tag = new CompoundTag();
                    tag.putString("Type", type.name());
                    tag.putLong("Value", time.longValue());
                    cooldownList.add(tag);
                }
            }
        }
        rootTag.put("Cooldowns", cooldownList);
        rootTag.putLong("SyncTimestamp", System.currentTimeMillis());
        return rootTag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (nbt == null || nbt.getLong("SyncTimestamp") <= this.syncTimestamp) {
            return;
        }
        
        this.clearCooldowns();
        
        ListTag cooldownList = nbt.getList("Cooldowns", Tag.TAG_COMPOUND);
        for (int index = 0; index < cooldownList.size(); index++) {
            CompoundTag tag = cooldownList.getCompound(index);
            CooldownType type = null;
            try {
                type = CooldownType.valueOf(tag.getString("Type"));
            } catch (Exception e) {}
            long time = tag.getLong("Value");
            if (type != null) {
                this.cooldowns.put(type, Long.valueOf(time));
            }
        }
    }

    @Override
    public boolean isOnCooldown(CooldownType type) {
        if (type == null) {
            return false;
        }
        // The cooldown is still active if the stored recovery time is greater than the current system time
        return (this.cooldowns.getOrDefault(type, Long.valueOf(0)).longValue() > System.currentTimeMillis());
    }
    
    @Override
    public long getRemainingCooldown(CooldownType type) {
        return Math.max(0, this.cooldowns.getOrDefault(type, Long.valueOf(0)).longValue() - System.currentTimeMillis());
    }

    @Override
    public void setCooldown(CooldownType type, int durationTicks) {
        if (type != null) {
            this.cooldowns.put(type, (System.currentTimeMillis() + (durationTicks * 50)));
        }
    }

    @Override
    public void clearCooldowns() {
        this.cooldowns.clear();
    }

    @Override
    public void sync(ServerPlayer player) {
        if (player != null) {
            PacketHandler.sendToPlayer(new SyncCooldownsPacket(player), player);
        }
    }
    
    /**
     * Capability provider for the player cooldowns capability.  Used to attach capability data to the owner.
     * 
     * @author Daedalus4096
     * @see {@link com.verdantartifice.primalmagick.common.events.CapabilityEvents}
     */
    public static class Provider implements ICapabilitySerializable<CompoundTag> {
        public static final ResourceLocation NAME = PrimalMagick.resource("capability_cooldowns");
        
        private final IPlayerCooldowns instance = new PlayerCooldowns();
        private final LazyOptional<IPlayerCooldowns> holder = LazyOptional.of(() -> instance);  // Cache a lazy optional of the capability instance

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            if (cap == PrimalMagickCapabilities.COOLDOWNS) {
                return holder.cast();
            } else {
                return LazyOptional.empty();
            }
        }

        @Override
        public CompoundTag serializeNBT() {
            return instance.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            instance.deserializeNBT(nbt);
        }
    }
}
