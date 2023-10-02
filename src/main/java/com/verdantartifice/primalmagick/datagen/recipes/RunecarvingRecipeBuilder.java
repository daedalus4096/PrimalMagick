package com.verdantartifice.primalmagick.datagen.recipes;

import java.util.Optional;
import java.util.function.Consumer;

import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.common.crafting.RecipeSerializersPM;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
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
    protected final Item result;
    protected final int count;
    protected Ingredient ingredient1;
    protected Ingredient ingredient2;
    protected String group;
    protected CompoundResearchKey research;
    
    protected RunecarvingRecipeBuilder(ItemLike item, int count) {
        this.result = item.asItem();
        this.count = count;
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
     * @param consumer a consumer for the finished recipe
     * @param id the ID of the finished recipe
     */
    public void build(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        this.validate(id);
        consumer.accept(new RunecarvingRecipeBuilder.Result(id, this.result, this.count, this.group == null ? "" : this.group, this.ingredient1, this.ingredient2, this.research));
    }
    
    /**
     * Builds this recipe into an {@link IFinishedRecipe}. Use {@link #build(Consumer)} if save is the same as the ID for
     * the result.
     * 
     * @param consumer a consumer for the finished recipe
     * @param save custom ID for the finished recipe
     */
    public void build(Consumer<FinishedRecipe> consumer, String save) {
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(this.result);
        ResourceLocation saveLoc = new ResourceLocation(save);
        if (saveLoc.equals(id)) {
            throw new IllegalStateException("Runecarving Recipe " + save + " should remove its 'save' argument");
        } else {
            this.build(consumer, saveLoc);
        }
    }
    
    /**
     * Builds this recipe into an {@link IFinishedRecipe}.
     * 
     * @param consumer a consumer for the finished recipe
     */
    public void build(Consumer<FinishedRecipe> consumer) {
        this.build(consumer, ForgeRegistries.ITEMS.getKey(this.result));
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
    
    public static class Result implements FinishedRecipe {
        protected final ResourceLocation id;
        protected final Item result;
        protected final int count;
        protected final String group;
        protected final Ingredient ingredient1;
        protected final Ingredient ingredient2;
        protected final CompoundResearchKey research;
        
        public Result(ResourceLocation id, Item result, int count, String group, Ingredient ing1, Ingredient ing2, CompoundResearchKey research) {
            this.id = id;
            this.result = result;
            this.count = count;
            this.group = group;
            this.ingredient1 = ing1;
            this.ingredient2 = ing2;
            this.research = research;
        }

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
            json.add("ingredient1", this.ingredient1.toJson());
            json.add("ingredient2", this.ingredient2.toJson());
            
            // Serialize the recipe result
            JsonObject resultJson = new JsonObject();
            resultJson.addProperty("item", ForgeRegistries.ITEMS.getKey(this.result).toString());
            if (this.count > 1) {
                resultJson.addProperty("count", this.count);
            }
            json.add("result", resultJson);
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return RecipeSerializersPM.RUNECARVING.get();
        }

        @Override
        public JsonObject serializeAdvancement() {
            // Runecarving recipes don't use the vanilla advancement unlock system, so return null
            return null;
        }

        @Override
        public ResourceLocation getAdvancementId() {
            return new ResourceLocation("");
        }
    }
}
