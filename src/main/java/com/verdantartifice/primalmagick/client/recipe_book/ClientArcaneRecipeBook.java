package com.verdantartifice.primalmagick.client.recipe_book;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Table;
import com.verdantartifice.primalmagick.client.gui.recipe_book.ArcaneRecipeCollection;
import com.verdantartifice.primalmagick.common.crafting.RecipeTypesPM;
import com.verdantartifice.primalmagick.common.crafting.recipe_book.ArcaneRecipeBook;
import com.verdantartifice.primalmagick.common.items.concoctions.AlchemicalBombItem;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraftforge.common.Tags;

/**
 * A client-side representation of the arcane recipe book data, capable of interacting safely with
 * client-side Minecraft code.
 * 
 * @author Daedalus4096
 */
public class ClientArcaneRecipeBook {
    protected static final Logger LOGGER = LogManager.getLogger();
    protected Map<ArcaneRecipeBookCategories, List<ArcaneRecipeCollection>> collectionsByTab = ImmutableMap.of();
    protected List<ArcaneRecipeCollection> allCollections = ImmutableList.of();
    protected final ArcaneRecipeBook book;
    
    public ClientArcaneRecipeBook(ArcaneRecipeBook book) {
        this.book = book;
    }
    
    public void setupCollections(Iterable<RecipeHolder<?>> recipes, RegistryAccess registryAccess) {
        Map<ArcaneRecipeBookCategories, List<List<RecipeHolder<?>>>> recipeListMap = categorizeAndGroupRecipes(recipes, registryAccess);
        Map<ArcaneRecipeBookCategories, List<ArcaneRecipeCollection>> recipeCollectionMap = new HashMap<>();
        Builder<ArcaneRecipeCollection> builder = ImmutableList.builder();

        recipeListMap.forEach((category, recipeLists) -> {
            recipeCollectionMap.put(category, recipeLists.stream().map(recipeList -> {
                return new ArcaneRecipeCollection(registryAccess, recipeList);
            }).peek(builder::add).collect(ImmutableList.toImmutableList()));
        });
        ArcaneRecipeBookCategories.AGGREGATE_CATEGORIES.forEach((searchCategory, subCategories) -> {
            recipeCollectionMap.put(searchCategory, subCategories.stream().flatMap(cat -> {
                return recipeCollectionMap.getOrDefault(cat, ImmutableList.of()).stream();
            }).collect(ImmutableList.toImmutableList()));
        });
        
        this.collectionsByTab = ImmutableMap.copyOf(recipeCollectionMap);
        this.allCollections = builder.build();
    }
    
    protected static Map<ArcaneRecipeBookCategories, List<List<RecipeHolder<?>>>> categorizeAndGroupRecipes(Iterable<RecipeHolder<?>> recipes, RegistryAccess registryAccess) {
        Map<ArcaneRecipeBookCategories, List<List<RecipeHolder<?>>>> retVal = new HashMap<>();
        Table<ArcaneRecipeBookCategories, String, List<RecipeHolder<?>>> table = HashBasedTable.create();

        for (RecipeHolder<?> recipeHolder : recipes) {
            Recipe<?> recipe = recipeHolder.value();
            if ((ArcaneRecipeBook.isValid(recipeHolder) || !recipe.isSpecial()) && !recipe.isIncomplete()) {
                ArcaneRecipeBookCategories category = getCategory(recipe, registryAccess);
                String group = recipe.getGroup();
                if (group.isEmpty()) {
                    retVal.computeIfAbsent(category, c -> new ArrayList<>()).add(ImmutableList.of(recipeHolder));
                } else {
                    List<RecipeHolder<?>> list = table.get(category, group);
                    if (list == null) {
                        list = new ArrayList<>();
                        table.put(category, group, list);
                        retVal.computeIfAbsent(category, c -> new ArrayList<>()).add(list);
                    }
                    list.add(recipeHolder);
                }
            }
        }
        
        return retVal;
    }
    
    protected static ArcaneRecipeBookCategories getCategory(Recipe<?> recipe, RegistryAccess registryAccess) {
        RecipeType<?> type = recipe.getType();
        if (type == RecipeTypesPM.ARCANE_CRAFTING.get()) {
            return ArcaneRecipeBookCategories.CRAFTING_ARCANE;
        } else if (type == RecipeTypesPM.CONCOCTING.get()) {
            return recipe.getResultItem(registryAccess).getItem() instanceof AlchemicalBombItem ? ArcaneRecipeBookCategories.CONCOCTER_BOMB : ArcaneRecipeBookCategories.CONCOCTER_DRINKABLE;
        } else if (type == RecipeTypesPM.DISSOLUTION.get()) {
            return recipe.getResultItem(registryAccess).is(Tags.Items.DUSTS) ? ArcaneRecipeBookCategories.DISSOLUTION_ORES : ArcaneRecipeBookCategories.DISSOLUTION_MISC;
        } else if (type == RecipeType.CRAFTING && recipe instanceof CraftingRecipe craftingRecipe) {
            return switch (craftingRecipe.category()) {
                case BUILDING -> ArcaneRecipeBookCategories.CRAFTING_BUILDING_BLOCKS;
                case EQUIPMENT -> ArcaneRecipeBookCategories.CRAFTING_EQUIPMENT;
                case REDSTONE -> ArcaneRecipeBookCategories.CRAFTING_REDSTONE;
                case MISC -> ArcaneRecipeBookCategories.CRAFTING_MISC;
            };
        } else if (type == RecipeType.SMELTING && recipe instanceof SmeltingRecipe smeltingRecipe) {
            return switch (smeltingRecipe.category()) {
                case BLOCKS -> ArcaneRecipeBookCategories.FURNACE_BLOCKS;
                case FOOD -> ArcaneRecipeBookCategories.FURNACE_FOOD;
                case MISC -> ArcaneRecipeBookCategories.FURNACE_MISC;
            };
        } else {
            // We don't deal with these crafting types
            return ArcaneRecipeBookCategories.UNKNOWN;
        }
    }
    
    public ArcaneRecipeBook getData() {
        return this.book;
    }
    
    public List<ArcaneRecipeCollection> getCollections() {
        return this.allCollections;
    }
    
    public List<ArcaneRecipeCollection> getCollection(ArcaneRecipeBookCategories category) {
        return this.collectionsByTab.getOrDefault(category, Collections.emptyList());
    }
}
