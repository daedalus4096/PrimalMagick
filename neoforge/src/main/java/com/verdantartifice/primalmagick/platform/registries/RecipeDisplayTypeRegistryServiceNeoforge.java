package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.platform.services.registries.IRecipeDisplayTypeRegistryService;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Supplier;

/**
 * Neoforge implementation of the research key type registry service.
 *
 * @author Daedalus4096
 */
public class RecipeDisplayTypeRegistryServiceNeoforge extends AbstractRegistryServiceNeoforge<RecipeDisplay.Type<?>> implements IRecipeDisplayTypeRegistryService {
    public static final Registry<RecipeDisplay.Type<?>> TYPES = new RegistryBuilder<>(Registries.RECIPE_DISPLAY)
            .sync(true)
            .create();
    private static final DeferredRegister<RecipeDisplay.Type<?>> DEFERRED_TYPES = DeferredRegister.create(TYPES, Constants.MOD_ID);

    @Override
    protected Supplier<DeferredRegister<RecipeDisplay.Type<?>>> getDeferredRegisterSupplier() {
        return () -> DEFERRED_TYPES;
    }

    @Override
    protected Registry<RecipeDisplay.Type<?>> getRegistry() {
        return TYPES;
    }
}
