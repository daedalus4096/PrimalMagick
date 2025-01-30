package com.verdantartifice.primalmagick.common.tiles.devices;

import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.menus.ResearchTableMenu;
import com.verdantartifice.primalmagick.common.theorycrafting.IWritingImplement;
import com.verdantartifice.primalmagick.common.tiles.BlockEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTileSidedInventoryPM;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

/**
 * Definition of a research table tile entity.  Holds the writing materials for the corresponding block.
 * 
 * @see com.verdantartifice.primalmagick.common.blocks.devices.ResearchTableBlock
 * @author Daedalus4096
 */
public class ResearchTableTileEntity extends AbstractTileSidedInventoryPM implements MenuProvider {
    protected static final int INPUT_INV_INDEX = 0;
    
    public ResearchTableTileEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypesPM.RESEARCH_TABLE.get(), pos, state);
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player) {
        ResearchTableMenu menu = new ResearchTableMenu(windowId, playerInv, this.getBlockPos(), this);
        this.addListener(Direction.UP, menu);
        return menu;
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
    public Optional<Integer> getInventoryIndexForFace(Direction face) {
        return switch (face) {
            case DOWN -> Optional.empty();
            default -> Optional.of(INPUT_INV_INDEX);
        };
    }

    @Override
    protected NonNullList<IItemHandlerPM> createHandlers() {
        NonNullList<IItemHandlerPM> retVal = NonNullList.withSize(this.getInventoryCount(), Services.ITEM_HANDLERS.create(this));
        
        // Create input handler
        retVal.set(INPUT_INV_INDEX, Services.ITEM_HANDLERS.builder(this.inventories.get(INPUT_INV_INDEX), this)
                .itemValidFunction((slot, stack) -> switch (slot) {
                    case 0 -> stack.getItem() instanceof IWritingImplement;
                    case 1 -> stack.is(Items.PAPER);
                    default -> false;
                }).build());

        return retVal;
    }
}
