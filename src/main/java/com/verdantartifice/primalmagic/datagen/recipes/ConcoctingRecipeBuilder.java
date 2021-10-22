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

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Definition of a recipe data file builder for concocting recipes.
 * 
 * @author Daedalus4096
 */
public class ConcoctingRecipeBuilder {
    protected final ItemStack result;
    protected final List<Ingredient> ingredients = new ArrayList<>();
    protected String group;
    protected boolean useDefaultGroup = false;
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
    
    public ConcoctingRecipeBuilder addIngredient(ItemLike item) {
        return this.addIngredient(item, 1);
    }
    
    public ConcoctingRecipeBuilder addIngredient(ItemLike item, int quantity) {
        return this.addIngredient(Ingredient.of(item), quantity);
    }
    
    public ConcoctingRecipeBuilder addIngredient(ItemStack stack) {
        ItemStack copy = stack.copy();
        copy.setCount(1);
        return this.addIngredient(NBTIngredientPM.fromStack(stack));
    }
    
    public ConcoctingRecipeBuilder addIngredient(Tag<Item> tag) {
        return this.addIngredient(Ingredient.of(tag));
    }
    
    public ConcoctingRecipeBuilder setGroup(String group) {
        this.group = group;
        return this;
    }
    
    public ConcoctingRecipeBuilder useDefaultGroup() {
        this.useDefaultGroup = true;
        return this;
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
    
    public void build(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        this.validate(id);
        String groupStr = this.useDefaultGroup ? PotionUtils.getPotion(this.result).getRegistryName().getPath() : this.group;
        consumer.accept(new ConcoctingRecipeBuilder.Result(id, this.result, this.ingredients, groupStr, this.research, this.manaCosts));
    }
    
    public void build(Consumer<FinishedRecipe> consumer) {
        Potion potion = PotionUtils.getPotion(this.result);
        ConcoctionType type = ConcoctionUtils.getConcoctionType(this.result);
        if (type == null || potion == null || potion == Potions.EMPTY) {
            throw new IllegalStateException("Output is not a concoction for concocting recipe with output " + this.result.getHoverName().getString());
        }
        this.build(consumer, new ResourceLocation(PrimalMagic.MODID, potion.getRegistryName().getPath() + "_" + type.getSerializedName()));
    }
    
    public static class Result implements FinishedRecipe {
        protected final ResourceLocation id;
        protected final ItemStack result;
        protected final List<Ingredient> ingredients;
        protected final String group;
        protected final CompoundResearchKey research;
        protected final SourceList manaCosts;
        
        public Result(ResourceLocation id, ItemStack result, List<Ingredient> ingredients, String group, CompoundResearchKey research, SourceList manaCosts) {
            this.id = id;
            this.result = result;
            this.ingredients = ingredients;
            this.group = group;
            this.research = research;
            this.manaCosts = manaCosts;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
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
                ingredientsJson.add(ingredient.toJson());
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
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return RecipeSerializersPM.CONCOCTING.get();
        }

        @Override
        public JsonObject serializeAdvancement() {
            // Concocting recipes don't use the vanilla advancement unlock system, so return null
            return null;
        }

        @Override
        public ResourceLocation getAdvancementId() {
            return new ResourceLocation("");
        }
    }
}
