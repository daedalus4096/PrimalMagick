package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.platform.services.IItemStackService;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ItemStackServiceForge implements IItemStackService {
    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, Player player) {
        return stack.onBlockStartBreak(pos, player);
    }
}
