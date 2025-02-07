package com.verdantartifice.primalmagick.client.events;

import com.verdantartifice.primalmagick.client.recipe_book.ArcaneSearchRegistry;
import net.minecraft.world.item.crafting.RecipeManager;

/**
 * Respond to client-only Forge registration events.
 * 
 * @author Daedalus4096
 */
public class ClientResourceEvents {
    public static void onRecipesUpdated(RecipeManager recipeManager) {
        ArcaneSearchRegistry.populate();
    }
}
