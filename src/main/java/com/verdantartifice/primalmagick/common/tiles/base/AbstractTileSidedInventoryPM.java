package com.verdantartifice.primalmagick.common.tiles.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

/**
 * Base class for a tile entity exposing a potentially different inventory on each face, each of
 * which may be synced to the client.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractTileSidedInventoryPM extends TilePM {
    protected final NonNullList<NonNullList<ItemStack>> inventories;
    protected final NonNullList<ItemStackHandler> itemHandlers;
    protected final NonNullList<LazyOptional<IItemHandler>> itemHandlerOpts;
    protected final NonNullList<List<ContainerListener>> listeners;
    
    public AbstractTileSidedInventoryPM(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        
        for (Direction dir : Direction.values()) {
            // Validate the outputs of the face-inventory mapping function
            int invIndex = this.getInventoryIndexForFace(dir);
            if (invIndex < 0 || invIndex >= this.getInventoryCount()) {
                throw new IllegalArgumentException("Face inventory mapping yields invalid index for direction " + dir.toString());
            }
        }
        
        // Initialize each inventory to the appropriate size
        this.inventories = NonNullList.withSize(this.getInventoryCount(), NonNullList.create());
        this.itemHandlers = NonNullList.withSize(this.getInventoryCount(), new ItemStackHandler());
        this.itemHandlerOpts = NonNullList.withSize(this.getInventoryCount(), LazyOptional.empty());
        this.listeners = NonNullList.withSize(this.getInventoryCount(), new ArrayList<>());
        for (int index = 0; index < this.getInventoryCount(); index++) {
            NonNullList<ItemStack> inv = NonNullList.withSize(this.getInventorySize(index), ItemStack.EMPTY);
            this.inventories.set(index, inv);
            ItemStackHandler handler = new ItemStackHandler(inv);
            this.itemHandlers.set(index, handler);
            this.itemHandlerOpts.set(index, LazyOptional.of(() -> handler));
        }
    }
    
    protected Set<Integer> getSyncedSlotIndices(int inventoryIndex) {
        // Determine which inventory slots should be synced to the client for a given inventory
        return Collections.emptySet();
    }
    
    public int getInventorySize(Direction face) {
        return this.getInventorySize(this.getInventoryIndexForFace(face));
    }
    
    protected abstract int getInventoryCount();
    
    protected abstract int getInventorySize(int inventoryIndex);
    
    protected abstract int getInventoryIndexForFace(Direction face);

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.itemHandlerOpts.forEach(opt -> opt.invalidate());
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction face) {
        if (!this.remove && cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.itemHandlerOpts.get(this.getInventoryIndexForFace(face)).cast();
        } else {
            return super.getCapability(cap, face);
        }
    }

    public void addListener(Direction face, ContainerListener listener) {
        this.listeners.get(this.getInventoryIndexForFace(face)).add(listener);
    }
    
    public void removeListener(ContainerListener listener) {
        this.listeners.forEach(invListeners -> invListeners.remove(listener));
    }

    @Override
    public void setChanged() {
        super.setChanged();
        for (int index = 0; index < this.getInventoryCount(); index++) {
            RecipeWrapper wrapper = new RecipeWrapper(this.itemHandlers.get(index));
            this.listeners.get(index).forEach(listener -> listener.containerChanged(wrapper));
        }
    }
}
