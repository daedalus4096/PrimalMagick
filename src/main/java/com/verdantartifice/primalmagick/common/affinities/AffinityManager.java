package com.verdantartifice.primalmagick.common.affinities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import com.verdantartifice.primalmagick.common.crafting.IHasManaCost;
import com.verdantartifice.primalmagick.common.menus.FakeMenu;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import net.minecraft.Util;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
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
        this.clearCachedResults();
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
        this.clearCachedResults();
        for (IAffinity affinity : affinities) {
            this.registerAffinity(affinity);
        }
        for (Map.Entry<AffinityType, Map<ResourceLocation, IAffinity>> entry : this.affinities.entrySet()) {
            LOGGER.info("Updated {} {} affinity definitions", entry.getValue().size(), entry.getKey().getSerializedName());
        }
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
    
    @Nullable
    protected IAffinity getAffinity(AffinityType type, ResourceLocation id) {
        return this.affinities.getOrDefault(type, Collections.emptyMap()).get(id);
    }
    
    public CompletableFuture<IAffinity> getOrGenerateItemAffinityAsync(@Nonnull ResourceLocation id, @Nonnull RecipeManager recipeManager, @Nonnull RegistryAccess registryAccess, @Nonnull List<ResourceLocation> history) {
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
    
    private void clearCachedResults() {
        synchronized (this.resultCacheLock) {
            this.resultCache.clear();
        }
    }
    
    @Nullable
    private CompletableFuture<SourceList> getCachedItemResult(ItemStack stack) {
        synchronized (this.resultCacheLock) {
            return this.resultCache.getOrDefault(AffinityType.ITEM, Collections.emptyMap()).get(ForgeRegistries.ITEMS.getKey(stack.getItem()));
        }
    }
    
    private void setCachedItemResult(ItemStack stack, @Nullable CompletableFuture<SourceList> result) {
        if (result != null) {
            synchronized (this.resultCacheLock) {
                this.resultCache.computeIfAbsent(AffinityType.ITEM, $ -> new ConcurrentHashMap<>()).put(ForgeRegistries.ITEMS.getKey(stack.getItem()), result);
            }
        }
    }
    
    @Nullable
    private CompletableFuture<SourceList> getCachedEntityResult(EntityType<?> entityType) {
        synchronized (this.resultCacheLock) {
            return this.resultCache.getOrDefault(AffinityType.ENTITY_TYPE, Collections.emptyMap()).get(ForgeRegistries.ENTITY_TYPES.getKey(entityType));
        }
    }
    
    private void setCachedEntityResult(EntityType<?> entityType, @Nullable CompletableFuture<SourceList> result) {
        if (result != null) {
            synchronized (this.resultCacheLock) {
                this.resultCache.computeIfAbsent(AffinityType.ENTITY_TYPE, $ -> new ConcurrentHashMap<>()).put(ForgeRegistries.ENTITY_TYPES.getKey(entityType), result);
            }
        }
    }
    
    public Optional<SourceList> getAffinityValues(@Nullable EntityType<?> type, @Nonnull RegistryAccess registryAccess) {
        Optional<SourceList> retVal;
        try {
            CompletableFuture<SourceList> cachedResult = this.getCachedEntityResult(type);
            if (cachedResult == null) {
                // If no result is cached for the entity type, get one asynchronously and save it for later
                this.setCachedEntityResult(type, this.getAffinityValuesAsync(type, registryAccess));
                retVal = Optional.empty();
            } else if (cachedResult.isDone()) {
                // If there is a cached result and it's done, fetch and return its value
                retVal = Optional.ofNullable(cachedResult.get());
            } else {
                // If there is a cached result and it's not done (i.e. still working), return an empty optional
                retVal = Optional.empty();
            }
        } catch (CancellationException e) {
            LOGGER.warn("Affinity calculation for entity type {} was cancelled before completion", ForgeRegistries.ENTITY_TYPES.getKey(type));
            retVal = Optional.empty();
        } catch (InterruptedException e) {
            LOGGER.warn("Affinity calculation for entity type {} was interrupted before completion", ForgeRegistries.ENTITY_TYPES.getKey(type));
            retVal = Optional.empty();
        } catch (ExecutionException e) {
            LOGGER.error("Failed to calculate entity type affinities", e);
            retVal = Optional.empty();
        }
        return retVal;
    }
    
    public CompletableFuture<SourceList> getAffinityValuesAsync(EntityType<?> type, RegistryAccess registryAccess) {
        return CompletableFuture.supplyAsync(() -> {
            IAffinity entityAffinity = this.getAffinity(AffinityType.ENTITY_TYPE, ForgeRegistries.ENTITY_TYPES.getKey(type));
            if (entityAffinity == null) {
                return SourceList.EMPTY;
            } else {
                return entityAffinity.getTotalAsync(null, registryAccess, new ArrayList<>()).thenApply(sources -> this.capAffinities(sources, MAX_AFFINITY)).join();
            }
        }, Util.backgroundExecutor()).exceptionally(e -> {
            LOGGER.error("Failed to generate affinity values for entity " + type.toString(), e);
            return SourceList.EMPTY;
        });
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
            CompletableFuture<SourceList> cachedResult = this.getCachedItemResult(stack);
            if (cachedResult == null) {
                // If no result is cached for the stack, get one asynchronously and save it for later
                this.setCachedItemResult(stack, this.getAffinityValuesAsync(stack, level));
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
        return CompletableFuture.supplyAsync(() -> {
            return this.getAffinityValuesAsync(stack, level.getRecipeManager(), level.registryAccess(), new ArrayList<>()).join();
        }, Util.backgroundExecutor()).exceptionally(e -> {
            LOGGER.error("Failed to generate affinity values for item stack " + stack.toString(), e);
            return SourceList.EMPTY;
        });
    }
    
    protected CompletableFuture<SourceList> getAffinityValuesAsync(@Nonnull ItemStack stack, @Nonnull RecipeManager recipeManager, @Nonnull RegistryAccess registryAccess, @Nonnull List<ResourceLocation> history) {
        if (stack.isEmpty()) {
            return CompletableFuture.completedFuture(SourceList.EMPTY);
        }
        
        ResourceLocation stackItemLoc = ForgeRegistries.ITEMS.getKey(stack.getItem());

        // First try looking up the affinity data from the registry
        CompletableFuture<IAffinity> itemAffinityFuture;
        if (this.isRegistered(AffinityType.ITEM, stackItemLoc)) {
            itemAffinityFuture = CompletableFuture.completedFuture(this.getAffinity(AffinityType.ITEM, stackItemLoc));
        } else {
            // If that doesn't work, generate affinities for the item and use those
            itemAffinityFuture = this.generateItemAffinityAsync(stackItemLoc, recipeManager, registryAccess, history);
        }
        
        return itemAffinityFuture.thenCompose(itemAffinity -> {
            // Extract source values from the affinity data
            return itemAffinity == null ? 
                    CompletableFuture.completedFuture(SourceList.EMPTY) : 
                    itemAffinity.getTotalAsync(recipeManager, registryAccess, history);
        }).thenCompose(sources -> {
            // Append any needed bonus affinities for NBT data
            return this.addBonusAffinitiesAsync(stack, sources, recipeManager, registryAccess);
        }).thenApply(sources -> {
            // Finally, cap the result to a reasonable value
            return this.capAffinities(sources, MAX_AFFINITY);
        });
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
            CompletableFuture<RecipeValues> valuesFuture = this.generateItemAffinityValuesFromRecipesAsync(id, recipeManager, registryAccess, history);
            return valuesFuture.thenApply(values -> {
                ItemAffinity retVal = new ItemAffinity(id, values.values());
                retVal.setSourceRecipe(values.recipe());
                this.registerAffinity(retVal);
                return retVal;
            });
        } else {
            return CompletableFuture.completedFuture(null);
        }
    }
    
    @Nullable
    protected CompletableFuture<RecipeValues> generateItemAffinityValuesFromRecipesAsync(@Nonnull ResourceLocation id, @Nonnull RecipeManager recipeManager, @Nonnull RegistryAccess registryAccess, @Nonnull List<ResourceLocation> history) {
        // Look up all recipes with the given item as an output
        List<CompletableFuture<RecipeValues>> recipeValueFutures = recipeManager.getRecipes().stream()
                .filter(r -> r.value().getResultItem(registryAccess) != null && ForgeRegistries.ITEMS.getKey(r.value().getResultItem(registryAccess).getItem()).equals(id))
                .map(recipeHolder -> {
                    // Compute the affinities from the recipe's ingredients
                    return this.generateItemAffinityValuesFromIngredientsAsync(recipeHolder, recipeManager, registryAccess, history).thenApply(ingSources -> {
                        // Add affinities from mana costs, if any
                        SourceList retVal = ingSources.copy();
                        if (recipeHolder.value() instanceof IHasManaCost manaRecipe) {
                            SourceList manaCosts = manaRecipe.getManaCosts();
                            for (Source source : manaCosts.getSources()) {
                                if (manaCosts.getAmount(source) > 0) {
                                    int manaAmount = (int)(Math.sqrt(1 + manaCosts.getAmount(source) / 2) / recipeHolder.value().getResultItem(registryAccess).getCount());
                                    if (manaAmount > 0) {
                                        retVal = retVal.add(source, manaAmount);
                                    }
                                }
                            }
                        }
                        return new RecipeValues(Optional.ofNullable(recipeHolder.id()), retVal);
                    });
                }).toList();
        return Util.sequence(recipeValueFutures).thenApply(recipeValuesList -> {
            MutableObject<RecipeValues> retVal = new MutableObject<>(RecipeValues.EMPTY);
            MutableInt maxValue = new MutableInt(Integer.MAX_VALUE);
            recipeValuesList.forEach(recipeValue -> {
                int manaSize = recipeValue.values().getManaSize();
                if (manaSize > 0 && manaSize < maxValue.intValue()) {
                    // Keep the source list with the smallest non-zero mana footprint
                    retVal.setValue(recipeValue);
                    maxValue.setValue(manaSize);
                }
            });
            return retVal.getValue();
        });
    }
    
    @Nonnull
    protected CompletableFuture<SourceList> generateItemAffinityValuesFromIngredientsAsync(@Nonnull RecipeHolder<?> recipeHolder, @Nonnull RecipeManager recipeManager, @Nonnull RegistryAccess registryAccess, @Nonnull List<ResourceLocation> history) {
        NonNullList<Ingredient> ingredients = recipeHolder.value().getIngredients();
        ItemStack output = recipeHolder.value().getResultItem(registryAccess);
        
        // Populate a fake crafting inventory with ingredients to see what container items would be left behind
        CompletableFuture<NonNullList<ItemStack>> containerFuture;
        if (recipeHolder.value() instanceof CraftingRecipe craftingRecipe) {
            CraftingContainer inv = new TransientCraftingContainer(new FakeMenu(), ingredients.size(), 1);
            List<CompletableFuture<ItemStack>> ingFutures = new ArrayList<>();
            for (Ingredient ingredient : ingredients) {
                ingFutures.add(this.getMatchingItemStackAsync(ingredient, recipeManager, registryAccess, history));
            }
            containerFuture = Util.sequence(ingFutures).thenApply(ingStackList -> {
                MutableInt index = new MutableInt(0);
                ingStackList.forEach(ingStack -> {
                    if (!ingStack.isEmpty()) {
                        inv.setItem(index.intValue(), ingStack);
                    }
                });
                return craftingRecipe.getRemainingItems(inv);
            });
        } else {
            containerFuture = CompletableFuture.completedFuture(NonNullList.create());
        }

        // Compute total affinities for each ingredient
        MutableObject<SourceList> intermediate = new MutableObject<>(SourceList.EMPTY);
        List<CompletableFuture<SourceList>> ingFutures = ingredients.stream().map(ingredient -> this.getMatchingItemStackAsync(ingredient, recipeManager, registryAccess, history)
                .thenCompose(ingStack -> this.getAffinityValuesAsync(ingStack, recipeManager, registryAccess, history))).toList();
        CompletableFuture<SourceList> intermediateFuture = Util.sequence(ingFutures).thenApply(valueList -> {
            valueList.forEach(values -> {
                intermediate.setValue(intermediate.getValue().add(values));
            });
            return intermediate.getValue();
        });
        
        // Subtract affinities for remaining containers
        CompletableFuture<SourceList> reducedFuture = containerFuture.thenCombine(intermediateFuture, (containerList, intermediateSources) -> {
            MutableObject<SourceList> toBeReduced = new MutableObject<>(intermediateSources.copy());
            List<CompletableFuture<SourceList>> reductionFutures = containerList.stream().filter(Predicate.not(ItemStack::isEmpty))
                    .map(containerStack -> this.getAffinityValuesAsync(containerStack, recipeManager, registryAccess, history)).toList();
            Util.sequence(reductionFutures).thenAccept(valueList -> {
                valueList.forEach(values -> {
                    toBeReduced.setValue(toBeReduced.getValue().remove(values));
                });
            });
            return toBeReduced.getValue();
        });
        
        // Scale down remaining affinities
        return reducedFuture.thenApply(intermediateSources -> {
            SourceList.Builder retVal = SourceList.builder();
            intermediateSources.getSources().forEach(source -> {
                double amount = intermediateSources.getAmount(source) * 0.75D / output.getCount();
                if (amount < 1.0D && amount > 0.75D) {
                    amount = 1.0D;
                }
                if ((int)amount > 0) {
                    retVal.with(source, (int)amount);
                }
            });
            return retVal.build();
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
        SourceList retVal = SourceList.EMPTY;
        for (Source source : sources.getSources()) {
            retVal = retVal.merge(source, Math.min(maxAmount, sources.getAmount(source)));
        }
        return retVal;
    }
    
    @Nullable
    protected CompletableFuture<SourceList> addBonusAffinitiesAsync(@Nonnull ItemStack stack, @Nonnull SourceList inputSources, @Nonnull RecipeManager recipeManager, @Nonnull RegistryAccess registryAccess) {
        List<CompletableFuture<SourceList>> bonusFutures = new ArrayList<>();
        MutableObject<SourceList> retVal = new MutableObject<>(inputSources.copy());
        
        // Determine bonus affinities from NBT-attached potion data
        Potion potion = PotionUtils.getPotion(stack);
        if (potion != null && potion != Potions.EMPTY) {
            IAffinity bonus = this.getAffinity(AffinityType.POTION_BONUS, ForgeRegistries.POTIONS.getKey(potion));
            if (bonus != null) {
                bonusFutures.add(bonus.getTotalAsync(recipeManager, registryAccess, new ArrayList<>()));
            }
        }
        
        // Determine bonus affinities from NBT-attached enchantment data
        Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(stack);
        if (enchants != null && !enchants.isEmpty()) {
            for (Enchantment enchant : enchants.keySet()) {
                IAffinity bonus = this.getAffinity(AffinityType.ENCHANTMENT_BONUS, ForgeRegistries.ENCHANTMENTS.getKey(enchant));
                if (bonus != null) {
                    bonusFutures.add(bonus.getTotalAsync(recipeManager, registryAccess, new ArrayList<>()).thenApply(enchBonus -> enchBonus.multiply(enchants.get(enchant))));
                }
            }
        }
        
        // Add any detected bonus affinities to the original input
        return Util.sequence(bonusFutures).thenApply(bonusList -> {
            bonusList.forEach(bonus -> {
                retVal.setValue(retVal.getValue().add(bonus));
            });
            return retVal.getValue();
        });
    }
    
    protected static record RecipeValues(Optional<ResourceLocation> recipe, SourceList values) {
        public static final RecipeValues EMPTY = new RecipeValues(Optional.empty(), SourceList.EMPTY);
    }
}
