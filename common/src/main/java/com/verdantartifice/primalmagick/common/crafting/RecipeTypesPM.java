package com.verdantartifice.primalmagick.common.crafting;

import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.function.Supplier;

/**
 * Holder for mod custom recipe types.
 * 
 * @author Daedalus4096
 */
public class RecipeTypesPM {
    public static void init() {
        // Pass the service initialization through this class so it gets class loaded and fields registered
        Services.RECIPE_TYPES_REGISTRY.init();
    }

    public static final IRegistryItem<RecipeType<?>, RecipeType<IArcaneRecipe>> ARCANE_CRAFTING = register("arcane_crafting", () -> simple("arcane_crafting"));
    public static final IRegistryItem<RecipeType<?>, RecipeType<IRitualRecipe>> RITUAL = register("ritual", () -> simple("ritual"));
    public static final IRegistryItem<RecipeType<?>, RecipeType<IRunecarvingRecipe>> RUNECARVING = register("runecarving", () -> simple("runecarving"));
    public static final IRegistryItem<RecipeType<?>, RecipeType<IConcoctingRecipe>> CONCOCTING = register("concocting", () -> simple("concocting"));
    public static final IRegistryItem<RecipeType<?>, RecipeType<IDissolutionRecipe>> DISSOLUTION = register("dissolution", () -> simple("dissolution"));

    private static <T extends Recipe<?>> IRegistryItem<RecipeType<?>, RecipeType<T>> register(String name, Supplier<RecipeType<T>> supplier) {
        return Services.RECIPE_TYPES_REGISTRY.register(name, supplier);
    }

    private static <T extends Recipe<?>> RecipeType<T> simple(String name) {
        final String locStr = ResourceUtils.loc(name).toString();
        return new RecipeType<>() {
            @Override
            public String toString() {
                return locStr;
            }
        };
    }
}
