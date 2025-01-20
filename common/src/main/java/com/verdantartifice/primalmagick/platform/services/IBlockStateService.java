package com.verdantartifice.primalmagick.platform.services;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

import javax.annotation.Nullable;

public interface IBlockStateService {
    float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion);
    int getExpDrop(BlockState state, Level level, BlockPos pos, @Nullable Entity breaker, ItemStack tool);
    boolean canHarvestBlock(BlockState state, BlockGetter level, BlockPos pos, Player player);
    boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid);
}
