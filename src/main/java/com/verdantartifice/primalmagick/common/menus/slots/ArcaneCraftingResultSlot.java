package com.verdantartifice.primalmagick.common.menus.slots;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.verdantartifice.primalmagick.common.crafting.IArcaneRecipe;
import com.verdantartifice.primalmagick.common.crafting.RecipeTypesPM;
import com.verdantartifice.primalmagick.common.crafting.WandInventory;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagick.common.research.ResearchDisciplines;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.stats.Stat;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.wands.IWand;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;

/**
 * Custom GUI slot for arcane crafting results.
 * 
 * @author Daedalus4096
 */
public class ArcaneCraftingResultSlot extends Slot {
    protected final CraftingContainer craftingInventory;
    protected final WandInventory wandInventory;
    protected final Player player;
    protected int amountCrafted;

    public ArcaneCraftingResultSlot(Player player, CraftingContainer craftingInventory, WandInventory wandInventory, Container inventoryIn, int slotIndex, int xPosition, int yPosition) {
        super(inventoryIn, slotIndex, xPosition, yPosition);
        this.craftingInventory = craftingInventory;
        this.wandInventory = wandInventory;
        this.player = player;
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
        this.checkTakeAchievements(stack);
    }

    @Override
    protected void onSwapCraft(int amount) {
        this.amountCrafted += amount;
    }
    
    @Override
    protected void checkTakeAchievements(ItemStack stack) {
        // Fire crafting handlers
        if (this.amountCrafted > 0) {
            stack.onCraftedBy(this.player.level(), this.player, this.amountCrafted);
            ForgeEventFactory.firePlayerCraftingEvent(this.player, stack, this.craftingInventory);
            
            // Increment the craft counter for the recipe's discipline
            if (this.container instanceof RecipeCraftingHolder recipeHolder && recipeHolder.getRecipeUsed() != null && recipeHolder.getRecipeUsed().value() instanceof IArcaneRecipe arcaneRecipe) {
                CompoundResearchKey key = arcaneRecipe.getRequiredResearch();
                List<ResearchEntry> entryList = ResearchEntries.getEntries(key);
                Set<String> recordedDisciplines = new HashSet<>();
                for (ResearchEntry entry : entryList) {
                    if (entry != null) {
                        ResearchDiscipline disc = ResearchDisciplines.getDiscipline(entry.getDisciplineKey());
                        if (disc != null) {
                            String discKey = disc.getKey();
                            if (!recordedDisciplines.contains(discKey)) {   // Only increment the stat for each discipline once
                                recordedDisciplines.add(discKey);
                                Stat craftingStat = disc.getCraftingStat();
                                if (craftingStat != null) {
                                    StatsManager.incrementValue(this.player, craftingStat, this.amountCrafted);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (this.container instanceof RecipeCraftingHolder recipeHolder) {
            recipeHolder.awardUsedRecipes(this.player, this.craftingInventory.getItems());
        }
        
        // Reset crafted amount
        this.amountCrafted = 0;
    }
    
    @Override
    public void onTake(Player thePlayer, ItemStack stack) {
        this.checkTakeAchievements(stack);
        
        // Do additional processing if the crafted recipe was arcane
        if (this.container instanceof RecipeCraftingHolder holder) {
            if (holder.getRecipeUsed() != null && holder.getRecipeUsed().value() instanceof IArcaneRecipe arcaneRecipe) {
                // Consume the recipe's mana cost from the wand
                SourceList manaCosts = arcaneRecipe.getManaCosts();
                if (!manaCosts.isEmpty()) {
                    ItemStack wandStack = this.wandInventory.getItem(0);
                    if (wandStack != null && !wandStack.isEmpty() && wandStack.getItem() instanceof IWand wand) {
                        wand.consumeRealMana(wandStack, this.player, manaCosts);
                    }
                }
            }
        }
        
        ForgeHooks.setCraftingPlayer(thePlayer);
        
        // Get the remaining items from the recipe, checking arcane recipes first, then vanilla recipes
        Level level = thePlayer.level();
        NonNullList<ItemStack> remainingList;
        Optional<RecipeHolder<IArcaneRecipe>> arcaneOptional = level.getRecipeManager().getRecipeFor(RecipeTypesPM.ARCANE_CRAFTING.get(), this.craftingInventory, level);
        if (arcaneOptional.isPresent()) {
            remainingList = arcaneOptional.get().value().getRemainingItems(this.craftingInventory);
        } else {
            Optional<RecipeHolder<CraftingRecipe>> vanillaOptional = level.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, this.craftingInventory, level);
            if (vanillaOptional.isPresent()) {
                remainingList = vanillaOptional.get().value().getRemainingItems(this.craftingInventory);
            } else {
                remainingList = NonNullList.withSize(this.craftingInventory.getContainerSize(), ItemStack.EMPTY);
                for (int index = 0; index < remainingList.size(); index++) {
                    remainingList.set(index, this.craftingInventory.getItem(index));
                }
            }
        }
        
        ForgeHooks.setCraftingPlayer(null);
        
        for (int index = 0; index < remainingList.size(); index++) {
            ItemStack materialStack = this.craftingInventory.getItem(index);
            ItemStack remainingStack = remainingList.get(index);
            if (!materialStack.isEmpty()) {
                this.craftingInventory.removeItem(index, 1);
                materialStack = this.craftingInventory.getItem(index);
            }
            
            if (!remainingStack.isEmpty()) {
                if (materialStack.isEmpty()) {
                    this.craftingInventory.setItem(index, remainingStack);
                } else if (ItemStack.isSameItemSameTags(materialStack, remainingStack)) {
                    remainingStack.grow(materialStack.getCount());
                    this.craftingInventory.setItem(index, remainingStack);
                } else if (!this.player.getInventory().add(remainingStack)) {
                    this.player.drop(remainingStack, false);
                }
            }
        }
    }
}
