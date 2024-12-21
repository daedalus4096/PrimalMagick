package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.capabilities.ItemStackHandlerPMNeoforge;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTilePM;
import com.verdantartifice.primalmagick.common.util.InvWrapperPMNeoforge;
import com.verdantartifice.primalmagick.common.util.SidedInvWrapperPMNeoforge;
import com.verdantartifice.primalmagick.platform.services.IItemHandlerService;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemHandlerServiceNeoforge implements IItemHandlerService {
    @Override
    public IItemHandlerPM create(@Nullable AbstractTilePM tile) {
        return new ItemStackHandlerPMNeoforge(tile);
    }

    @Override
    public IItemHandlerPM create(int size, @Nullable AbstractTilePM tile) {
        return new ItemStackHandlerPMNeoforge(size, tile);
    }

    @Override
    public IItemHandlerPM create(NonNullList<ItemStack> stacks, @Nullable AbstractTilePM tile) {
        return new ItemStackHandlerPMNeoforge(stacks, tile);
    }

    @Override
    public IItemHandlerPM.Builder builder(NonNullList<ItemStack> stacks, @Nullable AbstractTilePM tile) {
        return ItemStackHandlerPMNeoforge.builder(stacks, tile);
    }

    @Override
    public IItemHandlerPM wrap(Container container, @Nullable Direction side) {
        if (container instanceof WorldlyContainer worldlyContainer) {
            return new SidedInvWrapperPMNeoforge(worldlyContainer, side);
        } else {
            return new InvWrapperPMNeoforge(container);
        }
    }

    @Override
    public IItemHandlerPM get(@NotNull Level level, @NotNull BlockPos pos, @Nullable Direction side) {
        IItemHandler neoforgeHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, pos, side);
        if (neoforgeHandler != null) {
            // If the tile entity directly provides an item handler capability, return that
            return new ItemStackHandlerPMNeoforge(neoforgeHandler, null);
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
