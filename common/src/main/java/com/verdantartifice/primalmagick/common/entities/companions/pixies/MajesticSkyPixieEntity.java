package com.verdantartifice.primalmagick.common.entities.companions.pixies;

import com.verdantartifice.primalmagick.common.items.ItemRegistration;
import com.verdantartifice.primalmagick.common.items.misc.PixieItem;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * Definition of a majestic sky pixie.  Greatest of the sky pixies.
 * 
 * @author Daedalus4096
 */
public class MajesticSkyPixieEntity extends AbstractSkyPixieEntity implements IMajesticPixie {
    public MajesticSkyPixieEntity(EntityType<? extends AbstractPixieEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected PixieItem getSpawnItem() {
        return ItemsPM.MAJESTIC_SKY_PIXIE.get();
    }
}
