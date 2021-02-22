package com.verdantartifice.primalmagic.common.affinities;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.common.sources.SourceList;

import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;

/**
 * Data-defined affinity entry for a block, item, tag, potion bonus, or enchantment bonus.
 * 
 * @author Daedalus4096
 */
public abstract class AffinityEntry {
    protected ResourceLocation id;
    protected SourceList totalCache;
    
    protected AffinityEntry(@Nonnull ResourceLocation id) {
        this.id = id;
    }
    
    public ResourceLocation getId() {
        return this.id;
    }
    
    public SourceList getTotalAffinity(@Nonnull RecipeManager recipeManager) {
        if (this.totalCache == null) {
            this.totalCache = this.calculateTotal(recipeManager);
        }
        return this.totalCache;
    }
    
    protected abstract SourceList calculateTotal(@Nonnull RecipeManager recipeManager);
}
