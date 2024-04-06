package com.verdantartifice.primalmagick.common.books.grids.rewards;

import java.util.Optional;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Abstraction interface intended for use in determining rewards for linguistics comprehension grids.
 * 
 * @author Daedalus4096
 */
public interface IReward extends INBTSerializable<CompoundTag> {
    void grant(ServerPlayer player, RegistryAccess registryAccess);
    Component getDescription(Player player);
    ResourceLocation getIconLocation(Player player);
    Optional<Component> getAmountText();
    String getRewardType();
    <T extends IReward> IRewardSerializer<T> getSerializer();
}
