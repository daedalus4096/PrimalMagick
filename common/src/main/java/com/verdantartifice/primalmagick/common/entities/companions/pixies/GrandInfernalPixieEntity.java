package com.verdantartifice.primalmagick.common.entities.companions.pixies;

import com.verdantartifice.primalmagick.common.items.ItemRegistration;
import com.verdantartifice.primalmagick.common.items.misc.PixieItem;

import net.minecraft.world.entity.EntityType;
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
    protected PixieItem getSpawnItem() {
        return ItemRegistration.GRAND_INFERNAL_PIXIE.get();
    }

    @Override
    public int getSpellPower() {
        return 3;
    }
}
