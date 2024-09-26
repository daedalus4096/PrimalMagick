package com.verdantartifice.primalmagick.common.spells.vehicles;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.spells.ISpellComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public record SpellVehicleType<T extends AbstractSpellVehicle<T>>(ResourceLocation id, int sortOrder, Supplier<T> instanceSupplier, Supplier<AbstractRequirement<?>> requirementSupplier, 
        MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) implements ISpellComponentType {
}
