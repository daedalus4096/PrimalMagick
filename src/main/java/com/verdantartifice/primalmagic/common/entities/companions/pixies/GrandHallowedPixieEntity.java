package com.verdantartifice.primalmagic.common.entities.companions.pixies;

import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.items.misc.PixieItem;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

/**
 * Definition of a grand hallowed pixie.  Middle of the hallowed pixies.
 * 
 * @author Daedalus4096
 */
public class GrandHallowedPixieEntity extends AbstractHallowedPixieEntity implements IGrandPixie {
    public GrandHallowedPixieEntity(EntityType<? extends AbstractPixieEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected PixieItem getSpawnItem() {
        return ItemsPM.GRAND_HALLOWED_PIXIE.get();
    }
}
