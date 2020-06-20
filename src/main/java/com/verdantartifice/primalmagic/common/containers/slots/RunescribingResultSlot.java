package com.verdantartifice.primalmagic.common.containers.slots;

import java.util.List;
import java.util.Map;

import com.verdantartifice.primalmagic.common.research.ResearchManager;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.runes.Rune;
import com.verdantartifice.primalmagic.common.runes.RuneManager;
import com.verdantartifice.primalmagic.common.stats.StatsManager;
import com.verdantartifice.primalmagic.common.stats.StatsPM;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

/**
 * Custom GUI slot for runescribing results.
 * 
 * @author Daedalus4096
 */
public class RunescribingResultSlot extends Slot {
    protected final PlayerEntity player;
    protected final IInventory inputInventory;

    public RunescribingResultSlot(PlayerEntity player, IInventory inputInv, IInventory outputInv, int index, int xPosition, int yPosition) {
        super(outputInv, index, xPosition, yPosition);
        this.player = player;
        this.inputInventory = inputInv;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }
    
    @Override
    protected void onCrafting(ItemStack stack) {
        super.onCrafting(stack);
        
        if (!this.player.world.isRemote) {
            // Increment the player's runescribing stat
            if (this.player instanceof ServerPlayerEntity) {
                StatsManager.incrementValue((ServerPlayerEntity)this.player, StatsPM.ITEMS_RUNESCRIBED, stack.getCount());
            }

            // Grant the player rune enchantment research for each rune enchantmant imbued
            if (ResearchManager.isResearchComplete(this.player, SimpleResearchKey.parse("FIRST_STEPS"))) {
                List<Rune> runes = RuneManager.getRunes(stack);
                Map<Enchantment, Integer> enchants = RuneManager.getRuneEnchantments(runes, stack, false);
                if (!enchants.isEmpty() && !ResearchManager.isResearchComplete(this.player, SimpleResearchKey.parse("UNLOCK_RUNE_ENCHANTMENTS"))) {
                    ResearchManager.completeResearch(this.player, SimpleResearchKey.parse("UNLOCK_RUNE_ENCHANTMENTS"));
                }
                for (Enchantment enchant : enchants.keySet()) {
                    SimpleResearchKey key = SimpleResearchKey.parseRuneEnchantment(enchant);
                    if (key != null) {
                        ResearchManager.completeResearch(this.player, key);
                    }
                }
            }
        }
    }
    
    @Override
    public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
        // Handle crafting side effects
        this.onCrafting(stack);
        
        // Consume inputs
        for (int index = 0; index < this.inputInventory.getSizeInventory(); index++) {
            ItemStack materialStack = this.inputInventory.getStackInSlot(index);
            if (!materialStack.isEmpty()) {
                this.inputInventory.decrStackSize(index, 1);
            }
        }
        
        return stack;
    }
}
