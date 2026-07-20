package com.verdantartifice.primalmagick.common.menus;

import com.verdantartifice.primalmagick.common.menus.base.AbstractTileRecipeBookMenu;
import com.verdantartifice.primalmagick.common.menus.slots.FilteredSlotProperties;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;
import com.verdantartifice.primalmagick.common.tiles.devices.InfernalFurnaceTileEntity;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Server data container for the infernal furnace GUI.
 * 
 * @author Daedalus4096
 */
public class InfernalFurnaceMenu extends AbstractTileRecipeBookMenu<InfernalFurnaceTileEntity, SingleRecipeInput, AbstractCookingRecipe> {
    public static final Identifier IGNYX_SLOT_TEXTURE = ResourceUtils.loc("item/empty_ignyx_slot");
    protected static final Component IGNYX_SLOT_TOOLTIP = Component.translatable("tooltip.primalmagick.infernal_furnace.slot.ignyx");

    protected final ContainerData furnaceData;
    protected final Slot resultSlot;
    protected final Slot inputSlot;
    protected final Slot ignyxSlot;
    protected final Slot wandSlot;
    
    public InfernalFurnaceMenu(int id, Inventory playerInv, BlockPos tilePos) {
        this(id, playerInv, tilePos, null, new SimpleContainerData(6));
    }
    
    public InfernalFurnaceMenu(int id, Inventory playerInv, BlockPos tilePos, InfernalFurnaceTileEntity furnace, ContainerData furnaceData) {
        super(MenuTypesPM.INFERNAL_FURNACE.get(), id, InfernalFurnaceTileEntity.class, playerInv.player.level(), tilePos, furnace, 1, 1);
        checkContainerDataCount(furnaceData, 6);
        this.furnaceData = furnaceData;
        
        // Slot 0: chamber output
        this.resultSlot = this.addSlot(Services.MENU.makeInfernalFurnaceResultSlot(playerInv.player, this.getTileInventory(InfernalFurnaceTileEntity.OUTPUT_INV_INDEX), 0, 125, 35));
        
        // Slot 1: material input
        this.inputSlot = this.addSlot(Services.MENU.makeSlot(this.getTileInventory(InfernalFurnaceTileEntity.INPUT_INV_INDEX), 0, 44, 17));
        
        // Slot 2: ignyx input
        this.ignyxSlot = this.addSlot(Services.MENU.makeFilteredSlot(this.getTileInventory(InfernalFurnaceTileEntity.FUEL_INV_INDEX), 0, 44, 53,
                new FilteredSlotProperties().background(IGNYX_SLOT_TEXTURE).tooltip(IGNYX_SLOT_TOOLTIP).tag(ItemTagsPM.INFERNAL_SUPERCHARGE_FUEL)));
        
        // Slot 3: wand input
        this.wandSlot = this.addSlot(Services.MENU.makeWandSlot(this.getTileInventory(InfernalFurnaceTileEntity.WAND_INV_INDEX), 0, 8, 62, false));
        
        // Slots 4-30: player backpack
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // Slots 31-39: player hotbar
        for (int k = 0; k < 9; k++) {
            this.addSlot(new Slot(playerInv, k, 8 + k * 18, 142));
        }

        this.addDataSlots(this.furnaceData);
    }

    public int getProcessProgressionScaled() {
        // Determine how much of the progress arrow to show
        int i = this.furnaceData.get(0);
        int j = this.furnaceData.get(1);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }
    
    public int getCurrentMana() {
        return this.furnaceData.get(2);
    }
    
    public int getMaxMana() {
        return this.furnaceData.get(3);
    }
    
    public int getSuperchargeProgressionScaled() {
        // Determine how much of the supercharge burner to show
        int i = this.furnaceData.get(4);
        int j = this.furnaceData.get(5);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }
    
    public boolean isSupercharged() {
        return this.furnaceData.get(4) > 0;
    }

    @Override
    public @NotNull List<Slot> getInputGridSlots() {
        return List.of(this.inputSlot);
    }

    @Override
    protected @NotNull List<Slot> getSlotsToClear() {
        return List.of(this.inputSlot, this.resultSlot);
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
        return RecipeBookType.FURNACE;
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
                if (!this.moveItemStackTo(slotStack, 4, 40, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotStack, stack);
            } else if (index >= 4 && index < 31) {
                // If transferring from the player's backpack, put wands in the wand slot, ignyx in the ignyx slot, and everything else into the input slot or hotbar, in that order
                if (this.wandSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 3, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.ignyxSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(slotStack, 1, 2, false) && !this.moveItemStackTo(slotStack, 31, 40, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 31 && index < 40) {
                // If transferring from the player's hotbar, put wands in the wand slot, ignyx in the ignyx slot, and everything else into the input slot or backpack, in that order
                if (this.wandSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 3, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.ignyxSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(slotStack, 1, 2, false) && !this.moveItemStackTo(slotStack, 4, 31, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotStack, 4, 40, false)) {
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
}
