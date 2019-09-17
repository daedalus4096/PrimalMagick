package com.verdantartifice.primalmagic.common.containers;

import java.util.Optional;

import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.crafting.IArcaneRecipe;
import com.verdantartifice.primalmagic.common.crafting.RecipeTypesPM;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.world.World;

public class ArcaneWorkbenchContainer extends Container {
    protected final CraftingInventory craftingInv = new CraftingInventory(this, 3, 3);
    protected final CraftResultInventory resultInv = new CraftResultInventory();
    protected final IWorldPosCallable worldPosCallable;
    protected final PlayerEntity player;
    
    public ArcaneWorkbenchContainer(int windowId, PlayerInventory inv) {
        this(windowId, inv, IWorldPosCallable.DUMMY);
    }
    
    public ArcaneWorkbenchContainer(int windowId, PlayerInventory inv, IWorldPosCallable callable) {
        super(ContainersPM.ARCANE_WORKBENCH, windowId);
        this.worldPosCallable = callable;
        this.player = inv.player;
        
        this.addSlot(new CraftingResultSlot(this.player, this.craftingInv, this.resultInv, 0, 124, 35));
        
        int i, j;
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 3; j++) {
                this.addSlot(new Slot(this.craftingInv, j + i * 3, 30 + j * 18, 17 + i * 18));
            }
        }
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 9; j++) {
                this.addSlot(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (i = 0; i < 9; i++) {
            this.addSlot(new Slot(inv, i, 8 + i * 18, 142));
        }
    }
    
    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(this.worldPosCallable, playerIn, BlocksPM.ARCANE_WORKBENCH);
    }
    
    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        this.worldPosCallable.consume((world, blockPos) -> {
            this.clearContainer(playerIn, world, this.craftingInv);
        });
    }
    
    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            stack = slotStack.copy();
            if (index == 0) {
                this.worldPosCallable.consume((world, blockPos) -> {
                    slotStack.getItem().onCreated(slotStack, world, playerIn);
                });
                if (!this.mergeItemStack(slotStack, 10, 46, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(slotStack, stack);
            } else if (index >= 10 && index < 37) {
                if (!this.mergeItemStack(slotStack, 37, 46, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 37 && index < 46) {
                if (!this.mergeItemStack(slotStack, 10, 37, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(slotStack, 10, 46, false)) {
                return ItemStack.EMPTY;
            }
            
            if (slotStack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
            
            if (slotStack.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }
            
            ItemStack taken = slot.onTake(playerIn, slotStack);
            if (index == 0) {
                playerIn.dropItem(taken, false);
            }
        }
        return stack;
    }
    
    @Override
    public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
        return slotIn.inventory != this.resultInv && super.canMergeSlot(stack, slotIn);
    }
    
    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn) {
        this.worldPosCallable.consume((world, blockPos) -> {
            slotChangedCraftingGrid(this.windowId, world, this.player, this.craftingInv, this.resultInv);
        });
    }
    
    protected static void slotChangedCraftingGrid(int windowId, World world, PlayerEntity player, CraftingInventory craftInv, CraftResultInventory resultInv) {
        if (!world.isRemote && player instanceof ServerPlayerEntity) {
            ServerPlayerEntity spe = (ServerPlayerEntity)player;
            ItemStack stack = ItemStack.EMPTY;
            Optional<IArcaneRecipe> arcaneOptional = world.getServer().getRecipeManager().getRecipe(RecipeTypesPM.ARCANE_CRAFTING, craftInv, world);
            if (arcaneOptional.isPresent()) {
                IArcaneRecipe recipe = arcaneOptional.get();
                if (resultInv.canUseRecipe(world, spe, recipe) && (recipe.getRequiredResearch() == null || recipe.getRequiredResearch().isKnownByStrict(spe))) {
                    stack = recipe.getCraftingResult(craftInv);
                }
            } else {
                Optional<ICraftingRecipe> vanillaOptional = world.getServer().getRecipeManager().getRecipe(IRecipeType.CRAFTING, craftInv, world);
                if (vanillaOptional.isPresent()) {
                    ICraftingRecipe recipe = vanillaOptional.get();
                    if (resultInv.canUseRecipe(world, spe, recipe)) {
                        stack = recipe.getCraftingResult(craftInv);
                    }
                }
            }
            resultInv.setInventorySlotContents(0, stack);
            spe.connection.sendPacket(new SSetSlotPacket(windowId, 0, stack));
        }
    }
}
