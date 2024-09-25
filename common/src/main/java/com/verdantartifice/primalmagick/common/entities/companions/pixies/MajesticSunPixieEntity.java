package com.verdantartifice.primalmagick.common.entities.companions.pixies;

import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.misc.PixieItem;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * Definition of a majestic sun pixie.  Greatest of the sun pixies.
 * 
 * @author Daedalus4096
 */
public class MajesticSunPixieEntity extends AbstractSunPixieEntity implements IMajesticPixie {
    public MajesticSunPixieEntity(EntityType<? extends AbstractPixieEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected PixieItem getSpawnItem() {
        return ItemsPM.MAJESTIC_SUN_PIXIE.get();
    }
}
