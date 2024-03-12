package com.verdantartifice.primalmagick.common.books.grids.rewards;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Abstraction interface intended for use in determining rewards for linguistics comprehension grids.
 * 
 * @author Daedalus4096
 */
public interface IReward extends INBTSerializable<CompoundTag> {
    void grant(ServerPlayer player);
    Component getDescription();
    ResourceLocation getIconLocation();
    String getRewardType();
    <T extends IReward> IRewardSerializer<T> getSerializer();
}
