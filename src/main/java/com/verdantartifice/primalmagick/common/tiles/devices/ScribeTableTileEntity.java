package com.verdantartifice.primalmagick.common.tiles.devices;

import java.util.OptionalInt;

import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerLinguistics;
import com.verdantartifice.primalmagick.common.capabilities.ItemStackHandlerPM;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.items.books.StaticBookItem;
import com.verdantartifice.primalmagick.common.menus.AbstractScribeTableMenu;
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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
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
    protected static final int OUTPUT_INV_INDEX = 1;
    
    public ScribeTableTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.SCRIBE_TABLE.get(), pos, state);
    }

    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        LazyOptional<IPlayerLinguistics> capOpt = PrimalMagickCapabilities.getLinguistics(pPlayer);
        if (capOpt.isPresent()) {
            AbstractScribeTableMenu menu = switch (capOpt.resolve().get().getScribeTableMode()) {
                case STUDY_VOCABULARY -> new ScribeStudyVocabularyMenu(pContainerId, pPlayerInventory, this.getBlockPos(), this);
                case GAIN_COMPREHENSION -> new ScribeGainComprehensionMenu(pContainerId, pPlayerInventory, this.getBlockPos(), this);
                case TRANSCRIBE_WORKS -> new ScribeTranscribeWorksMenu(pContainerId, pPlayerInventory, this.getBlockPos(), this);
            };
            this.addListener(Direction.UP, menu);
            return menu;
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
        return 2;
    }

    @Override
    protected int getInventorySize(int inventoryIndex) {
        return switch (inventoryIndex) {
            case INPUT_INV_INDEX -> 2;
            case OUTPUT_INV_INDEX -> 1;
            default -> 0;
        };
    }

    @Override
    protected OptionalInt getInventoryIndexForFace(Direction face) {
        return switch (face) {
            case DOWN -> OptionalInt.of(OUTPUT_INV_INDEX);
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
        
        // Create output handler
        retVal.set(OUTPUT_INV_INDEX, new ItemStackHandlerPM(this.inventories.get(OUTPUT_INV_INDEX), this) {
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return false;
            }
        });

        return retVal;
    }

    @Override
    protected void loadLegacyItems(NonNullList<ItemStack> legacyItems) {
        // Nothing to do; this tile didn't exist in legacy versions
    }
    
    public void doTranscribe(Player player) {
        Level level = this.getLevel();
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            ItemStack sourceStack = this.getItem(INPUT_INV_INDEX, 0);
            ItemStack blankStack = this.getItem(INPUT_INV_INDEX, 1);
            if (sourceStack.is(ItemTagsPM.STATIC_BOOKS) && blankStack.is(Items.WRITABLE_BOOK)) {
                BookLanguage sourceLanguage = StaticBookItem.getBookLanguage(sourceStack);
                int sourceGeneration = StaticBookItem.getGeneration(sourceStack);
                if (sourceLanguage.isComplex() && sourceGeneration < StaticBookItem.MAX_GENERATION) {
                    PrimalMagickCapabilities.getLinguistics(serverPlayer).ifPresent(linguistics -> {
                        // Construct the translated result book if all prerequisites are met
                        ItemStack resultStack = sourceStack.copyWithCount(1);
                        int playerComprehension = linguistics.getComprehension(sourceLanguage.languageId());
                        int sourceComprehension = StaticBookItem.getTranslatedComprehension(sourceStack).orElse(0);
                        int maxComprehension = Math.max(playerComprehension, sourceComprehension);
                        StaticBookItem.setTranslatedComprehension(resultStack, maxComprehension <= 0 ? OptionalInt.empty() : OptionalInt.of(maxComprehension));
                        StaticBookItem.setGeneration(resultStack, sourceGeneration + 1);
                        
                        // Add the result book to the output slot if there's room, then shrink the blank book stack
                        ItemStack existingStack = this.getItem(OUTPUT_INV_INDEX, 0);
                        if (ItemStack.isSameItemSameTags(resultStack, existingStack) && existingStack.getCount() < existingStack.getMaxStackSize()) {
                            existingStack.grow(1);
                            blankStack.shrink(1);
                            this.setChanged();
                            this.syncTile(false);
                        } else if (existingStack.isEmpty()) {
                            this.setItem(OUTPUT_INV_INDEX, 0, resultStack);
                            blankStack.shrink(1);
                            this.setChanged();
                            this.syncTile(false);
                        }
                    });
                }
            }
        }
    }
}
