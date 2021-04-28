package com.verdantartifice.primalmagic.common.entities.companions.pixies;

import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.items.misc.PixieItem;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

/**
 * Definition of a basic infernal pixie.  Weakest of the infernal pixies.
 * 
 * @author Daedalus4096
 */
public class BasicInfernalPixieEntity extends AbstractInfernalPixieEntity implements IBasicPixie {
    public BasicInfernalPixieEntity(EntityType<? extends AbstractPixieEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected PixieItem getSpawnItem() {
        return ItemsPM.BASIC_INFERNAL_PIXIE.get();
    }
}
