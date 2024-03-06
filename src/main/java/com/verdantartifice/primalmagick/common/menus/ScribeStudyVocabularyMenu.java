package com.verdantartifice.primalmagick.common.menus;

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
public class ScribeStudyVocabularyMenu extends AbstractScribeTableMenu {
    public final int[] costs = new int[3];
    private final DataSlot nameSeed = DataSlot.standalone();
    private final DataSlot languageClue = DataSlot.standalone();

    protected Slot studySlot;
    
    public ScribeStudyVocabularyMenu(int windowId, Inventory inv, BlockPos pos) {
        this(windowId, inv, pos, null);
    }
    
    public ScribeStudyVocabularyMenu(int windowId, Inventory inv, BlockPos pos, ScribeTableTileEntity entity) {
        super(MenuTypesPM.SCRIBE_STUDY_VOCABULARY.get(), windowId, inv, pos, entity);
        this.addDataSlot(DataSlot.shared(this.costs, 0));
        this.addDataSlot(DataSlot.shared(this.costs, 1));
        this.addDataSlot(DataSlot.shared(this.costs, 2));
        this.addDataSlot(this.nameSeed).set(this.player.getEnchantmentSeed());
        this.addDataSlot(this.languageClue);
        this.refreshBookData();
    }
    
    @Override
    protected void createModeSlots() {
        // Slot 0: Original book
        this.studySlot = this.addSlot(new FilteredSlot(this.getTileInventory(Direction.UP), 0, 26, 47, 
                new FilteredSlot.Properties().filter(stack -> stack.is(ItemTagsPM.STATIC_BOOKS) && StaticBookItem.getBookLanguage(stack).isComplex())));
    }

    @Override
    public void containerChanged(Container pContainer) {
        super.containerChanged(pContainer);
        this.refreshBookData();
    }
    
    protected void refreshBookData() {
        ItemStack bookStack = this.studySlot.getItem();
        if (bookStack.is(ItemTagsPM.STATIC_BOOKS)) {
            BookLanguage lang = StaticBookItem.getBookLanguage(bookStack);
            int studyCount = LinguisticsManager.getTimesStudied(this.player, StaticBookItem.getBookDefinition(bookStack), lang);
            for (int index = 0; index < 3; index++) {
                // Set the cost of each slot, including the cost of any previous unstudied slots.  Studied slots are given a cost
                // of -1 as a marker.  In isolation, each slot's cost is equal to its index plus one (e.g. 1, 2, and 3 respectively).
                // Thus, the final costs in the case where none have been studied would be 1, 3, and 6 respectively.  If, rather, the
                // first slot had been studied, the costs would instead be -1, 2, and 5 respectively.
                this.costs[index] = (index >= studyCount) ? index + 1 + (index > 0 ? Math.max(this.costs[index - 1], 0) : 0) : -1;
            }
            this.languageClue.set(BookLanguagesPM.LANGUAGES.get().getKey(lang).hashCode());
        } else {
            for (int index = 0; index < 3; index++) {
                this.costs[index] = 0;
            }
            this.languageClue.set(BookLanguagesPM.DEFAULT.getId().hashCode());
        }
    }
    
    public int getNameSeed() {
        return this.nameSeed.get();
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
