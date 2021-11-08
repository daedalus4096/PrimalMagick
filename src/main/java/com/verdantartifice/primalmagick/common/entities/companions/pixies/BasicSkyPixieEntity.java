package com.verdantartifice.primalmagick.common.entities.companions.pixies;

import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.misc.PixieItem;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * Definition of a basic sky pixie.  Weakest of the sky pixies.
 * 
 * @author Daedalus4096
 */
public class BasicSkyPixieEntity extends AbstractSkyPixieEntity implements IBasicPixie {
    public BasicSkyPixieEntity(EntityType<? extends AbstractPixieEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected PixieItem getSpawnItem() {
        return ItemsPM.BASIC_SKY_PIXIE.get();
    }
}
