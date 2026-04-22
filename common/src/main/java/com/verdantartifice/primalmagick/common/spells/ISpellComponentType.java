package com.verdantartifice.primalmagick.common.spells;

import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

public interface ISpellComponentType {
    Identifier id();
    int sortOrder();
    Supplier<AbstractRequirement<?>> requirementSupplier();
}
