package com.verdantartifice.primalmagic.common.crafting;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IWandTransform {
    public boolean isValid(World world, PlayerEntity player, BlockPos pos);
    public void execute(World world, PlayerEntity player, BlockPos pos);
}
