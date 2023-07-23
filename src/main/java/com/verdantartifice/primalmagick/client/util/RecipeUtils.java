package com.verdantartifice.primalmagick.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

public class RecipeUtils {
    /**
     * Utility method to get a recipe's result without having to manually fetch a RegistryAccess every time.
     * 
     * @param recipe
     * @return
     */
    public static ItemStack getResultItem(Recipe<?> recipe) {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;
        if (level == null) {
            throw new NullPointerException("Available level must not be null!");
        }
        return recipe.getResultItem(level.registryAccess());
    }
}
