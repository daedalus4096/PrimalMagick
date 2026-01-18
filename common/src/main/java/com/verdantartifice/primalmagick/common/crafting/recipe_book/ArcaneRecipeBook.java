package com.verdantartifice.primalmagick.common.crafting.recipe_book;

import com.verdantartifice.primalmagick.common.crafting.IArcaneRecipeBookItem;
import net.minecraft.IdentifierException;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Definition of the arcane recipe book.  Like the vanilla recipe book, but it also supports arcane
 * recipes in combination with vanilla recipes.
 * 
 * @author Daedalus4096
 */
public class ArcaneRecipeBook {
    protected static final Logger LOGGER = LogManager.getLogger();
    
    protected final Set<ResourceKey<Recipe<?>>> known = new HashSet<>();
    protected final Set<ResourceKey<Recipe<?>>> highlight = new HashSet<>();
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
    
    public Set<ResourceKey<Recipe<?>>> getKnown() {
        return Collections.unmodifiableSet(this.known);
    }
    
    public Set<ResourceKey<Recipe<?>>> getHighlight() {
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
    
    protected void add(ResourceKey<Recipe<?>> loc) {
        this.known.add(loc);
    }
    
    public boolean contains(@Nullable RecipeHolder<?> recipe) {
        return recipe != null && this.contains(recipe.id());
    }
    
    public boolean contains(ResourceKey<Recipe<?>> loc) {
        return this.known.contains(loc);
    }
    
    public void remove(RecipeHolder<?> recipe) {
        this.remove(recipe.id());
    }
    
    protected void remove(ResourceKey<Recipe<?>> loc) {
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
        this.known.stream()
                .map(loc -> StringTag.valueOf(loc.toString()))
                .forEach(knownList::add);
        tag.put("Recipes", knownList);
        
        ListTag highlightList = new ListTag();
        this.highlight.stream()
                .map(loc -> StringTag.valueOf(loc.toString()))
                .forEach(highlightList::add);
        tag.put("ToBeDisplayed", highlightList);
        
        return tag;
    }
    
    public void fromNbt(CompoundTag tag, RecipeManager recipeManager) {
        this.clear();
        this.setBookSettings(ArcaneRecipeBookSettings.read(tag));
        this.loadRecipes(tag.getListOrEmpty("Recipes"), this::add, recipeManager);
        this.loadRecipes(tag.getListOrEmpty("ToBeDisplayed"), this::addHighlight, recipeManager);
    }
    
    protected void loadRecipes(ListTag tag, Consumer<RecipeHolder<?>> consumer, RecipeManager recipeManager) {
        for (int index = 0; index < tag.size(); index++) {
            tag.getString(index).ifPresent(locStr -> {
                try {
                    ResourceKey<Recipe<?>> loc = ResourceKey.create(Registries.RECIPE, Identifier.parse(locStr));
                    Optional<RecipeHolder<?>> recipeOpt = recipeManager.byKey(loc);
                    recipeOpt.ifPresentOrElse(consumer, () -> LOGGER.error("Failed to load unrecognized recipe: {}, removing", locStr));
                } catch (IdentifierException e) {
                    LOGGER.error("Failed to load improperly formatted recipe: {}, removing", locStr, e);
                }
            });
        }
    }
}
