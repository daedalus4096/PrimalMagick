package com.verdantartifice.primalmagic.common.init;

import com.verdantartifice.primalmagic.common.capabilities.IPlayerCooldowns;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.PlayerCooldowns;
import com.verdantartifice.primalmagic.common.capabilities.PlayerKnowledge;

import net.minecraftforge.common.capabilities.CapabilityManager;

public class InitCapabilities {
    public static void initCapabilities() {
        CapabilityManager.INSTANCE.register(IPlayerKnowledge.class, new PlayerKnowledge.Storage(), new PlayerKnowledge.Factory());
        CapabilityManager.INSTANCE.register(IPlayerCooldowns.class, new PlayerCooldowns.Storage(), new PlayerCooldowns.Factory());
    }
}
