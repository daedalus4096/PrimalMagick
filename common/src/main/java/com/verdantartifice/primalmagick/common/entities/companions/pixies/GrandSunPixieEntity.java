package com.verdantartifice.primalmagick.common.entities.companions.pixies;

import com.verdantartifice.primalmagick.common.items.ItemRegistration;
import com.verdantartifice.primalmagick.common.items.misc.PixieItem;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * Definition of a grand sun pixie.  Middle of the sun pixies.
 * 
 * @author Daedalus4096
 */
public class GrandSunPixieEntity extends AbstractSunPixieEntity implements IGrandPixie {
    public GrandSunPixieEntity(EntityType<? extends AbstractPixieEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected PixieItem getSpawnItem() {
        return ItemRegistration.GRAND_SUN_PIXIE.get();
    }
}
