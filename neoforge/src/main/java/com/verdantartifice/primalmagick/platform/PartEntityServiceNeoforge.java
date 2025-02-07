package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.platform.services.IPartEntityService;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.entity.PartEntity;

public class PartEntityServiceNeoforge implements IPartEntityService {
    @Override
    public boolean isPartEntity(Entity entity) {
        return entity instanceof PartEntity<?>;
    }

    @Override
    public Entity getParent(Entity partEntity) {
        if (partEntity instanceof PartEntity<?> part) {
            return part.getParent();
        } else {
            throw new IllegalArgumentException("Not a PartEntity");
        }
    }
}
