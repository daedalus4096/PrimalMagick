package com.verdantartifice.primalmagic.common.affinities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.containers.FakeContainer;
import com.verdantartifice.primalmagic.common.crafting.IHasManaCost;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;

import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid=PrimalMagic.MODID)
public class AffinityManager extends JsonReloadListener {
    protected static final int MAX_AFFINITY = 100;
    protected static final int HISTORY_LIMIT = 100;
    protected static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    protected static final Map<AffinityType, IAffinitySerializer<?>> SERIALIZERS = new ImmutableMap.Builder<AffinityType, IAffinitySerializer<?>>()
            .put(AffinityType.ITEM, ItemAffinity.SERIALIZER)
            .put(AffinityType.POTION_BONUS, PotionBonusAffinity.SERIALIZER)
            .put(AffinityType.ENCHANTMENT_BONUS, EnchantmentBonusAffinity.SERIALIZER)
            .build();

    private static AffinityManager INSTANCE;
    
    public static final int MAX_SCAN_COUNT = 108;   // Enough to scan a 9x12 inventory
    
    private Map<AffinityType, Map<ResourceLocation, IAffinity>> affinities = new HashMap<>();

    protected AffinityManager() {
        super(GSON, "affinities");
    }

    @SubscribeEvent
    public static void onResourceReload(AddReloadListenerEvent event) {
        INSTANCE = new AffinityManager();
        event.addListener(INSTANCE);
    }
    
    public static AffinityManager getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Cannot retrieve AffinityController until resources are loaded at least once");
        } else {
            return INSTANCE;
        }
    }
    
    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        this.affinities.clear();
        for (Map.Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
            ResourceLocation location = entry.getKey();
            if (location.getPath().startsWith("_")) {
                // Filter anything beginning with "_" as it's used for metadata.
                continue;
            }

            try {
                IAffinity aff = this.deserializeAffinity(location, JSONUtils.getJsonObject(entry.getValue(), "top member"));
                if (aff == null) {
                    PrimalMagic.LOGGER.info("Skipping loading affinity {} as its serializer returned null", location);
                    continue;
                }
                this.registerAffinity(aff);
            } catch (IllegalArgumentException | JsonParseException e) {
                PrimalMagic.LOGGER.error("Parsing error loading affinity {}", location, e);
            }
        }
        int size = this.affinities.entrySet().stream().mapToInt((m) -> m.getValue().size()).sum();
        PrimalMagic.LOGGER.info("Loaded {} affinity definitions", size);
    }
    
    protected IAffinity deserializeAffinity(ResourceLocation id, JsonObject json) {
        String s = JSONUtils.getString(json, "type");
        AffinityType type = AffinityType.parse(s);
        IAffinitySerializer<?> serializer = SERIALIZERS.get(type);
        if (serializer == null) {
            throw new JsonSyntaxException("Invalid or unsupported affinity type '" + s + "'");
        } else {
            return serializer.read(id, json);
        }
    }
    
    @Nullable
    public IAffinity getAffinity(AffinityType type, ResourceLocation id) {
        return this.affinities.getOrDefault(type, Collections.emptyMap()).get(id);
    }
    
    @Nullable
    public IAffinity getOrGenerateItemAffinity(@Nonnull ResourceLocation id, @Nonnull RecipeManager recipeManager) {
        Map<ResourceLocation, IAffinity> map = this.affinities.computeIfAbsent(AffinityType.ITEM, (affinityType) -> {
            return new HashMap<>();
        });
        if (map.containsKey(id)) {
            return map.get(id);
        } else {
            return this.generateItemAffinity(id, recipeManager, new ArrayList<>());
        }
    }
    
    protected void registerAffinity(@Nullable IAffinity affinity) {
        this.affinities.computeIfAbsent(affinity.getType(), (affinityType) -> {
            return new HashMap<>();
        }).put(affinity.getTarget(), affinity);
    }
    
    protected boolean isRegistered(AffinityType type, ResourceLocation id) {
        if (type == null || id == null) {
            return false;
        } else {
            return this.affinities.getOrDefault(type, Collections.emptyMap()).containsKey(id);
        }
    }
    
    @Nullable
    public SourceList getAffinityValues(@Nullable ItemStack stack, @Nonnull World world) {
        return this.getAffinityValues(stack, world.getRecipeManager(), new ArrayList<>());
    }
    
    @Nullable
    protected SourceList getAffinityValues(@Nullable ItemStack stack, @Nonnull RecipeManager recipeManager, @Nonnull List<ResourceLocation> history) {
        if (stack == null || stack.isEmpty()) {
            return null;
        }

        // First try looking up the affinity data from the registry
        IAffinity itemAffinity = this.getAffinity(AffinityType.ITEM, stack.getItem().getRegistryName());
        if (itemAffinity == null) {
            // If that doesn't work, generate affinities for the item and use those
            itemAffinity = this.generateItemAffinity(stack.getItem().getRegistryName(), recipeManager, history);
            if (itemAffinity == null) {
                // If that doesn't work either, return empty data
                return null;
            }
        }
        
        // Extract source values from the affinity data
        SourceList retVal = itemAffinity.getTotal(recipeManager);
        
        // Append any needed bonus affinities for NBT data, then cap the result to a reasonable value
        return this.capAffinities(this.addBonusAffinities(stack, retVal, recipeManager), MAX_AFFINITY);
    }
    
    @Nullable
    protected IAffinity generateItemAffinity(@Nonnull ResourceLocation id, @Nonnull RecipeManager recipeManager, @Nonnull List<ResourceLocation> history) {
        // If the affinity is already registered, just return that
        if (this.isRegistered(AffinityType.ITEM, id)) {
            return this.getAffinity(AffinityType.ITEM, id);
        }
        
        // Prevent cycles in affinity generation
        if (history.contains(id)) {
            return null;
        }
        history.add(id);

        // If we haven't hit a complexity limit, scan recipes to compute affinities
        if (history.size() < HISTORY_LIMIT) {
            SourceList values = this.generateItemAffinityValuesFromRecipes(id, recipeManager, history);
            if (values == null) {
                return null;
            } else {
                IAffinity retVal = new ItemAffinity(id, values);
                this.registerAffinity(retVal);
                return retVal;
            }
        } else {
            return null;
        }
    }
    
    @Nullable
    protected SourceList generateItemAffinityValuesFromRecipes(@Nonnull ResourceLocation id, @Nonnull RecipeManager recipeManager, @Nonnull List<ResourceLocation> history) {
        SourceList retVal = null;
        int maxValue = Integer.MAX_VALUE;
        
        // Look up all recipes with the given item as an output
        for (IRecipe<?> recipe : recipeManager.getRecipes().stream().filter(r -> r.getRecipeOutput() != null && r.getRecipeOutput().getItem().getRegistryName().equals(id)).collect(Collectors.toList())) {
            // Compute the affinities from the recipe's ingredients
            SourceList ingSources = generateItemAffinityValuesFromIngredients(recipe, recipeManager, history);
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
    protected SourceList generateItemAffinityValuesFromIngredients(@Nonnull IRecipe<?> recipe, @Nonnull RecipeManager recipeManager, @Nonnull List<ResourceLocation> history) {
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
                ItemStack ingStack = this.getMatchingItemStack(ingredient, recipeManager, history);
                if (!ingStack.isEmpty()) {
                    inv.setInventorySlotContents(index, ingStack);
                }
                index++;
            }
            containerList = craftingRecipe.getRemainingItems(inv);
        }

        // Compute total affinities for each ingredient
        for (Ingredient ingredient : ingredients) {
            ItemStack ingStack = this.getMatchingItemStack(ingredient, recipeManager, history);
            if (!ingStack.isEmpty()) {
                SourceList ingSources = this.getAffinityValues(ingStack, recipeManager, history);
                if (ingSources != null) {
                    intermediate.add(ingSources);
                }
            }
        }
        
        // Subtract affinities for remaining containers
        if (containerList != null) {
            for (ItemStack containerStack : containerList) {
                if (!containerStack.isEmpty()) {
                    SourceList containerSources = this.getAffinityValues(containerStack, recipeManager, history);
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
    protected ItemStack getMatchingItemStack(@Nullable Ingredient ingredient, @Nonnull RecipeManager recipeManager, @Nonnull List<ResourceLocation> history) {
        if (ingredient == null || ingredient.getMatchingStacks() == null || ingredient.getMatchingStacks().length <= 0) {
            return ItemStack.EMPTY;
        }
        
        int maxValue = Integer.MAX_VALUE;
        ItemStack retVal = ItemStack.EMPTY;
        
        // Scan through all of the ingredient's possible matches to determine which one to use for affinity computation
        for (ItemStack stack : ingredient.getMatchingStacks()) {
            SourceList stackSources = this.getAffinityValues(stack, recipeManager, history);
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
    protected SourceList capAffinities(@Nullable SourceList sources, int maxAmount) {
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
    protected SourceList addBonusAffinities(@Nonnull ItemStack stack, @Nullable SourceList inputSources, @Nonnull RecipeManager recipeManager) {
        if (inputSources == null) {
            return null;
        }
        
        SourceList retVal = inputSources.copy();
        
        // Determine bonus affinities from NBT-attached potion data
        Potion potion = PotionUtils.getPotionFromItem(stack);
        if (potion != null && potion != Potions.EMPTY) {
            IAffinity bonus = this.getAffinity(AffinityType.POTION_BONUS, potion.getRegistryName());
            if (bonus != null) {
                retVal.add(bonus.getTotal(recipeManager));
            }
        }
        
        // Determine bonus affinities from NBT-attached enchantment data
        Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(stack);
        if (enchants != null && !enchants.isEmpty()) {
            for (Enchantment enchant : enchants.keySet()) {
                IAffinity bonus = this.getAffinity(AffinityType.ENCHANTMENT_BONUS, enchant.getRegistryName());
                if (bonus != null) {
                    retVal.add(bonus.getTotal(recipeManager).multiply(enchants.get(enchant)));
                }
            }
        }
        
        return retVal;
    }
}
