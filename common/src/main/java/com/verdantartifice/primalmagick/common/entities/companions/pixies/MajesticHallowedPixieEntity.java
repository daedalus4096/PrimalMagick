package com.verdantartifice.primalmagick.common.entities.companions.pixies;

import com.verdantartifice.primalmagick.common.items.ItemRegistration;
import com.verdantartifice.primalmagick.common.items.misc.PixieItem;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * Definition of a majestic hallowed pixie.  Greatest of the hallowed pixies.
 * 
 * @author Daedalus4096
 */
public class MajesticHallowedPixieEntity extends AbstractHallowedPixieEntity implements IMajesticPixie {
    public MajesticHallowedPixieEntity(EntityType<? extends AbstractPixieEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected PixieItem getSpawnItem() {
        return ItemRegistration.MAJESTIC_HALLOWED_PIXIE.get();
    }
}