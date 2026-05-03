package com.verdantartifice.primalmagick.common.crafting.display;

import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.world.item.crafting.display.RecipeDisplay;

import java.util.function.Supplier;

/**
 * Holder for mod custom recipe display types.
 *
 * @author Daedalus4096
 */
public class RecipeDisplayTypesPM {
    public static void init() {
        // Pass the service initialization through this class so it gets class loaded and fields registered
        Services.RECIPE_DISPLAY_TYPES_REGISTRY.init();
    }

    public static final IRegistryItem<RecipeDisplay.Type<?>, RecipeDisplay.Type<ShapedArcaneCraftingRecipeDisplay>> SHAPED_ARCANE = register("shaped_arcane", () -> ShapedArcaneCraftingRecipeDisplay.TYPE);
    public static final IRegistryItem<RecipeDisplay.Type<?>, RecipeDisplay.Type<ShapelessArcaneCraftingRecipeDisplay>> SHAPELESS_ARCANE = register("shapeless_arcane", () -> ShapelessArcaneCraftingRecipeDisplay.TYPE);
    public static final IRegistryItem<RecipeDisplay.Type<?>, RecipeDisplay.Type<DissolutionRecipeDisplay>> DISSOLUTION = register("dissolution", () -> DissolutionRecipeDisplay.TYPE);

    private static <T extends RecipeDisplay> IRegistryItem<RecipeDisplay.Type<?>, RecipeDisplay.Type<T>> register(String name, Supplier<RecipeDisplay.Type<T>> supplier) {
        return Services.RECIPE_DISPLAY_TYPES_REGISTRY.register(name, supplier);
    }
}
