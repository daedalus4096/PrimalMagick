package com.verdantartifice.primalmagick.common.menus;

import com.verdantartifice.primalmagick.common.crafting.IDissolutionRecipe;
import com.verdantartifice.primalmagick.common.menus.base.AbstractTileRecipeBookMenu;
import com.verdantartifice.primalmagick.common.tiles.devices.DissolutionChamberTileEntity;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Server data container for the dissolution chamber GUI.
 * 
 * @author Daedalus4096
 */
public class DissolutionChamberMenu extends AbstractTileRecipeBookMenu<DissolutionChamberTileEntity, SingleRecipeInput, IDissolutionRecipe> {
    protected final ContainerData chamberData;
    protected final Slot resultSlot;
    protected final Slot inputSlot;
    protected final Slot wandSlot;
    
    public DissolutionChamberMenu(int id, Inventory playerInv, BlockPos tilePos) {
        this(id, playerInv, tilePos, null, new SimpleContainerData(4));
    }
    
    public DissolutionChamberMenu(int id, Inventory playerInv, BlockPos tilePos, DissolutionChamberTileEntity chamber, ContainerData chamberData) {
        super(MenuTypesPM.DISSOLUTION_CHAMBER.get(), id, DissolutionChamberTileEntity.class, playerInv.player.level(), tilePos, chamber, 1, 1);
        checkContainerDataCount(chamberData, 4);
        this.chamberData = chamberData;
        
        // Slot 0: chamber output
        this.resultSlot = this.addSlot(Services.MENU.makeGenericResultSlot(playerInv.player, this.getTileInventory(DissolutionChamberTileEntity.OUTPUT_INV_INDEX), 0, 125, 35));
        
        // Slot 1: ore input
        this.inputSlot = this.addSlot(Services.MENU.makeSlot(this.getTileInventory(DissolutionChamberTileEntity.INPUT_INV_INDEX), 0, 44, 35));
        
        // Slot 2: wand input
        this.wandSlot = this.addSlot(Services.MENU.makeWandSlot(this.getTileInventory(DissolutionChamberTileEntity.WAND_INV_INDEX), 0, 8, 62, false));
        
        // Slots 3-29: player backpack
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // Slots 30-38: player hotbar
        for (int k = 0; k < 9; k++) {
            this.addSlot(new Slot(playerInv, k, 8 + k * 18, 142));
        }

        this.addDataSlots(this.chamberData);
    }

    @Override
    @NotNull
    public ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            if (index == 0) {
                // If transferring an output item, move it into the player's backpack or hotbar
                if (!this.moveItemStackTo(slotStack, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotStack, stack);
            } else if (index >= 3 && index < 30) {
                // If transferring from the player's backpack, put wands in the wand slot and everything else into the input slots or hotbar, in that order
                if (this.wandSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(slotStack, 1, 2, false) && !this.moveItemStackTo(slotStack, 30, 39, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 30 && index < 39) {
                // If transferring from the player's hotbar, put wands in the wand slot and everything else into the input slots or backpack, in that order
                if (this.wandSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(slotStack, 1, 2, false) && !this.moveItemStackTo(slotStack, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotStack, 3, 39, false)) {
                // Move all other transfers into the backpack or hotbar
                return ItemStack.EMPTY;
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

    public int getDissolutionProgressionScaled() {
        // Determine how much of the progress arrow to show
        int i = this.chamberData.get(0);
        int j = this.chamberData.get(1);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }
    
    public int getCurrentMana() {
        return this.chamberData.get(2);
    }
    
    public int getMaxMana() {
        return this.chamberData.get(3);
    }

    @Override
    public @NotNull List<Slot> getInputGridSlots() {
        return List.of(this.inputSlot);
    }

    @Override
    protected @NotNull List<Slot> getSlotsToClear() {
        return List.of(this.resultSlot, this.inputSlot);
    }

    public Slot getResultSlot() {
        return this.resultSlot;
    }

    @Override
    protected @NotNull SingleRecipeInput getRecipeInputForMatch() {
        return new SingleRecipeInput(this.getTileInventory(Direction.UP) != null ? this.getTileInventory(Direction.UP).getStackInSlot(0) : ItemStack.EMPTY);
    }

    @Override
    @NotNull
    public RecipeBookType getRecipeBookType() {
        return RecipeBookType.CRAFTING;
    }
}
