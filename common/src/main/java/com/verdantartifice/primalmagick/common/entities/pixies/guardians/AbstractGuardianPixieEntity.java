package com.verdantartifice.primalmagick.common.entities.pixies.guardians;

import com.verdantartifice.primalmagick.common.entities.pixies.IPixie;
import com.verdantartifice.primalmagick.common.sources.Source;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

/**
 * Definition of a guardian pixie that is deployed by a Pixie House in self-defense.
 *
 * @author Daedalus4096
 */
public abstract class AbstractGuardianPixieEntity extends PathfinderMob implements FlyingAnimal, IPixie {
    @Nullable
    protected Source source;

    protected AbstractGuardianPixieEntity(EntityType<? extends AbstractGuardianPixieEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    protected static <T extends AbstractGuardianPixieEntity> T spawn(EntityType<T> entityType, Source source, ServerLevel level, BlockPos pos) {
        T pixie = entityType.spawn(level, $ -> {}, pos, MobSpawnType.SPAWN_EGG, true, true);
        if (pixie != null) {
            pixie.setPixieSource(source);
            level.addFreshEntityWithPassengers(pixie);
        }
        return pixie;
    }

    @Override
    public Source getPixieSource() {
        return this.source;
    }

    protected void setPixieSource(Source source) {
        this.source = source;
    }

    @Override
    public boolean isFlying() {
        return !this.onGround();
    }
}
