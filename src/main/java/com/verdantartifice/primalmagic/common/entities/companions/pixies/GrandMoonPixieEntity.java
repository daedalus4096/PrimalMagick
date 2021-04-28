package com.verdantartifice.primalmagic.common.entities.companions.pixies;

import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.items.misc.PixieItem;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

/**
 * Definition of a grand moon pixie.  Middle of the moon pixies.
 * 
 * @author Daedalus4096
 */
public class GrandMoonPixieEntity extends AbstractMoonPixieEntity implements IGrandPixie {
    public GrandMoonPixieEntity(EntityType<? extends AbstractPixieEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected PixieItem getSpawnItem() {
        return ItemsPM.GRAND_MOON_PIXIE.get();
    }
}
