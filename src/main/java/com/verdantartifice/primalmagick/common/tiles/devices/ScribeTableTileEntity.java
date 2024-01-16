package com.verdantartifice.primalmagick.common.tiles.devices;

import java.util.OptionalInt;

import com.verdantartifice.primalmagick.common.capabilities.IPlayerLinguistics;
import com.verdantartifice.primalmagick.common.capabilities.ItemStackHandlerPM;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.items.books.StaticBookItem;
import com.verdantartifice.primalmagick.common.menus.ScribeGainComprehensionMenu;
import com.verdantartifice.primalmagick.common.menus.ScribeStudyVocabularyMenu;
import com.verdantartifice.primalmagick.common.menus.ScribeTranscribeWorksMenu;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;
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
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Definition of a scribe table tile entity.  Holds the inventory for the corresponding block.
 * 
 * @see {@link com.verdantartifice.primalmagick.common.blocks.devices.ScribeTableBlock}
 * @author Daedalus4096
 */
public class ScribeTableTileEntity extends AbstractTileSidedInventoryPM implements MenuProvider {
    protected static final int INPUT_INV_INDEX = 0;
    
    public ScribeTableTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.SCRIBE_TABLE.get(), pos, state);
    }

    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        LazyOptional<IPlayerLinguistics> capOpt = PrimalMagickCapabilities.getLinguistics(pPlayer);
        if (capOpt.isPresent()) {
            return switch (capOpt.resolve().get().getScribeTableMode()) {
                case STUDY_VOCABULARY -> new ScribeStudyVocabularyMenu(pContainerId, pPlayerInventory, this.getBlockPos(), this);
                case GAIN_COMPREHENSION -> new ScribeGainComprehensionMenu(pContainerId, pPlayerInventory, this.getBlockPos(), this);
                case TRANSCRIBE_WORKS -> new ScribeTranscribeWorksMenu(pContainerId, pPlayerInventory, this.getBlockPos(), this);
            };
        } else {
            return null;
        }
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
                    return stack.is(ItemTagsPM.STATIC_BOOKS) && StaticBookItem.getBookLanguage(stack).isComplex();
                } else if (slot == 1) {
                    return stack.is(Items.WRITABLE_BOOK);
                } else {
                    return false;
                }
            }
        });

        return retVal;
    }

    @Override
    protected void loadLegacyItems(NonNullList<ItemStack> legacyItems) {
        // Nothing to do; this tile didn't exist in legacy versions
    }
}
