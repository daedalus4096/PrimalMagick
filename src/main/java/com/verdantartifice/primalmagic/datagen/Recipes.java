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
import com.verdantartifice.primalmagic.common.tags.BlockTagsPM;
import com.verdantartifice.primalmagic.common.tags.ItemTagsForgeExt;
import com.verdantartifice.primalmagic.common.tags.ItemTagsPM;

import net.minecraft.data.CookingRecipeBuilder;
import net.minecraft.data.CustomRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.data.SingleItemRecipeBuilder;
import net.minecraft.item.Item;
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
        this.registerPrimaliteRecipes(consumer);
        this.registerHexiumRecipes(consumer);
        this.registerHallowsteelRecipes(consumer);
        this.registerWandComponentRecipes(consumer);
        this.registerRitualCandleRecipes(consumer);
        this.registerRuneRecipes(consumer);
        
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
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.SUNLAMP.get())
            .patternLine("NNN")
            .patternLine("NTN")
            .patternLine("NNN")
            .key('N', ItemTagsPM.NUGGETS_PRIMALITE)
            .key('T', Items.TORCH)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SUNLAMP")))
            .manaCost(new SourceList().add(Source.SUN, 10))
            .build(consumer);
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.MANA_PRISM.get())
            .patternLine(" N ")
            .patternLine("QDQ")
            .patternLine(" N ")
            .key('N', Tags.Items.NUGGETS_IRON)
            .key('Q', ItemTagsForgeExt.NUGGETS_QUARTZ)
            .key('D', ItemTagsPM.ESSENCES_TERRESTRIAL_DUSTS)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("BASIC_MANAWEAVING")))
            .build(consumer);
        ArcaneShapelessRecipeBuilder.arcaneShapelessRecipe(ItemsPM.MANA_SALTS.get())
            .addIngredient(Tags.Items.DUSTS_REDSTONE)
            .addIngredient(ItemsPM.REFINED_SALT.get())
            .addIngredient(ItemTagsPM.ESSENCES_TERRESTRIAL_DUSTS)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("MANA_SALTS")))
            .manaCost(new SourceList().add(Source.EARTH, 5).add(Source.SEA, 5).add(Source.SKY, 5).add(Source.SUN, 5).add(Source.MOON, 5))
            .build(consumer);
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.WAND_CHARGER.get())
            .patternLine("GMG")
            .patternLine("MPM")
            .patternLine("GMG")
            .key('G', Tags.Items.INGOTS_GOLD)
            .key('M', ItemsPM.MARBLE_RAW.get())
            .key('P', ItemsPM.MANA_PRISM.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("WAND_CHARGER")))
            .build(consumer);
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.SPELLCRAFTING_ALTAR.get())
            .patternLine("MMM")
            .patternLine("MSM")
            .patternLine("MMM")
            .key('M', ItemsPM.MARBLE_RAW.get())
            .key('S', ItemsPM.SPELL_SCROLL_BLANK.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("BASIC_SORCERY")))
            .manaCost(new SourceList().add(Source.EARTH, 5).add(Source.SEA, 5).add(Source.SKY, 5).add(Source.SUN, 5).add(Source.MOON, 5))
            .build(consumer);
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.WAND_ASSEMBLY_TABLE.get())
            .patternLine("MM")
            .patternLine("MM")
            .key('M', ItemsPM.MARBLE_RUNED.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("ADVANCED_WANDMAKING")))
            .manaCost(new SourceList().add(Source.EARTH, 5).add(Source.SEA, 5).add(Source.SKY, 5).add(Source.SUN, 5).add(Source.MOON, 5))
            .build(consumer);
        ArcaneShapelessRecipeBuilder.arcaneShapelessRecipe(ItemsPM.WAND_INSCRIPTION_TABLE.get())
            .addIngredient(ItemsPM.WOOD_TABLE.get())
            .addIngredient(ItemsPM.SPELL_SCROLL_BLANK.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("WAND_INSCRIPTION")))
            .manaCost(new SourceList().add(Source.EARTH, 5).add(Source.SEA, 5).add(Source.SKY, 5).add(Source.SUN, 5).add(Source.MOON, 5))
            .build(consumer);
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.RITUAL_ALTAR.get())
            .patternLine("GRG")
            .patternLine("GMG")
            .key('G', Tags.Items.INGOTS_GOLD)
            .key('R', ItemsPM.MARBLE_RUNED.get())
            .key('M', ItemsPM.MARBLE_RAW.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("BASIC_RITUAL")))
            .manaCost(new SourceList().add(Source.EARTH, 10).add(Source.SEA, 10).add(Source.SKY, 10).add(Source.SUN, 10).add(Source.MOON, 10))
            .build(consumer);
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.OFFERING_PEDESTAL.get())
            .patternLine("S")
            .patternLine("P")
            .patternLine("S")
            .key('S', ItemsPM.MARBLE_SLAB.get())
            .key('P', ItemsPM.MARBLE_PILLAR.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("BASIC_RITUAL")))
            .build(consumer);
        RitualRecipeBuilder.ritualRecipe(ItemsPM.MANAFRUIT.get())
            .addIngredient(Items.APPLE)
            .addIngredient(Items.HONEY_BOTTLE)
            .addIngredient(ItemsPM.MANA_SALTS.get())
            .addProp(BlockTagsPM.RITUAL_CANDLES)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("MANAFRUIT")))
            .manaCost(new SourceList().add(Source.EARTH, 5).add(Source.SEA, 5).add(Source.SKY, 5).add(Source.SUN, 5).add(Source.MOON, 5))
            .instability(1)
            .build(consumer);
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.INCENSE_BRAZIER.get())
            .patternLine("GCG")
            .patternLine("GGG")
            .patternLine(" P ")
            .key('G', Tags.Items.INGOTS_GOLD)
            .key('C', ItemTags.COALS)
            .key('P', ItemTags.PLANKS)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("INCENSE_BRAZIER")))
            .build(consumer);
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.INCENSE_STICK.get())
            .patternLine(" F")
            .patternLine("S ")
            .key('F', ItemTags.SMALL_FLOWERS)
            .key('S', Tags.Items.RODS_WOODEN)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("INCENSE_BRAZIER")))
            .manaCost(new SourceList().add(Source.SKY, 1))
            .build(consumer);
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.RITUAL_LECTERN.get())
            .patternLine("SSS")
            .patternLine(" B ")
            .patternLine(" S ")
            .key('S', ItemsPM.MOONWOOD_SLAB.get())
            .key('B', Tags.Items.BOOKSHELVES)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("RITUAL_LECTERN")))
            .manaCost(new SourceList().add(Source.MOON, 10))
            .build(consumer);
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.RITUAL_BELL.get())
            .patternLine("PPP")
            .patternLine("PSP")
            .patternLine("PIP")
            .key('P', ItemTagsPM.INGOTS_PRIMALITE)
            .key('S', Tags.Items.RODS_WOODEN)
            .key('I', Tags.Items.NUGGETS_IRON)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("RITUAL_BELL")))
            .manaCost(new SourceList().add(Source.SEA, 10))
            .build(consumer);
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.BLOODLETTER.get())
            .patternLine("MSM")
            .patternLine("MMM")
            .patternLine(" P ")
            .key('M', ItemsPM.MARBLE_SMOKED.get())
            .key('S', Items.DIAMOND_SWORD)
            .key('P', ItemsPM.MARBLE_SMOKED_PILLAR.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("BLOODLETTER")))
            .manaCost(new SourceList().add(Source.BLOOD, 50))
            .build(consumer);
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.SOUL_ANVIL.get())
            .patternLine("BBB")
            .patternLine(" I ")
            .patternLine("III")
            .key('B', ItemTagsPM.STORAGE_BLOCKS_HEXIUM)
            .key('I', ItemTagsPM.INGOTS_HEXIUM)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("SOUL_ANVIL")))
            .manaCost(new SourceList().add(Source.INFERNAL, 50))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ItemsPM.SOUL_GEM.get())
            .addIngredient(ItemsPM.SOUL_GEM_SLIVER.get(), 9)
            .addCriterion("has_sliver", this.hasItem(ItemsPM.SOUL_GEM_SLIVER.get()))
            .build(consumer);
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.RUNIC_GRINDSTONE.get())
            .patternLine(" D ")
            .patternLine("SLS")
            .patternLine("PDP")
            .key('D', ItemsPM.RUNE_DISPEL.get())
            .key('S', Tags.Items.RODS_WOODEN)
            .key('L', Items.STONE_SLAB)
            .key('P', ItemsPM.SUNWOOD_PLANKS.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("RUNIC_GRINDSTONE")))
            .manaCost(new SourceList().add(Source.EARTH, 25))
            .build(consumer);

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
                    Item quartzItem = upgradeType.getUpgradeMedium();
                    if (!baseStack.isEmpty() && !upgradeStack.isEmpty() && quartzItem != null) {
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
                            .key('Q', quartzItem)
                            .research(research)
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
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(BlocksPM.QUARTZ_ORE.get()), Items.QUARTZ, 0.2F, 200)
            .addCriterion("has_quartz_ore", this.hasItem(BlocksPM.QUARTZ_ORE.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "quartz_from_smelting"));
        CookingRecipeBuilder.blastingRecipe(Ingredient.fromItems(BlocksPM.QUARTZ_ORE.get()), Items.QUARTZ, 0.2F, 200)
            .addCriterion("has_quartz_ore", this.hasItem(BlocksPM.QUARTZ_ORE.get()))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "quartz_from_blasting"));
        ShapelessRecipeBuilder.shapelessRecipe(ItemsPM.QUARTZ_NUGGET.get(), 9)
            .addIngredient(Items.QUARTZ)
            .addCriterion("has_quartz", this.hasItem(Items.QUARTZ))
            .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(Items.QUARTZ)
            .patternLine("NNN")
            .patternLine("NNN")
            .patternLine("NNN")
            .key('N', ItemTagsForgeExt.NUGGETS_QUARTZ)
            .addCriterion("has_nugget", this.hasItem(ItemTagsForgeExt.NUGGETS_QUARTZ))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "quartz_from_nuggets"));
    }
    
    protected void registerPrimaliteRecipes(Consumer<IFinishedRecipe> consumer) {
        ArcaneShapelessRecipeBuilder.arcaneShapelessRecipe(ItemsPM.PRIMALITE_INGOT.get())
            .addIngredient(Tags.Items.INGOTS_IRON)
            .addIngredient(ItemsPM.ESSENCE_DUST_EARTH.get())
            .addIngredient(ItemsPM.ESSENCE_DUST_SEA.get())
            .addIngredient(ItemsPM.ESSENCE_DUST_SKY.get())
            .addIngredient(ItemsPM.ESSENCE_DUST_SUN.get())
            .addIngredient(ItemsPM.ESSENCE_DUST_MOON.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("PRIMALITE")))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ItemsPM.PRIMALITE_NUGGET.get(), 9)
            .addIngredient(ItemTagsPM.INGOTS_PRIMALITE)
            .addCriterion("has_ingot", this.hasItem(ItemTagsPM.INGOTS_PRIMALITE))
            .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ItemsPM.PRIMALITE_INGOT.get())
            .patternLine("NNN")
            .patternLine("NNN")
            .patternLine("NNN")
            .key('N', ItemTagsPM.NUGGETS_PRIMALITE)
            .addCriterion("has_nugget", this.hasItem(ItemTagsPM.NUGGETS_PRIMALITE))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "primalite_ingot_from_nuggets"));
        ShapelessRecipeBuilder.shapelessRecipe(ItemsPM.PRIMALITE_INGOT.get(), 9)
            .addIngredient(ItemTagsPM.STORAGE_BLOCKS_PRIMALITE)
            .addCriterion("has_block", this.hasItem(ItemTagsPM.STORAGE_BLOCKS_PRIMALITE))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "primalite_ingots_from_block"));
        ShapedRecipeBuilder.shapedRecipe(ItemsPM.PRIMALITE_BLOCK.get())
            .patternLine("III")
            .patternLine("III")
            .patternLine("III")
            .key('I', ItemTagsPM.INGOTS_PRIMALITE)
            .addCriterion("has_ingot", this.hasItem(ItemTagsPM.INGOTS_PRIMALITE))
            .build(consumer);
    }
    
    protected void registerHexiumRecipes(Consumer<IFinishedRecipe> consumer) {
        ArcaneShapelessRecipeBuilder.arcaneShapelessRecipe(ItemsPM.HEXIUM_INGOT.get())
            .addIngredient(ItemTagsPM.INGOTS_PRIMALITE)
            .addIngredient(ItemsPM.ESSENCE_SHARD_BLOOD.get())
            .addIngredient(ItemsPM.ESSENCE_SHARD_INFERNAL.get())
            .addIngredient(ItemsPM.ESSENCE_SHARD_VOID.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("HEXIUM")))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ItemsPM.HEXIUM_NUGGET.get(), 9)
            .addIngredient(ItemTagsPM.INGOTS_HEXIUM)
            .addCriterion("has_ingot", this.hasItem(ItemTagsPM.INGOTS_HEXIUM))
            .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ItemsPM.HEXIUM_INGOT.get())
            .patternLine("NNN")
            .patternLine("NNN")
            .patternLine("NNN")
            .key('N', ItemTagsPM.NUGGETS_HEXIUM)
            .addCriterion("has_nugget", this.hasItem(ItemTagsPM.NUGGETS_HEXIUM))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "hexium_ingot_from_nuggets"));
        ShapelessRecipeBuilder.shapelessRecipe(ItemsPM.HEXIUM_INGOT.get(), 9)
            .addIngredient(ItemTagsPM.STORAGE_BLOCKS_HEXIUM)
            .addCriterion("has_block", this.hasItem(ItemTagsPM.STORAGE_BLOCKS_HEXIUM))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "hexium_ingots_from_block"));
        ShapedRecipeBuilder.shapedRecipe(ItemsPM.HEXIUM_BLOCK.get())
            .patternLine("III")
            .patternLine("III")
            .patternLine("III")
            .key('I', ItemTagsPM.INGOTS_HEXIUM)
            .addCriterion("has_ingot", this.hasItem(ItemTagsPM.INGOTS_HEXIUM))
            .build(consumer);
    }
    
    protected void registerHallowsteelRecipes(Consumer<IFinishedRecipe> consumer) {
        ArcaneShapelessRecipeBuilder.arcaneShapelessRecipe(ItemsPM.HALLOWSTEEL_INGOT.get())
            .addIngredient(ItemTagsPM.INGOTS_HEXIUM)
            .addIngredient(ItemsPM.ESSENCE_CRYSTAL_HALLOWED.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("HALLOWSTEEL")))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ItemsPM.HALLOWSTEEL_NUGGET.get(), 9)
            .addIngredient(ItemTagsPM.INGOTS_HALLOWSTEEL)
            .addCriterion("has_ingot", this.hasItem(ItemTagsPM.INGOTS_HALLOWSTEEL))
            .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ItemsPM.HALLOWSTEEL_INGOT.get())
            .patternLine("NNN")
            .patternLine("NNN")
            .patternLine("NNN")
            .key('N', ItemTagsPM.NUGGETS_HALLOWSTEEL)
            .addCriterion("has_nugget", this.hasItem(ItemTagsPM.NUGGETS_HALLOWSTEEL))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "hallowsteel_ingot_from_nuggets"));
        ShapelessRecipeBuilder.shapelessRecipe(ItemsPM.HALLOWSTEEL_INGOT.get(), 9)
            .addIngredient(ItemTagsPM.STORAGE_BLOCKS_HALLOWSTEEL)
            .addCriterion("has_block", this.hasItem(ItemTagsPM.STORAGE_BLOCKS_HALLOWSTEEL))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "hallowsteel_ingots_from_block"));
        ShapedRecipeBuilder.shapedRecipe(ItemsPM.HALLOWSTEEL_BLOCK.get())
            .patternLine("III")
            .patternLine("III")
            .patternLine("III")
            .key('I', ItemTagsPM.INGOTS_HALLOWSTEEL)
            .addCriterion("has_ingot", this.hasItem(ItemTagsPM.INGOTS_HALLOWSTEEL))
            .build(consumer);
    }
    
    protected void registerWandComponentRecipes(Consumer<IFinishedRecipe> consumer) {
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.HEARTWOOD_WAND_CORE_ITEM.get())
            .patternLine(" H")
            .patternLine("H ")
            .key('H', ItemsPM.HEARTWOOD.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("WAND_CORE_HEARTWOOD")))
            .manaCost(new SourceList().add(Source.EARTH, 5).add(Source.SEA, 5).add(Source.SKY, 5).add(Source.SUN, 5).add(Source.MOON, 5))
            .build(consumer);
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.IRON_WAND_CAP_ITEM.get())
            .patternLine("NNN")
            .patternLine("N N")
            .key('N', Tags.Items.NUGGETS_IRON)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("WAND_CAP_IRON")))
            .manaCost(new SourceList().add(Source.EARTH, 5).add(Source.SEA, 5).add(Source.SKY, 5).add(Source.SUN, 5).add(Source.MOON, 5))
            .build(consumer);
        ArcaneShapelessRecipeBuilder.arcaneShapelessRecipe(ItemsPM.APPRENTICE_WAND_GEM_ITEM.get())
            .addIngredient(Tags.Items.GEMS_DIAMOND)
            .addIngredient(ItemTagsPM.ESSENCES_TERRESTRIAL_DUSTS)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("WAND_GEM_APPRENTICE")))
            .manaCost(new SourceList().add(Source.EARTH, 10).add(Source.SEA, 10).add(Source.SKY, 10).add(Source.SUN, 10).add(Source.MOON, 10))
            .build(consumer);
    }
    
    protected void registerRitualCandleRecipes(Consumer<IFinishedRecipe> consumer) {
        ArcaneShapelessRecipeBuilder.arcaneShapelessRecipe(ItemsPM.TALLOW.get())
            .addIngredient(Items.ROTTEN_FLESH)
            .addIngredient(ItemsPM.ESSENCE_DUST_SUN.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("RITUAL_CANDLES")))
            .build(consumer);
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.RITUAL_CANDLE_WHITE.get())
            .patternLine("S")
            .patternLine("T")
            .patternLine("T")
            .key('S', Tags.Items.STRING)
            .key('T', ItemsPM.TALLOW.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("RITUAL_CANDLES")))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "ritual_candle_white_from_tallow"));
        ArcaneShapedRecipeBuilder.arcaneShapedRecipe(ItemsPM.RITUAL_CANDLE_WHITE.get())
            .patternLine("S")
            .patternLine("W")
            .patternLine("W")
            .key('S', Tags.Items.STRING)
            .key('W', ItemsPM.BEESWAX.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("RITUAL_CANDLES")))
            .build(consumer, new ResourceLocation(PrimalMagic.MODID, "ritual_candle_white_from_beeswax"));
        ShapelessRecipeBuilder.shapelessRecipe(ItemsPM.RITUAL_CANDLE_BLACK.get())
            .addIngredient(ItemTagsPM.RITUAL_CANDLES)
            .addIngredient(Tags.Items.DYES_BLACK)
            .addCriterion("has_candle", this.hasItem(ItemTagsPM.RITUAL_CANDLES))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ItemsPM.RITUAL_CANDLE_BLUE.get())
            .addIngredient(ItemTagsPM.RITUAL_CANDLES)
            .addIngredient(Tags.Items.DYES_BLUE)
            .addCriterion("has_candle", this.hasItem(ItemTagsPM.RITUAL_CANDLES))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ItemsPM.RITUAL_CANDLE_BROWN.get())
            .addIngredient(ItemTagsPM.RITUAL_CANDLES)
            .addIngredient(Tags.Items.DYES_BROWN)
            .addCriterion("has_candle", this.hasItem(ItemTagsPM.RITUAL_CANDLES))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ItemsPM.RITUAL_CANDLE_CYAN.get())
            .addIngredient(ItemTagsPM.RITUAL_CANDLES)
            .addIngredient(Tags.Items.DYES_CYAN)
            .addCriterion("has_candle", this.hasItem(ItemTagsPM.RITUAL_CANDLES))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ItemsPM.RITUAL_CANDLE_GRAY.get())
            .addIngredient(ItemTagsPM.RITUAL_CANDLES)
            .addIngredient(Tags.Items.DYES_GRAY)
            .addCriterion("has_candle", this.hasItem(ItemTagsPM.RITUAL_CANDLES))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ItemsPM.RITUAL_CANDLE_GREEN.get())
            .addIngredient(ItemTagsPM.RITUAL_CANDLES)
            .addIngredient(Tags.Items.DYES_GREEN)
            .addCriterion("has_candle", this.hasItem(ItemTagsPM.RITUAL_CANDLES))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ItemsPM.RITUAL_CANDLE_LIGHT_BLUE.get())
            .addIngredient(ItemTagsPM.RITUAL_CANDLES)
            .addIngredient(Tags.Items.DYES_LIGHT_BLUE)
            .addCriterion("has_candle", this.hasItem(ItemTagsPM.RITUAL_CANDLES))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ItemsPM.RITUAL_CANDLE_LIGHT_GRAY.get())
            .addIngredient(ItemTagsPM.RITUAL_CANDLES)
            .addIngredient(Tags.Items.DYES_LIGHT_GRAY)
            .addCriterion("has_candle", this.hasItem(ItemTagsPM.RITUAL_CANDLES))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ItemsPM.RITUAL_CANDLE_LIME.get())
            .addIngredient(ItemTagsPM.RITUAL_CANDLES)
            .addIngredient(Tags.Items.DYES_LIME)
            .addCriterion("has_candle", this.hasItem(ItemTagsPM.RITUAL_CANDLES))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ItemsPM.RITUAL_CANDLE_MAGENTA.get())
            .addIngredient(ItemTagsPM.RITUAL_CANDLES)
            .addIngredient(Tags.Items.DYES_MAGENTA)
            .addCriterion("has_candle", this.hasItem(ItemTagsPM.RITUAL_CANDLES))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ItemsPM.RITUAL_CANDLE_ORANGE.get())
            .addIngredient(ItemTagsPM.RITUAL_CANDLES)
            .addIngredient(Tags.Items.DYES_ORANGE)
            .addCriterion("has_candle", this.hasItem(ItemTagsPM.RITUAL_CANDLES))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ItemsPM.RITUAL_CANDLE_PINK.get())
            .addIngredient(ItemTagsPM.RITUAL_CANDLES)
            .addIngredient(Tags.Items.DYES_PINK)
            .addCriterion("has_candle", this.hasItem(ItemTagsPM.RITUAL_CANDLES))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ItemsPM.RITUAL_CANDLE_PURPLE.get())
            .addIngredient(ItemTagsPM.RITUAL_CANDLES)
            .addIngredient(Tags.Items.DYES_PURPLE)
            .addCriterion("has_candle", this.hasItem(ItemTagsPM.RITUAL_CANDLES))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ItemsPM.RITUAL_CANDLE_RED.get())
            .addIngredient(ItemTagsPM.RITUAL_CANDLES)
            .addIngredient(Tags.Items.DYES_RED)
            .addCriterion("has_candle", this.hasItem(ItemTagsPM.RITUAL_CANDLES))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ItemsPM.RITUAL_CANDLE_WHITE.get())
            .addIngredient(ItemTagsPM.RITUAL_CANDLES)
            .addIngredient(Tags.Items.DYES_WHITE)
            .addCriterion("has_candle", this.hasItem(ItemTagsPM.RITUAL_CANDLES))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ItemsPM.RITUAL_CANDLE_YELLOW.get())
            .addIngredient(ItemTagsPM.RITUAL_CANDLES)
            .addIngredient(Tags.Items.DYES_YELLOW)
            .addCriterion("has_candle", this.hasItem(ItemTagsPM.RITUAL_CANDLES))
            .build(consumer);
    }
    
    protected void registerRuneRecipes(Consumer<IFinishedRecipe> consumer) {
        RunecarvingRecipeBuilder.runecarvingRecipe(ItemsPM.RUNE_UNATTUNED.get())
            .firstIngredient(Items.STONE_SLAB)
            .secondIngredient(Tags.Items.GEMS_LAPIS)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("BASIC_RUNEWORKING")))
            .build(consumer);
        ArcaneShapelessRecipeBuilder.arcaneShapelessRecipe(ItemsPM.RUNE_EARTH.get())
            .addIngredient(ItemsPM.RUNE_UNATTUNED.get())
            .addIngredient(ItemsPM.ESSENCE_DUST_EARTH.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("RUNE_EARTH")))
            .build(consumer);
        ArcaneShapelessRecipeBuilder.arcaneShapelessRecipe(ItemsPM.RUNE_SEA.get())
            .addIngredient(ItemsPM.RUNE_UNATTUNED.get())
            .addIngredient(ItemsPM.ESSENCE_DUST_SEA.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("RUNE_SEA")))
            .build(consumer);
        ArcaneShapelessRecipeBuilder.arcaneShapelessRecipe(ItemsPM.RUNE_SKY.get())
            .addIngredient(ItemsPM.RUNE_UNATTUNED.get())
            .addIngredient(ItemsPM.ESSENCE_DUST_SKY.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("RUNE_SKY")))
            .build(consumer);
        ArcaneShapelessRecipeBuilder.arcaneShapelessRecipe(ItemsPM.RUNE_SUN.get())
            .addIngredient(ItemsPM.RUNE_UNATTUNED.get())
            .addIngredient(ItemsPM.ESSENCE_DUST_SUN.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("RUNE_SUN")))
            .build(consumer);
        ArcaneShapelessRecipeBuilder.arcaneShapelessRecipe(ItemsPM.RUNE_MOON.get())
            .addIngredient(ItemsPM.RUNE_UNATTUNED.get())
            .addIngredient(ItemsPM.ESSENCE_DUST_MOON.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("RUNE_MOON")))
            .build(consumer);
        RunecarvingRecipeBuilder.runecarvingRecipe(ItemsPM.RUNE_PROJECT.get())
            .firstIngredient(Items.STONE_SLAB)
            .secondIngredient(Tags.Items.GEMS_LAPIS)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("RUNE_PROJECT")))
            .build(consumer);
        RunecarvingRecipeBuilder.runecarvingRecipe(ItemsPM.RUNE_PROTECT.get())
            .firstIngredient(Items.STONE_SLAB)
            .secondIngredient(Tags.Items.GEMS_LAPIS)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("RUNE_PROTECT")))
            .build(consumer);
        RunecarvingRecipeBuilder.runecarvingRecipe(ItemsPM.RUNE_ITEM.get())
            .firstIngredient(Items.STONE_SLAB)
            .secondIngredient(Tags.Items.GEMS_LAPIS)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("RUNE_ITEM")))
            .build(consumer);
        RunecarvingRecipeBuilder.runecarvingRecipe(ItemsPM.RUNE_SELF.get())
            .firstIngredient(Items.STONE_SLAB)
            .secondIngredient(Tags.Items.GEMS_LAPIS)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("RUNE_SELF")))
            .build(consumer);
        ArcaneShapelessRecipeBuilder.arcaneShapelessRecipe(ItemsPM.RUNE_BLOOD.get())
            .addIngredient(ItemsPM.RUNE_UNATTUNED.get())
            .addIngredient(ItemsPM.ESSENCE_DUST_BLOOD.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("RUNE_BLOOD")))
            .build(consumer);
        ArcaneShapelessRecipeBuilder.arcaneShapelessRecipe(ItemsPM.RUNE_INFERNAL.get())
            .addIngredient(ItemsPM.RUNE_UNATTUNED.get())
            .addIngredient(ItemsPM.ESSENCE_DUST_INFERNAL.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("RUNE_INFERNAL")))
            .build(consumer);
        ArcaneShapelessRecipeBuilder.arcaneShapelessRecipe(ItemsPM.RUNE_VOID.get())
            .addIngredient(ItemsPM.RUNE_UNATTUNED.get())
            .addIngredient(ItemsPM.ESSENCE_DUST_VOID.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("RUNE_VOID")))
            .build(consumer);
        RunecarvingRecipeBuilder.runecarvingRecipe(ItemsPM.RUNE_ABSORB.get())
            .firstIngredient(Items.STONE_SLAB)
            .secondIngredient(Tags.Items.GEMS_LAPIS)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("RUNE_ABSORB")))
            .build(consumer);
        RunecarvingRecipeBuilder.runecarvingRecipe(ItemsPM.RUNE_DISPEL.get())
            .firstIngredient(Items.STONE_SLAB)
            .secondIngredient(Tags.Items.GEMS_LAPIS)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("RUNE_DISPEL")))
            .build(consumer);
        RunecarvingRecipeBuilder.runecarvingRecipe(ItemsPM.RUNE_SUMMON.get())
            .firstIngredient(Items.STONE_SLAB)
            .secondIngredient(Tags.Items.GEMS_LAPIS)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("RUNE_SUMMON")))
            .build(consumer);
        RunecarvingRecipeBuilder.runecarvingRecipe(ItemsPM.RUNE_AREA.get())
            .firstIngredient(Items.STONE_SLAB)
            .secondIngredient(Tags.Items.GEMS_LAPIS)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("RUNE_AREA")))
            .build(consumer);
        RunecarvingRecipeBuilder.runecarvingRecipe(ItemsPM.RUNE_CREATURE.get())
            .firstIngredient(Items.STONE_SLAB)
            .secondIngredient(Tags.Items.GEMS_LAPIS)
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("RUNE_CREATURE")))
            .build(consumer);
        ArcaneShapelessRecipeBuilder.arcaneShapelessRecipe(ItemsPM.RUNE_HALLOWED.get())
            .addIngredient(ItemsPM.RUNE_UNATTUNED.get())
            .addIngredient(ItemsPM.ESSENCE_DUST_HALLOWED.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("RUNE_HALLOWED")))
            .build(consumer);
        ArcaneShapelessRecipeBuilder.arcaneShapelessRecipe(ItemsPM.RUNE_POWER.get())
            .addIngredient(ItemsPM.RUNE_UNATTUNED.get())
            .addIngredient(ItemsPM.ESSENCE_CRYSTAL_EARTH.get())
            .addIngredient(ItemsPM.ESSENCE_CRYSTAL_SEA.get())
            .addIngredient(ItemsPM.ESSENCE_CRYSTAL_SKY.get())
            .addIngredient(ItemsPM.ESSENCE_CRYSTAL_SUN.get())
            .addIngredient(ItemsPM.ESSENCE_CRYSTAL_MOON.get())
            .addIngredient(ItemsPM.ESSENCE_CRYSTAL_BLOOD.get())
            .addIngredient(ItemsPM.ESSENCE_CRYSTAL_INFERNAL.get())
            .addIngredient(ItemsPM.ESSENCE_CRYSTAL_VOID.get())
            .research(CompoundResearchKey.from(SimpleResearchKey.parse("RUNE_POWER")))
            .build(consumer);
    }
}
