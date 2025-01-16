package com.verdantartifice.primalmagick.common.capabilities;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerArcaneRecipeBookForge extends PlayerArcaneRecipeBook implements IPlayerArcaneRecipeBookForge {
    /**
     * Capability provider for the player arcane recipe book capability.  Used to attach capability data to the owner.
     *
     * @author Daedalus4096
     * @see com.verdantartifice.primalmagick.common.events.CapabilityEvents
     */
    public static class Provider implements ICapabilitySerializable<CompoundTag> {
        public static final ResourceLocation NAME = ResourceUtils.loc("capability_arcane_recipe_book");

        private final IPlayerArcaneRecipeBook instance = new PlayerArcaneRecipeBook();
        private final LazyOptional<IPlayerArcaneRecipeBook> holder = LazyOptional.of(() -> instance);   // Cache a lazy optional of the capability instance
        private final RecipeManager recipeManager;

        public Provider(RecipeManager recipeManager) {
            this.recipeManager = recipeManager;
        }

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            if (cap == CapabilitiesForge.ARCANE_RECIPE_BOOK) {
                return holder.cast();
            } else {
                return LazyOptional.empty();
            }
        }

        @Override
        public CompoundTag serializeNBT(HolderLookup.Provider registries) {
            return this.instance.serializeNBT(registries);
        }

        @Override
        public void deserializeNBT(HolderLookup.Provider registries, CompoundTag nbt) {
            this.instance.deserializeNBT(registries, nbt, this.recipeManager);
        }
    }
}
