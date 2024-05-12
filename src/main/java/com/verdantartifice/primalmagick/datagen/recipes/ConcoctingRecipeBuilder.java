package com.verdantartifice.primalmagick.datagen.recipes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.concoctions.ConcoctionType;
import com.verdantartifice.primalmagick.common.concoctions.ConcoctionUtils;
import com.verdantartifice.primalmagick.common.crafting.ConcoctingRecipe;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.AndRequirement;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import net.minecraft.Util;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.ingredients.PartialNBTIngredient;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Definition of a recipe data file builder for concocting recipes.
 * 
 * @author Daedalus4096
 */
public class ConcoctingRecipeBuilder {
    protected static final CompoundTag WATER_FLASK_TAG = Util.make(new CompoundTag(), tag -> {
        tag.putString("Potion", "minecraft:water");
        tag.putString("ConcoctionType", ConcoctionType.WATER.getSerializedName());
    });
    protected static final CompoundTag WATER_BOMB_TAG = Util.make(new CompoundTag(), tag -> {
        tag.putString("Potion", "minecraft:water");
        tag.putString("ConcoctionType", ConcoctionType.BOMB.getSerializedName());
    });
    
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
    
    public ConcoctingRecipeBuilder addWaterFlaskIngredient() {
        // Can't use strict NBT ingredients because the Mojang codec turns the integer doses field into a
        // byte during deserialization, causing the strict NBT comparison to fail when querying recipes.
        return this.addIngredient(PartialNBTIngredient.of(ItemsPM.CONCOCTION.get(), WATER_FLASK_TAG));
    }
    
    public ConcoctingRecipeBuilder addWaterBombIngredient() {
        // Can't use strict NBT ingredients because the Mojang codec turns the integer doses field into a
        // byte during deserialization, causing the strict NBT comparison to fail when querying recipes.
        return this.addIngredient(PartialNBTIngredient.of(ItemsPM.ALCHEMICAL_BOMB.get(), WATER_BOMB_TAG));
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
    
    public ConcoctingRecipeBuilder manaCost(SourceList mana) {
        this.manaCosts = mana.copy();
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
        String groupStr = this.useDefaultGroup ? ForgeRegistries.POTIONS.getKey(PotionUtils.getPotion(this.result)).getPath() : this.group;
        ConcoctingRecipe recipe = new ConcoctingRecipe(Objects.requireNonNullElse(groupStr, ""), this.result, this.ingredients, this.getFinalRequirement(), Objects.requireNonNullElse(this.manaCosts, SourceList.EMPTY));
        output.accept(id, recipe, null);
    }
    
    public void build(RecipeOutput output) {
        Potion potion = PotionUtils.getPotion(this.result);
        ConcoctionType type = ConcoctionUtils.getConcoctionType(this.result);
        if (type == null || potion == null || potion == Potions.EMPTY) {
            throw new IllegalStateException("Output is not a concoction for concocting recipe with output " + this.result.getHoverName().getString());
        }
        this.build(output, PrimalMagick.resource(ForgeRegistries.POTIONS.getKey(potion).getPath() + "_" + type.getSerializedName()));
    }
}
