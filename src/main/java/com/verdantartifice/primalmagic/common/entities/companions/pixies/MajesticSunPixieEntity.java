package com.verdantartifice.primalmagic.common.entities.companions.pixies;

import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.items.misc.PixieItem;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

/**
 * Definition of a majestic sun pixie.  Greatest of the sun pixies.
 * 
 * @author Daedalus4096
 */
public class MajesticSunPixieEntity extends AbstractSunPixieEntity implements IMajesticPixie {
    public MajesticSunPixieEntity(EntityType<? extends AbstractPixieEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected PixieItem getSpawnItem() {
        return ItemsPM.MAJESTIC_SUN_PIXIE.get();
    }
}
