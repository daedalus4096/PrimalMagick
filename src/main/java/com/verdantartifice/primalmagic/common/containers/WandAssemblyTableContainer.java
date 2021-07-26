package com.verdantartifice.primalmagic.common.containers;

import java.util.Optional;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.containers.slots.WandCapSlot;
import com.verdantartifice.primalmagic.common.containers.slots.WandCoreSlot;
import com.verdantartifice.primalmagic.common.containers.slots.WandGemSlot;
import com.verdantartifice.primalmagic.common.crafting.WandAssemblyRecipe;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

/**
 * Server data container for the wand assembly table GUI.
 * 
 * @author Daedalus4096
 */
public class WandAssemblyTableContainer extends AbstractContainerMenu {
    protected static final ResourceLocation RECIPE_LOC = new ResourceLocation(PrimalMagic.MODID, "wand_assembly");
    
    protected final ContainerLevelAccess worldPosCallable;
    protected final WandComponentInventory componentInv = new WandComponentInventory();
    protected final ResultContainer resultInv = new ResultContainer();
    protected final Player player;
    protected final Slot coreSlot;
    protected final Slot capSlot;
    protected final Slot gemSlot;
    
    public WandAssemblyTableContainer(int windowId, Inventory inv) {
        this(windowId, inv, ContainerLevelAccess.NULL);
    }
    
    public WandAssemblyTableContainer(int windowId, Inventory inv, ContainerLevelAccess callable) {
        super(ContainersPM.WAND_ASSEMBLY_TABLE.get(), windowId);
        this.worldPosCallable = callable;
        this.player = inv.player;
        
        // Slot 0: Result
        this.addSlot(new ResultSlot(this.player, this.componentInv, this.resultInv, 0, 124, 35));
        
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
    public boolean stillValid(Player playerIn) {
        return stillValid(this.worldPosCallable, playerIn, BlocksPM.WAND_ASSEMBLY_TABLE.get());
    }
    
    @Override
    public void removed(Player playerIn) {
        // Return crafting inputs to the player's inventory when GUI is closed
        super.removed(playerIn);
        this.clearContainer(playerIn, this.componentInv);
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
                if (!this.moveItemStackTo(slotStack, 5, 41, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotStack, stack);
            } else if (index >= 5 && index < 32) {
                // If transferring from the backpack, move cores, caps, and gems to the appropriate slots, and everything else to the hotbar
                if (this.coreSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.gemSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.capSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 3, 5, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.moveItemStackTo(slotStack, 32, 41, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (index >= 32 && index < 41) {
                // If transferring from the hotbar, move cores, caps, and gems to the appropriate slots, and everything else to the backpack
                if (this.coreSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.gemSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.capSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 3, 5, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.moveItemStackTo(slotStack, 5, 32, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(slotStack, 5, 41, false)) {
                // Move all other transfers to the backpack or hotbar
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
    
    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slotIn) {
        return slotIn.container != this.resultInv && super.canTakeItemForPickAll(stack, slotIn);
    }
    
    @Override
    public void slotsChanged(Container inventoryIn) {
        super.slotsChanged(inventoryIn);
        this.worldPosCallable.execute((world, blockPos) -> {
            this.slotChangedCraftingGrid(world);
        });
    }
    
    protected void slotChangedCraftingGrid(Level world) {
        if (!world.isClientSide && this.player instanceof ServerPlayer) {
            ServerPlayer spe = (ServerPlayer)this.player;
            ItemStack stack = ItemStack.EMPTY;
            Optional<? extends Recipe<?>> opt = world.getServer().getRecipeManager().byKey(RECIPE_LOC);
            if (opt.isPresent() && opt.get() instanceof WandAssemblyRecipe) {
                // If the inputs make a valid wand, show the output
                WandAssemblyRecipe recipe = (WandAssemblyRecipe)opt.get();
                if (recipe.matches(this.componentInv, world)) {
                    stack = recipe.assemble(this.componentInv);
                }
            }
            
            // Send a packet to the client to update its GUI with the shown output
            this.resultInv.setItem(0, stack);
            spe.connection.send(new ClientboundContainerSetSlotPacket(this.containerId, this.incrementStateId(), 0, stack));
        }
    }

    protected class WandComponentInventory extends CraftingContainer {
        public WandComponentInventory() {
            super(WandAssemblyTableContainer.this, 2, 2);
        }
        
        @Override
        public int getMaxStackSize() {
            return 1;
        }
    }
}
