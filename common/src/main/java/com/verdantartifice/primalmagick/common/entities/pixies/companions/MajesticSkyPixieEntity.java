package com.verdantartifice.primalmagick.common.entities.pixies.companions;

import com.verdantartifice.primalmagick.common.entities.pixies.IMajesticPixie;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * Definition of a majestic sky pixie.  Greatest of the sky pixies.
 * 
 * @author Daedalus4096
 */
public class MajesticSkyPixieEntity extends AbstractSkyPixieEntity implements IMajesticPixie {
    public MajesticSkyPixieEntity(EntityType<? extends AbstractPixieEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected @NotNull SpawnEggItem getSpawnItem() {
        return ItemsPM.MAJESTIC_SKY_PIXIE.get();
    }
}
