package com.verdantartifice.primalmagick.common.affinities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.containers.FakeContainer;
import com.verdantartifice.primalmagick.common.crafting.IHasManaCost;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import net.minecraft.client.Minecraft;
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

    protected AffinityManager() {
        super(GSON, "affinities");
    }

    @SubscribeEvent
    public static void onResourceReload(AddReloadListenerEvent event) {
        event.addListener(createInstance());
    }
    
    public static AffinityManager createInstance() {
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

        Minecraft mc  = Minecraft.getInstance();
        Level world = mc.level;

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
    
    @Nullable
    public IAffinity getAffinity(AffinityType type, ResourceLocation id) {
        return this.affinities.getOrDefault(type, Collections.emptyMap()).get(id);
    }
    
    @Nullable
    public IAffinity getOrGenerateItemAffinity(@Nonnull ResourceLocation id, @Nonnull RecipeManager recipeManager, @Nonnull RegistryAccess registryAccess, @Nonnull List<ResourceLocation> history) {
        Map<ResourceLocation, IAffinity> map = this.affinities.computeIfAbsent(AffinityType.ITEM, (affinityType) -> {
            return new HashMap<>();
        });
        if (map.containsKey(id)) {
            return map.get(id);
        } else {
            return this.generateItemAffinity(id, recipeManager, registryAccess, history);
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
    
    @Nullable
    public SourceList getAffinityValues(@Nullable ItemStack stack, @Nonnull Level level) {
        return this.getAffinityValues(stack, level.getRecipeManager(), level.registryAccess(), new ArrayList<>());
    }
    
    @Nullable
    protected SourceList getAffinityValues(@Nullable ItemStack stack, @Nonnull RecipeManager recipeManager, @Nonnull RegistryAccess registryAccess, @Nonnull List<ResourceLocation> history) {
        if (stack == null || stack.isEmpty()) {
            return null;
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
    
    @Nullable
    protected IAffinity generateItemAffinity(@Nonnull ResourceLocation id, @Nonnull RecipeManager recipeManager, @Nonnull RegistryAccess registryAccess, @Nonnull List<ResourceLocation> history) {
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
            SourceList values = this.generateItemAffinityValuesFromRecipes(id, recipeManager, registryAccess, history);
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
    protected SourceList generateItemAffinityValuesFromRecipes(@Nonnull ResourceLocation id, @Nonnull RecipeManager recipeManager, @Nonnull RegistryAccess registryAccess, @Nonnull List<ResourceLocation> history) {
        SourceList retVal = null;
        int maxValue = Integer.MAX_VALUE;
        
        // Look up all recipes with the given item as an output
        for (Recipe<?> recipe : recipeManager.getRecipes().stream().filter(r -> r.getResultItem(registryAccess) != null && ForgeRegistries.ITEMS.getKey(r.getResultItem(registryAccess).getItem()).equals(id)).collect(Collectors.toList())) {
            // Compute the affinities from the recipe's ingredients
            SourceList ingSources = this.generateItemAffinityValuesFromIngredients(recipe, recipeManager, registryAccess, history);
            if (recipe instanceof IHasManaCost) {
                // Add affinities from mana costs
                IHasManaCost manaRecipe = (IHasManaCost)recipe;
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
    protected SourceList generateItemAffinityValuesFromIngredients(@Nonnull Recipe<?> recipe, @Nonnull RecipeManager recipeManager, @Nonnull RegistryAccess registryAccess, @Nonnull List<ResourceLocation> history) {
        NonNullList<Ingredient> ingredients = recipe.getIngredients();
        ItemStack output = recipe.getResultItem(registryAccess);
        SourceList intermediate = new SourceList();
        
        // Populate a fake crafting inventory with ingredients to see what container items would be left behind
        NonNullList<ItemStack> containerList = NonNullList.create();
        if (recipe instanceof CraftingRecipe) {
            CraftingRecipe craftingRecipe = (CraftingRecipe)recipe;
            CraftingContainer inv = new CraftingContainer(new FakeContainer(), ingredients.size(), 1);
            int index = 0;
            for (Ingredient ingredient : ingredients) {
                ItemStack ingStack = this.getMatchingItemStack(ingredient, recipeManager, registryAccess, history);
                if (!ingStack.isEmpty()) {
                    inv.setItem(index, ingStack);
                }
                index++;
            }
            containerList = craftingRecipe.getRemainingItems(inv);
        }

        // Compute total affinities for each ingredient
        for (Ingredient ingredient : ingredients) {
            ItemStack ingStack = this.getMatchingItemStack(ingredient, recipeManager, registryAccess, history);
            if (!ingStack.isEmpty()) {
                SourceList ingSources = this.getAffinityValues(ingStack, recipeManager, registryAccess, history);
                if (ingSources != null) {
                    intermediate.add(ingSources);
                }
            }
        }
        
        // Subtract affinities for remaining containers
        if (containerList != null) {
            for (ItemStack containerStack : containerList) {
                if (!containerStack.isEmpty()) {
                    SourceList containerSources = this.getAffinityValues(containerStack, recipeManager, registryAccess, history);
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
    protected ItemStack getMatchingItemStack(@Nullable Ingredient ingredient, @Nonnull RecipeManager recipeManager, @Nonnull RegistryAccess registryAccess, @Nonnull List<ResourceLocation> history) {
        if (ingredient == null || ingredient.getItems() == null || ingredient.getItems().length <= 0) {
            return ItemStack.EMPTY;
        }
        
        int maxValue = Integer.MAX_VALUE;
        ItemStack retVal = ItemStack.EMPTY;
        
        // Scan through all of the ingredient's possible matches to determine which one to use for affinity computation
        for (ItemStack stack : ingredient.getItems()) {
            SourceList stackSources = this.getAffinityValues(stack, recipeManager, registryAccess, history);
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
