package com.verdantartifice.primalmagick.common.entities.pixies.guardians;

import com.verdantartifice.primalmagick.common.entities.pixies.IGrandPixie;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class GrandGuardianPixieEntity extends AbstractGuardianPixieEntity implements IGrandPixie {
    public GrandGuardianPixieEntity(EntityType<? extends AbstractGuardianPixieEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
}
