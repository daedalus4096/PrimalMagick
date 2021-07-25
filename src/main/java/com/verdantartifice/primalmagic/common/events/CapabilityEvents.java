package com.verdantartifice.primalmagic.common.events;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.capabilities.PlayerAttunements;
import com.verdantartifice.primalmagic.common.capabilities.PlayerCompanions;
import com.verdantartifice.primalmagic.common.capabilities.PlayerCooldowns;
import com.verdantartifice.primalmagic.common.capabilities.PlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.PlayerStats;
import com.verdantartifice.primalmagic.common.capabilities.WorldEntitySwappers;

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
@Mod.EventBusSubscriber(modid=PrimalMagic.MODID)
public class CapabilityEvents {
    @SubscribeEvent
    public static void attachEntityCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            // Only attach these capabilities to players, not other types of entities
            event.addCapability(PlayerKnowledge.Provider.NAME, new PlayerKnowledge.Provider());
            event.addCapability(PlayerCooldowns.Provider.NAME, new PlayerCooldowns.Provider());
            event.addCapability(PlayerStats.Provider.NAME, new PlayerStats.Provider());
            event.addCapability(PlayerAttunements.Provider.NAME, new PlayerAttunements.Provider());
            event.addCapability(PlayerCompanions.Provider.NAME, new PlayerCompanions.Provider());
        }
    }
    
    @SubscribeEvent
    public static void attachWorldCapability(AttachCapabilitiesEvent<Level> event) {
        event.addCapability(WorldEntitySwappers.Provider.NAME, new WorldEntitySwappers.Provider());
    }
}
