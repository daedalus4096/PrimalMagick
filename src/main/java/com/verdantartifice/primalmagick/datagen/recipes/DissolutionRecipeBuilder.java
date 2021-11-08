package com.verdantartifice.primalmagick.datagen.recipes;

import java.util.function.Consumer;

import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.common.crafting.RecipeSerializersPM;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

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
    
    public DissolutionRecipeBuilder ingredient(Tag<Item> tag) {
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
    
    public void build(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        this.validate(id);
        consumer.accept(new DissolutionRecipeBuilder.Result(id, this.result, this.ingredient, this.group, this.manaCosts));
    }
    
    public static class Result implements FinishedRecipe {
        protected final ResourceLocation id;
        protected final ItemStack result;
        protected final Ingredient ingredient;
        protected final String group;
        protected final SourceList manaCosts;
        
        public Result(ResourceLocation id, ItemStack result, Ingredient ingredient, String group, SourceList manaCosts) {
            this.id = id;
            this.result = result;
            this.ingredient = ingredient;
            this.group = group;
            this.manaCosts = manaCosts;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            if (this.group != null && !this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }
            
            if (this.manaCosts != null && !this.manaCosts.isEmpty()) {
                JsonObject manaJson = new JsonObject();
                for (Source source : this.manaCosts.getSourcesSorted()) {
                    manaJson.addProperty(source.getTag(), this.manaCosts.getAmount(source));
                }
                json.add("mana", manaJson);
            }

            json.add("ingredient", this.ingredient.toJson());
            
            JsonObject resultJson = new JsonObject();
            resultJson.addProperty("item", ForgeRegistries.ITEMS.getKey(this.result.getItem()).toString());
            resultJson.addProperty("count", this.result.getCount());
            if (this.result.hasTag()) {
                resultJson.addProperty("nbt", this.result.getTag().toString());
            }
            json.add("result", resultJson);
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return RecipeSerializersPM.DISSOLUTION.get();
        }

        @Override
        public JsonObject serializeAdvancement() {
            // Dissolution recipes don't use the vanilla advancement unlock system, so return null
            return null;
        }

        @Override
        public ResourceLocation getAdvancementId() {
            return new ResourceLocation("");
        }
    }
}
