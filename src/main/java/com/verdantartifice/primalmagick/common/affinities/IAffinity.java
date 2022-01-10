package com.verdantartifice.primalmagick.common.affinities;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.sources.SourceList;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

/**
 * Primary interface for a data-defined affinity entry.
 * 
 * @author Daedalus4096
 */
public interface IAffinity {
    ResourceLocation getTarget();
    
    AffinityType getType();
    
    IAffinitySerializer<?> getSerializer();
    
    SourceList getTotal(@Nullable RecipeManager recipeManager, @Nonnull List<ResourceLocation> history);
}
