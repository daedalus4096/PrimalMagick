package com.verdantartifice.primalmagic.common.tiles.base;

import java.util.Collections;
import java.util.Set;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.Constants;

/**
 * Base class for a tile entity containing an inventory which may be synced to the client.
 * 
 * @author Daedalus4096
 */
public class TileInventoryPM extends TilePM implements WorldlyContainer {
    protected static final Logger LOGGER = LogManager.getLogger();
    
    protected NonNullList<ItemStack> items;         // The tile's inventory
    protected NonNullList<ItemStack> syncedItems;   // Client-side inventory data received from the server
    protected final Set<Integer> syncedSlotIndices; // Which slots of the inventory should be synced to the client
    protected final int[] faceSlots;                // The slots of this tile's inventory visible from its sides
    
    public TileInventoryPM(BlockEntityType<?> type, BlockPos pos, BlockState state, int invSize) {
        super(type, pos, state);
        this.items = NonNullList.withSize(invSize, ItemStack.EMPTY);
        this.syncedItems = NonNullList.withSize(invSize, ItemStack.EMPTY);
        this.syncedSlotIndices = this.getSyncedSlotIndices();
        this.faceSlots = new int[invSize];
        for (int index = 0; index < invSize; index++) {
            this.faceSlots[index] = index;
        }
    }
    
    protected Set<Integer> getSyncedSlotIndices() {
        // Determine which inventory slots should be synced to the client
        return Collections.emptySet();
    }
    
    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.items) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        return this.items.get(index);
    }
    
    public ItemStack getSyncedStackInSlot(int index) {
        return this.syncedItems.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack stack = ContainerHelper.removeItem(this.items, index, count);
        if (!stack.isEmpty() && this.isSyncedSlot(index)) {
            // Sync the inventory change to nearby clients
            this.syncSlots(null);
        }
        this.setChanged();
        return stack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack stack = ContainerHelper.takeItem(this.items, index);
        if (!stack.isEmpty() && this.isSyncedSlot(index)) {
            // Sync the inventory change to nearby clients
            this.syncSlots(null);
        }
        this.setChanged();
        return stack;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        this.items.set(index, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            // If the input stack is too big, pare it down to the allowed size
            stack.setCount(this.getMaxStackSize());
        }
        this.setChanged();
        if (this.isSyncedSlot(index)) {
            // Sync the inventory change to nearby clients
            this.syncSlots(null);
        }
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void clearContent() {
        this.items.clear();
        if (!this.syncedSlotIndices.isEmpty()) {
            this.syncSlots(null);
        }
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return this.faceSlots;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, Direction direction) {
        return this.canPlaceItem(index, itemStackIn);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return true;
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
            this.syncedItems = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
            ListTag tagList = nbt.getList("ItemsSynced", Constants.NBT.TAG_COMPOUND);
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
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compound, this.items);
    }
    
    @Override
    public CompoundTag save(CompoundTag compound) {
        super.save(compound);
        ContainerHelper.saveAllItems(compound, this.items);
        return compound;
    }
    
    @Override
    public void onLoad() {
        super.onLoad();
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
    
    @Override
    public void setRemoved() {
        BlockState state = this.level.getBlockState(this.worldPosition);
        LOGGER.debug("Dropping contents of {} tile entity at {}", state.getBlock().getRegistryName().toString(), this.worldPosition.toString());
        Containers.dropContents(this.level, this.worldPosition, this);
        super.setRemoved();
    }
}
