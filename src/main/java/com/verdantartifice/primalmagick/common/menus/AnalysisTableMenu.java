package com.verdantartifice.primalmagick.common.menus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.menus.slots.AnalysisResultSlot;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Server data container for the analysis table GUI.
 * 
 * @author Daedalus4096
 */
public class AnalysisTableMenu extends AbstractContainerMenu {
    protected static final Logger LOGGER = LogManager.getLogger();
    
    protected final Container analysisInventory = new SimpleContainer(2) {
        public int getMaxStackSize() {
            return 1;
        }
    };
    protected final ContainerLevelAccess worldPosCallable;
    protected final Player player;
    
    public AnalysisTableMenu(int windowId, Inventory inv) {
        this(windowId, inv, ContainerLevelAccess.NULL);
    }
    
    public AnalysisTableMenu(int windowId, Inventory inv, ContainerLevelAccess callable) {
        super(MenuTypesPM.ANALYSIS_TABLE.get(), windowId);
        this.worldPosCallable = callable;
        this.player = inv.player;
        
        // Slot 0: Item to analyze
        this.addSlot(new Slot(this.analysisInventory, 0, 56, 35));
        
        // Slot 1: Last analyzed item
        this.addSlot(new AnalysisResultSlot(this.analysisInventory, 1, 103, 35));
        
        // Slots 2-28: Player backpack
        int i, j;
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 9; j++) {
                this.addSlot(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        
        // Slots 29-37: Player hotbar
        for (i = 0; i < 9; i++) {
            this.addSlot(new Slot(inv, i, 8 + i * 18, 142));
        }
    }
    
    @Override
    public boolean stillValid(Player playerIn) {
        return stillValid(this.worldPosCallable, playerIn, BlocksPM.ANALYSIS_TABLE.get());
    }
    
    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            if (index == 0) {
                // If transferring the input item, move it into the player's backpack or hotbar
                if (!this.moveItemStackTo(slotStack, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 2 && index < 29) {
                // If transferring from the player's backpack, attempt to place it in the input item slot
                if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 29 && index < 38) {
                // If transferring from the player's hotbar, attempt to place it in the input item slot
                if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotStack.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }
        }
        
        return stack;
    }
    
    public ItemStack getLastScannedStack() {
        return this.analysisInventory.getItem(1);
    }
    
    public void doScan() {
        Level level = this.player.level();
        if (!level.isClientSide && this.player instanceof ServerPlayer serverPlayer) {
            // Move the input item into the recently-scanned slot and mark it as scanned
            ItemStack stack = this.analysisInventory.getItem(0).copy();
            if (stack.is(ItemTagsPM.ANALYSIS_TABLE_FORBIDDEN)) {
                // Send a message to the player explaining that they can't destroy this item on the table
                this.player.displayClientMessage(Component.translatable("event.primalmagick.analysis_table.forbidden").withStyle(ChatFormatting.RED), false);
            } else if (!stack.isEmpty()) {
                this.analysisInventory.setItem(0, ItemStack.EMPTY);
                this.analysisInventory.setItem(1, stack);
                ResearchManager.isScannedAsync(stack, serverPlayer).thenAccept(isScanned -> {
                    if (!isScanned) {
                        ResearchManager.setScanned(stack, serverPlayer);
                    }
                }).exceptionally(e -> {
                    LOGGER.error("Failed to analyze item stack at analysis table: " + stack.toString(), e);
                    return null;
                });
            }
        }
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.analysisInventory.setItem(1, ItemStack.EMPTY);
        this.clearContainer(player, this.analysisInventory);
    }
}
