package com.verdantartifice.primalmagick.common.menus;

import com.verdantartifice.primalmagick.common.menus.base.AbstractTileSidedInventoryMenu;
import com.verdantartifice.primalmagick.common.menus.slots.FilteredSlot;
import com.verdantartifice.primalmagick.common.menus.slots.GenericResultSlot;
import com.verdantartifice.primalmagick.common.menus.slots.WandSlot;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;
import com.verdantartifice.primalmagick.common.tiles.devices.EssenceTransmuterTileEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * Server data container for the essence transmuter GUI.
 * 
 * @author Daedalus4096
 */
public class EssenceTransmuterMenu extends AbstractTileSidedInventoryMenu<EssenceTransmuterTileEntity> {
    protected final ContainerData transmuterData;
    protected final Slot inputSlot;
    protected final Slot wandSlot;

    public EssenceTransmuterMenu(int id, Inventory playerInv, BlockPos tilePos) {
        this(id, playerInv, tilePos, null, new SimpleContainerData(4));
    }
    
    public EssenceTransmuterMenu(int id, Inventory playerInv, BlockPos tilePos, EssenceTransmuterTileEntity transmuter, ContainerData transmuterData) {
        super(MenuTypesPM.ESSENCE_TRANSMUTER.get(), id, EssenceTransmuterTileEntity.class, playerInv.player.level(), tilePos, transmuter);
        checkContainerDataCount(transmuterData, 4);
        this.transmuterData = transmuterData;
        
        // Slot 0: essence input
        this.inputSlot = this.addSlot(new FilteredSlot(this.getTileInventory(Direction.UP), 0, 44, 35, new FilteredSlot.Properties().tag(ItemTagsPM.ESSENCES)));
        
        // Slots 1-9: transmuter output
        for (int i = 0; i < 9; i++) {
            this.addSlot(new GenericResultSlot(playerInv.player, this.getTileInventory(Direction.DOWN), i, 98 + ((i % 3) * 18), 17 + ((i / 3) * 18)));
        }
        
        // Slot 10: wand input
        this.wandSlot = this.addSlot(new WandSlot(this.getTileInventory(Direction.NORTH), 0, 8, 62, false));
        
        // Slots 11-37: player backpack
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // Slots 38-46: player hotbar
        for (int k = 0; k < 9; k++) {
            this.addSlot(new Slot(playerInv, k, 8 + k * 18, 142));
        }

        this.addDataSlots(this.transmuterData);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            if (index >= 1 && index < 10) {
                // If transferring an output item, move it into the player's backpack or hotbar
                if (!this.moveItemStackTo(slotStack, 11, 47, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotStack, stack);
            } else if (index == 0 || index == 10) {
                // If transferring one of the input items, move it into the player's backpack or hotbar
                if (!this.moveItemStackTo(slotStack, 11, 47, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.inputSlot.mayPlace(slotStack)) {
                // If transferring a valid ingredient, move it into the appropriate slot
                if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.wandSlot.mayPlace(slotStack)) {
                // If transferring a valid wand, move it into the appropriate slot
                if (!this.moveItemStackTo(slotStack, 10, 11, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 1 && index < 38) {
                // If transferring from the backpack and not a valid fit, move to the hotbar
                if (!this.moveItemStackTo(slotStack, 38, 47, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 38 && index < 47) {
                // If transferring from the hotbar and not a valid fit, move to the backpack
                if (!this.moveItemStackTo(slotStack, 11, 38, false)) {
                    return ItemStack.EMPTY;
                }
            }
            
            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            }
            
            slot.setChanged();
            if (slotStack.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }
            
            slot.onTake(player, slotStack);
            this.broadcastChanges();
        }
        
        return stack;
    }

    public int getTransmuteProgressionScaled() {
        // Determine how much of the progress arrow to show
        int i = this.transmuterData.get(0);
        int j = this.transmuterData.get(1);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }
    
    public int getCurrentMana() {
        return this.transmuterData.get(2);
    }
    
    public int getMaxMana() {
        return this.transmuterData.get(3);
    }
}
