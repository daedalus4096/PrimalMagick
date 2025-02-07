package com.verdantartifice.primalmagick.common.tiles.devices;

import com.verdantartifice.primalmagick.common.capabilities.CapabilitiesForge;
import com.verdantartifice.primalmagick.common.capabilities.IManaStorage;
import com.verdantartifice.primalmagick.common.tiles.IHasItemHandlerCapabilityForge;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

public class HoneyExtractorTileEntityForge extends HoneyExtractorTileEntity implements IHasItemHandlerCapabilityForge {
    protected LazyOptional<IManaStorage<?>> manaStorageOpt = LazyOptional.of(() -> this.manaStorage);
    protected final NonNullList<LazyOptional<IItemHandler>> itemHandlerOpts;

    public HoneyExtractorTileEntityForge(BlockPos pos, BlockState state) {
        super(pos, state);
        this.itemHandlerOpts = this.makeItemHandlerOpts(this.itemHandlers);
    }

    @Override
    public NonNullList<LazyOptional<IItemHandler>> getItemHandlerOpts() {
        return this.itemHandlerOpts;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.doInventorySync();
        this.spinTimeTotal = this.getSpinTimeTotal();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (this.remove) {
            return super.getCapability(cap, side);
        } else {
            if (cap == CapabilitiesForge.MANA_STORAGE) {
                return this.manaStorageOpt.cast();
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
