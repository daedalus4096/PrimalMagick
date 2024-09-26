package com.verdantartifice.primalmagick.common.entities.companions.pixies;

import com.verdantartifice.primalmagick.common.items.ItemRegistration;
import com.verdantartifice.primalmagick.common.items.misc.PixieItem;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * Definition of a majestic infernal pixie.  Greatest of the infernal pixies.
 * 
 * @author Daedalus4096
 */
public class MajesticInfernalPixieEntity extends AbstractInfernalPixieEntity implements IMajesticPixie {
    public MajesticInfernalPixieEntity(EntityType<? extends AbstractPixieEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected PixieItem getSpawnItem() {
        return ItemRegistration.MAJESTIC_INFERNAL_PIXIE.get();
    }

    @Override
    public int getSpellPower() {
        return 4;
    }
}
