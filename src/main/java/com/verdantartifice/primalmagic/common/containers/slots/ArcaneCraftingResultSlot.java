package com.verdantartifice.primalmagic.common.containers.slots;

import java.util.Optional;

import com.verdantartifice.primalmagic.common.crafting.IArcaneRecipe;
import com.verdantartifice.primalmagic.common.crafting.RecipeTypesPM;
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
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.hooks.BasicEventHooks;

/**
 * Custom GUI slot for arcane crafting results.
 * 
 * @author Daedalus4096
 */
public class ArcaneCraftingResultSlot extends Slot {
    protected final CraftingInventory craftingInventory;
    protected final WandInventory wandInventory;
    protected final PlayerEntity player;
    protected int amountCrafted;

    public ArcaneCraftingResultSlot(PlayerEntity player, CraftingInventory craftingInventory, WandInventory wandInventory, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
        super(inventoryIn, slotIndex, xPosition, yPosition);
        this.craftingInventory = craftingInventory;
        this.wandInventory = wandInventory;
        this.player = player;
    }
    
    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }
    
    @Override
    public ItemStack decrStackSize(int amount) {
        if (this.getHasStack()) {
            this.amountCrafted += Math.min(amount, this.getStack().getCount());
        }
        return super.decrStackSize(amount);
    }
    
    @Override
    protected void onCrafting(ItemStack stack, int amount) {
        this.amountCrafted += amount;
        this.onCrafting(stack);
    }

    @Override
    protected void onSwapCraft(int amount) {
        this.amountCrafted += amount;
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
                        ((IWand)wandStack.getItem()).consumeRealMana(wandStack, this.player, manaCosts);
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
        
        // Fire crafting handlers
        if (this.amountCrafted > 0) {
            stack.onCrafting(this.player.world, this.player, this.amountCrafted);
            BasicEventHooks.firePlayerCraftingEvent(this.player, stack, this.craftingInventory);
        }
        if (this.inventory instanceof IRecipeHolder) {
            ((IRecipeHolder)this.inventory).onCrafting(this.player);
        }
        
        // Reset crafted amount
        this.amountCrafted = 0;
    }
    
    @Override
    public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
        this.onCrafting(stack);
        ForgeHooks.setCraftingPlayer(thePlayer);
        
        // Get the remaining items from the recipe, checking arcane recipes first, then vanilla recipes
        NonNullList<ItemStack> remainingList;
        Optional<IArcaneRecipe> arcaneOptional = thePlayer.world.getRecipeManager().getRecipe(RecipeTypesPM.ARCANE_CRAFTING, this.craftingInventory, thePlayer.world);
        if (arcaneOptional.isPresent()) {
            remainingList = arcaneOptional.get().getRemainingItems(this.craftingInventory);
        } else {
            Optional<ICraftingRecipe> vanillaOptional = thePlayer.world.getRecipeManager().getRecipe(IRecipeType.CRAFTING, this.craftingInventory, thePlayer.world);
            if (vanillaOptional.isPresent()) {
                remainingList = vanillaOptional.get().getRemainingItems(this.craftingInventory);
            } else {
                remainingList = NonNullList.withSize(this.craftingInventory.getSizeInventory(), ItemStack.EMPTY);
                for (int index = 0; index < remainingList.size(); index++) {
                    remainingList.set(index, this.craftingInventory.getStackInSlot(index));
                }
            }
        }
        
        ForgeHooks.setCraftingPlayer(null);
        
        for (int index = 0; index < remainingList.size(); index++) {
            ItemStack materialStack = this.craftingInventory.getStackInSlot(index);
            ItemStack remainingStack = remainingList.get(index);
            if (!materialStack.isEmpty()) {
                this.craftingInventory.decrStackSize(index, 1);
                materialStack = this.craftingInventory.getStackInSlot(index);
            }
            
            if (!remainingStack.isEmpty()) {
                if (materialStack.isEmpty()) {
                    this.craftingInventory.setInventorySlotContents(index, remainingStack);
                } else if (ItemStack.areItemsEqual(materialStack, remainingStack) && ItemStack.areItemStackTagsEqual(materialStack, remainingStack)) {
                    remainingStack.grow(materialStack.getCount());
                    this.craftingInventory.setInventorySlotContents(index, remainingStack);
                } else if (!this.player.inventory.addItemStackToInventory(remainingStack)) {
                    this.player.dropItem(remainingStack, false);
                }
            }
        }
        
        return stack;
    }
}
