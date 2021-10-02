package com.verdantartifice.primalmagic.common.crafting.recipebook;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;

/**
 * Definition of the arcane recipe book.  Like the vanilla recipe book, but it also supports arcane
 * recipes in combination with vanilla recipes.
 * 
 * @author Daedalus4096
 */
public class ArcaneRecipeBook {
    protected final Set<ResourceLocation> known = new HashSet<>();
    protected final Set<ResourceLocation> highlight = new HashSet<>();
    protected final ArcaneRecipeBookSettings settings = new ArcaneRecipeBookSettings();

    public void copyOverData(ArcaneRecipeBook other) {
        this.known.clear();
        this.highlight.clear();
        this.settings.replaceFrom(other.settings);
        this.known.addAll(other.known);
        this.highlight.addAll(other.highlight);
    }
    
    public void add(Recipe<?> recipe) {
        // TODO Test if it's a special arcane recipe (instead?)
        if (!recipe.isSpecial()) {
            this.add(recipe.getId());
        }
    }
    
    protected void add(ResourceLocation loc) {
        this.known.add(loc);
    }
    
    public boolean contains(@Nullable Recipe<?> recipe) {
        return recipe == null ? false : this.contains(recipe.getId());
    }
    
    public boolean contains(ResourceLocation loc) {
        return this.known.contains(loc);
    }
    
    public void remove(Recipe<?> recipe) {
        this.remove(recipe.getId());
    }
    
    protected void remove(ResourceLocation loc) {
        this.known.remove(loc);
        this.highlight.remove(loc);
    }
    
    public boolean willHighlight(Recipe<?> recipe) {
        return this.highlight.contains(recipe.getId());
    }
    
    public void removeHighlight(Recipe<?> recipe) {
        this.highlight.remove(recipe.getId());
    }
    
    public void addHighlight(Recipe<?> recipe) {
        this.highlight.add(recipe.getId());
    }
    
    public boolean isOpen(ArcaneRecipeBookType type) {
        return this.settings.isOpen(type);
    }
    
    public void setOpen(ArcaneRecipeBookType type, boolean open) {
        this.settings.setOpen(type, open);
    }
    
    public boolean isFiltering(ArcaneRecipeBookType type) {
        return this.settings.isFiltering(type);
    }
    
    public void setFiltering(ArcaneRecipeBookType type, boolean filtering) {
        this.settings.setFiltering(type, filtering);
    }
    
    public void setBookSettings(ArcaneRecipeBookSettings settings) {
        this.settings.replaceFrom(settings);
    }
    
    public ArcaneRecipeBookSettings getBookSettings() {
        return this.settings.copy();
    }
    
    public void setBookSettings(ArcaneRecipeBookType type, boolean open, boolean filtering) {
        this.settings.setOpen(type, open);
        this.settings.setFiltering(type, filtering);
    }
}
