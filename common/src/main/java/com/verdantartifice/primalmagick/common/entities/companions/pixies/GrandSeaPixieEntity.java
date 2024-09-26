package com.verdantartifice.primalmagick.common.entities.companions.pixies;

import com.verdantartifice.primalmagick.common.items.ItemRegistration;
import com.verdantartifice.primalmagick.common.items.misc.PixieItem;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * Definition of a grand sea pixie.  Middle of the sea pixies.
 * 
 * @author Daedalus4096
 */
public class GrandSeaPixieEntity extends AbstractSeaPixieEntity implements IGrandPixie {
    public GrandSeaPixieEntity(EntityType<? extends AbstractPixieEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected PixieItem getSpawnItem() {
        return ItemRegistration.GRAND_SEA_PIXIE.get();
    }
}
