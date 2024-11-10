package com.verdantartifice.primalmagick.common.entities.companions.pixies;

import com.verdantartifice.primalmagick.common.items.ItemRegistration;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;

/**
 * Definition of a majestic sea pixie.  Greatest of the sea pixies.
 * 
 * @author Daedalus4096
 */
public class MajesticSeaPixieEntity extends AbstractSeaPixieEntity implements IMajesticPixie {
    public MajesticSeaPixieEntity(EntityType<? extends AbstractPixieEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected SpawnEggItem getSpawnItem() {
        return ItemsPM.MAJESTIC_SEA_PIXIE.get();
    }
}
