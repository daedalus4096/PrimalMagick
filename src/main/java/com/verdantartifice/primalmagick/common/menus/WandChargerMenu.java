package com.verdantartifice.primalmagick.common.menus;

import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.menus.base.AbstractTileSidedInventoryMenu;
import com.verdantartifice.primalmagick.common.menus.slots.FilteredSlot;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;
import com.verdantartifice.primalmagick.common.tiles.mana.WandChargerTileEntity;
import com.verdantartifice.primalmagick.common.wands.IWand;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * Server data container for the wand charger GUI.
 * 
 * @author Daedalus4096
 */
public class WandChargerMenu extends AbstractTileSidedInventoryMenu<WandChargerTileEntity> {
    protected final ContainerData chargerData;
    protected final Slot essenceSlot;
    protected final Slot wandSlot;
    
    public WandChargerMenu(int id, Inventory playerInv, BlockPos tilePos) {
        this(id, playerInv, tilePos, null, new SimpleContainerData(2));
    }
    
    public WandChargerMenu(int id, Inventory playerInv, BlockPos tilePos, WandChargerTileEntity charger, ContainerData chargerData) {
        super(MenuTypesPM.WAND_CHARGER.get(), id, WandChargerTileEntity.class, playerInv.player.level(), tilePos, charger);
        checkContainerDataCount(chargerData, 2);
        this.chargerData = chargerData;
        
        // Slot 0: essence input
        this.essenceSlot = this.addSlot(new FilteredSlot(this.getTileInventory(Direction.UP), 0, 52, 35, new FilteredSlot.Properties().tag(ItemTagsPM.ESSENCES)));
        
        // Slot 1: wand input/output
        this.wandSlot = this.addSlot(new FilteredSlot(this.getTileInventory(Direction.NORTH), 0, 108, 35, 
                new FilteredSlot.Properties().filter(stack -> (stack.getItem() instanceof IWand) || stack.getCapability(PrimalMagickCapabilities.MANA_STORAGE).isPresent())));
        
        // Slots 2-28: player backpack
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // Slots 29-37: player hotbar
        for (int k = 0; k < 9; k++) {
            this.addSlot(new Slot(playerInv, k, 8 + k * 18, 142));
        }

        this.addDataSlots(this.chargerData);
    }
    
    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            if (index >= 2 && index < 29) {
                // If transferring from the backpack, move wands and essences to the appropriate slots, and everything else to the hotbar
                if (this.wandSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.essenceSlot.mayPlace(slotStack)) {
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
                if (this.wandSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.essenceSlot.mayPlace(slotStack)) {
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
            
            slot.onTake(playerIn, slotStack);
        }
        return stack;
    }
    
    public int getChargeProgressionScaled() {
        // Determine how much of the charge arrow to show
        int i = this.chargerData.get(0);
        int j = this.chargerData.get(1);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }
}
