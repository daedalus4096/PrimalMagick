package com.verdantartifice.primalmagic.common.capabilities;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.data.SyncCooldownsPacket;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
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

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT rootTag = new CompoundNBT();
        ListNBT cooldownList = new ListNBT();
        for (CooldownType type : this.cooldowns.keySet()) {
            if (type != null) {
                Long time = this.cooldowns.get(type);
                if (time != null && time.longValue() > 0) {
                    CompoundNBT tag = new CompoundNBT();
                    tag.putString("Type", type.name());
                    tag.putLong("Value", time.longValue());
                    cooldownList.add(tag);
                }
            }
        }
        rootTag.put("Cooldowns", cooldownList);
        return rootTag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (nbt == null) {
            return;
        }
        
        this.clearCooldowns();
        
        ListNBT cooldownList = nbt.getList("Cooldowns", 10);
        for (int index = 0; index < cooldownList.size(); index++) {
            CompoundNBT tag = cooldownList.getCompound(index);
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
    public void sync(ServerPlayerEntity player) {
        if (player != null) {
            PacketHandler.sendToPlayer(new SyncCooldownsPacket(player), player);
        }
    }
    
    /**
     * Capability provider for the player cooldowns capability.  Used to attach capability data to the owner.
     * 
     * @author Daedalus4096
     * @see {@link com.verdantartifice.primalmagic.common.events.CapabilityEvents}
     */
    public static class Provider implements ICapabilitySerializable<CompoundNBT> {
        public static final ResourceLocation NAME = new ResourceLocation(PrimalMagic.MODID, "capability_cooldowns");
        
        private final IPlayerCooldowns instance = PrimalMagicCapabilities.COOLDOWNS.getDefaultInstance();
        private final LazyOptional<IPlayerCooldowns> holder = LazyOptional.of(() -> instance);  // Cache a lazy optional of the capability instance

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            if (cap == PrimalMagicCapabilities.COOLDOWNS) {
                return holder.cast();
            } else {
                return LazyOptional.empty();
            }
        }

        @Override
        public CompoundNBT serializeNBT() {
            return instance.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            instance.deserializeNBT(nbt);
        }
    }
    
    /**
     * Storage manager for the player cooldowns capability.  Used to register the capability.
     * 
     * @author Daedalus4096
     * @see {@link com.verdantartifice.primalmagic.common.init.InitCapabilities}
     */
    public static class Storage implements Capability.IStorage<IPlayerCooldowns> {
        @Override
        public INBT writeNBT(Capability<IPlayerCooldowns> capability, IPlayerCooldowns instance, Direction side) {
            // Use the instance's pre-defined serialization
            return instance.serializeNBT();
        }

        @Override
        public void readNBT(Capability<IPlayerCooldowns> capability, IPlayerCooldowns instance, Direction side, INBT nbt) {
            // Use the instance's pre-defined deserialization
            instance.deserializeNBT((CompoundNBT)nbt);
        }
    }
    
    /**
     * Factory for the player cooldowns capability.  Used to register the capability.
     * 
     * @author Daedalus4096
     * @see {@link com.verdantartifice.primalmagic.common.init.InitCapabilities}
     */
    public static class Factory implements Callable<IPlayerCooldowns> {
        @Override
        public IPlayerCooldowns call() throws Exception {
            return new PlayerCooldowns();
        }
    }
}
