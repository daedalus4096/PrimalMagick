package com.verdantartifice.primalmagick.common.tiles.crafting;

import java.util.OptionalInt;

import com.verdantartifice.primalmagick.common.menus.RunecarvingTableMenu;
import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTileSidedInventoryPM;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Definition of a runecarving table tile entity.  Holds the crafting materials for the corresponding block.
 * 
 * @see {@link com.verdantartifice.primalmagick.common.blocks.crafting.RunecarvingTableBlock}
 * @author Daedalus4096
 */
public class RunecarvingTableTileEntity extends AbstractTileSidedInventoryPM implements MenuProvider {
    protected static final int INPUT_INV_INDEX = 0;
//    protected static final int[] SLOTS_FOR_UP = new int[] { 1 };
//    protected static final int[] SLOTS_FOR_DOWN = NULL_SLOTS;
//    protected static final int[] SLOTS_FOR_SIDES = new int[] { 0 };
    
    public RunecarvingTableTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.RUNECARVING_TABLE.get(), pos, state);
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player) {
        return new RunecarvingTableMenu(windowId, playerInv, this.getBlockPos(), this);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
    }

    @Override
    protected int getInventoryCount() {
        return 1;
    }

    @Override
    protected int getInventorySize(int inventoryIndex) {
        return switch (inventoryIndex) {
            case INPUT_INV_INDEX -> 2;
            default -> 0;
        };
    }

    @Override
    protected OptionalInt getInventoryIndexForFace(Direction face) {
        // TODO Auto-generated method stub
        return switch (face) {
            case DOWN -> OptionalInt.empty();
            default -> OptionalInt.of(INPUT_INV_INDEX);
        };
    }

    @Override
    protected NonNullList<ItemStackHandler> createHandlers() {
        NonNullList<ItemStackHandler> retVal = NonNullList.withSize(this.getInventoryCount(), new ItemStackHandler());
        
        // Create input handler
        retVal.set(INPUT_INV_INDEX, new ItemStackHandler(this.inventories.get(INPUT_INV_INDEX)) {
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                if (slot == 0) {
                    return stack.is(Items.STONE_SLAB);
                } else if (slot == 1) {
                    return stack.is(Tags.Items.GEMS_LAPIS);
                } else {
                    return false;
                }
            }
        });
        
        return retVal;
    }

    @Override
    protected void loadLegacyItems(NonNullList<ItemStack> legacyItems) {
        // TODO Auto-generated method stub
        
    }

//    @Override
//    public boolean canPlaceItem(int slotIndex, ItemStack stack) {
//        if (slotIndex == 1) {
//            return stack.is(Tags.Items.GEMS_LAPIS);
//        } else if (slotIndex == 0) {
//            return stack.is(Items.STONE_SLAB);
//        } else {
//            return false;
//        }
//    }
//
//    @Override
//    public int[] getSlotsForFace(Direction side) {
//        if (side == Direction.UP) {
//            return SLOTS_FOR_UP;
//        } else if (side == Direction.DOWN) {
//            return SLOTS_FOR_DOWN;
//        } else {
//            return SLOTS_FOR_SIDES;
//        }
//    }
//
//    @Override
//    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, Direction direction) {
//        return this.canPlaceItem(index, itemStackIn);
//    }
//
//    @Override
//    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
//        return true;
//    }
}
