package com.verdantartifice.primalmagick.common.menus;

import com.verdantartifice.primalmagick.common.menus.base.AbstractTileSidedInventoryMenu;
import com.verdantartifice.primalmagick.common.tiles.devices.DesalinatorTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * Server data container for the desalinator GUI.
 *
 * @author Daedalus4096
 */
public class DesalinatorMenu extends AbstractTileSidedInventoryMenu<DesalinatorTileEntity> {
    protected final ContainerData containerData;
    protected final Slot waterBucketSlot;
    protected final Slot saltSlot;
    protected final Slot essenceSlot;
    protected final Slot emptyBucketSlot;
    protected final Slot wandSlot;

    public DesalinatorMenu(int id, Inventory playerInv, BlockPos tilePos) {
        this(id, playerInv, tilePos, null, new SimpleContainerData(4));
    }

    public DesalinatorMenu(int id, Inventory playerInv, BlockPos tilePos, DesalinatorTileEntity desalinator, ContainerData containerData) {
        super(MenuTypesPM.DESALINATOR.get(), id, DesalinatorTileEntity.class, playerInv.player.level(), tilePos, desalinator);
        checkContainerDataCount(containerData, 6);
        this.containerData = containerData;

        // TODO Slot 0: water bucket input
        // TODO Slot 1: salt output
        // TODO Slot 2: essence output
        // TODO Slot 3: empty bucket output
        // TODO Slot 4: wand input

        // Slots 5-31: player backpack
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // Slots 32-40: player hotbar
        for (int k = 0; k < 9; k++) {
            this.addSlot(new Slot(playerInv, k, 8 + k * 18, 142));
        }

        this.addDataSlots(this.containerData);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            if (index == 1 || index == 2 || index == 3) {
                // If transferring an output item, move it into the player's backpack or hotbar
                if (!this.moveItemStackTo(slotStack, 5, 41, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotStack, stack);
            } else if (index == 0 || index == 4) {
                // If transferring one of the input items, move it into the player's backpack or hotbar
                if (!this.moveItemStackTo(slotStack, 5, 41, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.waterBucketSlot.mayPlace(slotStack)) {
                // If transferring a valid ingredient, move it into the appropriate slot
                if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.wandSlot.mayPlace(slotStack)) {
                // If transferring a valid wand, move it into the appropriate slot
                if (!this.moveItemStackTo(slotStack, 4, 5, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 5 && index < 32) {
                // If transferring from the backpack and not a valid fit, move to the hotbar
                if (!this.moveItemStackTo(slotStack, 32, 41, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 32 && index < 41) {
                // If transferring from the hotbar and not a valid fit, move to the backpack
                if (!this.moveItemStackTo(slotStack, 5, 32, false)) {
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

    public int getBoilProgressionScaled() {
        // Determine how much of the progress arrow to show
        int i = this.containerData.get(0);
        int j = this.containerData.get(1);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }

    public int getCurrentMana() {
        return this.containerData.get(2);
    }

    public int getMaxMana() {
        return this.containerData.get(3);
    }

    public int getCurrentWaterAmount() {
        return this.containerData.get(4);
    }

    public int getWaterCapacity() {
        return this.containerData.get(5);
    }
}
