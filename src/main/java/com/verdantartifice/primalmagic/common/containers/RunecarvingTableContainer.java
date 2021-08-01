package com.verdantartifice.primalmagic.common.containers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.containers.slots.LapisLazuliSlot;
import com.verdantartifice.primalmagic.common.containers.slots.StoneSlabSlot;
import com.verdantartifice.primalmagic.common.crafting.IRunecarvingRecipe;
import com.verdantartifice.primalmagic.common.crafting.RecipeTypesPM;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Server data container for the runecarving table GUI.
 * 
 * @author Daedalus4096
 */
public class RunecarvingTableContainer extends AbstractContainerMenu {
    protected final ContainerLevelAccess worldPosCallable;
    protected final DataSlot selectedRecipe = DataSlot.standalone();
    protected final Player player;
    protected final Level world;
    
    protected final Slot inputSlabSlot;
    protected final Slot inputLapisSlot;
    protected final Slot outputSlot;
    
    protected final Container inputInventory = new SimpleContainer(2) {
        public void setChanged() {
            super.setChanged();
            RunecarvingTableContainer.this.slotsChanged(this);
            RunecarvingTableContainer.this.inventoryUpdateListener.run();
        };
    };
    protected final ResultContainer outputInventory = new ResultContainer();

    protected List<IRunecarvingRecipe> recipes = new ArrayList<>();
    
    protected ItemStack slabInput = ItemStack.EMPTY;
    protected ItemStack lapisInput = ItemStack.EMPTY;

    /**
     * Stores the game time of the last time the player took items from the the crafting result slot. This is used to
     * prevent the sound from being played multiple times on the same tick.
     */
    protected long lastOnTake;
    protected Runnable inventoryUpdateListener = () -> {};

    public RunecarvingTableContainer(int windowId, Inventory inv) {
        this(windowId, inv, ContainerLevelAccess.NULL);
    }
    
    public RunecarvingTableContainer(int windowId, Inventory inv, ContainerLevelAccess worldPosCallable) {
        super(ContainersPM.RUNECARVING_TABLE.get(), windowId);
        this.worldPosCallable = worldPosCallable;
        this.player = inv.player;
        this.world = inv.player.level;
        
        // Slot 0: input slabs
        this.inputSlabSlot = this.addSlot(new StoneSlabSlot(this.inputInventory, 0, 20, 21));
        
        // Slot 1: input lapis
        this.inputLapisSlot = this.addSlot(new LapisLazuliSlot(this.inputInventory, 1, 20, 46));
        
        // Slot 2: runecarving output
        this.outputSlot = this.addSlot(new Slot(this.outputInventory, 0, 143, 33) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
            
            @Override
            public void onTake(Player thePlayer, ItemStack stack) {
                RunecarvingTableContainer.this.inputSlabSlot.remove(1);
                RunecarvingTableContainer.this.inputLapisSlot.remove(1);
                RunecarvingTableContainer.this.updateRecipeResultSlot();
                
                stack.getItem().onCraftedBy(stack, thePlayer.level, thePlayer);
                RunecarvingTableContainer.this.worldPosCallable.execute((world, pos) -> {
                    long time = world.getGameTime();
                    if (RunecarvingTableContainer.this.lastOnTake != time) {
                        world.playSound(null, pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
                        RunecarvingTableContainer.this.lastOnTake = time;
                    }
                });
                super.onTake(thePlayer, stack);
            }
        });
        
        // Slots 3-29: Player backpack
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        
        // Slots 30-38: Player hotbar
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(inv, i, 8 + i * 18, 142));
        }

        this.addDataSlot(this.selectedRecipe);
    }

    public int getSelectedRecipe() {
        return this.selectedRecipe.get();
    }

    public List<IRunecarvingRecipe> getRecipeList() {
        return this.recipes;
    }

    public int getRecipeListSize() {
        return this.recipes.size();
    }

    public boolean hasItemsInInputSlot() {
        return this.inputSlabSlot.hasItem() && this.inputLapisSlot.hasItem() && !this.recipes.isEmpty();
    }
    
    @Override
    public boolean stillValid(Player playerIn) {
        return stillValid(this.worldPosCallable, playerIn, BlocksPM.RUNECARVING_TABLE.get());
    }

    @Override
    public boolean clickMenuButton(Player playerIn, int id) {
        if (id >= 0 && id < this.recipes.size()) {
            this.selectedRecipe.set(id);
            this.updateRecipeResultSlot();
        }
        return true;
    }
    
    public void slotsChanged(Container inventoryIn) {
        ItemStack slabStack = this.inputSlabSlot.getItem();
        ItemStack lapisStack = this.inputLapisSlot.getItem();
        if (slabStack.getItem() != this.slabInput.getItem() || lapisStack.getItem() != this.lapisInput.getItem()) {
            this.slabInput = slabStack.copy();
            this.lapisInput = lapisStack.copy();
            this.updateAvailableRecipes(inventoryIn, slabStack, lapisStack);
        }
    };
    
    protected void updateAvailableRecipes(Container inventoryIn, ItemStack slabStack, ItemStack lapisStack) {
        this.recipes.clear();
        this.selectedRecipe.set(-1);
        this.outputSlot.set(ItemStack.EMPTY);
        if (!slabStack.isEmpty() && !lapisStack.isEmpty()) {
            this.recipes = this.world.getRecipeManager().getRecipesFor(RecipeTypesPM.RUNECARVING, inventoryIn, this.world).stream()
                    .filter(r -> r != null && (r.getRequiredResearch() == null || r.getRequiredResearch().isKnownByStrict(this.player)))
                    .collect(Collectors.toList());
        }
    }
    
    protected void updateRecipeResultSlot() {
        if (!this.recipes.isEmpty()) {
            IRunecarvingRecipe recipe = this.recipes.get(this.selectedRecipe.get());
            this.outputSlot.set(recipe.assemble(this.inputInventory));
        } else {
            this.outputSlot.set(ItemStack.EMPTY);
        }
        this.broadcastChanges();
    }

    public void setInventoryUpdateListener(Runnable listenerIn) {
        this.inventoryUpdateListener = listenerIn;
    }
    
    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slotIn) {
        return slotIn.container != this.outputInventory && super.canTakeItemForPickAll(stack, slotIn);
    }
    
    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            if (index == 2) {
                // If transferring the output item, move it into the player's backpack or hotbar
                slotStack.getItem().onCraftedBy(slotStack, playerIn.level, playerIn);
                if (!this.moveItemStackTo(slotStack, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotStack, stack);
            } else if (index == 0 || index == 1) {
                // If transferring one of the input items, move it into the player's backpack or hotbar
                if (!this.moveItemStackTo(slotStack, 3, 39, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.inputSlabSlot.mayPlace(slotStack)) {
                // If transferring a valid ingredient, move it into the appropriate slot
                if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.inputLapisSlot.mayPlace(slotStack)) {
                // If transferring a valid ingredient, move it into the appropriate slot
                if (!this.moveItemStackTo(slotStack, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 3 && index < 30) {
                // If transferring from the backpack and not a valid fit, move to the hotbar
                if (!this.moveItemStackTo(slotStack, 30, 39, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 30 && index < 39) {
                // If transferring from the hotbar and not a valid fit, move to the backpack
                if (!this.moveItemStackTo(slotStack, 3, 30, false)) {
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
            
            slot.onTake(playerIn, slotStack);
            this.broadcastChanges();
        }
        
        return stack;
    }
    
    @Override
    public void removed(Player playerIn) {
        super.removed(playerIn);
        this.outputInventory.removeItemNoUpdate(0);
        this.clearContainer(playerIn, this.inputInventory);
    }
}
