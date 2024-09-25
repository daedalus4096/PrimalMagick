package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.capabilities.PlayerArcaneRecipeBook;
import com.verdantartifice.primalmagick.common.capabilities.PlayerAttunements;
import com.verdantartifice.primalmagick.common.capabilities.PlayerCompanions;
import com.verdantartifice.primalmagick.common.capabilities.PlayerCooldowns;
import com.verdantartifice.primalmagick.common.capabilities.PlayerKnowledge;
import com.verdantartifice.primalmagick.common.capabilities.PlayerLinguistics;
import com.verdantartifice.primalmagick.common.capabilities.PlayerStats;
import com.verdantartifice.primalmagick.common.capabilities.PlayerWard;
import com.verdantartifice.primalmagick.common.capabilities.WorldEntitySwappers;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handlers for capability-related events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=Constants.MOD_ID)
public class CapabilityEvents {
    @SubscribeEvent
    public static void attachEntityCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            // Only attach these capabilities to players, not other types of entities
            event.addCapability(PlayerKnowledge.Provider.NAME, new PlayerKnowledge.Provider());
            event.addCapability(PlayerCooldowns.Provider.NAME, new PlayerCooldowns.Provider());
            event.addCapability(PlayerStats.Provider.NAME, new PlayerStats.Provider());
            event.addCapability(PlayerAttunements.Provider.NAME, new PlayerAttunements.Provider());
            event.addCapability(PlayerCompanions.Provider.NAME, new PlayerCompanions.Provider());
            event.addCapability(PlayerArcaneRecipeBook.Provider.NAME, new PlayerArcaneRecipeBook.Provider(player.level().getRecipeManager()));
            event.addCapability(PlayerWard.Provider.NAME, new PlayerWard.Provider());
            event.addCapability(PlayerLinguistics.Provider.NAME, new PlayerLinguistics.Provider());
        }
    }
    
    @SubscribeEvent
    public static void attachWorldCapability(AttachCapabilitiesEvent<Level> event) {
        event.addCapability(WorldEntitySwappers.Provider.NAME, new WorldEntitySwappers.Provider());
    }
}
