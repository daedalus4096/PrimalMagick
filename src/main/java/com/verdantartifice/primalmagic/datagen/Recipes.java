package com.verdantartifice.primalmagic.datagen;

import java.util.function.Consumer;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blocks.BlocksPM;

import net.minecraft.data.CookingRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.SingleItemRecipeBuilder;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class Recipes extends RecipeProvider {
    public Recipes(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        this.registerMarbleRecipes(consumer);
        this.registerEnchantedMarbleRecipes(consumer);
        this.registerSmokedMarbleRecipes(consumer);
    }

    protected void registerMarbleRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_BRICK_SLAB, 6)
            .patternLine("MMM")
            .key('M', BlocksPM.MARBLE_BRICKS)
            .setGroup("marble")
            .addCriterion("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW))
            .build(consumer);
        SingleItemRecipeBuilder.func_218644_a(Ingredient.fromItems(BlocksPM.MARBLE_RAW), BlocksPM.MARBLE_BRICK_SLAB, 2)
            .func_218643_a("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW))
            .func_218647_a(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_brick_slab_from_marble_raw_stonecutting"));
        SingleItemRecipeBuilder.func_218644_a(Ingredient.fromItems(BlocksPM.MARBLE_BRICKS), BlocksPM.MARBLE_BRICK_SLAB, 2)
            .func_218643_a("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW))
            .func_218647_a(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_brick_slab_from_marble_bricks_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_BRICK_STAIRS, 4)
            .patternLine("M  ")
            .patternLine("MM ")
            .patternLine("MMM")
            .key('M', BlocksPM.MARBLE_BRICKS)
            .setGroup("marble")
            .addCriterion("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW))
            .build(consumer);
        SingleItemRecipeBuilder.func_218648_a(Ingredient.fromItems(BlocksPM.MARBLE_RAW), BlocksPM.MARBLE_BRICK_STAIRS)
            .func_218643_a("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW))
            .func_218647_a(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_brick_stairs_from_marble_raw_stonecutting"));
        SingleItemRecipeBuilder.func_218648_a(Ingredient.fromItems(BlocksPM.MARBLE_BRICKS), BlocksPM.MARBLE_BRICK_STAIRS)
            .func_218643_a("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW))
            .func_218647_a(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_brick_stairs_from_marble_bricks_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_BRICK_WALL, 6)
            .patternLine("MMM")
            .patternLine("MMM")
            .key('M', BlocksPM.MARBLE_BRICKS)
            .setGroup("marble")
            .addCriterion("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW))
            .build(consumer);
        SingleItemRecipeBuilder.func_218648_a(Ingredient.fromItems(BlocksPM.MARBLE_RAW), BlocksPM.MARBLE_BRICK_WALL)
            .func_218643_a("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW))
            .func_218647_a(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_brick_wall_from_marble_raw_stonecutting"));
        SingleItemRecipeBuilder.func_218648_a(Ingredient.fromItems(BlocksPM.MARBLE_BRICKS), BlocksPM.MARBLE_BRICK_WALL)
            .func_218643_a("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW))
            .func_218647_a(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_brick_wall_from_marble_bricks_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_BRICKS, 4)
            .patternLine("MM")
            .patternLine("MM")
            .key('M', BlocksPM.MARBLE_RAW)
            .setGroup("marble")
            .addCriterion("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW))
            .build(consumer);
        SingleItemRecipeBuilder.func_218648_a(Ingredient.fromItems(BlocksPM.MARBLE_RAW), BlocksPM.MARBLE_BRICKS)
            .func_218643_a("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW))
            .func_218647_a(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_bricks_from_marble_raw_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_CHISELED)
            .patternLine("M")
            .patternLine("M")
            .key('M', BlocksPM.MARBLE_SLAB)
            .setGroup("marble")
            .addCriterion("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW))
            .build(consumer);
        SingleItemRecipeBuilder.func_218648_a(Ingredient.fromItems(BlocksPM.MARBLE_RAW), BlocksPM.MARBLE_CHISELED)
            .func_218643_a("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW))
            .func_218647_a(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_chiseled_from_marble_raw_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_PILLAR, 2)
            .patternLine("M")
            .patternLine("M")
            .key('M', BlocksPM.MARBLE_RAW)
            .setGroup("marble")
            .addCriterion("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW))
            .build(consumer);
        SingleItemRecipeBuilder.func_218648_a(Ingredient.fromItems(BlocksPM.MARBLE_RAW), BlocksPM.MARBLE_PILLAR)
            .func_218643_a("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW))
            .func_218647_a(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_pillar_from_marble_raw_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_RUNED, 5)
            .patternLine(" M ")
            .patternLine("MCM")
            .patternLine(" M ")
            .key('M', BlocksPM.MARBLE_RAW)
            .key('C', BlocksPM.MARBLE_CHISELED)
            .setGroup("marble")
            .addCriterion("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW))
            .build(consumer);
        SingleItemRecipeBuilder.func_218648_a(Ingredient.fromItems(BlocksPM.MARBLE_RAW), BlocksPM.MARBLE_RUNED)
            .func_218643_a("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW))
            .func_218647_a(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_runed_from_marble_raw_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_SLAB, 6)
            .patternLine("MMM")
            .key('M', BlocksPM.MARBLE_RAW)
            .setGroup("marble")
            .addCriterion("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW))
            .build(consumer);
        SingleItemRecipeBuilder.func_218644_a(Ingredient.fromItems(BlocksPM.MARBLE_RAW), BlocksPM.MARBLE_SLAB, 2)
            .func_218643_a("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW))
            .func_218647_a(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_slab_from_marble_raw_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_STAIRS, 4)
            .patternLine("M  ")
            .patternLine("MM ")
            .patternLine("MMM")
            .key('M', BlocksPM.MARBLE_RAW)
            .setGroup("marble")
            .addCriterion("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW))
            .build(consumer);
        SingleItemRecipeBuilder.func_218648_a(Ingredient.fromItems(BlocksPM.MARBLE_RAW), BlocksPM.MARBLE_STAIRS)
            .func_218643_a("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW))
            .func_218647_a(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_stairs_from_marble_raw_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_WALL, 6)
            .patternLine("MMM")
            .patternLine("MMM")
            .key('M', BlocksPM.MARBLE_RAW)
            .setGroup("marble")
            .addCriterion("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW))
            .build(consumer);
        SingleItemRecipeBuilder.func_218648_a(Ingredient.fromItems(BlocksPM.MARBLE_RAW), BlocksPM.MARBLE_WALL)
            .func_218643_a("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW))
            .func_218647_a(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_wall_from_marble_raw_stonecutting"));
    }

    protected void registerEnchantedMarbleRecipes(Consumer<IFinishedRecipe> consumer) {
        // TODO Add base enchanted marble recipe
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_ENCHANTED_BRICK_SLAB, 6)
            .patternLine("MMM")
            .key('M', BlocksPM.MARBLE_ENCHANTED)
            .setGroup("marble_enchanted")
            .addCriterion("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED))
            .build(consumer);
        SingleItemRecipeBuilder.func_218644_a(Ingredient.fromItems(BlocksPM.MARBLE_ENCHANTED), BlocksPM.MARBLE_ENCHANTED_BRICK_SLAB, 2)
            .func_218643_a("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED))
            .func_218647_a(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_enchanted_brick_slab_from_marble_enchanted_stonecutting"));
        SingleItemRecipeBuilder.func_218644_a(Ingredient.fromItems(BlocksPM.MARBLE_ENCHANTED_BRICKS), BlocksPM.MARBLE_ENCHANTED_BRICK_SLAB, 2)
            .func_218643_a("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED))
            .func_218647_a(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_enchanted_brick_slab_from_marble_enchanted_bricks_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_ENCHANTED_BRICK_STAIRS, 4)
            .patternLine("M  ")
            .patternLine("MM ")
            .patternLine("MMM")
            .key('M', BlocksPM.MARBLE_ENCHANTED_BRICKS)
            .setGroup("marble_enchanted")
            .addCriterion("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED))
            .build(consumer);
        SingleItemRecipeBuilder.func_218648_a(Ingredient.fromItems(BlocksPM.MARBLE_ENCHANTED), BlocksPM.MARBLE_ENCHANTED_BRICK_STAIRS)
            .func_218643_a("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED))
            .func_218647_a(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_enchanted_brick_stairs_from_marble_enchanted_stonecutting"));
        SingleItemRecipeBuilder.func_218648_a(Ingredient.fromItems(BlocksPM.MARBLE_ENCHANTED_BRICKS), BlocksPM.MARBLE_ENCHANTED_BRICK_STAIRS)
            .func_218643_a("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED))
            .func_218647_a(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_enchanted_brick_stairs_from_marble_enchanted_bricks_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_ENCHANTED_BRICK_WALL, 6)
            .patternLine("MMM")
            .patternLine("MMM")
            .key('M', BlocksPM.MARBLE_ENCHANTED_BRICKS)
            .setGroup("marble_enchanted")
            .addCriterion("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED))
            .build(consumer);
        SingleItemRecipeBuilder.func_218648_a(Ingredient.fromItems(BlocksPM.MARBLE_ENCHANTED), BlocksPM.MARBLE_ENCHANTED_BRICK_WALL)
            .func_218643_a("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED))
            .func_218647_a(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_enchanted_brick_wall_from_marble_enchanted_stonecutting"));
        SingleItemRecipeBuilder.func_218648_a(Ingredient.fromItems(BlocksPM.MARBLE_ENCHANTED_BRICKS), BlocksPM.MARBLE_ENCHANTED_BRICK_WALL)
            .func_218643_a("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED))
            .func_218647_a(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_enchanted_brick_wall_from_marble_enchanted_bricks_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_ENCHANTED_BRICKS, 4)
            .patternLine("MM")
            .patternLine("MM")
            .key('M', BlocksPM.MARBLE_ENCHANTED)
            .setGroup("marble_enchanted")
            .addCriterion("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED))
            .build(consumer);
        SingleItemRecipeBuilder.func_218648_a(Ingredient.fromItems(BlocksPM.MARBLE_ENCHANTED), BlocksPM.MARBLE_ENCHANTED_BRICKS)
            .func_218643_a("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED))
            .func_218647_a(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_enchanted_bricks_from_marble_enchanted_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_ENCHANTED_CHISELED)
            .patternLine("M")
            .patternLine("M")
            .key('M', BlocksPM.MARBLE_ENCHANTED_SLAB)
            .setGroup("marble_enchanted")
            .addCriterion("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED))
            .build(consumer);
        SingleItemRecipeBuilder.func_218648_a(Ingredient.fromItems(BlocksPM.MARBLE_ENCHANTED), BlocksPM.MARBLE_ENCHANTED_CHISELED)
            .func_218643_a("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED))
            .func_218647_a(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_enchanted_chiseled_from_marble_enchanted_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_ENCHANTED_PILLAR, 2)
            .patternLine("M")
            .patternLine("M")
            .key('M', BlocksPM.MARBLE_ENCHANTED)
            .setGroup("marble_enchanted")
            .addCriterion("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED))
            .build(consumer);
        SingleItemRecipeBuilder.func_218648_a(Ingredient.fromItems(BlocksPM.MARBLE_ENCHANTED), BlocksPM.MARBLE_ENCHANTED_PILLAR)
            .func_218643_a("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED))
            .func_218647_a(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_enchanted_pillar_from_marble_enchanted_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_ENCHANTED_RUNED, 5)
            .patternLine(" M ")
            .patternLine("MCM")
            .patternLine(" M ")
            .key('M', BlocksPM.MARBLE_ENCHANTED)
            .key('C', BlocksPM.MARBLE_ENCHANTED_CHISELED)
            .setGroup("marble_enchanted")
            .addCriterion("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED))
            .build(consumer);
        SingleItemRecipeBuilder.func_218648_a(Ingredient.fromItems(BlocksPM.MARBLE_ENCHANTED), BlocksPM.MARBLE_ENCHANTED_RUNED)
            .func_218643_a("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED))
            .func_218647_a(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_enchanted_runed_from_marble_enchanted_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_ENCHANTED_SLAB, 6)
            .patternLine("MMM")
            .key('M', BlocksPM.MARBLE_ENCHANTED)
            .setGroup("marble_enchanted")
            .addCriterion("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED))
            .build(consumer);
        SingleItemRecipeBuilder.func_218644_a(Ingredient.fromItems(BlocksPM.MARBLE_ENCHANTED), BlocksPM.MARBLE_ENCHANTED_SLAB, 2)
            .func_218643_a("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED))
            .func_218647_a(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_enchanted_slab_from_marble_enchanted_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_ENCHANTED_STAIRS, 4)
            .patternLine("M  ")
            .patternLine("MM ")
            .patternLine("MMM")
            .key('M', BlocksPM.MARBLE_ENCHANTED)
            .setGroup("marble_enchanted")
            .addCriterion("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED))
            .build(consumer);
        SingleItemRecipeBuilder.func_218648_a(Ingredient.fromItems(BlocksPM.MARBLE_ENCHANTED), BlocksPM.MARBLE_ENCHANTED_STAIRS)
            .func_218643_a("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED))
            .func_218647_a(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_enchanted_stairs_from_marble_enchanted_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_ENCHANTED_WALL, 6)
            .patternLine("MMM")
            .patternLine("MMM")
            .key('M', BlocksPM.MARBLE_ENCHANTED)
            .setGroup("marble_enchanted")
            .addCriterion("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED))
            .build(consumer);
        SingleItemRecipeBuilder.func_218648_a(Ingredient.fromItems(BlocksPM.MARBLE_ENCHANTED), BlocksPM.MARBLE_ENCHANTED_WALL)
            .func_218643_a("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED))
            .func_218647_a(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_enchanted_wall_from_marble_enchanted_stonecutting"));
    }
    
    protected void registerSmokedMarbleRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_SMOKED, 8)
            .patternLine("MMM")
            .patternLine("MCM")
            .patternLine("MMM")
            .key('M', BlocksPM.MARBLE_RAW)
            .key('C', ItemTags.COALS)
            .setGroup("marble_smoked")
            .addCriterion("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW))
            .build(consumer);
        CookingRecipeBuilder.cookingRecipe(Ingredient.fromItems(BlocksPM.MARBLE_RAW), BlocksPM.MARBLE_SMOKED, 0, 100, IRecipeSerializer.SMOKING)
            .addCriterion("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_smoked_from_smoking"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_SMOKED_BRICK_SLAB, 6)
            .patternLine("MMM")
            .key('M', BlocksPM.MARBLE_SMOKED_BRICKS)
            .setGroup("marble_smoked")
            .addCriterion("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED))
            .build(consumer);
        SingleItemRecipeBuilder.func_218644_a(Ingredient.fromItems(BlocksPM.MARBLE_SMOKED), BlocksPM.MARBLE_SMOKED_BRICK_SLAB, 2)
            .func_218643_a("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED))
            .func_218647_a(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_smoked_brick_slab_from_marble_smoked_stonecutting"));
        SingleItemRecipeBuilder.func_218644_a(Ingredient.fromItems(BlocksPM.MARBLE_SMOKED_BRICKS), BlocksPM.MARBLE_SMOKED_BRICK_SLAB, 2)
            .func_218643_a("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED))
            .func_218647_a(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_smoked_brick_slab_from_marble_smoked_bricks_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_SMOKED_BRICK_STAIRS, 4)
            .patternLine("M  ")
            .patternLine("MM ")
            .patternLine("MMM")
            .key('M', BlocksPM.MARBLE_SMOKED_BRICKS)
            .setGroup("marble_smoked")
            .addCriterion("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED))
            .build(consumer);
        SingleItemRecipeBuilder.func_218648_a(Ingredient.fromItems(BlocksPM.MARBLE_SMOKED), BlocksPM.MARBLE_SMOKED_BRICK_STAIRS)
            .func_218643_a("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED))
            .func_218647_a(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_smoked_brick_stairs_from_marble_smoked_stonecutting"));
        SingleItemRecipeBuilder.func_218648_a(Ingredient.fromItems(BlocksPM.MARBLE_SMOKED_BRICKS), BlocksPM.MARBLE_SMOKED_BRICK_STAIRS)
            .func_218643_a("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED))
            .func_218647_a(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_smoked_brick_stairs_from_marble_smoked_bricks_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_SMOKED_BRICK_WALL, 6)
            .patternLine("MMM")
            .patternLine("MMM")
            .key('M', BlocksPM.MARBLE_SMOKED_BRICKS)
            .setGroup("marble_smoked")
            .addCriterion("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED))
            .build(consumer);
        SingleItemRecipeBuilder.func_218648_a(Ingredient.fromItems(BlocksPM.MARBLE_SMOKED), BlocksPM.MARBLE_SMOKED_BRICK_WALL)
            .func_218643_a("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED))
            .func_218647_a(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_smoked_brick_wall_from_marble_smoked_stonecutting"));
        SingleItemRecipeBuilder.func_218648_a(Ingredient.fromItems(BlocksPM.MARBLE_SMOKED_BRICKS), BlocksPM.MARBLE_SMOKED_BRICK_WALL)
            .func_218643_a("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED))
            .func_218647_a(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_smoked_brick_wall_from_marble_smoked_bricks_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_SMOKED_BRICKS, 4)
            .patternLine("MM")
            .patternLine("MM")
            .key('M', BlocksPM.MARBLE_SMOKED)
            .setGroup("marble_smoked")
            .addCriterion("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED))
            .build(consumer);
        SingleItemRecipeBuilder.func_218648_a(Ingredient.fromItems(BlocksPM.MARBLE_SMOKED), BlocksPM.MARBLE_SMOKED_BRICKS)
            .func_218643_a("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED))
            .func_218647_a(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_smoked_bricks_from_marble_smoked_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_SMOKED_CHISELED)
            .patternLine("M")
            .patternLine("M")
            .key('M', BlocksPM.MARBLE_SMOKED_SLAB)
            .setGroup("marble_smoked")
            .addCriterion("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED))
            .build(consumer);
        SingleItemRecipeBuilder.func_218648_a(Ingredient.fromItems(BlocksPM.MARBLE_SMOKED), BlocksPM.MARBLE_SMOKED_CHISELED)
            .func_218643_a("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED))
            .func_218647_a(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_smoked_chiseled_from_marble_smoked_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_SMOKED_PILLAR, 2)
            .patternLine("M")
            .patternLine("M")
            .key('M', BlocksPM.MARBLE_SMOKED)
            .setGroup("marble_smoked")
            .addCriterion("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED))
            .build(consumer);
        SingleItemRecipeBuilder.func_218648_a(Ingredient.fromItems(BlocksPM.MARBLE_SMOKED), BlocksPM.MARBLE_SMOKED_PILLAR)
            .func_218643_a("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED))
            .func_218647_a(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_smoked_pillar_from_marble_smoked_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_SMOKED_RUNED, 5)
            .patternLine(" M ")
            .patternLine("MCM")
            .patternLine(" M ")
            .key('M', BlocksPM.MARBLE_SMOKED)
            .key('C', BlocksPM.MARBLE_SMOKED_CHISELED)
            .setGroup("marble_smoked")
            .addCriterion("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED))
            .build(consumer);
        SingleItemRecipeBuilder.func_218648_a(Ingredient.fromItems(BlocksPM.MARBLE_SMOKED), BlocksPM.MARBLE_SMOKED_RUNED)
            .func_218643_a("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED))
            .func_218647_a(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_smoked_runed_from_marble_smoked_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_SMOKED_SLAB, 6)
            .patternLine("MMM")
            .key('M', BlocksPM.MARBLE_SMOKED)
            .setGroup("marble_smoked")
            .addCriterion("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED))
            .build(consumer);
        SingleItemRecipeBuilder.func_218644_a(Ingredient.fromItems(BlocksPM.MARBLE_SMOKED), BlocksPM.MARBLE_SMOKED_SLAB, 2)
            .func_218643_a("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED))
            .func_218647_a(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_smoked_slab_from_marble_smoked_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_SMOKED_STAIRS, 4)
            .patternLine("M  ")
            .patternLine("MM ")
            .patternLine("MMM")
            .key('M', BlocksPM.MARBLE_SMOKED)
            .setGroup("marble_smoked")
            .addCriterion("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED))
            .build(consumer);
        SingleItemRecipeBuilder.func_218648_a(Ingredient.fromItems(BlocksPM.MARBLE_SMOKED), BlocksPM.MARBLE_SMOKED_STAIRS)
            .func_218643_a("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED))
            .func_218647_a(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_smoked_stairs_from_marble_smoked_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_SMOKED_WALL, 6)
            .patternLine("MMM")
            .patternLine("MMM")
            .key('M', BlocksPM.MARBLE_SMOKED)
            .setGroup("marble_smoked")
            .addCriterion("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED))
            .build(consumer);
        SingleItemRecipeBuilder.func_218648_a(Ingredient.fromItems(BlocksPM.MARBLE_SMOKED), BlocksPM.MARBLE_SMOKED_WALL)
            .func_218643_a("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED))
            .func_218647_a(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_smoked_wall_from_marble_smoked_stonecutting"));
    }
}
