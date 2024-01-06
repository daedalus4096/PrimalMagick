package com.verdantartifice.primalmagick.common.crafting.recipe_book;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.verdantartifice.primalmagick.common.menus.base.IArcaneRecipeBookMenu;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.recipe_book.PlaceGhostArcaneRecipePacket;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipebook.PlaceRecipe;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

public class ServerPlaceArcaneRecipe<C extends Container> implements PlaceRecipe<Integer> {
    protected static final Logger LOGGER = LogManager.getLogger();
    protected final StackedNbtContents stackedContents = new StackedNbtContents();
    protected final IArcaneRecipeBookMenu<C> menu;
    protected Inventory inventory;
    protected RecipeHolder<? extends Recipe<C>> activeRecipeHolder;
    
    public ServerPlaceArcaneRecipe(IArcaneRecipeBookMenu<C> menu) {
        this.menu = menu;
    }
    
    public void recipeClicked(ServerPlayer player, @Nullable RecipeHolder<? extends Recipe<C>> recipeHolder, boolean shiftDown) {
        if (recipeHolder != null) {
            boolean inVanillaBook = player.getRecipeBook().contains(recipeHolder);
            boolean inArcaneBook = ArcaneRecipeBookManager.containsRecipe(player, recipeHolder);
            if (inVanillaBook || inArcaneBook) {
                this.inventory = player.getInventory();
                if (this.testClearGrid() || player.isCreative()) {
                    this.stackedContents.clear();
                    player.getInventory().fillStackedContents(this.stackedContents);
                    this.menu.fillCraftSlotsStackedContents(this.stackedContents);
                    if (this.stackedContents.canCraft(recipeHolder.value(), null)) {
                        this.activeRecipeHolder = recipeHolder;
                        this.handleRecipeClicked(recipeHolder, shiftDown);
                    } else {
                        this.clearGrid();
                        PacketHandler.sendToPlayer(new PlaceGhostArcaneRecipePacket(player.containerMenu.containerId, recipeHolder), player);
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
    
    protected void handleRecipeClicked(RecipeHolder<? extends Recipe<C>> recipeHolder, boolean shiftDown) {
        boolean matches = this.menu.recipeMatches(recipeHolder);
        int i = this.stackedContents.getBiggestCraftableStack(recipeHolder, null);
        
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
        if (this.stackedContents.canCraft(recipeHolder.value(), list, stackSize)) {
            int curSize = stackSize;
            for (int value : list) {
                int size = StackedContents.fromStackingIndex(value).getMaxStackSize();
                if (size < curSize) {
                    curSize = size;
                }
            }
            if (this.stackedContents.canCraft(recipeHolder.value(), list, curSize)) {
                this.clearGrid();
                this.placeRecipe(this.menu.getGridWidth(), this.menu.getGridHeight(), this.menu.getResultSlotIndex(), recipeHolder, list.iterator(), curSize);
            }
        }
    }

    @Override
    public void addItemToSlot(Iterator<Integer> iterator, int slotIndex, int count, int p_135418_, int p_135419_) {
        Slot slot = this.menu.getSlot(slotIndex);
        int itemId = iterator.next();
        ItemStack stack = StackedContents.fromStackingIndex(itemId);
        if (this.stackedContents.hasNbtData(itemId)) {
            stack.setTag(this.findMatchingTag(itemId, stack));
        }
        if (!stack.isEmpty()) {
            for (int index = 0; index < count; index++) {
                this.moveItemToGrid(slot, stack);
            }
        }
    }
    
    protected CompoundTag findMatchingTag(int itemId, ItemStack baseStack) {
        ItemStack workingStack = baseStack.copy();
        if (this.activeRecipeHolder != null && this.activeRecipeHolder.value() != null) {
            for (CompoundTag tag : this.stackedContents.getNbtData(itemId)) {
                for (Ingredient ing : this.activeRecipeHolder.value().getIngredients()) {
                    workingStack.setTag(tag);
                    if (ing.test(workingStack)) {
                        return tag;
                    }
                }
            }
        }
        return new CompoundTag();
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
                            if (ItemStack.isSameItem(listStack, stack) && listStack.getCount() != listStack.getMaxStackSize() && listStack.getCount() + stack.getCount() <= listStack.getMaxStackSize()) {
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
