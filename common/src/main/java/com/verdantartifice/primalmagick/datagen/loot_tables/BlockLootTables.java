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
        this.registerBasicTable(BlockRegistration.ARCANE_WORKBENCH.get());
        this.registerBasicTable(BlockRegistration.WAND_ASSEMBLY_TABLE.get());
        this.registerBasicTable(BlockRegistration.WOOD_TABLE.get());
        this.registerBasicTable(BlockRegistration.ANALYSIS_TABLE.get());
        this.registerBasicTable(BlockRegistration.ESSENCE_FURNACE.get());
        this.registerBasicTable(BlockRegistration.CALCINATOR_BASIC.get());
        this.registerBasicTable(BlockRegistration.CALCINATOR_ENCHANTED.get());
        this.registerBasicTable(BlockRegistration.CALCINATOR_FORBIDDEN.get());
        this.registerBasicTable(BlockRegistration.CALCINATOR_HEAVENLY.get());
        this.registerBasicTable(BlockRegistration.WAND_INSCRIPTION_TABLE.get());
        this.registerBasicTable(BlockRegistration.SPELLCRAFTING_ALTAR.get());
        this.registerBasicTable(BlockRegistration.WAND_CHARGER.get());
        this.registerBasicTable(BlockRegistration.RESEARCH_TABLE.get());
        this.registerBasicTable(BlockRegistration.SUNLAMP.get());
        this.registerBasicTable(BlockRegistration.SPIRIT_LANTERN.get());
        this.registerBasicTable(BlockRegistration.RITUAL_ALTAR.get());
        this.registerBasicTable(BlockRegistration.OFFERING_PEDESTAL.get());
        this.registerBasicTable(BlockRegistration.INCENSE_BRAZIER.get());
        this.registerBasicTable(BlockRegistration.RITUAL_LECTERN.get());
        this.registerBasicTable(BlockRegistration.RITUAL_BELL.get());
        this.registerBasicTable(BlockRegistration.BLOODLETTER.get());
        this.registerBasicTable(BlockRegistration.SOUL_ANVIL.get());
        this.registerBasicTable(BlockRegistration.RUNIC_GRINDSTONE.get());
        this.registerManaBearingDeviceTable(BlockRegistration.HONEY_EXTRACTOR.get());
        this.registerBasicTable(BlockRegistration.PRIMALITE_GOLEM_CONTROLLER.get());
        this.registerBasicTable(BlockRegistration.HEXIUM_GOLEM_CONTROLLER.get());
        this.registerBasicTable(BlockRegistration.HALLOWSTEEL_GOLEM_CONTROLLER.get());
        this.registerBasicTable(BlockRegistration.SANGUINE_CRUCIBLE.get());
        this.registerManaBearingDeviceTable(BlockRegistration.CONCOCTER.get());
        this.registerBasicTable(BlockRegistration.RUNECARVING_TABLE.get());
        this.registerBasicTable(BlockRegistration.RUNESCRIBING_ALTAR_BASIC.get());
        this.registerBasicTable(BlockRegistration.RUNESCRIBING_ALTAR_ENCHANTED.get());
        this.registerBasicTable(BlockRegistration.RUNESCRIBING_ALTAR_FORBIDDEN.get());
        this.registerBasicTable(BlockRegistration.RUNESCRIBING_ALTAR_HEAVENLY.get());
        this.registerBasicTable(BlockRegistration.CELESTIAL_HARP.get());
        this.registerBasicTable(BlockRegistration.ENTROPY_SINK.get());
        this.registerBasicTable(BlockRegistration.AUTO_CHARGER.get());
        this.registerManaBearingDeviceTable(BlockRegistration.ESSENCE_TRANSMUTER.get());
        this.registerManaBearingDeviceTable(BlockRegistration.DISSOLUTION_CHAMBER.get());
        this.registerBasicTable(BlockRegistration.ZEPHYR_ENGINE.get());
        this.registerBasicTable(BlockRegistration.VOID_TURBINE.get());
        this.registerBasicTable(BlockRegistration.ESSENCE_CASK_ENCHANTED.get());
        this.registerBasicTable(BlockRegistration.ESSENCE_CASK_FORBIDDEN.get());
        this.registerBasicTable(BlockRegistration.ESSENCE_CASK_HEAVENLY.get());
        this.registerBasicTable(BlockRegistration.WAND_GLAMOUR_TABLE.get());
        this.registerManaBearingDeviceTable(BlockRegistration.INFERNAL_FURNACE.get());
        this.registerManaBearingDeviceTable(BlockRegistration.MANA_NEXUS.get());
        this.registerManaBearingDeviceTable(BlockRegistration.MANA_SINGULARITY.get());
        this.registerBasicTable(BlockRegistration.MANA_SINGULARITY_CREATIVE.get());
        this.registerBasicTable(BlockRegistration.SCRIBE_TABLE.get());

        // Register misc loot tables
        this.registerBasicTable(BlockRegistration.SALT_TRAIL.get());
        this.registerMultiGemOreTable(BlockRegistration.ROCK_SALT_ORE.get(), ItemsPM.ROCK_SALT.get(), 3.0F, 4.0F);
        this.registerGemOreTable(BlockRegistration.QUARTZ_ORE.get(), Items.QUARTZ);
        this.registerBasicTable(BlockRegistration.PRIMALITE_BLOCK.get());
        this.registerBasicTable(BlockRegistration.HEXIUM_BLOCK.get());
        this.registerBasicTable(BlockRegistration.HALLOWSTEEL_BLOCK.get());
        this.registerBasicTable(BlockRegistration.IGNYX_BLOCK.get());
        this.registerBasicTable(BlockRegistration.SALT_BLOCK.get());
        this.registerBasicTable(BlockRegistration.TREEFOLK_SPROUT.get());
        this.registerBasicTable(BlockRegistration.ENDERWARD.get());
    }

    private void registerEmptyLootTables() {
        this.registerEmptyTable(BlockRegistration.ANCIENT_FONT_EARTH.get());
        this.registerEmptyTable(BlockRegistration.ANCIENT_FONT_SEA.get());
        this.registerEmptyTable(BlockRegistration.ANCIENT_FONT_SKY.get());
        this.registerEmptyTable(BlockRegistration.ANCIENT_FONT_SUN.get());
        this.registerEmptyTable(BlockRegistration.ANCIENT_FONT_MOON.get());
        this.registerEmptyTable(BlockRegistration.GLOW_FIELD.get());
        this.registerEmptyTable(BlockRegistration.SOUL_GLOW_FIELD.get());
        this.registerEmptyTable(BlockRegistration.CONSECRATION_FIELD.get());
    }

    private void registerMarbleLootTables() {
        this.registerBasicTable(BlockRegistration.MARBLE_RAW.get());
        this.registerSlabTable(BlockRegistration.MARBLE_BRICK_SLAB.get());
        this.registerBasicTable(BlockRegistration.MARBLE_BRICK_STAIRS.get());
        this.registerBasicTable(BlockRegistration.MARBLE_BRICK_WALL.get());
        this.registerBasicTable(BlockRegistration.MARBLE_BRICKS.get());
        this.registerBasicTable(BlockRegistration.MARBLE_CHISELED.get());
        this.registerBasicTable(BlockRegistration.MARBLE_PILLAR.get());
        this.registerBasicTable(BlockRegistration.MARBLE_RUNED.get());
        this.registerSlabTable(BlockRegistration.MARBLE_SLAB.get());
        this.registerBasicTable(BlockRegistration.MARBLE_STAIRS.get());
        this.registerBasicTable(BlockRegistration.MARBLE_WALL.get());
        this.registerBasicTable(BlockRegistration.MARBLE_TILES.get());
        this.registerBasicTable(BlockRegistration.MARBLE_BOOKSHELF.get());
    }
    
    private void registerEnchantedMarbleLootTables() {
        this.registerBasicTable(BlockRegistration.MARBLE_ENCHANTED.get());
        this.registerSlabTable(BlockRegistration.MARBLE_ENCHANTED_BRICK_SLAB.get());
        this.registerBasicTable(BlockRegistration.MARBLE_ENCHANTED_BRICK_STAIRS.get());
        this.registerBasicTable(BlockRegistration.MARBLE_ENCHANTED_BRICK_WALL.get());
        this.registerBasicTable(BlockRegistration.MARBLE_ENCHANTED_BRICKS.get());
        this.registerBasicTable(BlockRegistration.MARBLE_ENCHANTED_CHISELED.get());
        this.registerBasicTable(BlockRegistration.MARBLE_ENCHANTED_PILLAR.get());
        this.registerBasicTable(BlockRegistration.MARBLE_ENCHANTED_RUNED.get());
        this.registerSlabTable(BlockRegistration.MARBLE_ENCHANTED_SLAB.get());
        this.registerBasicTable(BlockRegistration.MARBLE_ENCHANTED_STAIRS.get());
        this.registerBasicTable(BlockRegistration.MARBLE_ENCHANTED_WALL.get());
        this.registerBasicTable(BlockRegistration.MARBLE_ENCHANTED_BOOKSHELF.get());
    }
    
    private void registerSmokedMarbleLootTables() {
        this.registerBasicTable(BlockRegistration.MARBLE_SMOKED.get());
        this.registerSlabTable(BlockRegistration.MARBLE_SMOKED_BRICK_SLAB.get());
        this.registerBasicTable(BlockRegistration.MARBLE_SMOKED_BRICK_STAIRS.get());
        this.registerBasicTable(BlockRegistration.MARBLE_SMOKED_BRICK_WALL.get());
        this.registerBasicTable(BlockRegistration.MARBLE_SMOKED_BRICKS.get());
        this.registerBasicTable(BlockRegistration.MARBLE_SMOKED_CHISELED.get());
        this.registerBasicTable(BlockRegistration.MARBLE_SMOKED_PILLAR.get());
        this.registerBasicTable(BlockRegistration.MARBLE_SMOKED_RUNED.get());
        this.registerSlabTable(BlockRegistration.MARBLE_SMOKED_SLAB.get());
        this.registerBasicTable(BlockRegistration.MARBLE_SMOKED_STAIRS.get());
        this.registerBasicTable(BlockRegistration.MARBLE_SMOKED_WALL.get());
        this.registerBasicTable(BlockRegistration.MARBLE_SMOKED_BOOKSHELF.get());
    }
    
    private void registerHallowedMarbleLootTables() {
        this.registerBasicTable(BlockRegistration.MARBLE_HALLOWED.get());
        this.registerSlabTable(BlockRegistration.MARBLE_HALLOWED_BRICK_SLAB.get());
        this.registerBasicTable(BlockRegistration.MARBLE_HALLOWED_BRICK_STAIRS.get());
        this.registerBasicTable(BlockRegistration.MARBLE_HALLOWED_BRICK_WALL.get());
        this.registerBasicTable(BlockRegistration.MARBLE_HALLOWED_BRICKS.get());
        this.registerBasicTable(BlockRegistration.MARBLE_HALLOWED_CHISELED.get());
        this.registerBasicTable(BlockRegistration.MARBLE_HALLOWED_PILLAR.get());
        this.registerBasicTable(BlockRegistration.MARBLE_HALLOWED_RUNED.get());
        this.registerSlabTable(BlockRegistration.MARBLE_HALLOWED_SLAB.get());
        this.registerBasicTable(BlockRegistration.MARBLE_HALLOWED_STAIRS.get());
        this.registerBasicTable(BlockRegistration.MARBLE_HALLOWED_WALL.get());
        this.registerBasicTable(BlockRegistration.MARBLE_HALLOWED_BOOKSHELF.get());
    }
    
    private void registerSunwoodLootTables() {
        this.registerPulsingLogTable(BlockRegistration.SUNWOOD_LOG.get());
        this.registerPulsingLogTable(BlockRegistration.STRIPPED_SUNWOOD_LOG.get());
        this.registerPulsingLogTable(BlockRegistration.SUNWOOD_WOOD.get());
        this.registerPulsingLogTable(BlockRegistration.STRIPPED_SUNWOOD_WOOD.get());
        this.registerLeavesTable(BlockRegistration.SUNWOOD_LEAVES.get(), BlockRegistration.SUNWOOD_SAPLING.get(), new float[] { 0.1F, 0.125F, 0.16666667F, 0.2F });
        this.registerBasicTable(BlockRegistration.SUNWOOD_SAPLING.get());
        this.registerBasicTable(BlockRegistration.SUNWOOD_PLANKS.get());
        this.registerSlabTable(BlockRegistration.SUNWOOD_SLAB.get());
        this.registerBasicTable(BlockRegistration.SUNWOOD_STAIRS.get());
        this.registerBasicTable(BlockRegistration.SUNWOOD_PILLAR.get());
    }
    
    private void registerMoonwoodLootTables() {
        this.registerPulsingLogTable(BlockRegistration.MOONWOOD_LOG.get());
        this.registerPulsingLogTable(BlockRegistration.STRIPPED_MOONWOOD_LOG.get());
        this.registerPulsingLogTable(BlockRegistration.MOONWOOD_WOOD.get());
        this.registerPulsingLogTable(BlockRegistration.STRIPPED_MOONWOOD_WOOD.get());
        this.registerLeavesTable(BlockRegistration.MOONWOOD_LEAVES.get(), BlockRegistration.MOONWOOD_SAPLING.get(), new float[] { 0.1F, 0.125F, 0.16666667F, 0.2F });
        this.registerBasicTable(BlockRegistration.MOONWOOD_SAPLING.get());
        this.registerBasicTable(BlockRegistration.MOONWOOD_PLANKS.get());
        this.registerSlabTable(BlockRegistration.MOONWOOD_SLAB.get());
        this.registerBasicTable(BlockRegistration.MOONWOOD_STAIRS.get());
        this.registerBasicTable(BlockRegistration.MOONWOOD_PILLAR.get());
    }
    
    private void registerHallowoodLootTables() {
        this.registerBasicTable(BlockRegistration.HALLOWOOD_LOG.get());
        this.registerBasicTable(BlockRegistration.STRIPPED_HALLOWOOD_LOG.get());
        this.registerBasicTable(BlockRegistration.HALLOWOOD_WOOD.get());
        this.registerBasicTable(BlockRegistration.STRIPPED_HALLOWOOD_WOOD.get());
        this.registerLeavesTable(BlockRegistration.HALLOWOOD_LEAVES.get(), BlockRegistration.HALLOWOOD_SAPLING.get(), new float[] { 0.15F, 0.1875F, 0.25F, 0.3F });
        this.registerBasicTable(BlockRegistration.HALLOWOOD_SAPLING.get());
        this.registerBasicTable(BlockRegistration.HALLOWOOD_PLANKS.get());
        this.registerSlabTable(BlockRegistration.HALLOWOOD_SLAB.get());
        this.registerBasicTable(BlockRegistration.HALLOWOOD_STAIRS.get());
        this.registerBasicTable(BlockRegistration.HALLOWOOD_PILLAR.get());
    }
    
    private void registerCropLootTables() {
        this.registerSplittingTable(BlockRegistration.HYDROMELON.get(), ItemsPM.HYDROMELON_SLICE.get(), UniformGenerator.between(3F, 7F), OptionalInt.of(9));
        this.registerLootTableBuilder(BlockRegistration.HYRDOMELON_STEM.get(), b -> this.createStemDrops(b, ItemsPM.HYDROMELON_SEEDS.get()));
        this.registerLootTableBuilder(BlockRegistration.ATTACHED_HYDROMELON_STEM.get(), b -> this.createAttachedStemDrops(b, ItemsPM.HYDROMELON_SEEDS.get()));
        this.registerLootTableBuilder(BlockRegistration.BLOOD_ROSE.get(), b -> this.createSinglePropConditionTable(b, BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER));
        this.registerLootTableBuilder(BlockRegistration.EMBERFLOWER.get(), b -> this.createSinglePropConditionTable(b, BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER));
    }
    
    private void registerInfusedStoneLootTables() {
        this.registerInfusedStoneTable(BlockRegistration.INFUSED_STONE_EARTH.get(), ItemsPM.ESSENCE_DUST_EARTH.get());
        this.registerInfusedStoneTable(BlockRegistration.INFUSED_STONE_SEA.get(), ItemsPM.ESSENCE_DUST_SEA.get());
        this.registerInfusedStoneTable(BlockRegistration.INFUSED_STONE_SKY.get(), ItemsPM.ESSENCE_DUST_SKY.get());
        this.registerInfusedStoneTable(BlockRegistration.INFUSED_STONE_SUN.get(), ItemsPM.ESSENCE_DUST_SUN.get());
        this.registerInfusedStoneTable(BlockRegistration.INFUSED_STONE_MOON.get(), ItemsPM.ESSENCE_DUST_MOON.get());
    }
    
    private void registerSkyglassLootTables() {
        this.registerBasicTable(BlockRegistration.SKYGLASS.get());
        this.registerBasicTable(BlockRegistration.STAINED_SKYGLASS_BLACK.get());
        this.registerBasicTable(BlockRegistration.STAINED_SKYGLASS_BLUE.get());
        this.registerBasicTable(BlockRegistration.STAINED_SKYGLASS_BROWN.get());
        this.registerBasicTable(BlockRegistration.STAINED_SKYGLASS_CYAN.get());
        this.registerBasicTable(BlockRegistration.STAINED_SKYGLASS_GRAY.get());
        this.registerBasicTable(BlockRegistration.STAINED_SKYGLASS_GREEN.get());
        this.registerBasicTable(BlockRegistration.STAINED_SKYGLASS_LIGHT_BLUE.get());
        this.registerBasicTable(BlockRegistration.STAINED_SKYGLASS_LIGHT_GRAY.get());
        this.registerBasicTable(BlockRegistration.STAINED_SKYGLASS_LIME.get());
        this.registerBasicTable(BlockRegistration.STAINED_SKYGLASS_MAGENTA.get());
        this.registerBasicTable(BlockRegistration.STAINED_SKYGLASS_ORANGE.get());
        this.registerBasicTable(BlockRegistration.STAINED_SKYGLASS_PINK.get());
        this.registerBasicTable(BlockRegistration.STAINED_SKYGLASS_PURPLE.get());
        this.registerBasicTable(BlockRegistration.STAINED_SKYGLASS_RED.get());
        this.registerBasicTable(BlockRegistration.STAINED_SKYGLASS_WHITE.get());
        this.registerBasicTable(BlockRegistration.STAINED_SKYGLASS_YELLOW.get());
        
        this.registerBasicTable(BlockRegistration.SKYGLASS_PANE.get());
        this.registerBasicTable(BlockRegistration.STAINED_SKYGLASS_PANE_BLACK.get());
        this.registerBasicTable(BlockRegistration.STAINED_SKYGLASS_PANE_BLUE.get());
        this.registerBasicTable(BlockRegistration.STAINED_SKYGLASS_PANE_BROWN.get());
        this.registerBasicTable(BlockRegistration.STAINED_SKYGLASS_PANE_CYAN.get());
        this.registerBasicTable(BlockRegistration.STAINED_SKYGLASS_PANE_GRAY.get());
        this.registerBasicTable(BlockRegistration.STAINED_SKYGLASS_PANE_GREEN.get());
        this.registerBasicTable(BlockRegistration.STAINED_SKYGLASS_PANE_LIGHT_BLUE.get());
        this.registerBasicTable(BlockRegistration.STAINED_SKYGLASS_PANE_LIGHT_GRAY.get());
        this.registerBasicTable(BlockRegistration.STAINED_SKYGLASS_PANE_LIME.get());
        this.registerBasicTable(BlockRegistration.STAINED_SKYGLASS_PANE_MAGENTA.get());
        this.registerBasicTable(BlockRegistration.STAINED_SKYGLASS_PANE_ORANGE.get());
        this.registerBasicTable(BlockRegistration.STAINED_SKYGLASS_PANE_PINK.get());
        this.registerBasicTable(BlockRegistration.STAINED_SKYGLASS_PANE_PURPLE.get());
        this.registerBasicTable(BlockRegistration.STAINED_SKYGLASS_PANE_RED.get());
        this.registerBasicTable(BlockRegistration.STAINED_SKYGLASS_PANE_WHITE.get());
        this.registerBasicTable(BlockRegistration.STAINED_SKYGLASS_PANE_YELLOW.get());
    }
    
    private void registerRitualCandleLootTables() {
        this.registerBasicTable(BlockRegistration.RITUAL_CANDLE_BLACK.get());
        this.registerBasicTable(BlockRegistration.RITUAL_CANDLE_BLUE.get());
        this.registerBasicTable(BlockRegistration.RITUAL_CANDLE_BROWN.get());
        this.registerBasicTable(BlockRegistration.RITUAL_CANDLE_CYAN.get());
        this.registerBasicTable(BlockRegistration.RITUAL_CANDLE_GRAY.get());
        this.registerBasicTable(BlockRegistration.RITUAL_CANDLE_GREEN.get());
        this.registerBasicTable(BlockRegistration.RITUAL_CANDLE_LIGHT_BLUE.get());
        this.registerBasicTable(BlockRegistration.RITUAL_CANDLE_LIGHT_GRAY.get());
        this.registerBasicTable(BlockRegistration.RITUAL_CANDLE_LIME.get());
        this.registerBasicTable(BlockRegistration.RITUAL_CANDLE_MAGENTA.get());
        this.registerBasicTable(BlockRegistration.RITUAL_CANDLE_ORANGE.get());
        this.registerBasicTable(BlockRegistration.RITUAL_CANDLE_PINK.get());
        this.registerBasicTable(BlockRegistration.RITUAL_CANDLE_PURPLE.get());
        this.registerBasicTable(BlockRegistration.RITUAL_CANDLE_RED.get());
        this.registerBasicTable(BlockRegistration.RITUAL_CANDLE_WHITE.get());
        this.registerBasicTable(BlockRegistration.RITUAL_CANDLE_YELLOW.get());
    }

    private void registerManaFontLootTables() {
        this.registerBasicTable(BlockRegistration.ARTIFICIAL_FONT_EARTH.get());
        this.registerBasicTable(BlockRegistration.ARTIFICIAL_FONT_SEA.get());
        this.registerBasicTable(BlockRegistration.ARTIFICIAL_FONT_SKY.get());
        this.registerBasicTable(BlockRegistration.ARTIFICIAL_FONT_SUN.get());
        this.registerBasicTable(BlockRegistration.ARTIFICIAL_FONT_MOON.get());
        this.registerBasicTable(BlockRegistration.ARTIFICIAL_FONT_BLOOD.get());
        this.registerBasicTable(BlockRegistration.ARTIFICIAL_FONT_INFERNAL.get());
        this.registerBasicTable(BlockRegistration.ARTIFICIAL_FONT_VOID.get());
        this.registerBasicTable(BlockRegistration.ARTIFICIAL_FONT_HALLOWED.get());
        this.registerBasicTable(BlockRegistration.FORBIDDEN_FONT_EARTH.get());
        this.registerBasicTable(BlockRegistration.FORBIDDEN_FONT_SEA.get());
        this.registerBasicTable(BlockRegistration.FORBIDDEN_FONT_SKY.get());
        this.registerBasicTable(BlockRegistration.FORBIDDEN_FONT_SUN.get());
        this.registerBasicTable(BlockRegistration.FORBIDDEN_FONT_MOON.get());
        this.registerBasicTable(BlockRegistration.FORBIDDEN_FONT_BLOOD.get());
        this.registerBasicTable(BlockRegistration.FORBIDDEN_FONT_INFERNAL.get());
        this.registerBasicTable(BlockRegistration.FORBIDDEN_FONT_VOID.get());
        this.registerBasicTable(BlockRegistration.FORBIDDEN_FONT_HALLOWED.get());
        this.registerBasicTable(BlockRegistration.HEAVENLY_FONT_EARTH.get());
        this.registerBasicTable(BlockRegistration.HEAVENLY_FONT_SEA.get());
        this.registerBasicTable(BlockRegistration.HEAVENLY_FONT_SKY.get());
        this.registerBasicTable(BlockRegistration.HEAVENLY_FONT_SUN.get());
        this.registerBasicTable(BlockRegistration.HEAVENLY_FONT_MOON.get());
        this.registerBasicTable(BlockRegistration.HEAVENLY_FONT_BLOOD.get());
        this.registerBasicTable(BlockRegistration.HEAVENLY_FONT_INFERNAL.get());
        this.registerBasicTable(BlockRegistration.HEAVENLY_FONT_VOID.get());
        this.registerBasicTable(BlockRegistration.HEAVENLY_FONT_HALLOWED.get());
    }
    
    private void registerBuddingGemLootTables() {
        Holder<Enchantment> fortuneHolder = this.registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE);
        this.registerLootTableBuilder(BlockRegistration.SYNTHETIC_AMETHYST_CLUSTER.get(), b -> createSilkTouchDispatchTable(b, LootItem.lootTableItem(Items.AMETHYST_SHARD).apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F))).apply(ApplyBonusCount.addOreBonusCount(fortuneHolder)).when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.CLUSTER_MAX_HARVESTABLES))).otherwise(this.applyExplosionDecay(b, LootItem.lootTableItem(Items.AMETHYST_SHARD).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))))));
        this.dropWhenSilkTouch(BlockRegistration.LARGE_SYNTHETIC_AMETHYST_BUD.get());
        this.dropWhenSilkTouch(BlockRegistration.MEDIUM_SYNTHETIC_AMETHYST_BUD.get());
        this.dropWhenSilkTouch(BlockRegistration.SMALL_SYNTHETIC_AMETHYST_BUD.get());
        this.registerLootTableBuilder(BlockRegistration.DAMAGED_BUDDING_AMETHYST_BLOCK.get(), b -> createSilkTouchDispatchTable(b, LootItem.lootTableItem(Items.AMETHYST_BLOCK)));
        this.registerLootTableBuilder(BlockRegistration.CHIPPED_BUDDING_AMETHYST_BLOCK.get(), b -> createSilkTouchDispatchTable(b, LootItem.lootTableItem(ItemsPM.DAMAGED_BUDDING_AMETHYST_BLOCK.get())));
        this.registerLootTableBuilder(BlockRegistration.FLAWED_BUDDING_AMETHYST_BLOCK.get(), b -> createSilkTouchDispatchTable(b, LootItem.lootTableItem(ItemsPM.CHIPPED_BUDDING_AMETHYST_BLOCK.get())));
        this.registerLootTableBuilder(BlockRegistration.SYNTHETIC_DIAMOND_CLUSTER.get(), b -> createSilkTouchDispatchTable(b, LootItem.lootTableItem(Items.DIAMOND).apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F))).apply(ApplyBonusCount.addOreBonusCount(fortuneHolder)).when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.CLUSTER_MAX_HARVESTABLES))).otherwise(this.applyExplosionDecay(b, LootItem.lootTableItem(Items.DIAMOND).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))))));
        this.dropWhenSilkTouch(BlockRegistration.LARGE_SYNTHETIC_DIAMOND_BUD.get());
        this.dropWhenSilkTouch(BlockRegistration.MEDIUM_SYNTHETIC_DIAMOND_BUD.get());
        this.dropWhenSilkTouch(BlockRegistration.SMALL_SYNTHETIC_DIAMOND_BUD.get());
        this.registerLootTableBuilder(BlockRegistration.DAMAGED_BUDDING_DIAMOND_BLOCK.get(), b -> createSilkTouchDispatchTable(b, LootItem.lootTableItem(Items.DIAMOND_BLOCK)));
        this.registerLootTableBuilder(BlockRegistration.CHIPPED_BUDDING_DIAMOND_BLOCK.get(), b -> createSilkTouchDispatchTable(b, LootItem.lootTableItem(ItemsPM.DAMAGED_BUDDING_DIAMOND_BLOCK.get())));
        this.registerLootTableBuilder(BlockRegistration.FLAWED_BUDDING_DIAMOND_BLOCK.get(), b -> createSilkTouchDispatchTable(b, LootItem.lootTableItem(ItemsPM.CHIPPED_BUDDING_DIAMOND_BLOCK.get())));
        this.registerLootTableBuilder(BlockRegistration.SYNTHETIC_EMERALD_CLUSTER.get(), b -> createSilkTouchDispatchTable(b, LootItem.lootTableItem(Items.EMERALD).apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F))).apply(ApplyBonusCount.addOreBonusCount(fortuneHolder)).when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.CLUSTER_MAX_HARVESTABLES))).otherwise(this.applyExplosionDecay(b, LootItem.lootTableItem(Items.EMERALD).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))))));
        this.dropWhenSilkTouch(BlockRegistration.LARGE_SYNTHETIC_EMERALD_BUD.get());
        this.dropWhenSilkTouch(BlockRegistration.MEDIUM_SYNTHETIC_EMERALD_BUD.get());
        this.dropWhenSilkTouch(BlockRegistration.SMALL_SYNTHETIC_EMERALD_BUD.get());
        this.registerLootTableBuilder(BlockRegistration.DAMAGED_BUDDING_EMERALD_BLOCK.get(), b -> createSilkTouchDispatchTable(b, LootItem.lootTableItem(Items.EMERALD_BLOCK)));
        this.registerLootTableBuilder(BlockRegistration.CHIPPED_BUDDING_EMERALD_BLOCK.get(), b -> createSilkTouchDispatchTable(b, LootItem.lootTableItem(ItemsPM.DAMAGED_BUDDING_EMERALD_BLOCK.get())));
        this.registerLootTableBuilder(BlockRegistration.FLAWED_BUDDING_EMERALD_BLOCK.get(), b -> createSilkTouchDispatchTable(b, LootItem.lootTableItem(ItemsPM.CHIPPED_BUDDING_EMERALD_BLOCK.get())));
        this.registerLootTableBuilder(BlockRegistration.SYNTHETIC_QUARTZ_CLUSTER.get(), b -> createSilkTouchDispatchTable(b, LootItem.lootTableItem(Items.QUARTZ).apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F))).apply(ApplyBonusCount.addOreBonusCount(fortuneHolder)).when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.CLUSTER_MAX_HARVESTABLES))).otherwise(this.applyExplosionDecay(b, LootItem.lootTableItem(Items.QUARTZ).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))))));
        this.dropWhenSilkTouch(BlockRegistration.LARGE_SYNTHETIC_QUARTZ_BUD.get());
        this.dropWhenSilkTouch(BlockRegistration.MEDIUM_SYNTHETIC_QUARTZ_BUD.get());
        this.dropWhenSilkTouch(BlockRegistration.SMALL_SYNTHETIC_QUARTZ_BUD.get());
        this.registerLootTableBuilder(BlockRegistration.DAMAGED_BUDDING_QUARTZ_BLOCK.get(), b -> createSilkTouchDispatchTable(b, LootItem.lootTableItem(Items.QUARTZ_BLOCK)));
        this.registerLootTableBuilder(BlockRegistration.CHIPPED_BUDDING_QUARTZ_BLOCK.get(), b -> createSilkTouchDispatchTable(b, LootItem.lootTableItem(ItemsPM.DAMAGED_BUDDING_QUARTZ_BLOCK.get())));
        this.registerLootTableBuilder(BlockRegistration.FLAWED_BUDDING_QUARTZ_BLOCK.get(), b -> createSilkTouchDispatchTable(b, LootItem.lootTableItem(ItemsPM.CHIPPED_BUDDING_QUARTZ_BLOCK.get())));
    }
    
    public static LootTableProvider.SubProviderEntry getSubProviderEntry() {
        return new LootTableProvider.SubProviderEntry(BlockLootTables::new, LootContextParamSets.BLOCK);
    }
}
