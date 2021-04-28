package com.verdantartifice.primalmagic.common.entities.companions.pixies;

import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.items.misc.PixieItem;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

/**
 * Definition of a basic blood pixie.  Weakest of the blood pixies.
 * 
 * @author Daedalus4096
 */
public class BasicBloodPixieEntity extends AbstractBloodPixieEntity implements IBasicPixie {
    public BasicBloodPixieEntity(EntityType<? extends AbstractPixieEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected PixieItem getSpawnItem() {
        return ItemsPM.BASIC_BLOOD_PIXIE.get();
    }
}
