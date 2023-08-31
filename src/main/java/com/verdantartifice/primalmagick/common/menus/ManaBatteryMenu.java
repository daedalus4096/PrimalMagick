package com.verdantartifice.primalmagick.common.menus;

import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.items.essence.EssenceItem;
import com.verdantartifice.primalmagick.common.menus.slots.FilteredSlot;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.wands.IWand;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * Server data container for mana battery GUIs.
 * 
 * @author Daedalus4096
 */
public class ManaBatteryMenu extends AbstractContainerMenu {
    protected final Container inv;
    protected final ContainerData data;
    protected final Slot inputSlot;
    protected final Slot chargeSlot;
    
    public ManaBatteryMenu(int id, Inventory playerInv) {
        this(id, playerInv, new SimpleContainer(2), new SimpleContainerData(20));
    }
    
    public ManaBatteryMenu(int id, Inventory playerInv, Container inv, ContainerData data) {
        super(MenuTypesPM.MANA_BATTERY.get(), id);
        checkContainerSize(inv, 2);
        checkContainerDataCount(data, 20);
        this.inv = inv;
        this.data = data;
        
        // Slot 0: input slot
        this.inputSlot = this.addSlot(new FilteredSlot(this.inv, 0, 8, 34, new FilteredSlot.Properties().typeOf(EssenceItem.class, IWand.class)));
        
        // Slot 1: charge slot
        this.chargeSlot = this.addSlot(new FilteredSlot(this.inv, 1, 206, 34, 
                new FilteredSlot.Properties().filter(stack -> (stack.getItem() instanceof IWand) || stack.getCapability(PrimalMagickCapabilities.MANA_STORAGE).isPresent())));

        // Slots 2-28: player backpack
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 35 + j * 18, 82 + i * 18));
            }
        }

        // Slots 29-37: player hotbar
        for (int k = 0; k < 9; k++) {
            this.addSlot(new Slot(playerInv, k, 35 + k * 18, 140));
        }

        this.addDataSlots(this.data);
    }
    
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            if (index >= 2 && index < 29) {
                // If transferring from the backpack, move wands and essences to the appropriate slots, and everything else to the hotbar
                if (this.chargeSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.inputSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.moveItemStackTo(slotStack, 29, 38, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (index >= 29 && index < 38) {
                // If transferring from the hotbar, move wands and essences to the appropriate slots, and everything else to the backpack
                if (this.chargeSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.inputSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.moveItemStackTo(slotStack, 2, 29, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(slotStack, 2, 38, false)) {
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
            
            slot.onTake(player, slotStack);
        }
        return stack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return this.inv.stillValid(pPlayer);
    }

    public int getChargeProgressionScaled() {
        // Determine how much of the charge arrow to show
        int i = this.data.get(0);
        int j = this.data.get(1);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }
    
    public int getCurrentMana(Source source) {
        int sourceIndex = Source.SORTED_SOURCES.indexOf(source);
        return this.data.get(2 + (2 * sourceIndex));
    }
    
    public int getMaxMana(Source source) {
        int sourceIndex = Source.SORTED_SOURCES.indexOf(source);
        return this.data.get(3 + (2 * sourceIndex));
    }
}
