package com.verdantartifice.primalmagick.common.tiles.crafting;

import com.verdantartifice.primalmagick.common.capabilities.CapabilitiesForge;
import com.verdantartifice.primalmagick.common.capabilities.ITileResearchCache;
import com.verdantartifice.primalmagick.common.tiles.IHasItemHandlerCapabilityForge;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

public abstract class AbstractCalcinatorTileEntityForge extends AbstractCalcinatorTileEntity implements IHasItemHandlerCapabilityForge {
    protected LazyOptional<ITileResearchCache> researchCacheOpt = LazyOptional.of(() -> this.researchCache);
    protected final NonNullList<LazyOptional<IItemHandler>> itemHandlerOpts;

    public AbstractCalcinatorTileEntityForge(BlockEntityType<? extends AbstractCalcinatorTileEntity> tileEntityType, BlockPos pos, BlockState state) {
        super(tileEntityType, pos, state);
        this.itemHandlerOpts = this.makeItemHandlerOpts(this.itemHandlers);
    }

    @Override
    public NonNullList<LazyOptional<IItemHandler>> getItemHandlerOpts() {
        return this.itemHandlerOpts;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!this.level.isClientSide) {
            this.relevantResearch = assembleRelevantResearch();
        }
        this.cookTimeTotal = this.getCookTimeTotal();
    }

    @Override
    protected boolean hasFuelRemainingItem(ItemStack fuelStack) {
        return fuelStack.hasCraftingRemainingItem();
    }

    @Override
    protected ItemStack getFuelRemainingItem(ItemStack fuelStack) {
        return fuelStack.getCraftingRemainingItem();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (this.remove) {
            return super.getCapability(cap, side);
        } else {
            if (cap == CapabilitiesForge.RESEARCH_CACHE) {
                return this.researchCacheOpt.cast();
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
        this.researchCacheOpt.invalidate();
    }
}
