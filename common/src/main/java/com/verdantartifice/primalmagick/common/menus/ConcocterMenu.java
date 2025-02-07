package com.verdantartifice.primalmagick.common.menus;

import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.crafting.IConcoctingRecipe;
import com.verdantartifice.primalmagick.common.crafting.recipe_book.ArcaneRecipeBookType;
import com.verdantartifice.primalmagick.common.menus.base.AbstractTileSidedInventoryMenu;
import com.verdantartifice.primalmagick.common.menus.base.IArcaneRecipeBookMenu;
import com.verdantartifice.primalmagick.common.tiles.crafting.ConcocterTileEntity;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeHolder;

/**
 * Server data container for the concocter GUI.
 * 
 * @author Daedalus4096
 */
public class ConcocterMenu extends AbstractTileSidedInventoryMenu<ConcocterTileEntity> implements IArcaneRecipeBookMenu<CraftingInput, IConcoctingRecipe> {
    protected final ContainerData concocterData;
    protected final Slot wandSlot;
    
    public ConcocterMenu(int id, Inventory playerInv, BlockPos tilePos) {
        this(id, playerInv, tilePos, null, new SimpleContainerData(4));
    }
    
    @SuppressWarnings("resource")
    public ConcocterMenu(int id, Inventory playerInv, BlockPos tilePos, ConcocterTileEntity concocter, ContainerData concocterData) {
        super(MenuTypesPM.CONCOCTER.get(), id, ConcocterTileEntity.class, playerInv.player.level(), tilePos, concocter);
        checkContainerDataCount(concocterData, 4);
        this.concocterData = concocterData;
        
        // Slot 0: Output slot
        this.addSlot(Services.MENU.makeConcocterResultSlot(playerInv.player, this.getTileInventory(Direction.DOWN), 0, 138, 35));
        
        // Slots 1-9: Input slots
        int i, j;
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 3; j++) {
                this.addSlot(Services.MENU.makeSlot(this.getTileInventory(Direction.UP), j + i * 3, 44 + j * 18, 17 + i * 18));
            }
        }
        
        // Slot 10: Wand slot
        this.wandSlot = this.addSlot(Services.MENU.makeWandSlot(this.getTileInventory(Direction.NORTH), 0, 8, 62, false));
        
        // Slots 11-37: Player backpack
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        
        // Slots 38-46: Player hotbar
        for (i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInv, i, 8 + i * 18, 142));
        }
        
        this.addDataSlots(this.concocterData);
    }
    
    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            if (index == 0) {
                // If transferring the output item, move it into the player's backpack or hotbar
                if (!this.moveItemStackTo(slotStack, 11, 47, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotStack, stack);
            } else if (index >= 11 && index < 38) {
                // If transferring from the player's backpack, put wands in the wand slot and everything else into the input slots or hotbar, in that order
                if (this.wandSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 10, 11, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(slotStack, 1, 10, false) && !this.moveItemStackTo(slotStack, 38, 47, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 38 && index < 47) {
                // If transferring from the player's hotbar, put wands in the wand slot and everything else into the input slots or backpack, in that order
                if (this.wandSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 10, 11, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(slotStack, 1, 10, false) && !this.moveItemStackTo(slotStack, 11, 38, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotStack, 11, 47, false)) {
                // Move all other transfers into the backpack or hotbar
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

    public int getCookProgressionScaled() {
        // Determine how much of the cook arrow to show
        int i = this.concocterData.get(0);
        int j = this.concocterData.get(1);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }
    
    public int getCurrentMana() {
        return this.concocterData.get(2);
    }
    
    public int getMaxMana() {
        return this.concocterData.get(3);
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedContents stackedContents) {
        this.tile.fillStackedContents(stackedContents);
    }

    @Override
    public void clearCraftingContent() {
        for (int index = 0; index <= 9; index++) {
            this.getSlot(index).set(ItemStack.EMPTY);
        }
    }
    
    private static CraftingInput createRecipeInput(IItemHandlerPM container) {
        NonNullList<ItemStack> items = NonNullList.withSize(container.getSlots(), ItemStack.EMPTY);
        for (int index = 0; index < container.getSlots(); index++) {
            items.set(index, container.getStackInSlot(index));
        }
        return CraftingInput.of(IConcoctingRecipe.MAX_WIDTH, IConcoctingRecipe.MAX_HEIGHT, items);
    }

    @Override
    public boolean recipeMatches(RecipeHolder<IConcoctingRecipe> recipe) {
        return recipe.value().matches(createRecipeInput(this.getTileInventory(Direction.UP)), this.level);
    }

    @Override
    public int getResultSlotIndex() {
        return 0;
    }

    @Override
    public int getGridWidth() {
        return 3;
    }

    @Override
    public int getGridHeight() {
        return 3;
    }

    @Override
    public int getSize() {
        return 10;
    }

    @Override
    public ArcaneRecipeBookType getRecipeBookType() {
        return ArcaneRecipeBookType.CONCOCTER;
    }

    @Override
    public boolean shouldMoveToInventory(int index) {
        return index != this.getResultSlotIndex();
    }

    @Override
    public NonNullList<Slot> getSlots() {
        return this.slots;
    }
}
