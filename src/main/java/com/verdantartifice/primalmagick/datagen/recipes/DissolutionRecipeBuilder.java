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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

/**
 * Definition of a recipe data file builder for dissolution recipes.
 * 
 * @author Daedalus4096
 */
public class DissolutionRecipeBuilder {
    protected final ItemStack result;
    protected Ingredient ingredient;
    protected String group;
    protected SourceList manaCosts;

    protected DissolutionRecipeBuilder(ItemStack result) {
        this.result = result.copy();
    }
    
    public static DissolutionRecipeBuilder dissolutionRecipe(ItemStack result) {
        return new DissolutionRecipeBuilder(result);
    }
    
    public static DissolutionRecipeBuilder dissolutionRecipe(ItemLike item, int count) {
        return dissolutionRecipe(new ItemStack(item.asItem(), count));
    }
    
    public static DissolutionRecipeBuilder dissolutionRecipe(ItemLike item) {
        return dissolutionRecipe(item, 1);
    }
    
    public DissolutionRecipeBuilder ingredient(Ingredient ingredient) {
        this.ingredient = ingredient;
        return this;
    }
    
    public DissolutionRecipeBuilder ingredient(ItemLike item) {
        return this.ingredient(Ingredient.of(item));
    }
    
    public DissolutionRecipeBuilder ingredient(TagKey<Item> tag) {
        return this.ingredient(Ingredient.of(tag));
    }
    
    public DissolutionRecipeBuilder setGroup(String group) {
        this.group = group;
        return this;
    }
    
    public DissolutionRecipeBuilder manaCost(SourceList mana) {
        this.manaCosts = mana.copy();
        return this;
    }
    
    protected void validate(ResourceLocation id) {
        if (this.ingredient == null) {
            throw new IllegalStateException("No ingredient defined for dissolution recipe " + id + "!");
        }
    }
    
    public void build(RecipeOutput output, ResourceLocation id) {
        this.validate(id);
        output.accept(new DissolutionRecipeBuilder.Result(id, this.result, this.ingredient, this.group, this.manaCosts));
    }
    
    public static record Result(ResourceLocation id, ItemStack result, Ingredient ingredient, String group, SourceList manaCosts) implements FinishedRecipe {
        @Override
        public void serializeRecipeData(JsonObject json) {
            if (this.group != null && !this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }
            
            if (this.manaCosts != null && !this.manaCosts.isEmpty()) {
                json.add("mana", Util.getOrThrow(SourceList.CODEC.encodeStart(JsonOps.INSTANCE, this.manaCosts), JsonGenerationException::new));
            }

            json.add("ingredient", this.ingredient.toJson(true));
            
            json.add("result", Util.getOrThrow(ItemStack.CODEC.encodeStart(JsonOps.INSTANCE, this.result), JsonGenerationException::new));
        }

        @Override
        public RecipeSerializer<?> type() {
            return RecipeSerializersPM.DISSOLUTION.get();
        }

        @Override
        public AdvancementHolder advancement() {
            // Dissolution recipes don't use the vanilla advancement unlock system, so return null
            return null;
        }
    }
}
