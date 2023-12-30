package com.verdantartifice.primalmagick.datagen.recipes;

import java.util.Optional;

import javax.json.stream.JsonGenerationException;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.verdantartifice.primalmagick.common.crafting.RecipeSerializersPM;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;

import net.minecraft.Util;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Definition of a recipe data file builder for runecarving recipes.
 * 
 * @author Daedalus4096
 */
public class RunecarvingRecipeBuilder {
    protected final ItemStack result;
    protected Ingredient ingredient1;
    protected Ingredient ingredient2;
    protected String group;
    protected CompoundResearchKey research;
    
    protected RunecarvingRecipeBuilder(ItemLike item, int count) {
        this.result = new ItemStack(item, count);
    }
    
    /**
     * Creates a new builder for a runecarving recipe.
     * 
     * @param result the output item type
     * @param count the output item quantity
     * @return a new builder for a runecarving recipe
     */
    public static RunecarvingRecipeBuilder runecarvingRecipe(ItemLike item, int count) {
        return new RunecarvingRecipeBuilder(item, count);
    }
    
    /**
     * Creates a new builder for a runecarving recipe.
     * 
     * @param result the output item type
     * @return a new builder for a runecarving recipe
     */
    public static RunecarvingRecipeBuilder runecarvingRecipe(ItemLike item) {
        return new RunecarvingRecipeBuilder(item, 1);
    }
    
    /**
     * Add an ingredient to the recipe.
     * 
     * @param ingredient the ingredient to be added
     * @return the modified builder
     */
    public RunecarvingRecipeBuilder firstIngredient(Ingredient ingredient) {
        this.ingredient1 = ingredient;
        return this;
    }
    
    /**
     * Add an ingredient of the given item to the recipe.
     * 
     * @param item the item to be added
     * @return the modified builder
     */
    public RunecarvingRecipeBuilder firstIngredient(ItemLike item) {
        return this.firstIngredient(Ingredient.of(item));
    }
    
    /**
     * Add an ingredient of the given tag to the recipe.
     * 
     * @param tag the tag to be added
     * @return the modified builder
     */
    public RunecarvingRecipeBuilder firstIngredient(TagKey<Item> tag) {
        return this.firstIngredient(Ingredient.of(tag));
    }
    
    /**
     * Add an ingredient to the recipe.
     * 
     * @param ingredient the ingredient to be added
     * @return the modified builder
     */
    public RunecarvingRecipeBuilder secondIngredient(Ingredient ingredient) {
        this.ingredient2 = ingredient;
        return this;
    }
    
    /**
     * Add an ingredient of the given item to the recipe.
     * 
     * @param item the item to be added
     * @return the modified builder
     */
    public RunecarvingRecipeBuilder secondIngredient(ItemLike item) {
        return this.secondIngredient(Ingredient.of(item));
    }
    
    /**
     * Add an ingredient of the given tag to the recipe.
     * 
     * @param tag the tag to be added
     * @return the modified builder
     */
    public RunecarvingRecipeBuilder secondIngredient(TagKey<Item> tag) {
        return this.secondIngredient(Ingredient.of(tag));
    }
    
    /**
     * Adds a group to this recipe.
     * 
     * @param group the group to add
     * @return the modified builder
     */
    public RunecarvingRecipeBuilder setGroup(String group) {
        this.group = group;
        return this;
    }
    
    /**
     * Adds a research requirement to this recipe.
     * 
     * @param research the research requirement to add
     * @return the modified builder
     */
    public RunecarvingRecipeBuilder research(CompoundResearchKey research) {
        this.research = research.copy();
        return this;
    }
    
    /**
     * Adds a research requirement to this recipe.  Throws if the optional is empty.
     * 
     * @param researchOpt the research requirement to add
     * @return the modified builder
     */
    public RunecarvingRecipeBuilder research(Optional<CompoundResearchKey> researchOpt) {
        return this.research(researchOpt.orElseThrow());
    }
    
    /**
     * Builds this recipe into an {@link IFinishedRecipe}.
     * 
     * @param output a consumer for the finished recipe
     * @param id the ID of the finished recipe
     */
    public void build(RecipeOutput output, ResourceLocation id) {
        this.validate(id);
        output.accept(new RunecarvingRecipeBuilder.Result(id, this.result, this.group == null ? "" : this.group, this.ingredient1, this.ingredient2, this.research));
    }
    
    /**
     * Builds this recipe into an {@link IFinishedRecipe}. Use {@link #build(RecipeOutput)} if save is the same as the ID for
     * the result.
     * 
     * @param output a consumer for the finished recipe
     * @param save custom ID for the finished recipe
     */
    public void build(RecipeOutput output, String save) {
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(this.result.getItem());
        ResourceLocation saveLoc = new ResourceLocation(save);
        if (saveLoc.equals(id)) {
            throw new IllegalStateException("Runecarving Recipe " + save + " should remove its 'save' argument");
        } else {
            this.build(output, saveLoc);
        }
    }
    
    /**
     * Builds this recipe into an {@link IFinishedRecipe}.
     * 
     * @param output a consumer for the finished recipe
     */
    public void build(RecipeOutput output) {
        this.build(output, ForgeRegistries.ITEMS.getKey(this.result.getItem()));
    }
    
    /**
     * Makes sure that this recipe is valid.
     * 
     * @param id the ID of the recipe
     */
    protected void validate(ResourceLocation id) {
        if ( this.ingredient1 == null || this.ingredient1.isEmpty() ||
             this.ingredient2 == null || this.ingredient2.isEmpty() ) {
            throw new IllegalStateException("Missing ingredient for runecarving recipe " + id + "!");
        }
        if (this.research == null) {
            throw new IllegalStateException("No research is defined for runecarving recipe " + id + "!");
        }
    }
    
    public static record Result(ResourceLocation id, ItemStack result, String group, Ingredient ingredient1, Ingredient ingredient2, CompoundResearchKey research) implements FinishedRecipe {
        @Override
        public void serializeRecipeData(JsonObject json) {
            // Serialize the recipe group, if present
            if (this.group != null && !this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }
            
            // Serialize the recipe research requirement, if present
            if (this.research != null) {
                json.addProperty("research", this.research.toString());
            }
            
            // Serialize the recipe ingredients
            json.add("ingredient1", this.ingredient1.toJson(true));
            json.add("ingredient2", this.ingredient2.toJson(true));
            
            // Serialize the recipe result
            json.add("result", Util.getOrThrow(ItemStack.CODEC.encodeStart(JsonOps.INSTANCE, this.result), JsonGenerationException::new));
        }

        @Override
        public RecipeSerializer<?> type() {
            return RecipeSerializersPM.RUNECARVING.get();
        }

        @Override
        public AdvancementHolder advancement() {
            // Runecarving recipes don't use the vanilla advancement unlock system, so return null
            return null;
        }
    }
}
