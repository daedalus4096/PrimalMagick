package com.verdantartifice.primalmagick.datagen.recipes;

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
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Definition of a recipe data file builder for runecarving recipes.
 * 
 * @author Daedalus4096
 */
public class RunecarvingRecipeBuilder {
    protected final HolderGetter<Item> itemGetter;
    protected final ItemStack result;
    protected boolean showNotification = true;
    protected Ingredient baseIngredient;
    protected Ingredient etchingIngredient;
    protected final List<AbstractRequirement<?>> requirements = new ArrayList<>();
    protected Optional<Integer> baseExpertiseOverride = Optional.empty();
    protected Optional<Integer> bonusExpertiseOverride = Optional.empty();
    protected Optional<Identifier> expertiseGroup = Optional.empty();
    protected Optional<ResearchDisciplineKey> disciplineOverride = Optional.empty();

    protected RunecarvingRecipeBuilder(HolderGetter<Item> itemGetter, ItemLike item, int count) {
        this.itemGetter = itemGetter;
        this.result = new ItemStack(item, count);
    }
    
    /**
     * Creates a new builder for a runecarving recipe.
     * 
     * @param item the output item type
     * @param count the output item quantity
     * @return a new builder for a runecarving recipe
     */
    public static RunecarvingRecipeBuilder runecarvingRecipe(HolderGetter<Item> itemGetter, ItemLike item, int count) {
        return new RunecarvingRecipeBuilder(itemGetter, item, count);
    }
    
    /**
     * Creates a new builder for a runecarving recipe.
     * 
     * @param item the output item type
     * @return a new builder for a runecarving recipe
     */
    public static RunecarvingRecipeBuilder runecarvingRecipe(HolderGetter<Item> itemGetter, ItemLike item) {
        return new RunecarvingRecipeBuilder(itemGetter, item, 1);
    }
    
    /**
     * Add an ingredient to the recipe.
     * 
     * @param ingredient the ingredient to be added
     * @return the modified builder
     */
    public RunecarvingRecipeBuilder baseIngredient(Ingredient ingredient) {
        this.baseIngredient = ingredient;
        return this;
    }
    
    /**
     * Add an ingredient of the given item to the recipe.
     * 
     * @param item the item to be added
     * @return the modified builder
     */
    public RunecarvingRecipeBuilder baseIngredient(ItemLike item) {
        return this.baseIngredient(Ingredient.of(item));
    }
    
    /**
     * Add an ingredient of the given tag to the recipe.
     * 
     * @param tag the tag to be added
     * @return the modified builder
     */
    public RunecarvingRecipeBuilder baseIngredient(TagKey<Item> tag) {
        return this.baseIngredient(Ingredient.of(this.itemGetter.getOrThrow(tag)));
    }
    
    /**
     * Add an ingredient to the recipe.
     * 
     * @param ingredient the ingredient to be added
     * @return the modified builder
     */
    public RunecarvingRecipeBuilder etchingIngredient(Ingredient ingredient) {
        this.etchingIngredient = ingredient;
        return this;
    }
    
    /**
     * Add an ingredient of the given item to the recipe.
     * 
     * @param item the item to be added
     * @return the modified builder
     */
    public RunecarvingRecipeBuilder etchingIngredient(ItemLike item) {
        return this.etchingIngredient(Ingredient.of(item));
    }
    
    /**
     * Add an ingredient of the given tag to the recipe.
     * 
     * @param tag the tag to be added
     * @return the modified builder
     */
    public RunecarvingRecipeBuilder etchingIngredient(TagKey<Item> tag) {
        return this.etchingIngredient(Ingredient.of(this.itemGetter.getOrThrow(tag)));
    }

    public RunecarvingRecipeBuilder showNotification(boolean show) {
        this.showNotification = show;
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
    
    public RunecarvingRecipeBuilder expertiseGroup(Identifier groupLoc) {
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
            return Optional.of(this.requirements.getFirst());
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
    public void build(RecipeOutput output, ResourceKey<Recipe<?>> id) {
        this.validate(id);
        RunecarvingRecipe recipe = new RunecarvingRecipe(
                new Recipe.CommonInfo(this.showNotification),
                ItemStackTemplate.fromNonEmptyStack(this.result),
                this.baseIngredient,
                this.etchingIngredient,
                this.getFinalRequirement(),
                this.baseExpertiseOverride,
                this.bonusExpertiseOverride,
                this.expertiseGroup,
                this.disciplineOverride);
        output.accept(id, recipe, null);
    }

    public void build(RecipeOutput output, Identifier id) {
        this.build(output, ResourceKey.create(Registries.RECIPE, id));
    }
    
    /**
     * Builds this recipe into a finished recipe. Use {@link #build(RecipeOutput)} if save is the same as the ID for
     * the result.
     * 
     * @param output a consumer for the finished recipe
     * @param save custom ID for the finished recipe
     */
    public void build(RecipeOutput output, String save) {
        Identifier id = Services.ITEMS_REGISTRY.getKey(this.result.getItem());
        Identifier saveLoc = Identifier.parse(save);
        if (saveLoc.equals(id)) {
            throw new IllegalStateException("Runecarving Recipe " + save + " should remove its 'save' argument");
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
    protected void validate(ResourceKey<Recipe<?>> id) {
        if ( this.baseIngredient == null || this.baseIngredient.isEmpty() ||
             this.etchingIngredient == null || this.etchingIngredient.isEmpty() ) {
            throw new IllegalStateException("Missing ingredient for runecarving recipe " + id + "!");
        }
        if (this.requirements.isEmpty()) {
            throw new IllegalStateException("No requirement is defined for runecarving recipe " + id + "!");
        }
    }
}
