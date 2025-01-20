package com.verdantartifice.primalmagick.platform.services;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IItemStackService {
    boolean onBlockStartBreak(ItemStack stack, BlockPos pos, Player player);
}
