package com.verdantartifice.primalmagic.common.sources;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.containers.FakeContainer;
import com.verdantartifice.primalmagic.common.crafting.IArcaneRecipe;
import com.verdantartifice.primalmagic.common.util.ItemUtils;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class AffinityManager {
    protected static final Map<Integer, SourceList> REGISTRY = new ConcurrentHashMap<>();
    protected static final int MAX_AFFINITY = 100;
    protected static final int HISTORY_LIMIT = 100;
    
    public static void registerAffinities(@Nullable ItemStack stack, @Nullable SourceList sources) {
        if (stack == null || stack.isEmpty()) {
            return;
        }
        if (sources == null) {
            sources = new SourceList();
        }
        REGISTRY.put(Integer.valueOf(ItemUtils.getHashCode(stack)), sources);
    }
    
    public static void registerAffinities(@Nullable ResourceLocation tag, @Nullable SourceList sources) {
        if (tag == null) {
            return;
        }
        if (sources == null) {
            sources = new SourceList();
        }
        for (Item item : ItemTags.getCollection().getOrCreate(tag).getAllElements()) {
            registerAffinities(new ItemStack(item, 1), sources);
        }
    }
    
    public static boolean isRegistered(@Nullable ItemStack stack) {
        return REGISTRY.containsKey(Integer.valueOf(ItemUtils.getHashCode(stack, false))) ||
               REGISTRY.containsKey(Integer.valueOf(ItemUtils.getHashCode(stack, true)));
    }
    
    @Nullable
    public static SourceList getAffinities(@Nullable ItemStack stack, @Nonnull World world) {
        return getAffinities(stack, world, new ArrayList<>());
    }
    
    @Nullable
    protected static SourceList getAffinities(@Nullable ItemStack stack, @Nonnull World world, @Nonnull List<String> history) {
        if (stack == null || stack.isEmpty()) {
            return null;
        }
        // First try a straight lookup of the item
        SourceList retVal = REGISTRY.get(Integer.valueOf(ItemUtils.getHashCode(stack, false)));
        if (retVal == null) {
            // If that doesn't work, do a lookup of an NBT-stripped copy of the item
            retVal = REGISTRY.get(Integer.valueOf(ItemUtils.getHashCode(stack, true)));
            if (retVal == null) {
                // If that doesn't work either, generate affinities for the item and return those
                retVal = generateAffinities(stack, world, history);
            }
        }
        return capAffinities(retVal, MAX_AFFINITY);
    }

    @Nullable
    protected static SourceList capAffinities(@Nullable SourceList sources, int maxAmount) {
        if (sources == null) {
            return null;
        }
        SourceList retVal = new SourceList();
        for (Source source : sources.getSources()) {
            retVal.merge(source, Math.min(maxAmount, sources.getAmount(source)));
        }
        return retVal;
    }

    @Nullable
    protected static SourceList generateAffinities(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull List<String> history) {
        ItemStack stackCopy = stack.copy();
        stackCopy.setCount(1);
        
        // If the stack is already registered, just return that
        if (AffinityManager.isRegistered(stackCopy)) {
            return getAffinities(stackCopy, world, history);
        }
        
        // Prevent cycles in affinity generation
        String stackStr = stackCopy.write(new CompoundNBT()).toString();
        if (history.contains(stackStr)) {
            return null;
        }
        history.add(stackStr);
        
        // If we haven't hit a complexity limit, scan recipes to compute affinities
        if (history.size() < HISTORY_LIMIT) {
            SourceList retVal = capAffinities(generateAffinitiesFromRecipes(stackCopy, world, history), MAX_AFFINITY);
            registerAffinities(stack, retVal);
            return retVal;
        } else {
            return null;
        }
    }
    
    @Nullable
    protected static SourceList generateAffinitiesFromRecipes(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull List<String> history) {
        SourceList retVal = null;
        int maxValue = Integer.MAX_VALUE;
        for (IRecipe<?> recipe : world.getRecipeManager().getRecipes().stream().filter(r -> r.getRecipeOutput() != null && r.getRecipeOutput().isItemEqual(stack)).collect(Collectors.toList())) {
            SourceList ingSources = generateAffinitiesFromIngredients(recipe, world, history);
            if (recipe instanceof IArcaneRecipe) {
                // Add affinities from mana costs
                IArcaneRecipe arcaneRecipe = (IArcaneRecipe)recipe;
                SourceList manaCosts = arcaneRecipe.getManaCosts();
                for (Source source : manaCosts.getSources()) {
                    if (manaCosts.getAmount(source) > 0) {
                        int manaAmount = (int)(Math.sqrt(1 + manaCosts.getAmount(source) / 2) / recipe.getRecipeOutput().getCount());
                        if (manaAmount > 0) {
                            ingSources.add(source, manaAmount);
                        }
                    }
                }
            }
            int manaSize = ingSources.getManaSize();
            if (manaSize > 0 && manaSize < maxValue) {
                // Keep the source list with the smallest mana footprint
                retVal = ingSources;
                maxValue = manaSize;
            }
        }
        return retVal;
    }
    
    @Nonnull
    protected static SourceList generateAffinitiesFromIngredients(@Nonnull IRecipe<?> recipe, @Nonnull World world, @Nonnull List<String> history) {
        NonNullList<Ingredient> ingredients = recipe.getIngredients();
        ItemStack output = recipe.getRecipeOutput();
        SourceList intermediate = new SourceList();
        
        // Populate a fake crafting inventory with ingredients to see what container items would be left behind
        NonNullList<ItemStack> containerList = NonNullList.create();
        if (recipe instanceof ICraftingRecipe) {
            ICraftingRecipe craftingRecipe = (ICraftingRecipe)recipe;
            CraftingInventory inv = new CraftingInventory(new FakeContainer(), 3, 3);
            int index = 0;
            for (Ingredient ingredient : ingredients) {
                if (ingredient.getMatchingStacks().length > 0) {
                    inv.setInventorySlotContents(index, ingredient.getMatchingStacks()[0]);
                }
                index++;
            }
            containerList = craftingRecipe.getRemainingItems(inv);
        }

        // Compute total affinities for each ingredient
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getMatchingStacks().length > 0) {
                SourceList ingSources = getAffinities(ingredient.getMatchingStacks()[0], world, history);
                if (ingSources != null) {
                    intermediate.add(ingSources);
                }
            }
        }
        
        // Subtract affinities for remaining containers
        if (containerList != null) {
            for (ItemStack containerStack : containerList) {
                if (!containerStack.isEmpty()) {
                    SourceList containerSources = getAffinities(containerStack, world);
                    if (containerSources != null) {
                        for (Source source : containerSources.getSources()) {
                            intermediate.reduce(source, containerSources.getAmount(source));
                        }
                    }
                }
            }
        }
        
        // Scale down remaining affinities
        SourceList retVal = new SourceList();
        for (Source source : intermediate.getSources()) {
            double amount = intermediate.getAmount(source) * 0.75D / output.getCount();
            if (amount < 1.0D && amount > 0.75D) {
                amount = 1.0D;
            }
            if ((int)amount > 0) {
                retVal.add(source, (int)amount);
            }
        }
        
        return retVal;
    }
}
