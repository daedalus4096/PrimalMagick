package com.verdantartifice.primalmagick.common.capabilities;

import com.verdantartifice.primalmagick.common.crafting.recipe_book.ArcaneRecipeBook;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

import javax.annotation.Nullable;

@AutoRegisterCapability
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

    public CompoundTag serializeNBT(HolderLookup.Provider registries);

    public void deserializeNBT(HolderLookup.Provider registries, CompoundTag nbt, RecipeManager recipeManager);
}
