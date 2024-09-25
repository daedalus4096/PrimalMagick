package com.verdantartifice.primalmagick.datagen.recipes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.crafting.RunecarvingRecipe;
import com.verdantartifice.primalmagick.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.ResearchTier;
import com.verdantartifice.primalmagick.common.research.keys.ResearchDisciplineKey;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.keys.ResearchStageKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.AndRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Definition of a recipe data file builder for runecarving recipes.
 * 
 * @author Daedalus4096
 */
public class RunecarvingRecipeBuilder {
    protected final ItemStack result;
    protected Ingredient ingredient1;
    protected Ingredient ingredient2;
    protected String group;
    protected final List<AbstractRequirement<?>> requirements = new ArrayList<>();
    protected Optional<Integer> baseExpertiseOverride = Optional.empty();
    protected Optional<Integer> bonusExpertiseOverride = Optional.empty();
    protected Optional<ResourceLocation> expertiseGroup = Optional.empty();
    protected Optional<ResearchDisciplineKey> disciplineOverride = Optional.empty();

    protected RunecarvingRecipeBuilder(ItemLike item, int count) {
        this.result = new ItemStack(item, count);
    }
    
    /**
     * Creates a new builder for a runecarving recipe.
     * 
     * @param result the output item type
     * @param count the output item quantity
     * @return a new builder for a runecarving recipe
     */
    public static RunecarvingRecipeBuilder runecarvingRecipe(ItemLike item, int count) {
        return new RunecarvingRecipeBuilder(item, count);
    }
    
    /**
     * Creates a new builder for a runecarving recipe.
     * 
     * @param result the output item type
     * @return a new builder for a runecarving recipe
     */
    public static RunecarvingRecipeBuilder runecarvingRecipe(ItemLike item) {
        return new RunecarvingRecipeBuilder(item, 1);
    }
    
    /**
     * Add an ingredient to the recipe.
     * 
     * @param ingredient the ingredient to be added
     * @return the modified builder
     */
    public RunecarvingRecipeBuilder firstIngredient(Ingredient ingredient) {
        this.ingredient1 = ingredient;
        return this;
    }
    
    /**
     * Add an ingredient of the given item to the recipe.
     * 
     * @param item the item to be added
     * @return the modified builder
     */
    public RunecarvingRecipeBuilder firstIngredient(ItemLike item) {
        return this.firstIngredient(Ingredient.of(item));
    }
    
    /**
     * Add an ingredient of the given tag to the recipe.
     * 
     * @param tag the tag to be added
     * @return the modified builder
     */
    public RunecarvingRecipeBuilder firstIngredient(TagKey<Item> tag) {
        return this.firstIngredient(Ingredient.of(tag));
    }
    
    /**
     * Add an ingredient to the recipe.
     * 
     * @param ingredient the ingredient to be added
     * @return the modified builder
     */
    public RunecarvingRecipeBuilder secondIngredient(Ingredient ingredient) {
        this.ingredient2 = ingredient;
        return this;
    }
    
    /**
     * Add an ingredient of the given item to the recipe.
     * 
     * @param item the item to be added
     * @return the modified builder
     */
    public RunecarvingRecipeBuilder secondIngredient(ItemLike item) {
        return this.secondIngredient(Ingredient.of(item));
    }
    
    /**
     * Add an ingredient of the given tag to the recipe.
     * 
     * @param tag the tag to be added
     * @return the modified builder
     */
    public RunecarvingRecipeBuilder secondIngredient(TagKey<Item> tag) {
        return this.secondIngredient(Ingredient.of(tag));
    }
    
    /**
     * Adds a group to this recipe.
     * 
     * @param group the group to add
     * @return the modified builder
     */
    public RunecarvingRecipeBuilder setGroup(String group) {
        this.group = group;
        return this;
    }
    
    public RunecarvingRecipeBuilder requirement(AbstractRequirement<?> requirement) {
        this.requirements.add(requirement);
        return this;
    }
    
    public RunecarvingRecipeBuilder requiredResearch(ResourceKey<ResearchEntry> research) {
        return this.requirement(new ResearchRequirement(new ResearchEntryKey(research)));
    }
    
    public RunecarvingRecipeBuilder requiredResearch(ResourceKey<ResearchEntry> research, int stage) {
        return this.requirement(new ResearchRequirement(new ResearchStageKey(research, stage)));
    }
    
    public RunecarvingRecipeBuilder expertise(int baseValue, int bonusValue) {
        this.baseExpertiseOverride = Optional.of(baseValue);
        this.bonusExpertiseOverride = Optional.of(bonusValue);
        return this;
    }
    
    public RunecarvingRecipeBuilder noExpertise() {
        return this.expertise(0, 0);
    }
    
    public RunecarvingRecipeBuilder expertiseTier(ResearchTier tier) {
        return this.expertise(tier.getDefaultExpertise(), tier.getDefaultBonusExpertise());
    }
    
    public RunecarvingRecipeBuilder expertiseGroup(ResourceLocation groupLoc) {
        this.expertiseGroup = Optional.ofNullable(groupLoc);
        return this;
    }
    
    public RunecarvingRecipeBuilder expertiseGroup(String groupName) {
        return this.expertiseGroup(ResourceUtils.loc(groupName));
    }
    
    public RunecarvingRecipeBuilder discipline(ResourceKey<ResearchDiscipline> rawDiscipline) {
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
     * Builds this recipe into an {@link IFinishedRecipe}.
     * 
     * @param output a consumer for the finished recipe
     * @param id the ID of the finished recipe
     */
    public void build(RecipeOutput output, ResourceLocation id) {
        this.validate(id);
        RunecarvingRecipe recipe = new RunecarvingRecipe(Objects.requireNonNullElse(this.group, ""), this.result, this.ingredient1, this.ingredient2, this.getFinalRequirement(),
                this.baseExpertiseOverride, this.bonusExpertiseOverride, this.expertiseGroup, this.disciplineOverride);
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
        ResourceLocation saveLoc = ResourceLocation.parse(save);
        if (saveLoc.equals(id)) {
            throw new IllegalStateException("Runecarving Recipe " + save + " should remove its 'save' argument");
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
        if ( this.ingredient1 == null || this.ingredient1.isEmpty() ||
             this.ingredient2 == null || this.ingredient2.isEmpty() ) {
            throw new IllegalStateException("Missing ingredient for runecarving recipe " + id + "!");
        }
        if (this.requirements.isEmpty()) {
            throw new IllegalStateException("No requirement is defined for runecarving recipe " + id + "!");
        }
    }
}
