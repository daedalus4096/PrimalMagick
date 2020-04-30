package com.verdantartifice.primalmagic.datagen;

import java.util.function.Consumer;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.crafting.RecipeSerializersPM;
import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.items.essence.EssenceItem;
import com.verdantartifice.primalmagic.common.items.essence.EssenceType;
import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;
import com.verdantartifice.primalmagic.common.tags.ItemTagsPM;

import net.minecraft.data.CookingRecipeBuilder;
import net.minecraft.data.CustomRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.data.SingleItemRecipeBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

/**
 * Data provider for all of the mod's recipes.
 * 
 * @author Daedalus4096
 */
public class Recipes extends RecipeProvider {
    public Recipes(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        this.registerMarbleRecipes(consumer);
        this.registerEnchantedMarbleRecipes(consumer);
        this.registerSmokedMarbleRecipes(consumer);
        this.registerSunwoodRecipes(consumer);
        this.registerMoonwoodRecipes(consumer);
        this.registerEssenceUpgradeRecipes(consumer);
        this.registerEssenceDowngradeRecipes(consumer);
        this.registerSaltRecipes(consumer);
        this.registerSkyglassRecipes(consumer);
        this.registerSkyglassPaneRecipes(consumer);
        this.registerEarthshatterHammerRecipes(consumer);
        this.registerMineralRecipes(consumer);
        
        ShapelessRecipeBuilder.shapelessRecipe(ItemsPM.MUNDANE_WAND.get())
            .addIngredient(Tags.Items.RODS_WOODEN)
            .addIngredient(ItemTagsPM.ESSENCES_TERRESTRIAL_DUSTS)
            .addCriterion("has_terrestrial_dust", this.hasItem(ItemTagsPM.ESSENCES_TERRESTRIAL_DUSTS))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(BlocksPM.ANALYSIS_TABLE.get())
            .addIngredient(BlocksPM.WOOD_TABLE.get())
            .addIngredient(ItemsPM.MAGNIFYING_GLASS.get())
            .addIngredient(Items.PAPER)
            .addCriterion("has_wood_table", this.hasItem(BlocksPM.WOOD_TABLE.get()))
            .addCriterion("has_magnifying_glass", this.hasItem(ItemsPM.MAGNIFYING_GLASS.get()))
            .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ItemsPM.MAGNIFYING_GLASS.get())
            .patternLine(" I ")
            .patternLine("IGI")
            .patternLine("SI ")
            .key('I', Tags.Items.INGOTS_IRON)
            .key('G', Tags.Items.GLASS_PANES_COLORLESS)
            .key('S', Tags.Items.RODS_WOODEN)
            .addCriterion("has_iron_ingot", this.hasItem(Tags.Items.INGOTS_IRON))
            .addCriterion("has_glass_pane", this.hasItem(Tags.Items.GLASS_PANES_COLORLESS))
            .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.WOOD_TABLE.get())
            .patternLine("SSS")
            .patternLine(" P ")
            .patternLine("SPS")
            .key('S', ItemTags.WOODEN_SLABS)
            .key('P', ItemTags.PLANKS)
            .addCriterion("has_planks", this.hasItem(ItemTags.PLANKS))
            .build(consumer);
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.SPELL_SCROLL_BLANK.get())
            .patternLine("  S")
            .patternLine("PPP")
            .patternLine("S  ")
            .key('S', Tags.Items.RODS_WOODEN)
            .key('P', Items.PAPER)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("BASIC_SORCERY")))
            .build(consumer);
        ArcaneShapelessRecipeBuilder.arcaneShapelessRecipe(BlocksPM.RESEARCH_TABLE.get())
            .addIngredient(BlocksPM.WOOD_TABLE.get())
            .addIngredient(Items.BOOK)
            .addIngredient(Items.PAPER)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("THEORYCRAFTING")))
            .build(consumer);
        ArcaneShapelessRecipeBuilder.arcaneShapelessRecipe(ItemsPM.ENCHANTED_INK.get())
            .addIngredient(Items.GLASS_BOTTLE)
            .addIngredient(Tags.Items.DYES_BLACK)
            .addIngredient(ItemTagsPM.ESSENCES_TERRESTRIAL_DUSTS)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("THEORYCRAFTING")))
            .build(consumer);
        ArcaneShapelessRecipeBuilder.arcaneShapelessRecipe(ItemsPM.ENCHANTED_INK_AND_QUILL.get())
            .addIngredient(ItemsPM.ENCHANTED_INK.get())
            .addIngredient(Tags.Items.FEATHERS)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("THEORYCRAFTING")))
            .build(consumer);
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(Items.STONE, 8)
            .patternLine("SSS")
            .patternLine("SDS")
            .patternLine("SSS")
            .key('S', Items.COBBLESTONE)
            .key('D', ItemsPM.ESSENCE_DUST_EARTH.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("STONEMELDING")))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "stone_from_stonemelding"));

        CustomRecipeBuilder.func_218656_a(RecipeSerializersPM.WAND_ASSEMBLY_SPECIAL.get())
            .build(consumer, "primalmagic:wand_assembly");
        CustomRecipeBuilder.func_218656_a(RecipeSerializersPM.WAND_INSCRIPTION_SPECIAL.get())
            .build(consumer, "primalmagic:wand_inscription");
        CustomRecipeBuilder.func_218656_a(RecipeSerializersPM.SPELLCRAFTING_SPECIAL.get())
            .build(consumer, "primalmagic:spellcrafting");
    }

    protected void registerMarbleRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_BRICK_SLAB.get(), 6)
            .patternLine("MMM")
            .key('M', BlocksPM.MARBLE_BRICKS.get())
            .setGroup("marble")
            .addCriterion("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW.get()))
            .build(consumer);
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(BlocksPM.MARBLE_RAW.get()), BlocksPM.MARBLE_BRICK_SLAB.get(), 2)
            .addCriterion("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_brick_slab_from_marble_raw_stonecutting"));
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(BlocksPM.MARBLE_BRICKS.get()), BlocksPM.MARBLE_BRICK_SLAB.get(), 2)
            .addCriterion("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_brick_slab_from_marble_bricks_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_BRICK_STAIRS.get(), 4)
            .patternLine("M  ")
            .patternLine("MM ")
            .patternLine("MMM")
            .key('M', BlocksPM.MARBLE_BRICKS.get())
            .setGroup("marble")
            .addCriterion("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW.get()))
            .build(consumer);
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(BlocksPM.MARBLE_RAW.get()), BlocksPM.MARBLE_BRICK_STAIRS.get())
            .addCriterion("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_brick_stairs_from_marble_raw_stonecutting"));
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(BlocksPM.MARBLE_BRICKS.get()), BlocksPM.MARBLE_BRICK_STAIRS.get())
            .addCriterion("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_brick_stairs_from_marble_bricks_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_BRICK_WALL.get(), 6)
            .patternLine("MMM")
            .patternLine("MMM")
            .key('M', BlocksPM.MARBLE_BRICKS.get())
            .setGroup("marble")
            .addCriterion("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW.get()))
            .build(consumer);
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(BlocksPM.MARBLE_RAW.get()), BlocksPM.MARBLE_BRICK_WALL.get())
            .addCriterion("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_brick_wall_from_marble_raw_stonecutting"));
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(BlocksPM.MARBLE_BRICKS.get()), BlocksPM.MARBLE_BRICK_WALL.get())
            .addCriterion("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_brick_wall_from_marble_bricks_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_BRICKS.get(), 4)
            .patternLine("MM")
            .patternLine("MM")
            .key('M', BlocksPM.MARBLE_RAW.get())
            .setGroup("marble")
            .addCriterion("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW.get()))
            .build(consumer);
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(BlocksPM.MARBLE_RAW.get()), BlocksPM.MARBLE_BRICKS.get())
            .addCriterion("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_bricks_from_marble_raw_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_CHISELED.get())
            .patternLine("M")
            .patternLine("M")
            .key('M', BlocksPM.MARBLE_SLAB.get())
            .setGroup("marble")
            .addCriterion("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW.get()))
            .build(consumer);
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(BlocksPM.MARBLE_RAW.get()), BlocksPM.MARBLE_CHISELED.get())
            .addCriterion("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_chiseled_from_marble_raw_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_PILLAR.get(), 2)
            .patternLine("M")
            .patternLine("M")
            .key('M', BlocksPM.MARBLE_RAW.get())
            .setGroup("marble")
            .addCriterion("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW.get()))
            .build(consumer);
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(BlocksPM.MARBLE_RAW.get()), BlocksPM.MARBLE_PILLAR.get())
            .addCriterion("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_pillar_from_marble_raw_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_RUNED.get(), 5)
            .patternLine(" M ")
            .patternLine("MCM")
            .patternLine(" M ")
            .key('M', BlocksPM.MARBLE_RAW.get())
            .key('C', BlocksPM.MARBLE_CHISELED.get())
            .setGroup("marble")
            .addCriterion("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW.get()))
            .build(consumer);
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(BlocksPM.MARBLE_RAW.get()), BlocksPM.MARBLE_RUNED.get())
            .addCriterion("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_runed_from_marble_raw_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_SLAB.get(), 6)
            .patternLine("MMM")
            .key('M', BlocksPM.MARBLE_RAW.get())
            .setGroup("marble")
            .addCriterion("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW.get()))
            .build(consumer);
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(BlocksPM.MARBLE_RAW.get()), BlocksPM.MARBLE_SLAB.get(), 2)
            .addCriterion("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_slab_from_marble_raw_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_STAIRS.get(), 4)
            .patternLine("M  ")
            .patternLine("MM ")
            .patternLine("MMM")
            .key('M', BlocksPM.MARBLE_RAW.get())
            .setGroup("marble")
            .addCriterion("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW.get()))
            .build(consumer);
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(BlocksPM.MARBLE_RAW.get()), BlocksPM.MARBLE_STAIRS.get())
            .addCriterion("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_stairs_from_marble_raw_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_WALL.get(), 6)
            .patternLine("MMM")
            .patternLine("MMM")
            .key('M', BlocksPM.MARBLE_RAW.get())
            .setGroup("marble")
            .addCriterion("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW.get()))
            .build(consumer);
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(BlocksPM.MARBLE_RAW.get()), BlocksPM.MARBLE_WALL.get())
            .addCriterion("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_wall_from_marble_raw_stonecutting"));
    }

    protected void registerEnchantedMarbleRecipes(Consumer<IFinishedRecipe> consumer) {
        ArcaneShapelessRecipeBuilder.arcaneShapelessRecipe(BlocksPM.MARBLE_ENCHANTED.get(), 9)
            .addIngredient(BlocksPM.MARBLE_RAW.get(), 9)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("FIRST_STEPS")))
            .manaCost(new SourceList().add(Source.EARTH, 1).add(Source.SEA, 1).add(Source.SKY, 1).add(Source.SUN, 1).add(Source.MOON, 1))
            .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_ENCHANTED_BRICK_SLAB.get(), 6)
            .patternLine("MMM")
            .key('M', BlocksPM.MARBLE_ENCHANTED.get())
            .setGroup("marble_enchanted")
            .addCriterion("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED.get()))
            .build(consumer);
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(BlocksPM.MARBLE_ENCHANTED.get()), BlocksPM.MARBLE_ENCHANTED_BRICK_SLAB.get(), 2)
            .addCriterion("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_enchanted_brick_slab_from_marble_enchanted_stonecutting"));
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(BlocksPM.MARBLE_ENCHANTED_BRICKS.get()), BlocksPM.MARBLE_ENCHANTED_BRICK_SLAB.get(), 2)
            .addCriterion("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_enchanted_brick_slab_from_marble_enchanted_bricks_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_ENCHANTED_BRICK_STAIRS.get(), 4)
            .patternLine("M  ")
            .patternLine("MM ")
            .patternLine("MMM")
            .key('M', BlocksPM.MARBLE_ENCHANTED_BRICKS.get())
            .setGroup("marble_enchanted")
            .addCriterion("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED.get()))
            .build(consumer);
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(BlocksPM.MARBLE_ENCHANTED.get()), BlocksPM.MARBLE_ENCHANTED_BRICK_STAIRS.get())
            .addCriterion("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_enchanted_brick_stairs_from_marble_enchanted_stonecutting"));
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(BlocksPM.MARBLE_ENCHANTED_BRICKS.get()), BlocksPM.MARBLE_ENCHANTED_BRICK_STAIRS.get())
            .addCriterion("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_enchanted_brick_stairs_from_marble_enchanted_bricks_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_ENCHANTED_BRICK_WALL.get(), 6)
            .patternLine("MMM")
            .patternLine("MMM")
            .key('M', BlocksPM.MARBLE_ENCHANTED_BRICKS.get())
            .setGroup("marble_enchanted")
            .addCriterion("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED.get()))
            .build(consumer);
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(BlocksPM.MARBLE_ENCHANTED.get()), BlocksPM.MARBLE_ENCHANTED_BRICK_WALL.get())
            .addCriterion("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_enchanted_brick_wall_from_marble_enchanted_stonecutting"));
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(BlocksPM.MARBLE_ENCHANTED_BRICKS.get()), BlocksPM.MARBLE_ENCHANTED_BRICK_WALL.get())
            .addCriterion("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_enchanted_brick_wall_from_marble_enchanted_bricks_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_ENCHANTED_BRICKS.get(), 4)
            .patternLine("MM")
            .patternLine("MM")
            .key('M', BlocksPM.MARBLE_ENCHANTED.get())
            .setGroup("marble_enchanted")
            .addCriterion("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED.get()))
            .build(consumer);
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(BlocksPM.MARBLE_ENCHANTED.get()), BlocksPM.MARBLE_ENCHANTED_BRICKS.get())
            .addCriterion("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_enchanted_bricks_from_marble_enchanted_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_ENCHANTED_CHISELED.get())
            .patternLine("M")
            .patternLine("M")
            .key('M', BlocksPM.MARBLE_ENCHANTED_SLAB.get())
            .setGroup("marble_enchanted")
            .addCriterion("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED.get()))
            .build(consumer);
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(BlocksPM.MARBLE_ENCHANTED.get()), BlocksPM.MARBLE_ENCHANTED_CHISELED.get())
            .addCriterion("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_enchanted_chiseled_from_marble_enchanted_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_ENCHANTED_PILLAR.get(), 2)
            .patternLine("M")
            .patternLine("M")
            .key('M', BlocksPM.MARBLE_ENCHANTED.get())
            .setGroup("marble_enchanted")
            .addCriterion("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED.get()))
            .build(consumer);
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(BlocksPM.MARBLE_ENCHANTED.get()), BlocksPM.MARBLE_ENCHANTED_PILLAR.get())
            .addCriterion("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_enchanted_pillar_from_marble_enchanted_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_ENCHANTED_RUNED.get(), 5)
            .patternLine(" M ")
            .patternLine("MCM")
            .patternLine(" M ")
            .key('M', BlocksPM.MARBLE_ENCHANTED.get())
            .key('C', BlocksPM.MARBLE_ENCHANTED_CHISELED.get())
            .setGroup("marble_enchanted")
            .addCriterion("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED.get()))
            .build(consumer);
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(BlocksPM.MARBLE_ENCHANTED.get()), BlocksPM.MARBLE_ENCHANTED_RUNED.get())
            .addCriterion("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_enchanted_runed_from_marble_enchanted_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_ENCHANTED_SLAB.get(), 6)
            .patternLine("MMM")
            .key('M', BlocksPM.MARBLE_ENCHANTED.get())
            .setGroup("marble_enchanted")
            .addCriterion("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED.get()))
            .build(consumer);
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(BlocksPM.MARBLE_ENCHANTED.get()), BlocksPM.MARBLE_ENCHANTED_SLAB.get(), 2)
            .addCriterion("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_enchanted_slab_from_marble_enchanted_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_ENCHANTED_STAIRS.get(), 4)
            .patternLine("M  ")
            .patternLine("MM ")
            .patternLine("MMM")
            .key('M', BlocksPM.MARBLE_ENCHANTED.get())
            .setGroup("marble_enchanted")
            .addCriterion("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED.get()))
            .build(consumer);
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(BlocksPM.MARBLE_ENCHANTED.get()), BlocksPM.MARBLE_ENCHANTED_STAIRS.get())
            .addCriterion("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_enchanted_stairs_from_marble_enchanted_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_ENCHANTED_WALL.get(), 6)
            .patternLine("MMM")
            .patternLine("MMM")
            .key('M', BlocksPM.MARBLE_ENCHANTED.get())
            .setGroup("marble_enchanted")
            .addCriterion("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED.get()))
            .build(consumer);
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(BlocksPM.MARBLE_ENCHANTED.get()), BlocksPM.MARBLE_ENCHANTED_WALL.get())
            .addCriterion("has_marble_enchanted", this.hasItem(BlocksPM.MARBLE_ENCHANTED.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_enchanted_wall_from_marble_enchanted_stonecutting"));
    }
    
    protected void registerSmokedMarbleRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_SMOKED.get(), 8)
            .patternLine("MMM")
            .patternLine("MCM")
            .patternLine("MMM")
            .key('M', BlocksPM.MARBLE_RAW.get())
            .key('C', ItemTags.COALS)
            .setGroup("marble_smoked")
            .addCriterion("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW.get()))
            .build(consumer);
        CookingRecipeBuilder.cookingRecipe(Ingredient.fromItems(BlocksPM.MARBLE_RAW.get()), BlocksPM.MARBLE_SMOKED.get(), 0, 100, IRecipeSerializer.SMOKING)
            .addCriterion("has_marble_raw", this.hasItem(BlocksPM.MARBLE_RAW.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_smoked_from_smoking"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_SMOKED_BRICK_SLAB.get(), 6)
            .patternLine("MMM")
            .key('M', BlocksPM.MARBLE_SMOKED_BRICKS.get())
            .setGroup("marble_smoked")
            .addCriterion("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED.get()))
            .build(consumer);
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(BlocksPM.MARBLE_SMOKED.get()), BlocksPM.MARBLE_SMOKED_BRICK_SLAB.get(), 2)
            .addCriterion("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_smoked_brick_slab_from_marble_smoked_stonecutting"));
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(BlocksPM.MARBLE_SMOKED_BRICKS.get()), BlocksPM.MARBLE_SMOKED_BRICK_SLAB.get(), 2)
            .addCriterion("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_smoked_brick_slab_from_marble_smoked_bricks_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_SMOKED_BRICK_STAIRS.get(), 4)
            .patternLine("M  ")
            .patternLine("MM ")
            .patternLine("MMM")
            .key('M', BlocksPM.MARBLE_SMOKED_BRICKS.get())
            .setGroup("marble_smoked")
            .addCriterion("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED.get()))
            .build(consumer);
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(BlocksPM.MARBLE_SMOKED.get()), BlocksPM.MARBLE_SMOKED_BRICK_STAIRS.get())
            .addCriterion("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_smoked_brick_stairs_from_marble_smoked_stonecutting"));
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(BlocksPM.MARBLE_SMOKED_BRICKS.get()), BlocksPM.MARBLE_SMOKED_BRICK_STAIRS.get())
            .addCriterion("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_smoked_brick_stairs_from_marble_smoked_bricks_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_SMOKED_BRICK_WALL.get(), 6)
            .patternLine("MMM")
            .patternLine("MMM")
            .key('M', BlocksPM.MARBLE_SMOKED_BRICKS.get())
            .setGroup("marble_smoked")
            .addCriterion("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED.get()))
            .build(consumer);
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(BlocksPM.MARBLE_SMOKED.get()), BlocksPM.MARBLE_SMOKED_BRICK_WALL.get())
            .addCriterion("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_smoked_brick_wall_from_marble_smoked_stonecutting"));
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(BlocksPM.MARBLE_SMOKED_BRICKS.get()), BlocksPM.MARBLE_SMOKED_BRICK_WALL.get())
            .addCriterion("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_smoked_brick_wall_from_marble_smoked_bricks_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_SMOKED_BRICKS.get(), 4)
            .patternLine("MM")
            .patternLine("MM")
            .key('M', BlocksPM.MARBLE_SMOKED.get())
            .setGroup("marble_smoked")
            .addCriterion("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED.get()))
            .build(consumer);
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(BlocksPM.MARBLE_SMOKED.get()), BlocksPM.MARBLE_SMOKED_BRICKS.get())
            .addCriterion("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_smoked_bricks_from_marble_smoked_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_SMOKED_CHISELED.get())
            .patternLine("M")
            .patternLine("M")
            .key('M', BlocksPM.MARBLE_SMOKED_SLAB.get())
            .setGroup("marble_smoked")
            .addCriterion("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED.get()))
            .build(consumer);
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(BlocksPM.MARBLE_SMOKED.get()), BlocksPM.MARBLE_SMOKED_CHISELED.get())
            .addCriterion("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_smoked_chiseled_from_marble_smoked_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_SMOKED_PILLAR.get(), 2)
            .patternLine("M")
            .patternLine("M")
            .key('M', BlocksPM.MARBLE_SMOKED.get())
            .setGroup("marble_smoked")
            .addCriterion("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED.get()))
            .build(consumer);
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(BlocksPM.MARBLE_SMOKED.get()), BlocksPM.MARBLE_SMOKED_PILLAR.get())
            .addCriterion("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_smoked_pillar_from_marble_smoked_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_SMOKED_RUNED.get(), 5)
            .patternLine(" M ")
            .patternLine("MCM")
            .patternLine(" M ")
            .key('M', BlocksPM.MARBLE_SMOKED.get())
            .key('C', BlocksPM.MARBLE_SMOKED_CHISELED.get())
            .setGroup("marble_smoked")
            .addCriterion("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED.get()))
            .build(consumer);
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(BlocksPM.MARBLE_SMOKED.get()), BlocksPM.MARBLE_SMOKED_RUNED.get())
            .addCriterion("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_smoked_runed_from_marble_smoked_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_SMOKED_SLAB.get(), 6)
            .patternLine("MMM")
            .key('M', BlocksPM.MARBLE_SMOKED.get())
            .setGroup("marble_smoked")
            .addCriterion("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED.get()))
            .build(consumer);
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(BlocksPM.MARBLE_SMOKED.get()), BlocksPM.MARBLE_SMOKED_SLAB.get(), 2)
            .addCriterion("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_smoked_slab_from_marble_smoked_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_SMOKED_STAIRS.get(), 4)
            .patternLine("M  ")
            .patternLine("MM ")
            .patternLine("MMM")
            .key('M', BlocksPM.MARBLE_SMOKED.get())
            .setGroup("marble_smoked")
            .addCriterion("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED.get()))
            .build(consumer);
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(BlocksPM.MARBLE_SMOKED.get()), BlocksPM.MARBLE_SMOKED_STAIRS.get())
            .addCriterion("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_smoked_stairs_from_marble_smoked_stonecutting"));
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MARBLE_SMOKED_WALL.get(), 6)
            .patternLine("MMM")
            .patternLine("MMM")
            .key('M', BlocksPM.MARBLE_SMOKED.get())
            .setGroup("marble_smoked")
            .addCriterion("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED.get()))
            .build(consumer);
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(BlocksPM.MARBLE_SMOKED.get()), BlocksPM.MARBLE_SMOKED_WALL.get())
            .addCriterion("has_marble_smoked", this.hasItem(BlocksPM.MARBLE_SMOKED.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "marble_smoked_wall_from_marble_smoked_stonecutting"));
    }

    protected void registerSunwoodRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.SUNWOOD_WOOD.get(), 3)
            .patternLine("WW")
            .patternLine("WW")
            .key('W', BlocksPM.SUNWOOD_LOG.get())
            .setGroup("bark")
            .addCriterion("has_sunwood_log", this.hasItem(BlocksPM.SUNWOOD_LOG.get()))
            .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.STRIPPED_SUNWOOD_WOOD.get(), 3)
            .patternLine("WW")
            .patternLine("WW")
            .key('W', BlocksPM.STRIPPED_SUNWOOD_LOG.get())
            .setGroup("stripped_bark")
            .addCriterion("has_sunwood_log", this.hasItem(BlocksPM.SUNWOOD_LOG.get()))
            .addCriterion("has_stripped_sunwood_log", this.hasItem(BlocksPM.STRIPPED_SUNWOOD_LOG.get()))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(BlocksPM.SUNWOOD_PLANKS.get(), 4)
            .addIngredient(ItemTagsPM.SUNWOOD_LOGS)
            .setGroup("planks")
            .addCriterion("has_sunwood_log", this.hasItem(ItemTagsPM.SUNWOOD_LOGS))
            .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.SUNWOOD_SLAB.get(), 6)
            .patternLine("###")
            .key('#', BlocksPM.SUNWOOD_PLANKS.get())
            .setGroup("wooden_slab")
            .addCriterion("has_planks", this.hasItem(BlocksPM.SUNWOOD_PLANKS.get()))
            .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.SUNWOOD_STAIRS.get(), 4)
            .patternLine("#  ")
            .patternLine("## ")
            .patternLine("###")
            .key('#', BlocksPM.SUNWOOD_PLANKS.get())
            .setGroup("wooden_stairs")
            .addCriterion("has_planks", this.hasItem(BlocksPM.SUNWOOD_PLANKS.get()))
            .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.SUNWOOD_PILLAR.get(), 2)
            .patternLine("#")
            .patternLine("#")
            .key('#', ItemTagsPM.SUNWOOD_LOGS)
            .addCriterion("has_sunwood_log", this.hasItem(ItemTagsPM.SUNWOOD_LOGS))
            .build(consumer);
    }
    
    protected void registerMoonwoodRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MOONWOOD_WOOD.get(), 3)
            .patternLine("WW")
            .patternLine("WW")
            .key('W', BlocksPM.MOONWOOD_LOG.get())
            .setGroup("bark")
            .addCriterion("has_moonwood_log", this.hasItem(BlocksPM.MOONWOOD_LOG.get()))
            .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.STRIPPED_MOONWOOD_WOOD.get(), 3)
            .patternLine("WW")
            .patternLine("WW")
            .key('W', BlocksPM.STRIPPED_MOONWOOD_LOG.get())
            .setGroup("stripped_bark")
            .addCriterion("has_moonwood_log", this.hasItem(BlocksPM.MOONWOOD_LOG.get()))
            .addCriterion("has_stripped_moonwood_log", this.hasItem(BlocksPM.STRIPPED_MOONWOOD_LOG.get()))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(BlocksPM.MOONWOOD_PLANKS.get(), 4)
            .addIngredient(ItemTagsPM.MOONWOOD_LOGS)
            .setGroup("planks")
            .addCriterion("has_moonwood_log", this.hasItem(ItemTagsPM.MOONWOOD_LOGS))
            .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MOONWOOD_SLAB.get(), 6)
            .patternLine("###")
            .key('#', BlocksPM.MOONWOOD_PLANKS.get())
            .setGroup("wooden_slab")
            .addCriterion("has_planks", this.hasItem(BlocksPM.MOONWOOD_PLANKS.get()))
            .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MOONWOOD_STAIRS.get(), 4)
            .patternLine("#  ")
            .patternLine("## ")
            .patternLine("###")
            .key('#', BlocksPM.MOONWOOD_PLANKS.get())
            .setGroup("wooden_stairs")
            .addCriterion("has_planks", this.hasItem(BlocksPM.MOONWOOD_PLANKS.get()))
            .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(BlocksPM.MOONWOOD_PILLAR.get(), 2)
            .patternLine("#")
            .patternLine("#")
            .key('#', ItemTagsPM.MOONWOOD_LOGS)
            .addCriterion("has_moonwood_log", this.hasItem(ItemTagsPM.MOONWOOD_LOGS))
            .build(consumer);
    }

    protected void registerEssenceUpgradeRecipes(Consumer<IFinishedRecipe> consumer) {
        for (Source source : Source.SORTED_SOURCES) {
            for (EssenceType baseType : EssenceType.values()) {
                EssenceType upgradeType = baseType.getUpgrade();
                if (upgradeType != null) {
                    ItemStack baseStack = EssenceItem.getEssence(baseType, source);
                    ItemStack upgradeStack = EssenceItem.getEssence(upgradeType, source);
                    if (!baseStack.isEmpty() && !upgradeStack.isEmpty()) {
                        CompoundResearchKey research;
                        SimpleResearchKey baseResearch = SimpleResearchKey.parse(upgradeType.getName().toUpperCase() + "_SYNTHESIS");
                        if (source.getDiscoverKey() == null) {
                            research = CompoundResearchKey.from(baseResearch);
                        } else {
                            research = CompoundResearchKey.from(true, baseResearch, source.getDiscoverKey());
                        }
                        String name = "essence_" + upgradeType.getName() + "_" + source.getTag() + "_from_" + baseType.getName();
                        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(upgradeStack.getItem())
                            .patternLine("###")
                            .patternLine("#Q#")
                            .patternLine("###")
                            .key('#', baseStack.getItem())
                            .key('Q', Items.QUARTZ)
                            .research(research)
                            .manaCost(new SourceList().add(source, upgradeType.getAffinity()))
                            .build(consumer, new ResourceLocation(PrimalMagic.MODID, name));
                    }
                }
            }
        }
    }
    
    protected void registerEssenceDowngradeRecipes(Consumer<IFinishedRecipe> consumer) {
        for (Source source : Source.SORTED_SOURCES) {
            for (EssenceType baseType : EssenceType.values()) {
                EssenceType downgradeType = baseType.getDowngrade();
                if (downgradeType != null) {
                    ItemStack baseStack = EssenceItem.getEssence(baseType, source);
                    ItemStack downgradeStack = EssenceItem.getEssence(downgradeType, source);
                    if (!baseStack.isEmpty() && !downgradeStack.isEmpty()) {
                        CompoundResearchKey research;
                        SimpleResearchKey baseResearch = SimpleResearchKey.parse(baseType.getName().toUpperCase() + "_DESYNTHESIS");
                        if (source.getDiscoverKey() == null) {
                            research = CompoundResearchKey.from(baseResearch);
                        } else {
                            research = CompoundResearchKey.from(true, baseResearch, source.getDiscoverKey());
                        }
                        String name = "essence_" + downgradeType.getName() + "_" + source.getTag() + "_from_" + baseType.getName();
                        ArcaneShapelessRecipeBuilder.arcaneShapelessRecipe(downgradeStack.getItem(), 4)
                            .addIngredient(baseStack.getItem())
                            .research(research)
                            .manaCost(new SourceList().add(source, baseType.getAffinity()))
                            .build(consumer, new ResourceLocation(PrimalMagic.MODID, name));
                    }
                }
            }
        }
    }

    protected void registerSaltRecipes(Consumer<IFinishedRecipe> consumer) {
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(BlocksPM.ROCK_SALT_ORE.get()), ItemsPM.ROCK_SALT.get(), 0, 200)
            .addCriterion("has_rock_salt_ore", this.hasItem(BlocksPM.ROCK_SALT_ORE.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "rock_salt_from_smelting"));
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ItemsPM.ROCK_SALT.get()), ItemsPM.REFINED_SALT.get(), 0.2F, 200)
            .addCriterion("has_rock_salt", this.hasItem(ItemsPM.ROCK_SALT.get()))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ItemsPM.SALTED_BAKED_POTATO.get())
            .addIngredient(ItemsPM.REFINED_SALT.get())
            .addIngredient(Items.BAKED_POTATO)
            .addCriterion("has_salt", this.hasItem(ItemsPM.REFINED_SALT.get()))
            .addCriterion("has_baked_potato", this.hasItem(Items.BAKED_POTATO))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ItemsPM.SALTED_COOKED_BEEF.get())
            .addIngredient(ItemsPM.REFINED_SALT.get())
            .addIngredient(Items.COOKED_BEEF)
            .addCriterion("has_salt", this.hasItem(ItemsPM.REFINED_SALT.get()))
            .addCriterion("has_cooked_beef", this.hasItem(Items.COOKED_BEEF))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ItemsPM.SALTED_COOKED_CHICKEN.get())
            .addIngredient(ItemsPM.REFINED_SALT.get())
            .addIngredient(Items.COOKED_CHICKEN)
            .addCriterion("has_salt", this.hasItem(ItemsPM.REFINED_SALT.get()))
            .addCriterion("has_cooked_chicken", this.hasItem(Items.COOKED_CHICKEN))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ItemsPM.SALTED_COOKED_COD.get())
            .addIngredient(ItemsPM.REFINED_SALT.get())
            .addIngredient(Items.COOKED_COD)
            .addCriterion("has_salt", this.hasItem(ItemsPM.REFINED_SALT.get()))
            .addCriterion("has_cooked_cod", this.hasItem(Items.COOKED_COD))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ItemsPM.SALTED_COOKED_MUTTON.get())
            .addIngredient(ItemsPM.REFINED_SALT.get())
            .addIngredient(Items.COOKED_MUTTON)
            .addCriterion("has_salt", this.hasItem(ItemsPM.REFINED_SALT.get()))
            .addCriterion("has_cooked_mutton", this.hasItem(Items.COOKED_MUTTON))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ItemsPM.SALTED_COOKED_PORKCHOP.get())
            .addIngredient(ItemsPM.REFINED_SALT.get())
            .addIngredient(Items.COOKED_PORKCHOP)
            .addCriterion("has_salt", this.hasItem(ItemsPM.REFINED_SALT.get()))
            .addCriterion("has_cooked_porkchop", this.hasItem(Items.COOKED_PORKCHOP))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ItemsPM.SALTED_COOKED_RABBIT.get())
            .addIngredient(ItemsPM.REFINED_SALT.get())
            .addIngredient(Items.COOKED_RABBIT)
            .addCriterion("has_salt", this.hasItem(ItemsPM.REFINED_SALT.get()))
            .addCriterion("has_cooked_rabbit", this.hasItem(Items.COOKED_RABBIT))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ItemsPM.SALTED_COOKED_SALMON.get())
            .addIngredient(ItemsPM.REFINED_SALT.get())
            .addIngredient(Items.COOKED_SALMON)
            .addCriterion("has_salt", this.hasItem(ItemsPM.REFINED_SALT.get()))
            .addCriterion("has_cooked_salmon", this.hasItem(Items.COOKED_SALMON))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ItemsPM.SALTED_BEETROOT_SOUP.get())
            .addIngredient(ItemsPM.REFINED_SALT.get())
            .addIngredient(Items.BEETROOT_SOUP)
            .addCriterion("has_salt", this.hasItem(ItemsPM.REFINED_SALT.get()))
            .addCriterion("has_beetroot_soup", this.hasItem(Items.BEETROOT_SOUP))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ItemsPM.SALTED_MUSHROOM_STEW.get())
            .addIngredient(ItemsPM.REFINED_SALT.get())
            .addIngredient(Items.MUSHROOM_STEW)
            .addCriterion("has_salt", this.hasItem(ItemsPM.REFINED_SALT.get()))
            .addCriterion("has_mushroom_stew", this.hasItem(Items.MUSHROOM_STEW))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ItemsPM.SALTED_RABBIT_STEW.get())
            .addIngredient(ItemsPM.REFINED_SALT.get())
            .addIngredient(Items.RABBIT_STEW)
            .addCriterion("has_salt", this.hasItem(ItemsPM.REFINED_SALT.get()))
            .addCriterion("has_rabbit_stew", this.hasItem(Items.RABBIT_STEW))
            .build(consumer);
    }
    
    protected void registerSkyglassRecipes(Consumer<IFinishedRecipe> consumer) {
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.SKYGLASS.get(), 8)
            .patternLine("GGG")
            .patternLine("GDG")
            .patternLine("GGG")
            .key('G', Items.GLASS)
            .key('D', ItemsPM.ESSENCE_DUST_SKY.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer);
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_BLACK.get(), 8)
            .patternLine("GGG")
            .patternLine("GDG")
            .patternLine("GGG")
            .key('G', ItemTagsPM.SKYGLASS)
            .key('D', Tags.Items.DYES_BLACK)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer);
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_BLUE.get(), 8)
            .patternLine("GGG")
            .patternLine("GDG")
            .patternLine("GGG")
            .key('G', ItemTagsPM.SKYGLASS)
            .key('D', Tags.Items.DYES_BLUE)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer);
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_BROWN.get(), 8)
            .patternLine("GGG")
            .patternLine("GDG")
            .patternLine("GGG")
            .key('G', ItemTagsPM.SKYGLASS)
            .key('D', Tags.Items.DYES_BROWN)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer);
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_CYAN.get(), 8)
            .patternLine("GGG")
            .patternLine("GDG")
            .patternLine("GGG")
            .key('G', ItemTagsPM.SKYGLASS)
            .key('D', Tags.Items.DYES_CYAN)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer);
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_GRAY.get(), 8)
            .patternLine("GGG")
            .patternLine("GDG")
            .patternLine("GGG")
            .key('G', ItemTagsPM.SKYGLASS)
            .key('D', Tags.Items.DYES_GRAY)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer);
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_GREEN.get(), 8)
            .patternLine("GGG")
            .patternLine("GDG")
            .patternLine("GGG")
            .key('G', ItemTagsPM.SKYGLASS)
            .key('D', Tags.Items.DYES_GREEN)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer);
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_LIGHT_BLUE.get(), 8)
            .patternLine("GGG")
            .patternLine("GDG")
            .patternLine("GGG")
            .key('G', ItemTagsPM.SKYGLASS)
            .key('D', Tags.Items.DYES_LIGHT_BLUE)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer);
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_LIGHT_GRAY.get(), 8)
            .patternLine("GGG")
            .patternLine("GDG")
            .patternLine("GGG")
            .key('G', ItemTagsPM.SKYGLASS)
            .key('D', Tags.Items.DYES_LIGHT_GRAY)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer);
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_LIME.get(), 8)
            .patternLine("GGG")
            .patternLine("GDG")
            .patternLine("GGG")
            .key('G', ItemTagsPM.SKYGLASS)
            .key('D', Tags.Items.DYES_LIME)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer);
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_MAGENTA.get(), 8)
            .patternLine("GGG")
            .patternLine("GDG")
            .patternLine("GGG")
            .key('G', ItemTagsPM.SKYGLASS)
            .key('D', Tags.Items.DYES_MAGENTA)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer);
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_ORANGE.get(), 8)
            .patternLine("GGG")
            .patternLine("GDG")
            .patternLine("GGG")
            .key('G', ItemTagsPM.SKYGLASS)
            .key('D', Tags.Items.DYES_ORANGE)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer);
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_PINK.get(), 8)
            .patternLine("GGG")
            .patternLine("GDG")
            .patternLine("GGG")
            .key('G', ItemTagsPM.SKYGLASS)
            .key('D', Tags.Items.DYES_PINK)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer);
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_PURPLE.get(), 8)
            .patternLine("GGG")
            .patternLine("GDG")
            .patternLine("GGG")
            .key('G', ItemTagsPM.SKYGLASS)
            .key('D', Tags.Items.DYES_PURPLE)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer);
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_RED.get(), 8)
            .patternLine("GGG")
            .patternLine("GDG")
            .patternLine("GGG")
            .key('G', ItemTagsPM.SKYGLASS)
            .key('D', Tags.Items.DYES_RED)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer);
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_WHITE.get(), 8)
            .patternLine("GGG")
            .patternLine("GDG")
            .patternLine("GGG")
            .key('G', ItemTagsPM.SKYGLASS)
            .key('D', Tags.Items.DYES_WHITE)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer);
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_YELLOW.get(), 8)
            .patternLine("GGG")
            .patternLine("GDG")
            .patternLine("GGG")
            .key('G', ItemTagsPM.SKYGLASS)
            .key('D', Tags.Items.DYES_YELLOW)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer);
    }
    
    protected void registerSkyglassPaneRecipes(Consumer<IFinishedRecipe> consumer) {
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.SKYGLASS_PANE.get(), 16)
            .patternLine("GGG")
            .patternLine("GGG")
            .key('G', ItemsPM.SKYGLASS.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer);
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_PANE_BLACK.get(), 16)
            .patternLine("GGG")
            .patternLine("GGG")
            .key('G', ItemsPM.STAINED_SKYGLASS_BLACK.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "stained_skyglass_pane_black_from_blocks"));
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_PANE_BLACK.get(), 8)
            .patternLine("GGG")
            .patternLine("GDG")
            .patternLine("GGG")
            .key('G', ItemTagsPM.SKYGLASS_PANES)
            .key('D', Tags.Items.DYES_BLACK)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "stained_skyglass_pane_black_from_panes"));
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_PANE_BLUE.get(), 16)
            .patternLine("GGG")
            .patternLine("GGG")
            .key('G', ItemsPM.STAINED_SKYGLASS_BLUE.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "stained_skyglass_pane_blue_from_blocks"));
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_PANE_BLUE.get(), 8)
            .patternLine("GGG")
            .patternLine("GDG")
            .patternLine("GGG")
            .key('G', ItemTagsPM.SKYGLASS_PANES)
            .key('D', Tags.Items.DYES_BLUE)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "stained_skyglass_pane_blue_from_panes"));
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_PANE_BROWN.get(), 16)
            .patternLine("GGG")
            .patternLine("GGG")
            .key('G', ItemsPM.STAINED_SKYGLASS_BROWN.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "stained_skyglass_pane_brown_from_blocks"));
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_PANE_BROWN.get(), 8)
            .patternLine("GGG")
            .patternLine("GDG")
            .patternLine("GGG")
            .key('G', ItemTagsPM.SKYGLASS_PANES)
            .key('D', Tags.Items.DYES_BROWN)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "stained_skyglass_pane_brown_from_panes"));
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_PANE_CYAN.get(), 16)
            .patternLine("GGG")
            .patternLine("GGG")
            .key('G', ItemsPM.STAINED_SKYGLASS_CYAN.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "stained_skyglass_pane_cyan_from_blocks"));
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_PANE_CYAN.get(), 8)
            .patternLine("GGG")
            .patternLine("GDG")
            .patternLine("GGG")
            .key('G', ItemTagsPM.SKYGLASS_PANES)
            .key('D', Tags.Items.DYES_CYAN)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "stained_skyglass_pane_cyan_from_panes"));
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_PANE_GRAY.get(), 16)
            .patternLine("GGG")
            .patternLine("GGG")
            .key('G', ItemsPM.STAINED_SKYGLASS_GRAY.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "stained_skyglass_pane_gray_from_blocks"));
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_PANE_GRAY.get(), 8)
            .patternLine("GGG")
            .patternLine("GDG")
            .patternLine("GGG")
            .key('G', ItemTagsPM.SKYGLASS_PANES)
            .key('D', Tags.Items.DYES_GRAY)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "stained_skyglass_pane_gray_from_panes"));
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_PANE_GREEN.get(), 16)
            .patternLine("GGG")
            .patternLine("GGG")
            .key('G', ItemsPM.STAINED_SKYGLASS_GREEN.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "stained_skyglass_pane_green_from_blocks"));
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_PANE_GREEN.get(), 8)
            .patternLine("GGG")
            .patternLine("GDG")
            .patternLine("GGG")
            .key('G', ItemTagsPM.SKYGLASS_PANES)
            .key('D', Tags.Items.DYES_GREEN)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "stained_skyglass_pane_green_from_panes"));
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_PANE_LIGHT_BLUE.get(), 16)
            .patternLine("GGG")
            .patternLine("GGG")
            .key('G', ItemsPM.STAINED_SKYGLASS_LIGHT_BLUE.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "stained_skyglass_pane_light_blue_from_blocks"));
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_PANE_LIGHT_BLUE.get(), 8)
            .patternLine("GGG")
            .patternLine("GDG")
            .patternLine("GGG")
            .key('G', ItemTagsPM.SKYGLASS_PANES)
            .key('D', Tags.Items.DYES_LIGHT_BLUE)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "stained_skyglass_pane_light_blue_from_panes"));
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_PANE_LIGHT_GRAY.get(), 16)
            .patternLine("GGG")
            .patternLine("GGG")
            .key('G', ItemsPM.STAINED_SKYGLASS_LIGHT_GRAY.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "stained_skyglass_pane_light_gray_from_blocks"));
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_PANE_LIGHT_GRAY.get(), 8)
            .patternLine("GGG")
            .patternLine("GDG")
            .patternLine("GGG")
            .key('G', ItemTagsPM.SKYGLASS_PANES)
            .key('D', Tags.Items.DYES_LIGHT_GRAY)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "stained_skyglass_pane_light_gray_from_panes"));
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_PANE_LIME.get(), 16)
            .patternLine("GGG")
            .patternLine("GGG")
            .key('G', ItemsPM.STAINED_SKYGLASS_LIME.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "stained_skyglass_pane_lime_from_blocks"));
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_PANE_LIME.get(), 8)
            .patternLine("GGG")
            .patternLine("GDG")
            .patternLine("GGG")
            .key('G', ItemTagsPM.SKYGLASS_PANES)
            .key('D', Tags.Items.DYES_LIME)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "stained_skyglass_pane_lime_from_panes"));
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_PANE_MAGENTA.get(), 16)
            .patternLine("GGG")
            .patternLine("GGG")
            .key('G', ItemsPM.STAINED_SKYGLASS_MAGENTA.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "stained_skyglass_pane_magenta_from_blocks"));
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_PANE_MAGENTA.get(), 8)
            .patternLine("GGG")
            .patternLine("GDG")
            .patternLine("GGG")
            .key('G', ItemTagsPM.SKYGLASS_PANES)
            .key('D', Tags.Items.DYES_MAGENTA)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "stained_skyglass_pane_magenta_from_panes"));
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_PANE_ORANGE.get(), 16)
            .patternLine("GGG")
            .patternLine("GGG")
            .key('G', ItemsPM.STAINED_SKYGLASS_ORANGE.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "stained_skyglass_pane_orange_from_blocks"));
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_PANE_ORANGE.get(), 8)
            .patternLine("GGG")
            .patternLine("GDG")
            .patternLine("GGG")
            .key('G', ItemTagsPM.SKYGLASS_PANES)
            .key('D', Tags.Items.DYES_ORANGE)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "stained_skyglass_pane_orange_from_panes"));
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_PANE_PINK.get(), 16)
            .patternLine("GGG")
            .patternLine("GGG")
            .key('G', ItemsPM.STAINED_SKYGLASS_PINK.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "stained_skyglass_pane_pink_from_blocks"));
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_PANE_PINK.get(), 8)
            .patternLine("GGG")
            .patternLine("GDG")
            .patternLine("GGG")
            .key('G', ItemTagsPM.SKYGLASS_PANES)
            .key('D', Tags.Items.DYES_PINK)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "stained_skyglass_pane_pink_from_panes"));
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_PANE_PURPLE.get(), 16)
            .patternLine("GGG")
            .patternLine("GGG")
            .key('G', ItemsPM.STAINED_SKYGLASS_PURPLE.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "stained_skyglass_pane_purple_from_blocks"));
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_PANE_PURPLE.get(), 8)
            .patternLine("GGG")
            .patternLine("GDG")
            .patternLine("GGG")
            .key('G', ItemTagsPM.SKYGLASS_PANES)
            .key('D', Tags.Items.DYES_PURPLE)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "stained_skyglass_pane_purple_from_panes"));
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_PANE_RED.get(), 16)
            .patternLine("GGG")
            .patternLine("GGG")
            .key('G', ItemsPM.STAINED_SKYGLASS_RED.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "stained_skyglass_pane_red_from_blocks"));
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_PANE_RED.get(), 8)
            .patternLine("GGG")
            .patternLine("GDG")
            .patternLine("GGG")
            .key('G', ItemTagsPM.SKYGLASS_PANES)
            .key('D', Tags.Items.DYES_RED)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "stained_skyglass_pane_red_from_panes"));
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_PANE_WHITE.get(), 16)
            .patternLine("GGG")
            .patternLine("GGG")
            .key('G', ItemsPM.STAINED_SKYGLASS_WHITE.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "stained_skyglass_pane_white_from_blocks"));
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_PANE_WHITE.get(), 8)
            .patternLine("GGG")
            .patternLine("GDG")
            .patternLine("GGG")
            .key('G', ItemTagsPM.SKYGLASS_PANES)
            .key('D', Tags.Items.DYES_WHITE)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "stained_skyglass_pane_white_from_panes"));
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_PANE_YELLOW.get(), 16)
            .patternLine("GGG")
            .patternLine("GGG")
            .key('G', ItemsPM.STAINED_SKYGLASS_YELLOW.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "stained_skyglass_pane_yellow_from_blocks"));
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.STAINED_SKYGLASS_PANE_YELLOW.get(), 8)
            .patternLine("GGG")
            .patternLine("GDG")
            .patternLine("GGG")
            .key('G', ItemTagsPM.SKYGLASS_PANES)
            .key('D', Tags.Items.DYES_YELLOW)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SKYGLASS")))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "stained_skyglass_pane_yellow_from_panes"));
    }

    protected void registerEarthshatterHammerRecipes(Consumer<IFinishedRecipe> consumer) {
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.EARTHSHATTER_HAMMER.get())
            .patternLine("III")
            .patternLine("IEI")
            .patternLine(" S ")
            .key('I', Tags.Items.INGOTS_IRON)
            .key('E', ItemsPM.ESSENCE_SHARD_EARTH.get())
            .key('S', Tags.Items.RODS_WOODEN)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("EARTHSHATTER_HAMMER")))
            .manaCost(new SourceList().add(Source.EARTH, 20))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ItemsPM.IRON_GRIT.get(), 2)
            .addIngredient(ItemsPM.EARTHSHATTER_HAMMER.get())
            .addIngredient(Tags.Items.ORES_IRON)
            .addCriterion("has_hammer", this.hasItem(ItemsPM.EARTHSHATTER_HAMMER.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "iron_grit_from_earthshatter_hammer"));
        ShapelessRecipeBuilder.shapelessRecipe(ItemsPM.GOLD_GRIT.get(), 2)
            .addIngredient(ItemsPM.EARTHSHATTER_HAMMER.get())
            .addIngredient(Tags.Items.ORES_GOLD)
            .addCriterion("has_hammer", this.hasItem(ItemsPM.EARTHSHATTER_HAMMER.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "gold_grit_from_earthshatter_hammer"));
    }

    protected void registerMineralRecipes(Consumer<IFinishedRecipe> consumer) {
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ItemsPM.IRON_GRIT.get()), Items.IRON_INGOT, 0.7F, 200)
            .addCriterion("has_grit", this.hasItem(ItemsPM.IRON_GRIT.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "iron_ingot_from_grit_smelting"));
        CookingRecipeBuilder.blastingRecipe(Ingredient.fromItems(ItemsPM.IRON_GRIT.get()), Items.IRON_INGOT, 0.7F, 100)
            .addCriterion("has_grit", this.hasItem(ItemsPM.IRON_GRIT.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "iron_ingot_from_grit_blasting"));
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ItemsPM.GOLD_GRIT.get()), Items.GOLD_INGOT, 0.7F, 200)
            .addCriterion("has_grit", this.hasItem(ItemsPM.GOLD_GRIT.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "gold_ingot_from_grit_smelting"));
        CookingRecipeBuilder.blastingRecipe(Ingredient.fromItems(ItemsPM.GOLD_GRIT.get()), Items.GOLD_INGOT, 0.7F, 100)
            .addCriterion("has_grit", this.hasItem(ItemsPM.GOLD_GRIT.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "gold_ingot_from_grit_blasting"));
    }
}
