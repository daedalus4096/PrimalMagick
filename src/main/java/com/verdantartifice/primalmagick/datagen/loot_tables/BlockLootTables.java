package com.verdantartifice.primalmagick.datagen.loot_tables;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;

import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

/**
 * Data provider for all of the mod's block loot tables.
 * 
 * @author Daedalus4096
 */
public class BlockLootTables extends AbstractBlockLootTableProvider {
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
        this.registerInfusedStoneLootTables();
        this.registerSkyglassLootTables();
        this.registerRitualCandleLootTables();
        this.registerManaFontLootTables();
        
        // Register device loot tables
        this.registerBasicTable(BlocksPM.ARCANE_WORKBENCH.get());
        this.registerBasicTable(BlocksPM.WAND_ASSEMBLY_TABLE.get());
        this.registerBasicTable(BlocksPM.WOOD_TABLE.get());
        this.registerBasicTable(BlocksPM.ANALYSIS_TABLE.get());
        this.registerBasicTable(BlocksPM.ESSENCE_FURNACE.get());
        this.registerBasicTable(BlocksPM.CALCINATOR_BASIC.get());
        this.registerBasicTable(BlocksPM.CALCINATOR_ENCHANTED.get());
        this.registerBasicTable(BlocksPM.CALCINATOR_FORBIDDEN.get());
        this.registerBasicTable(BlocksPM.CALCINATOR_HEAVENLY.get());
        this.registerBasicTable(BlocksPM.WAND_INSCRIPTION_TABLE.get());
        this.registerBasicTable(BlocksPM.SPELLCRAFTING_ALTAR.get());
        this.registerBasicTable(BlocksPM.WAND_CHARGER.get());
        this.registerBasicTable(BlocksPM.RESEARCH_TABLE.get());
        this.registerBasicTable(BlocksPM.SUNLAMP.get());
        this.registerBasicTable(BlocksPM.SPIRIT_LANTERN.get());
        this.registerBasicTable(BlocksPM.RITUAL_ALTAR.get());
        this.registerBasicTable(BlocksPM.OFFERING_PEDESTAL.get());
        this.registerBasicTable(BlocksPM.INCENSE_BRAZIER.get());
        this.registerBasicTable(BlocksPM.RITUAL_LECTERN.get());
        this.registerBasicTable(BlocksPM.RITUAL_BELL.get());
        this.registerBasicTable(BlocksPM.BLOODLETTER.get());
        this.registerBasicTable(BlocksPM.SOUL_ANVIL.get());
        this.registerBasicTable(BlocksPM.RUNIC_GRINDSTONE.get());
        this.registerManaBearingDeviceTable(BlocksPM.HONEY_EXTRACTOR.get());
        this.registerBasicTable(BlocksPM.PRIMALITE_GOLEM_CONTROLLER.get());
        this.registerBasicTable(BlocksPM.HEXIUM_GOLEM_CONTROLLER.get());
        this.registerBasicTable(BlocksPM.HALLOWSTEEL_GOLEM_CONTROLLER.get());
        this.registerBasicTable(BlocksPM.SANGUINE_CRUCIBLE.get());
        this.registerManaBearingDeviceTable(BlocksPM.CONCOCTER.get());
        this.registerBasicTable(BlocksPM.RUNECARVING_TABLE.get());
        this.registerBasicTable(BlocksPM.RUNESCRIBING_ALTAR_BASIC.get());
        this.registerBasicTable(BlocksPM.RUNESCRIBING_ALTAR_ENCHANTED.get());
        this.registerBasicTable(BlocksPM.RUNESCRIBING_ALTAR_FORBIDDEN.get());
        this.registerBasicTable(BlocksPM.RUNESCRIBING_ALTAR_HEAVENLY.get());
        this.registerBasicTable(BlocksPM.CELESTIAL_HARP.get());
        this.registerBasicTable(BlocksPM.ENTROPY_SINK.get());
        this.registerBasicTable(BlocksPM.AUTO_CHARGER.get());
        this.registerManaBearingDeviceTable(BlocksPM.ESSENCE_TRANSMUTER.get());
        this.registerManaBearingDeviceTable(BlocksPM.DISSOLUTION_CHAMBER.get());
        this.registerBasicTable(BlocksPM.ZEPHYR_ENGINE.get());
        this.registerBasicTable(BlocksPM.VOID_TURBINE.get());
        this.registerBasicTable(BlocksPM.ESSENCE_CASK_ENCHANTED.get());
        this.registerBasicTable(BlocksPM.ESSENCE_CASK_FORBIDDEN.get());
        this.registerBasicTable(BlocksPM.ESSENCE_CASK_HEAVENLY.get());
        this.registerBasicTable(BlocksPM.WAND_GLAMOUR_TABLE.get());
        this.registerManaBearingDeviceTable(BlocksPM.INFERNAL_FURNACE.get());
        this.registerManaBearingDeviceTable(BlocksPM.MANA_NEXUS.get());

        // Register misc loot tables
        this.registerBasicTable(BlocksPM.SALT_TRAIL.get());
        this.registerMultiGemOreTable(BlocksPM.ROCK_SALT_ORE.get(), ItemsPM.ROCK_SALT.get(), 3.0F, 4.0F);
        this.registerGemOreTable(BlocksPM.QUARTZ_ORE.get(), Items.QUARTZ);
        this.registerBasicTable(BlocksPM.PRIMALITE_BLOCK.get());
        this.registerBasicTable(BlocksPM.HEXIUM_BLOCK.get());
        this.registerBasicTable(BlocksPM.HALLOWSTEEL_BLOCK.get());
        this.registerBasicTable(BlocksPM.IGNYX_BLOCK.get());
        this.registerBasicTable(BlocksPM.SALT_BLOCK.get());
        this.registerBasicTable(BlocksPM.TREEFOLK_SPROUT.get());
    }

    private void registerEmptyLootTables() {
        this.registerEmptyTable(BlocksPM.ANCIENT_FONT_EARTH.get());
        this.registerEmptyTable(BlocksPM.ANCIENT_FONT_SEA.get());
        this.registerEmptyTable(BlocksPM.ANCIENT_FONT_SKY.get());
        this.registerEmptyTable(BlocksPM.ANCIENT_FONT_SUN.get());
        this.registerEmptyTable(BlocksPM.ANCIENT_FONT_MOON.get());
        this.registerEmptyTable(BlocksPM.GLOW_FIELD.get());
        this.registerEmptyTable(BlocksPM.SOUL_GLOW_FIELD.get());
        this.registerEmptyTable(BlocksPM.CONSECRATION_FIELD.get());
    }

    private void registerMarbleLootTables() {
        this.registerBasicTable(BlocksPM.MARBLE_RAW.get());
        this.registerSlabTable(BlocksPM.MARBLE_BRICK_SLAB.get());
        this.registerBasicTable(BlocksPM.MARBLE_BRICK_STAIRS.get());
        this.registerBasicTable(BlocksPM.MARBLE_BRICK_WALL.get());
        this.registerBasicTable(BlocksPM.MARBLE_BRICKS.get());
        this.registerBasicTable(BlocksPM.MARBLE_CHISELED.get());
        this.registerBasicTable(BlocksPM.MARBLE_PILLAR.get());
        this.registerBasicTable(BlocksPM.MARBLE_RUNED.get());
        this.registerSlabTable(BlocksPM.MARBLE_SLAB.get());
        this.registerBasicTable(BlocksPM.MARBLE_STAIRS.get());
        this.registerBasicTable(BlocksPM.MARBLE_WALL.get());
        this.registerBasicTable(BlocksPM.MARBLE_TILES.get());
    }
    
    private void registerEnchantedMarbleLootTables() {
        this.registerBasicTable(BlocksPM.MARBLE_ENCHANTED.get());
        this.registerSlabTable(BlocksPM.MARBLE_ENCHANTED_BRICK_SLAB.get());
        this.registerBasicTable(BlocksPM.MARBLE_ENCHANTED_BRICK_STAIRS.get());
        this.registerBasicTable(BlocksPM.MARBLE_ENCHANTED_BRICK_WALL.get());
        this.registerBasicTable(BlocksPM.MARBLE_ENCHANTED_BRICKS.get());
        this.registerBasicTable(BlocksPM.MARBLE_ENCHANTED_CHISELED.get());
        this.registerBasicTable(BlocksPM.MARBLE_ENCHANTED_PILLAR.get());
        this.registerBasicTable(BlocksPM.MARBLE_ENCHANTED_RUNED.get());
        this.registerSlabTable(BlocksPM.MARBLE_ENCHANTED_SLAB.get());
        this.registerBasicTable(BlocksPM.MARBLE_ENCHANTED_STAIRS.get());
        this.registerBasicTable(BlocksPM.MARBLE_ENCHANTED_WALL.get());
    }
    
    private void registerSmokedMarbleLootTables() {
        this.registerBasicTable(BlocksPM.MARBLE_SMOKED.get());
        this.registerSlabTable(BlocksPM.MARBLE_SMOKED_BRICK_SLAB.get());
        this.registerBasicTable(BlocksPM.MARBLE_SMOKED_BRICK_STAIRS.get());
        this.registerBasicTable(BlocksPM.MARBLE_SMOKED_BRICK_WALL.get());
        this.registerBasicTable(BlocksPM.MARBLE_SMOKED_BRICKS.get());
        this.registerBasicTable(BlocksPM.MARBLE_SMOKED_CHISELED.get());
        this.registerBasicTable(BlocksPM.MARBLE_SMOKED_PILLAR.get());
        this.registerBasicTable(BlocksPM.MARBLE_SMOKED_RUNED.get());
        this.registerSlabTable(BlocksPM.MARBLE_SMOKED_SLAB.get());
        this.registerBasicTable(BlocksPM.MARBLE_SMOKED_STAIRS.get());
        this.registerBasicTable(BlocksPM.MARBLE_SMOKED_WALL.get());
    }
    
    private void registerHallowedMarbleLootTables() {
        this.registerBasicTable(BlocksPM.MARBLE_HALLOWED.get());
        this.registerSlabTable(BlocksPM.MARBLE_HALLOWED_BRICK_SLAB.get());
        this.registerBasicTable(BlocksPM.MARBLE_HALLOWED_BRICK_STAIRS.get());
        this.registerBasicTable(BlocksPM.MARBLE_HALLOWED_BRICK_WALL.get());
        this.registerBasicTable(BlocksPM.MARBLE_HALLOWED_BRICKS.get());
        this.registerBasicTable(BlocksPM.MARBLE_HALLOWED_CHISELED.get());
        this.registerBasicTable(BlocksPM.MARBLE_HALLOWED_PILLAR.get());
        this.registerBasicTable(BlocksPM.MARBLE_HALLOWED_RUNED.get());
        this.registerSlabTable(BlocksPM.MARBLE_HALLOWED_SLAB.get());
        this.registerBasicTable(BlocksPM.MARBLE_HALLOWED_STAIRS.get());
        this.registerBasicTable(BlocksPM.MARBLE_HALLOWED_WALL.get());
    }
    
    private void registerSunwoodLootTables() {
        this.registerPulsingLogTable(BlocksPM.SUNWOOD_LOG.get());
        this.registerPulsingLogTable(BlocksPM.STRIPPED_SUNWOOD_LOG.get());
        this.registerPulsingLogTable(BlocksPM.SUNWOOD_WOOD.get());
        this.registerPulsingLogTable(BlocksPM.STRIPPED_SUNWOOD_WOOD.get());
        this.registerLeavesTable(BlocksPM.SUNWOOD_LEAVES.get(), BlocksPM.SUNWOOD_SAPLING.get(), new float[] { 0.1F, 0.125F, 0.16666667F, 0.2F });
        this.registerBasicTable(BlocksPM.SUNWOOD_SAPLING.get());
        this.registerBasicTable(BlocksPM.SUNWOOD_PLANKS.get());
        this.registerSlabTable(BlocksPM.SUNWOOD_SLAB.get());
        this.registerBasicTable(BlocksPM.SUNWOOD_STAIRS.get());
        this.registerBasicTable(BlocksPM.SUNWOOD_PILLAR.get());
    }
    
    private void registerMoonwoodLootTables() {
        this.registerPulsingLogTable(BlocksPM.MOONWOOD_LOG.get());
        this.registerPulsingLogTable(BlocksPM.STRIPPED_MOONWOOD_LOG.get());
        this.registerPulsingLogTable(BlocksPM.MOONWOOD_WOOD.get());
        this.registerPulsingLogTable(BlocksPM.STRIPPED_MOONWOOD_WOOD.get());
        this.registerLeavesTable(BlocksPM.MOONWOOD_LEAVES.get(), BlocksPM.MOONWOOD_SAPLING.get(), new float[] { 0.1F, 0.125F, 0.16666667F, 0.2F });
        this.registerBasicTable(BlocksPM.MOONWOOD_SAPLING.get());
        this.registerBasicTable(BlocksPM.MOONWOOD_PLANKS.get());
        this.registerSlabTable(BlocksPM.MOONWOOD_SLAB.get());
        this.registerBasicTable(BlocksPM.MOONWOOD_STAIRS.get());
        this.registerBasicTable(BlocksPM.MOONWOOD_PILLAR.get());
    }
    
    private void registerHallowoodLootTables() {
        this.registerBasicTable(BlocksPM.HALLOWOOD_LOG.get());
        this.registerBasicTable(BlocksPM.STRIPPED_HALLOWOOD_LOG.get());
        this.registerBasicTable(BlocksPM.HALLOWOOD_WOOD.get());
        this.registerBasicTable(BlocksPM.STRIPPED_HALLOWOOD_WOOD.get());
        this.registerLeavesTable(BlocksPM.HALLOWOOD_LEAVES.get(), BlocksPM.HALLOWOOD_SAPLING.get(), new float[] { 0.15F, 0.1875F, 0.25F, 0.3F });
        this.registerBasicTable(BlocksPM.HALLOWOOD_SAPLING.get());
        this.registerBasicTable(BlocksPM.HALLOWOOD_PLANKS.get());
        this.registerSlabTable(BlocksPM.HALLOWOOD_SLAB.get());
        this.registerBasicTable(BlocksPM.HALLOWOOD_STAIRS.get());
        this.registerBasicTable(BlocksPM.HALLOWOOD_PILLAR.get());
    }
    
    private void registerInfusedStoneLootTables() {
        this.registerInfusedStoneTable(BlocksPM.INFUSED_STONE_EARTH.get(), ItemsPM.ESSENCE_DUST_EARTH.get());
        this.registerInfusedStoneTable(BlocksPM.INFUSED_STONE_SEA.get(), ItemsPM.ESSENCE_DUST_SEA.get());
        this.registerInfusedStoneTable(BlocksPM.INFUSED_STONE_SKY.get(), ItemsPM.ESSENCE_DUST_SKY.get());
        this.registerInfusedStoneTable(BlocksPM.INFUSED_STONE_SUN.get(), ItemsPM.ESSENCE_DUST_SUN.get());
        this.registerInfusedStoneTable(BlocksPM.INFUSED_STONE_MOON.get(), ItemsPM.ESSENCE_DUST_MOON.get());
    }
    
    private void registerSkyglassLootTables() {
        this.registerBasicTable(BlocksPM.SKYGLASS.get());
        this.registerBasicTable(BlocksPM.STAINED_SKYGLASS_BLACK.get());
        this.registerBasicTable(BlocksPM.STAINED_SKYGLASS_BLUE.get());
        this.registerBasicTable(BlocksPM.STAINED_SKYGLASS_BROWN.get());
        this.registerBasicTable(BlocksPM.STAINED_SKYGLASS_CYAN.get());
        this.registerBasicTable(BlocksPM.STAINED_SKYGLASS_GRAY.get());
        this.registerBasicTable(BlocksPM.STAINED_SKYGLASS_GREEN.get());
        this.registerBasicTable(BlocksPM.STAINED_SKYGLASS_LIGHT_BLUE.get());
        this.registerBasicTable(BlocksPM.STAINED_SKYGLASS_LIGHT_GRAY.get());
        this.registerBasicTable(BlocksPM.STAINED_SKYGLASS_LIME.get());
        this.registerBasicTable(BlocksPM.STAINED_SKYGLASS_MAGENTA.get());
        this.registerBasicTable(BlocksPM.STAINED_SKYGLASS_ORANGE.get());
        this.registerBasicTable(BlocksPM.STAINED_SKYGLASS_PINK.get());
        this.registerBasicTable(BlocksPM.STAINED_SKYGLASS_PURPLE.get());
        this.registerBasicTable(BlocksPM.STAINED_SKYGLASS_RED.get());
        this.registerBasicTable(BlocksPM.STAINED_SKYGLASS_WHITE.get());
        this.registerBasicTable(BlocksPM.STAINED_SKYGLASS_YELLOW.get());
        
        this.registerBasicTable(BlocksPM.SKYGLASS_PANE.get());
        this.registerBasicTable(BlocksPM.STAINED_SKYGLASS_PANE_BLACK.get());
        this.registerBasicTable(BlocksPM.STAINED_SKYGLASS_PANE_BLUE.get());
        this.registerBasicTable(BlocksPM.STAINED_SKYGLASS_PANE_BROWN.get());
        this.registerBasicTable(BlocksPM.STAINED_SKYGLASS_PANE_CYAN.get());
        this.registerBasicTable(BlocksPM.STAINED_SKYGLASS_PANE_GRAY.get());
        this.registerBasicTable(BlocksPM.STAINED_SKYGLASS_PANE_GREEN.get());
        this.registerBasicTable(BlocksPM.STAINED_SKYGLASS_PANE_LIGHT_BLUE.get());
        this.registerBasicTable(BlocksPM.STAINED_SKYGLASS_PANE_LIGHT_GRAY.get());
        this.registerBasicTable(BlocksPM.STAINED_SKYGLASS_PANE_LIME.get());
        this.registerBasicTable(BlocksPM.STAINED_SKYGLASS_PANE_MAGENTA.get());
        this.registerBasicTable(BlocksPM.STAINED_SKYGLASS_PANE_ORANGE.get());
        this.registerBasicTable(BlocksPM.STAINED_SKYGLASS_PANE_PINK.get());
        this.registerBasicTable(BlocksPM.STAINED_SKYGLASS_PANE_PURPLE.get());
        this.registerBasicTable(BlocksPM.STAINED_SKYGLASS_PANE_RED.get());
        this.registerBasicTable(BlocksPM.STAINED_SKYGLASS_PANE_WHITE.get());
        this.registerBasicTable(BlocksPM.STAINED_SKYGLASS_PANE_YELLOW.get());
    }
    
    private void registerRitualCandleLootTables() {
        this.registerBasicTable(BlocksPM.RITUAL_CANDLE_BLACK.get());
        this.registerBasicTable(BlocksPM.RITUAL_CANDLE_BLUE.get());
        this.registerBasicTable(BlocksPM.RITUAL_CANDLE_BROWN.get());
        this.registerBasicTable(BlocksPM.RITUAL_CANDLE_CYAN.get());
        this.registerBasicTable(BlocksPM.RITUAL_CANDLE_GRAY.get());
        this.registerBasicTable(BlocksPM.RITUAL_CANDLE_GREEN.get());
        this.registerBasicTable(BlocksPM.RITUAL_CANDLE_LIGHT_BLUE.get());
        this.registerBasicTable(BlocksPM.RITUAL_CANDLE_LIGHT_GRAY.get());
        this.registerBasicTable(BlocksPM.RITUAL_CANDLE_LIME.get());
        this.registerBasicTable(BlocksPM.RITUAL_CANDLE_MAGENTA.get());
        this.registerBasicTable(BlocksPM.RITUAL_CANDLE_ORANGE.get());
        this.registerBasicTable(BlocksPM.RITUAL_CANDLE_PINK.get());
        this.registerBasicTable(BlocksPM.RITUAL_CANDLE_PURPLE.get());
        this.registerBasicTable(BlocksPM.RITUAL_CANDLE_RED.get());
        this.registerBasicTable(BlocksPM.RITUAL_CANDLE_WHITE.get());
        this.registerBasicTable(BlocksPM.RITUAL_CANDLE_YELLOW.get());
    }

    private void registerManaFontLootTables() {
        this.registerBasicTable(BlocksPM.ARTIFICIAL_FONT_EARTH.get());
        this.registerBasicTable(BlocksPM.ARTIFICIAL_FONT_SEA.get());
        this.registerBasicTable(BlocksPM.ARTIFICIAL_FONT_SKY.get());
        this.registerBasicTable(BlocksPM.ARTIFICIAL_FONT_SUN.get());
        this.registerBasicTable(BlocksPM.ARTIFICIAL_FONT_MOON.get());
        this.registerBasicTable(BlocksPM.ARTIFICIAL_FONT_BLOOD.get());
        this.registerBasicTable(BlocksPM.ARTIFICIAL_FONT_INFERNAL.get());
        this.registerBasicTable(BlocksPM.ARTIFICIAL_FONT_VOID.get());
        this.registerBasicTable(BlocksPM.ARTIFICIAL_FONT_HALLOWED.get());
        this.registerBasicTable(BlocksPM.FORBIDDEN_FONT_EARTH.get());
        this.registerBasicTable(BlocksPM.FORBIDDEN_FONT_SEA.get());
        this.registerBasicTable(BlocksPM.FORBIDDEN_FONT_SKY.get());
        this.registerBasicTable(BlocksPM.FORBIDDEN_FONT_SUN.get());
        this.registerBasicTable(BlocksPM.FORBIDDEN_FONT_MOON.get());
        this.registerBasicTable(BlocksPM.FORBIDDEN_FONT_BLOOD.get());
        this.registerBasicTable(BlocksPM.FORBIDDEN_FONT_INFERNAL.get());
        this.registerBasicTable(BlocksPM.FORBIDDEN_FONT_VOID.get());
        this.registerBasicTable(BlocksPM.FORBIDDEN_FONT_HALLOWED.get());
        this.registerBasicTable(BlocksPM.HEAVENLY_FONT_EARTH.get());
        this.registerBasicTable(BlocksPM.HEAVENLY_FONT_SEA.get());
        this.registerBasicTable(BlocksPM.HEAVENLY_FONT_SKY.get());
        this.registerBasicTable(BlocksPM.HEAVENLY_FONT_SUN.get());
        this.registerBasicTable(BlocksPM.HEAVENLY_FONT_MOON.get());
        this.registerBasicTable(BlocksPM.HEAVENLY_FONT_BLOOD.get());
        this.registerBasicTable(BlocksPM.HEAVENLY_FONT_INFERNAL.get());
        this.registerBasicTable(BlocksPM.HEAVENLY_FONT_VOID.get());
        this.registerBasicTable(BlocksPM.HEAVENLY_FONT_HALLOWED.get());
    }
    
    public static LootTableProvider.SubProviderEntry getSubProviderEntry() {
        return new LootTableProvider.SubProviderEntry(BlockLootTables::new, LootContextParamSets.BLOCK);
    }
}
