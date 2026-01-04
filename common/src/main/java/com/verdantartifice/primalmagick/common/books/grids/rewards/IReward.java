package com.verdantartifice.primalmagick.common.books.grids.rewards;

import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

/**
 * Abstraction interface intended for use in determining rewards for linguistics comprehension grids.
 * 
 * @author Daedalus4096
 */
public interface IReward {
    void grant(ServerPlayer player, RegistryAccess registryAccess);
    Component getDescription(Player player, RegistryAccess registryAccess);
    Identifier getIconLocation(Player player);
    Optional<Component> getAmountText();
}
