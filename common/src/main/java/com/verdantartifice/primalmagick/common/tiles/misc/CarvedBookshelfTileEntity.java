package com.verdantartifice.primalmagick.common.tiles.misc;

import com.verdantartifice.primalmagick.common.blocks.misc.CarvedBookshelfBlock;
import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.tiles.BlockEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTileSidedInventoryPM;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

/**
 * Definition of a carved bookshelf tile entity.  Provides the storage for a carved bookshelf block.  Like
 * a chiseled bookshelf, but able to be assigned a loot table for contents generation during worldgen.
 * 
 * @author Daedalus4096
 */
public abstract class CarvedBookshelfTileEntity extends AbstractTileSidedInventoryPM {
    private static final Logger LOGGER = LogManager.getLogger();

    protected static final int INPUT_INV_INDEX = 0;
    protected static final int BOOK_CAPACITY = CarvedBookshelfBlock.MAX_BOOKS;

    private int lastInteractedSlot = -1;

    public CarvedBookshelfTileEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypesPM.CARVED_BOOKSHELF.get(), pos, state);
    }
    
    public int getLastInteractedSlot() {
        return this.lastInteractedSlot;
    }

    @Override
    protected int getInventoryCount() {
        return 1;
    }

    @Override
    protected int getInventorySize(int inventoryIndex) {
        return switch (inventoryIndex) {
            case INPUT_INV_INDEX -> BOOK_CAPACITY;
            default -> 0;
        };
    }

    @Override
    public Optional<Integer> getInventoryIndexForFace(@NotNull Direction face) {
        return Optional.of(INPUT_INV_INDEX);
    }

    @Override
    protected NonNullList<IItemHandlerPM> createHandlers() {
        NonNullList<IItemHandlerPM> retVal = NonNullList.withSize(this.getInventoryCount(), Services.ITEM_HANDLERS.create(this));
        
        // Create input handler
        retVal.set(INPUT_INV_INDEX, Services.ITEM_HANDLERS.builder(this.inventories.get(INPUT_INV_INDEX), this)
                .slotLimitFunction(slot -> 1)
                .itemValidFunction((slot, stack) -> stack.is(ItemTags.BOOKSHELF_BOOKS))
                .contentsChangedFunction(CarvedBookshelfTileEntity.this::updateState)
                .build());
        
        return retVal;
    }

    @Override
    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        this.lastInteractedSlot = pTag.getInt("LastInteractedSlot");
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        pTag.putInt("LastInteractedSlot", this.lastInteractedSlot);
    }

    @Override
    public Optional<IItemHandlerPM> getTargetRandomizedInventory() {
        return Optional.ofNullable(this.itemHandlers.get(INPUT_INV_INDEX));
    }

    protected void updateState(int slot) {
        if (slot >= 0 && slot < BOOK_CAPACITY) {
            // Update the last interacted slot for redstone signal purposes
            this.lastInteractedSlot = slot;
            
            // Update all slot block states based on current item occupancy
            BlockState state = this.getBlockState();
            for (int index = 0; index < CarvedBookshelfBlock.SLOT_OCCUPIED_PROPERTIES.size(); index++) {
                state = state.setValue(CarvedBookshelfBlock.SLOT_OCCUPIED_PROPERTIES.get(index), !this.getItem(INPUT_INV_INDEX, index).isEmpty());
            }
            Objects.requireNonNull(this.level).setBlock(this.worldPosition, state, Block.UPDATE_ALL);
            
            // Fire a game event that the block has changed
            this.level.gameEvent(GameEvent.BLOCK_CHANGE, this.worldPosition, GameEvent.Context.of(state));
        } else {
            LOGGER.error("Expected slot 0-{}, got {}", BOOK_CAPACITY - 1, slot);
        }
    }
    
    public void addBook(int slot, ItemStack bookStack) {
        if (bookStack.is(ItemTags.BOOKSHELF_BOOKS) && this.getItem(INPUT_INV_INDEX, slot).isEmpty()) {
            this.setItem(INPUT_INV_INDEX, slot, bookStack);
        }
    }
    
    public ItemStack removeBook(int slot) {
        return this.removeItem(INPUT_INV_INDEX, slot, 1);
    }
}
