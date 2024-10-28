package com.verdantartifice.primalmagick.common.util;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.Tag;

/**
 * Common utility interface for types that can be serialized to NBT tags and back. Mirrors the
 * functionality provided by the Forge and Neoforge INBTSerializable interfaces.
 *
 * @param <T> the type of NBT tag used for serialization
 */
public interface INBTSerializablePM<T extends Tag> {
    T serializeNBT(HolderLookup.Provider registryAccess);
    void deserializeNBT(HolderLookup.Provider registryAccess, T nbt);
}
