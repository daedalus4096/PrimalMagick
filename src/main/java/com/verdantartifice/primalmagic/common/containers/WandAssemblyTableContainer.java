package com.verdantartifice.primalmagic.common.containers;

import java.util.Optional;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.containers.slots.WandCapSlot;
import com.verdantartifice.primalmagic.common.containers.slots.WandCoreSlot;
import com.verdantartifice.primalmagic.common.containers.slots.WandGemSlot;
import com.verdantartifice.primalmagic.common.crafting.WandAssemblyRecipe;

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
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Server data container for the wand assembly table GUI.
 * 
 * @author Daedalus4096
 */
public class WandAssemblyTableContainer extends Container {
    protected static final ResourceLocation RECIPE_LOC = new ResourceLocation(PrimalMagic.MODID, "wand_assembly");
    
    protected final IWorldPosCallable worldPosCallable;
    protected final WandComponentInventory componentInv = new WandComponentInventory();
    protected final CraftResultInventory resultInv = new CraftResultInventory();
    protected final PlayerEntity player;
    protected final Slot coreSlot;
    protected final Slot capSlot;
    protected final Slot gemSlot;
    
    public WandAssemblyTableContainer(int windowId, PlayerInventory inv) {
        this(windowId, inv, IWorldPosCallable.DUMMY);
    }
    
    public WandAssemblyTableContainer(int windowId, PlayerInventory inv, IWorldPosCallable callable) {
        super(ContainersPM.WAND_ASSEMBLY_TABLE, windowId);
        this.worldPosCallable = callable;
        this.player = inv.player;
        
        // Slot 0: Result
        this.addSlot(new CraftingResultSlot(this.player, this.componentInv, this.resultInv, 0, 124, 35));
        
        // Slot 1: Wand core
        this.coreSlot = this.addSlot(new WandCoreSlot(this.componentInv, 0, 48, 35));
        
        // Slot 2: Wand gem
        this.gemSlot = this.addSlot(new WandGemSlot(this.componentInv, 1, 48, 17));
        
        // Slots 3-4: Wand caps
        this.capSlot = this.addSlot(new WandCapSlot(this.componentInv, 2, 30, 53));
        this.addSlot(new WandCapSlot(this.componentInv, 3, 66, 17));
        
        // Slots 5-31: Player backpack
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(inv, j + (i * 9) + 9, 8 + (j * 18), 84 + (i * 18)));
            }
        }
        
        // Slots 32-40: Player hotbar
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(inv, i, 8 + (i * 18), 142));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(this.worldPosCallable, playerIn, BlocksPM.WAND_ASSEMBLY_TABLE);
    }
    
    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        // Return crafting inputs to the player's inventory when GUI is closed
        super.onContainerClosed(playerIn);
        this.worldPosCallable.consume((world, blockPos) -> {
            this.clearContainer(playerIn, world, this.componentInv);
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
                // If transferring the output item, move it into the player's backpack or hotbar
                if (!this.mergeItemStack(slotStack, 5, 41, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(slotStack, stack);
            } else if (index >= 5 && index < 32) {
                // If transferring from the backpack, move cores, caps, and gems to the appropriate slots, and everything else to the hotbar
                if (this.coreSlot.isItemValid(slotStack)) {
                    if (!this.mergeItemStack(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.gemSlot.isItemValid(slotStack)) {
                    if (!this.mergeItemStack(slotStack, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.capSlot.isItemValid(slotStack)) {
                    if (!this.mergeItemStack(slotStack, 3, 5, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.mergeItemStack(slotStack, 32, 41, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (index >= 32 && index < 41) {
                // If transferring from the hotbar, move cores, caps, and gems to the appropriate slots, and everything else to the backpack
                if (this.coreSlot.isItemValid(slotStack)) {
                    if (!this.mergeItemStack(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.gemSlot.isItemValid(slotStack)) {
                    if (!this.mergeItemStack(slotStack, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.capSlot.isItemValid(slotStack)) {
                    if (!this.mergeItemStack(slotStack, 3, 5, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.mergeItemStack(slotStack, 5, 32, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.mergeItemStack(slotStack, 5, 41, false)) {
                // Move all other transfers to the backpack or hotbar
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
        super.onCraftMatrixChanged(inventoryIn);
        this.worldPosCallable.consume((world, blockPos) -> {
            this.slotChangedCraftingGrid(world);
        });
    }
    
    protected void slotChangedCraftingGrid(World world) {
        if (!world.isRemote && this.player instanceof ServerPlayerEntity) {
            ServerPlayerEntity spe = (ServerPlayerEntity)this.player;
            ItemStack stack = ItemStack.EMPTY;
            Optional<? extends IRecipe<?>> opt = world.getServer().getRecipeManager().getRecipe(RECIPE_LOC);
            if (opt.isPresent() && opt.get() instanceof WandAssemblyRecipe) {
                // If the inputs make a valid wand, show the output
                WandAssemblyRecipe recipe = (WandAssemblyRecipe)opt.get();
                if (recipe.matches(this.componentInv, world)) {
                    stack = recipe.getCraftingResult(this.componentInv);
                }
            }
            
            // Send a packet to the client to update its GUI with the shown output
            this.resultInv.setInventorySlotContents(0, stack);
            spe.connection.sendPacket(new SSetSlotPacket(this.windowId, 0, stack));
        }
    }

    protected class WandComponentInventory extends CraftingInventory {
        public WandComponentInventory() {
            super(WandAssemblyTableContainer.this, 2, 2);
        }
        
        @Override
        public int getInventoryStackLimit() {
            return 1;
        }
    }
}
