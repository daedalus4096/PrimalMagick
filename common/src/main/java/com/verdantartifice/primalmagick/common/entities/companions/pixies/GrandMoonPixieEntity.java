package com.verdantartifice.primalmagick.common.entities.companions.pixies;

import com.verdantartifice.primalmagick.common.items.ItemRegistration;
import com.verdantartifice.primalmagick.common.items.misc.PixieItem;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * Definition of a grand moon pixie.  Middle of the moon pixies.
 * 
 * @author Daedalus4096
 */
public class GrandMoonPixieEntity extends AbstractMoonPixieEntity implements IGrandPixie {
    public GrandMoonPixieEntity(EntityType<? extends AbstractPixieEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected PixieItem getSpawnItem() {
        return ItemRegistration.GRAND_MOON_PIXIE.get();
    }
}
