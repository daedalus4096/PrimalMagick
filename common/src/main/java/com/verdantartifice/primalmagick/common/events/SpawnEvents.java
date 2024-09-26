package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.entities.treefolk.TreefolkEntity;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handlers for entity spawning events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid= Constants.MOD_ID)
public class SpawnEvents {
    @SubscribeEvent
    public static void onRegisterSpawnPlacement(SpawnPlacementRegisterEvent event) {
        event.register(EntityTypesPM.TREEFOLK.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TreefolkEntity::canSpawnOn, SpawnPlacementRegisterEvent.Operation.OR);
    }
}
