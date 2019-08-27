package com.verdantartifice.primalmagic.common.init;

import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.PlayerKnowledge;

import net.minecraftforge.common.capabilities.CapabilityManager;

public class InitCapabilities {
    public static void initCapabilities() {
        CapabilityManager.INSTANCE.register(IPlayerKnowledge.class, new PlayerKnowledge.Storage(), new PlayerKnowledge.Factory());
    }
}
