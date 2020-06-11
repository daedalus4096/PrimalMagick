package com.verdantartifice.primalmagic.common.containers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.common.containers.slots.RunescribingResultSlot;
import com.verdantartifice.primalmagic.common.items.misc.RuneItem;
import com.verdantartifice.primalmagic.common.runes.Rune;
import com.verdantartifice.primalmagic.common.runes.RuneManager;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.world.World;

/**
 * Base server data container for the runescribing altar GUIs.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractRunescribingAltarContainer extends Container {
    protected final CraftingInventory altarInv = new CraftingInventory(this, 4, 3);
    protected final IInventory resultInv = new Inventory(1);
    protected final PlayerEntity player;
    protected final World world;
    protected final Slot runeSlot;

    public AbstractRunescribingAltarContainer(@Nonnull ContainerType<?> type, int id, @Nonnull PlayerInventory playerInv) {
        super(type, id);
        this.player = playerInv.player;
        this.world = this.player.world;
        
        // Slot 0: runescribing output
        this.addSlot(new RunescribingResultSlot(this.player, this.altarInv, this.resultInv, 0, 138, 35));
        
        // Slot 1: runescribing input
        this.addSlot(new Slot(this.altarInv, 0, 19, 35));
        
        // Slots 2-(R+1), where R = rune capacity: runes
        this.runeSlot = this.addRuneSlots();
        
        // Slots (R+2)-(R+28), where R = rune capacity: Player backpack
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        
        // Slots (R+29)-(R+37), where R = rune capacity: Player hotbar
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInv, i, 8 + i * 18, 142));
        }
    }
    
    /**
     * Get the number of runes holdable by this container.
     * 
     * @return the number of runes holdable by this container
     */
    protected abstract int getRuneCapacity();
    
    /**
     * Add slots for runes to this container.
     */
    @Nonnull
    protected abstract Slot addRuneSlots();
    
    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.altarInv.isUsableByPlayer(playerIn);
    }
    
    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        this.clearContainer(playerIn, this.world, this.altarInv);
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
                if (!this.mergeItemStack(slotStack, this.getRuneCapacity() + 2, this.getRuneCapacity() + 38, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(slotStack, stack);
            } else if (index >= (this.getRuneCapacity() + 2) && index < (this.getRuneCapacity() + 38)) {
                // If transferring from the player's backpack or hotbar, put runes in the rune section and everything else into the input slot
                if (this.runeSlot.isItemValid(slotStack)) {
                    if (!this.mergeItemStack(slotStack, 2, this.getRuneCapacity() + 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.mergeItemStack(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.mergeItemStack(slotStack, this.getRuneCapacity() + 2, this.getRuneCapacity() + 38, false)) {
                // Move all other transfers into the backpack or hotbar
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
        this.slotChangedCraftingGrid();
    }
    
    protected void slotChangedCraftingGrid() {
        if (!this.world.isRemote && this.player instanceof ServerPlayerEntity) {
            ServerPlayerEntity spe = (ServerPlayerEntity)this.player;
            ItemStack stack = ItemStack.EMPTY;
            ItemStack baseStack = this.altarInv.getStackInSlot(0);
            
            // Don't allow application to items that already have runes
            if (!RuneManager.hasRunes(baseStack)) {
                // Get the list of runes in the input
                List<Rune> runes = new ArrayList<>();
                for (int index = 1; index < this.altarInv.getSizeInventory(); index++) {
                    ItemStack inputStack = this.altarInv.getStackInSlot(index);
                    if (inputStack != null && inputStack.getItem() instanceof RuneItem) {
                        runes.add(((RuneItem)inputStack.getItem()).getRune());
                    }
                }

                // Determine what enchantments can be applied with the slotted rune combination
                Map<Enchantment, Integer> inputEnch = RuneManager.getRuneEnchantments(runes, baseStack);
                if (!inputEnch.isEmpty()) {
                    Map<Enchantment, Integer> finalEnch = RuneManager.mergeEnchantments(EnchantmentHelper.getEnchantments(baseStack), inputEnch);
                    stack = baseStack.copy();
                    EnchantmentHelper.setEnchantments(finalEnch, stack);
                    RuneManager.setRunes(stack, runes);
                }
            }
            
            // Send a packet to the client to update its GUI with the shown output
            this.resultInv.setInventorySlotContents(0, stack);
            spe.connection.sendPacket(new SSetSlotPacket(this.windowId, 0, stack));
        }
    }
}
