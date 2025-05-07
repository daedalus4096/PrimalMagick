package com.verdantartifice.primalmagick.common.entities.pixies.guardians;

import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.entities.pixies.IBasicPixie;
import com.verdantartifice.primalmagick.common.sources.Source;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class BasicGuardianPixieEntity extends AbstractGuardianPixieEntity implements IBasicPixie {
    public BasicGuardianPixieEntity(EntityType<? extends AbstractGuardianPixieEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static BasicGuardianPixieEntity spawn(Source source, ServerLevel level, BlockPos pos) {
        return AbstractGuardianPixieEntity.spawn(EntityTypesPM.BASIC_GUARDIAN_PIXIE.get(), source, level, pos);
    }
}
