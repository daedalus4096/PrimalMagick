package com.verdantartifice.primalmagick.common.entities.companions.pixies;

import com.verdantartifice.primalmagick.common.items.ItemRegistration;
import com.verdantartifice.primalmagick.common.items.misc.PixieItem;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * Definition of a basic void pixie.  Weakest of the void pixies.
 * 
 * @author Daedalus4096
 */
public class BasicVoidPixieEntity extends AbstractVoidPixieEntity implements IBasicPixie {
    public BasicVoidPixieEntity(EntityType<? extends AbstractPixieEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected PixieItem getSpawnItem() {
        return ItemsPM.BASIC_VOID_PIXIE.get();
    }
}
