package com.verdantartifice.primalmagick.common.capabilities;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerCooldownsForge extends PlayerCooldowns implements IPlayerCooldownsForge {
    /**
     * Capability provider for the player cooldowns capability.  Used to attach capability data to the owner.
     *
     * @author Daedalus4096
     * @see com.verdantartifice.primalmagick.common.events.CapabilityEvents
     */
    public static class Provider implements ICapabilitySerializable<CompoundTag> {
        public static final ResourceLocation NAME = ResourceUtils.loc("capability_cooldowns");

        private final PlayerCooldowns instance = new PlayerCooldownsForge();
        private final LazyOptional<IPlayerCooldowns> holder = LazyOptional.of(() -> instance);  // Cache a lazy optional of the capability instance

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            if (cap == CapabilitiesForge.COOLDOWNS) {
                return holder.cast();
            } else {
                return LazyOptional.empty();
            }
        }

        @SuppressWarnings("deprecation")
        @Override
        public CompoundTag serializeNBT(HolderLookup.Provider registries) {
            return instance.serializeNBT(registries);
        }

        @SuppressWarnings("deprecation")
        @Override
        public void deserializeNBT(HolderLookup.Provider registries, CompoundTag nbt) {
            instance.deserializeNBT(registries, nbt);
        }
    }
}
