package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.platform.services.IShearableService;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.IShearable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class ShearableServiceNeoforge implements IShearableService {
    @Override
    public boolean isShearable(@NotNull Entity entity, @Nullable Player player, @NotNull ItemStack item, Level level, BlockPos pos) {
        return entity instanceof IShearable shearable && shearable.isShearable(player, item, level, pos);
    }

    @Override
    public List<ItemStack> onSheared(@NotNull Entity entity, @Nullable Player player, @NotNull ItemStack item, Level level, BlockPos pos, int fortune) {
        return entity instanceof IShearable shearable ?
                shearable.onSheared(player, item, level, pos) :
                Collections.emptyList();
    }
}
