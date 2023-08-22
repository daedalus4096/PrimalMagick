package com.verdantartifice.primalmagick.datagen.lang;

import java.util.List;

import com.verdantartifice.primalmagick.client.config.KeyBindings;
import com.verdantartifice.primalmagick.common.armortrim.TrimMaterialsPM;
import com.verdantartifice.primalmagick.common.armortrim.TrimPatternsPM;
import com.verdantartifice.primalmagick.common.attunements.AttunementThreshold;
import com.verdantartifice.primalmagick.common.attunements.AttunementType;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.damagesource.DamageTypesPM;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.research.KnowledgeType;
import com.verdantartifice.primalmagick.common.research.ResearchDisciplines;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.spells.mods.EmptySpellMod;
import com.verdantartifice.primalmagick.common.spells.payloads.EarthDamageSpellPayload;
import com.verdantartifice.primalmagick.common.spells.vehicles.TouchSpellVehicle;
import com.verdantartifice.primalmagick.common.stats.StatsPM;
import com.verdantartifice.primalmagick.common.wands.WandCap;
import com.verdantartifice.primalmagick.common.wands.WandCore;
import com.verdantartifice.primalmagick.common.wands.WandGem;

import net.minecraft.data.PackOutput;

/**
 * Language data provider for US English.
 * 
 * @author Daedalus4096
 */
public class LanguageProviderEnUs extends AbstractLanguageProviderPM {
    public LanguageProviderEnUs(PackOutput output) {
        super(output, "en_us");
    }

    @Override
    protected void addLocalizations() {
        // Generate magickal source localizations; it's important to do these first so that the language provider has access to
        // the localized source names for the source-multiplied language builders used later.
        this.source(Source.EARTH).name("Earth")
            .attunement("Minor attunement to the Earth source makes it easier to channel mana.  In practice, I'll pay 5% less Earth mana for all purposes.<BR>Lesser attunement to the Earth will grant me increased stamina, allowing me to swing swords and tools faster without tiring.<BR>Finally, greater attunement to the Earth will cause the very ground to rise beneath my feet when I walk, allowing me to step up the full height of a block without needing to jump.")
            .build();
        this.source(Source.SEA).name("Sea").build();
        this.source(Source.SKY).name("Sky").build();
        this.source(Source.SUN).name("Sun").build();
        this.source(Source.MOON).name("Moon").build();
        this.source(Source.BLOOD).name("Blood").build();
        this.source(Source.INFERNAL).name("Infernal").build();
        this.source(Source.VOID).name("Void").build();
        this.source(Source.HALLOWED).name("Hallowed").build();
        this.add(Source.getUnknownTranslationKey(), "Unknown");
        
        // Generate item group (i.e. creative tab) localizations
        this.add("itemGroup.primalmagick", "Primal Magick");
        
        // Generate block localizations
        this.block(BlocksPM.MARBLE_RAW).name("Marble").build();
        this.block(BlocksPM.MARBLE_SLAB).name("Marble Slab").build();
        this.block(BlocksPM.MARBLE_STAIRS).name("Marble Stairs").build();
        this.block(BlocksPM.MARBLE_WALL).name("Marble Wall").build();
        this.block(BlocksPM.MARBLE_BRICKS).name("Marble Bricks").build();
        this.block(BlocksPM.MARBLE_BRICK_SLAB).name("Marble Brick Slab").build();
        this.block(BlocksPM.MARBLE_BRICK_STAIRS).name("Marble Brick Stairs").build();
        this.block(BlocksPM.MARBLE_BRICK_WALL).name("Marble Brick Wall").build();
        this.block(BlocksPM.MARBLE_PILLAR).name("Marble Pillar").build();
        this.block(BlocksPM.MARBLE_CHISELED).name("Chiseled Marble").build();
        this.block(BlocksPM.MARBLE_RUNED).name("Runed Marble").build();
        this.block(BlocksPM.MARBLE_TILES).name("Marble Tiles").build();
        this.block(BlocksPM.MARBLE_ENCHANTED).name("Enchanted Marble").build();
        this.block(BlocksPM.MARBLE_ENCHANTED_SLAB).name("Enchanted Marble Slab").build();
        this.block(BlocksPM.MARBLE_ENCHANTED_STAIRS).name("Enchanted Marble Stairs").build();
        this.block(BlocksPM.MARBLE_ENCHANTED_WALL).name("Enchanted Marble Wall").build();
        this.block(BlocksPM.MARBLE_ENCHANTED_BRICKS).name("Enchanted Marble Bricks").build();
        this.block(BlocksPM.MARBLE_ENCHANTED_BRICK_SLAB).name("Enchanted Marble Brick Slab").build();
        this.block(BlocksPM.MARBLE_ENCHANTED_BRICK_STAIRS).name("Enchanted Marble Brick Stairs").build();
        this.block(BlocksPM.MARBLE_ENCHANTED_BRICK_WALL).name("Enchanted Marble Brick Wall").build();
        this.block(BlocksPM.MARBLE_ENCHANTED_PILLAR).name("Enchanted Marble Pillar").build();
        this.block(BlocksPM.MARBLE_ENCHANTED_CHISELED).name("Enchanted Chiseled Marble").build();
        this.block(BlocksPM.MARBLE_ENCHANTED_RUNED).name("Enchanted Runed Marble").build();
        this.block(BlocksPM.MARBLE_SMOKED).name("Smoked Marble").build();
        this.block(BlocksPM.MARBLE_SMOKED_SLAB).name("Smoked Marble Slab").build();
        this.block(BlocksPM.MARBLE_SMOKED_STAIRS).name("Smoked Marble Stairs").build();
        this.block(BlocksPM.MARBLE_SMOKED_WALL).name("Smoked Marble Wall").build();
        this.block(BlocksPM.MARBLE_SMOKED_BRICKS).name("Smoked Marble Bricks").build();
        this.block(BlocksPM.MARBLE_SMOKED_BRICK_SLAB).name("Smoked Marble Brick Slab").build();
        this.block(BlocksPM.MARBLE_SMOKED_BRICK_STAIRS).name("Smoked Marble Brick Stairs").build();
        this.block(BlocksPM.MARBLE_SMOKED_BRICK_WALL).name("Smoked Marble Brick Wall").build();
        this.block(BlocksPM.MARBLE_SMOKED_PILLAR).name("Smoked Marble Pillar").build();
        this.block(BlocksPM.MARBLE_SMOKED_CHISELED).name("Smoked Chiseled Marble").build();
        this.block(BlocksPM.MARBLE_SMOKED_RUNED).name("Smoked Runed Marble").build();
        this.block(BlocksPM.MARBLE_HALLOWED).name("Hallowed Marble").build();
        this.block(BlocksPM.MARBLE_HALLOWED_SLAB).name("Hallowed Marble Slab").build();
        this.block(BlocksPM.MARBLE_HALLOWED_STAIRS).name("Hallowed Marble Stairs").build();
        this.block(BlocksPM.MARBLE_HALLOWED_WALL).name("Hallowed Marble Wall").build();
        this.block(BlocksPM.MARBLE_HALLOWED_BRICKS).name("Hallowed Marble Bricks").build();
        this.block(BlocksPM.MARBLE_HALLOWED_BRICK_SLAB).name("Hallowed Marble Brick Slab").build();
        this.block(BlocksPM.MARBLE_HALLOWED_BRICK_STAIRS).name("Hallowed Marble Brick Stairs").build();
        this.block(BlocksPM.MARBLE_HALLOWED_BRICK_WALL).name("Hallowed Marble Brick Wall").build();
        this.block(BlocksPM.MARBLE_HALLOWED_PILLAR).name("Hallowed Marble Pillar").build();
        this.block(BlocksPM.MARBLE_HALLOWED_CHISELED).name("Hallowed Chiseled Marble").build();
        this.block(BlocksPM.MARBLE_HALLOWED_RUNED).name("Hallowed Runed Marble").build();
        this.block(BlocksPM.SUNWOOD_LOG).name("Sunwood Log").build();
        this.block(BlocksPM.STRIPPED_SUNWOOD_LOG).name("Stripped Sunwood Log").build();
        this.block(BlocksPM.SUNWOOD_WOOD).name("Sunwood Wood").build();
        this.block(BlocksPM.STRIPPED_SUNWOOD_WOOD).name("Stripped Sunwood Wood").build();
        this.block(BlocksPM.SUNWOOD_LEAVES).name("Sunwood Leaves").build();
        this.block(BlocksPM.SUNWOOD_SAPLING).name("Sunwood Sapling").build();
        this.block(BlocksPM.SUNWOOD_PLANKS).name("Sunwood Planks").build();
        this.block(BlocksPM.SUNWOOD_SLAB).name("Sunwood Slab").build();
        this.block(BlocksPM.SUNWOOD_STAIRS).name("Sunwood Stairs").build();
        this.block(BlocksPM.SUNWOOD_PILLAR).name("Sunwood Pillar").build();
        this.block(BlocksPM.MOONWOOD_LOG).name("Moonwood Log").build();
        this.block(BlocksPM.STRIPPED_MOONWOOD_LOG).name("Stripped Moonwood Log").build();
        this.block(BlocksPM.MOONWOOD_WOOD).name("Moonwood Wood").build();
        this.block(BlocksPM.STRIPPED_MOONWOOD_WOOD).name("Stripped Moonwood Wood").build();
        this.block(BlocksPM.MOONWOOD_LEAVES).name("Moonwood Leaves").build();
        this.block(BlocksPM.MOONWOOD_SAPLING).name("Moonwood Sapling").build();
        this.block(BlocksPM.MOONWOOD_PLANKS).name("Moonwood Planks").build();
        this.block(BlocksPM.MOONWOOD_SLAB).name("Moonwood Slab").build();
        this.block(BlocksPM.MOONWOOD_STAIRS).name("Moonwood Stairs").build();
        this.block(BlocksPM.MOONWOOD_PILLAR).name("Moonwood Pillar").build();
        this.block(BlocksPM.HALLOWOOD_LOG).name("Hallowood Log").build();
        this.block(BlocksPM.STRIPPED_HALLOWOOD_LOG).name("Stripped Hallowood Log").build();
        this.block(BlocksPM.HALLOWOOD_WOOD).name("Hallowood Wood").build();
        this.block(BlocksPM.STRIPPED_HALLOWOOD_WOOD).name("Stripped Hallowood Wood").build();
        this.block(BlocksPM.HALLOWOOD_LEAVES).name("Hallowood Leaves").build();
        this.block(BlocksPM.HALLOWOOD_SAPLING).name("Hallowood Sapling").build();
        this.block(BlocksPM.HALLOWOOD_PLANKS).name("Hallowood Planks").build();
        this.block(BlocksPM.HALLOWOOD_SLAB).name("Hallowood Slab").build();
        this.block(BlocksPM.HALLOWOOD_STAIRS).name("Hallowood Stairs").build();
        this.block(BlocksPM.HALLOWOOD_PILLAR).name("Hallowood Pillar").build();
        this.block(BlocksPM.ARCANE_WORKBENCH).name("Arcane Workbench").build();
        this.block(BlocksPM.ANCIENT_FONT_EARTH).name("Ancient Earth Font").build();
        this.block(BlocksPM.ANCIENT_FONT_SEA).name("Ancient Sea Font").build();
        this.block(BlocksPM.ANCIENT_FONT_SKY).name("Ancient Sky Font").build();
        this.block(BlocksPM.ANCIENT_FONT_SUN).name("Ancient Sun Font").build();
        this.block(BlocksPM.ANCIENT_FONT_MOON).name("Ancient Moon Font").build();
        this.block(BlocksPM.ARTIFICIAL_FONT_EARTH).name("Artificial Earth Font").build();
        this.block(BlocksPM.ARTIFICIAL_FONT_SEA).name("Artificial Sea Font").build();
        this.block(BlocksPM.ARTIFICIAL_FONT_SKY).name("Artificial Sky Font").build();
        this.block(BlocksPM.ARTIFICIAL_FONT_SUN).name("Artificial Sun Font").build();
        this.block(BlocksPM.ARTIFICIAL_FONT_MOON).name("Artificial Moon Font").build();
        this.block(BlocksPM.ARTIFICIAL_FONT_BLOOD).name("Artificial Blood Font").build();
        this.block(BlocksPM.ARTIFICIAL_FONT_INFERNAL).name("Artificial Infernal Font").build();
        this.block(BlocksPM.ARTIFICIAL_FONT_VOID).name("Artificial Void Font").build();
        this.block(BlocksPM.ARTIFICIAL_FONT_HALLOWED).name("Artificial Hallowed Font").build();
        this.block(BlocksPM.FORBIDDEN_FONT_EARTH).name("Forbidden Earth Font").build();
        this.block(BlocksPM.FORBIDDEN_FONT_SEA).name("Forbidden Sea Font").build();
        this.block(BlocksPM.FORBIDDEN_FONT_SKY).name("Forbidden Sky Font").build();
        this.block(BlocksPM.FORBIDDEN_FONT_SUN).name("Forbidden Sun Font").build();
        this.block(BlocksPM.FORBIDDEN_FONT_MOON).name("Forbidden Moon Font").build();
        this.block(BlocksPM.FORBIDDEN_FONT_BLOOD).name("Forbidden Blood Font").build();
        this.block(BlocksPM.FORBIDDEN_FONT_INFERNAL).name("Forbidden Infernal Font").build();
        this.block(BlocksPM.FORBIDDEN_FONT_VOID).name("Forbidden Void Font").build();
        this.block(BlocksPM.FORBIDDEN_FONT_HALLOWED).name("Forbidden Hallowed Font").build();
        this.block(BlocksPM.HEAVENLY_FONT_EARTH).name("Heavenly Earth Font").build();
        this.block(BlocksPM.HEAVENLY_FONT_SEA).name("Heavenly Sea Font").build();
        this.block(BlocksPM.HEAVENLY_FONT_SKY).name("Heavenly Sky Font").build();
        this.block(BlocksPM.HEAVENLY_FONT_SUN).name("Heavenly Sun Font").build();
        this.block(BlocksPM.HEAVENLY_FONT_MOON).name("Heavenly Moon Font").build();
        this.block(BlocksPM.HEAVENLY_FONT_BLOOD).name("Heavenly Blood Font").build();
        this.block(BlocksPM.HEAVENLY_FONT_INFERNAL).name("Heavenly Infernal Font").build();
        this.block(BlocksPM.HEAVENLY_FONT_VOID).name("Heavenly Void Font").build();
        this.block(BlocksPM.HEAVENLY_FONT_HALLOWED).name("Heavenly Hallowed Font").build();
        this.block(BlocksPM.WAND_ASSEMBLY_TABLE).name("Wand Assembly Table").build();
        this.block(BlocksPM.WOOD_TABLE).name("Wood Table").build();
        this.block(BlocksPM.ANALYSIS_TABLE).name("Analysis Table").build();
        this.block(BlocksPM.ESSENCE_FURNACE).name("Essence Furnace").build();
        this.block(BlocksPM.CALCINATOR_BASIC).name("Basic Calcinator").build();
        this.block(BlocksPM.CALCINATOR_ENCHANTED).name("Enchanted Calcinator").build();
        this.block(BlocksPM.CALCINATOR_FORBIDDEN).name("Forbidden Calcinator").build();
        this.block(BlocksPM.CALCINATOR_HEAVENLY).name("Heavenly Calcinator").build();
        this.block(BlocksPM.WAND_INSCRIPTION_TABLE).name("Wand Inscription Table").build();
        this.block(BlocksPM.SPELLCRAFTING_ALTAR).name("Spellcrafting Altar").build();
        this.block(BlocksPM.WAND_CHARGER).name("Wand Charger").build();
        this.block(BlocksPM.CONSECRATION_FIELD).name("Consecration Field").build();
        this.block(BlocksPM.INFUSED_STONE_EARTH).name("Earth-Infused Stone").build();
        this.block(BlocksPM.INFUSED_STONE_SEA).name("Sea-Infused Stone").build();
        this.block(BlocksPM.INFUSED_STONE_SKY).name("Sky-Infused Stone").build();
        this.block(BlocksPM.INFUSED_STONE_SUN).name("Sun-Infused Stone").build();
        this.block(BlocksPM.INFUSED_STONE_MOON).name("Moon-Infused Stone").build();
        this.block(BlocksPM.GLOW_FIELD).name("Glow Field").build();
        this.block(BlocksPM.RESEARCH_TABLE).name("Research Table").build();
        this.block(BlocksPM.SALT_TRAIL).name("Salt Trail").build();
        this.block(BlocksPM.RITUAL_ALTAR).name("Ritual Altar").build();
        this.block(BlocksPM.ROCK_SALT_ORE).name("Rock Salt Ore").build();
        this.block(BlocksPM.SKYGLASS).name("Skyglass").build();
        this.block(BlocksPM.STAINED_SKYGLASS_BLACK).name("Black Stained Skyglass").build();
        this.block(BlocksPM.STAINED_SKYGLASS_BLUE).name("Blue Stained Skyglass").build();
        this.block(BlocksPM.STAINED_SKYGLASS_BROWN).name("Brown Stained Skyglass").build();
        this.block(BlocksPM.STAINED_SKYGLASS_CYAN).name("Cyan Stained Skyglass").build();
        this.block(BlocksPM.STAINED_SKYGLASS_GRAY).name("Gray Stained Skyglass").build();
        this.block(BlocksPM.STAINED_SKYGLASS_GREEN).name("Green Stained Skyglass").build();
        this.block(BlocksPM.STAINED_SKYGLASS_LIGHT_BLUE).name("Light Blue Stained Skyglass").build();
        this.block(BlocksPM.STAINED_SKYGLASS_LIGHT_GRAY).name("Light Gray Stained Skyglass").build();
        this.block(BlocksPM.STAINED_SKYGLASS_LIME).name("Lime Stained Skyglass").build();
        this.block(BlocksPM.STAINED_SKYGLASS_MAGENTA).name("Magenta Stained Skyglass").build();
        this.block(BlocksPM.STAINED_SKYGLASS_ORANGE).name("Orange Stained Skyglass").build();
        this.block(BlocksPM.STAINED_SKYGLASS_PINK).name("Pink Stained Skyglass").build();
        this.block(BlocksPM.STAINED_SKYGLASS_PURPLE).name("Purple Stained Skyglass").build();
        this.block(BlocksPM.STAINED_SKYGLASS_RED).name("Red Stained Skyglass").build();
        this.block(BlocksPM.STAINED_SKYGLASS_WHITE).name("White Stained Skyglass").build();
        this.block(BlocksPM.STAINED_SKYGLASS_YELLOW).name("Yellow Stained Skyglass").build();
        this.block(BlocksPM.SKYGLASS_PANE).name("Skyglass Pane").build();
        this.block(BlocksPM.STAINED_SKYGLASS_PANE_BLACK).name("Black Stained Skyglass Pane").build();
        this.block(BlocksPM.STAINED_SKYGLASS_PANE_BLUE).name("Blue Stained Skyglass Pane").build();
        this.block(BlocksPM.STAINED_SKYGLASS_PANE_BROWN).name("Brown Stained Skyglass Pane").build();
        this.block(BlocksPM.STAINED_SKYGLASS_PANE_CYAN).name("Cyan Stained Skyglass Pane").build();
        this.block(BlocksPM.STAINED_SKYGLASS_PANE_GRAY).name("Gray Stained Skyglass Pane").build();
        this.block(BlocksPM.STAINED_SKYGLASS_PANE_GREEN).name("Green Stained Skyglass Pane").build();
        this.block(BlocksPM.STAINED_SKYGLASS_PANE_LIGHT_BLUE).name("Light Blue Stained Skyglass Pane").build();
        this.block(BlocksPM.STAINED_SKYGLASS_PANE_LIGHT_GRAY).name("Light Gray Stained Skyglass Pane").build();
        this.block(BlocksPM.STAINED_SKYGLASS_PANE_LIME).name("Lime Stained Skyglass Pane").build();
        this.block(BlocksPM.STAINED_SKYGLASS_PANE_MAGENTA).name("Magenta Stained Skyglass Pane").build();
        this.block(BlocksPM.STAINED_SKYGLASS_PANE_ORANGE).name("Orange Stained Skyglass Pane").build();
        this.block(BlocksPM.STAINED_SKYGLASS_PANE_PINK).name("Pink Stained Skyglass Pane").build();
        this.block(BlocksPM.STAINED_SKYGLASS_PANE_PURPLE).name("Purple Stained Skyglass Pane").build();
        this.block(BlocksPM.STAINED_SKYGLASS_PANE_RED).name("Red Stained Skyglass Pane").build();
        this.block(BlocksPM.STAINED_SKYGLASS_PANE_WHITE).name("White Stained Skyglass Pane").build();
        this.block(BlocksPM.STAINED_SKYGLASS_PANE_YELLOW).name("Yellow Stained Skyglass Pane").build();
        this.block(BlocksPM.PRIMALITE_BLOCK).name("Primalite Block").build();
        this.block(BlocksPM.HEXIUM_BLOCK).name("Hexium Block").build();
        this.block(BlocksPM.HALLOWSTEEL_BLOCK).name("Hallowsteel Block").build();
        this.block(BlocksPM.QUARTZ_ORE).name("Quartz Ore").build();
        this.block(BlocksPM.SUNLAMP).name("Sunlamp").build();
        this.block(BlocksPM.SPIRIT_LANTERN).name("Spirit Lantern").build();
        this.block(BlocksPM.RITUAL_CANDLE_BLACK).name("Black Ritual Candle").build();
        this.block(BlocksPM.RITUAL_CANDLE_BLUE).name("Blue Ritual Candle").build();
        this.block(BlocksPM.RITUAL_CANDLE_BROWN).name("Brown Ritual Candle").build();
        this.block(BlocksPM.RITUAL_CANDLE_CYAN).name("Cyan Ritual Candle").build();
        this.block(BlocksPM.RITUAL_CANDLE_GRAY).name("Gray Ritual Candle").build();
        this.block(BlocksPM.RITUAL_CANDLE_GREEN).name("Green Ritual Candle").build();
        this.block(BlocksPM.RITUAL_CANDLE_LIGHT_BLUE).name("Light Blue Ritual Candle").build();
        this.block(BlocksPM.RITUAL_CANDLE_LIGHT_GRAY).name("Light Gray Ritual Candle").build();
        this.block(BlocksPM.RITUAL_CANDLE_LIME).name("Lime Ritual Candle").build();
        this.block(BlocksPM.RITUAL_CANDLE_MAGENTA).name("Magenta Ritual Candle").build();
        this.block(BlocksPM.RITUAL_CANDLE_ORANGE).name("Orange Ritual Candle").build();
        this.block(BlocksPM.RITUAL_CANDLE_PINK).name("Pink Ritual Candle").build();
        this.block(BlocksPM.RITUAL_CANDLE_PURPLE).name("Purple Ritual Candle").build();
        this.block(BlocksPM.RITUAL_CANDLE_RED).name("Red Ritual Candle").build();
        this.block(BlocksPM.RITUAL_CANDLE_WHITE).name("White Ritual Candle").build();
        this.block(BlocksPM.RITUAL_CANDLE_YELLOW).name("Yellow Ritual Candle").build();
        this.block(BlocksPM.OFFERING_PEDESTAL).name("Offering Pedestal").build();
        this.block(BlocksPM.INCENSE_BRAZIER).name("Incense Brazier").build();
        this.block(BlocksPM.RITUAL_LECTERN).name("Ritual Lectern").build();
        this.block(BlocksPM.RITUAL_BELL).name("Ritual Bell").build();
        this.block(BlocksPM.BLOODLETTER).name("Bloodletter").build();
        this.block(BlocksPM.SOUL_ANVIL).name("Soul Anvil").build();
        this.block(BlocksPM.CELESTIAL_HARP).name("Celestial Harp").build();
        this.block(BlocksPM.ENTROPY_SINK).name("Entropy Sink").build();
        this.block(BlocksPM.RUNESCRIBING_ALTAR_BASIC).name("Basic Runescribing Altar").build();
        this.block(BlocksPM.RUNESCRIBING_ALTAR_ENCHANTED).name("Enchanted Runescribing Altar").build();
        this.block(BlocksPM.RUNESCRIBING_ALTAR_FORBIDDEN).name("Forbidden Runescribing Altar").build();
        this.block(BlocksPM.RUNESCRIBING_ALTAR_HEAVENLY).name("Heavenly Runescribing Altar").build();
        this.block(BlocksPM.RUNECARVING_TABLE).name("Runecarving Table").build();
        this.block(BlocksPM.RUNIC_GRINDSTONE).name("Runic Grindstone").build();
        this.block(BlocksPM.HONEY_EXTRACTOR).name("Honey Extractor").build();
        this.block(BlocksPM.PRIMALITE_GOLEM_CONTROLLER).name("Primalite Golem Controller").build();
        this.block(BlocksPM.HEXIUM_GOLEM_CONTROLLER).name("Hexium Golem Controller").build();
        this.block(BlocksPM.HALLOWSTEEL_GOLEM_CONTROLLER).name("Hallowsteel Golem Controller").build();
        this.block(BlocksPM.SANGUINE_CRUCIBLE).name("Sanguine Crucible").build();
        this.block(BlocksPM.CONCOCTER).name("Concocter").build();
        this.block(BlocksPM.AUTO_CHARGER).name("Auto-Charger").build();
        this.block(BlocksPM.IGNYX_BLOCK).name("Block of Ignyx").build();
        this.block(BlocksPM.SALT_BLOCK).name("Block of Refined Salt").build();
        this.block(BlocksPM.ESSENCE_TRANSMUTER).name("Essence Transmuter").build();
        this.block(BlocksPM.DISSOLUTION_CHAMBER).name("Dissolution Chamber").build();
        this.block(BlocksPM.ZEPHYR_ENGINE).name("Zephyr Engine").build();
        this.block(BlocksPM.VOID_TURBINE).name("Void Turbine").build();
        this.block(BlocksPM.ESSENCE_CASK_ENCHANTED).name("Enchanted Essence Cask").build();
        this.block(BlocksPM.ESSENCE_CASK_FORBIDDEN).name("Forbidden Essence Cask").build();
        this.block(BlocksPM.ESSENCE_CASK_HEAVENLY).name("Heavenly Essence Cask").build();
        this.block(BlocksPM.WAND_GLAMOUR_TABLE).name("Wand Glamour Table").build();
        this.block(BlocksPM.TREEFOLK_SPROUT).name("Treefolk Sprout").build();
        
        // Generate item localizations
        this.item(ItemsPM.HALLOWED_ORB).name("Hallowed Orb").tooltip("A sense of peace washes over", "you as you hold this").build();
        this.item(ItemsPM.RUNE_EARTH).name("Earth Rune").tooltip("\"Teq\"").build();
        
        // Generate entity type localizations
        this.entity(EntityTypesPM.TREEFOLK).name("Treefolk").build();
        this.entity(EntityTypesPM.INNER_DEMON).name("Inner Demon").build();
        this.entity(EntityTypesPM.FRIENDLY_WITCH).name("Witch").build();
        this.entity(EntityTypesPM.PRIMALITE_GOLEM).name("Primalite Golem").build();
        this.entity(EntityTypesPM.HEXIUM_GOLEM).name("Hexium Golem").build();
        this.entity(EntityTypesPM.HALLOWSTEEL_GOLEM).name("Hallowsteel Golem").build();
        this.entity(EntityTypesPM.BASIC_EARTH_PIXIE).name("Earth Pixie").build();
        this.entity(EntityTypesPM.GRAND_EARTH_PIXIE).name("Grand Earth Pixie").build();
        this.entity(EntityTypesPM.MAJESTIC_EARTH_PIXIE).name("Majestic Earth Pixie").build();
        this.entity(EntityTypesPM.BASIC_SEA_PIXIE).name("Sea Pixie").build();
        this.entity(EntityTypesPM.GRAND_SEA_PIXIE).name("Grand Sea Pixie").build();
        this.entity(EntityTypesPM.MAJESTIC_SEA_PIXIE).name("Majestic Sea Pixie").build();
        this.entity(EntityTypesPM.BASIC_SKY_PIXIE).name("Sky Pixie").build();
        this.entity(EntityTypesPM.GRAND_SKY_PIXIE).name("Grand Sky Pixie").build();
        this.entity(EntityTypesPM.MAJESTIC_SKY_PIXIE).name("Majestic Sky Pixie").build();
        this.entity(EntityTypesPM.BASIC_SUN_PIXIE).name("Sun Pixie").build();
        this.entity(EntityTypesPM.GRAND_SUN_PIXIE).name("Grand Sun Pixie").build();
        this.entity(EntityTypesPM.MAJESTIC_SUN_PIXIE).name("Majestic Sun Pixie").build();
        this.entity(EntityTypesPM.BASIC_MOON_PIXIE).name("Moon Pixie").build();
        this.entity(EntityTypesPM.GRAND_MOON_PIXIE).name("Grand Moon Pixie").build();
        this.entity(EntityTypesPM.MAJESTIC_MOON_PIXIE).name("Majestic Moon Pixie").build();
        this.entity(EntityTypesPM.BASIC_BLOOD_PIXIE).name("Blood Pixie").build();
        this.entity(EntityTypesPM.GRAND_BLOOD_PIXIE).name("Grand Blood Pixie").build();
        this.entity(EntityTypesPM.MAJESTIC_BLOOD_PIXIE).name("Majestic Blood Pixie").build();
        this.entity(EntityTypesPM.BASIC_INFERNAL_PIXIE).name("Infernal Pixie").build();
        this.entity(EntityTypesPM.GRAND_INFERNAL_PIXIE).name("Grand Infernal Pixie").build();
        this.entity(EntityTypesPM.MAJESTIC_INFERNAL_PIXIE).name("Majestic Infernal Pixie").build();
        this.entity(EntityTypesPM.BASIC_VOID_PIXIE).name("Void Pixie").build();
        this.entity(EntityTypesPM.GRAND_VOID_PIXIE).name("Grand Void Pixie").build();
        this.entity(EntityTypesPM.MAJESTIC_VOID_PIXIE).name("Majestic Void Pixie").build();
        this.entity(EntityTypesPM.BASIC_HALLOWED_PIXIE).name("Hallowed Pixie").build();
        this.entity(EntityTypesPM.GRAND_HALLOWED_PIXIE).name("Grand Hallowed Pixie").build();
        this.entity(EntityTypesPM.MAJESTIC_HALLOWED_PIXIE).name("Majestic Hallowed Pixie").build();
        this.entity(EntityTypesPM.FLYING_CARPET).name("Flying Carpet").build();
        this.entity(EntityTypesPM.SIN_CRYSTAL).name("Sin Crystal").build();
        
        // Generate enchantment localizations
        this.enchantment(EnchantmentsPM.LIFESTEAL).name("Lifesteal")
            .description("Grants a chance for the wielder to heal themselves a small amount when striking a creature.")
            .fullRuneText("The Lifesteal enchantment can be imbued through the use of Absorb, Self, and Blood runes.  It can be applied to any sword, axe, or trident.  When applied, it grants a chance for the wielder to heal themselves a small amount when striking a creature.")
            .partialRuneText("The Lifesteal enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any sword, axe, or trident.  When applied, it grants a chance for the wielder to heal themselves a small amount when striking a creature.")
            .build();
        
        // Generate armor trim localizations
        this.trimMaterial("runic_trim_materials", List.of(TrimMaterialsPM.RUNE_EARTH, TrimMaterialsPM.RUNE_SEA, TrimMaterialsPM.RUNE_SKY, TrimMaterialsPM.RUNE_SUN, TrimMaterialsPM.RUNE_MOON, TrimMaterialsPM.RUNE_BLOOD, TrimMaterialsPM.RUNE_INFERNAL, TrimMaterialsPM.RUNE_VOID, TrimMaterialsPM.RUNE_HALLOWED))
            .namePattern("%s Rune Material").build();
        this.trimPattern(TrimPatternsPM.RUNIC).name("Runic Armor Trim").build();
        
        // Generate death message localizations for damage types
        this.damageType(DamageTypesPM.BLEEDING).name("%1$s bled to death").player("%1$s bled to death whilst fighting %2$s").build();
        this.damageType(DamageTypesPM.HELLISH_CHAIN).name("%1$s was killed by %2$s").item("%1$s was killed by %2$s using %3$s").build();
        
        // Generate wand component localizations
        this.wandComponent(WandCore.HEARTWOOD).name("Heartwood").build();
        this.wandComponent(WandCap.IRON).name("Iron-Shod").build();
        this.wandComponent(WandGem.APPRENTICE).name("Apprentice's").build();
        
        // Generate research discipline localizations
        this.researchDiscipline(ResearchDisciplines.BASICS).name("Fundamentals").build();
        
        // Generate knowledge type localizations
        this.knowledgeType(KnowledgeType.OBSERVATION).name("Observation").build();
        this.knowledgeType(KnowledgeType.THEORY).name("Theory").build();
        
        // Generate attunement type localizations
        this.attunementType(AttunementType.PERMANENT).name("Permanent").build();
        this.attunementType(AttunementType.INDUCED).name("Induced").build();
        this.attunementType(AttunementType.TEMPORARY).name("Temporary").build();
        
        // Generate attunement threshold localizations
        this.attunementThreshold(AttunementThreshold.MINOR).name("Minor")
            .effect(Source.EARTH, "5% Earth mana discount")
            .build();
        this.attunementThreshold(AttunementThreshold.LESSER).name("Lesser")
            .effect(Source.EARTH, "Increased attack/mining speed")
            .build();
        this.attunementThreshold(AttunementThreshold.GREATER).name("Greater")
            .effect(Source.EARTH, "Step height increase")
            .build();
        
        // Generate command output localizations
        this.command("research").sub("grant").output("Granting research to %1$s: %2$s").end().build();
        this.command("research").sub("grant").sub("target").output("%1$s has granted you %2$s research and its parents").end().build();
        this.command("research").sub("details").output("Research %1$s of player %2$s:", "  Status: %1$s", "  Current stage: %1$d", "  Flags: %1$s").end().build();
        this.command("error").name("Error executing command").build();
        
        // Generate event output localizations
        this.event("add_addendum").name("Addendum added to %1$s").build();
        this.event("scan").sub("success").output("You have gained valuable research data").end().build();
        this.event("scan").sub("fail").output("You are unable to scan that").end().build();
        this.event("scan").sub("repeat").output("You learn nothing new from scanning that").end().build();
        this.event("scan").sub("toobig").output("There are too many items there to scan completely").end().build();
        
        // Generate written book localizations
        this.book("dream_journal").name("Dream Journal")
            .text("\"I dreamed of the shrine last night. The same strange energy still permeated the air, but this time I knew the word for it.\n\nMagick.\n\nAs if the word unlocked something in my mind, I knew what to do. In the dream, I dug beneath the base of\"")
            .text("\"the shrine and found stone laced with a curious dust. Sensing more magick within it, I took a handful of the dust and rubbed it onto an ordinary stick.\n\nSo imbued, the stick became something more. In the dream, I took it and waved it at a bookcase.\"")
            .text("\"The dream ended before I could see what resulted, but I feel like it would have been something wondrous.\n\nI feel like this could be a key to something amazing, if I just have the courage to take that first step.\"")
            .build();
        
        // Generate spell vehicle localizations
        this.add("spells.primalmagick.vehicle.header", "Spell Type");
        this.spellVehicle(TouchSpellVehicle.TYPE).name("Touch").defaultName("Hand").build();
        
        // Generate spell payload localizations
        this.add("spells.primalmagick.payload.header", "Spell Payload");
        this.spellPayload(EarthDamageSpellPayload.TYPE).name("Earth Damage").defaultName("Earthen").detailTooltip("Earth Damage (%1$s damage)").build();
        
        // Generate spell mod localizations
        this.add("spells.primalmagick.primary_mod.header", "Primary Mod");
        this.add("spells.primalmagick.secondary_mod.header", "Secondary Mod");
        this.spellMod(EmptySpellMod.TYPE).name("None").defaultName("").build();
        
        // Generate spell property localizations
        this.spellProperty("power").name("Power").build();
        
        // Generate grimoire GUI localizations
        this.grimoire("index_header").name("Disciplines").build();
        this.grimoire("section_header").sub("new").output("New").end().build();
        
        // Generate JEI GUI localizations
        this.jei("ritual").sub("offerings").sub("header").output("Offerings").end().build();
        this.jei("ritual").sub("props").sub("header").output("Props").end().build();
        
        // Generate ritual feedback localizations
        this.ritual("instability").name("%1$s: %2$s").build();
        this.ritual("instability").sub("header").output("Instability").end().build();
        this.ritual("instability").sub("rating").output(0, "Trivial", "Low", "Moderate", "High", "Very High", "Extreme").end().build();
        
        // Generate concoction localizations
        this.concoction("doses_remaining").name("Doses: %1$d").build();
        this.concoction("fuse").sub("impact").output("Impact").end().build();
        
        // Generate keybind localizations
        this.add(KeyBindings.KEY_CATEGORY, "Primal Magick");
        this.keyMapping(KeyBindings.CHANGE_SPELL_KEY, "change_spell").name("Change Active Spell").build();
        
        // Generate statistics localizations
        this.stat(StatsPM.GRIMOIRE_READ).name("Times Grimoire consulted").build();
        
        // Generate research project localizations
        this.researchProject("trade").name("Trade").text("Perhaps some nearby villagers have magickal knowledge to trade.  You assemble some goods they'll find valuable.").build();
        
        // Generate research requirement localizations
        this.researchRequirement("m_dummy").name("Location Discovery").build();
        this.researchRequirement(Source.BLOOD.getDiscoverKey()).name("Discover a source of sanguine power").build();
        this.researchRequirement("b_scan_primalite").name("Analyze a magickal metal").hint("Complete the Alchemy research entry for Primalite, then scan a nugget, ingot, or block of it").build();
        
        // Generate research entry localizations
        this.researchEntry("CONCOCTING_BOMBS").name("Concocting: Bombs")
            .stages()
                .add("Some potions just aren't meant to be drunk, at least by oneself.  Or perhaps meant to be shared?<BR>Either way, skyglass flasks don't shatter like the regular glass bottles in splash potions do.  And even if they did, who wants to waste all those doses of potion at once?<BR>I must devise a new method of distribution.")
                .add("Bombs!  Bombs are great!<BR>By creating a set of special bomb casings, I can distribute individual doses of potion to anyone at range.  A single set of casings will produce six bombs.<BR>Alchemical bombs don't just explode on impact, necessarily.  I mean, they will if you score a direct hit on a creature, but if you miss, they'll bounce.  Bombs are on a timed fuse and will detonate when that time runs out.  I can change the length of the fuse by using it while sneaking.")
                .end()
            .addenda()
                .add("Some bomb recipes requires the use of Blood essence.")
                .add("Some bomb recipes requires the use of Infernal essence.")
                .add("Some bomb recipes requires the use of Void essence.")
                .end()
            .build();
    }
}
