package com.verdantartifice.primalmagic.common.containers;

import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.containers.slots.LapisLazuliSlot;
import com.verdantartifice.primalmagic.common.containers.slots.StoneSlabSlot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Server data container for the runecarving table GUI.
 * 
 * @author Daedalus4096
 */
public class RunecarvingTableContainer extends Container {
    protected final IWorldPosCallable worldPosCallable;
    protected final IntReferenceHolder selectedRecipe = IntReferenceHolder.single();
    protected final World world;
    
    protected final Slot inputSlabSlot;
    protected final Slot inputLapisSlot;
    protected final Slot outputSlot;
    
    protected final IInventory inputInventory = new Inventory(2) {
        public void markDirty() {
            super.markDirty();
            RunecarvingTableContainer.this.onCraftMatrixChanged(this);
            RunecarvingTableContainer.this.inventoryUpdateListener.run();
        };
    };
    protected final CraftResultInventory outputInventory = new CraftResultInventory();

    // TODO Recipe list
    
    protected ItemStack slabInput = ItemStack.EMPTY;
    protected ItemStack lapisInput = ItemStack.EMPTY;

    /**
     * Stores the game time of the last time the player took items from the the crafting result slot. This is used to
     * prevent the sound from being played multiple times on the same tick.
     */
    protected long lastOnTake;
    protected Runnable inventoryUpdateListener = () -> {};

    public RunecarvingTableContainer(int windowId, PlayerInventory inv) {
        this(windowId, inv, IWorldPosCallable.DUMMY);
    }
    
    public RunecarvingTableContainer(int windowId, PlayerInventory inv, IWorldPosCallable worldPosCallable) {
        super(ContainersPM.RUNECARVING_TABLE.get(), windowId);
        this.worldPosCallable = worldPosCallable;
        this.world = inv.player.world;
        
        // Slot 0: input slabs
        this.inputSlabSlot = this.addSlot(new StoneSlabSlot(this.inputInventory, 0, 20, 21));
        
        // Slot 1: input lapis
        this.inputLapisSlot = this.addSlot(new LapisLazuliSlot(this.inputInventory, 1, 20, 46));
        
        // Slot 2: runecarving output
        this.outputSlot = this.addSlot(new Slot(this.outputInventory, 0, 143, 33) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return false;
            }
            
            @Override
            public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
                RunecarvingTableContainer.this.inputSlabSlot.decrStackSize(1);
                RunecarvingTableContainer.this.inputLapisSlot.decrStackSize(1);
                RunecarvingTableContainer.this.updateRecipeResultSlot();
                
                stack.getItem().onCreated(stack, thePlayer.world, thePlayer);
                RunecarvingTableContainer.this.worldPosCallable.consume((world, pos) -> {
                    long time = world.getGameTime();
                    if (RunecarvingTableContainer.this.lastOnTake != time) {
                        world.playSound(null, pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        RunecarvingTableContainer.this.lastOnTake = time;
                    }
                });
                return super.onTake(thePlayer, stack);
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

        this.trackInt(this.selectedRecipe);
    }

    @OnlyIn(Dist.CLIENT)
    public int getSelectedRecipe() {
       return this.selectedRecipe.get();
    }

//    @OnlyIn(Dist.CLIENT)
//    public List<StonecuttingRecipe> getRecipeList() {
//       return this.recipes;
//    }

    @OnlyIn(Dist.CLIENT)
    public int getRecipeListSize() {
        return 0;
//       return this.recipes.size();
    }

    @OnlyIn(Dist.CLIENT)
    public boolean hasItemsinInputSlot() {
        return this.inputSlabSlot.getHasStack() && this.inputLapisSlot.getHasStack() /* && !this.recipes.isEmpty() */;
    }
    
    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(this.worldPosCallable, playerIn, BlocksPM.RUNECARVING_TABLE.get());
    }

    @Override
    public boolean enchantItem(PlayerEntity playerIn, int id) {
//        if (id >= 0 && id < this.recipes.size()) {
//            this.selectedRecipe.set(id);
//            this.updateRecipeResultSlot();
//        }
        return true;
    }
    
    public void onCraftMatrixChanged(IInventory inventoryIn) {
        ItemStack slabStack = this.inputSlabSlot.getStack();
        ItemStack lapisStack = this.inputLapisSlot.getStack();
        if (slabStack.getItem() != this.slabInput.getItem() || lapisStack.getItem() != this.lapisInput.getItem()) {
            this.slabInput = slabStack.copy();
            this.lapisInput = lapisStack.copy();
            this.updateAvailableRecipes(inventoryIn, slabStack, lapisStack);
        }
    };
    
    protected void updateAvailableRecipes(IInventory inventoryIn, ItemStack slabStack, ItemStack lapisStack) {
//        this.recipes.clear();
        this.selectedRecipe.set(-1);
        this.outputSlot.putStack(ItemStack.EMPTY);
        if (!slabStack.isEmpty() && !lapisStack.isEmpty()) {
//            this.recipes = this.world.getRecipeManager().getRecipes(IRecipeType.STONECUTTING, inventoryIn, this.world);
        }
    }
    
    protected void updateRecipeResultSlot() {
//        if (!this.recipes.isEmpty()) {
//            StonecuttingRecipe stonecuttingrecipe = this.recipes.get(this.selectedRecipe.get());
//            this.outputInventorySlot.putStack(stonecuttingrecipe.getCraftingResult(this.inputInventory));
//        } else {
//            this.outputInventorySlot.putStack(ItemStack.EMPTY);
//        }
        this.detectAndSendChanges();
    }

    @OnlyIn(Dist.CLIENT)
    public void setInventoryUpdateListener(Runnable listenerIn) {
        this.inventoryUpdateListener = listenerIn;
    }
    
    @Override
    public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
        return slotIn.inventory != this.outputInventory && super.canMergeSlot(stack, slotIn);
    }
    
    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            stack = slotStack.copy();
            if (index == 2) {
                // If transferring the output item, move it into the player's backpack or hotbar
                slotStack.getItem().onCreated(slotStack, playerIn.world, playerIn);
                if (!this.mergeItemStack(slotStack, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(slotStack, stack);
            } else if (index == 0 || index == 1) {
                // If transferring one of the input items, move it into the player's backpack or hotbar
                if (!this.mergeItemStack(slotStack, 3, 39, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.inputSlabSlot.isItemValid(slotStack)) {
                // If transferring a valid ingredient, move it into the appropriate slot
                if (!this.mergeItemStack(slotStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.inputLapisSlot.isItemValid(slotStack)) {
                // If transferring a valid ingredient, move it into the appropriate slot
                if (!this.mergeItemStack(slotStack, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 3 && index < 30) {
                // If transferring from the backpack and not a valid fit, move to the hotbar
                if (!this.mergeItemStack(slotStack, 30, 39, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 30 && index < 39) {
                // If transferring from the hotbar and not a valid fit, move to the backpack
                if (!this.mergeItemStack(slotStack, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            }
            
            if (slotStack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            }
            
            slot.onSlotChanged();
            if (slotStack.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }
            
            slot.onTake(playerIn, slotStack);
            this.detectAndSendChanges();
        }
        
        return stack;
    }
    
    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        this.outputInventory.removeStackFromSlot(0);
        this.worldPosCallable.consume((world, pos) -> {
            this.clearContainer(playerIn, playerIn.world, this.inputInventory);
        });
    }
}
