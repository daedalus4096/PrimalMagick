package com.verdantartifice.primalmagick.common.spells;

import java.util.function.Supplier;

import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;

import net.minecraft.resources.ResourceLocation;

public interface ISpellComponentType {
    ResourceLocation id();
    int sortOrder();
    Supplier<AbstractRequirement<?>> requirementSupplier();
}
