package com.verdantartifice.primalmagick.client.events;

import com.verdantartifice.primalmagick.client.crafting.ClientRecipes;
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.Set;

public class ClientDatapackEvents {
    public static void onRecipesReceived(Set<RecipeType<?>> recipeTypes, RecipeMap recipeMap) {
        // Import the recipe data from the server into the client store for the player
        // TODO Reset the ClientRecipes store when the player logs out
        ClientRecipes.set(recipeTypes, recipeMap);
    }
}
