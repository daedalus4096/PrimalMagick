package com.verdantartifice.primalmagick.datagen.recipes;

import com.verdantartifice.primalmagick.common.concoctions.ConcoctionType;
import com.verdantartifice.primalmagick.common.concoctions.ConcoctionUtils;
import com.verdantartifice.primalmagick.common.crafting.ConcoctingRecipe;
import com.verdantartifice.primalmagick.common.crafting.IConcoctingRecipe;
import com.verdantartifice.primalmagick.common.crafting.recipe_book.ConcoctingBookCategory;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.keys.ResearchStageKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.AndRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.sources.Sources;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Definition of a recipe data file builder for concocting recipes.
 * 
 * @author Daedalus4096
 */
public class ConcoctingRecipeBuilder {
    protected final HolderGetter<Item> itemGetter;
    protected final ItemStack result;
    protected final NonNullList<Ingredient> ingredients = NonNullList.create();
    protected boolean showNotification = true;
    protected ConcoctingBookCategory category = ConcoctingBookCategory.DRINKABLE;
    protected String group;
    protected boolean useDefaultGroup = false;
    protected final List<AbstractRequirement<?>> requirements = new ArrayList<>();
    protected SourceList manaCosts;

    protected ConcoctingRecipeBuilder(HolderGetter<Item> itemGetter, ItemStack result) {
        this.itemGetter = itemGetter;
        this.result = result.copy();
    }
    
    public static ConcoctingRecipeBuilder concoctingRecipe(HolderGetter<Item> itemGetter, ItemStack result) {
        return new ConcoctingRecipeBuilder(itemGetter, result);
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
    
    public ConcoctingRecipeBuilder addIngredient(TagKey<Item> tag) {
        return this.addIngredient(Ingredient.of(this.itemGetter.getOrThrow(tag)));
    }

    public ConcoctingRecipeBuilder showNotification(boolean show) {
        this.showNotification = show;
        return this;
    }

    public ConcoctingRecipeBuilder category(ConcoctingBookCategory category) {
        this.category = category;
        return this;
    }

    public ConcoctingRecipeBuilder setGroup(String group) {
        this.group = group;
        return this;
    }
    
    public ConcoctingRecipeBuilder useDefaultGroup() {
        this.useDefaultGroup = true;
        return this;
    }
    
    public ConcoctingRecipeBuilder requirement(AbstractRequirement<?> requirement) {
        this.requirements.add(requirement);
        return this;
    }
    
    public ConcoctingRecipeBuilder requiredResearch(ResourceKey<ResearchEntry> research) {
        return this.requirement(new ResearchRequirement(new ResearchEntryKey(research)));
    }
    
    public ConcoctingRecipeBuilder requiredResearch(ResourceKey<ResearchEntry> research, int stage) {
        return this.requirement(new ResearchRequirement(new ResearchStageKey(research, stage)));
    }

    public ConcoctingRecipeBuilder defaultManaCost(ConcoctionType type) {
        return this.manaCost(SourceList.EMPTY.add(Sources.INFERNAL, type.getDefaultManaCost()));
    }
    
    public ConcoctingRecipeBuilder manaCost(SourceList mana) {
        return this.centimanaCost(mana.multiply(100));
    }

    public ConcoctingRecipeBuilder centimanaCost(SourceList centimana) {
        this.manaCosts = centimana.copy();
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
    
    protected void validate(ResourceKey<Recipe<?>> id) {
        PotionContents contents = this.result.get(DataComponents.POTION_CONTENTS);
        if (contents == null || contents.potion().isEmpty()) {
            throw new IllegalStateException("No potion effect defined for result of concocting recipe with output " + this.result.getHoverName().getString());
        }
        if (this.category == null) {
            throw new IllegalStateException("Null category specified for concocting recipe " + id + "!");
        }
        if (this.ingredients.isEmpty()) {
            throw new IllegalStateException("No ingredients defined for concocting recipe " + id + "!");
        }
        if (this.requirements.isEmpty()) {
            throw new IllegalStateException("No requirement is defined for concocting recipe " + id + "!");
        }
    }
    
    public void build(RecipeOutput output, ResourceKey<Recipe<?>> id) {
        this.validate(id);
        String groupStr = this.getResultPotionKey().map(Identifier::getPath).orElse(this.group);
        ConcoctingRecipe recipe = new ConcoctingRecipe(
                new Recipe.CommonInfo(this.showNotification),
                new IConcoctingRecipe.ConcoctingCraftingBookInfo(
                        Objects.requireNonNullElse(this.category, ConcoctingBookCategory.DRINKABLE),
                        Objects.requireNonNullElse(groupStr, "")),
                ItemStackTemplate.fromNonEmptyStack(this.result),
                this.ingredients,
                this.getFinalRequirement(),
                Objects.requireNonNullElse(this.manaCosts, SourceList.EMPTY));
        output.accept(id, recipe, null);
    }
    
    public void build(RecipeOutput output) {
        ConcoctionType type = ConcoctionUtils.getConcoctionType(this.result);
        Identifier recipeId = this.getResultPotionKey().map(id -> id.withSuffix("_" + type.getSerializedName())).orElseThrow(() -> new IllegalStateException("Cannot determine concoction ID in recipe builder!"));
        this.build(output, ResourceKey.create(Registries.RECIPE, recipeId));
    }

    private Optional<Identifier> getResultPotionKey() {
        PotionContents contents = this.result.get(DataComponents.POTION_CONTENTS);
        if (contents == null) {
            return Optional.empty();
        } else {
            return contents.potion().map(Holder::value).map(BuiltInRegistries.POTION::getKey);
        }
    }
}
