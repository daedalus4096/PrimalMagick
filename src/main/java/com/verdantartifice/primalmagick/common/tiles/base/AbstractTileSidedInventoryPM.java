package com.verdantartifice.primalmagick.common.tiles.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.OptionalInt;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.mutable.MutableInt;

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
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

/**
 * Base class for a tile entity exposing a potentially different inventory on each face, each of
 * which may be synced to the client.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractTileSidedInventoryPM extends AbstractTilePM {
    protected final NonNullList<NonNullList<ItemStack>> inventories;
    protected final NonNullList<NonNullList<ItemStack>> syncedInventories;
    protected final NonNullList<ItemStackHandler> itemHandlers;
    protected final NonNullList<LazyOptional<IItemHandler>> itemHandlerOpts;
    protected final NonNullList<List<ContainerListener>> listeners;
    
    public AbstractTileSidedInventoryPM(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        
        for (Direction dir : Direction.values()) {
            // Validate the outputs of the face-inventory mapping function
            this.getInventoryIndexForFace(dir).ifPresent(invIndex -> {
                if (invIndex < 0 || invIndex >= this.getInventoryCount()) {
                    throw new IllegalArgumentException("Face inventory mapping yields invalid index for direction " + dir.toString());
                }
            });
        }
        
        // Initialize each inventory to the appropriate size
        this.inventories = NonNullList.withSize(this.getInventoryCount(), NonNullList.create());
        this.syncedInventories = NonNullList.withSize(this.getInventoryCount(), NonNullList.create());
        for (int index = 0; index < this.getInventoryCount(); index++) {
            this.inventories.set(index, NonNullList.withSize(this.getInventorySize(index), ItemStack.EMPTY));
            this.syncedInventories.set(index, NonNullList.withSize(this.getInventorySize(index), ItemStack.EMPTY));
        }
        
        // Create item handler capabilities
        this.itemHandlers = this.createHandlers();
        this.itemHandlerOpts = NonNullList.withSize(this.getInventoryCount(), LazyOptional.empty());
        for (int index = 0; index < this.itemHandlers.size(); index++) {
            final int optIndex = index;
            this.itemHandlerOpts.set(index, LazyOptional.of(() -> this.itemHandlers.get(optIndex)));
        }
        
        // Create container listener list
        this.listeners = NonNullList.withSize(this.getInventoryCount(), new ArrayList<>());
    }
    
    protected Set<Integer> getSyncedSlotIndices(int inventoryIndex) {
        // Determine which inventory slots should be synced to the client for a given inventory
        return Collections.emptySet();
    }
    
    public int getInventorySize(Direction face) {
        MutableInt retVal = new MutableInt(0);
        if (face != null) {
            this.getInventoryIndexForFace(face).ifPresent(invIndex -> {
                retVal.setValue(this.getInventorySize(invIndex));
            });
        }
        return retVal.getValue();
    }
    
    protected abstract int getInventoryCount();
    
    protected abstract int getInventorySize(int inventoryIndex);
    
    protected abstract OptionalInt getInventoryIndexForFace(@Nonnull Direction face);
    
    protected abstract NonNullList<ItemStackHandler> createHandlers();

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.itemHandlerOpts.forEach(opt -> opt.invalidate());
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction face) {
        if (!this.remove && cap == ForgeCapabilities.ITEM_HANDLER) {
            if (face != null && this.getInventoryIndexForFace(face).isPresent()) {
                return this.itemHandlerOpts.get(this.getInventoryIndexForFace(face).getAsInt()).cast();
            } else {
                return LazyOptional.empty();
            }
        } else {
            return super.getCapability(cap, face);
        }
    }

    public void addListener(Direction face, ContainerListener listener) {
        if (face != null) {
            this.getInventoryIndexForFace(face).ifPresent(invIndex -> {
                this.listeners.get(invIndex).add(listener);
            });
        }
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
    
    protected boolean isSyncedSlot(int inventoryIndex, int slotIndex) {
        return this.getSyncedSlotIndices(inventoryIndex).contains(Integer.valueOf(slotIndex));
    }
    
    /**
     * Send the contents of this tile's synced slots to the given player's client.
     * 
     * @param player the player of the client to receive the sync data
     */
    protected void syncSlots(@Nullable ServerPlayer player) {
        ListTag tagList = new ListTag();
        for (int invIndex = 0; invIndex < this.getInventoryCount(); invIndex++) {
            for (int slotIndex = 0; slotIndex < this.getInventorySize(invIndex); slotIndex++) {
                ItemStack stack = this.getItem(invIndex, slotIndex);
                if (this.isSyncedSlot(invIndex, slotIndex) && !stack.isEmpty()) {
                    // Only include populated, sync-tagged slots to lessen packet size
                    CompoundTag slotTag = new CompoundTag();
                    slotTag.putByte("Inv", (byte)invIndex);
                    slotTag.putByte("Slot", (byte)slotIndex);
                    stack.save(slotTag);
                    tagList.add(slotTag);
                }
            }
        }
        CompoundTag nbt = new CompoundTag();
        nbt.put("ItemsSynced", tagList);
        this.sendMessageToClient(nbt, player);
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
            for (int invIndex = 0; invIndex < this.getInventoryCount(); invIndex++) {
                this.syncedInventories.get(invIndex).clear();
            }
            ListTag tagList = nbt.getList("ItemsSynced", Tag.TAG_COMPOUND);
            for (int tagIndex = 0; tagIndex < tagList.size(); tagIndex++) {
                CompoundTag slotTag = tagList.getCompound(tagIndex);
                byte invIndex = slotTag.getByte("Inv");
                byte slotIndex = slotTag.getByte("Slot");
                if (this.isSyncedSlot(invIndex, slotIndex)) {
                    ItemStack stack = ItemStack.of(slotTag);
                    this.syncedInventories.get(invIndex).set(slotIndex, stack);
                }
            }
        }
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        for (int invIndex = 0; invIndex < this.getInventoryCount(); invIndex++) {
            this.inventories.get(invIndex).clear();
        }
        if (pTag.contains("TileSidedInventoriesPM")) {
            ListTag listTag = pTag.getList("TileSidedInventoriesPM", Tag.TAG_COMPOUND);
            for (int invIndex = 0; invIndex < this.getInventoryCount() && invIndex < listTag.size(); invIndex++) {
                CompoundTag invTag = listTag.getCompound(invIndex);
                ContainerHelper.loadAllItems(invTag, this.inventories.get(invIndex));
            }
        } else if (pTag.contains("Items")) {
            // Compatibility layer for tiles that used to be AbstractTileInventoryPM instances pre-4.0.8
            // TODO Remove in next major revision
            int legacySize = 0;
            for (int invIndex = 0; invIndex < this.getInventoryCount(); invIndex++) {
                legacySize += this.getInventorySize(invIndex);
            }
            NonNullList<ItemStack> legacyItems = NonNullList.withSize(legacySize, ItemStack.EMPTY);
            ContainerHelper.loadAllItems(pTag, legacyItems);
            this.loadLegacyItems(legacyItems);
        }
    }
    
    /**
     * Load a given list of AbstractTileInventoryPM-formatted items into the multi-inventory format used by
     * this tile type.  Should only ever be called at most once for any given block entity instance,
     * when it's first loaded into the new format.
     * 
     * @param legacyItems the list of all items loaded for this tile in the legacy format
     */
    // TODO Remove in next major revision
    protected abstract void loadLegacyItems(NonNullList<ItemStack> legacyItems);

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        ListTag listTag = new ListTag();
        for (int invIndex = 0; invIndex < this.getInventoryCount(); invIndex++) {
            CompoundTag invTag = new CompoundTag();
            ContainerHelper.saveAllItems(invTag, this.inventories.get(invIndex));
            listTag.add(invIndex, invTag);
        }
        pTag.put("TileSidedInventoriesPM", listTag);
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
        this.inventories.forEach(inv -> Containers.dropContents(level, pos, inv));
    }
    
    public ItemStack getItem(int invIndex, int slotIndex) {
        return this.inventories.get(invIndex).get(slotIndex);
    }
    
    public ItemStack getSyncedItem(int invIndex, int slotIndex) {
        return this.syncedInventories.get(invIndex).get(slotIndex);
    }
    
    public void setItem(int invIndex, int slotIndex, ItemStack stack) {
        this.itemHandlers.get(invIndex).setStackInSlot(slotIndex, stack);
    }
}
