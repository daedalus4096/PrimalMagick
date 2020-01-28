package com.verdantartifice.primalmagic.common.containers.slots;

import com.verdantartifice.primalmagic.common.crafting.IArcaneRecipe;
import com.verdantartifice.primalmagic.common.crafting.WandInventory;
import com.verdantartifice.primalmagic.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagic.common.research.ResearchDisciplines;
import com.verdantartifice.primalmagic.common.research.ResearchEntries;
import com.verdantartifice.primalmagic.common.research.ResearchEntry;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sources.SourceList;
import com.verdantartifice.primalmagic.common.stats.Stat;
import com.verdantartifice.primalmagic.common.stats.StatsManager;
import com.verdantartifice.primalmagic.common.wands.IWand;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.item.ItemStack;

/**
 * Custom GUI slot for arcane crafting results.
 * 
 * @author Daedalus4096
 */
public class ArcaneCraftingResultSlot extends CraftingResultSlot {
    protected final WandInventory wandInventory;
    protected final PlayerEntity player;

    public ArcaneCraftingResultSlot(PlayerEntity player, CraftingInventory craftingInventory, WandInventory wandInventory, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
        super(player, craftingInventory, inventoryIn, slotIndex, xPosition, yPosition);
        this.wandInventory = wandInventory;
        this.player = player;
    }

    @Override
    protected void onCrafting(ItemStack stack) {
        // Do additional processing if the crafted recipe was arcane
        if (this.inventory instanceof IRecipeHolder) {
            IRecipeHolder holder = (IRecipeHolder)this.inventory;
            if (holder.getRecipeUsed() != null && holder.getRecipeUsed() instanceof IArcaneRecipe) {
                // Consume the recipe's mana cost from the wand
                SourceList manaCosts = ((IArcaneRecipe)holder.getRecipeUsed()).getManaCosts();
                if (!manaCosts.isEmpty()) {
                    ItemStack wandStack = this.wandInventory.getStackInSlot(0);
                    if (wandStack != null && !wandStack.isEmpty() && wandStack.getItem() instanceof IWand) {
                        ((IWand)wandStack.getItem()).consumeMana(wandStack, this.player, manaCosts);
                    }
                }
                
                // Increment the craft counter for the recipe's discipline
                if (this.player instanceof ServerPlayerEntity) {
                    SimpleResearchKey key = ((IArcaneRecipe)holder.getRecipeUsed()).getRequiredResearch();
                    ResearchEntry entry = ResearchEntries.getEntry(key);
                    if (entry != null) {
                        ResearchDiscipline disc = ResearchDisciplines.getDiscipline(entry.getDisciplineKey());
                        if (disc != null) {
                            Stat craftingStat = disc.getCraftingStat();
                            if (craftingStat != null) {
                                StatsManager.incrementValue((ServerPlayerEntity)this.player, craftingStat, stack.getCount());
                            }
                        }
                    }
                }
            }
        }
        super.onCrafting(stack);
    }
}
