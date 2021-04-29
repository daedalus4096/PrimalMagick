package com.verdantartifice.primalmagic.common.entities.companions.pixies;

import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.items.misc.PixieItem;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

/**
 * Definition of a grand infernal pixie.  Middle of the infernal pixies.
 * 
 * @author Daedalus4096
 */
public class GrandInfernalPixieEntity extends AbstractInfernalPixieEntity implements IGrandPixie {
    public GrandInfernalPixieEntity(EntityType<? extends AbstractPixieEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected PixieItem getSpawnItem() {
        return ItemsPM.GRAND_INFERNAL_PIXIE.get();
    }

    @Override
    public int getSpellPower() {
        return 3;
    }
}
