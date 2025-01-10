package com.verdantartifice.primalmagick.common.tiles.devices;

import com.verdantartifice.primalmagick.common.capabilities.CapabilitiesForge;
import com.verdantartifice.primalmagick.common.capabilities.IManaStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class DissolutionChamberTileEntityForge extends DissolutionChamberTileEntity {
    protected LazyOptional<IManaStorage<?>> manaStorageOpt = LazyOptional.of(() -> this.manaStorage);

    public DissolutionChamberTileEntityForge(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.processTimeTotal = this.getProcessTimeTotal();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.remove && cap == CapabilitiesForge.MANA_STORAGE) {
            return this.manaStorageOpt.cast();
        } else {
            return super.getCapability(cap, side);
        }
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.manaStorageOpt.invalidate();
    }
}
