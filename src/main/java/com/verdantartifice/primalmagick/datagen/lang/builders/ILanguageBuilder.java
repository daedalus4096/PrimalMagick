package com.verdantartifice.primalmagick.datagen.lang.builders;

import net.minecraft.resources.ResourceKey;

public interface ILanguageBuilder {
    ResourceKey<?> getKey();
    boolean isEmpty();
    void build();
}
