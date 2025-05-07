package com.verdantartifice.primalmagick.common.entities.pixies.companions;

import com.verdantartifice.primalmagick.common.entities.pixies.IMajesticPixie;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.SpawnEggItem;
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
    protected SpawnEggItem getSpawnItem() {
        return ItemsPM.MAJESTIC_SUN_PIXIE.get();
    }
}
