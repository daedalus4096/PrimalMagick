package com.verdantartifice.primalmagick.datagen.recipes;

import javax.json.stream.JsonGenerationException;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.verdantartifice.primalmagick.common.crafting.RecipeSerializersPM;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import net.minecraft.Util;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

/**
 * Definition of a recipe data file builder for tag-outputting dissolution recipes.
 * 
 * @author Daedalus4096
 */
public class DissolutionTagRecipeBuilder {
    protected final TagKey<Item> recipeOutputTag;
    protected final int recipeOutputAmount;
    protected Ingredient ingredient;
    protected String group;
    protected SourceList manaCosts;

    protected DissolutionTagRecipeBuilder(TagKey<Item> resultTag, int resultAmount) {
        this.recipeOutputTag = resultTag;
        this.recipeOutputAmount = resultAmount;
    }
    
    public static DissolutionTagRecipeBuilder dissolutionRecipe(TagKey<Item> resultTag, int resultAmount) {
        return new DissolutionTagRecipeBuilder(resultTag, resultAmount);
    }
    
    public static DissolutionTagRecipeBuilder dissolutionRecipe(TagKey<Item> resultTag) {
        return dissolutionRecipe(resultTag, 1);
    }
    
    public DissolutionTagRecipeBuilder ingredient(Ingredient ingredient) {
        this.ingredient = ingredient;
        return this;
    }
    
    public DissolutionTagRecipeBuilder ingredient(ItemLike item) {
        return this.ingredient(Ingredient.of(item));
    }
    
    public DissolutionTagRecipeBuilder ingredient(TagKey<Item> tag) {
        return this.ingredient(Ingredient.of(tag));
    }
    
    public DissolutionTagRecipeBuilder setGroup(String group) {
        this.group = group;
        return this;
    }
    
    public DissolutionTagRecipeBuilder manaCost(SourceList mana) {
        this.manaCosts = mana.copy();
        return this;
    }
    
    protected void validate(ResourceLocation id) {
        if (this.recipeOutputTag == null) {
            throw new IllegalStateException("No result tag defined for dissolution tag recipe " + id + "!");
        }
        if (this.recipeOutputAmount <= 0) {
            throw new IllegalStateException("Invalid result amount " + this.recipeOutputAmount + " specified for dissolution tag recipe " + id + "!");
        }
        if (this.ingredient == null) {
            throw new IllegalStateException("No ingredient defined for dissolution recipe " + id + "!");
        }
    }
    
    public void build(RecipeOutput output, ResourceLocation id) {
        this.validate(id);
        output.accept(new DissolutionTagRecipeBuilder.Result(id, this.recipeOutputTag, this.recipeOutputAmount, this.ingredient, this.group, this.manaCosts));
    }
    
    public static record Result(ResourceLocation id, TagKey<Item> resultTag, int resultAmount, Ingredient ingredient, String group, SourceList manaCosts) implements FinishedRecipe {
        @Override
        public void serializeRecipeData(JsonObject json) {
            if (this.group != null && !this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }
            
            if (this.manaCosts != null && !this.manaCosts.isEmpty()) {
                json.add("mana", Util.getOrThrow(SourceList.CODEC.encodeStart(JsonOps.INSTANCE, this.manaCosts), JsonGenerationException::new));
            }

            json.add("ingredient", this.ingredient.toJson(true));
            
            json.addProperty("recipeOutputTag", this.resultTag.location().toString());
            json.addProperty("recipeOutputAmount", this.resultAmount);
        }

        @Override
        public RecipeSerializer<?> type() {
            return RecipeSerializersPM.DISSOLUTION_TAG.get();
        }

        @Override
        public AdvancementHolder advancement() {
            // Dissolution recipes don't use the vanilla advancement unlock system, so return null
            return null;
        }
    }
}
