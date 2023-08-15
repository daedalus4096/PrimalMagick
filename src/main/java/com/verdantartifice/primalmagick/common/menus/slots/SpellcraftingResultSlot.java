package com.verdantartifice.primalmagick.common.menus.slots;

import java.util.function.Supplier;

import com.verdantartifice.primalmagick.common.crafting.WandInventory;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.stats.StatsPM;
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
    protected int amountCrafted;

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

    @Override
    protected void checkTakeAchievements(ItemStack stack) {
        super.checkTakeAchievements(stack);
        
        // Increment the craft counter statistic for the discipline
        if (this.amountCrafted > 0) {
            StatsManager.incrementValue(this.player, StatsPM.CRAFTED_SORCERY, this.amountCrafted);
        }
        
        // Reset crafted amount
        this.amountCrafted = 0;
    }
    
    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }
    
    @Override
    public ItemStack remove(int amount) {
        if (this.hasItem()) {
            this.amountCrafted += Math.min(amount, this.getItem().getCount());
        }
        return super.remove(amount);
    }
    
    @Override
    protected void onQuickCraft(ItemStack stack, int amount) {
        this.amountCrafted += amount;
        super.onQuickCraft(stack, amount);
    }

    @Override
    protected void onSwapCraft(int amount) {
        this.amountCrafted += amount;
        super.onSwapCraft(amount);
    }
}
