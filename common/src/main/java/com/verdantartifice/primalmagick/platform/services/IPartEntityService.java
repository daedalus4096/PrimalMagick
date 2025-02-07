package com.verdantartifice.primalmagick.platform.services;

import net.minecraft.world.entity.Entity;

public interface IPartEntityService {
    boolean isPartEntity(Entity entity);
    Entity getParent(Entity partEntity);
}
