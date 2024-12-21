package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.capabilities.ItemStackHandlerPMForge;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTilePM;
import com.verdantartifice.primalmagick.common.util.InvWrapperPMForge;
import com.verdantartifice.primalmagick.common.util.SidedInvWrapperPMForge;
import com.verdantartifice.primalmagick.platform.services.IItemHandlerService;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.VanillaInventoryCodeHooks;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ItemHandlerServiceForge implements IItemHandlerService {
    @Override
    public IItemHandlerPM create(@Nullable AbstractTilePM tile) {
        return new ItemStackHandlerPMForge(tile);
    }

    @Override
    public IItemHandlerPM create(int size, @Nullable AbstractTilePM tile) {
        return new ItemStackHandlerPMForge(size, tile);
    }

    @Override
    public IItemHandlerPM create(NonNullList<ItemStack> stacks, @Nullable AbstractTilePM tile) {
        return new ItemStackHandlerPMForge(stacks, tile);
    }

    @Override
    public IItemHandlerPM.Builder builder(NonNullList<ItemStack> stacks, @Nullable AbstractTilePM tile) {
        return ItemStackHandlerPMForge.builder(stacks, tile);
    }

    @Override
    public IItemHandlerPM wrap(Container container, @Nullable Direction side) {
        if (container instanceof WorldlyContainer worldlyContainer) {
            return new SidedInvWrapperPMForge(worldlyContainer, side);
        } else {
            return new InvWrapperPMForge(container);
        }
    }

    @Override
    public IItemHandlerPM get(@NotNull Level level, @NotNull BlockPos pos, @Nullable Direction side) {
        Optional<Pair<IItemHandler, Object>> optional = VanillaInventoryCodeHooks.getItemHandler(level, pos.getX(), pos.getY(), pos.getZ(), side);
        Pair<IItemHandler, Object> pair = optional.orElse(null);
        if (pair != null && pair.getLeft() != null) {
            // If the tile entity directly provides an item handler capability, return that
            return new ItemStackHandlerPMForge(pair.getLeft(), null);
        } else {
            if (level.getBlockEntity(pos) instanceof Container container) {
                // If the tile entity does not provide an item handler but does have an inventory, return a wrapper around that
                return this.wrap(container, side);
            } else {
                // If the tile entity does not have an inventory at all, return null
                return null;
            }
        }
    }
}
