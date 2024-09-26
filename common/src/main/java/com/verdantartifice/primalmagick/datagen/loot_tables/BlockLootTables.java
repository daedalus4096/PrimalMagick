package com.verdantartifice.primalmagick.datagen.loot_tables;

import java.util.OptionalInt;

import com.verdantartifice.primalmagick.common.blocks.BlockRegistration;
import com.verdantartifice.primalmagick.common.items.ItemsPM;

import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

/**
 * Data provider for all of the mod's block loot tables.
 * 
 * @author Daedalus4096
 */
public class BlockLootTables extends AbstractBlockLootTableProvider {
    public BlockLootTables(HolderLookup.Provider registries) {
        super(registries);
    }

    @Override
    protected void generate() {
        // Mark blocks as not having a loot table
        this.registerEmptyLootTables();
        
        // Register groups of tables
        this.registerMarbleLootTables();
        this.registerEnchantedMarbleLootTables();
        this.registerSmokedMarbleLootTables();
        this.registerHallowedMarbleLootTables();
        this.registerSunwoodLootTables();
        this.registerMoonwoodLootTables();
        this.registerHallowoodLootTables();
        this.registerCropLootTables();
        this.registerInfusedStoneLootTables();
        this.registerSkyglassLootTables();
        this.registerRitualCandleLootTables();
        this.registerManaFontLootTables();
        this.registerBuddingGemLootTables();
        
        // Register device loot tables
        this.registerBasicTable(BlocksPM.get(BlocksPM.ARCANE_WORKBENCH));
        this.registerBasicTable(BlocksPM.get(BlocksPM.WAND_ASSEMBLY_TABLE));
        this.registerBasicTable(BlocksPM.get(BlocksPM.WOOD_TABLE));
        this.registerBasicTable(BlocksPM.get(BlocksPM.ANALYSIS_TABLE));
        this.registerBasicTable(BlocksPM.get(BlocksPM.ESSENCE_FURNACE));
        this.registerBasicTable(BlocksPM.get(BlocksPM.CALCINATOR_BASIC));
        this.registerBasicTable(BlocksPM.get(BlocksPM.CALCINATOR_ENCHANTED));
        this.registerBasicTable(BlocksPM.get(BlocksPM.CALCINATOR_FORBIDDEN));
        this.registerBasicTable(BlocksPM.get(BlocksPM.CALCINATOR_HEAVENLY));
        this.registerBasicTable(BlocksPM.get(BlocksPM.WAND_INSCRIPTION_TABLE));
        this.registerBasicTable(BlocksPM.get(BlocksPM.SPELLCRAFTING_ALTAR));
        this.registerBasicTable(BlocksPM.get(BlocksPM.WAND_CHARGER));
        this.registerBasicTable(BlocksPM.get(BlocksPM.RESEARCH_TABLE));
        this.registerBasicTable(BlocksPM.get(BlocksPM.SUNLAMP));
        this.registerBasicTable(BlocksPM.get(BlocksPM.SPIRIT_LANTERN));
        this.registerBasicTable(BlocksPM.get(BlocksPM.RITUAL_ALTAR));
        this.registerBasicTable(BlocksPM.get(BlocksPM.OFFERING_PEDESTAL));
        this.registerBasicTable(BlocksPM.get(BlocksPM.INCENSE_BRAZIER));
        this.registerBasicTable(BlocksPM.get(BlocksPM.RITUAL_LECTERN));
        this.registerBasicTable(BlocksPM.get(BlocksPM.RITUAL_BELL));
        this.registerBasicTable(BlocksPM.get(BlocksPM.BLOODLETTER));
        this.registerBasicTable(BlocksPM.get(BlocksPM.SOUL_ANVIL));
        this.registerBasicTable(BlocksPM.get(BlocksPM.RUNIC_GRINDSTONE));
        this.registerManaBearingDeviceTable(BlocksPM.get(BlocksPM.HONEY_EXTRACTOR));
        this.registerBasicTable(BlocksPM.get(BlocksPM.PRIMALITE_GOLEM_CONTROLLER));
        this.registerBasicTable(BlocksPM.get(BlocksPM.HEXIUM_GOLEM_CONTROLLER));
        this.registerBasicTable(BlocksPM.get(BlocksPM.HALLOWSTEEL_GOLEM_CONTROLLER));
        this.registerBasicTable(BlocksPM.get(BlocksPM.SANGUINE_CRUCIBLE));
        this.registerManaBearingDeviceTable(BlocksPM.get(BlocksPM.CONCOCTER));
        this.registerBasicTable(BlocksPM.get(BlocksPM.RUNECARVING_TABLE));
        this.registerBasicTable(BlocksPM.get(BlocksPM.RUNESCRIBING_ALTAR_BASIC));
        this.registerBasicTable(BlocksPM.get(BlocksPM.RUNESCRIBING_ALTAR_ENCHANTED));
        this.registerBasicTable(BlocksPM.get(BlocksPM.RUNESCRIBING_ALTAR_FORBIDDEN));
        this.registerBasicTable(BlocksPM.get(BlocksPM.RUNESCRIBING_ALTAR_HEAVENLY));
        this.registerBasicTable(BlocksPM.get(BlocksPM.CELESTIAL_HARP));
        this.registerBasicTable(BlocksPM.get(BlocksPM.ENTROPY_SINK));
        this.registerBasicTable(BlocksPM.get(BlocksPM.AUTO_CHARGER));
        this.registerManaBearingDeviceTable(BlocksPM.get(BlocksPM.ESSENCE_TRANSMUTER));
        this.registerManaBearingDeviceTable(BlocksPM.get(BlocksPM.DISSOLUTION_CHAMBER));
        this.registerBasicTable(BlocksPM.get(BlocksPM.ZEPHYR_ENGINE));
        this.registerBasicTable(BlocksPM.get(BlocksPM.VOID_TURBINE));
        this.registerBasicTable(BlocksPM.get(BlocksPM.ESSENCE_CASK_ENCHANTED));
        this.registerBasicTable(BlocksPM.get(BlocksPM.ESSENCE_CASK_FORBIDDEN));
        this.registerBasicTable(BlocksPM.get(BlocksPM.ESSENCE_CASK_HEAVENLY));
        this.registerBasicTable(BlocksPM.get(BlocksPM.WAND_GLAMOUR_TABLE));
        this.registerManaBearingDeviceTable(BlocksPM.get(BlocksPM.INFERNAL_FURNACE));
        this.registerManaBearingDeviceTable(BlocksPM.get(BlocksPM.MANA_NEXUS));
        this.registerManaBearingDeviceTable(BlocksPM.get(BlocksPM.MANA_SINGULARITY));
        this.registerBasicTable(BlocksPM.get(BlocksPM.MANA_SINGULARITY_CREATIVE));
        this.registerBasicTable(BlocksPM.get(BlocksPM.SCRIBE_TABLE));

        // Register misc loot tables
        this.registerBasicTable(BlocksPM.get(BlocksPM.SALT_TRAIL));
        this.registerMultiGemOreTable(BlocksPM.get(BlocksPM.ROCK_SALT_ORE), ItemsPM.ROCK_SALT.get(), 3.0F, 4.0F);
        this.registerGemOreTable(BlocksPM.get(BlocksPM.QUARTZ_ORE), Items.QUARTZ);
        this.registerBasicTable(BlocksPM.get(BlocksPM.PRIMALITE_BLOCK));
        this.registerBasicTable(BlocksPM.get(BlocksPM.HEXIUM_BLOCK));
        this.registerBasicTable(BlocksPM.get(BlocksPM.HALLOWSTEEL_BLOCK));
        this.registerBasicTable(BlocksPM.get(BlocksPM.IGNYX_BLOCK));
        this.registerBasicTable(BlocksPM.get(BlocksPM.SALT_BLOCK));
        this.registerBasicTable(BlocksPM.get(BlocksPM.TREEFOLK_SPROUT));
        this.registerBasicTable(BlocksPM.get(BlocksPM.ENDERWARD));
    }

    private void registerEmptyLootTables() {
        this.registerEmptyTable(BlocksPM.get(BlocksPM.ANCIENT_FONT_EARTH));
        this.registerEmptyTable(BlocksPM.get(BlocksPM.ANCIENT_FONT_SEA));
        this.registerEmptyTable(BlocksPM.get(BlocksPM.ANCIENT_FONT_SKY));
        this.registerEmptyTable(BlocksPM.get(BlocksPM.ANCIENT_FONT_SUN));
        this.registerEmptyTable(BlocksPM.get(BlocksPM.ANCIENT_FONT_MOON));
        this.registerEmptyTable(BlocksPM.get(BlocksPM.GLOW_FIELD));
        this.registerEmptyTable(BlocksPM.get(BlocksPM.SOUL_GLOW_FIELD));
        this.registerEmptyTable(BlocksPM.get(BlocksPM.CONSECRATION_FIELD));
    }

    private void registerMarbleLootTables() {
        this.registerBasicTable(BlocksPM.get(BlocksPM.MARBLE_RAW));
        this.registerSlabTable(BlocksPM.get(BlocksPM.MARBLE_BRICK_SLAB));
        this.registerBasicTable(BlocksPM.get(BlocksPM.MARBLE_BRICK_STAIRS));
        this.registerBasicTable(BlocksPM.get(BlocksPM.MARBLE_BRICK_WALL));
        this.registerBasicTable(BlocksPM.get(BlocksPM.MARBLE_BRICKS));
        this.registerBasicTable(BlocksPM.get(BlocksPM.MARBLE_CHISELED));
        this.registerBasicTable(BlocksPM.get(BlocksPM.MARBLE_PILLAR));
        this.registerBasicTable(BlocksPM.get(BlocksPM.MARBLE_RUNED));
        this.registerSlabTable(BlocksPM.get(BlocksPM.MARBLE_SLAB));
        this.registerBasicTable(BlocksPM.get(BlocksPM.MARBLE_STAIRS));
        this.registerBasicTable(BlocksPM.get(BlocksPM.MARBLE_WALL));
        this.registerBasicTable(BlocksPM.get(BlocksPM.MARBLE_TILES));
        this.registerBasicTable(BlocksPM.get(BlocksPM.MARBLE_BOOKSHELF));
    }
    
    private void registerEnchantedMarbleLootTables() {
        this.registerBasicTable(BlocksPM.get(BlocksPM.MARBLE_ENCHANTED));
        this.registerSlabTable(BlocksPM.get(BlocksPM.MARBLE_ENCHANTED_BRICK_SLAB));
        this.registerBasicTable(BlocksPM.get(BlocksPM.MARBLE_ENCHANTED_BRICK_STAIRS));
        this.registerBasicTable(BlocksPM.get(BlocksPM.MARBLE_ENCHANTED_BRICK_WALL));
        this.registerBasicTable(BlocksPM.get(BlocksPM.MARBLE_ENCHANTED_BRICKS));
        this.registerBasicTable(BlocksPM.get(BlocksPM.MARBLE_ENCHANTED_CHISELED));
        this.registerBasicTable(BlocksPM.get(BlocksPM.MARBLE_ENCHANTED_PILLAR));
        this.registerBasicTable(BlocksPM.get(BlocksPM.MARBLE_ENCHANTED_RUNED));
        this.registerSlabTable(BlocksPM.get(BlocksPM.MARBLE_ENCHANTED_SLAB));
        this.registerBasicTable(BlocksPM.get(BlocksPM.MARBLE_ENCHANTED_STAIRS));
        this.registerBasicTable(BlocksPM.get(BlocksPM.MARBLE_ENCHANTED_WALL));
        this.registerBasicTable(BlocksPM.get(BlocksPM.MARBLE_ENCHANTED_BOOKSHELF));
    }
    
    private void registerSmokedMarbleLootTables() {
        this.registerBasicTable(BlocksPM.get(BlocksPM.MARBLE_SMOKED));
        this.registerSlabTable(BlocksPM.get(BlocksPM.MARBLE_SMOKED_BRICK_SLAB));
        this.registerBasicTable(BlocksPM.get(BlocksPM.MARBLE_SMOKED_BRICK_STAIRS));
        this.registerBasicTable(BlocksPM.get(BlocksPM.MARBLE_SMOKED_BRICK_WALL));
        this.registerBasicTable(BlocksPM.get(BlocksPM.MARBLE_SMOKED_BRICKS));
        this.registerBasicTable(BlocksPM.get(BlocksPM.MARBLE_SMOKED_CHISELED));
        this.registerBasicTable(BlocksPM.get(BlocksPM.MARBLE_SMOKED_PILLAR));
        this.registerBasicTable(BlocksPM.get(BlocksPM.MARBLE_SMOKED_RUNED));
        this.registerSlabTable(BlocksPM.get(BlocksPM.MARBLE_SMOKED_SLAB));
        this.registerBasicTable(BlocksPM.get(BlocksPM.MARBLE_SMOKED_STAIRS));
        this.registerBasicTable(BlocksPM.get(BlocksPM.MARBLE_SMOKED_WALL));
        this.registerBasicTable(BlocksPM.get(BlocksPM.MARBLE_SMOKED_BOOKSHELF));
    }
    
    private void registerHallowedMarbleLootTables() {
        this.registerBasicTable(BlocksPM.get(BlocksPM.MARBLE_HALLOWED));
        this.registerSlabTable(BlocksPM.get(BlocksPM.MARBLE_HALLOWED_BRICK_SLAB));
        this.registerBasicTable(BlocksPM.get(BlocksPM.MARBLE_HALLOWED_BRICK_STAIRS));
        this.registerBasicTable(BlocksPM.get(BlocksPM.MARBLE_HALLOWED_BRICK_WALL));
        this.registerBasicTable(BlocksPM.get(BlocksPM.MARBLE_HALLOWED_BRICKS));
        this.registerBasicTable(BlocksPM.get(BlocksPM.MARBLE_HALLOWED_CHISELED));
        this.registerBasicTable(BlocksPM.get(BlocksPM.MARBLE_HALLOWED_PILLAR));
        this.registerBasicTable(BlocksPM.get(BlocksPM.MARBLE_HALLOWED_RUNED));
        this.registerSlabTable(BlocksPM.get(BlocksPM.MARBLE_HALLOWED_SLAB));
        this.registerBasicTable(BlocksPM.get(BlocksPM.MARBLE_HALLOWED_STAIRS));
        this.registerBasicTable(BlocksPM.get(BlocksPM.MARBLE_HALLOWED_WALL));
        this.registerBasicTable(BlocksPM.get(BlocksPM.MARBLE_HALLOWED_BOOKSHELF));
    }
    
    private void registerSunwoodLootTables() {
        this.registerPulsingLogTable(BlocksPM.get(BlocksPM.SUNWOOD_LOG));
        this.registerPulsingLogTable(BlocksPM.get(BlocksPM.STRIPPED_SUNWOOD_LOG));
        this.registerPulsingLogTable(BlocksPM.get(BlocksPM.SUNWOOD_WOOD));
        this.registerPulsingLogTable(BlocksPM.get(BlocksPM.STRIPPED_SUNWOOD_WOOD));
        this.registerLeavesTable(BlocksPM.get(BlocksPM.SUNWOOD_LEAVES), BlocksPM.get(BlocksPM.SUNWOOD_SAPLING), new float[] { 0.1F, 0.125F, 0.16666667F, 0.2F });
        this.registerBasicTable(BlocksPM.get(BlocksPM.SUNWOOD_SAPLING));
        this.registerBasicTable(BlocksPM.get(BlocksPM.SUNWOOD_PLANKS));
        this.registerSlabTable(BlocksPM.get(BlocksPM.SUNWOOD_SLAB));
        this.registerBasicTable(BlocksPM.get(BlocksPM.SUNWOOD_STAIRS));
        this.registerBasicTable(BlocksPM.get(BlocksPM.SUNWOOD_PILLAR));
    }
    
    private void registerMoonwoodLootTables() {
        this.registerPulsingLogTable(BlocksPM.get(BlocksPM.MOONWOOD_LOG));
        this.registerPulsingLogTable(BlocksPM.get(BlocksPM.STRIPPED_MOONWOOD_LOG));
        this.registerPulsingLogTable(BlocksPM.get(BlocksPM.MOONWOOD_WOOD));
        this.registerPulsingLogTable(BlocksPM.get(BlocksPM.STRIPPED_MOONWOOD_WOOD));
        this.registerLeavesTable(BlocksPM.get(BlocksPM.MOONWOOD_LEAVES), BlocksPM.get(BlocksPM.MOONWOOD_SAPLING), new float[] { 0.1F, 0.125F, 0.16666667F, 0.2F });
        this.registerBasicTable(BlocksPM.get(BlocksPM.MOONWOOD_SAPLING));
        this.registerBasicTable(BlocksPM.get(BlocksPM.MOONWOOD_PLANKS));
        this.registerSlabTable(BlocksPM.get(BlocksPM.MOONWOOD_SLAB));
        this.registerBasicTable(BlocksPM.get(BlocksPM.MOONWOOD_STAIRS));
        this.registerBasicTable(BlocksPM.get(BlocksPM.MOONWOOD_PILLAR));
    }
    
    private void registerHallowoodLootTables() {
        this.registerBasicTable(BlocksPM.get(BlocksPM.HALLOWOOD_LOG));
        this.registerBasicTable(BlocksPM.get(BlocksPM.STRIPPED_HALLOWOOD_LOG));
        this.registerBasicTable(BlocksPM.get(BlocksPM.HALLOWOOD_WOOD));
        this.registerBasicTable(BlocksPM.get(BlocksPM.STRIPPED_HALLOWOOD_WOOD));
        this.registerLeavesTable(BlocksPM.get(BlocksPM.HALLOWOOD_LEAVES), BlocksPM.get(BlocksPM.HALLOWOOD_SAPLING), new float[] { 0.15F, 0.1875F, 0.25F, 0.3F });
        this.registerBasicTable(BlocksPM.get(BlocksPM.HALLOWOOD_SAPLING));
        this.registerBasicTable(BlocksPM.get(BlocksPM.HALLOWOOD_PLANKS));
        this.registerSlabTable(BlocksPM.get(BlocksPM.HALLOWOOD_SLAB));
        this.registerBasicTable(BlocksPM.get(BlocksPM.HALLOWOOD_STAIRS));
        this.registerBasicTable(BlocksPM.get(BlocksPM.HALLOWOOD_PILLAR));
    }
    
    private void registerCropLootTables() {
        this.registerSplittingTable(BlocksPM.get(BlocksPM.HYDROMELON), ItemsPM.HYDROMELON_SLICE.get(), UniformGenerator.between(3F, 7F), OptionalInt.of(9));
        this.registerLootTableBuilder(BlocksPM.get(BlocksPM.HYRDOMELON_STEM), b -> this.createStemDrops(b, ItemsPM.HYDROMELON_SEEDS.get()));
        this.registerLootTableBuilder(BlocksPM.get(BlocksPM.ATTACHED_HYDROMELON_STEM), b -> this.createAttachedStemDrops(b, ItemsPM.HYDROMELON_SEEDS.get()));
        this.registerLootTableBuilder(BlocksPM.get(BlocksPM.BLOOD_ROSE), b -> this.createSinglePropConditionTable(b, BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER));
        this.registerLootTableBuilder(BlocksPM.get(BlocksPM.EMBERFLOWER), b -> this.createSinglePropConditionTable(b, BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER));
    }
    
    private void registerInfusedStoneLootTables() {
        this.registerInfusedStoneTable(BlocksPM.get(BlocksPM.INFUSED_STONE_EARTH), ItemsPM.ESSENCE_DUST_EARTH.get());
        this.registerInfusedStoneTable(BlocksPM.get(BlocksPM.INFUSED_STONE_SEA), ItemsPM.ESSENCE_DUST_SEA.get());
        this.registerInfusedStoneTable(BlocksPM.get(BlocksPM.INFUSED_STONE_SKY), ItemsPM.ESSENCE_DUST_SKY.get());
        this.registerInfusedStoneTable(BlocksPM.get(BlocksPM.INFUSED_STONE_SUN), ItemsPM.ESSENCE_DUST_SUN.get());
        this.registerInfusedStoneTable(BlocksPM.get(BlocksPM.INFUSED_STONE_MOON), ItemsPM.ESSENCE_DUST_MOON.get());
    }
    
    private void registerSkyglassLootTables() {
        this.registerBasicTable(BlocksPM.get(BlocksPM.SKYGLASS));
        this.registerBasicTable(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_BLACK));
        this.registerBasicTable(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_BLUE));
        this.registerBasicTable(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_BROWN));
        this.registerBasicTable(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_CYAN));
        this.registerBasicTable(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_GRAY));
        this.registerBasicTable(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_GREEN));
        this.registerBasicTable(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_LIGHT_BLUE));
        this.registerBasicTable(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_LIGHT_GRAY));
        this.registerBasicTable(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_LIME));
        this.registerBasicTable(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_MAGENTA));
        this.registerBasicTable(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_ORANGE));
        this.registerBasicTable(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PINK));
        this.registerBasicTable(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PURPLE));
        this.registerBasicTable(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_RED));
        this.registerBasicTable(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_WHITE));
        this.registerBasicTable(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_YELLOW));
        
        this.registerBasicTable(BlocksPM.get(BlocksPM.SKYGLASS_PANE));
        this.registerBasicTable(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_BLACK));
        this.registerBasicTable(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_BLUE));
        this.registerBasicTable(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_BROWN));
        this.registerBasicTable(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_CYAN));
        this.registerBasicTable(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_GRAY));
        this.registerBasicTable(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_GREEN));
        this.registerBasicTable(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_LIGHT_BLUE));
        this.registerBasicTable(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_LIGHT_GRAY));
        this.registerBasicTable(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_LIME));
        this.registerBasicTable(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_MAGENTA));
        this.registerBasicTable(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_ORANGE));
        this.registerBasicTable(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_PINK));
        this.registerBasicTable(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_PURPLE));
        this.registerBasicTable(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_RED));
        this.registerBasicTable(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_WHITE));
        this.registerBasicTable(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_YELLOW));
    }
    
    private void registerRitualCandleLootTables() {
        this.registerBasicTable(BlocksPM.get(BlocksPM.RITUAL_CANDLE_BLACK));
        this.registerBasicTable(BlocksPM.get(BlocksPM.RITUAL_CANDLE_BLUE));
        this.registerBasicTable(BlocksPM.get(BlocksPM.RITUAL_CANDLE_BROWN));
        this.registerBasicTable(BlocksPM.get(BlocksPM.RITUAL_CANDLE_CYAN));
        this.registerBasicTable(BlocksPM.get(BlocksPM.RITUAL_CANDLE_GRAY));
        this.registerBasicTable(BlocksPM.get(BlocksPM.RITUAL_CANDLE_GREEN));
        this.registerBasicTable(BlocksPM.get(BlocksPM.RITUAL_CANDLE_LIGHT_BLUE));
        this.registerBasicTable(BlocksPM.get(BlocksPM.RITUAL_CANDLE_LIGHT_GRAY));
        this.registerBasicTable(BlocksPM.get(BlocksPM.RITUAL_CANDLE_LIME));
        this.registerBasicTable(BlocksPM.get(BlocksPM.RITUAL_CANDLE_MAGENTA));
        this.registerBasicTable(BlocksPM.get(BlocksPM.RITUAL_CANDLE_ORANGE));
        this.registerBasicTable(BlocksPM.get(BlocksPM.RITUAL_CANDLE_PINK));
        this.registerBasicTable(BlocksPM.get(BlocksPM.RITUAL_CANDLE_PURPLE));
        this.registerBasicTable(BlocksPM.get(BlocksPM.RITUAL_CANDLE_RED));
        this.registerBasicTable(BlocksPM.get(BlocksPM.RITUAL_CANDLE_WHITE));
        this.registerBasicTable(BlocksPM.get(BlocksPM.RITUAL_CANDLE_YELLOW));
    }

    private void registerManaFontLootTables() {
        this.registerBasicTable(BlocksPM.get(BlocksPM.ARTIFICIAL_FONT_EARTH));
        this.registerBasicTable(BlocksPM.get(BlocksPM.ARTIFICIAL_FONT_SEA));
        this.registerBasicTable(BlocksPM.get(BlocksPM.ARTIFICIAL_FONT_SKY));
        this.registerBasicTable(BlocksPM.get(BlocksPM.ARTIFICIAL_FONT_SUN));
        this.registerBasicTable(BlocksPM.get(BlocksPM.ARTIFICIAL_FONT_MOON));
        this.registerBasicTable(BlocksPM.get(BlocksPM.ARTIFICIAL_FONT_BLOOD));
        this.registerBasicTable(BlocksPM.get(BlocksPM.ARTIFICIAL_FONT_INFERNAL));
        this.registerBasicTable(BlocksPM.get(BlocksPM.ARTIFICIAL_FONT_VOID));
        this.registerBasicTable(BlocksPM.get(BlocksPM.ARTIFICIAL_FONT_HALLOWED));
        this.registerBasicTable(BlocksPM.get(BlocksPM.FORBIDDEN_FONT_EARTH));
        this.registerBasicTable(BlocksPM.get(BlocksPM.FORBIDDEN_FONT_SEA));
        this.registerBasicTable(BlocksPM.get(BlocksPM.FORBIDDEN_FONT_SKY));
        this.registerBasicTable(BlocksPM.get(BlocksPM.FORBIDDEN_FONT_SUN));
        this.registerBasicTable(BlocksPM.get(BlocksPM.FORBIDDEN_FONT_MOON));
        this.registerBasicTable(BlocksPM.get(BlocksPM.FORBIDDEN_FONT_BLOOD));
        this.registerBasicTable(BlocksPM.get(BlocksPM.FORBIDDEN_FONT_INFERNAL));
        this.registerBasicTable(BlocksPM.get(BlocksPM.FORBIDDEN_FONT_VOID));
        this.registerBasicTable(BlocksPM.get(BlocksPM.FORBIDDEN_FONT_HALLOWED));
        this.registerBasicTable(BlocksPM.get(BlocksPM.HEAVENLY_FONT_EARTH));
        this.registerBasicTable(BlocksPM.get(BlocksPM.HEAVENLY_FONT_SEA));
        this.registerBasicTable(BlocksPM.get(BlocksPM.HEAVENLY_FONT_SKY));
        this.registerBasicTable(BlocksPM.get(BlocksPM.HEAVENLY_FONT_SUN));
        this.registerBasicTable(BlocksPM.get(BlocksPM.HEAVENLY_FONT_MOON));
        this.registerBasicTable(BlocksPM.get(BlocksPM.HEAVENLY_FONT_BLOOD));
        this.registerBasicTable(BlocksPM.get(BlocksPM.HEAVENLY_FONT_INFERNAL));
        this.registerBasicTable(BlocksPM.get(BlocksPM.HEAVENLY_FONT_VOID));
        this.registerBasicTable(BlocksPM.get(BlocksPM.HEAVENLY_FONT_HALLOWED));
    }
    
    private void registerBuddingGemLootTables() {
        Holder<Enchantment> fortuneHolder = this.registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE);
        this.registerLootTableBuilder(BlocksPM.get(BlocksPM.SYNTHETIC_AMETHYST_CLUSTER), b -> createSilkTouchDispatchTable(b, LootItem.lootTableItem(Items.AMETHYST_SHARD).apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F))).apply(ApplyBonusCount.addOreBonusCount(fortuneHolder)).when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.CLUSTER_MAX_HARVESTABLES))).otherwise(this.applyExplosionDecay(b, LootItem.lootTableItem(Items.AMETHYST_SHARD).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))))));
        this.dropWhenSilkTouch(BlocksPM.get(BlocksPM.LARGE_SYNTHETIC_AMETHYST_BUD));
        this.dropWhenSilkTouch(BlocksPM.get(BlocksPM.MEDIUM_SYNTHETIC_AMETHYST_BUD));
        this.dropWhenSilkTouch(BlocksPM.get(BlocksPM.SMALL_SYNTHETIC_AMETHYST_BUD));
        this.registerLootTableBuilder(BlocksPM.get(BlocksPM.DAMAGED_BUDDING_AMETHYST_BLOCK), b -> createSilkTouchDispatchTable(b, LootItem.lootTableItem(Items.AMETHYST_BLOCK)));
        this.registerLootTableBuilder(BlocksPM.get(BlocksPM.CHIPPED_BUDDING_AMETHYST_BLOCK), b -> createSilkTouchDispatchTable(b, LootItem.lootTableItem(ItemsPM.DAMAGED_BUDDING_AMETHYST_BLOCK.get())));
        this.registerLootTableBuilder(BlocksPM.get(BlocksPM.FLAWED_BUDDING_AMETHYST_BLOCK), b -> createSilkTouchDispatchTable(b, LootItem.lootTableItem(ItemsPM.CHIPPED_BUDDING_AMETHYST_BLOCK.get())));
        this.registerLootTableBuilder(BlocksPM.get(BlocksPM.SYNTHETIC_DIAMOND_CLUSTER), b -> createSilkTouchDispatchTable(b, LootItem.lootTableItem(Items.DIAMOND).apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F))).apply(ApplyBonusCount.addOreBonusCount(fortuneHolder)).when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.CLUSTER_MAX_HARVESTABLES))).otherwise(this.applyExplosionDecay(b, LootItem.lootTableItem(Items.DIAMOND).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))))));
        this.dropWhenSilkTouch(BlocksPM.get(BlocksPM.LARGE_SYNTHETIC_DIAMOND_BUD));
        this.dropWhenSilkTouch(BlocksPM.get(BlocksPM.MEDIUM_SYNTHETIC_DIAMOND_BUD));
        this.dropWhenSilkTouch(BlocksPM.get(BlocksPM.SMALL_SYNTHETIC_DIAMOND_BUD));
        this.registerLootTableBuilder(BlocksPM.get(BlocksPM.DAMAGED_BUDDING_DIAMOND_BLOCK), b -> createSilkTouchDispatchTable(b, LootItem.lootTableItem(Items.DIAMOND_BLOCK)));
        this.registerLootTableBuilder(BlocksPM.get(BlocksPM.CHIPPED_BUDDING_DIAMOND_BLOCK), b -> createSilkTouchDispatchTable(b, LootItem.lootTableItem(ItemsPM.DAMAGED_BUDDING_DIAMOND_BLOCK.get())));
        this.registerLootTableBuilder(BlocksPM.get(BlocksPM.FLAWED_BUDDING_DIAMOND_BLOCK), b -> createSilkTouchDispatchTable(b, LootItem.lootTableItem(ItemsPM.CHIPPED_BUDDING_DIAMOND_BLOCK.get())));
        this.registerLootTableBuilder(BlocksPM.get(BlocksPM.SYNTHETIC_EMERALD_CLUSTER), b -> createSilkTouchDispatchTable(b, LootItem.lootTableItem(Items.EMERALD).apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F))).apply(ApplyBonusCount.addOreBonusCount(fortuneHolder)).when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.CLUSTER_MAX_HARVESTABLES))).otherwise(this.applyExplosionDecay(b, LootItem.lootTableItem(Items.EMERALD).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))))));
        this.dropWhenSilkTouch(BlocksPM.get(BlocksPM.LARGE_SYNTHETIC_EMERALD_BUD));
        this.dropWhenSilkTouch(BlocksPM.get(BlocksPM.MEDIUM_SYNTHETIC_EMERALD_BUD));
        this.dropWhenSilkTouch(BlocksPM.get(BlocksPM.SMALL_SYNTHETIC_EMERALD_BUD));
        this.registerLootTableBuilder(BlocksPM.get(BlocksPM.DAMAGED_BUDDING_EMERALD_BLOCK), b -> createSilkTouchDispatchTable(b, LootItem.lootTableItem(Items.EMERALD_BLOCK)));
        this.registerLootTableBuilder(BlocksPM.get(BlocksPM.CHIPPED_BUDDING_EMERALD_BLOCK), b -> createSilkTouchDispatchTable(b, LootItem.lootTableItem(ItemsPM.DAMAGED_BUDDING_EMERALD_BLOCK.get())));
        this.registerLootTableBuilder(BlocksPM.get(BlocksPM.FLAWED_BUDDING_EMERALD_BLOCK), b -> createSilkTouchDispatchTable(b, LootItem.lootTableItem(ItemsPM.CHIPPED_BUDDING_EMERALD_BLOCK.get())));
        this.registerLootTableBuilder(BlocksPM.get(BlocksPM.SYNTHETIC_QUARTZ_CLUSTER), b -> createSilkTouchDispatchTable(b, LootItem.lootTableItem(Items.QUARTZ).apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F))).apply(ApplyBonusCount.addOreBonusCount(fortuneHolder)).when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.CLUSTER_MAX_HARVESTABLES))).otherwise(this.applyExplosionDecay(b, LootItem.lootTableItem(Items.QUARTZ).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))))));
        this.dropWhenSilkTouch(BlocksPM.get(BlocksPM.LARGE_SYNTHETIC_QUARTZ_BUD));
        this.dropWhenSilkTouch(BlocksPM.get(BlocksPM.MEDIUM_SYNTHETIC_QUARTZ_BUD));
        this.dropWhenSilkTouch(BlocksPM.get(BlocksPM.SMALL_SYNTHETIC_QUARTZ_BUD));
        this.registerLootTableBuilder(BlocksPM.get(BlocksPM.DAMAGED_BUDDING_QUARTZ_BLOCK), b -> createSilkTouchDispatchTable(b, LootItem.lootTableItem(Items.QUARTZ_BLOCK)));
        this.registerLootTableBuilder(BlocksPM.get(BlocksPM.CHIPPED_BUDDING_QUARTZ_BLOCK), b -> createSilkTouchDispatchTable(b, LootItem.lootTableItem(ItemsPM.DAMAGED_BUDDING_QUARTZ_BLOCK.get())));
        this.registerLootTableBuilder(BlocksPM.get(BlocksPM.FLAWED_BUDDING_QUARTZ_BLOCK), b -> createSilkTouchDispatchTable(b, LootItem.lootTableItem(ItemsPM.CHIPPED_BUDDING_QUARTZ_BLOCK.get())));
    }
    
    public static LootTableProvider.SubProviderEntry getSubProviderEntry() {
        return new LootTableProvider.SubProviderEntry(BlockLootTables::new, LootContextParamSets.BLOCK);
    }
}
