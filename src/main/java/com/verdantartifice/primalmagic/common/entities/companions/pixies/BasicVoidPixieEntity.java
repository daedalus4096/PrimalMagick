package com.verdantartifice.primalmagic.common.entities.companions.pixies;

import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.items.misc.PixieItem;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

/**
 * Definition of a basic void pixie.  Weakest of the void pixies.
 * 
 * @author Daedalus4096
 */
public class BasicVoidPixieEntity extends AbstractVoidPixieEntity implements IBasicPixie {
    public BasicVoidPixieEntity(EntityType<? extends AbstractPixieEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected PixieItem getSpawnItem() {
        return ItemsPM.BASIC_VOID_PIXIE.get();
    }
}
