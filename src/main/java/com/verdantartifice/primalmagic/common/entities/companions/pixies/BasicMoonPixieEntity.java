package com.verdantartifice.primalmagic.common.entities.companions.pixies;

import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.items.misc.PixieItem;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * Definition of a basic moon pixie.  Weakest of the moon pixies.
 * 
 * @author Daedalus4096
 */
public class BasicMoonPixieEntity extends AbstractMoonPixieEntity implements IBasicPixie {
    public BasicMoonPixieEntity(EntityType<? extends AbstractPixieEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected PixieItem getSpawnItem() {
        return ItemsPM.BASIC_MOON_PIXIE.get();
    }
}
