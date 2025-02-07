package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.platform.services.IBlockStateService;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.Nullable;

public class BlockStateServiceNeoforge implements IBlockStateService {
    @Override
    public float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
        return state.getExplosionResistance(level, pos, explosion);
    }

    @Override
    public int getExpDrop(BlockState state, Level level, BlockPos pos, @Nullable Entity breaker, ItemStack tool) {
        return state.getExpDrop(level, pos, level.getBlockEntity(pos), breaker, tool);
    }

    @Override
    public boolean canHarvestBlock(BlockState state, BlockGetter level, BlockPos pos, Player player) {
        return state.canHarvestBlock(level, pos, player);
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        return state.onDestroyedByPlayer(level, pos, player, willHarvest, fluid);
    }

    @Override
    public @Nullable BlockState getShearsModifiedState(BlockState state, UseOnContext context, boolean simulate) {
        for (ItemAbility ability : ItemAbilities.DEFAULT_SHEARS_ACTIONS) {
            BlockState modifiedState = state.getToolModifiedState(context, ability, simulate);
            if (modifiedState != null) {
                return modifiedState;
            }
        }
        return null;
    }
}
