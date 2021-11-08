package com.verdantartifice.primalmagick.common.containers.slots;

import java.util.List;
import java.util.Map;

import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagick.common.runes.Rune;
import com.verdantartifice.primalmagick.common.runes.RuneManager;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.stats.StatsPM;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * Custom GUI slot for runescribing results.
 * 
 * @author Daedalus4096
 */
public class RunescribingResultSlot extends Slot {
    protected final Player player;
    protected final Container inputInventory;

    public RunescribingResultSlot(Player player, Container inputInv, Container outputInv, int index, int xPosition, int yPosition) {
        super(outputInv, index, xPosition, yPosition);
        this.player = player;
        this.inputInventory = inputInv;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }
    
    @Override
    protected void checkTakeAchievements(ItemStack stack) {
        super.checkTakeAchievements(stack);
        
        if (!this.player.level.isClientSide) {
            // Increment the player's runescribing stat
            if (this.player instanceof ServerPlayer) {
                StatsManager.incrementValue((ServerPlayer)this.player, StatsPM.ITEMS_RUNESCRIBED, stack.getCount());
            }

            // Grant the player rune enchantment research for each rune enchantmant imbued
            if (ResearchManager.isResearchComplete(this.player, SimpleResearchKey.FIRST_STEPS)) {
                List<Rune> runes = RuneManager.getRunes(stack);
                Map<Enchantment, Integer> enchants = RuneManager.getRuneEnchantments(runes, stack, this.player, false);
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
    public void onTake(Player thePlayer, ItemStack stack) {
        // Handle crafting side effects
        this.checkTakeAchievements(stack);
        
        // Consume inputs
        for (int index = 0; index < this.inputInventory.getContainerSize(); index++) {
            ItemStack materialStack = this.inputInventory.getItem(index);
            if (!materialStack.isEmpty()) {
                this.inputInventory.removeItem(index, 1);
            }
        }
    }
}
