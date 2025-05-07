package com.verdantartifice.primalmagick.common.entities.pixies.companions;

import com.verdantartifice.primalmagick.common.entities.pixies.IBasicPixie;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.SpawnEggItem;
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
    protected SpawnEggItem getSpawnItem() {
        return ItemsPM.BASIC_MOON_PIXIE.get();
    }
}
