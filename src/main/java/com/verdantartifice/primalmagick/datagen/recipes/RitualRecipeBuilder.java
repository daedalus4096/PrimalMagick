package com.verdantartifice.primalmagick.datagen.recipes;

import java.util.Objects;
import java.util.Optional;

import com.verdantartifice.primalmagick.common.crafting.BlockIngredient;
import com.verdantartifice.primalmagick.common.crafting.RitualRecipe;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
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
    protected final NonNullList<Ingredient> ingredients = NonNullList.create();
    protected final NonNullList<BlockIngredient> props = NonNullList.create();
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
        RitualRecipe recipe = new RitualRecipe(Objects.requireNonNullElse(this.group, ""), this.result, this.ingredients, this.props, this.research, this.manaCosts, this.instability);
        output.accept(id, recipe, null);
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
}
