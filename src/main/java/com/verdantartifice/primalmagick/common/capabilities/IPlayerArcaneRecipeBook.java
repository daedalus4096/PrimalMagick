package com.verdantartifice.primalmagick.common.capabilities;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.crafting.recipe_book.ArcaneRecipeBook;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.RecipeManager;

public interface IPlayerArcaneRecipeBook {
    /**
     * Get the active arcane recipe book.
     * 
     * @return the active arcane recipe book
     */
    public ArcaneRecipeBook get();
    
    /**
     * Sync the given player's arcane recipe book data to the their client.
     * 
     * @param player the player whose client should receive the data
     */
    public void sync(@Nullable ServerPlayer player);

    public CompoundTag serializeNBT();

    public void deserializeNBT(CompoundTag nbt, RecipeManager recipeManager);
}
