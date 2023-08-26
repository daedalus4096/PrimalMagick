package com.verdantartifice.primalmagick.common.menus;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.crafting.recipe_book.ArcaneRecipeBookType;
import com.verdantartifice.primalmagick.common.menus.slots.FilteredSlot;
import com.verdantartifice.primalmagick.common.menus.slots.GenericResultSlot;
import com.verdantartifice.primalmagick.common.menus.slots.WandSlot;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

/**
 * Server data container for the infernal furnace GUI.
 * 
 * @author Daedalus4096
 */
public class InfernalFurnaceMenu extends AbstractArcaneRecipeBookMenu<Container> {
    public static final ResourceLocation IGNYX_SLOT_TEXTURE = PrimalMagick.resource("item/empty_ignyx_slot");

    protected final Container furnaceInv;
    protected final ContainerData furnaceData;
    protected final Slot inputSlot;
    protected final Slot ignyxSlot;
    protected final Slot wandSlot;
    protected final Level level;
    
    public InfernalFurnaceMenu(int id, Inventory playerInv) {
        this(id, playerInv, new SimpleContainer(4), new SimpleContainerData(6));
    }
    
    public InfernalFurnaceMenu(int id, Inventory playerInv, Container furnaceInv, ContainerData furnaceData) {
        super(MenuTypesPM.INFERNAL_FURNACE.get(), id);
        checkContainerSize(furnaceInv, 3);
        checkContainerDataCount(furnaceData, 4);
        this.furnaceInv = furnaceInv;
        this.furnaceData = furnaceData;
        this.level = playerInv.player.level();
        
        // Slot 0: chamber output
        this.addSlot(new GenericResultSlot(playerInv.player, this.furnaceInv, 0, 125, 35)); // FIXME Should this be a special slot type instead for handling XP drops?
        
        // Slot 1: material input
        this.inputSlot = this.addSlot(new Slot(this.furnaceInv, 1, 44, 17));
        
        // Slot 2: ignyx input
        this.ignyxSlot = this.addSlot(new FilteredSlot(this.furnaceInv, 2, 44, 53, 
                new FilteredSlot.Properties().background(IGNYX_SLOT_TEXTURE).tag(ItemTagsPM.INFERNAL_SUPERCHARGE_FUEL)));
        
        // Slot 3: wand input
        this.wandSlot = this.addSlot(new WandSlot(this.furnaceInv, 3, 8, 62, false));
        
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

    @Override
    public void fillCraftSlotsStackedContents(StackedContents contents) {
        if (this.furnaceInv instanceof StackedContentsCompatible stackedContainer) {
            stackedContainer.fillStackedContents(contents);
        }
    }

    @Override
    public void clearCraftingContent() {
        this.getSlot(0).set(ItemStack.EMPTY);
        this.getSlot(1).set(ItemStack.EMPTY);
    }

    @Override
    public boolean recipeMatches(Recipe<? super Container> recipe) {
        return recipe.matches(this.furnaceInv, this.level);
    }

    @Override
    public int getResultSlotIndex() {
        return 0;
    }

    @Override
    public int getGridWidth() {
        return 1;
    }

    @Override
    public int getGridHeight() {
        return 1;
    }

    @Override
    public int getSize() {
        return 3;
    }

    @Override
    public ArcaneRecipeBookType getRecipeBookType() {
        return ArcaneRecipeBookType.FURNACE;
    }

    @Override
    public boolean shouldMoveToInventory(int index) {
        return index != this.getResultSlotIndex();
    }

    @Override
    public boolean isSingleIngredientMenu() {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
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

    @Override
    public boolean stillValid(Player pPlayer) {
        return this.furnaceInv.stillValid(pPlayer);
    }
}
