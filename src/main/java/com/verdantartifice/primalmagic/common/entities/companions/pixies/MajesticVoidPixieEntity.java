package com.verdantartifice.primalmagic.common.entities.companions.pixies;

import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.items.misc.PixieItem;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

/**
 * Definition of a majestic void pixie.  Greatest of the void pixies.
 * 
 * @author Daedalus4096
 */
public class MajesticVoidPixieEntity extends AbstractVoidPixieEntity implements IMajesticPixie {
    public MajesticVoidPixieEntity(EntityType<? extends AbstractPixieEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected PixieItem getSpawnItem() {
        return ItemsPM.MAJESTIC_VOID_PIXIE.get();
    }
}
