package com.verdantartifice.primalmagick.common.tiles.devices;

import com.verdantartifice.primalmagick.common.capabilities.CapabilitiesForge;
import com.verdantartifice.primalmagick.common.capabilities.FluidHandlerPMForge;
import com.verdantartifice.primalmagick.common.capabilities.IFluidHandlerPM;
import com.verdantartifice.primalmagick.common.capabilities.IManaStorage;
import com.verdantartifice.primalmagick.common.tiles.IHasFluidHandlerCapabilityForge;
import com.verdantartifice.primalmagick.common.tiles.IHasItemHandlerCapabilityForge;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.EmptyFluidHandler;
import net.minecraftforge.items.IItemHandler;

public class DesalinatorTileEntityForge extends DesalinatorTileEntity implements IHasItemHandlerCapabilityForge, IHasFluidHandlerCapabilityForge {
    protected LazyOptional<IManaStorage<?>> manaStorageOpt = LazyOptional.of(() -> this.manaStorage);
    protected final NonNullList<LazyOptional<IFluidHandler>> fluidHandlerOpts;
    protected final NonNullList<LazyOptional<IItemHandler>> itemHandlerOpts;

    public DesalinatorTileEntityForge(BlockPos pos, BlockState state) {
        super(pos, state);
        this.itemHandlerOpts = this.makeItemHandlerOpts(this.itemHandlers);
        this.fluidHandlerOpts = this.makeFluidHandlerOpts(NonNullList.of(FluidHandlerPMForge.EMPTY, this.waterTank));
    }

    @Override
    public NonNullList<LazyOptional<IItemHandler>> getItemHandlerOpts() {
        return this.itemHandlerOpts;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.doInventorySync();
        this.boilTimeTotal = this.getBoilTimeTotal();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (this.remove) {
            return super.getCapability(cap, side);
        } else {
            if (cap == CapabilitiesForge.MANA_STORAGE) {
                return this.manaStorageOpt.cast();
            } else if (cap == ForgeCapabilities.FLUID_HANDLER) {
                return this.fluidHandlerOpts.getFirst().cast();
            } else if (cap == ForgeCapabilities.ITEM_HANDLER) {
                return this.getItemHandlerCapability(side).cast();
            } else {
                return super.getCapability(cap, side);
            }
        }
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.itemHandlerOpts.forEach(LazyOptional::invalidate);
        this.manaStorageOpt.invalidate();
    }
}
