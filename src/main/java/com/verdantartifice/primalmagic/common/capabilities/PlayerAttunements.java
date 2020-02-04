package com.verdantartifice.primalmagic.common.capabilities;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.attunements.AttunementType;
import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.data.SyncAttunementsPacket;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;

/**
 * Default implementation of the player attunements capability.
 * 
 * @author Daedalus4096
 */
public class PlayerAttunements implements IPlayerAttunements {
    // Nested map of sources to attunement types to values
    private final Map<Source, Map<AttunementType, Integer>> attunements = new ConcurrentHashMap<>();

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT rootTag = new CompoundNBT();
        
        // Serialize recorded attunement values
        ListNBT attunementList = new ListNBT();
        for (Map.Entry<Source, Map<AttunementType, Integer>> sourceEntry : this.attunements.entrySet()) {
            if (sourceEntry != null) {
                for (Map.Entry<AttunementType, Integer> typeEntry : sourceEntry.getValue().entrySet()) {
                    if (typeEntry != null && sourceEntry.getKey() != null && typeEntry.getKey() != null && typeEntry.getValue() != null) {
                        CompoundNBT tag = new CompoundNBT();
                        tag.putString("Source", sourceEntry.getKey().getTag());
                        tag.putString("Type", typeEntry.getKey().name());
                        tag.putInt("Value", typeEntry.getValue().intValue());
                        attunementList.add(tag);
                    }
                }
            }
        }
        rootTag.put("Attunements", attunementList);
        
        return rootTag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (nbt == null) {
            return;
        }
        
        this.clear();
        
        // Deserialize attunement values
        ListNBT attunementList = nbt.getList("Attunements", Constants.NBT.TAG_COMPOUND);
        for (int index = 0; index < attunementList.size(); index++) {
            CompoundNBT tag = attunementList.getCompound(index);
            Source source = Source.getSource(tag.getString("Source"));
            AttunementType type = null;
            try {
                type = AttunementType.valueOf(tag.getString("Type"));
            } catch (Exception e) {}
            int value = tag.getInt("Value");
            this.setValue(source, type, value);
        }
    }

    @Override
    public void clear() {
        this.attunements.clear();
    }

    @Override
    public int getValue(Source source, AttunementType type) {
        return this.attunements.getOrDefault(source, Collections.emptyMap()).getOrDefault(type, Integer.valueOf(0)).intValue();
    }

    @Override
    public void setValue(Source source, AttunementType type, int value) {
        // Don't allow null keys or values in the data
        if (source != null && type != null) {
            // Get the map of types to values for the source, creating an empty one if it doesn't exist
            Map<AttunementType, Integer> typeMap = this.attunements.computeIfAbsent(source, k -> new ConcurrentHashMap<>());
            
            // Determine if the value to be set must be capped
            int toSet = type.isCapped() ? MathHelper.clamp(value, 0, type.getMaximum()) : Math.max(0, value);
            
            // Add the given value to the type map
            typeMap.put(type, Integer.valueOf(toSet));
        }
    }

    @Override
    public void sync(ServerPlayerEntity player) {
        if (player != null) {
            PacketHandler.sendToPlayer(new SyncAttunementsPacket(player), player);
        }
    }

    /**
     * Capability provider for the player attunements capability.  Used to attach capability data to the owner.
     * 
     * @author Daedalus4096
     * @see {@link com.verdantartifice.primalmagic.common.events.CapabilityEvents}
     */
    public static class Provider implements ICapabilitySerializable<CompoundNBT> {
        public static final ResourceLocation NAME = new ResourceLocation(PrimalMagic.MODID, "capability_attunements");
        
        private final IPlayerAttunements instance = PrimalMagicCapabilities.ATTUNEMENTS.getDefaultInstance();
        private final LazyOptional<IPlayerAttunements> holder = LazyOptional.of(() -> instance);  // Cache a lazy optional of the capability instance
        
        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            if (cap == PrimalMagicCapabilities.ATTUNEMENTS) {
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
     * Storage manager for the player attunements capability.  Used to register the capability.
     * 
     * @author Daedalus4096
     * @see {@link com.verdantartifice.primalmagic.common.init.InitCapabilities}
     */
    public static class Storage implements Capability.IStorage<IPlayerAttunements> {
        @Override
        public INBT writeNBT(Capability<IPlayerAttunements> capability, IPlayerAttunements instance, Direction side) {
            // Use the instance's pre-defined serialization
            return instance.serializeNBT();
        }

        @Override
        public void readNBT(Capability<IPlayerAttunements> capability, IPlayerAttunements instance, Direction side, INBT nbt) {
            // Use the instance's pre-defined deserialization
            instance.deserializeNBT((CompoundNBT)nbt);
        }
    }
    
    /**
     * Factory for the player attunements capability.  Used to register the capability.
     * 
     * @author Daedalus4096
     * @see {@link com.verdantartifice.primalmagic.common.init.InitCapabilities}
     */
    public static class Factory implements Callable<IPlayerAttunements> {
        @Override
        public IPlayerAttunements call() throws Exception {
            return new PlayerAttunements();
        }
    }
}
