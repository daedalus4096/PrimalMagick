package com.verdantartifice.primalmagick.common.capabilities;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.Identifier;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

public class PlayerAttunementsForge extends PlayerAttunements implements IPlayerAttunementsForge {
    /**
     * Capability provider for the player attunements capability.  Used to attach capability data to the owner.
     *
     * @author Daedalus4096
     * @see com.verdantartifice.primalmagick.common.events.CapabilityEvents
     */
    public static class Provider implements ICapabilitySerializable<Tag> {
        public static final Identifier NAME = ResourceUtils.loc("capability_attunements");

        private final IPlayerAttunements instance = new PlayerAttunements();
        private final LazyOptional<IPlayerAttunements> holder = LazyOptional.of(() -> instance);  // Cache a lazy optional of the capability instance

        @Override
        @NotNull
        public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
            if (cap == CapabilitiesForge.ATTUNEMENTS) {
                return holder.cast();
            } else {
                return LazyOptional.empty();
            }
        }

        @Override
        public Tag serializeNBT(HolderLookup.Provider registries) {
            return instance.serializeNBT(registries);
        }

        @Override
        public void deserializeNBT(HolderLookup.Provider registries, Tag nbt) {
            instance.deserializeNBT(registries, nbt);
        }
    }
}
