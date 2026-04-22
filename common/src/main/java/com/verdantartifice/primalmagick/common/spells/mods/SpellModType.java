package com.verdantartifice.primalmagick.common.spells.mods;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.spells.ISpellComponentType;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

public record SpellModType<T extends AbstractSpellMod<T>>(Identifier id, int sortOrder, Supplier<T> instanceSupplier, Supplier<AbstractRequirement<?>> requirementSupplier, 
        Supplier<SpellProperty> tiebreaker, MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) implements ISpellComponentType {
}
