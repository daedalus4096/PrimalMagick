package com.verdantartifice.primalmagic.common.tiles.base;

import java.util.Collections;
import java.util.Set;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;

/**
 * Base class for a tile entity containing an inventory which may be synced to the client.
 * 
 * @author Daedalus4096
 */
public class TileInventoryPM extends TilePM implements ISidedInventory {
    protected NonNullList<ItemStack> items;         // The tile's inventory
    protected NonNullList<ItemStack> syncedItems;   // Client-side inventory data received from the server
    protected final Set<Integer> syncedSlotIndices; // Which slots of the inventory should be synced to the client
    protected final int[] faceSlots;                // The slots of this tile's inventory visible from its sides
    
    public TileInventoryPM(TileEntityType<?> type, int invSize) {
        super(type);
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
    public int getSizeInventory() {
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
    public ItemStack getStackInSlot(int index) {
        return this.items.get(index);
    }
    
    public ItemStack getSyncedStackInSlot(int index) {
        return this.syncedItems.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack stack = ItemStackHelper.getAndSplit(this.items, index, count);
        if (!stack.isEmpty() && this.isSyncedSlot(index)) {
            // Sync the inventory change to nearby clients
            this.syncSlots(null);
        }
        this.markDirty();
        return stack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack stack = ItemStackHelper.getAndRemove(this.items, index);
        if (!stack.isEmpty() && this.isSyncedSlot(index)) {
            // Sync the inventory change to nearby clients
            this.syncSlots(null);
        }
        this.markDirty();
        return stack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.items.set(index, stack);
        if (stack.getCount() > this.getInventoryStackLimit()) {
            // If the input stack is too big, pare it down to the allowed size
            stack.setCount(this.getInventoryStackLimit());
        }
        this.markDirty();
        if (this.isSyncedSlot(index)) {
            // Sync the inventory change to nearby clients
            this.syncSlots(null);
        }
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        if (this.world.getTileEntity(this.pos) != this) {
            return false;
        } else {
            return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void clear() {
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
    public boolean canInsertItem(int index, ItemStack itemStackIn, Direction direction) {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
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
    protected void syncSlots(@Nullable ServerPlayerEntity player) {
        if (!this.syncedSlotIndices.isEmpty()) {
            CompoundNBT nbt = new CompoundNBT();
            ListNBT tagList = new ListNBT();
            for (int index = 0; index < this.items.size(); index++) {
                ItemStack stack = this.items.get(index);
                if (this.isSyncedSlot(index) && !stack.isEmpty()) {
                    // Only include populated, sync-tagged slots to lessen packet size
                    CompoundNBT slotTag = new CompoundNBT();
                    slotTag.putByte("Slot", (byte)index);
                    stack.write(slotTag);
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
    public void onMessageFromClient(CompoundNBT nbt, ServerPlayerEntity player) {
        super.onMessageFromClient(nbt, player);
        if (nbt.contains("RequestSync")) {
            // If the message was a request for a sync, send one to just that player's client
            this.syncSlots(player);
        }
    }
    
    @Override
    public void onMessageFromServer(CompoundNBT nbt) {
        // If the message was a data sync, load the data into the sync inventory
        super.onMessageFromServer(nbt);
        if (nbt.contains("ItemsSynced")) {
            this.syncedItems = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
            ListNBT tagList = nbt.getList("ItemsSynced", Constants.NBT.TAG_COMPOUND);
            for (int index = 0; index < tagList.size(); index++) {
                CompoundNBT slotTag = tagList.getCompound(index);
                byte slotIndex = slotTag.getByte("Slot");
                if (this.isSyncedSlot(slotIndex)) {
                    this.syncedItems.set(slotIndex, ItemStack.read(slotTag));
                }
            }
        }
    }
    
    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.items);
    }
    
    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        ItemStackHelper.saveAllItems(compound, this.items);
        return compound;
    }
    
    @Override
    public void onLoad() {
        super.onLoad();
        if (!this.world.isRemote) {
            // When first loaded, server-side tiles should immediately sync their contents to all nearby clients
            this.syncSlots(null);
        } else {
            // When first loaded, client-side tiles should request a sync from the server
            CompoundNBT nbt = new CompoundNBT();
            nbt.putBoolean("RequestSync", true);
            this.sendMessageToServer(nbt);
        }
    }
    
    @Override
    public void remove() {
        InventoryHelper.dropInventoryItems(this.world, this.pos, this);
        super.remove();
    }
}
