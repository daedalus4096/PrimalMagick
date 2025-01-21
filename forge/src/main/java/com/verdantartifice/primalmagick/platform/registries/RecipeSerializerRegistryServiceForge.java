package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.platform.services.registries.IRecipeSerializerRegistryService;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Forge implementation of the recipe serializer registry service.
 *
 * @author Daedalus4096
 */
public class RecipeSerializerRegistryServiceForge extends AbstractBuiltInRegistryServiceForge<RecipeSerializer<?>> implements IRecipeSerializerRegistryService {
    private static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Constants.MOD_ID);

    @Override
    protected Supplier<DeferredRegister<RecipeSerializer<?>>> getDeferredRegisterSupplier() {
        return () -> SERIALIZERS;
    }

    @Override
    protected Registry<RecipeSerializer<?>> getRegistry() {
        return BuiltInRegistries.RECIPE_SERIALIZER;
    }
}
