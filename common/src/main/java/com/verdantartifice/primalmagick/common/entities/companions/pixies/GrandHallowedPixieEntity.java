package com.verdantartifice.primalmagick.common.entities.companions.pixies;

import com.verdantartifice.primalmagick.common.items.ItemRegistration;
import com.verdantartifice.primalmagick.common.items.misc.PixieItem;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * Definition of a grand hallowed pixie.  Middle of the hallowed pixies.
 * 
 * @author Daedalus4096
 */
public class GrandHallowedPixieEntity extends AbstractHallowedPixieEntity implements IGrandPixie {
    public GrandHallowedPixieEntity(EntityType<? extends AbstractPixieEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected PixieItem getSpawnItem() {
        return ItemsPM.GRAND_HALLOWED_PIXIE.get();
    }
}
