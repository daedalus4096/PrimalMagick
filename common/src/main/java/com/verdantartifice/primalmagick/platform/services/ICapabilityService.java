package com.verdantartifice.primalmagick.platform.services;

import com.verdantartifice.primalmagick.common.capabilities.IPlayerCooldowns;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public interface ICapabilityService {
    Optional<IPlayerCooldowns> cooldowns(Player player);
}
