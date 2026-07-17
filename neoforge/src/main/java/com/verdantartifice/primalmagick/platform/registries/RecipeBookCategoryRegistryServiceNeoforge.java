package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.platform.services.registries.IRecipeBookCategoryRegistryService;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Supplier;

/**
 * Neoforge implementation of the recipe book category registry service.
 *
 * @author Daedalus4096
 */
public class RecipeBookCategoryRegistryServiceNeoforge extends AbstractRegistryServiceNeoforge<RecipeBookCategory> implements IRecipeBookCategoryRegistryService {
    public static final Registry<RecipeBookCategory> CATEGORIES = new RegistryBuilder<>(Registries.RECIPE_BOOK_CATEGORY)
            .sync(true)
            .create();
    private static final DeferredRegister<RecipeBookCategory> DEFERRED_CATEGORIES = DeferredRegister.create(CATEGORIES, Constants.MOD_ID);

    @Override
    protected Supplier<DeferredRegister<RecipeBookCategory>> getDeferredRegisterSupplier() {
        return () -> DEFERRED_CATEGORIES;
    }

    @Override
    protected Registry<RecipeBookCategory> getRegistry() {
        return CATEGORIES;
    }
}
