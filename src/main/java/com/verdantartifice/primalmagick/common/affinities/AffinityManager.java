package com.verdantartifice.primalmagick.common.affinities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Functions;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.crafting.IHasManaCost;
import com.verdantartifice.primalmagick.common.menus.FakeMenu;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid=PrimalMagick.MODID)
public class AffinityManager extends SimpleJsonResourceReloadListener {
    protected static final int MAX_AFFINITY = 100;
    protected static final int HISTORY_LIMIT = 100;
    protected static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    protected static final Map<AffinityType, IAffinitySerializer<?>> SERIALIZERS = new ImmutableMap.Builder<AffinityType, IAffinitySerializer<?>>()
            .put(AffinityType.ITEM, ItemAffinity.SERIALIZER)
            .put(AffinityType.POTION_BONUS, PotionBonusAffinity.SERIALIZER)
            .put(AffinityType.ENCHANTMENT_BONUS, EnchantmentBonusAffinity.SERIALIZER)
            .put(AffinityType.ENTITY_TYPE, EntityTypeAffinity.SERIALIZER)
            .build();
    private static final Logger LOGGER = LogManager.getLogger();

    private static AffinityManager INSTANCE;
    
    public static final int MAX_SCAN_COUNT = 108;   // Enough to scan a 9x12 inventory
    
    private Map<AffinityType, Map<ResourceLocation, IAffinity>> affinities = new HashMap<>();
    
    private final Map<AffinityType, Map<ResourceLocation, CompletableFuture<SourceList>>> resultCache = new ConcurrentHashMap<>();
    private final Object resultCacheLock = new Object();

    protected AffinityManager() {
        super(GSON, "affinities");
    }

    @SubscribeEvent
    public static void onResourceReload(AddReloadListenerEvent event) {
        event.addListener(getOrCreateInstance());
    }
    
    public static AffinityManager getOrCreateInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AffinityManager();
        }
        return INSTANCE;
    }
    
    public static AffinityManager getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Cannot retrieve AffinityController until resources are loaded at least once");
        } else {
            return INSTANCE;
        }
    }
    
    public static IAffinitySerializer<?> getSerializer(AffinityType type) {
        return SERIALIZERS.get(type);
    }
    
    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
        this.affinities.clear();
        this.resultCache.clear();
        for (Map.Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
            ResourceLocation location = entry.getKey();
            if (location.getPath().startsWith("_")) {
                // Filter anything beginning with "_" as it's used for metadata.
                continue;
            }

            try {
                IAffinity aff = this.deserializeAffinity(location, GsonHelper.convertToJsonObject(entry.getValue(), "top member"));
                if (aff == null) {
                    LOGGER.info("Skipping loading affinity {} as its serializer returned null", location);
                    continue;
                }
                this.registerAffinity(aff);
            } catch (IllegalArgumentException | JsonParseException e) {
                LOGGER.error("Parsing error loading affinity {}", location, e);
            }
        }
        for (Map.Entry<AffinityType, Map<ResourceLocation, IAffinity>> entry : this.affinities.entrySet()) {
            LOGGER.info("Loaded {} {} affinity definitions", entry.getValue().size(), entry.getKey().getSerializedName());
        }
    }
    
    public void replaceAffinities(List<IAffinity> affinities) {
        this.affinities.clear();
        this.resultCache.clear();
        for (IAffinity affinity : affinities) {
            this.registerAffinity(affinity);
        }
        for (Map.Entry<AffinityType, Map<ResourceLocation, IAffinity>> entry : this.affinities.entrySet()) {
            LOGGER.info("Updated {} {} affinity definitions", entry.getValue().size(), entry.getKey().getSerializedName());
        }
        // Now that we've accepted the upstream set of static affinities, populate the derived affinities.
        // This is here to handle modpack QoL, where the expense of individual recipe derivation becomes noticeable
        // client lag, particularly visible in JEI.
        this.deriveAffinities();
    }

    // Derives all current affinities proactively based on current AffinityManager state.
    // In degenerate cases (400 mods! 30000 items!) takes 600ms or more to complete.
    private void deriveAffinities() {
        Level world = (FMLEnvironment.dist == Dist.CLIENT) ? ClientUtils.getCurrentLevel() : null;
        if (world == null) {
            LOGGER.atWarn().log("Unable to derive affinities - no world available");
            return;
        }

        Set<Entry<ResourceKey<Item>, Item>> itemEntries = ForgeRegistries.ITEMS.getEntries();
        if (itemEntries == null ) {
            LOGGER.atWarn().log("Unable to derive affinities - got null ItemRegistry from ForgeRegistries");
            return;
        }
        Iterator<Entry<ResourceKey<Item>, Item>> itr = itemEntries.iterator();

        LOGGER.atInfo().log("Starting affinity derivation for "+itemEntries.size()+" item entries");

        while (itr.hasNext()) {
            Entry<ResourceKey<Item>, Item> entry = itr.next();
            this.getAffinityValues(
              entry.getValue().getDefaultInstance(),
              world);
        }

        LOGGER.atInfo().log("Completed affinity derivation with "+ this.getAllAffinities().size() + " entries");
    }
    
    protected IAffinity deserializeAffinity(ResourceLocation id, JsonObject json) {
        String s = GsonHelper.getAsString(json, "type");
        AffinityType type = AffinityType.parse(s);
        IAffinitySerializer<?> serializer = SERIALIZERS.get(type);
        if (serializer == null) {
            throw new JsonSyntaxException("Invalid or unsupported affinity type '" + s + "'");
        } else {
            return serializer.read(id, json);
        }
    }
    
    protected IAffinity getAffinity(AffinityType type, ResourceLocation id) {
        return this.affinities.getOrDefault(type, Collections.emptyMap()).get(id);
    }
    
    @Nullable
    CompletableFuture<IAffinity> getOrGenerateItemAffinityAsync(@Nonnull ResourceLocation id, @Nonnull RecipeManager recipeManager, @Nonnull RegistryAccess registryAccess, @Nonnull List<ResourceLocation> history) {
        Map<ResourceLocation, IAffinity> map = this.affinities.computeIfAbsent(AffinityType.ITEM, (affinityType) -> {
            return new HashMap<>();
        });
        if (map.containsKey(id)) {
            return CompletableFuture.completedFuture(map.get(id));
        } else {
            return this.generateItemAffinityAsync(id, recipeManager, registryAccess, history);
        }
    }
    
    @Nonnull
    public Collection<IAffinity> getAllAffinities() {
        return this.affinities.values().stream().flatMap((typeMap) -> {
            return typeMap.values().stream();
        }).collect(Collectors.toSet());
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
    public SourceList getAffinityValues(@Nullable EntityType<?> type, @Nonnull RegistryAccess registryAccess) {
        IAffinity entityAffinity = this.getAffinity(AffinityType.ENTITY_TYPE, ForgeRegistries.ENTITY_TYPES.getKey(type));
        if (entityAffinity == null) {
            return null;
        } else {
            return this.capAffinities(entityAffinity.getTotal(null, registryAccess, new ArrayList<>()), MAX_AFFINITY);
        }
    }
    
    private boolean isResultCached(ItemStack stack) {
        synchronized (this.resultCacheLock) {
            return this.resultCache.getOrDefault(AffinityType.ITEM, Collections.emptyMap()).containsKey(ForgeRegistries.ITEMS.getKey(stack.getItem()));
        }
    }
    
    @Nullable
    private CompletableFuture<SourceList> getCachedResult(ItemStack stack) {
        synchronized (this.resultCacheLock) {
            return this.resultCache.getOrDefault(AffinityType.ITEM, Collections.emptyMap()).get(ForgeRegistries.ITEMS.getKey(stack.getItem()));
        }
    }
    
    private void setCachedResult(ItemStack stack, @Nullable CompletableFuture<SourceList> result) {
        if (result != null) {
            synchronized (this.resultCacheLock) {
                this.resultCache.computeIfAbsent(AffinityType.ITEM, $ -> new ConcurrentHashMap<>()).put(ForgeRegistries.ITEMS.getKey(stack.getItem()), result);
            }
        }
    }
    
    /**
     * Retrieves the affinity values for the given item stack, if they have already been calculated.  Triggers
     * calculation if they have not.
     * 
     * @param stack the stack to be queried
     * @param level the level in which to perform the query
     * @return an optional containing the affinity values if they have been calculated already, or an empty
     * optional if not
     */
    public Optional<SourceList> getAffinityValues(@Nullable ItemStack stack, @Nonnull Level level) {
        Optional<SourceList> retVal;
        try {
            CompletableFuture<SourceList> cachedResult = this.getCachedResult(stack);
            if (cachedResult == null) {
                // If no result is cached for the stack, get one asynchronously and save it for later
                this.setCachedResult(stack, this.getAffinityValuesAsync(stack, level));
                retVal = Optional.empty();
            } else if (cachedResult.isDone()) {
                // If there is a cached result and it's done, fetch and return its value
                retVal = Optional.ofNullable(cachedResult.get());
            } else {
                // If there is a cached result and it's not done (i.e. still working), return an empty optional
                retVal = Optional.empty();
            }
        } catch (CancellationException e) {
            LOGGER.warn("Affinity calculation for stack of item {} was cancelled before completion", ForgeRegistries.ITEMS.getKey(stack.getItem()));
            retVal = Optional.empty();
        } catch (InterruptedException e) {
            LOGGER.warn("Affinity calculation for stack of item {} was interrupted before completion", ForgeRegistries.ITEMS.getKey(stack.getItem()));
            retVal = Optional.empty();
        } catch (ExecutionException e) {
            LOGGER.error("Failed to calculate item affinities", e);
            retVal = Optional.empty();
        }
        return retVal;
    }
    
    public CompletableFuture<SourceList> getAffinityValuesAsync(@Nonnull ItemStack stack, @Nonnull Level level) {
        return this.getAffinityValuesAsync(stack, level.getRecipeManager(), level.registryAccess(), new ArrayList<>());
    }
    
    protected CompletableFuture<SourceList> getAffinityValuesAsync(@Nonnull ItemStack stack, @Nonnull RecipeManager recipeManager, @Nonnull RegistryAccess registryAccess, @Nonnull List<ResourceLocation> history) {
        if (stack.isEmpty()) {
            return CompletableFuture.completedFuture(new SourceList());
        }

        // First try looking up the affinity data from the registry
        IAffinity itemAffinity = this.getAffinity(AffinityType.ITEM, ForgeRegistries.ITEMS.getKey(stack.getItem()));
        if (itemAffinity == null) {
            // If that doesn't work, generate affinities for the item and use those
            itemAffinity = this.generateItemAffinity(ForgeRegistries.ITEMS.getKey(stack.getItem()), recipeManager, registryAccess, history);
            if (itemAffinity == null) {
                // If that doesn't work either, return empty data
                return null;
            }
        }
        
        // Extract source values from the affinity data
        SourceList retVal = itemAffinity.getTotal(recipeManager, registryAccess, history);
        
        // Append any needed bonus affinities for NBT data, then cap the result to a reasonable value
        return this.capAffinities(this.addBonusAffinities(stack, retVal, recipeManager, registryAccess), MAX_AFFINITY);
    }
    
    protected CompletableFuture<IAffinity> generateItemAffinityAsync(@Nonnull ResourceLocation id, @Nonnull RecipeManager recipeManager, @Nonnull RegistryAccess registryAccess, @Nonnull List<ResourceLocation> history) {
        // If the affinity is already registered, just return that
        if (this.isRegistered(AffinityType.ITEM, id)) {
            return CompletableFuture.completedFuture(this.getAffinity(AffinityType.ITEM, id));
        }
        
        // Prevent cycles in affinity generation
        if (history.contains(id)) {
            return CompletableFuture.completedFuture(null);
        }
        history.add(id);

        // If we haven't hit a complexity limit, scan recipes to compute affinities
        if (history.size() < HISTORY_LIMIT) {
            CompletableFuture<SourceList> valuesFuture = this.generateItemAffinityValuesFromRecipesAsync(id, recipeManager, registryAccess, history);
            return valuesFuture.thenApply(values -> {
                IAffinity retVal = new ItemAffinity(id, values);
                this.registerAffinity(retVal);
                return retVal;
            });
        } else {
            return CompletableFuture.completedFuture(null);
        }
    }
    
    @Nullable
    protected CompletableFuture<SourceList> generateItemAffinityValuesFromRecipesAsync(@Nonnull ResourceLocation id, @Nonnull RecipeManager recipeManager, @Nonnull RegistryAccess registryAccess, @Nonnull List<ResourceLocation> history) {
        // Look up all recipes with the given item as an output
        List<CompletableFuture<SourceList>> recipeSourceFutures = recipeManager.getRecipes().stream()
                .filter(r -> r.getResultItem(registryAccess) != null && ForgeRegistries.ITEMS.getKey(r.getResultItem(registryAccess).getItem()).equals(id))
                .map(recipe -> {
                    // Compute the affinities from the recipe's ingredients
                    return this.generateItemAffinityValuesFromIngredientsAsync(recipe, recipeManager, registryAccess, history).thenApply(ingSources -> {
                        // Add affinities from mana costs, if any
                        if (recipe instanceof IHasManaCost manaRecipe) {
                            SourceList manaCosts = manaRecipe.getManaCosts();
                            for (Source source : manaCosts.getSources()) {
                                if (manaCosts.getAmount(source) > 0) {
                                    int manaAmount = (int)(Math.sqrt(1 + manaCosts.getAmount(source) / 2) / recipe.getResultItem(registryAccess).getCount());
                                    if (manaAmount > 0) {
                                        ingSources.add(source, manaAmount);
                                    }
                                }
                            }
                        }
                        return ingSources;
                    });
                }).toList();
        return CompletableFuture.allOf(recipeSourceFutures.toArray(CompletableFuture[]::new)).thenApply($ -> {
            MutableObject<SourceList> retVal = new MutableObject<>(new SourceList());
            MutableInt maxValue = new MutableInt(Integer.MAX_VALUE);
            recipeSourceFutures.forEach(recipeSourceFuture -> {
                recipeSourceFuture.thenAccept(recipeSources -> {
                    int manaSize = recipeSources.getManaSize();
                    if (manaSize > 0 && manaSize < maxValue.intValue()) {
                        // Keep the source list with the smallest non-zero mana footprint
                        retVal.setValue(recipeSources);
                        maxValue.setValue(manaSize);
                    }
                });
            });
            return retVal.getValue();
        });
    }
    
    @Nonnull
    protected CompletableFuture<SourceList> generateItemAffinityValuesFromIngredientsAsync(@Nonnull Recipe<?> recipe, @Nonnull RecipeManager recipeManager, @Nonnull RegistryAccess registryAccess, @Nonnull List<ResourceLocation> history) {
        NonNullList<Ingredient> ingredients = recipe.getIngredients();
        ItemStack output = recipe.getResultItem(registryAccess);
        
        // Populate a fake crafting inventory with ingredients to see what container items would be left behind
        CompletableFuture<NonNullList<ItemStack>> containerFuture;
        if (recipe instanceof CraftingRecipe craftingRecipe) {
            CraftingContainer inv = new TransientCraftingContainer(new FakeMenu(), ingredients.size(), 1);
            List<CompletableFuture<ItemStack>> ingFutures = new ArrayList<>();
            for (Ingredient ingredient : ingredients) {
                ingFutures.add(this.getMatchingItemStackAsync(ingredient, recipeManager, registryAccess, history));
            }
            containerFuture = CompletableFuture.allOf(ingFutures.toArray(CompletableFuture[]::new)).thenApply($ -> {
                MutableInt index = new MutableInt(0);
                for (CompletableFuture<ItemStack> ingFuture : ingFutures) {
                    ingFuture.thenAccept(ingStack -> {
                        if (!ingStack.isEmpty()) {
                            inv.setItem(index.intValue(), ingStack);
                        }
                    });
                    index.increment();
                }
                return craftingRecipe.getRemainingItems(inv);
            });
        } else {
            containerFuture = CompletableFuture.completedFuture(NonNullList.create());
        }

        // Compute total affinities for each ingredient
        SourceList intermediate = new SourceList();
        List<CompletableFuture<SourceList>> ingFutures = ingredients.stream().map(ingredient -> this.getMatchingItemStackAsync(ingredient, recipeManager, registryAccess, history)
                .thenCompose(ingStack -> this.getAffinityValuesAsync(ingStack, recipeManager, registryAccess, history))).toList();
        CompletableFuture<SourceList> intermediateFuture = CompletableFuture.allOf(ingFutures.toArray(CompletableFuture[]::new)).thenApply($ -> {
            ingFutures.forEach(ingFuture -> ingFuture.thenAccept(intermediate::add));
            return intermediate;
        });
        
        // Subtract affinities for remaining containers
        CompletableFuture<SourceList> reducedFuture = containerFuture.thenCombine(intermediateFuture, (containerList, intermediateSources) -> {
            List<CompletableFuture<SourceList>> reductionFutures = containerList.stream().filter(Predicate.not(ItemStack::isEmpty))
                    .map(containerStack -> this.getAffinityValuesAsync(containerStack, recipeManager, registryAccess, history)).toList();
            CompletableFuture.allOf(reductionFutures.toArray(CompletableFuture[]::new)).thenAccept($ -> {
                reductionFutures.forEach(reductionFuture -> reductionFuture.thenAccept(intermediateSources::remove));
            });
            return intermediateSources;
        });
        
        // Scale down remaining affinities
        return reducedFuture.thenApply(intermediateSources -> {
            SourceList retVal = new SourceList();
            intermediateSources.getSources().forEach(source -> {
                double amount = intermediateSources.getAmount(source) * 0.75D / output.getCount();
                if (amount < 1.0D && amount > 0.75D) {
                    amount = 1.0D;
                }
                if ((int)amount > 0) {
                    retVal.add(source, (int)amount);
                }
            });
            return retVal;
        });
    }
    
    @Nonnull
    protected CompletableFuture<ItemStack> getMatchingItemStackAsync(@Nullable Ingredient ingredient, @Nonnull RecipeManager recipeManager, @Nonnull RegistryAccess registryAccess, @Nonnull List<ResourceLocation> history) {
        if (ingredient == null || ingredient.getItems() == null || ingredient.getItems().length <= 0) {
            return CompletableFuture.completedFuture(ItemStack.EMPTY);
        }
        
        // Scan through all of the ingredient's possible matches to determine which one to use for affinity computation
        var futuresMap = Stream.of(ingredient.getItems()).collect(Collectors.toMap(Functions.identity(), stack -> this.getAffinityValuesAsync(stack, recipeManager, registryAccess, history)));
        return CompletableFuture.allOf(futuresMap.values().toArray(CompletableFuture[]::new)).thenApply($ -> {
            MutableInt maxValue = new MutableInt(Integer.MAX_VALUE);
            MutableObject<ItemStack> retVal = new MutableObject<>(ItemStack.EMPTY);
            futuresMap.entrySet().forEach(entry -> {
                entry.getValue().thenAccept(stackSources -> {
                    int manaSize = stackSources.getManaSize();
                    if (manaSize > 0 && manaSize < maxValue.intValue()) {
                        // Keep the ingredient match-stack with the smallest non-zero mana footprint
                        retVal.setValue(entry.getKey());
                        maxValue.setValue(manaSize);
                    }
                });
            });
            return retVal.getValue();
        });
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
    protected SourceList addBonusAffinities(@Nonnull ItemStack stack, @Nullable SourceList inputSources, @Nonnull RecipeManager recipeManager, @Nonnull RegistryAccess registryAccess) {
        if (inputSources == null) {
            return null;
        }
        
        SourceList retVal = inputSources.copy();
        
        // Determine bonus affinities from NBT-attached potion data
        Potion potion = PotionUtils.getPotion(stack);
        if (potion != null && potion != Potions.EMPTY) {
            IAffinity bonus = this.getAffinity(AffinityType.POTION_BONUS, ForgeRegistries.POTIONS.getKey(potion));
            if (bonus != null) {
                retVal.add(bonus.getTotal(recipeManager, registryAccess, new ArrayList<>()));
            }
        }
        
        // Determine bonus affinities from NBT-attached enchantment data
        Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(stack);
        if (enchants != null && !enchants.isEmpty()) {
            for (Enchantment enchant : enchants.keySet()) {
                IAffinity bonus = this.getAffinity(AffinityType.ENCHANTMENT_BONUS, ForgeRegistries.ENCHANTMENTS.getKey(enchant));
                if (bonus != null) {
                    retVal.add(bonus.getTotal(recipeManager, registryAccess, new ArrayList<>()).multiply(enchants.get(enchant)));
                }
            }
        }
        
        return retVal;
    }
}
