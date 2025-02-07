package com.verdantartifice.primalmagick.platform.services;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IShearableService {
    boolean isShearable(@NotNull Entity entity, @Nullable Player player, @NotNull ItemStack item, Level level, BlockPos pos);
    List<ItemStack> onSheared(@NotNull Entity entity, @Nullable Player player, @NotNull ItemStack item, Level level, BlockPos pos, int fortune);
}
