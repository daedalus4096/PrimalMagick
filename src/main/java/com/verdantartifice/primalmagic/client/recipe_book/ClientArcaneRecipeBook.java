package com.verdantartifice.primalmagic.client.recipe_book;

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
import com.verdantartifice.primalmagic.client.gui.recipe_book.ArcaneRecipeCollection;
import com.verdantartifice.primalmagic.common.crafting.RecipeTypesPM;
import com.verdantartifice.primalmagic.common.crafting.recipe_book.ArcaneRecipeBook;

import net.minecraft.core.Registry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

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
    
    public void setupCollections(Iterable<Recipe<?>> recipes) {
        Map<ArcaneRecipeBookCategories, List<List<Recipe<?>>>> recipeListMap = categorizeAndGroupRecipes(recipes);
        Map<ArcaneRecipeBookCategories, List<ArcaneRecipeCollection>> recipeCollectionMap = new HashMap<>();
        Builder<ArcaneRecipeCollection> builder = ImmutableList.builder();

        recipeListMap.forEach((category, recipeLists) -> {
            recipeCollectionMap.put(category, recipeLists.stream().map(ArcaneRecipeCollection::new).peek(builder::add).collect(ImmutableList.toImmutableList()));
        });
        ArcaneRecipeBookCategories.AGGREGATE_CATEGORIES.forEach((searchCategory, subCategories) -> {
            recipeCollectionMap.put(searchCategory, subCategories.stream().flatMap(cat -> {
                return recipeCollectionMap.getOrDefault(cat, ImmutableList.of()).stream();
            }).collect(ImmutableList.toImmutableList()));
        });
        
        this.collectionsByTab = ImmutableMap.copyOf(recipeCollectionMap);
        this.allCollections = builder.build();
    }
    
    protected static Map<ArcaneRecipeBookCategories, List<List<Recipe<?>>>> categorizeAndGroupRecipes(Iterable<Recipe<?>> recipes) {
        Map<ArcaneRecipeBookCategories, List<List<Recipe<?>>>> retVal = new HashMap<>();
        Table<ArcaneRecipeBookCategories, String, List<Recipe<?>>> table = HashBasedTable.create();

        for (Recipe<?> recipe : recipes) {
            if ((ArcaneRecipeBook.isValid(recipe) || !recipe.isSpecial()) && !recipe.isIncomplete()) {
                ArcaneRecipeBookCategories category = getCategory(recipe);
                String group = recipe.getGroup();
                if (group.isEmpty()) {
                    retVal.computeIfAbsent(category, c -> new ArrayList<>()).add(ImmutableList.of(recipe));
                } else {
                    List<Recipe<?>> list = table.get(category, group);
                    if (list == null) {
                        list = new ArrayList<>();
                        table.put(category, group, list);
                        retVal.computeIfAbsent(category, c -> new ArrayList<>()).add(list);
                    }
                    list.add(recipe);
                }
            }
        }
        
        return retVal;
    }
    
    protected static ArcaneRecipeBookCategories getCategory(Recipe<?> recipe) {
        RecipeType<?> type = recipe.getType();
        if (type == RecipeTypesPM.ARCANE_CRAFTING) {
            return ArcaneRecipeBookCategories.CRAFTING_ARCANE;
        } else if (type == RecipeTypesPM.CONCOCTING) {
            // TODO Return appropriate concocting category
            return ArcaneRecipeBookCategories.UNKNOWN;
        } else if (type == RecipeType.CRAFTING) {
            CreativeModeTab tab = recipe.getResultItem().getItem().getItemCategory();
            if (tab == CreativeModeTab.TAB_BUILDING_BLOCKS) {
                return ArcaneRecipeBookCategories.CRAFTING_BUILDING_BLOCKS;
            } else if (tab == CreativeModeTab.TAB_TOOLS || tab == CreativeModeTab.TAB_COMBAT) {
                return ArcaneRecipeBookCategories.CRAFTING_EQUIPMENT;
            } else if (tab == CreativeModeTab.TAB_REDSTONE) {
                return ArcaneRecipeBookCategories.CRAFTING_REDSTONE;
            } else {
                return ArcaneRecipeBookCategories.CRAFTING_MISC;
            }
        } else if (type == RecipeType.SMELTING || type == RecipeType.BLASTING || type == RecipeType.SMOKING || type == RecipeType.STONECUTTING || 
                type == RecipeType.CAMPFIRE_COOKING || type == RecipeType.SMITHING) {
            // We don't deal with these crafting types
            return ArcaneRecipeBookCategories.UNKNOWN;
        } else {
            LOGGER.warn("Unknown recipe category: {}/{}", () -> {
                return Registry.RECIPE_TYPE.getKey(recipe.getType());
            }, recipe::getId);
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
