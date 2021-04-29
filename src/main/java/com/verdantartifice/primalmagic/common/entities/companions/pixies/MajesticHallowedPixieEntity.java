package com.verdantartifice.primalmagic.common.entities.companions.pixies;

import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.items.misc.PixieItem;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

/**
 * Definition of a majestic hallowed pixie.  Greatest of the hallowed pixies.
 * 
 * @author Daedalus4096
 */
public class MajesticHallowedPixieEntity extends AbstractHallowedPixieEntity implements IMajesticPixie {
    public MajesticHallowedPixieEntity(EntityType<? extends AbstractPixieEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected PixieItem getSpawnItem() {
        return ItemsPM.MAJESTIC_HALLOWED_PIXIE.get();
    }
}
