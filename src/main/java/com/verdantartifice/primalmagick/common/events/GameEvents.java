package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.stats.StatsPM;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.event.VanillaGameEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handler that listens for vanilla game events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagick.MODID)
public class GameEvents {
    @SubscribeEvent(priority=EventPriority.LOWEST)
    public static void onGameEventLowest(VanillaGameEvent event) {
        if (!event.isCanceled() && event.getCause() instanceof Player player && event.getVanillaEvent() == GameEvent.SHEAR) {
            // Grant appropriate stat when the player shears something
            StatsManager.incrementValue(player, StatsPM.SHEARS_USED);
        }
    }
}
