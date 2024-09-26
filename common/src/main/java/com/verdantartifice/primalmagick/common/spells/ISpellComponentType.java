package com.verdantartifice.primalmagick.common.spells;

import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public interface ISpellComponentType {
    ResourceLocation id();
    int sortOrder();
    Supplier<AbstractRequirement<?>> requirementSupplier();
}
