package com.verdantartifice.primalmagick.common.entities.companions.pixies;

import com.verdantartifice.primalmagick.common.items.ItemsPM;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;

/**
 * Definition of a basic sun pixie.  Weakest of the sun pixies.
 * 
 * @author Daedalus4096
 */
public class BasicSunPixieEntity extends AbstractSunPixieEntity implements IBasicPixie {
    public BasicSunPixieEntity(EntityType<? extends AbstractPixieEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected SpawnEggItem getSpawnItem() {
        return ItemsPM.BASIC_SUN_PIXIE.get();
    }
}
