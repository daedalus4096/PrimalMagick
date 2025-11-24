package com.verdantartifice.primalmagick.common.util;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * Common utility interface for types that can be serialized using {@link net.minecraft.world.level.storage.ValueInput}
 * and {@link net.minecraft.world.level.storage.ValueOutput}. Mirrors the functionality provided by the NeoForge
 * ValueIOSerializable interface.
 */
public interface IValueIOSerializablePM {
    void serialize(ValueOutput output);
    void deserialize(ValueInput input);
}
