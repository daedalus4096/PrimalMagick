package com.verdantartifice.primalmagick.common.menus.slots;

import com.verdantartifice.primalmagick.common.crafting.IArcaneRecipe;
import com.verdantartifice.primalmagick.common.crafting.RecipeTypesPM;
import com.verdantartifice.primalmagick.common.crafting.WandInventory;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.stats.ExpertiseManager;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.wands.IWand;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;

import java.util.Optional;

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
            Services.EVENTS.firePlayerCraftingEvent(this.player, stack, this.craftingInventory);
            
            // Increment the expertise and craft counter stats for the recipe's discipline
            if (this.container instanceof RecipeCraftingHolder recipeHolder && recipeHolder.getRecipeUsed() != null) {
                ExpertiseManager.awardExpertise(this.player, recipeHolder.getRecipeUsed());
                StatsManager.incrementCraftCount(this.player, recipeHolder.getRecipeUsed(), this.amountCrafted);
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
                        wand.consumeRealMana(wandStack, this.player, manaCosts, thePlayer.registryAccess());
                    }
                }
            }
        }

        CraftingInput.Positioned positionedCraftingInput = this.craftingInventory.asPositionedCraftInput();
        CraftingInput craftingInput = positionedCraftingInput.input();
        int leftBound = positionedCraftingInput.left();
        int topBound = positionedCraftingInput.top();

        ForgeHooks.setCraftingPlayer(thePlayer);
        
        // Get the remaining items from the recipe, checking arcane recipes first, then vanilla recipes
        Level level = thePlayer.level();
        NonNullList<ItemStack> remainingList;
        Optional<RecipeHolder<IArcaneRecipe>> arcaneOptional = level.getRecipeManager().getRecipeFor(RecipeTypesPM.ARCANE_CRAFTING.get(), this.craftingInventory.asCraftInput(), level);
        if (arcaneOptional.isPresent()) {
            remainingList = arcaneOptional.get().value().getRemainingItems(craftingInput);
        } else {
            Optional<RecipeHolder<CraftingRecipe>> vanillaOptional = level.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, this.craftingInventory.asCraftInput(), level);
            if (vanillaOptional.isPresent()) {
                remainingList = vanillaOptional.get().value().getRemainingItems(craftingInput);
            } else {
                remainingList = NonNullList.withSize(this.craftingInventory.getContainerSize(), ItemStack.EMPTY);
                for (int index = 0; index < remainingList.size(); index++) {
                    remainingList.set(index, this.craftingInventory.getItem(index));
                }
            }
        }
        
        ForgeHooks.setCraftingPlayer(null);
        
        for (int y = 0; y < craftingInput.height(); y++) {
            for (int x = 0; x < craftingInput.width(); x++) {
                int originalIndex = leftBound + x + ((topBound + y) * this.craftingInventory.getWidth());
                int remainderIndex = x + (y * craftingInput.width());
                ItemStack materialStack = this.craftingInventory.getItem(originalIndex);
                ItemStack remainingStack = remainingList.get(remainderIndex);
                if (!materialStack.isEmpty()) {
                    this.craftingInventory.removeItem(originalIndex, 1);
                    materialStack = this.craftingInventory.getItem(originalIndex);
                }
                
                if (!remainingStack.isEmpty()) {
                    if (materialStack.isEmpty()) {
                        this.craftingInventory.setItem(originalIndex, remainingStack);
                    } else if (ItemStack.isSameItemSameComponents(materialStack, remainingStack)) {
                        remainingStack.grow(materialStack.getCount());
                        this.craftingInventory.setItem(originalIndex, remainingStack);
                    } else if (!this.player.getInventory().add(remainingStack)) {
                        this.player.drop(remainingStack, false);
                    }
                }
            }
        }
    }
}
