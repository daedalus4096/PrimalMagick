package com.verdantartifice.primalmagick.common.entities.companions.pixies;

import com.verdantartifice.primalmagick.common.items.ItemRegistration;
import com.verdantartifice.primalmagick.common.items.misc.PixieItem;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * Definition of a basic blood pixie.  Weakest of the blood pixies.
 * 
 * @author Daedalus4096
 */
public class BasicBloodPixieEntity extends AbstractBloodPixieEntity implements IBasicPixie {
    public BasicBloodPixieEntity(EntityType<? extends AbstractPixieEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected PixieItem getSpawnItem() {
        return ItemRegistration.BASIC_BLOOD_PIXIE.get();
    }
}
