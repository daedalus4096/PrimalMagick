package com.verdantartifice.primalmagick.common.crafting;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Deferred registry for mod recipe types in Neoforge.
 *
 * @author Daedalus4096
 */
public class RecipeTypeRegistration {
    private static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, Constants.MOD_ID);

    public static DeferredRegister<RecipeType<?>> getDeferredRegister() {
        return TYPES;
    }

    public static void init() {
        TYPES.register(PrimalMagick.getEventBus());
    }
}
