package com.verdantartifice.primalmagic.common.init;

import com.verdantartifice.primalmagic.common.capabilities.IManaStorage;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerAttunements;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerCompanions;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerCooldowns;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerStats;
import com.verdantartifice.primalmagic.common.capabilities.IWorldEntitySwappers;

import net.minecraftforge.common.capabilities.CapabilityManager;

/**
 * Point of registration for mod capabilities.
 * 
 * @author Daedalus4096
 */
public class InitCapabilities {
    public static void initCapabilities() {
        CapabilityManager.INSTANCE.register(IPlayerKnowledge.class);
        CapabilityManager.INSTANCE.register(IPlayerCooldowns.class);
        CapabilityManager.INSTANCE.register(IPlayerStats.class);
        CapabilityManager.INSTANCE.register(IPlayerAttunements.class);
        CapabilityManager.INSTANCE.register(IPlayerCompanions.class);
        CapabilityManager.INSTANCE.register(IWorldEntitySwappers.class);
        CapabilityManager.INSTANCE.register(IManaStorage.class);
    }
}
