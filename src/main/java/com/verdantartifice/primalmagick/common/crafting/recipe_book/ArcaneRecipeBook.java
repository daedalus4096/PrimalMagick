package com.verdantartifice.primalmagick.common.crafting.recipe_book;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.verdantartifice.primalmagick.common.crafting.IArcaneRecipeBookItem;

import net.minecraft.ResourceLocationException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

/**
 * Definition of the arcane recipe book.  Like the vanilla recipe book, but it also supports arcane
 * recipes in combination with vanilla recipes.
 * 
 * @author Daedalus4096
 */
public class ArcaneRecipeBook {
    protected static final Logger LOGGER = LogManager.getLogger();
    
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
    
    public void clear() {
        this.known.clear();
        this.highlight.clear();
        this.settings.clear();
    }
    
    public Set<ResourceLocation> getKnown() {
        return Collections.unmodifiableSet(this.known);
    }
    
    public Set<ResourceLocation> getHighlight() {
        return Collections.unmodifiableSet(this.highlight);
    }
    
    public static boolean isValid(RecipeHolder<?> recipe) {
        return recipe.value() instanceof IArcaneRecipeBookItem arbi && !arbi.isArcaneSpecial();
    }
    
    public void add(RecipeHolder<?> recipe) {
        if (isValid(recipe)) {
            this.add(recipe.id());
        }
    }
    
    protected void add(ResourceLocation loc) {
        this.known.add(loc);
    }
    
    public boolean contains(@Nullable RecipeHolder<?> recipe) {
        return recipe == null ? false : this.contains(recipe.id());
    }
    
    public boolean contains(ResourceLocation loc) {
        return this.known.contains(loc);
    }
    
    public void remove(RecipeHolder<?> recipe) {
        this.remove(recipe.id());
    }
    
    protected void remove(ResourceLocation loc) {
        this.known.remove(loc);
        this.highlight.remove(loc);
    }
    
    public boolean willHighlight(RecipeHolder<?> recipe) {
        return this.highlight.contains(recipe.id());
    }
    
    public void removeHighlight(RecipeHolder<?> recipe) {
        this.highlight.remove(recipe.id());
    }
    
    public void addHighlight(RecipeHolder<?> recipe) {
        this.highlight.add(recipe.id());
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
    
    public CompoundTag toNbt() {
        CompoundTag tag = new CompoundTag();
        this.getBookSettings().write(tag);
        
        ListTag knownList = new ListTag();
        for (ResourceLocation loc : this.known) {
            knownList.add(StringTag.valueOf(loc.toString()));
        }
        tag.put("Recipes", knownList);
        
        ListTag highlightList = new ListTag();
        for (ResourceLocation loc : this.highlight) {
            highlightList.add(StringTag.valueOf(loc.toString()));
        }
        tag.put("ToBeDisplayed", highlightList);
        
        return tag;
    }
    
    public void fromNbt(CompoundTag tag, RecipeManager recipeManager) {
        this.clear();
        this.setBookSettings(ArcaneRecipeBookSettings.read(tag));
        this.loadRecipes(tag.getList("Recipes", Tag.TAG_STRING), this::add, recipeManager);
        this.loadRecipes(tag.getList("ToBeDisplayed", Tag.TAG_STRING), this::addHighlight, recipeManager);
    }
    
    protected void loadRecipes(ListTag tag, Consumer<RecipeHolder<?>> consumer, RecipeManager recipeManager) {
        for (int index = 0; index < tag.size(); index++) {
            String locStr = tag.getString(index);
            
            try {
                ResourceLocation loc = new ResourceLocation(locStr);
                Optional<RecipeHolder<?>> recipeOpt = recipeManager.byKey(loc);
                recipeOpt.ifPresentOrElse(recipe -> {
                    consumer.accept(recipe);
                }, () -> {
                    LOGGER.error("Failed to load unrecognized recipe: {}, removing", locStr);
                });
            } catch (ResourceLocationException e) {
                LOGGER.error("Failed to load improperly formatted recipe: {}, removing", locStr, e);
            }
        }
    }
}
