package com.verdantartifice.primalmagick.common.entities.companions.pixies;

import com.verdantartifice.primalmagick.common.items.ItemRegistration;
import com.verdantartifice.primalmagick.common.items.misc.PixieItem;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * Definition of a basic hallowed pixie.  Weakest of the hallowed pixies.
 * 
 * @author Daedalus4096
 */
public class BasicHallowedPixieEntity extends AbstractHallowedPixieEntity implements IBasicPixie {
    public BasicHallowedPixieEntity(EntityType<? extends AbstractPixieEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected PixieItem getSpawnItem() {
        return ItemsPM.BASIC_HALLOWED_PIXIE.get();
    }
}
