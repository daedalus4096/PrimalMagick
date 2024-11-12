package com.verdantartifice.primalmagick.common.tiles.base;

import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.items.IItemHandlerChangeListener;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Base class for a tile entity exposing a potentially different inventory on each face, each of
 * which may be synced to the client.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractTileSidedInventoryPM extends AbstractTilePM implements IRandomizableContents {
    private static final Logger LOGGER = LogManager.getLogger();

    protected final NonNullList<NonNullList<ItemStack>> inventories;
    protected final NonNullList<NonNullList<ItemStack>> syncedInventories;
    protected final NonNullList<IItemHandlerPM> itemHandlers;
    protected final NonNullList<List<IItemHandlerChangeListener>> listeners;
    
    protected ResourceKey<LootTable> lootTable;
    protected long lootTableSeed;

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
    
    protected abstract Optional<Integer> getInventoryIndexForFace(@Nonnull Direction face);
    
    protected abstract NonNullList<IItemHandlerPM> createHandlers();

    /**
     * This method is intended to provide access to the block entity item handler for a given face during Neoforge
     * capability registration. Prefer using other accessor methods such as {@link #getItem(int, int)} whenever possible.
     *
     * @param face the face of the block whose item handler to fetch
     * @return the item handler for the given face, or null if one doesn't exist
     */
    public IItemHandlerPM getRawItemHandler(Direction face) {
        return this.getInventoryIndexForFace(face).map(this.itemHandlers::get).orElse(null);
    }

    public void addListener(Direction face, IItemHandlerChangeListener listener) {
        if (face != null) {
            this.getInventoryIndexForFace(face).ifPresent(invIndex -> {
                this.listeners.get(invIndex).add(listener);
            });
        }
    }
    
    public void removeListener(IItemHandlerChangeListener listener) {
        this.listeners.forEach(invListeners -> invListeners.remove(listener));
    }

    @Override
    public void setChanged() {
        super.setChanged();
        for (int index = 0; index < this.getInventoryCount(); index++) {
            final int invIndex = index;
            this.listeners.get(invIndex).forEach(listener -> listener.itemsChanged(this.itemHandlers.get(invIndex)));
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
                    final byte invIndexByte = (byte)invIndex;
                    slotTag.putByte("Inv", invIndexByte);
                    final byte slotIndexByte = (byte)slotIndex;
                    slotTag.putByte("Slot", slotIndexByte);
                    ItemStack.OPTIONAL_CODEC.encodeStart(this.getLevel().registryAccess().createSerializationContext(NbtOps.INSTANCE), stack).resultOrPartial(msg -> {
                        LOGGER.error("Failed to encode inv {} slot {}: {}", invIndexByte, slotIndexByte, msg);
                    }).ifPresent(tag -> slotTag.put("Item", tag));
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
                    ItemStack.OPTIONAL_CODEC.parse(this.getLevel().registryAccess().createSerializationContext(NbtOps.INSTANCE), slotTag.getCompound("Item")).resultOrPartial(msg -> {
                        LOGGER.error("Failed to decode inv {} slot {}: {}", invIndex, slotIndex, msg);
                    }).ifPresent(stack -> this.syncedInventories.get(invIndex).set(slotIndex, stack));
                }
            }
        }
    }

    @Override
    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        for (int invIndex = 0; invIndex < this.getInventoryCount(); invIndex++) {
            this.inventories.get(invIndex).clear();
        }
        if (!this.tryLoadLootTable(pTag)) {
            if (pTag.contains("TileSidedInventoriesPM")) {
                ListTag listTag = pTag.getList("TileSidedInventoriesPM", Tag.TAG_COMPOUND);
                for (int invIndex = 0; invIndex < this.getInventoryCount() && invIndex < listTag.size(); invIndex++) {
                    CompoundTag invTag = listTag.getCompound(invIndex);
                    ContainerHelper.loadAllItems(invTag, this.inventories.get(invIndex), pRegistries);
                }
            }
        }
    }
    
    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        ListTag listTag = new ListTag();
        if (!this.trySaveLootTable(pTag)) {
            for (int invIndex = 0; invIndex < this.getInventoryCount(); invIndex++) {
                CompoundTag invTag = new CompoundTag();
                ContainerHelper.saveAllItems(invTag, this.inventories.get(invIndex), pRegistries);
                listTag.add(invIndex, invTag);
            }
        }
        pTag.put("TileSidedInventoriesPM", listTag);
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
    
    public ItemStack removeItem(int invIndex, int slotIndex, int amount) {
        return this.itemHandlers.get(invIndex).extractItem(slotIndex, amount, false);
    }

    @Override
    public void setLootTable(ResourceKey<LootTable> lootTable, long lootTableSeed) {
        this.lootTable = lootTable;
        this.lootTableSeed = lootTableSeed;
    }

    @Override
    public void unpackLootTable(Player player) {
        if (this.lootTable != null && this.level.getServer() != null) {
            LootTable loot = this.level.getServer().reloadableRegistries().getLootTable(this.lootTable);
            if (player instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.GENERATE_LOOT.trigger(serverPlayer, this.lootTable);
            }
            this.lootTable = null;
            LootParams.Builder paramsBuilder = new LootParams.Builder((ServerLevel)this.level).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(this.worldPosition));
            if (player != null) {
                paramsBuilder.withLuck(player.getLuck()).withParameter(LootContextParams.THIS_ENTITY, player);
            }
            this.getTargetRandomizedInventory().ifPresentOrElse(inv -> {
                loot.fill(Services.ITEM_HANDLERS.wrapAsContainer(inv), paramsBuilder.create(LootContextParamSets.CHEST), this.lootTableSeed);
            }, () -> {
                LOGGER.error("Attempting to unpack loot table into undefined destination!");
            });
        }
    }

    /**
     * Optionally returns the item handler capability into which randomized contents should be placed
     * when this block entity's loot table is unpacked.
     * 
     * @return an optional item handler capability
     */
    protected Optional<IItemHandlerPM> getTargetRandomizedInventory() {
        // Return an empty optional by default, so that block entities that don't need loot table support
        // don't have to override this method.
        return Optional.empty();
    }

    protected boolean tryLoadLootTable(CompoundTag pTag) {
        if (pTag.contains("LootTable", Tag.TAG_STRING)) {
            this.lootTable = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.parse(pTag.getString("LootTable")));
            this.lootTableSeed = pTag.getLong("LootTableSeed");
            return true;
        } else {
            return false;
        }
    }

    protected boolean trySaveLootTable(CompoundTag pTag) {
        if (this.lootTable == null) {
            return false;
        } else {
            pTag.putString("LootTable", this.lootTable.location().toString());
            if (this.lootTableSeed != 0L) {
                pTag.putLong("LootTableSeed", this.lootTableSeed);
            }
            return true;
        }
    }
}
