package com.verdantartifice.primalmagick.common.entities.companions.pixies;

import com.verdantartifice.primalmagick.common.items.ItemRegistration;
import com.verdantartifice.primalmagick.common.items.misc.PixieItem;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * Definition of a grand sky pixie.  Middle of the sky pixies.
 * 
 * @author Daedalus4096
 */
public class GrandSkyPixieEntity extends AbstractSkyPixieEntity implements IGrandPixie {
    public GrandSkyPixieEntity(EntityType<? extends AbstractPixieEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected PixieItem getSpawnItem() {
        return ItemsPM.GRAND_SKY_PIXIE.get();
    }
}
