package com.verdantartifice.primalmagick.common.crafting.recipe_book;

import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.world.item.crafting.RecipeBookCategory;

/**
 * Holder for mod custom recipe book categories.
 *
 * @author Daedalus4096
 */
public class RecipeBookCategoriesPM {
    public static void init() {
        // Pass the service initialization through this class so it gets class loaded and fields registered
        Services.RECIPE_BOOK_CATEGORIES_REGISTRY.init();
    }

    public static final IRegistryItem<RecipeBookCategory, RecipeBookCategory> CRAFTING_ARCANE = register("crafting_arcane");
    public static final IRegistryItem<RecipeBookCategory, RecipeBookCategory> CONCOCTER_DRINKABLE = register("concocter_drinkable");
    public static final IRegistryItem<RecipeBookCategory, RecipeBookCategory> CONCOCTER_BOMB = register("concocter_bomb");
    public static final IRegistryItem<RecipeBookCategory, RecipeBookCategory> DISSOLUTION_ORES = register("dissolution_ores");
    public static final IRegistryItem<RecipeBookCategory, RecipeBookCategory> DISSOLUTION_MISC = register("dissolution_misc");
    public static final IRegistryItem<RecipeBookCategory, RecipeBookCategory> RUNECARVING_RUNE = register("runecarving_rune");
    public static final IRegistryItem<RecipeBookCategory, RecipeBookCategory> RITUAL_MISC = register("ritual_misc");

    public static final IRegistryItem<RecipeBookCategory, RecipeBookCategory> SEARCH_ARCANE_CRAFTING = register("search_arcane_crafting");
    public static final IRegistryItem<RecipeBookCategory, RecipeBookCategory> SEARCH_CONCOCTER = register("search_concocter");
    public static final IRegistryItem<RecipeBookCategory, RecipeBookCategory> SEARCH_DISSOLUTION = register("search_dissolution");

    private static IRegistryItem<RecipeBookCategory, RecipeBookCategory> register(String id) {
        return Services.RECIPE_BOOK_CATEGORIES_REGISTRY.register(id, RecipeBookCategory::new);
    }
}
