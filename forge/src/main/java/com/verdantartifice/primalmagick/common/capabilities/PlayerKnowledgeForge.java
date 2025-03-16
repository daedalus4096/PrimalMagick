package com.verdantartifice.primalmagick.common.capabilities;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerKnowledgeForge extends PlayerKnowledge implements IPlayerKnowledgeForge {
    /**
     * Capability provider for the player knowledge capability.  Used to attach capability data to the owner.
     *
     * @author Daedalus4096
     * @see com.verdantartifice.primalmagick.common.events.CapabilityEvents
     */
    public static class Provider implements ICapabilitySerializable<Tag> {
        public static final ResourceLocation NAME = ResourceUtils.loc("capability_knowledge");

        private final IPlayerKnowledge instance = new PlayerKnowledge();
        private final LazyOptional<IPlayerKnowledge> holder = LazyOptional.of(() -> instance);  // Cache a lazy optional of the capability instance

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            if (cap == CapabilitiesForge.KNOWLEDGE) {
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
