package com.verdantartifice.primalmagic.common.init;

import com.verdantartifice.primalmagic.common.capabilities.IPlayerAttunements;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerCompanions;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerCooldowns;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerStats;
import com.verdantartifice.primalmagic.common.capabilities.IManaStorage;
import com.verdantartifice.primalmagic.common.capabilities.IWorldEntitySwappers;
import com.verdantartifice.primalmagic.common.capabilities.PlayerAttunements;
import com.verdantartifice.primalmagic.common.capabilities.PlayerCompanions;
import com.verdantartifice.primalmagic.common.capabilities.PlayerCooldowns;
import com.verdantartifice.primalmagic.common.capabilities.PlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.PlayerStats;
import com.verdantartifice.primalmagic.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagic.common.capabilities.WorldEntitySwappers;

import net.minecraftforge.common.capabilities.CapabilityManager;

/**
 * Point of registration for mod capabilities.
 * 
 * @author Daedalus4096
 */
public class InitCapabilities {
    public static void initCapabilities() {
        CapabilityManager.INSTANCE.register(IPlayerKnowledge.class, new PlayerKnowledge.Storage(), new PlayerKnowledge.Factory());
        CapabilityManager.INSTANCE.register(IPlayerCooldowns.class, new PlayerCooldowns.Storage(), new PlayerCooldowns.Factory());
        CapabilityManager.INSTANCE.register(IPlayerStats.class, new PlayerStats.Storage(), new PlayerStats.Factory());
        CapabilityManager.INSTANCE.register(IPlayerAttunements.class, new PlayerAttunements.Storage(), new PlayerAttunements.Factory());
        CapabilityManager.INSTANCE.register(IPlayerCompanions.class, new PlayerCompanions.Storage(), new PlayerCompanions.Factory());
        CapabilityManager.INSTANCE.register(IWorldEntitySwappers.class, new WorldEntitySwappers.Storage(), new WorldEntitySwappers.Factory());
        CapabilityManager.INSTANCE.register(IManaStorage.class, new ManaStorage.Storage(), new ManaStorage.Factory());
    }
}
