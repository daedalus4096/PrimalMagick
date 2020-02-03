package com.verdantartifice.primalmagic.common.containers.slots;

import java.util.function.Supplier;

import com.verdantartifice.primalmagic.common.crafting.WandInventory;
import com.verdantartifice.primalmagic.common.sources.SourceList;
import com.verdantartifice.primalmagic.common.wands.IWand;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.item.ItemStack;

/**
 * Custom GUI slot for spellcrafting altar outputs.
 * 
 * @author Daedalus4096
 */
public class SpellcraftingResultSlot extends CraftingResultSlot {
    protected final WandInventory wandInventory;
    protected final PlayerEntity player;
    protected final Supplier<SourceList> costSupplier;

    public SpellcraftingResultSlot(PlayerEntity player, CraftingInventory craftingInventory, WandInventory wandInventory, Supplier<SourceList> costSupplier, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
        super(player, craftingInventory, inventoryIn, slotIndex, xPosition, yPosition);
        this.wandInventory = wandInventory;
        this.player = player;
        this.costSupplier = costSupplier;
    }
    
    @Override
    protected void onCrafting(ItemStack stack) {
        // Deduct the cost of the spell from the wand
        SourceList manaCosts = this.costSupplier.get();
        if (manaCosts != null && !manaCosts.isEmpty()) {
            ItemStack wandStack = this.wandInventory.getStackInSlot(0);
            if (wandStack != null && !wandStack.isEmpty() && wandStack.getItem() instanceof IWand) {
                ((IWand)wandStack.getItem()).consumeRealMana(wandStack, this.player, manaCosts);
            }
        }
        super.onCrafting(stack);
    }
}
