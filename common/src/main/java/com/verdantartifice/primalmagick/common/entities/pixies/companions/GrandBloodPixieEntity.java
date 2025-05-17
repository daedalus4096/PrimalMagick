package com.verdantartifice.primalmagick.common.entities.pixies.companions;

import com.verdantartifice.primalmagick.common.entities.pixies.IGrandPixie;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;

/**
 * Definition of a grand blood pixie.  Middle of the blood pixies.
 * 
 * @author Daedalus4096
 */
public class GrandBloodPixieEntity extends AbstractBloodPixieEntity implements IGrandPixie {
    public GrandBloodPixieEntity(EntityType<? extends AbstractPixieEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected SpawnEggItem getSpawnItem() {
        return ItemsPM.GRAND_BLOOD_PIXIE.get();
    }
}
