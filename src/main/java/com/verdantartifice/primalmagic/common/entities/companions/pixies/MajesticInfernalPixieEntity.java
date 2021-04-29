package com.verdantartifice.primalmagic.common.entities.companions.pixies;

import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.items.misc.PixieItem;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

/**
 * Definition of a majestic infernal pixie.  Greatest of the infernal pixies.
 * 
 * @author Daedalus4096
 */
public class MajesticInfernalPixieEntity extends AbstractInfernalPixieEntity implements IMajesticPixie {
    public MajesticInfernalPixieEntity(EntityType<? extends AbstractPixieEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected PixieItem getSpawnItem() {
        return ItemsPM.MAJESTIC_INFERNAL_PIXIE.get();
    }

    @Override
    public int getSpellPower() {
        return 4;
    }
}
