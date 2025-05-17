package com.verdantartifice.primalmagick.common.entities.pixies.guardians;

import com.verdantartifice.primalmagick.common.entities.pixies.IMajesticPixie;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class MajesticGuardianPixieEntity extends AbstractGuardianPixieEntity implements IMajesticPixie {
    public MajesticGuardianPixieEntity(EntityType<? extends AbstractGuardianPixieEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
}
