package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.capabilities.IPlayerCooldowns;
import com.verdantartifice.primalmagick.platform.services.ICapabilityService;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public class CapabilityServiceNeoforge implements ICapabilityService {
    @Override
    public Optional<IPlayerCooldowns> cooldowns(Player player) {
        // TODO Stub
        return Optional.empty();
    }
}
