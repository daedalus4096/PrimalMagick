package com.verdantartifice.primalmagick.common.entities.pixies.companions;

import com.verdantartifice.primalmagick.common.entities.pixies.IMajesticPixie;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.SpawnEggItem;
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
    protected SpawnEggItem getSpawnItem() {
        return ItemsPM.MAJESTIC_VOID_PIXIE.get();
    }
}
