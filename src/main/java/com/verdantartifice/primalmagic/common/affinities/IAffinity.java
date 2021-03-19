package com.verdantartifice.primalmagic.common.affinities;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.common.sources.SourceList;

import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;

/**
 * Primary interface for a data-defined affinity entry.
 * 
 * @author Daedalus4096
 */
public interface IAffinity {
    ResourceLocation getTarget();
    
    AffinityType getType();
    
    IAffinitySerializer<?> getSerializer();
    
    SourceList getTotal(@Nonnull RecipeManager recipeManager);
}
