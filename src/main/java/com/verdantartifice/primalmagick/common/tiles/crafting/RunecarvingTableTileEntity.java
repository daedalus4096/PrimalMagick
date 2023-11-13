package com.verdantartifice.primalmagick.common.tiles.crafting;

import java.util.OptionalInt;

import com.verdantartifice.primalmagick.common.capabilities.ItemStackHandlerPM;
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
        return switch (face) {
            case DOWN -> OptionalInt.empty();
            default -> OptionalInt.of(INPUT_INV_INDEX);
        };
    }

    @Override
    protected NonNullList<ItemStackHandler> createHandlers() {
        NonNullList<ItemStackHandler> retVal = NonNullList.withSize(this.getInventoryCount(), new ItemStackHandlerPM(this));
        
        // Create input handler
        retVal.set(INPUT_INV_INDEX, new ItemStackHandlerPM(this.inventories.get(INPUT_INV_INDEX), this) {
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
        // Slot 0 was the slab item stack
        this.setItem(INPUT_INV_INDEX, 0, legacyItems.get(0));
        
        // Slot 1 was the lapis item stack
        this.setItem(INPUT_INV_INDEX, 1, legacyItems.get(1));
    }
}
