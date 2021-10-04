package com.verdantartifice.primalmagic.common.crafting.recipe_book;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.verdantartifice.primalmagic.common.containers.AbstractArcaneRecipeBookMenu;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.recipebook.PlaceRecipe;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

public class ServerPlaceArcaneRecipe<C extends Container> implements PlaceRecipe<Integer> {
    protected static final Logger LOGGER = LogManager.getLogger();
    protected final StackedContents stackedContents = new StackedContents();
    protected final AbstractArcaneRecipeBookMenu<C> menu;
    protected Inventory inventory;
    
    public ServerPlaceArcaneRecipe(AbstractArcaneRecipeBookMenu<C> menu) {
        this.menu = menu;
    }
    
    public void recipeClicked(ServerPlayer player, @Nullable Recipe<C> recipe, boolean shiftDown) {
        if (recipe != null) {
            boolean inVanillaBook = player.getRecipeBook().contains(recipe);
            boolean inArcaneBook = ArcaneRecipeBookManager.containsRecipe(player, recipe);
            if (inVanillaBook || inArcaneBook) {
                this.inventory = player.getInventory();
                if (this.testClearGrid() || player.isCreative()) {
                    this.stackedContents.clear();
                    player.getInventory().fillStackedContents(this.stackedContents);
                    this.menu.fillCraftSlotsStackedContents(this.stackedContents);
                    if (this.stackedContents.canCraft(recipe, null)) {
                        this.handleRecipeClicked(recipe, shiftDown);
                    } else {
                        this.clearGrid();
                        // TODO Fire place ghost recipe packet
                    }
                    player.getInventory().setChanged();
                }
            }
        }
    }
    
    protected void clearGrid() {
        for (int index = 0; index < this.menu.getSize(); index++) {
            if (this.menu.shouldMoveToInventory(index)) {
                ItemStack stack = this.menu.getSlot(index).getItem().copy();
                this.inventory.placeItemBackInInventory(stack, false);
                this.menu.getSlot(index).set(stack);
            }
        }
        this.menu.clearCraftingContent();
    }
    
    protected void handleRecipeClicked(Recipe<C> recipe, boolean shiftDown) {
        boolean matches = this.menu.recipeMatches(recipe);
        int i = this.stackedContents.getBiggestCraftableStack(recipe, null);
        
        if (matches) {
            for (int index = 0; index < this.menu.getGridHeight() * this.menu.getGridWidth() + 1; index++) {
                if (index != this.menu.getResultSlotIndex()) {
                    ItemStack stack = this.menu.getSlot(index).getItem();
                    if (!stack.isEmpty() && Math.min(i, stack.getMaxStackSize()) < stack.getCount() + 1) {
                        return;
                    }
                }
            }
        }
        
        int stackSize = this.getStackSize(shiftDown, i, matches);
        IntList list = new IntArrayList();
        if (this.stackedContents.canCraft(recipe, list, stackSize)) {
            int curSize = stackSize;
            for (int value : list) {
                int size = StackedContents.fromStackingIndex(value).getMaxStackSize();
                if (size < curSize) {
                    curSize = size;
                }
            }
            if (this.stackedContents.canCraft(recipe, list, curSize)) {
                this.clearGrid();
                this.placeRecipe(this.menu.getGridWidth(), this.menu.getGridHeight(), this.menu.getResultSlotIndex(), recipe, list.iterator(), curSize);
            }
        }
    }

    @Override
    public void addItemToSlot(Iterator<Integer> iterator, int slotIndex, int count, int p_135418_, int p_135419_) {
        Slot slot = this.menu.getSlot(slotIndex);
        ItemStack stack = StackedContents.fromStackingIndex(iterator.next());
        if (!stack.isEmpty()) {
            for (int index = 0; index < count; index++) {
                this.moveItemToGrid(slot, stack);
            }
        }
    }

    protected int getStackSize(boolean shiftDown, int stackSize, boolean matches) {
        int retVal = 1;
        if (shiftDown) {
            retVal = stackSize;
        } else if (matches) {
            retVal = 64;
            for (int index = 0; index < this.menu.getGridWidth() * this.menu.getGridHeight() + 1; index++) {
                if (index != this.menu.getResultSlotIndex()) {
                    ItemStack stack = this.menu.getSlot(index).getItem();
                    if (!stack.isEmpty() && retVal > stack.getCount()) {
                        retVal = stack.getCount();
                    }
                }
            }
            if (retVal < 64) {
                retVal++;
            }
        }
        return retVal;
    }
    
    protected void moveItemToGrid(Slot slot, ItemStack stack) {
        int index = this.inventory.findSlotMatchingUnusedItem(stack);
        if (index != -1) {
            ItemStack invStack = this.inventory.getItem(index).copy();
            if (!invStack.isEmpty()) {
                if (invStack.getCount() > 1) {
                    this.inventory.removeItem(index, 1);
                } else {
                    this.inventory.removeItemNoUpdate(index);
                }
                
                invStack.setCount(1);
                if (slot.getItem().isEmpty()) {
                    slot.set(invStack);
                } else {
                    slot.getItem().grow(1);
                }
            }
        }
    }
    
    protected boolean testClearGrid() {
        List<ItemStack> stackList = new ArrayList<>();
        int count = this.getAmountOfFreeSlotsInInventory();
        
        for (int index = 0; index < this.menu.getGridWidth() * this.menu.getGridHeight() + 1; index++) {
            if (index != this.menu.getResultSlotIndex()) {
                ItemStack stack = this.menu.getSlot(index).getItem().copy();
                if (!stack.isEmpty()) {
                    int slotIndex = this.inventory.getSlotWithRemainingSpace(stack);
                    if (slotIndex == -1 && stackList.size() < count) {
                        for (ItemStack listStack : stackList) {
                            if (listStack.sameItem(stack) && listStack.getCount() != listStack.getMaxStackSize() && listStack.getCount() + stack.getCount() <= listStack.getMaxStackSize()) {
                                listStack.grow(stack.getCount());
                                stack.setCount(0);
                                break;
                            }
                        }
                        if (!stack.isEmpty()) {
                            if (stackList.size() >= count) {
                                return false;
                            }
                            stackList.add(stack);
                        }
                    } else if (slotIndex == -1) {
                        return false;
                    }
                }
            }
        }
        
        return true;
    }
    
    protected int getAmountOfFreeSlotsInInventory() {
        int retVal = 0;
        for (ItemStack stack : this.inventory.items) {
            if (stack.isEmpty()) {
                retVal++;
            }
        }
        return retVal;
    }
}
