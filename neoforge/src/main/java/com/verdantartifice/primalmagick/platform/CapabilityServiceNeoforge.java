package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.capabilities.CapabilitiesNeoforge;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerAttunements;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerCompanions;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerCooldowns;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerStats;
import com.verdantartifice.primalmagick.platform.services.ICapabilityService;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public class CapabilityServiceNeoforge implements ICapabilityService {
    @Override
    public Optional<IPlayerKnowledge> knowledge(Player player) {
        return player == null ? Optional.empty() : Optional.of(player.getData(CapabilitiesNeoforge.KNOWLEDGE));
    }

    @Override
    public Optional<IPlayerCooldowns> cooldowns(Player player) {
        return player == null ? Optional.empty() : Optional.of(player.getData(CapabilitiesNeoforge.COOLDOWNS));
    }

    @Override
    public Optional<IPlayerStats> stats(Player player) {
        return player == null ? Optional.empty() : Optional.of(player.getData(CapabilitiesNeoforge.STATS));
    }

    @Override
    public Optional<IPlayerAttunements> attunements(Player player) {
        return player == null ? Optional.empty() : Optional.of(player.getData(CapabilitiesNeoforge.ATTUNEMENTS));
    }

    @Override
    public Optional<IPlayerCompanions> companions(Player player) {
        return player == null ? Optional.empty() : Optional.of(player.getData(CapabilitiesNeoforge.COMPANIONS));
    }
}
