package com.verdantartifice.primalmagick.common.crafting;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Deferred registry for mod recipe serializers in Neoforge.
 *
 * @author Daedalus4096
 */
public class RecipeSerializerRegistration {
    private static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Constants.MOD_ID);

    public static DeferredRegister<RecipeSerializer<?>> getDeferredRegister() {
        return SERIALIZERS;
    }

    public static void init() {
        SERIALIZERS.register(PrimalMagick.getEventBus());
    }
}
