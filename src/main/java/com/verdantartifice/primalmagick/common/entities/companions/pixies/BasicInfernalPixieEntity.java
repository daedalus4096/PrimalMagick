package com.verdantartifice.primalmagick.common.entities.companions.pixies;

import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.misc.PixieItem;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * Definition of a basic infernal pixie.  Weakest of the infernal pixies.
 * 
 * @author Daedalus4096
 */
public class BasicInfernalPixieEntity extends AbstractInfernalPixieEntity implements IBasicPixie {
    public BasicInfernalPixieEntity(EntityType<? extends AbstractPixieEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected PixieItem getSpawnItem() {
        return ItemsPM.BASIC_INFERNAL_PIXIE.get();
    }

    @Override
    public int getSpellPower() {
        return 2;
    }
}
