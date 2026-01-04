package com.verdantartifice.primalmagick.common.affinities;

import com.google.common.base.Functions;
import com.verdantartifice.primalmagick.common.crafting.IHasManaCost;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.util.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;

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

public class AffinityManager extends SimpleJsonResourceReloadListener<AbstractAffinity<?>> {
    protected static final int MAX_AFFINITY = 100;
    protected static final int HISTORY_LIMIT = 100;
    private static final Logger LOGGER = LogManager.getLogger();

    private static AffinityManager INSTANCE;
    
    public static final int MAX_SCAN_COUNT = 108;   // Enough to scan a 9x12 inventory
    
    private final Map<AffinityType<?>, Map<Identifier, IAffinity>> affinities = new HashMap<>();
    
    private final Map<AffinityType<?>, Map<Identifier, CompletableFuture<SourceList>>> resultCache = new ConcurrentHashMap<>();
    private final Object resultCacheLock = new Object();

    protected AffinityManager() {
        super(AbstractAffinity.dispatchCodec(), FileToIdConverter.json("affinities"));
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
    
    @Override
    protected void apply(@NotNull Map<Identifier, AbstractAffinity<?>> objectIn, @NotNull ResourceManager resourceManagerIn, @NotNull ProfilerFiller profilerIn) {
        this.affinities.clear();
        this.clearCachedResults();
        objectIn.forEach((id, affinity) -> {
            if (affinity == null) {
                LOGGER.error("Failed to register affinity definition {}", id);
            } else {
                this.registerAffinity(affinity);
            }
        });
        this.affinities.forEach((affinityType, affinityMap) -> {
            LOGGER.info("Loaded {} {} affinity definitions", affinityMap.size(), affinityType.id());
        });
    }
    
    public void replaceAffinities(List<IAffinity> affinities) {
        this.affinities.clear();
        this.clearCachedResults();
        affinities.forEach(this::registerAffinity);
        this.affinities.forEach((affinityType, affinityMap) -> {
            LOGGER.info("Updated {} {} affinity definitions", affinityMap.size(), affinityType.id());
        });
    }

    @Nullable
    protected IAffinity getAffinity(AffinityType<?> type, Identifier id) {
        return this.affinities.getOrDefault(type, Collections.emptyMap()).get(id);
    }
    
    public CompletableFuture<IAffinity> getOrGenerateItemAffinityAsync(@NotNull Identifier id, @NotNull RecipeManager recipeManager, @NotNull RegistryAccess registryAccess, @NotNull List<Identifier> history) {
        Map<Identifier, IAffinity> map = this.affinities.computeIfAbsent(AffinityTypesPM.ITEM.get(), affinityType -> new HashMap<>());
        if (map.containsKey(id)) {
            return CompletableFuture.completedFuture(map.get(id));
        } else {
            return this.generateItemAffinityAsync(id, recipeManager, registryAccess, history);
        }
    }
    
    @NotNull
    public Collection<IAffinity> getAllAffinities() {
        return this.affinities.values().stream().flatMap(typeMap -> typeMap.values().stream()).collect(Collectors.toSet());
    }
    
    protected void registerAffinity(@NotNull IAffinity affinity) {
        this.affinities.computeIfAbsent(affinity.getType(), affinityType -> new HashMap<>()).put(affinity.getTarget(), affinity);
    }
    
    protected boolean isRegistered(AffinityType<?> type, Identifier id) {
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
            return this.resultCache.getOrDefault(AffinityTypesPM.ITEM.get(), Collections.emptyMap()).get(Services.ITEMS_REGISTRY.getKey(stack.getItem()));
        }
    }

    @VisibleForTesting
    public void setCachedItemResult(ItemStack stack, @Nullable CompletableFuture<SourceList> result) {
        if (result != null) {
            synchronized (this.resultCacheLock) {
                this.resultCache.computeIfAbsent(AffinityTypesPM.ITEM.get(), $ -> new ConcurrentHashMap<>()).put(Services.ITEMS_REGISTRY.getKey(stack.getItem()), result);
            }
        }
    }
    
    @Nullable
    private CompletableFuture<SourceList> getCachedEntityResult(EntityType<?> entityType) {
        synchronized (this.resultCacheLock) {
            return this.resultCache.getOrDefault(AffinityTypesPM.ENTITY_TYPE.get(), Collections.emptyMap()).get(Services.ENTITY_TYPES_REGISTRY.getKey(entityType));
        }
    }
    
    private void setCachedEntityResult(EntityType<?> entityType, @Nullable CompletableFuture<SourceList> result) {
        if (result != null) {
            synchronized (this.resultCacheLock) {
                this.resultCache.computeIfAbsent(AffinityTypesPM.ENTITY_TYPE.get(), $ -> new ConcurrentHashMap<>()).put(Services.ENTITY_TYPES_REGISTRY.getKey(entityType), result);
            }
        }
    }
    
    public Optional<SourceList> getAffinityValues(@Nullable EntityType<?> type, @NotNull RegistryAccess registryAccess) {
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
            LOGGER.warn("Affinity calculation for entity type {} was cancelled before completion", Services.ENTITY_TYPES_REGISTRY.getKey(type));
            retVal = Optional.empty();
        } catch (InterruptedException e) {
            LOGGER.warn("Affinity calculation for entity type {} was interrupted before completion", Services.ENTITY_TYPES_REGISTRY.getKey(type));
            retVal = Optional.empty();
        } catch (ExecutionException e) {
            LOGGER.error("Failed to calculate entity type affinities", e);
            retVal = Optional.empty();
        }
        return retVal;
    }
    
    public CompletableFuture<SourceList> getAffinityValuesAsync(EntityType<?> type, RegistryAccess registryAccess) {
        return CompletableFuture.supplyAsync(() -> {
            IAffinity entityAffinity = this.getAffinity(AffinityTypesPM.ENTITY_TYPE.get(), Services.ENTITY_TYPES_REGISTRY.getKey(type));
            if (entityAffinity == null) {
                return SourceList.EMPTY;
            } else {
                return entityAffinity.getTotalAsync(null, registryAccess, new ArrayList<>()).thenApply(sources -> this.capAffinities(sources, MAX_AFFINITY)).join();
            }
        }, Util.backgroundExecutor()).exceptionally(e -> {
            LOGGER.error("Failed to generate affinity values for entity {}", type.toString(), e);
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
    public Optional<SourceList> getAffinityValues(@NotNull ItemStack stack, @NotNull Level level) {
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
            LOGGER.warn("Affinity calculation for stack of item {} was cancelled before completion", Services.ITEMS_REGISTRY.getKey(stack.getItem()));
            retVal = Optional.empty();
        } catch (InterruptedException e) {
            LOGGER.warn("Affinity calculation for stack of item {} was interrupted before completion", Services.ITEMS_REGISTRY.getKey(stack.getItem()));
            retVal = Optional.empty();
        } catch (ExecutionException e) {
            LOGGER.error("Failed to calculate item affinities", e);
            retVal = Optional.empty();
        }
        return retVal;
    }
    
    public CompletableFuture<SourceList> getAffinityValuesAsync(@NotNull ItemStack stack, @NotNull Level level) {
        return CompletableFuture.supplyAsync(() -> {
            return this.getAffinityValuesAsync(stack, level.getRecipeManager(), level.registryAccess(), new ArrayList<>()).join();
        }, Util.backgroundExecutor()).exceptionally(e -> {
            LOGGER.error("Failed to generate affinity values for item stack {}", stack.toString(), e);
            return SourceList.EMPTY;
        });
    }
    
    protected CompletableFuture<SourceList> getAffinityValuesAsync(@NotNull ItemStack stack, @NotNull RecipeManager recipeManager, @NotNull RegistryAccess registryAccess, @NotNull List<Identifier> history) {
        if (stack.isEmpty()) {
            return CompletableFuture.completedFuture(SourceList.EMPTY);
        }
        
        Identifier stackItemLoc = Services.ITEMS_REGISTRY.getKey(stack.getItem());
        if (stackItemLoc == null) {
            return CompletableFuture.completedFuture(SourceList.EMPTY);
        }

        // First try looking up the affinity data from the registry
        CompletableFuture<IAffinity> itemAffinityFuture;
        if (this.isRegistered(AffinityTypesPM.ITEM.get(), stackItemLoc)) {
            itemAffinityFuture = CompletableFuture.completedFuture(this.getAffinity(AffinityTypesPM.ITEM.get(), stackItemLoc));
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
    
    protected CompletableFuture<IAffinity> generateItemAffinityAsync(@NotNull Identifier id, @NotNull RecipeManager recipeManager, @NotNull RegistryAccess registryAccess, @NotNull List<Identifier> history) {
        // If the affinity is already registered, just return that
        if (this.isRegistered(AffinityTypesPM.ITEM.get(), id)) {
            return CompletableFuture.completedFuture(this.getAffinity(AffinityTypesPM.ITEM.get(), id));
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
                ItemAffinity retVal = ItemAffinity.fixed(id, values.values(), values.recipe());
                this.registerAffinity(retVal);
                return retVal;
            });
        } else {
            return CompletableFuture.completedFuture(null);
        }
    }
    
    @NotNull
    protected CompletableFuture<RecipeValues> generateItemAffinityValuesFromRecipesAsync(@NotNull Identifier id, @NotNull RecipeManager recipeManager, @NotNull RegistryAccess registryAccess, @NotNull List<Identifier> history) {
        // Look up all recipes with the given item as an output
        List<CompletableFuture<RecipeValues>> recipeValueFutures = recipeManager.getRecipes().stream()
                .filter(r -> r.value().getResultItem(registryAccess) != null && id.equals(Services.ITEMS_REGISTRY.getKey(r.value().getResultItem(registryAccess).getItem())))
                .map(recipeHolder -> {
                    // Compute the affinities from the recipe's ingredients
                    return this.generateItemAffinityValuesFromIngredientsAsync(recipeHolder, recipeManager, registryAccess, history).thenApply(ingSources -> {
                        // Add affinities from mana costs, if any
                        SourceList retVal = ingSources.copy();
                        if (recipeHolder.value() instanceof IHasManaCost manaRecipe) {
                            SourceList manaCosts = manaRecipe.getManaCosts();
                            for (Source source : manaCosts.getSources()) {
                                if (manaCosts.getAmount(source) > 0) {
                                    int manaAmount = (int)(Math.sqrt(1 + manaCosts.getAmount(source) / 200D) / recipeHolder.value().getResultItem(registryAccess).getCount());
                                    if (manaAmount > 0) {
                                        retVal = retVal.add(source, manaAmount);
                                    }
                                }
                            }
                        }
                        return new RecipeValues(Optional.of(recipeHolder.id()), retVal);
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
    
    @NotNull
    protected CompletableFuture<SourceList> generateItemAffinityValuesFromIngredientsAsync(@NotNull RecipeHolder<?> recipeHolder, @NotNull RecipeManager recipeManager, @NotNull RegistryAccess registryAccess, @NotNull List<Identifier> history) {
        List<Ingredient> ingredients = recipeHolder.value().placementInfo().ingredients();
        ItemStack output = recipeHolder.value().getResultItem(registryAccess);
        
        // Populate a fake crafting inventory with ingredients to see what container items would be left behind
        CompletableFuture<NonNullList<ItemStack>> containerFuture;
        if (recipeHolder.value() instanceof CraftingRecipe) {
            List<CompletableFuture<ItemStack>> ingFutures = new ArrayList<>();
            for (Ingredient ingredient : ingredients) {
                ingFutures.add(this.getMatchingItemStackAsync(ingredient, recipeManager, registryAccess, history));
            }
            containerFuture = Util.sequence(ingFutures).thenApply(ingStackList -> {
                // Determine remaining container items manually. Don't call Recipe#getRemainingItems because that would
                // require reassembling the recipe structure from incomplete data. Some mods, such as Immersive Engineering,
                // are very sensitive about this and throw errors if you get the crafting input structure wrong.
                // See: https://github.com/daedalus4096/PrimalMagick/issues/246
                return NonNullList.of(ItemStack.EMPTY, ingStackList.stream()
                        .map(ItemStack::getItem)
                        .map(Item::getCraftingRemainder)
                        .filter(Predicate.not(ItemStack::isEmpty))
                        .toArray(ItemStack[]::new));
            });
        } else {
            containerFuture = CompletableFuture.completedFuture(NonNullList.create());
        }

        // Compute total affinities for each ingredient
        MutableObject<SourceList> intermediate = new MutableObject<>(SourceList.EMPTY);
        List<CompletableFuture<SourceList>> ingFutures = ingredients.stream().map(ingredient -> this.getMatchingItemStackAsync(ingredient, recipeManager, registryAccess, history)
                .thenCompose(ingStack -> this.getAffinityValuesAsync(ingStack, recipeManager, registryAccess, history))).toList();
        CompletableFuture<SourceList> intermediateFuture = Util.sequence(ingFutures).thenApply(valueList -> {
            valueList.forEach(values -> intermediate.setValue(intermediate.getValue().add(values)));
            return intermediate.getValue();
        });
        
        // Subtract affinities for remaining containers
        CompletableFuture<SourceList> reducedFuture = containerFuture.thenCombine(intermediateFuture, (containerList, intermediateSources) -> {
            MutableObject<SourceList> toBeReduced = new MutableObject<>(intermediateSources.copy());
            List<CompletableFuture<SourceList>> reductionFutures = containerList.stream().filter(Predicate.not(ItemStack::isEmpty))
                    .map(containerStack -> this.getAffinityValuesAsync(containerStack, recipeManager, registryAccess, history)).toList();
            Util.sequence(reductionFutures).thenAccept(valueList -> valueList.forEach(values -> toBeReduced.setValue(toBeReduced.getValue().remove(values))));
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

    @SuppressWarnings("deprecation")
    @NotNull
    protected CompletableFuture<ItemStack> getMatchingItemStackAsync(@Nullable Ingredient ingredient, @NotNull RecipeManager recipeManager, @NotNull RegistryAccess registryAccess, @NotNull List<Identifier> history) {
        if (ingredient == null || ingredient.isEmpty()) {
            return CompletableFuture.completedFuture(ItemStack.EMPTY);
        }
        
        // Scan through all the ingredient's possible matches to determine which one to use for affinity computation
        var futuresMap = ingredient.items().map(ItemStack::new).collect(Collectors.toMap(Functions.identity(), stack -> this.getAffinityValuesAsync(stack, recipeManager, registryAccess, history)));
        return CompletableFuture.allOf(futuresMap.values().toArray(CompletableFuture[]::new)).thenApply($ -> {
            MutableInt maxValue = new MutableInt(Integer.MAX_VALUE);
            MutableObject<ItemStack> retVal = new MutableObject<>(ItemStack.EMPTY);
            futuresMap.forEach((stack, future) -> future.thenAccept(stackSources -> {
                int manaSize = stackSources.getManaSize();
                if (manaSize > 0 && manaSize < maxValue.intValue()) {
                    // Keep the ingredient match-stack with the smallest non-zero mana footprint
                    retVal.setValue(stack);
                    maxValue.setValue(manaSize);
                }
            }));
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
    protected CompletableFuture<SourceList> addBonusAffinitiesAsync(@NotNull ItemStack stack, @NotNull SourceList inputSources, @NotNull RecipeManager recipeManager, @NotNull RegistryAccess registryAccess) {
        List<CompletableFuture<SourceList>> bonusFutures = new ArrayList<>();
        MutableObject<SourceList> retVal = new MutableObject<>(inputSources.copy());
        
        // Determine bonus affinities from NBT-attached potion data
        Optional<Holder<Potion>> potionHolderOpt = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).potion();
        potionHolderOpt.ifPresent(potionHolder -> {
            IAffinity bonus = this.getAffinity(AffinityTypesPM.POTION_BONUS.get(), BuiltInRegistries.POTION.getKey(potionHolder.value()));
            if (bonus != null) {
                bonusFutures.add(bonus.getTotalAsync(recipeManager, registryAccess, new ArrayList<>()));
            }
        });
        
        // Determine bonus affinities from NBT-attached enchantment data
        ItemEnchantments enchants = stack.getEnchantments();
        enchants.entrySet().forEach(entry -> {
            IAffinity bonus = this.getAffinity(AffinityTypesPM.ENCHANTMENT_BONUS.get(), entry.getKey().unwrapKey().get().identifier());
            if (bonus != null) {
                bonusFutures.add(bonus.getTotalAsync(recipeManager, registryAccess, new ArrayList<>()).thenApply(enchantBonus -> enchantBonus.multiply(entry.getIntValue())));
            }
        });
        
        // Add any detected bonus affinities to the original input
        return Util.sequence(bonusFutures).thenApply(bonusList -> {
            bonusList.forEach(bonus -> retVal.setValue(retVal.getValue().add(bonus)));
            return retVal.getValue();
        });
    }
    
    protected record RecipeValues(Optional<ResourceKey<Recipe<?>>> recipe, SourceList values) {
        public static final RecipeValues EMPTY = new RecipeValues(Optional.empty(), SourceList.EMPTY);
    }
}
