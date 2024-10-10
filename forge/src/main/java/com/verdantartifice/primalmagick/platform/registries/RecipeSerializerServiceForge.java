package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.crafting.RecipeSerializerRegistration;
import com.verdantartifice.primalmagick.platform.services.IRecipeSerializerService;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Forge implementation of the recipe serializer registry service.
 *
 * @author Daedalus4096
 */
public class RecipeSerializerServiceForge extends AbstractBuiltInRegistryServiceForge<RecipeSerializer<?>> implements IRecipeSerializerService {
    @Override
    protected Supplier<DeferredRegister<RecipeSerializer<?>>> getDeferredRegisterSupplier() {
        return RecipeSerializerRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<RecipeSerializer<?>> getRegistry() {
        return BuiltInRegistries.RECIPE_SERIALIZER;
    }
}
