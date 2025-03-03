package com.verdantartifice.primalmagick.datagen.recipes;

import com.verdantartifice.primalmagick.common.concoctions.ConcoctionType;
import com.verdantartifice.primalmagick.common.concoctions.ConcoctionUtils;
import com.verdantartifice.primalmagick.common.crafting.ConcoctingRecipe;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.keys.ResearchStageKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.AndRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.Ingredient;
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
    protected final ItemStack result;
    protected final NonNullList<Ingredient> ingredients = NonNullList.create();
    protected String group;
    protected boolean useDefaultGroup = false;
    protected final List<AbstractRequirement<?>> requirements = new ArrayList<>();
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
    
    public ConcoctingRecipeBuilder addIngredient(TagKey<Item> tag) {
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
            return Optional.of(this.requirements.get(0));
        } else {
            return Optional.of(new AndRequirement(this.requirements));
        }
    }
    
    protected void validate(ResourceLocation id) {
        if (this.ingredients.isEmpty()) {
            throw new IllegalStateException("No ingredients defined for concocting recipe " + id + "!");
        }
        if (this.requirements.isEmpty()) {
            throw new IllegalStateException("No requirement is defined for concocting recipe " + id + "!");
        }
    }
    
    public void build(RecipeOutput output, ResourceLocation id) {
        this.validate(id);
        String groupStr = this.useDefaultGroup ? BuiltInRegistries.POTION.getKey(this.result.get(DataComponents.POTION_CONTENTS).potion().get().value()).getPath() : this.group;
        ConcoctingRecipe recipe = new ConcoctingRecipe(Objects.requireNonNullElse(groupStr, ""), this.result, this.ingredients, this.getFinalRequirement(), Objects.requireNonNullElse(this.manaCosts, SourceList.EMPTY));
        output.accept(id, recipe, null);
    }
    
    public void build(RecipeOutput output) {
        PotionContents contents = this.result.get(DataComponents.POTION_CONTENTS);
        if (contents == null || contents.potion().isEmpty()) {
            throw new IllegalStateException("No potion effect defined for result of concocting recipe with output " + this.result.getHoverName().getString());
        }
        ConcoctionType type = ConcoctionUtils.getConcoctionType(this.result);
        if (type == null) {
            throw new IllegalStateException("Output is not a concoction for concocting recipe with output " + this.result.getHoverName().getString());
        }
        this.build(output, ResourceUtils.loc(BuiltInRegistries.POTION.getKey(contents.potion().get().value()).getPath() + "_" + type.getSerializedName()));
    }
}
