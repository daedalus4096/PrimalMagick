package com.verdantartifice.primalmagick.common.tiles.misc;

import java.util.Optional;
import java.util.OptionalInt;

import com.verdantartifice.primalmagick.common.capabilities.ItemStackHandlerPM;
import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTileSidedInventoryPM;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Definition of a carved bookshelf tile entity.  Provides the storage for a carved bookshelf block.  Like
 * a chiseled bookshelf, but able to be assigned a loot table for contents generation during worldgen.
 * 
 * @author Daedalus4096
 */
public class CarvedBookshelfTileEntity extends AbstractTileSidedInventoryPM {
    protected static final int INPUT_INV_INDEX = 0;
    protected static final int BOOK_CAPACITY = 6;
    
    public CarvedBookshelfTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.CARVED_BOOKSHELF.get(), pos, state);
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
    protected OptionalInt getInventoryIndexForFace(Direction face) {
        return OptionalInt.of(INPUT_INV_INDEX);
    }

    @Override
    protected NonNullList<ItemStackHandler> createHandlers() {
        NonNullList<ItemStackHandler> retVal = NonNullList.withSize(this.getInventoryCount(), new ItemStackHandlerPM(this));
        
        // Create input handler
        retVal.set(INPUT_INV_INDEX, new ItemStackHandlerPM(this.inventories.get(INPUT_INV_INDEX), this) {
            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }

            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return stack.is(ItemTags.BOOKSHELF_BOOKS);
            }

            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                CarvedBookshelfTileEntity.this.updateState(slot);
            }
        });
        
        return retVal;
    }

    @Override
    protected void loadLegacyItems(NonNullList<ItemStack> legacyItems) {
        // Nothing to do; block didn't exist in legacy versions
    }

    @Override
    public void onLoad() {
        this.unpackLootTable(null);
        super.onLoad();
    }

    @Override
    public Optional<IItemHandlerModifiable> getTargetRandomizedInventory() {
        return Optional.ofNullable(this.itemHandlers.get(INPUT_INV_INDEX));
    }

    protected void updateState(int slot) {
        // TODO Auto-generated method stub
        LOGGER.debug("Updating block state for slot {} of carved bookshelf at {}", slot, this.getBlockPos());
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
