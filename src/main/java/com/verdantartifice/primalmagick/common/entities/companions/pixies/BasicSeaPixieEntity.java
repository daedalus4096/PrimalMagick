package com.verdantartifice.primalmagick.common.entities.companions.pixies;

import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.misc.PixieItem;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * Definition of a basic sea pixie.  Weakest of the sea pixies.
 * 
 * @author Daedalus4096
 */
public class BasicSeaPixieEntity extends AbstractSeaPixieEntity implements IBasicPixie {
    public BasicSeaPixieEntity(EntityType<? extends AbstractPixieEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected PixieItem getSpawnItem() {
        return ItemsPM.BASIC_SEA_PIXIE.get();
    }
}
