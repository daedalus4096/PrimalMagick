package com.verdantartifice.primalmagick.client.crafting;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Local store for recipe datapack information, synced from the server upon player connection or data reload.
 * 
 * @author Daedalus4096
 */
public class ClientRecipes {
    private static final Set<RecipeType<?>> RECIPE_TYPES = new HashSet<>();
    private static RecipeMap RECIPE_MAP = RecipeMap.EMPTY;

    public static void set(Set<RecipeType<?>> recipeTypes, RecipeMap recipeMap) {
        RECIPE_TYPES.clear();
        RECIPE_TYPES.addAll(recipeTypes);
        RECIPE_MAP = recipeMap;
    }

    public static void reset() {
        RECIPE_TYPES.clear();
        RECIPE_MAP = RecipeMap.EMPTY;
    }

    public static boolean isSynced(RecipeType<?> recipeType) {
        return RECIPE_TYPES.contains(recipeType);
    }

    @NotNull
    public static <I extends RecipeInput, T extends Recipe<I>> Collection<RecipeHolder<T>> byType(RecipeType<T> type) {
        return RECIPE_MAP.byType(type);
    }

    @NotNull
    public static Optional<RecipeHolder<?>> byKey(ResourceKey<Recipe<?>> recipeId) {
        return Optional.ofNullable(RECIPE_MAP.byKey(recipeId));
    }

    @NotNull
    public static <I extends RecipeInput, T extends Recipe<I>> Stream<RecipeHolder<T>> getRecipesFor(RecipeType<T> type, I container, Level level) {
        return RECIPE_MAP.getRecipesFor(type, container, level);
    }

    public static Collection<RecipeHolder<?>> getAll() {
        return RECIPE_MAP.values();
    }
}
