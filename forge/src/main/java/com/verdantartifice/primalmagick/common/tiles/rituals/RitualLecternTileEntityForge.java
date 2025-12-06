package com.verdantartifice.primalmagick.common.tiles.rituals;

import com.verdantartifice.primalmagick.common.tiles.IHasItemHandlerCapabilityForge;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RitualLecternTileEntityForge extends RitualLecternTileEntity implements IHasItemHandlerCapabilityForge {
    protected final NonNullList<LazyOptional<IItemHandler>> itemHandlerOpts;

    public RitualLecternTileEntityForge(BlockPos pos, BlockState state) {
        super(pos, state);
        this.itemHandlerOpts = this.makeItemHandlerOpts(this.itemHandlers);
    }

    @Override
    public NonNullList<LazyOptional<IItemHandler>> getItemHandlerOpts() {
        return this.itemHandlerOpts;
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.itemHandlerOpts.forEach(LazyOptional::invalidate);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (this.remove) {
            return super.getCapability(cap, side);
        } else {
            if (cap == ForgeCapabilities.ITEM_HANDLER) {
                return this.getItemHandlerCapability(side).cast();
            } else {
                return super.getCapability(cap, side);
            }
        }
    }
}
