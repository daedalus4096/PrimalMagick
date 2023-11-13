package com.verdantartifice.primalmagick.common.tiles.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.capabilities.ItemStackHandlerPM;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

/**
 * Base class for a tile entity containing an inventory which may be synced to the client.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractTileInventoryPM extends AbstractTilePM {
    protected NonNullList<ItemStack> items;         // The tile's inventory
    protected NonNullList<ItemStack> syncedItems;   // Client-side inventory data received from the server
    protected List<ContainerListener> listeners;    // Listeners to be informed when the inventory contents change
    protected final Set<Integer> syncedSlotIndices; // Which slots of the inventory should be synced to the client
    protected final int inventorySize;              // Number of slots in the tile's inventory

    protected ItemStackHandlerPM itemHandler;         // Forge item handler capability instance
    protected final LazyOptional<IItemHandler> itemHandlerOpt = LazyOptional.of(() -> this.itemHandler);

    public AbstractTileInventoryPM(BlockEntityType<?> type, BlockPos pos, BlockState state, int invSize) {
        super(type, pos, state);
        this.items = NonNullList.withSize(invSize, ItemStack.EMPTY);
        this.syncedItems = NonNullList.withSize(invSize, ItemStack.EMPTY);
        this.syncedSlotIndices = this.getSyncedSlotIndices();
        this.itemHandler = this.createHandler();
        this.inventorySize = invSize;
    }
    
    protected Set<Integer> getSyncedSlotIndices() {
        // Determine which inventory slots should be synced to the client
        return Collections.emptySet();
    }
    
    public int getInventorySize() {
        return this.inventorySize;
    }
    
    protected ItemStackHandlerPM createHandler() {
        return new ItemStackHandlerPM(this.items, this);
    }
    
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.remove && cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.itemHandlerOpt.cast();
        } else {
            return super.getCapability(cap, side);
        }
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.itemHandlerOpt.invalidate();
    }

    public void addListener(ContainerListener listener) {
        if (this.listeners == null) {
            this.listeners = new ArrayList<>();
        }
        this.listeners.add(listener);
    }
    
    public void removeListener(ContainerListener listener) {
        this.listeners.remove(listener);
    }
    
    public ItemStack getItem(int index) {
        return this.itemHandler.getStackInSlot(index);
    }
    
    public ItemStack getSyncedStackInSlot(int index) {
        return this.syncedItems.get(index);
    }
    
    public int getMaxStackSize(int index) {
        return this.itemHandler.getSlotLimit(index);
    }

    public ItemStack removeItem(int index, int count) {
        ItemStack stack = this.itemHandler.extractItem(index, count, false);
        if (!stack.isEmpty() && this.isSyncedSlot(index)) {
            // Sync the inventory change to nearby clients
            this.syncSlots(null);
        }
        this.setChanged();
        return stack;
    }

    public void setItem(int index, ItemStack stack) {
        this.itemHandler.setStackInSlot(index, stack);
        this.setChanged();
        if (this.isSyncedSlot(index)) {
            // Sync the inventory change to nearby clients
            this.syncSlots(null);
        }
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.listeners != null) {
            RecipeWrapper wrapper = new RecipeWrapper(this.itemHandler);
            this.listeners.forEach((listener) -> { listener.containerChanged(wrapper); });
        }
    }

    protected boolean isSyncedSlot(int index) {
        return this.syncedSlotIndices.contains(Integer.valueOf(index));
    }
    
    /**
     * Send the contents of this tile's synced slots to the given player's client.
     * 
     * @param player the player of the client to receive the sync data
     */
    protected void syncSlots(@Nullable ServerPlayer player) {
        if (!this.syncedSlotIndices.isEmpty()) {
            CompoundTag nbt = new CompoundTag();
            ListTag tagList = new ListTag();
            for (int index = 0; index < this.items.size(); index++) {
                ItemStack stack = this.items.get(index);
                if (this.isSyncedSlot(index) && !stack.isEmpty()) {
                    // Only include populated, sync-tagged slots to lessen packet size
                    CompoundTag slotTag = new CompoundTag();
                    slotTag.putByte("Slot", (byte)index);
                    stack.save(slotTag);
                    tagList.add(slotTag);
                }
            }
            nbt.put("ItemsSynced", tagList);
            this.sendMessageToClient(nbt, player);
        }
    }
    
    @Override
    public void syncTile(boolean rerender) {
        super.syncTile(rerender);
        this.syncSlots(null);
    }
    
    @Override
    public void onMessageFromClient(CompoundTag nbt, ServerPlayer player) {
        super.onMessageFromClient(nbt, player);
        if (nbt.contains("RequestSync")) {
            // If the message was a request for a sync, send one to just that player's client
            this.syncSlots(player);
        }
    }
    
    @Override
    public void onMessageFromServer(CompoundTag nbt) {
        // If the message was a data sync, load the data into the sync inventory
        super.onMessageFromServer(nbt);
        if (nbt.contains("ItemsSynced")) {
            this.syncedItems = NonNullList.withSize(this.itemHandler.getSlots(), ItemStack.EMPTY);
            ListTag tagList = nbt.getList("ItemsSynced", Tag.TAG_COMPOUND);
            for (int index = 0; index < tagList.size(); index++) {
                CompoundTag slotTag = tagList.getCompound(index);
                byte slotIndex = slotTag.getByte("Slot");
                if (this.isSyncedSlot(slotIndex)) {
                    this.syncedItems.set(slotIndex, ItemStack.of(slotTag));
                }
            }
        }
    }
    
    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.items.clear();
        ContainerHelper.loadAllItems(compound, this.items);
    }
    
    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        ContainerHelper.saveAllItems(compound, this.items);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.doInventorySync();
    }
    
    protected void doInventorySync() {
        if (!this.level.isClientSide) {
            // When first loaded, server-side tiles should immediately sync their contents to all nearby clients
            this.syncSlots(null);
        } else {
            // When first loaded, client-side tiles should request a sync from the server
            CompoundTag nbt = new CompoundTag();
            nbt.putBoolean("RequestSync", true);
            this.sendMessageToServer(nbt);
        }
    }
    
    public void dropContents(Level level, BlockPos pos) {
        Containers.dropContents(level, pos, this.items);
    }
}
