package com.verdantartifice.primalmagick.common.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class CapabilitiesForge {
    public static final Capability<IPlayerCooldowns> COOLDOWNS = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<IPlayerStats> STATS = CapabilityManager.get(new CapabilityToken<>(){});
}
