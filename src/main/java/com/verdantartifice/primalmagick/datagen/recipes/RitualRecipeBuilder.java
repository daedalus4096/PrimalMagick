package com.verdantartifice.primalmagick.datagen.recipes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.json.stream.JsonGenerationException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.verdantartifice.primalmagick.common.crafting.BlockIngredient;
import com.verdantartifice.primalmagick.common.crafting.RecipeSerializersPM;
import com.verdantartifice.primalmagick.common.crafting.RitualRecipe;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import net.minecraft.Util;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.crafting.ingredients.PartialNBTIngredient;
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
    public RitualRecipeBuilder addIngredient(TagKey<Item> tag, int quantity) {
        return this.addIngredient(Ingredient.of(tag), quantity);
    }
    
    /**
     * Add an ingredient to the recipe that can be any item in the given tag.
     * 
     * @param tag the tag of items to be added
     * @return the modified builder
     */
    public RitualRecipeBuilder addIngredient(TagKey<Item> tag) {
        return this.addIngredient(tag, 1);
    }
    
    /**
     * Add an ingredient to the recipe that requires a partial NBT match on one or more item types.
     * 
     * @param nbt the NBT data which must be satisfied for an ingredient match
     * @param items the types of allowed items for the ingredient to be added
     * @return the modified builder
     */
    public RitualRecipeBuilder addIngredientNbtPartial(CompoundTag nbt, ItemLike... items) {
        return this.addIngredient(PartialNBTIngredient.of(nbt, items));
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
    public RitualRecipeBuilder addProp(TagKey<Block> tag, int quantity) {
        return this.addProp(BlockIngredient.fromTag(tag), quantity);
    }
    
    /**
     * Add a prop ingredient to the recipe that can be any block in the given tag.
     * 
     * @param tag the tag of blocks to be added
     * @return the modified builder
     */
    public RitualRecipeBuilder addProp(TagKey<Block> tag) {
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
     * Adds a research requirement to this recipe.  Throws if the optional is empty.
     * 
     * @param researchOpt the research requirement to add
     * @return the modified builder
     */
    public RitualRecipeBuilder research(Optional<CompoundResearchKey> researchOpt) {
        return this.research(researchOpt.orElseThrow());
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
     * @param output a consumer for the finished recipe
     * @param id the ID of the finished recipe
     */
    public void build(RecipeOutput output, ResourceLocation id) {
        this.validate(id);
        output.accept(new RitualRecipeBuilder.Result(id, this.result, this.group == null ? "" : this.group, this.ingredients, this.props, this.research, this.manaCosts, this.instability));
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
            throw new IllegalStateException("Ritual Recipe " + save + " should remove its 'save' argument");
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
    
    public static record Result(ResourceLocation id, ItemStack result, String group, List<Ingredient> ingredients, List<BlockIngredient> props, CompoundResearchKey research, SourceList manaCosts, int instability) implements FinishedRecipe {
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
                json.add("mana", Util.getOrThrow(SourceList.CODEC.encodeStart(JsonOps.INSTANCE, this.manaCosts), JsonGenerationException::new));
            }
            
            // Serialize the instability rating
            json.addProperty("instability", this.instability);
            
            // Serialize the recipe ingredient list
            JsonArray ingredientsJson = new JsonArray();
            for (Ingredient ingredient : this.ingredients) {
                ingredientsJson.add(ingredient.toJson(true));
            }
            json.add("ingredients", ingredientsJson);
            
            // Serialize the recipe prop list, if present
            if (this.props != null && !this.props.isEmpty()) {
                JsonArray propsJson = new JsonArray();
                for (BlockIngredient prop : this.props) {
                    propsJson.add(prop.toJson(true));
                }
                json.add("props", propsJson);
            }
            
            // Serialize the recipe result
            json.add("result", Util.getOrThrow(ItemStack.CODEC.encodeStart(JsonOps.INSTANCE, this.result), JsonGenerationException::new));
        }

        @Override
        public RecipeSerializer<?> type() {
            return RecipeSerializersPM.RITUAL.get();
        }

        @Override
        public AdvancementHolder advancement() {
            // Ritual recipes don't use the vanilla advancement unlock system, so return null
            return null;
        }
    }
}
