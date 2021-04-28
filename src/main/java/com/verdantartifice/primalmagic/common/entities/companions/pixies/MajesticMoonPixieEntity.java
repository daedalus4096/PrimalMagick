package com.verdantartifice.primalmagic.common.entities.companions.pixies;

import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.items.misc.PixieItem;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

/**
 * Definition of a majestic moon pixie.  Greatest of the moon pixies.
 * 
 * @author Daedalus4096
 */
public class MajesticMoonPixieEntity extends AbstractMoonPixieEntity implements IMajesticPixie {
    public MajesticMoonPixieEntity(EntityType<? extends AbstractPixieEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected PixieItem getSpawnItem() {
        return ItemsPM.MAJESTIC_MOON_PIXIE.get();
    }
}
