package com.verdantartifice.primalmagick.common.entities.companions.pixies;

import com.verdantartifice.primalmagick.common.items.ItemsPM;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.SpawnEggItem;
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
    protected SpawnEggItem getSpawnItem() {
        return ItemsPM.GRAND_HALLOWED_PIXIE.get();
    }
}
