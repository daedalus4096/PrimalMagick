package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.platform.services.IItemStackService;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ItemStackServiceNeoforge implements IItemStackService {
    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, Player player) {
        // This method does not exist in the NeoForge item stack extension interface, so always return false.
        return false;
    }

    @Override
    public boolean canGrindstoneRepair(ItemStack stack) {
        return stack.canGrindstoneRepair();
    }
}
