package com.verdantartifice.primalmagic.datagen;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.verdantartifice.primalmagic.common.crafting.RecipeSerializersPM;
import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;

import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class ArcaneShapelessRecipeBuilder {
    protected final Item result;
    protected final int count;
    protected final List<Ingredient> ingredients = new ArrayList<>();
    protected String group;
    protected CompoundResearchKey research;
    protected SourceList manaCosts;

    protected ArcaneShapelessRecipeBuilder(IItemProvider result, int count) {
        this.result = result.asItem();
        this.count = count;
    }
    
    public static ArcaneShapelessRecipeBuilder arcaneShapelessRecipe(IItemProvider result, int count) {
        return new ArcaneShapelessRecipeBuilder(result, count);
    }
    
    public static ArcaneShapelessRecipeBuilder arcaneShapelessRecipe(IItemProvider result) {
        return arcaneShapelessRecipe(result, 1);
    }
    
    public ArcaneShapelessRecipeBuilder addIngredient(Ingredient ingredient, int quantity) {
        for (int index = 0; index < quantity; index++) {
            this.ingredients.add(ingredient);
        }
        return this;
    }
    
    public ArcaneShapelessRecipeBuilder addIngredient(Ingredient ingredient) {
        return this.addIngredient(ingredient, 1);
    }
    
    public ArcaneShapelessRecipeBuilder addIngredient(IItemProvider item, int quantity) {
        return this.addIngredient(Ingredient.fromItems(item), quantity);
    }
    
    public ArcaneShapelessRecipeBuilder addIngredient(IItemProvider item) {
        return this.addIngredient(item, 1);
    }
    
    public ArcaneShapelessRecipeBuilder addIngredient(Tag<Item> tag) {
        return this.addIngredient(Ingredient.fromTag(tag));
    }
    
    public ArcaneShapelessRecipeBuilder setGroup(String group) {
        this.group = group;
        return this;
    }
    
    public ArcaneShapelessRecipeBuilder research(CompoundResearchKey research) {
        this.research = research.copy();
        return this;
    }
    
    public ArcaneShapelessRecipeBuilder manaCost(SourceList mana) {
        this.manaCosts = mana.copy();
        return this;
    }
    
    public void build(Consumer<IFinishedRecipe> consumer, ResourceLocation id) {
        this.validate(id);
        consumer.accept(new ArcaneShapelessRecipeBuilder.Result(id, this.result, this.count, this.group == null ? "" : this.group, this.ingredients, this.research, this.manaCosts));
    }
    
    public void build(Consumer<IFinishedRecipe> consumer, String save) {
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(this.result);
        ResourceLocation saveLoc = new ResourceLocation(save);
        if (saveLoc.equals(id)) {
            throw new IllegalStateException("Arcane Shapeless Recipe " + save + " should remove its 'save' argument");
        } else {
            this.build(consumer, saveLoc);
        }
    }
    
    public void build(Consumer<IFinishedRecipe> consumer) {
        this.build(consumer, ForgeRegistries.ITEMS.getKey(this.result));
    }

    protected void validate(ResourceLocation id) {
        if (this.ingredients.isEmpty()) {
            throw new IllegalStateException("No ingredients defined for arcane shapeless recipe " + id + "!");
        }
        if (this.research == null) {
            throw new IllegalStateException("No research is defined for arcane shapeless recipe " + id + "!");
        }
    }
    
    public static class Result implements IFinishedRecipe {
        protected final ResourceLocation id;
        protected final Item result;
        protected final int count;
        protected final String group;
        protected final List<Ingredient> ingredients;
        protected final CompoundResearchKey research;
        protected final SourceList manaCosts;
        
        public Result(ResourceLocation id, Item result, int count, String group, List<Ingredient> ingredients, CompoundResearchKey research, SourceList manaCosts) {
            this.id = id;
            this.result = result;
            this.count = count;
            this.group = group;
            this.ingredients = ingredients;
            this.research = research;
            this.manaCosts = manaCosts;
        }

        @Override
        public void serialize(JsonObject json) {
            if (this.group != null && !this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }
            if (this.research != null) {
                json.addProperty("research", this.research.toString());
            }
            
            if (this.manaCosts != null && !this.manaCosts.isEmpty()) {
                JsonObject manaJson = new JsonObject();
                for (Source source : this.manaCosts.getSourcesSorted()) {
                    manaJson.addProperty(source.getTag(), this.manaCosts.getAmount(source));
                }
                json.add("mana", manaJson);
            }
            
            JsonArray ingredientsJson = new JsonArray();
            for (Ingredient ingredient : this.ingredients) {
                ingredientsJson.add(ingredient.serialize());
            }
            json.add("ingredients", ingredientsJson);
            
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
            return RecipeSerializersPM.ARCANE_CRAFTING_SHAPELESS;
        }

        @Override
        public JsonObject getAdvancementJson() {
            return null;
        }

        @Override
        public ResourceLocation getAdvancementID() {
            return new ResourceLocation("");
        }
    }
}
