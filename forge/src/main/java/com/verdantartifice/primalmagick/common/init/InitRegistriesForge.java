package com.verdantartifice.primalmagick.common.init;

import com.verdantartifice.primalmagick.common.crafting.ingredients.IngredientsPM;
import com.verdantartifice.primalmagick.common.loot.modifiers.LootModifierSerializersPM;

/**
 * Point of initialization for mod deferred registries.
 * 
 * @author Daedalus4096
 */
public class InitRegistriesForge {
    public static void initDeferredRegistries() {
        // Platform implementations of cross-platform registries
        InitRegistries.initDeferredRegistries();

        // Platform specific registries
        IngredientsPM.init();
        LootModifierSerializersPM.init();
    }
}
