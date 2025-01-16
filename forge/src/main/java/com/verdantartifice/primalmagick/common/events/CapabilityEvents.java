package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.capabilities.PlayerArcaneRecipeBookForge;
import com.verdantartifice.primalmagick.common.capabilities.PlayerAttunementsForge;
import com.verdantartifice.primalmagick.common.capabilities.PlayerCompanionsForge;
import com.verdantartifice.primalmagick.common.capabilities.PlayerCooldownsForge;
import com.verdantartifice.primalmagick.common.capabilities.PlayerKnowledgeForge;
import com.verdantartifice.primalmagick.common.capabilities.PlayerLinguisticsForge;
import com.verdantartifice.primalmagick.common.capabilities.PlayerStatsForge;
import com.verdantartifice.primalmagick.common.capabilities.PlayerWardForge;
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
@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class CapabilityEvents {
    @SubscribeEvent
    public static void attachEntityCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            // Only attach these capabilities to players, not other types of entities
            event.addCapability(PlayerKnowledgeForge.Provider.NAME, new PlayerKnowledgeForge.Provider());
            event.addCapability(PlayerCooldownsForge.Provider.NAME, new PlayerCooldownsForge.Provider());
            event.addCapability(PlayerStatsForge.Provider.NAME, new PlayerStatsForge.Provider());
            event.addCapability(PlayerAttunementsForge.Provider.NAME, new PlayerAttunementsForge.Provider());
            event.addCapability(PlayerCompanionsForge.Provider.NAME, new PlayerCompanionsForge.Provider());
            event.addCapability(PlayerArcaneRecipeBookForge.Provider.NAME, new PlayerArcaneRecipeBookForge.Provider(player.level().getRecipeManager()));
            event.addCapability(PlayerWardForge.Provider.NAME, new PlayerWardForge.Provider());
            event.addCapability(PlayerLinguisticsForge.Provider.NAME, new PlayerLinguisticsForge.Provider());
        }
    }
    
    @SubscribeEvent
    public static void attachWorldCapability(AttachCapabilitiesEvent<Level> event) {
        event.addCapability(WorldEntitySwappers.Provider.NAME, new WorldEntitySwappers.Provider());
    }
}
