package com.verdantartifice.primalmagic.datagen.recipes;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.verdantartifice.primalmagic.common.crafting.BlockIngredient;
import com.verdantartifice.primalmagic.common.crafting.RecipeSerializersPM;
import com.verdantartifice.primalmagic.common.crafting.RitualRecipe;
import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;

import net.minecraft.world.level.block.Block;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.ItemLike;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Definition of a recipe data file builder for ritual recipes.
 * 
 * @author Daedalus4096
 */
public class RitualRecipeBuilder {
    protected final ItemStack result;
    protected final List<Ingredient> ingredients = new ArrayList<>();
    protected final List<BlockIngredient> props = new ArrayList<>();
    protected String group;
    protected CompoundResearchKey research;
    protected SourceList manaCosts;
    protected int instability = 0;

    protected RitualRecipeBuilder(ItemStack result) {
        this.result = result.copy();
    }
    
    /**
     * Creates a new builder for a ritual recipe.
     * 
     * @param result the output item type
     * @param count the output item quantity
     * @return a new builder for a ritual recipe
     */
    public static RitualRecipeBuilder ritualRecipe(ItemLike result, int count) {
        return new RitualRecipeBuilder(new ItemStack(result, count));
    }
    
    /**
     * Creates a new builder for a ritual recipe.
     * 
     * @param result the output item type
     * @return a new builder for a ritual recipe
     */
    public static RitualRecipeBuilder ritualRecipe(ItemLike result) {
        return ritualRecipe(result, 1);
    }
    
    /**
     * Creates a new builder for a ritual recipe.
     * 
     * @param result the output item stack
     * @return a new builder for a ritual recipe
     */
    public static RitualRecipeBuilder ritualRecipe(ItemStack result) {
        return new RitualRecipeBuilder(result);
    }
    
    /**
     * Add an ingredient to the recipe multiple times.
     * 
     * @param ingredient the ingredient to be added
     * @param quantity the number of the ingredient to add
     * @return the modified builder
     */
    public RitualRecipeBuilder addIngredient(Ingredient ingredient, int quantity) {
        for (int index = 0; index < quantity; index++) {
            this.ingredients.add(ingredient);
        }
        return this;
    }
    
    /**
     * Add an ingredient to the recipe.
     * 
     * @param ingredient the ingredient to be added
     * @return the modified builder
     */
    public RitualRecipeBuilder addIngredient(Ingredient ingredient) {
        return this.addIngredient(ingredient, 1);
    }
    
    /**
     * Add an ingredient of the given item to the recipe multiple times.
     * 
     * @param item the item to be added
     * @param quantity the number of the item to add
     * @return the modified builder
     */
    public RitualRecipeBuilder addIngredient(ItemLike item, int quantity) {
        return this.addIngredient(Ingredient.of(item), quantity);
    }
    
    /**
     * Add an ingredient of the given item to the recipe.
     * 
     * @param item the item to be added
     * @return the modified builder
     */
    public RitualRecipeBuilder addIngredient(ItemLike item) {
        return this.addIngredient(item, 1);
    }
    
    /**
     * Add an ingredient to the recipe multiple times that can be any item in the given tag.
     * 
     * @param tag the tag of items to be added
     * @param quantity the number of the tag to add
     * @return the modified builder
     */
    public RitualRecipeBuilder addIngredient(Tag<Item> tag, int quantity) {
        return this.addIngredient(Ingredient.of(tag), quantity);
    }
    
    /**
     * Add an ingredient to the recipe that can be any item in the given tag.
     * 
     * @param tag the tag of items to be added
     * @return the modified builder
     */
    public RitualRecipeBuilder addIngredient(Tag<Item> tag) {
        return this.addIngredient(tag, 1);
    }
    
    /**
     * Add a prop ingredient to the recipe multiple times.
     * 
     * @param ingredient the prop ingredient to be added
     * @param quantity the number of the prop ingredient to add
     * @return the modified builder
     */
    public RitualRecipeBuilder addProp(BlockIngredient ingredient, int quantity) {
        for (int index = 0; index < quantity; index++) {
            this.props.add(ingredient);
        }
        return this;
    }
    
    /**
     * Add a prop ingredient to the recipe.
     * 
     * @param ingredient the prop ingredient to be added
     * @return the modified builder
     */
    public RitualRecipeBuilder addProp(BlockIngredient ingredient) {
        return this.addProp(ingredient, 1);
    }
    
    /**
     * Add a prop ingredient of the given block to the recipe multiple times.
     * 
     * @param block the block to be added
     * @param quantity the number of the block to add
     * @return the modified builder
     */
    public RitualRecipeBuilder addProp(Block block, int quantity) {
        return this.addProp(BlockIngredient.fromBlocks(block), quantity);
    }
    
    /**
     * Add a prop ingredient of the given block to the recipe.
     * 
     * @param block the block to be added
     * @return the modified builder
     */
    public RitualRecipeBuilder addProp(Block block) {
        return this.addProp(block, 1);
    }
    
    /**
     * Add a prop ingredient to the recipe multiple times that can be any block in the given tag.
     * 
     * @param tag the tag of blocks to be added
     * @param quantity the number of the tag to add
     * @return the modified builder
     */
    public RitualRecipeBuilder addProp(Tag<Block> tag, int quantity) {
        return this.addProp(BlockIngredient.fromTag(tag), quantity);
    }
    
    /**
     * Add a prop ingredient to the recipe that can be any block in the given tag.
     * 
     * @param tag the tag of blocks to be added
     * @return the modified builder
     */
    public RitualRecipeBuilder addProp(Tag<Block> tag) {
        return this.addProp(tag, 1);
    }
    
    /**
     * Adds a group to this recipe.
     * 
     * @param group the group to add
     * @return the modified builder
     */
    public RitualRecipeBuilder setGroup(String group) {
        this.group = group;
        return this;
    }
    
    /**
     * Adds a research requirement to this recipe.
     * 
     * @param research the research requirement to add
     * @return the modified builder
     */
    public RitualRecipeBuilder research(CompoundResearchKey research) {
        this.research = research.copy();
        return this;
    }
    
    /**
     * Adds a mana cost to this recipe.
     * 
     * @param mana the mana cost to add
     * @return the modified builder
     */
    public RitualRecipeBuilder manaCost(SourceList mana) {
        this.manaCosts = mana.copy();
        return this;
    }
    
    /**
     * Adds an instability rating to this recipe.
     * 
     * @param instability the instability rating to add
     * @return the modified builder
     */
    public RitualRecipeBuilder instability(int instability) {
        this.instability = instability;
        return this;
    }
    
    /**
     * Builds this recipe into an {@link IFinishedRecipe}.
     * 
     * @param consumer a consumer for the finished recipe
     * @param id the ID of the finished recipe
     */
    public void build(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        this.validate(id);
        consumer.accept(new RitualRecipeBuilder.Result(id, this.result, this.group == null ? "" : this.group, this.ingredients, this.props, this.research, this.manaCosts, this.instability));
    }
    
    /**
     * Builds this recipe into an {@link IFinishedRecipe}. Use {@link #build(Consumer)} if save is the same as the ID for
     * the result.
     * 
     * @param consumer a consumer for the finished recipe
     * @param save custom ID for the finished recipe
     */
    public void build(Consumer<FinishedRecipe> consumer, String save) {
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(this.result.getItem());
        ResourceLocation saveLoc = new ResourceLocation(save);
        if (saveLoc.equals(id)) {
            throw new IllegalStateException("Ritual Recipe " + save + " should remove its 'save' argument");
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
        this.build(consumer, ForgeRegistries.ITEMS.getKey(this.result.getItem()));
    }
    
    /**
     * Makes sure that this recipe is valid.
     * 
     * @param id the ID of the recipe
     */
    protected void validate(ResourceLocation id) {
        if (this.ingredients.isEmpty()) {
            throw new IllegalStateException("No ingredients defined for ritual recipe " + id + "!");
        }
        if (this.research == null) {
            throw new IllegalStateException("No research is defined for ritual recipe " + id + "!");
        }
        if (this.instability < RitualRecipe.MIN_INSTABILITY || this.instability > RitualRecipe.MAX_INSTABILITY) {
            throw new IllegalStateException("Instability out of bounds for ritual recipe " + id + "!");
        }
    }
    
    public static class Result implements FinishedRecipe {
        protected final ResourceLocation id;
        protected final ItemStack result;
        protected final String group;
        protected final List<Ingredient> ingredients;
        protected final List<BlockIngredient> props;
        protected final CompoundResearchKey research;
        protected final SourceList manaCosts;
        protected final int instability;

        public Result(ResourceLocation id, ItemStack result, String group, List<Ingredient> ingredients, List<BlockIngredient> props, CompoundResearchKey research, SourceList manaCosts, int instability) {
            this.id = id;
            this.result = result;
            this.group = group;
            this.ingredients = ingredients;
            this.props = props;
            this.research = research;
            this.manaCosts = manaCosts;
            this.instability = instability;
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
            
            // Serialize the recipe mana costs, if present
            if (this.manaCosts != null && !this.manaCosts.isEmpty()) {
                JsonObject manaJson = new JsonObject();
                for (Source source : this.manaCosts.getSourcesSorted()) {
                    manaJson.addProperty(source.getTag(), this.manaCosts.getAmount(source));
                }
                json.add("mana", manaJson);
            }
            
            // Serialize the instability rating
            json.addProperty("instability", this.instability);
            
            // Serialize the recipe ingredient list
            JsonArray ingredientsJson = new JsonArray();
            for (Ingredient ingredient : this.ingredients) {
                ingredientsJson.add(ingredient.toJson());
            }
            json.add("ingredients", ingredientsJson);
            
            // Serialize the recipe prop list, if present
            if (this.props != null && !this.props.isEmpty()) {
                JsonArray propsJson = new JsonArray();
                for (BlockIngredient prop : this.props) {
                    propsJson.add(prop.serialize());
                }
                json.add("props", propsJson);
            }
            
            // Serialize the recipe result
            JsonObject resultJson = new JsonObject();
            resultJson.addProperty("item", ForgeRegistries.ITEMS.getKey(this.result.getItem()).toString());
            if (this.result.getCount() > 1) {
                resultJson.addProperty("count", this.result.getCount());
            }
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
            return RecipeSerializersPM.RITUAL.get();
        }

        @Override
        public JsonObject serializeAdvancement() {
            // Ritual recipes don't use the vanilla advancement unlock system, so return null
            return null;
        }

        @Override
        public ResourceLocation getAdvancementId() {
            return new ResourceLocation("");
        }
    }
}
