package com.verdantartifice.primalmagick.common.tiles.devices;

import com.verdantartifice.primalmagick.common.menus.ResearchTableMenu;
import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.TileInventoryPM;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Definition of a research table tile entity.  Holds the writing materials for the corresponding block.
 * 
 * @see {@link com.verdantartifice.primalmagick.common.blocks.devices.ResearchTableBlock}
 * @author Daedalus4096
 */
public class ResearchTableTileEntity extends TileInventoryPM implements MenuProvider {
    protected static final int[] SLOTS_FOR_UP = new int[] { 1 };
    protected static final int[] SLOTS_FOR_DOWN = new int[0];
    protected static final int[] SLOTS_FOR_SIDES = new int[] { 0 };
    
    public ResearchTableTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.RESEARCH_TABLE.get(), pos, state, 2);
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player) {
        ResearchTableMenu menu = new ResearchTableMenu(windowId, playerInv, this.getBlockPos(), this);
        this.addListener(menu);
        return menu;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
    }

//    @Override
//    public boolean canPlaceItem(int slotIndex, ItemStack stack) {
//        if (slotIndex == 1) {
//            return stack.is(Items.PAPER);
//        } else {
//            return stack.getItem() instanceof IWritingImplement;
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
