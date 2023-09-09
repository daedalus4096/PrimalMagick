package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagick.common.capabilities.PlayerArcaneRecipeBook;
import com.verdantartifice.primalmagick.common.capabilities.PlayerAttunements;
import com.verdantartifice.primalmagick.common.capabilities.PlayerCompanions;
import com.verdantartifice.primalmagick.common.capabilities.PlayerCooldowns;
import com.verdantartifice.primalmagick.common.capabilities.PlayerKnowledge;
import com.verdantartifice.primalmagick.common.capabilities.PlayerLinguistics;
import com.verdantartifice.primalmagick.common.capabilities.PlayerStats;
import com.verdantartifice.primalmagick.common.capabilities.PlayerWard;
import com.verdantartifice.primalmagick.common.capabilities.WorldEntitySwappers;
import com.verdantartifice.primalmagick.common.items.armor.WardingModuleItem;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handlers for capability-related events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagick.MODID)
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
    
    @SubscribeEvent
    public static void attachItemStackCapability(AttachCapabilitiesEvent<ItemStack> event) {
        if (WardingModuleItem.hasWardAttached(event.getObject())) {
            // Only attach these capabilities to certain item stacks, not all of them
            event.addCapability(ManaStorage.Provider.NAME, new ManaStorage.Provider(WardingModuleItem.MANA_CAPACITY, WardingModuleItem.CHARGE_RATE, WardingModuleItem.REGEN_COST, Source.EARTH));
        }
    }
}
