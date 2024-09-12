package com.verdantartifice.primalmagick.common.crafting.ingredients;

import java.util.function.Supplier;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraftforge.common.crafting.ingredients.AbstractIngredient;
import net.minecraftforge.common.crafting.ingredients.IIngredientSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class IngredientsPM {
    private static final DeferredRegister<IIngredientSerializer<?>> DEFERRED_SERIALIZERS = DeferredRegister.create(ForgeRegistries.INGREDIENT_SERIALIZERS, PrimalMagick.MODID);
    
    public static void init() {
        DEFERRED_SERIALIZERS.register(PrimalMagick.getModLoadingContext().getModEventBus());
    }
    
    public static final RegistryObject<IIngredientSerializer<PartialComponentIngredient>> PARTIAL_COMPONENT = register("partial_component", () -> PartialComponentIngredient.SERIALIZER);
    
    protected static final <T extends AbstractIngredient> RegistryObject<IIngredientSerializer<T>> register(String id, Supplier<IIngredientSerializer<T>> serializerSupplier) {
        return DEFERRED_SERIALIZERS.register(id, serializerSupplier);
    }
}
