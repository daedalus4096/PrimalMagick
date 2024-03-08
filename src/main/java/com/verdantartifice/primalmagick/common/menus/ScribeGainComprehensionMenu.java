package com.verdantartifice.primalmagick.common.menus;

import org.joml.Vector2i;

import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.books.LinguisticsManager;
import com.verdantartifice.primalmagick.common.items.books.StaticBookItem;
import com.verdantartifice.primalmagick.common.menus.slots.FilteredSlot;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;
import com.verdantartifice.primalmagick.common.tiles.devices.ScribeTableTileEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * Server data container for the study vocabulary mode of the scribe table GUI.
 * 
 * @author Daedalus4096
 */
public class ScribeGainComprehensionMenu extends AbstractScribeTableMenu {
    private final DataSlot languageClue = DataSlot.standalone();
    private final DataSlot vocabularyCount = DataSlot.standalone();

    protected Slot studySlot;
    
    public ScribeGainComprehensionMenu(int windowId, Inventory inv, BlockPos pos) {
        this(windowId, inv, pos, null);
        this.addDataSlot(this.languageClue);
        this.addDataSlot(this.vocabularyCount);
        this.refreshBookData();
    }
    
    public ScribeGainComprehensionMenu(int windowId, Inventory inv, BlockPos pos, ScribeTableTileEntity entity) {
        super(MenuTypesPM.SCRIBE_GAIN_COMPREHENSION.get(), windowId, inv, pos, entity);
    }
    
    @Override
    protected void createModeSlots() {
        // Slot 0: Original book
        this.studySlot = this.addSlot(new FilteredSlot(this.getTileInventory(Direction.UP), 0, 8, 8, 
                new FilteredSlot.Properties().filter(stack -> stack.is(ItemTagsPM.STATIC_BOOKS) && StaticBookItem.getBookLanguage(stack).isComplex())));
    }

    @Override
    protected Vector2i getInventorySlotsOffset() {
        return new Vector2i(27, 56);
    }

    @Override
    public void containerChanged(Container pContainer) {
        super.containerChanged(pContainer);
        this.refreshBookData();
    }
    
    protected void refreshBookData() {
        ItemStack bookStack = this.studySlot.getItem();
        BookLanguage lang = bookStack.is(ItemTagsPM.STATIC_BOOKS) ? StaticBookItem.getBookLanguage(bookStack) : BookLanguagesPM.DEFAULT.get();
        this.languageClue.set(BookLanguagesPM.LANGUAGES.get().getKey(lang).hashCode());
        this.vocabularyCount.set(LinguisticsManager.getVocabulary(this.player, lang));
    }
    
    public BookLanguage getBookLanguage() {
        int hashCode = this.languageClue.get();
        for (ResourceLocation key : BookLanguagesPM.LANGUAGES.get().getKeys()) {
            if (key.hashCode() == hashCode) {
                return BookLanguagesPM.LANGUAGES.get().getValue(key);
            }
        }
        return BookLanguagesPM.DEFAULT.get();
    }
    
    public int getVocabularyCount() {
        return this.vocabularyCount.get();
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            if (pIndex == 0) {
                // If transferring the study item, move it into the player's backpack or hotbar
                if (!this.moveItemStackTo(slotStack, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotStack, stack);
            } else if (pIndex >= 1 && pIndex < 28) {
                // If transferring from the backpack, move static books to the appropriate slot, and everything else to the hotbar
                if (this.studySlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.moveItemStackTo(slotStack, 28, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (pIndex >= 28 && pIndex < 37) {
                // If transferring from the hotbar, move static books to the appropriate slot, and everything else to the backpack
                if (this.studySlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.moveItemStackTo(slotStack, 1, 28, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(slotStack, 1, 37, false)) {
                // Move all other transfers to the backpack or hotbar
                return ItemStack.EMPTY;
            }
            
            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            
            if (slotStack.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }
            
            slot.onTake(pPlayer, slotStack);
        }
        return stack;
    }
}
