package com.verdantartifice.primalmagick.common.books.grids.rewards;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/**
 * Abstraction interface intended for use in determining rewards for linguistics comprehension grids.
 * 
 * @author Daedalus4096
 */
public interface IReward {
    void grant(ServerPlayer player);
    Component getDescription();
    ResourceLocation getIconLocation();
    String getRewardType();
    <T extends IReward> IRewardSerializer<T> getSerializer();
}
