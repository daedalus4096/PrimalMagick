package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.crafting.RecipeTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.registries.IRecipeTypeRegistryService;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Neoforge implementation of the recipe type registry service.
 *
 * @author Daedalus4096
 */
public class RecipeTypeRegistryServiceNeoforge extends AbstractRegistryServiceNeoforge<RecipeType<?>> implements IRecipeTypeRegistryService {
    @Override
    protected Supplier<DeferredRegister<RecipeType<?>>> getDeferredRegisterSupplier() {
        return RecipeTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<RecipeType<?>> getRegistry() {
        return BuiltInRegistries.RECIPE_TYPE;
    }
}
