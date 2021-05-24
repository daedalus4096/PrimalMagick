package com.verdantartifice.primalmagic.datagen.recipes;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.concoctions.ConcoctionType;
import com.verdantartifice.primalmagic.common.concoctions.ConcoctionUtils;
import com.verdantartifice.primalmagic.common.crafting.NBTIngredientPM;
import com.verdantartifice.primalmagic.common.crafting.RecipeSerializersPM;
import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;

import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Definition of a recipe data file builder for concocting recipes.
 * 
 * @author Daedalus4096
 */
public class ConcoctingRecipeBuilder {
    protected final ItemStack result;
    protected final List<Ingredient> ingredients = new ArrayList<>();
    protected CompoundResearchKey research;
    protected SourceList manaCosts;

    protected ConcoctingRecipeBuilder(ItemStack result) {
        this.result = result.copy();
    }
    
    public static ConcoctingRecipeBuilder concoctingRecipe(ItemStack result) {
        return new ConcoctingRecipeBuilder(result);
    }
    
    public ConcoctingRecipeBuilder addIngredient(Ingredient ingredient, int quantity) {
        for (int index = 0; index < quantity; index++) {
            this.ingredients.add(ingredient);
        }
        return this;
    }
    
    public ConcoctingRecipeBuilder addIngredient(Ingredient ingredient) {
        return this.addIngredient(ingredient, 1);
    }
    
    public ConcoctingRecipeBuilder addIngredient(IItemProvider item) {
        return this.addIngredient(item, 1);
    }
    
    public ConcoctingRecipeBuilder addIngredient(IItemProvider item, int quantity) {
        return this.addIngredient(Ingredient.fromItems(item), quantity);
    }
    
    public ConcoctingRecipeBuilder addIngredient(ItemStack stack) {
        ItemStack copy = stack.copy();
        copy.setCount(1);
        return this.addIngredient(NBTIngredientPM.fromStack(stack));
    }
    
    public ConcoctingRecipeBuilder addIngredient(ITag<Item> tag) {
        return this.addIngredient(Ingredient.fromTag(tag));
    }
    
    public ConcoctingRecipeBuilder research(CompoundResearchKey research) {
        this.research = research;
        return this;
    }
    
    public ConcoctingRecipeBuilder manaCost(SourceList mana) {
        this.manaCosts = mana.copy();
        return this;
    }
    
    protected void validate(ResourceLocation id) {
        if (this.ingredients.isEmpty()) {
            throw new IllegalStateException("No ingredients defined for concocting recipe " + id + "!");
        }
        if (this.research == null) {
            throw new IllegalStateException("No research is defined for concocting recipe " + id + "!");
        }
    }
    
    public void build(Consumer<IFinishedRecipe> consumer, ResourceLocation id) {
        this.validate(id);
        consumer.accept(new ConcoctingRecipeBuilder.Result(id, this.result, this.ingredients, this.research, this.manaCosts));
    }
    
    public void build(Consumer<IFinishedRecipe> consumer) {
        Potion potion = PotionUtils.getPotionFromItem(this.result);
        ConcoctionType type = ConcoctionUtils.getConcoctionType(this.result);
        if (type == null || potion == null || potion == Potions.EMPTY) {
            throw new IllegalStateException("Output is not a concoction for concocting recipe with output " + this.result.getDisplayName().getString());
        }
        this.build(consumer, new ResourceLocation(PrimalMagic.MODID, potion.getRegistryName().getPath() + "_" + type.getString()));
    }
    
    public static class Result implements IFinishedRecipe {
        protected final ResourceLocation id;
        protected final ItemStack result;
        protected final List<Ingredient> ingredients;
        protected final CompoundResearchKey research;
        protected final SourceList manaCosts;
        
        public Result(ResourceLocation id, ItemStack result, List<Ingredient> ingredients, CompoundResearchKey research, SourceList manaCosts) {
            this.id = id;
            this.result = result;
            this.ingredients = ingredients;
            this.research = research;
            this.manaCosts = manaCosts;
        }

        @Override
        public void serialize(JsonObject json) {
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
            resultJson.addProperty("item", ForgeRegistries.ITEMS.getKey(this.result.getItem()).toString());
            resultJson.addProperty("count", this.result.getCount());
            if (this.result.hasTag()) {
                resultJson.addProperty("nbt", this.result.getTag().toString());
            }
            json.add("result", resultJson);
        }

        @Override
        public ResourceLocation getID() {
            return this.id;
        }

        @Override
        public IRecipeSerializer<?> getSerializer() {
            return RecipeSerializersPM.CONCOCTING.get();
        }

        @Override
        public JsonObject getAdvancementJson() {
            // Concocting recipes don't use the vanilla advancement unlock system, so return null
            return null;
        }

        @Override
        public ResourceLocation getAdvancementID() {
            return new ResourceLocation("");
        }
    }
}
