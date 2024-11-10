package com.verdantartifice.primalmagick.common.entities.companions.pixies;

import com.verdantartifice.primalmagick.common.items.ItemRegistration;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;

/**
 * Definition of a majestic moon pixie.  Greatest of the moon pixies.
 * 
 * @author Daedalus4096
 */
public class MajesticMoonPixieEntity extends AbstractMoonPixieEntity implements IMajesticPixie {
    public MajesticMoonPixieEntity(EntityType<? extends AbstractPixieEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected SpawnEggItem getSpawnItem() {
        return ItemsPM.MAJESTIC_MOON_PIXIE.get();
    }
}
