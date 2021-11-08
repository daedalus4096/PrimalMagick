package com.verdantartifice.primalmagick.common.containers.slots;

import java.util.function.Supplier;

import com.verdantartifice.primalmagick.common.crafting.WandInventory;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.wands.IWand;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;

/**
 * Custom GUI slot for spellcrafting altar outputs.
 * 
 * @author Daedalus4096
 */
public class SpellcraftingResultSlot extends ResultSlot {
    protected final WandInventory wandInventory;
    protected final Player player;
    protected final Supplier<SourceList> costSupplier;

    public SpellcraftingResultSlot(Player player, CraftingContainer craftingInventory, WandInventory wandInventory, Supplier<SourceList> costSupplier, Container inventoryIn, int slotIndex, int xPosition, int yPosition) {
        super(player, craftingInventory, inventoryIn, slotIndex, xPosition, yPosition);
        this.wandInventory = wandInventory;
        this.player = player;
        this.costSupplier = costSupplier;
    }
    
    @Override
    public void onTake(Player player, ItemStack stack) {
        // Deduct the cost of the spell from the wand
        SourceList manaCosts = this.costSupplier.get();
        if (manaCosts != null && !manaCosts.isEmpty()) {
            ItemStack wandStack = this.wandInventory.getItem(0);
            if (wandStack != null && !wandStack.isEmpty() && wandStack.getItem() instanceof IWand wand) {
                wand.consumeRealMana(wandStack, this.player, manaCosts);
            }
        }
        super.onTake(player, stack);
    }
}
