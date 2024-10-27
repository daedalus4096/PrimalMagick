package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.capabilities.CapabilitiesForge;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerCooldowns;
import com.verdantartifice.primalmagick.platform.services.ICapabilityService;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public class CapabilityServiceForge implements ICapabilityService {
    @Override
    public Optional<IPlayerCooldowns> cooldowns(Player player) {
        return player == null ? Optional.empty() : player.getCapability(CapabilitiesForge.COOLDOWNS).resolve();
    }
}
