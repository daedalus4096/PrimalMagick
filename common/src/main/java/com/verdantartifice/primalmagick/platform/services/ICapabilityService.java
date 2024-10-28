package com.verdantartifice.primalmagick.platform.services;

import com.verdantartifice.primalmagick.common.capabilities.IPlayerCooldowns;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerStats;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public interface ICapabilityService {
    Optional<IPlayerCooldowns> cooldowns(Player player);
    Optional<IPlayerStats> stats(Player player);
}
