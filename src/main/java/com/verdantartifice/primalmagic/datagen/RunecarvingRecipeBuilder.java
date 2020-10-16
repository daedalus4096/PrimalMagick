package com.verdantartifice.primalmagic.datagen;

import java.util.function.Consumer;

import com.google.gson.JsonObject;
import com.verdantartifice.primalmagic.common.crafting.RecipeSerializersPM;
import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;

import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
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
    
    protected RunecarvingRecipeBuilder(IItemProvider item, int count) {
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
    public static RunecarvingRecipeBuilder runecarvingRecipe(IItemProvider item, int count) {
        return new RunecarvingRecipeBuilder(item, count);
    }
    
    /**
     * Creates a new builder for a runecarving recipe.
     * 
     * @param result the output item type
     * @return a new builder for a runecarving recipe
     */
    public static RunecarvingRecipeBuilder runecarvingRecipe(IItemProvider item) {
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
    public RunecarvingRecipeBuilder firstIngredient(IItemProvider item) {
        return this.firstIngredient(Ingredient.fromItems(item));
    }
    
    /**
     * Add an ingredient of the given tag to the recipe.
     * 
     * @param tag the tag to be added
     * @return the modified builder
     */
    public RunecarvingRecipeBuilder firstIngredient(ITag<Item> tag) {
        return this.firstIngredient(Ingredient.fromTag(tag));
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
    public RunecarvingRecipeBuilder secondIngredient(IItemProvider item) {
        return this.secondIngredient(Ingredient.fromItems(item));
    }
    
    /**
     * Add an ingredient of the given tag to the recipe.
     * 
     * @param tag the tag to be added
     * @return the modified builder
     */
    public RunecarvingRecipeBuilder secondIngredient(ITag<Item> tag) {
        return this.secondIngredient(Ingredient.fromTag(tag));
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
     * Builds this recipe into an {@link IFinishedRecipe}.
     * 
     * @param consumer a consumer for the finished recipe
     * @param id the ID of the finished recipe
     */
    public void build(Consumer<IFinishedRecipe> consumer, ResourceLocation id) {
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
    public void build(Consumer<IFinishedRecipe> consumer, String save) {
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
    public void build(Consumer<IFinishedRecipe> consumer) {
        this.build(consumer, ForgeRegistries.ITEMS.getKey(this.result));
    }
    
    /**
     * Makes sure that this recipe is valid.
     * 
     * @param id the ID of the recipe
     */
    protected void validate(ResourceLocation id) {
        if ( this.ingredient1 == null || this.ingredient1.hasNoMatchingItems() ||
             this.ingredient2 == null || this.ingredient2.hasNoMatchingItems() ) {
            throw new IllegalStateException("Missing ingredient for runecarving recipe " + id + "!");
        }
        if (this.research == null) {
            throw new IllegalStateException("No research is defined for runecarving recipe " + id + "!");
        }
    }
    
    public static class Result implements IFinishedRecipe {
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
        public void serialize(JsonObject json) {
            // Serialize the recipe group, if present
            if (this.group != null && !this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }
            
            // Serialize the recipe research requirement, if present
            if (this.research != null) {
                json.addProperty("research", this.research.toString());
            }
            
            // Serialize the recipe ingredients
            json.add("ingredient1", this.ingredient1.serialize());
            json.add("ingredient2", this.ingredient2.serialize());
            
            // Serialize the recipe result
            JsonObject resultJson = new JsonObject();
            resultJson.addProperty("item", ForgeRegistries.ITEMS.getKey(this.result).toString());
            if (this.count > 1) {
                resultJson.addProperty("count", this.count);
            }
            json.add("result", resultJson);
        }

        @Override
        public ResourceLocation getID() {
            return this.id;
        }

        @Override
        public IRecipeSerializer<?> getSerializer() {
            return RecipeSerializersPM.RUNECARVING.get();
        }

        @Override
        public JsonObject getAdvancementJson() {
            // Runecarving recipes don't use the vanilla advancement unlock system, so return null
            return null;
        }

        @Override
        public ResourceLocation getAdvancementID() {
            return new ResourceLocation("");
        }
    }
}
