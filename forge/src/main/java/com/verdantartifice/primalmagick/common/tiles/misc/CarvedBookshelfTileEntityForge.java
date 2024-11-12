package com.verdantartifice.primalmagick.common.tiles.misc;

import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;

public class CarvedBookshelfTileEntityForge extends CarvedBookshelfTileEntity {
    protected final NonNullList<LazyOptional<IItemHandlerPM>> itemHandlerOpts;

    public CarvedBookshelfTileEntityForge(BlockPos pos, BlockState state) {
        super(pos, state);
        this.itemHandlerOpts = NonNullList.withSize(this.getInventoryCount(), LazyOptional.empty());
        for (int index = 0; index < this.itemHandlers.size(); index++) {
            final int optIndex = index;
            this.itemHandlerOpts.set(index, LazyOptional.of(() -> this.itemHandlers.get(optIndex)));
        }
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.itemHandlerOpts.forEach(opt -> opt.invalidate());
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction face) {
        if (!this.remove && cap == ForgeCapabilities.ITEM_HANDLER) {
            if (face != null && this.getInventoryIndexForFace(face).isPresent()) {
                return this.itemHandlerOpts.get(this.getInventoryIndexForFace(face).get()).cast();
            } else {
                return LazyOptional.empty();
            }
        } else {
            return super.getCapability(cap, face);
        }
    }

    @Override
    public void onLoad() {
        this.unpackLootTable(null);
        super.onLoad();
        this.doInventorySync();
    }
}
