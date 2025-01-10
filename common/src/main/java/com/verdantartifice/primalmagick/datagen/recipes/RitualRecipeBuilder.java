package com.verdantartifice.primalmagick.datagen.recipes;

import com.verdantartifice.primalmagick.common.crafting.BlockIngredient;
import com.verdantartifice.primalmagick.common.crafting.RitualRecipe;
import com.verdantartifice.primalmagick.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.ResearchTier;
import com.verdantartifice.primalmagick.common.research.keys.ResearchDisciplineKey;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.keys.ResearchStageKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.AndRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.crafting.ingredients.PartialNBTIngredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    protected final List<AbstractRequirement<?>> requirements = new ArrayList<>();
    protected SourceList manaCosts;
    protected int instability = 0;
    protected Optional<Integer> baseExpertiseOverride = Optional.empty();
    protected Optional<Integer> bonusExpertiseOverride = Optional.empty();
    protected Optional<ResourceLocation> expertiseGroup = Optional.empty();
    protected Optional<ResearchDisciplineKey> disciplineOverride = Optional.empty();

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
    
    public RitualRecipeBuilder requirement(AbstractRequirement<?> requirement) {
        this.requirements.add(requirement);
        return this;
    }
    
    public RitualRecipeBuilder requiredResearch(ResourceKey<ResearchEntry> research) {
        return this.requirement(new ResearchRequirement(new ResearchEntryKey(research)));
    }
    
    public RitualRecipeBuilder requiredResearch(ResourceKey<ResearchEntry> research, int stage) {
        return this.requirement(new ResearchRequirement(new ResearchStageKey(research, stage)));
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
    
    public RitualRecipeBuilder expertise(int baseValue, int bonusValue) {
        this.baseExpertiseOverride = Optional.of(baseValue);
        this.bonusExpertiseOverride = Optional.of(bonusValue);
        return this;
    }
    
    public RitualRecipeBuilder noExpertise() {
        return this.expertise(0, 0);
    }
    
    public RitualRecipeBuilder expertiseTier(ResearchTier tier) {
        return this.expertise(tier.getDefaultExpertise(), tier.getDefaultBonusExpertise());
    }
    
    public RitualRecipeBuilder expertiseGroup(ResourceLocation groupLoc) {
        this.expertiseGroup = Optional.ofNullable(groupLoc);
        return this;
    }
    
    public RitualRecipeBuilder expertiseGroup(String groupName) {
        return this.expertiseGroup(ResourceUtils.loc(groupName));
    }
    
    public RitualRecipeBuilder discipline(ResourceKey<ResearchDiscipline> rawDiscipline) {
        this.disciplineOverride = Optional.of(new ResearchDisciplineKey(rawDiscipline));
        return this;
    }
    
    protected Optional<AbstractRequirement<?>> getFinalRequirement() {
        if (this.requirements.isEmpty()) {
            return Optional.empty();
        } else if (this.requirements.size() == 1) {
            return Optional.of(this.requirements.get(0));
        } else {
            return Optional.of(new AndRequirement(this.requirements));
        }
    }
    
    /**
     * Builds this recipe into a finished recipe.
     * 
     * @param output a consumer for the finished recipe
     * @param id the ID of the finished recipe
     */
    public void build(RecipeOutput output, ResourceLocation id) {
        this.validate(id);
        RitualRecipe recipe = new RitualRecipe(Objects.requireNonNullElse(this.group, ""), this.result, this.ingredients, this.props, this.getFinalRequirement(), this.manaCosts, this.instability,
                this.baseExpertiseOverride, this.bonusExpertiseOverride, this.expertiseGroup, this.disciplineOverride);
        output.accept(id, recipe, null);
    }
    
    /**
     * Builds this recipe into a finished recipe. Use {@link #build(RecipeOutput)} if save is the same as the ID for
     * the result.
     * 
     * @param output a consumer for the finished recipe
     * @param save custom ID for the finished recipe
     */
    public void build(RecipeOutput output, String save) {
        ResourceLocation id = Services.ITEMS_REGISTRY.getKey(this.result.getItem());
        ResourceLocation saveLoc = ResourceLocation.parse(save);
        if (saveLoc.equals(id)) {
            throw new IllegalStateException("Ritual Recipe " + save + " should remove its 'save' argument");
        } else {
            this.build(output, saveLoc);
        }
    }
    
    /**
     * Builds this recipe into a finished recipe.
     * 
     * @param output a consumer for the finished recipe
     */
    public void build(RecipeOutput output) {
        this.build(output, Services.ITEMS_REGISTRY.getKey(this.result.getItem()));
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
        if (this.requirements.isEmpty()) {
            throw new IllegalStateException("No requirement is defined for ritual recipe " + id + "!");
        }
        if (this.instability < RitualRecipe.MIN_INSTABILITY || this.instability > RitualRecipe.MAX_INSTABILITY) {
            throw new IllegalStateException("Instability out of bounds for ritual recipe " + id + "!");
        }
    }
}
