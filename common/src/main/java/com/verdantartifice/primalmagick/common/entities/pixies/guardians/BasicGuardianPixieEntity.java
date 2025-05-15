package com.verdantartifice.primalmagick.common.entities.pixies.guardians;

import com.verdantartifice.primalmagick.common.entities.pixies.IBasicPixie;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class BasicGuardianPixieEntity extends AbstractGuardianPixieEntity implements IBasicPixie {
    public BasicGuardianPixieEntity(EntityType<? extends AbstractGuardianPixieEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
}
