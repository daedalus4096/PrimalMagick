package com.verdantartifice.primalmagick.common.entities.pixies.companions;

import com.verdantartifice.primalmagick.common.entities.pixies.IMajesticPixie;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * Definition of a majestic hallowed pixie.  Greatest of the hallowed pixies.
 * 
 * @author Daedalus4096
 */
public class MajesticHallowedPixieEntity extends AbstractHallowedPixieEntity implements IMajesticPixie {
    public MajesticHallowedPixieEntity(EntityType<? extends AbstractPixieEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected @NotNull SpawnEggItem getSpawnItem() {
        return ItemsPM.MAJESTIC_HALLOWED_PIXIE.get();
    }
}
