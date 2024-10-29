package com.verdantartifice.primalmagick.platform.services;

import com.verdantartifice.primalmagick.common.capabilities.IPlayerAttunements;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerCompanions;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerCooldowns;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerStats;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public interface ICapabilityService {
    Optional<IPlayerKnowledge> knowledge(Player player);
    Optional<IPlayerCooldowns> cooldowns(Player player);
    Optional<IPlayerStats> stats(Player player);
    Optional<IPlayerAttunements> attunements(Player player);
    Optional<IPlayerCompanions> companions(Player player);
}
