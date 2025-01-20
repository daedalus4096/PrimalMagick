package com.verdantartifice.primalmagick.datagen.recipes;

import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.concoctions.ConcoctionType;
import com.verdantartifice.primalmagick.common.crafting.ingredients.PartialComponentIngredient;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.tags.CommonTags;
import com.verdantartifice.primalmagick.common.tags.ItemTagsForgeExt;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.AndCondition;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import net.minecraftforge.common.crafting.conditions.TagEmptyCondition;
import net.minecraftforge.common.crafting.conditions.TrueCondition;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RecipesForge extends Recipes {
    public RecipesForge(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, registries);
    }

    private static ICondition tagsNotEmpty(List<TagKey<Item>> tags) {
        List<ICondition> subConditions = tags.stream().<ICondition>map(t -> new NotCondition(new TagEmptyCondition(t))).toList();
        return new AndCondition(subConditions);
    }

    @Override
    protected void buildRecipes(RecipeOutput pRecipeOutput) {
        this.buildRecipesInner(pRecipeOutput, pRecipeOutput.registry());
    }

    @Override
    protected void registerEarthshatterHammerConditionalRecipes(RecipeOutput consumer) {
        ConditionalRecipe.builder()
                .mainCondition(tagsNotEmpty(List.of(ItemTagsForgeExt.DUSTS_IRON, CommonTags.Items.ORES_IRON)))
                .condition(TrueCondition.INSTANCE)
                .recipe(output -> ShapelessTagRecipeBuilder.shapelessTagRecipe(RecipeCategory.MISC, ItemTagsForgeExt.DUSTS_IRON, 2)
                        .addIngredient(ItemsPM.EARTHSHATTER_HAMMER.get())
                        .addIngredient(CommonTags.Items.ORES_IRON)
                        .setGroup("earthshatter_hammer_grit")
                        .unlockedBy("has_hammer", has(ItemsPM.EARTHSHATTER_HAMMER.get()))
                        .build(output, ResourceUtils.loc("iron_grit_from_ore")))
                .save(consumer, ResourceUtils.loc("iron_grit_from_ore"));
        ConditionalRecipe.builder()
                .mainCondition(tagsNotEmpty(List.of(ItemTagsForgeExt.DUSTS_IRON, CommonTags.Items.RAW_MATERIALS_IRON)))
                .condition(TrueCondition.INSTANCE)
                .recipe(output -> ShapelessTagRecipeBuilder.shapelessTagRecipe(RecipeCategory.MISC, ItemTagsForgeExt.DUSTS_IRON, 2)
                        .addIngredient(ItemsPM.EARTHSHATTER_HAMMER.get())
                        .addIngredient(CommonTags.Items.RAW_MATERIALS_IRON)
                        .setGroup("earthshatter_hammer_grit")
                        .unlockedBy("has_hammer", has(ItemsPM.EARTHSHATTER_HAMMER.get()))
                        .build(output, ResourceUtils.loc("iron_grit_from_raw_metal")))
                .save(consumer, ResourceUtils.loc("iron_grit_from_raw_metal"));
        ConditionalRecipe.builder()
                .mainCondition(tagsNotEmpty(List.of(ItemTagsForgeExt.DUSTS_GOLD, CommonTags.Items.ORES_GOLD)))
                .condition(TrueCondition.INSTANCE)
                .recipe(output -> ShapelessTagRecipeBuilder.shapelessTagRecipe(RecipeCategory.MISC, ItemTagsForgeExt.DUSTS_GOLD, 2)
                        .addIngredient(ItemsPM.EARTHSHATTER_HAMMER.get())
                        .addIngredient(CommonTags.Items.ORES_GOLD)
                        .setGroup("earthshatter_hammer_grit")
                        .unlockedBy("has_hammer", has(ItemsPM.EARTHSHATTER_HAMMER.get()))
                        .build(output, ResourceUtils.loc("gold_grit_from_ore")))
                .save(consumer, ResourceUtils.loc("gold_grit_from_ore"));
        ConditionalRecipe.builder()
                .mainCondition(tagsNotEmpty(List.of(ItemTagsForgeExt.DUSTS_GOLD, CommonTags.Items.RAW_MATERIALS_GOLD)))
                .condition(TrueCondition.INSTANCE)
                .recipe(output -> ShapelessTagRecipeBuilder.shapelessTagRecipe(RecipeCategory.MISC, ItemTagsForgeExt.DUSTS_GOLD, 2)
                        .addIngredient(ItemsPM.EARTHSHATTER_HAMMER.get())
                        .addIngredient(CommonTags.Items.RAW_MATERIALS_GOLD)
                        .setGroup("earthshatter_hammer_grit")
                        .unlockedBy("has_hammer", has(ItemsPM.EARTHSHATTER_HAMMER.get()))
                        .build(output, ResourceUtils.loc("gold_grit_from_raw_metal")))
                .save(consumer, ResourceUtils.loc("gold_grit_from_raw_metal"));
        ConditionalRecipe.builder()
                .mainCondition(tagsNotEmpty(List.of(ItemTagsForgeExt.DUSTS_COPPER, CommonTags.Items.ORES_COPPER)))
                .condition(TrueCondition.INSTANCE)
                .recipe(output -> ShapelessTagRecipeBuilder.shapelessTagRecipe(RecipeCategory.MISC, ItemTagsForgeExt.DUSTS_COPPER, 2)
                        .addIngredient(ItemsPM.EARTHSHATTER_HAMMER.get())
                        .addIngredient(CommonTags.Items.ORES_COPPER)
                        .setGroup("earthshatter_hammer_grit")
                        .unlockedBy("has_hammer", has(ItemsPM.EARTHSHATTER_HAMMER.get()))
                        .build(output, ResourceUtils.loc("copper_grit_from_ore")))
                .save(consumer, ResourceUtils.loc("copper_grit_from_ore"));
        ConditionalRecipe.builder()
                .mainCondition(tagsNotEmpty(List.of(ItemTagsForgeExt.DUSTS_COPPER, CommonTags.Items.RAW_MATERIALS_COPPER)))
                .condition(TrueCondition.INSTANCE)
                .recipe(output -> ShapelessTagRecipeBuilder.shapelessTagRecipe(RecipeCategory.MISC, ItemTagsForgeExt.DUSTS_COPPER, 2)
                        .addIngredient(ItemsPM.EARTHSHATTER_HAMMER.get())
                        .addIngredient(CommonTags.Items.RAW_MATERIALS_COPPER)
                        .setGroup("earthshatter_hammer_grit")
                        .unlockedBy("has_hammer", has(ItemsPM.EARTHSHATTER_HAMMER.get()))
                        .build(output, ResourceUtils.loc("copper_grit_from_raw_metal")))
                .save(consumer, ResourceUtils.loc("copper_grit_from_raw_metal"));
        ConditionalRecipe.builder()
                .mainCondition(tagsNotEmpty(List.of(ItemTagsForgeExt.DUSTS_TIN, ItemTagsForgeExt.ORES_TIN)))
                .condition(TrueCondition.INSTANCE)
                .recipe(output -> ShapelessTagRecipeBuilder.shapelessTagRecipe(RecipeCategory.MISC, ItemTagsForgeExt.DUSTS_TIN, 2)
                        .addIngredient(ItemsPM.EARTHSHATTER_HAMMER.get())
                        .addIngredient(ItemTagsForgeExt.ORES_TIN)
                        .setGroup("earthshatter_hammer_grit")
                        .unlockedBy("has_hammer", has(ItemsPM.EARTHSHATTER_HAMMER.get()))
                        .build(output, ResourceUtils.loc("tin_dust_from_ore")))
                .save(consumer, ResourceUtils.loc("tin_dust_from_ore"));
        ConditionalRecipe.builder()
                .mainCondition(tagsNotEmpty(List.of(ItemTagsForgeExt.DUSTS_TIN, ItemTagsForgeExt.RAW_MATERIALS_TIN)))
                .condition(TrueCondition.INSTANCE)
                .recipe(output -> ShapelessTagRecipeBuilder.shapelessTagRecipe(RecipeCategory.MISC, ItemTagsForgeExt.DUSTS_TIN, 2)
                        .addIngredient(ItemsPM.EARTHSHATTER_HAMMER.get())
                        .addIngredient(ItemTagsForgeExt.RAW_MATERIALS_TIN)
                        .setGroup("earthshatter_hammer_grit")
                        .unlockedBy("has_hammer", has(ItemsPM.EARTHSHATTER_HAMMER.get()))
                        .build(output, ResourceUtils.loc("tin_dust_from_raw_metal")))
                .save(consumer, ResourceUtils.loc("tin_dust_from_raw_metal"));
        ConditionalRecipe.builder()
                .mainCondition(tagsNotEmpty(List.of(ItemTagsForgeExt.DUSTS_LEAD, ItemTagsForgeExt.ORES_LEAD)))
                .condition(TrueCondition.INSTANCE)
                .recipe(output -> ShapelessTagRecipeBuilder.shapelessTagRecipe(RecipeCategory.MISC, ItemTagsForgeExt.DUSTS_LEAD, 2)
                        .addIngredient(ItemsPM.EARTHSHATTER_HAMMER.get())
                        .addIngredient(ItemTagsForgeExt.ORES_LEAD)
                        .setGroup("earthshatter_hammer_grit")
                        .unlockedBy("has_hammer", has(ItemsPM.EARTHSHATTER_HAMMER.get()))
                        .build(output, ResourceUtils.loc("lead_dust_from_ore")))
                .save(consumer, ResourceUtils.loc("lead_dust_from_ore"));
        ConditionalRecipe.builder()
                .mainCondition(tagsNotEmpty(List.of(ItemTagsForgeExt.DUSTS_LEAD, ItemTagsForgeExt.RAW_MATERIALS_LEAD)))
                .condition(TrueCondition.INSTANCE)
                .recipe(output -> ShapelessTagRecipeBuilder.shapelessTagRecipe(RecipeCategory.MISC, ItemTagsForgeExt.DUSTS_LEAD, 2)
                        .addIngredient(ItemsPM.EARTHSHATTER_HAMMER.get())
                        .addIngredient(ItemTagsForgeExt.RAW_MATERIALS_LEAD)
                        .setGroup("earthshatter_hammer_grit")
                        .unlockedBy("has_hammer", has(ItemsPM.EARTHSHATTER_HAMMER.get()))
                        .build(output, ResourceUtils.loc("lead_dust_from_raw_metal")))
                .save(consumer, ResourceUtils.loc("lead_dust_from_raw_metal"));
        ConditionalRecipe.builder()
                .mainCondition(tagsNotEmpty(List.of(ItemTagsForgeExt.DUSTS_SILVER, ItemTagsForgeExt.ORES_SILVER)))
                .condition(TrueCondition.INSTANCE)
                .recipe(output -> ShapelessTagRecipeBuilder.shapelessTagRecipe(RecipeCategory.MISC, ItemTagsForgeExt.DUSTS_SILVER, 2)
                        .addIngredient(ItemsPM.EARTHSHATTER_HAMMER.get())
                        .addIngredient(ItemTagsForgeExt.ORES_SILVER)
                        .setGroup("earthshatter_hammer_grit")
                        .unlockedBy("has_hammer", has(ItemsPM.EARTHSHATTER_HAMMER.get()))
                        .build(output, ResourceUtils.loc("silver_dust_from_ore")))
                .save(consumer, ResourceUtils.loc("silver_dust_from_ore"));
        ConditionalRecipe.builder()
                .mainCondition(tagsNotEmpty(List.of(ItemTagsForgeExt.DUSTS_SILVER, ItemTagsForgeExt.RAW_MATERIALS_SILVER)))
                .condition(TrueCondition.INSTANCE)
                .recipe(output -> ShapelessTagRecipeBuilder.shapelessTagRecipe(RecipeCategory.MISC, ItemTagsForgeExt.DUSTS_SILVER, 2)
                        .addIngredient(ItemsPM.EARTHSHATTER_HAMMER.get())
                        .addIngredient(ItemTagsForgeExt.RAW_MATERIALS_SILVER)
                        .setGroup("earthshatter_hammer_grit")
                        .unlockedBy("has_hammer", has(ItemsPM.EARTHSHATTER_HAMMER.get()))
                        .build(output, ResourceUtils.loc("silver_dust_from_raw_metal")))
                .save(consumer, ResourceUtils.loc("silver_dust_from_raw_metal"));
        ConditionalRecipe.builder()
                .mainCondition(tagsNotEmpty(List.of(ItemTagsForgeExt.DUSTS_URANIUM, ItemTagsForgeExt.ORES_URANIUM)))
                .condition(TrueCondition.INSTANCE)
                .recipe(output -> ShapelessTagRecipeBuilder.shapelessTagRecipe(RecipeCategory.MISC, ItemTagsForgeExt.DUSTS_URANIUM, 2)
                        .addIngredient(ItemsPM.EARTHSHATTER_HAMMER.get())
                        .addIngredient(ItemTagsForgeExt.ORES_URANIUM)
                        .setGroup("earthshatter_hammer_grit")
                        .unlockedBy("has_hammer", has(ItemsPM.EARTHSHATTER_HAMMER.get()))
                        .build(output, ResourceUtils.loc("uranium_dust_from_ore")))
                .save(consumer, ResourceUtils.loc("uranium_dust_from_ore"));
        ConditionalRecipe.builder()
                .mainCondition(tagsNotEmpty(List.of(ItemTagsForgeExt.DUSTS_URANIUM, ItemTagsForgeExt.RAW_MATERIALS_URANIUM)))
                .condition(TrueCondition.INSTANCE)
                .recipe(output -> ShapelessTagRecipeBuilder.shapelessTagRecipe(RecipeCategory.MISC, ItemTagsForgeExt.DUSTS_URANIUM, 2)
                        .addIngredient(ItemsPM.EARTHSHATTER_HAMMER.get())
                        .addIngredient(ItemTagsForgeExt.RAW_MATERIALS_URANIUM)
                        .setGroup("earthshatter_hammer_grit")
                        .unlockedBy("has_hammer", has(ItemsPM.EARTHSHATTER_HAMMER.get()))
                        .build(output, ResourceUtils.loc("uranium_dust_from_raw_metal")))
                .save(consumer, ResourceUtils.loc("uranium_dust_from_raw_metal"));
    }

    @Override
    protected Ingredient makeWaterFlaskIngredient() {
        return this.makeWaterIngredientInner(ItemsPM.CONCOCTION.get());
    }

    @Override
    protected Ingredient makeWaterBombIngredient() {
        return this.makeWaterIngredientInner(ItemsPM.ALCHEMICAL_BOMB.get());
    }

    private Ingredient makeWaterIngredientInner(ItemLike baseItem) {
        return PartialComponentIngredient.builder()
                .item(baseItem)
                .data(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER))
                .data(DataComponentsPM.CONCOCTION_TYPE.get(), ConcoctionType.WATER)
                .build();
    }

    @Override
    protected void registerDissolutionChamberConditionalRecipes(RecipeOutput consumer) {
        ConditionalRecipe.builder()
                .mainCondition(tagsNotEmpty(List.of(ItemTagsForgeExt.DUSTS_IRON, CommonTags.Items.ORES_IRON)))
                .condition(TrueCondition.INSTANCE)
                .recipe(output -> DissolutionTagRecipeBuilder.dissolutionTagRecipe(ItemTagsForgeExt.DUSTS_IRON, 3)
                        .ingredient(CommonTags.Items.ORES_IRON)
                        .setGroup("iron_grit_dissolution")
                        .manaCost(SourceList.EMPTY.add(Sources.EARTH, 1))
                        .build(output, ResourceUtils.loc("iron_grit_from_dissolving_ore")))
                .save(consumer, ResourceUtils.loc("iron_grit_from_dissolving_ore"));
        ConditionalRecipe.builder()
                .mainCondition(tagsNotEmpty(List.of(ItemTagsForgeExt.DUSTS_IRON, CommonTags.Items.RAW_MATERIALS_IRON)))
                .condition(TrueCondition.INSTANCE)
                .recipe(output -> DissolutionTagRecipeBuilder.dissolutionTagRecipe(ItemTagsForgeExt.DUSTS_IRON, 3)
                        .ingredient(CommonTags.Items.RAW_MATERIALS_IRON)
                        .setGroup("iron_grit_dissolution")
                        .manaCost(SourceList.EMPTY.add(Sources.EARTH, 1))
                        .build(output, ResourceUtils.loc("iron_grit_from_dissolving_raw_metal")))
                .save(consumer, ResourceUtils.loc("iron_grit_from_dissolving_raw_metal"));
        ConditionalRecipe.builder()
                .mainCondition(tagsNotEmpty(List.of(ItemTagsForgeExt.DUSTS_GOLD, CommonTags.Items.ORES_GOLD)))
                .condition(TrueCondition.INSTANCE)
                .recipe(output -> DissolutionTagRecipeBuilder.dissolutionTagRecipe(ItemTagsForgeExt.DUSTS_GOLD, 3)
                        .ingredient(CommonTags.Items.ORES_GOLD)
                        .setGroup("gold_grit_dissolution")
                        .manaCost(SourceList.EMPTY.add(Sources.EARTH, 1))
                        .build(output, ResourceUtils.loc("gold_grit_from_dissolving_ore")))
                .save(consumer, ResourceUtils.loc("gold_grit_from_dissolving_ore"));
        ConditionalRecipe.builder()
                .mainCondition(tagsNotEmpty(List.of(ItemTagsForgeExt.DUSTS_GOLD, CommonTags.Items.RAW_MATERIALS_GOLD)))
                .condition(TrueCondition.INSTANCE)
                .recipe(output -> DissolutionTagRecipeBuilder.dissolutionTagRecipe(ItemTagsForgeExt.DUSTS_GOLD, 3)
                        .ingredient(CommonTags.Items.RAW_MATERIALS_GOLD)
                        .setGroup("gold_grit_dissolution")
                        .manaCost(SourceList.EMPTY.add(Sources.EARTH, 1))
                        .build(output, ResourceUtils.loc("gold_grit_from_dissolving_raw_metal")))
                .save(consumer, ResourceUtils.loc("gold_grit_from_dissolving_raw_metal"));
        ConditionalRecipe.builder()
                .mainCondition(tagsNotEmpty(List.of(ItemTagsForgeExt.DUSTS_COPPER, CommonTags.Items.ORES_COPPER)))
                .condition(TrueCondition.INSTANCE)
                .recipe(output -> DissolutionTagRecipeBuilder.dissolutionTagRecipe(ItemTagsForgeExt.DUSTS_COPPER, 3)
                        .ingredient(CommonTags.Items.ORES_COPPER)
                        .setGroup("copper_grit_dissolution")
                        .manaCost(SourceList.EMPTY.add(Sources.EARTH, 1))
                        .build(output, ResourceUtils.loc("copper_grit_from_dissolving_ore")))
                .save(consumer, ResourceUtils.loc("copper_grit_from_dissolving_ore"));
        ConditionalRecipe.builder()
                .mainCondition(tagsNotEmpty(List.of(ItemTagsForgeExt.DUSTS_COPPER, CommonTags.Items.RAW_MATERIALS_COPPER)))
                .condition(TrueCondition.INSTANCE)
                .recipe(output -> DissolutionTagRecipeBuilder.dissolutionTagRecipe(ItemTagsForgeExt.DUSTS_COPPER, 3)
                        .ingredient(CommonTags.Items.RAW_MATERIALS_COPPER)
                        .setGroup("copper_grit_dissolution")
                        .manaCost(SourceList.EMPTY.add(Sources.EARTH, 1))
                        .build(output, ResourceUtils.loc("copper_grit_from_dissolving_raw_metal")))
                .save(consumer, ResourceUtils.loc("copper_grit_from_dissolving_raw_metal"));
        ConditionalRecipe.builder()
                .mainCondition(tagsNotEmpty(List.of(ItemTagsForgeExt.DUSTS_TIN, ItemTagsForgeExt.ORES_TIN)))
                .condition(TrueCondition.INSTANCE)
                .recipe(output -> DissolutionTagRecipeBuilder.dissolutionTagRecipe(ItemTagsForgeExt.DUSTS_TIN, 3)
                        .ingredient(ItemTagsForgeExt.ORES_TIN)
                        .setGroup("tin_dust_dissolution")
                        .manaCost(SourceList.EMPTY.add(Sources.EARTH, 1))
                        .build(output, ResourceUtils.loc("tin_dust_from_dissolving_ore")))
                .save(consumer, ResourceUtils.loc("tin_dust_from_dissolving_ore"));
        ConditionalRecipe.builder()
                .mainCondition(tagsNotEmpty(List.of(ItemTagsForgeExt.DUSTS_TIN, ItemTagsForgeExt.RAW_MATERIALS_TIN)))
                .condition(TrueCondition.INSTANCE)
                .recipe(output -> DissolutionTagRecipeBuilder.dissolutionTagRecipe(ItemTagsForgeExt.DUSTS_TIN, 3)
                        .ingredient(ItemTagsForgeExt.RAW_MATERIALS_TIN)
                        .setGroup("tin_dust_dissolution")
                        .manaCost(SourceList.EMPTY.add(Sources.EARTH, 1))
                        .build(output, ResourceUtils.loc("tin_dust_from_dissolving_raw_metal")))
                .save(consumer, ResourceUtils.loc("tin_dust_from_dissolving_raw_metal"));
        ConditionalRecipe.builder()
                .mainCondition(tagsNotEmpty(List.of(ItemTagsForgeExt.DUSTS_LEAD, ItemTagsForgeExt.ORES_LEAD)))
                .condition(TrueCondition.INSTANCE)
                .recipe(output -> DissolutionTagRecipeBuilder.dissolutionTagRecipe(ItemTagsForgeExt.DUSTS_LEAD, 3)
                        .ingredient(ItemTagsForgeExt.ORES_LEAD)
                        .setGroup("lead_dust_dissolution")
                        .manaCost(SourceList.EMPTY.add(Sources.EARTH, 1))
                        .build(output, ResourceUtils.loc("lead_dust_from_dissolving_ore")))
                .save(consumer, ResourceUtils.loc("lead_dust_from_dissolving_ore"));
        ConditionalRecipe.builder()
                .mainCondition(tagsNotEmpty(List.of(ItemTagsForgeExt.DUSTS_LEAD, ItemTagsForgeExt.RAW_MATERIALS_LEAD)))
                .condition(TrueCondition.INSTANCE)
                .recipe(output -> DissolutionTagRecipeBuilder.dissolutionTagRecipe(ItemTagsForgeExt.DUSTS_LEAD, 3)
                        .ingredient(ItemTagsForgeExt.RAW_MATERIALS_LEAD)
                        .setGroup("lead_dust_dissolution")
                        .manaCost(SourceList.EMPTY.add(Sources.EARTH, 1))
                        .build(output, ResourceUtils.loc("lead_dust_from_dissolving_raw_metal")))
                .save(consumer, ResourceUtils.loc("lead_dust_from_dissolving_raw_metal"));
        ConditionalRecipe.builder()
                .mainCondition(tagsNotEmpty(List.of(ItemTagsForgeExt.DUSTS_SILVER, ItemTagsForgeExt.ORES_SILVER)))
                .condition(TrueCondition.INSTANCE)
                .recipe(output -> DissolutionTagRecipeBuilder.dissolutionTagRecipe(ItemTagsForgeExt.DUSTS_SILVER, 3)
                        .ingredient(ItemTagsForgeExt.ORES_SILVER)
                        .setGroup("silver_dust_dissolution")
                        .manaCost(SourceList.EMPTY.add(Sources.EARTH, 1))
                        .build(output, ResourceUtils.loc("silver_dust_from_dissolving_ore")))
                .save(consumer, ResourceUtils.loc("silver_dust_from_dissolving_ore"));
        ConditionalRecipe.builder()
                .mainCondition(tagsNotEmpty(List.of(ItemTagsForgeExt.DUSTS_SILVER, ItemTagsForgeExt.RAW_MATERIALS_SILVER)))
                .condition(TrueCondition.INSTANCE)
                .recipe(output -> DissolutionTagRecipeBuilder.dissolutionTagRecipe(ItemTagsForgeExt.DUSTS_SILVER, 3)
                        .ingredient(ItemTagsForgeExt.RAW_MATERIALS_SILVER)
                        .setGroup("silver_dust_dissolution")
                        .manaCost(SourceList.EMPTY.add(Sources.EARTH, 1))
                        .build(output, ResourceUtils.loc("silver_dust_from_dissolving_raw_metal")))
                .save(consumer, ResourceUtils.loc("silver_dust_from_dissolving_raw_metal"));
        ConditionalRecipe.builder()
                .mainCondition(tagsNotEmpty(List.of(ItemTagsForgeExt.DUSTS_URANIUM, ItemTagsForgeExt.ORES_URANIUM)))
                .condition(TrueCondition.INSTANCE)
                .recipe(output -> DissolutionTagRecipeBuilder.dissolutionTagRecipe(ItemTagsForgeExt.DUSTS_URANIUM, 3)
                        .ingredient(ItemTagsForgeExt.ORES_URANIUM)
                        .setGroup("uranium_dust_dissolution")
                        .manaCost(SourceList.EMPTY.add(Sources.EARTH, 1))
                        .build(output, ResourceUtils.loc("uranium_dust_from_dissolving_ore")))
                .save(consumer, ResourceUtils.loc("uranium_dust_from_dissolving_ore"));
        ConditionalRecipe.builder()
                .mainCondition(tagsNotEmpty(List.of(ItemTagsForgeExt.DUSTS_URANIUM, ItemTagsForgeExt.RAW_MATERIALS_URANIUM)))
                .condition(TrueCondition.INSTANCE)
                .recipe(output -> DissolutionTagRecipeBuilder.dissolutionTagRecipe(ItemTagsForgeExt.DUSTS_URANIUM, 3)
                        .ingredient(ItemTagsForgeExt.RAW_MATERIALS_URANIUM)
                        .setGroup("uranium_dust_dissolution")
                        .manaCost(SourceList.EMPTY.add(Sources.EARTH, 1))
                        .build(output, ResourceUtils.loc("uranium_dust_from_dissolving_raw_metal")))
                .save(consumer, ResourceUtils.loc("uranium_dust_from_dissolving_raw_metal"));
    }
}
