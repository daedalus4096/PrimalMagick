package com.verdantartifice.primalmagick.common.menus.slots;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.verdantartifice.primalmagick.common.advancements.critereon.CriteriaTriggersPM;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.research.keys.RuneEnchantmentKey;
import com.verdantartifice.primalmagick.common.runes.Rune;
import com.verdantartifice.primalmagick.common.runes.RuneManager;
import com.verdantartifice.primalmagick.common.stats.ExpertiseManager;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.stats.StatsPM;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

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
        
        Level level = this.player.level();
        if (!level.isClientSide) {
            List<Rune> runes = RuneManager.getRunes(stack);
            Map<Holder<Enchantment>, Integer> enchants = RuneManager.getRuneEnchantments(level.registryAccess(), runes, stack, this.player, false);

            if (this.player instanceof ServerPlayer serverPlayer) {
                // Increment the player's runescribing craft stat
                StatsManager.incrementValue(serverPlayer, StatsPM.ITEMS_RUNESCRIBED, stack.getCount());
                
                // Award appropriate expertise and advancements for each enchant
                enchants.keySet().forEach(enchant -> {
                    ExpertiseManager.awardExpertise(serverPlayer, enchant);
                    CriteriaTriggersPM.RUNESCRIBING.trigger(serverPlayer, enchant);
                });
                
                // Assemble a frequency map of runes that go into the found enchants to determine which runes were used more than once
                Map<Rune, Integer> outputFrequencyMap = enchants.keySet().stream()
                        .filter(ench -> RuneManager.hasRuneDefinition(level.registryAccess(), ench))
                        .flatMap(ench -> RuneManager.getRuneDefinition(level.registryAccess(), ench).get().getRunes().stream())
                        .collect(Collectors.groupingBy(Function.identity(), Collectors.summingInt(element -> 1)));
                Map<Rune, Integer> inputFrequencyMap = runes.stream()
                        .collect(Collectors.groupingBy(Function.identity(), Collectors.summingInt(element -> 1)));
                inputFrequencyMap.entrySet().stream()
                        .map(entry -> Map.entry(entry.getKey(), Math.max(0, 1 + outputFrequencyMap.getOrDefault(entry.getKey(), 0) - inputFrequencyMap.get(entry.getKey()))))
                        .forEach(entry -> CriteriaTriggersPM.RUNE_USE_COUNT.trigger(serverPlayer, entry.getKey(), entry.getValue()));
            }

            // Grant the player rune enchantment research for each rune enchantment imbued
            if (!enchants.isEmpty() && !ResearchManager.isResearchComplete(this.player, ResearchEntries.UNLOCK_RUNE_ENCHANTMENTS)) {
                ResearchManager.completeResearch(this.player, ResearchEntries.UNLOCK_RUNE_ENCHANTMENTS);
            }
            enchants.keySet().forEach(enchant -> {
                ResearchManager.completeResearch(this.player, new RuneEnchantmentKey(enchant));
            });
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

    @Override
    protected void onQuickCraft(ItemStack stack, int amount) {
        // Handle crafting side effects
        this.checkTakeAchievements(stack);
    }
}
