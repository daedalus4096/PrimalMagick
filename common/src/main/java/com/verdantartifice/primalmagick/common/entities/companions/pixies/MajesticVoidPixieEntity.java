package com.verdantartifice.primalmagick.common.entities.companions.pixies;

import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.misc.PixieItem;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * Definition of a majestic void pixie.  Greatest of the void pixies.
 * 
 * @author Daedalus4096
 */
public class MajesticVoidPixieEntity extends AbstractVoidPixieEntity implements IMajesticPixie {
    public MajesticVoidPixieEntity(EntityType<? extends AbstractPixieEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected PixieItem getSpawnItem() {
        return ItemsPM.MAJESTIC_VOID_PIXIE.get();
    }
}
