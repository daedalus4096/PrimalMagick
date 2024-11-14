package com.verdantartifice.primalmagick.common.menus;

import com.verdantartifice.primalmagick.common.menus.slots.FilteredSlot;
import com.verdantartifice.primalmagick.common.menus.slots.FilteredSlotProperties;
import com.verdantartifice.primalmagick.common.menus.slots.GenericResultSlot;
import com.verdantartifice.primalmagick.common.tiles.devices.ScribeTableTileEntity;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.joml.Vector2i;

/**
 * Server data container for the study vocabulary mode of the scribe table GUI.
 * 
 * @author Daedalus4096
 */
public class ScribeTranscribeWorksMenu extends AbstractScribeTableMenu {
    public static final ResourceLocation BOOK_SLOT_TEXTURE = ResourceUtils.loc("item/empty_book_slot");
    public static final ResourceLocation WRITABLE_BOOK_SLOT_TEXTURE = ResourceUtils.loc("item/empty_writable_book_slot");
    protected static final Component ANCIENT_BOOK_TOOLTIP = Component.translatable("tooltip.primalmagick.scribe_table.slot.ancient_book");
    protected static final Component WRITABLE_BOOK_TOOLTIP = Component.translatable("tooltip.primalmagick.scribe_table.slot.writable_book");

    protected final ResultContainer resultInv = new ResultContainer();
    protected Slot originalSlot;
    protected Slot blankSlot;
    
    public ScribeTranscribeWorksMenu(int windowId, Inventory inv, BlockPos pos) {
        this(windowId, inv, pos, null);
    }
    
    public ScribeTranscribeWorksMenu(int windowId, Inventory inv, BlockPos pos, ScribeTableTileEntity entity) {
        super(MenuTypesPM.SCRIBE_TRANSCRIBE_WORKS.get(), windowId, inv, pos, entity);
    }
    
    @Override
    protected void createModeSlots() {
        // Slot 0: Result
        this.addSlot(new GenericResultSlot(this.player, this.getTileInventory(Direction.DOWN), 0, 124, 63));

        // Slot 1: Original book
        this.originalSlot = this.addSlot(new FilteredSlot(this.getTileInventory(Direction.UP), 0, 30, 63, 
                new FilteredSlotProperties().filter(this::isAncientBookStack).background(BOOK_SLOT_TEXTURE).tooltip(ANCIENT_BOOK_TOOLTIP)));
        
        // Slot 2: Blank book and quill
        this.blankSlot = this.addSlot(new FilteredSlot(this.getTileInventory(Direction.UP), 1, 66, 63,
                new FilteredSlotProperties().item(Items.WRITABLE_BOOK).background(WRITABLE_BOOK_SLOT_TEXTURE).tooltip(WRITABLE_BOOK_TOOLTIP)));
    }

    @Override
    protected Vector2i getInventorySlotsOffset() {
        return new Vector2i(0, 56);
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            if (pIndex == 0) {
                // If transferring the output item, move it into the player's backpack or hotbar
                if (!this.moveItemStackTo(slotStack, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotStack, stack);
            } else if (pIndex >= 3 && pIndex < 30) {
                // If transferring from the backpack, move written and blank books to the appropriate slots, and everything else to the hotbar
                if (this.originalSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.blankSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.moveItemStackTo(slotStack, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (pIndex >= 30 && pIndex < 39) {
                // If transferring from the hotbar, move written and blank books to the appropriate slots, and everything else to the backpack
                if (this.originalSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.blankSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.moveItemStackTo(slotStack, 3, 30, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(slotStack, 3, 39, false)) {
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

    public void doTranscribe() {
        this.tile.doTranscribe(this.player);
    }
}
