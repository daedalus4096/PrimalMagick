package com.verdantartifice.primalmagick.platform.services.registries;

import net.minecraft.world.entity.EntityType;

public interface IEntityTypeRegistryService extends IRegistryService<EntityType<?>> {
    void init();
}
