package com.verdantartifice.primalmagick.common.entities.companions.pixies;

import com.verdantartifice.primalmagick.common.items.ItemRegistration;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;

/**
 * Definition of a grand infernal pixie.  Middle of the infernal pixies.
 * 
 * @author Daedalus4096
 */
public class GrandInfernalPixieEntity extends AbstractInfernalPixieEntity implements IGrandPixie {
    public GrandInfernalPixieEntity(EntityType<? extends AbstractPixieEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected SpawnEggItem getSpawnItem() {
        return ItemsPM.GRAND_INFERNAL_PIXIE.get();
    }

    @Override
    public int getSpellPower() {
        return 3;
    }
}
