package com.verdantartifice.primalmagic.common.sources;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.containers.FakeContainer;
import com.verdantartifice.primalmagic.common.crafting.IHasManaCost;
import com.verdantartifice.primalmagic.common.util.ItemUtils;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Primary access point for affinity-related methods.  Also stores block/item affinity data.
 * 
 * @author Daedalus4096
 */
public class AffinityManager {
    // Map of block/item hash code to affinity source list
    protected static final Map<Integer, SourceList> REGISTRY = new ConcurrentHashMap<>();
    
    // Map of potion resource location to bonus affinity source list
    protected static final Map<ResourceLocation, SourceList> POTION_BONUS_REGISTRY = new ConcurrentHashMap<>();
    
    // Map of enchantment resource location to bonus affinity source; amount is determined by enchantment level
    protected static final Map<ResourceLocation, Source> ENCHANTMENT_BONUS_REGISTRY = new ConcurrentHashMap<>();
    
    protected static final int MAX_AFFINITY = 100;
    protected static final int HISTORY_LIMIT = 100;
    
    public static final int MAX_SCAN_COUNT = 108;   // Enough to scan a 9x12 inventory
    
    public static void registerAffinities(@Nullable ItemStack stack, @Nullable SourceList sources) {
        if (stack == null || stack.isEmpty()) {
            return;
        }
        if (sources == null) {
            sources = new SourceList();
        }
        
        // Register affinities for this stack's hash code
        REGISTRY.put(Integer.valueOf(ItemUtils.getHashCode(stack)), sources);
    }
    
    public static void registerItemTagAffinities(@Nullable ResourceLocation tag, @Nullable SourceList sources) {
        if (tag == null) {
            return;
        }
        if (sources == null) {
            sources = new SourceList();
        }
        for (Item item : TagCollectionManager.getManager().getItemTags().get(tag).getAllElements()) {
            // Register affinities for each item in the tag
            registerAffinities(new ItemStack(item, 1), sources);
        }
    }
    
    public static void registerBlockTagAffinities(@Nullable ResourceLocation tag, @Nullable SourceList sources) {
        if (tag == null) {
            return;
        }
        if (sources == null) {
            sources = new SourceList();
        }
        for (Block block : TagCollectionManager.getManager().getBlockTags().get(tag).getAllElements()) {
            // Register affinities for each block in the tag
            registerAffinities(new ItemStack(block, 1), sources);
        }
    }
    
    public static void appendAffinities(@Nullable ItemStack stack, @Nullable SourceList sources, @Nonnull MinecraftServer server) {
        // Merge the given affinities to any already registered for the given itemstack
        appendAffinities(stack, sources, server.getRecipeManager(), new ArrayList<>());
    }
    
    protected static void appendAffinities(@Nullable ItemStack stack, @Nullable SourceList sources, @Nonnull RecipeManager recipeManager, @Nonnull List<String> history) {
        // Merge the given affinities to any already registered for the given itemstack
        if (stack == null || stack.isEmpty()) {
            return;
        }
        if (sources == null) {
            sources = new SourceList();
        }
        
        // If the given object has no affinities registered yet, assume an empty list
        SourceList originalAffinities = getAffinities(stack, recipeManager, history);
        if (originalAffinities == null) {
            originalAffinities = new SourceList();
        }
        
        // Merge, don't add, the two affinity lists
        registerAffinities(stack, originalAffinities.merge(sources));
    }
    
    public static void registerPotionBonusAffinity(@Nullable Potion potion, @Nullable SourceList sources) {
        if (potion == null) {
            return;
        }
        if (sources == null) {
            sources = new SourceList();
        }
        POTION_BONUS_REGISTRY.put(potion.getRegistryName(), sources);
    }
    
    public static void registerEnchantmentBonusAffinity(@Nullable Enchantment enchant, @Nullable Source source) {
        if (enchant == null || source == null) {
            return;
        }
        ENCHANTMENT_BONUS_REGISTRY.put(enchant.getRegistryName(), source);
    }
    
    protected static boolean isRegistered(@Nullable ItemStack stack) {
        // See if the given itemstack has been registered, first with its NBT data intact, and if that fails try again without it
        return REGISTRY.containsKey(Integer.valueOf(ItemUtils.getHashCode(stack, false))) ||
               REGISTRY.containsKey(Integer.valueOf(ItemUtils.getHashCode(stack, true)));
    }
    
    @Nullable
    public static SourceList getAffinities(@Nullable ItemStack stack, @Nonnull World world) {
        return getAffinities(stack, world.getRecipeManager(), new ArrayList<>());
    }
    
    public static SourceList getAffinities(@Nullable ItemStack stack, @Nonnull MinecraftServer server) {
        return getAffinities(stack, server.getRecipeManager(), new ArrayList<>());
    }
    
    @Nullable
    protected static SourceList getAffinities(@Nullable ItemStack stack, @Nonnull RecipeManager recipeManager, @Nonnull List<String> history) {
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
                retVal = generateAffinities(stack, recipeManager, history);
            }
        }
        
        // Append any needed bonus affinities for NBT data, then cap the result to a reasonable value
        return capAffinities(addBonusAffinities(stack, retVal), MAX_AFFINITY);
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
    protected static SourceList generateAffinities(@Nonnull ItemStack stack, @Nonnull RecipeManager recipeManager, @Nonnull List<String> history) {
        ItemStack stackCopy = stack.copy();
        stackCopy.setCount(1);
        
        // If the stack is already registered, just return that
        if (isRegistered(stackCopy)) {
            return getAffinities(stackCopy, recipeManager, history);
        }
        
        // Prevent cycles in affinity generation
        String stackStr = stackCopy.write(new CompoundNBT()).toString();
        if (history.contains(stackStr)) {
            return null;
        }
        history.add(stackStr);
        
        // If we haven't hit a complexity limit, scan recipes to compute affinities
        if (history.size() < HISTORY_LIMIT) {
            SourceList retVal = capAffinities(generateAffinitiesFromRecipes(stackCopy, recipeManager, history), MAX_AFFINITY);
            registerAffinities(stack, retVal);
            return retVal;
        } else {
            return null;
        }
    }
    
    @Nullable
    protected static SourceList generateAffinitiesFromRecipes(@Nonnull ItemStack stack, @Nonnull RecipeManager recipeManager, @Nonnull List<String> history) {
        SourceList retVal = null;
        int maxValue = Integer.MAX_VALUE;
        
        // Look up all recipes with the given item as an output
        for (IRecipe<?> recipe : recipeManager.getRecipes().stream().filter(r -> r.getRecipeOutput() != null && r.getRecipeOutput().isItemEqual(stack)).collect(Collectors.toList())) {
            // Compute the affinities from the recipe's ingredients
            SourceList ingSources = generateAffinitiesFromIngredients(recipe, recipeManager, history);
            if (recipe instanceof IHasManaCost) {
                // Add affinities from mana costs
                IHasManaCost manaRecipe = (IHasManaCost)recipe;
                SourceList manaCosts = manaRecipe.getManaCosts();
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
                // Keep the source list with the smallest non-zero mana footprint
                retVal = ingSources;
                maxValue = manaSize;
            }
        }
        return retVal;
    }
    
    @Nonnull
    protected static SourceList generateAffinitiesFromIngredients(@Nonnull IRecipe<?> recipe, @Nonnull RecipeManager recipeManager, @Nonnull List<String> history) {
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
                ItemStack ingStack = getMatchingItemStack(ingredient, recipeManager, history);
                if (!ingStack.isEmpty()) {
                    inv.setInventorySlotContents(index, ingStack);
                }
                index++;
            }
            containerList = craftingRecipe.getRemainingItems(inv);
        }

        // Compute total affinities for each ingredient
        for (Ingredient ingredient : ingredients) {
            ItemStack ingStack = getMatchingItemStack(ingredient, recipeManager, history);
            if (!ingStack.isEmpty()) {
                SourceList ingSources = getAffinities(ingStack, recipeManager, history);
                if (ingSources != null) {
                    intermediate.add(ingSources);
                }
            }
        }
        
        // Subtract affinities for remaining containers
        if (containerList != null) {
            for (ItemStack containerStack : containerList) {
                if (!containerStack.isEmpty()) {
                    SourceList containerSources = getAffinities(containerStack, recipeManager, history);
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
    
    @Nonnull
    protected static ItemStack getMatchingItemStack(@Nullable Ingredient ingredient, @Nonnull RecipeManager recipeManager, @Nonnull List<String> history) {
        if (ingredient == null || ingredient.getMatchingStacks() == null || ingredient.getMatchingStacks().length <= 0) {
            return ItemStack.EMPTY;
        }
        
        int maxValue = Integer.MAX_VALUE;
        ItemStack retVal = ItemStack.EMPTY;
        
        // Scan through all of the ingredient's possible matches to determine which one to use for affinity computation
        for (ItemStack stack : ingredient.getMatchingStacks()) {
            SourceList stackSources = getAffinities(stack, recipeManager, history);
            if (stackSources != null) {
                int manaSize = stackSources.getManaSize();
                if (manaSize > 0 && manaSize < maxValue) {
                    // Keep the ingredient match-stack with the smallest non-zero mana footprint
                    retVal = stack;
                    maxValue = manaSize;
                }
            }
        }
        return retVal;
    }
    
    @Nullable
    protected static SourceList addBonusAffinities(@Nonnull ItemStack stack, @Nullable SourceList inputSources) {
        if (inputSources == null) {
            return null;
        }
        
        SourceList retVal = inputSources.copy();
        
        // Determine bonus affinities from NBT-attached potion data
        Potion potion = PotionUtils.getPotionFromItem(stack);
        if (potion != null && potion != Potions.EMPTY) {
            SourceList bonus = getPotionBonusAffinities(potion);
            if (bonus != null && !bonus.isEmpty()) {
                retVal.add(bonus);
            }
        }
        
        // Determine bonus affinities from NBT-attached enchantment data
        Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(stack);
        if (enchants != null && !enchants.isEmpty()) {
            for (Enchantment enchant : enchants.keySet()) {
                SourceList bonus = getEnchantmentBonusAffinities(enchant, enchants.get(enchant));
                if (bonus != null && !bonus.isEmpty()) {
                    retVal.add(bonus);
                }
            }
        }
        
        return retVal;
    }
    
    @Nullable
    protected static SourceList getPotionBonusAffinities(@Nonnull Potion potion) {
        return POTION_BONUS_REGISTRY.get(potion.getRegistryName());
    }
    
    @Nonnull
    protected static SourceList getEnchantmentBonusAffinities(@Nonnull Enchantment enchant, @Nonnull int level) {
        SourceList retVal = new SourceList();
        Source source = ENCHANTMENT_BONUS_REGISTRY.get(enchant.getRegistryName());
        if (source != null) {
            retVal.add(source, level);
        }
        return retVal;
    }
}
