package com.verdantartifice.primalmagick.common.menus;

import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.books.LinguisticsManager;
import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.items.books.StaticBookItem;
import com.verdantartifice.primalmagick.common.menus.slots.FilteredSlotProperties;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;
import com.verdantartifice.primalmagick.common.tiles.devices.ScribeTableTileEntity;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector2i;

/**
 * Server data container for the study vocabulary mode of the scribe table GUI.
 * 
 * @author Daedalus4096
 */
public class ScribeGainComprehensionMenu extends AbstractScribeTableMenu {
    public static final ResourceLocation BOOK_SLOT_TEXTURE = ResourceUtils.loc("item/empty_book_slot");
    protected static final Component ANCIENT_BOOK_TOOLTIP = Component.translatable("tooltip.primalmagick.scribe_table.slot.ancient_book");

    private final DataSlot languageClue = DataSlot.standalone();
    private final DataSlot vocabularyCount = DataSlot.standalone();

    protected Slot studySlot;
    
    public ScribeGainComprehensionMenu(int windowId, Inventory inv, BlockPos pos) {
        this(windowId, inv, pos, null);
    }
    
    public ScribeGainComprehensionMenu(int windowId, Inventory inv, BlockPos pos, ScribeTableTileEntity entity) {
        super(MenuTypesPM.SCRIBE_GAIN_COMPREHENSION.get(), windowId, inv, pos, entity);
        this.addDataSlot(this.languageClue);
        this.addDataSlot(this.vocabularyCount);
        this.refreshBookData();
    }
    
    @Override
    protected void createModeSlots() {
        // Slot 0: Original book
        this.studySlot = this.addSlot(Services.MENU.makeFilteredSlot(this.getTileInventory(Direction.UP), 0, 8, 18,
                new FilteredSlotProperties().filter(this::isAncientBookStack).background(BOOK_SLOT_TEXTURE).tooltip(ANCIENT_BOOK_TOOLTIP)));
    }

    @Override
    protected Vector2i getInventorySlotsOffset() {
        return new Vector2i(0, 56);
    }

    @Override
    public void itemsChanged(IItemHandlerPM pContainer) {
        super.itemsChanged(pContainer);
        this.refreshBookData();
    }
    
    public void refreshBookData() {
        ItemStack bookStack = this.studySlot.getItem();
        ResourceKey<BookLanguage> langKey = bookStack.is(ItemTagsPM.STATIC_BOOKS) ? StaticBookItem.getBookLanguageId(bookStack).orElse(BookLanguagesPM.DEFAULT) : BookLanguagesPM.DEFAULT;
        Holder.Reference<BookLanguage> lang = BookLanguagesPM.getLanguageOrDefault(langKey, this.level.registryAccess(), BookLanguagesPM.DEFAULT);
        this.languageClue.set(langKey.location().hashCode());
        this.vocabularyCount.set(LinguisticsManager.getVocabulary(this.player, lang));
    }
    
    public Holder.Reference<BookLanguage> getBookLanguage() {
        int hashCode = this.languageClue.get();
        return this.level.registryAccess().registryOrThrow(RegistryKeysPM.BOOK_LANGUAGES).holders().filter(h -> h.key().location().hashCode() == hashCode).findFirst()
                .orElse(BookLanguagesPM.getLanguageOrThrow(BookLanguagesPM.DEFAULT, this.level.registryAccess()));
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
