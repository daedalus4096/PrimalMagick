package com.verdantartifice.primalmagick.common.theorycrafting.rewards;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

/**
 * Abstraction interface intended for use in determining non-theory rewards for theorycrafting projects.
 * 
 * @author Daedalus4096
 */
public interface IReward {
    void grant(ServerPlayer player);
    Component getDescription();
    String getRewardType();
    <T extends AbstractReward> IRewardSerializer<T> getSerializer();
}
