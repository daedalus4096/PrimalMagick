package com.verdantartifice.primalmagick.datagen.lang;

import java.util.List;

import com.verdantartifice.primalmagick.client.config.KeyBindings;
import com.verdantartifice.primalmagick.common.armortrim.TrimMaterialsPM;
import com.verdantartifice.primalmagick.common.armortrim.TrimPatternsPM;
import com.verdantartifice.primalmagick.common.attunements.AttunementThreshold;
import com.verdantartifice.primalmagick.common.attunements.AttunementType;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.damagesource.DamageTypesPM;
import com.verdantartifice.primalmagick.common.effects.EffectsPM;
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
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.registries.RegistryObject;

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
        this.item(ItemsPM.GRIMOIRE).name("Grimoire").build();
        this.item(ItemsPM.ARCANOMETER).name("Arcanometer").build();
        this.item(ItemsPM.MAGNIFYING_GLASS).name("Magnifying Glass").build();
        this.item(ItemsPM.ESSENCE_DUST_EARTH).name("Earth Dust").build();
        this.item(ItemsPM.ESSENCE_DUST_SEA).name("Sea Dust").build();
        this.item(ItemsPM.ESSENCE_DUST_SKY).name("Sky Dust").build();
        this.item(ItemsPM.ESSENCE_DUST_SUN).name("Sun Dust").build();
        this.item(ItemsPM.ESSENCE_DUST_MOON).name("Moon Dust").build();
        this.item(ItemsPM.ESSENCE_DUST_BLOOD).name("Blood Dust").build();
        this.item(ItemsPM.ESSENCE_DUST_INFERNAL).name("Infernal Dust").build();
        this.item(ItemsPM.ESSENCE_DUST_VOID).name("Void Dust").build();
        this.item(ItemsPM.ESSENCE_DUST_HALLOWED).name("Hallowed Dust").build();
        this.item(ItemsPM.ESSENCE_SHARD_EARTH).name("Earth Shard").build();
        this.item(ItemsPM.ESSENCE_SHARD_SEA).name("Sea Shard").build();
        this.item(ItemsPM.ESSENCE_SHARD_SKY).name("Sky Shard").build();
        this.item(ItemsPM.ESSENCE_SHARD_SUN).name("Sun Shard").build();
        this.item(ItemsPM.ESSENCE_SHARD_MOON).name("Moon Shard").build();
        this.item(ItemsPM.ESSENCE_SHARD_BLOOD).name("Blood Shard").build();
        this.item(ItemsPM.ESSENCE_SHARD_INFERNAL).name("Infernal Shard").build();
        this.item(ItemsPM.ESSENCE_SHARD_VOID).name("Void Shard").build();
        this.item(ItemsPM.ESSENCE_SHARD_HALLOWED).name("Hallowed Shard").build();
        this.item(ItemsPM.ESSENCE_CRYSTAL_EARTH).name("Earth Crystal").build();
        this.item(ItemsPM.ESSENCE_CRYSTAL_SEA).name("Sea Crystal").build();
        this.item(ItemsPM.ESSENCE_CRYSTAL_SKY).name("Sky Crystal").build();
        this.item(ItemsPM.ESSENCE_CRYSTAL_SUN).name("Sun Crystal").build();
        this.item(ItemsPM.ESSENCE_CRYSTAL_MOON).name("Moon Crystal").build();
        this.item(ItemsPM.ESSENCE_CRYSTAL_BLOOD).name("Blood Crystal").build();
        this.item(ItemsPM.ESSENCE_CRYSTAL_INFERNAL).name("Infernal Crystal").build();
        this.item(ItemsPM.ESSENCE_CRYSTAL_VOID).name("Void Crystal").build();
        this.item(ItemsPM.ESSENCE_CRYSTAL_HALLOWED).name("Hallowed Crystal").build();
        this.item(ItemsPM.ESSENCE_CLUSTER_EARTH).name("Earth Cluster").build();
        this.item(ItemsPM.ESSENCE_CLUSTER_SEA).name("Sea Cluster").build();
        this.item(ItemsPM.ESSENCE_CLUSTER_SKY).name("Sky Cluster").build();
        this.item(ItemsPM.ESSENCE_CLUSTER_SUN).name("Sun Cluster").build();
        this.item(ItemsPM.ESSENCE_CLUSTER_MOON).name("Moon Cluster").build();
        this.item(ItemsPM.ESSENCE_CLUSTER_BLOOD).name("Blood Cluster").build();
        this.item(ItemsPM.ESSENCE_CLUSTER_INFERNAL).name("Infernal Cluster").build();
        this.item(ItemsPM.ESSENCE_CLUSTER_VOID).name("Void Cluster").build();
        this.item(ItemsPM.ESSENCE_CLUSTER_HALLOWED).name("Hallowed Cluster").build();
        this.item(ItemsPM.ALCHEMICAL_WASTE).name("Alchemical Waste").build();
        this.item(ItemsPM.BLOODY_FLESH).name("Bloody Flesh").tooltip("A dark hunger comes over you", "as you look at this...").build();
        this.item(ItemsPM.HALLOWED_ORB).name("Hallowed Orb").tooltip("A sense of peace washes over", "you as you hold this").build();
        this.item(ItemsPM.HEARTWOOD).name("Heartwood").build();
        this.item(ItemsPM.ENCHANTED_INK).name("Enchanted Ink").build();
        this.item(ItemsPM.ENCHANTED_INK_AND_QUILL).name("Enchanted Ink and Quill").build();
        this.item(ItemsPM.ROCK_SALT).name("Rock Salt").build();
        this.item(ItemsPM.REFINED_SALT).name("Refined Salt").build();
        this.item(ItemsPM.SALTED_BAKED_POTATO).name("Salted Baked Potato").build();
        this.item(ItemsPM.SALTED_COOKED_BEEF).name("Salted Steak").build();
        this.item(ItemsPM.SALTED_COOKED_CHICKEN).name("Salted Cooked Chicken").build();
        this.item(ItemsPM.SALTED_COOKED_COD).name("Salted Cooked Cod").build();
        this.item(ItemsPM.SALTED_COOKED_MUTTON).name("Salted Cooked Mutton").build();
        this.item(ItemsPM.SALTED_COOKED_PORKCHOP).name("Salted Cooked Porkchop").build();
        this.item(ItemsPM.SALTED_COOKED_RABBIT).name("Salted Cooked Rabbit").build();
        this.item(ItemsPM.SALTED_COOKED_SALMON).name("Salted Cooked Salmon").build();
        this.item(ItemsPM.SALTED_BEETROOT_SOUP).name("Salted Beetroot Soup").build();
        this.item(ItemsPM.SALTED_MUSHROOM_STEW).name("Salted Mushroom Stew").build();
        this.item(ItemsPM.SALTED_RABBIT_STEW).name("Salted Rabbit Stew").build();
        this.item(ItemsPM.EARTHSHATTER_HAMMER).name("Earthshatter Hammer").build();
        this.item(ItemsPM.IRON_GRIT).name("Iron Grit").build();
        this.item(ItemsPM.GOLD_GRIT).name("Gold Grit").build();
        this.item(ItemsPM.COPPER_GRIT).name("Copper Grit").build();
        this.item(ItemsPM.PRIMALITE_INGOT).name("Primalite Ingot").build();
        this.item(ItemsPM.HEXIUM_INGOT).name("Hexium Ingot").build();
        this.item(ItemsPM.HALLOWSTEEL_INGOT).name("Hallowsteel Ingot").build();
        this.item(ItemsPM.PRIMALITE_NUGGET).name("Primalite Nugget").build();
        this.item(ItemsPM.HEXIUM_NUGGET).name("Hexium Nugget").build();
        this.item(ItemsPM.HALLOWSTEEL_NUGGET).name("Hallowsteel Nugget").build();
        this.item(ItemsPM.QUARTZ_NUGGET).name("Quartz Sliver").build();
        this.item(ItemsPM.MANA_PRISM).name("Mana Prism").build();
        this.item(ItemsPM.TALLOW).name("Alchemical Tallow").build();
        this.item(ItemsPM.BEESWAX).name("Beeswax").build();
        this.item(ItemsPM.MANA_SALTS).name("Mana Salts").build();
        this.item(ItemsPM.MANAFRUIT).name("Manafruit").build();
        this.item(ItemsPM.INCENSE_STICK).name("Incense Stick").build();
        this.item(ItemsPM.SOUL_GEM).name("Soul Gem").build();
        this.item(ItemsPM.SOUL_GEM_SLIVER).name("Soul Gem Sliver").build();
        this.item(ItemsPM.SPELLCLOTH).name("Spellcloth").build();
        this.item(ItemsPM.HEXWEAVE).name("Hexweave").build();
        this.item(ItemsPM.SAINTSWOOL).name("Saintswool").build();
        this.item(ItemsPM.RUNE_UNATTUNED).name("Unattuned Source Rune").build();
        this.item(ItemsPM.RUNE_EARTH).name("Earth Rune").tooltip("\"Teq\"").build();
        this.item(ItemsPM.RUNE_SEA).name("Sea Rune").tooltip("\"Ald\"").build();
        this.item(ItemsPM.RUNE_SKY).name("Sky Rune").tooltip("\"Der\"").build();
        this.item(ItemsPM.RUNE_SUN).name("Sun Rune").tooltip("\"Sid\"").build();
        this.item(ItemsPM.RUNE_MOON).name("Moon Rune").tooltip("\"Lun\"").build();
        this.item(ItemsPM.RUNE_BLOOD).name("Blood Rune").tooltip("\"Hem\"").build();
        this.item(ItemsPM.RUNE_INFERNAL).name("Infernal Rune").tooltip("\"Cey\"").build();
        this.item(ItemsPM.RUNE_VOID).name("Void Rune").tooltip("\"Zir\"").build();
        this.item(ItemsPM.RUNE_HALLOWED).name("Hallowed Rune").tooltip("\"Phal\"").build();
        this.item(ItemsPM.RUNE_ABSORB).name("Absorb Rune").tooltip("\"Kath\"").build();
        this.item(ItemsPM.RUNE_DISPEL).name("Dispel Rune").tooltip("\"Neth\"").build();
        this.item(ItemsPM.RUNE_PROJECT).name("Project Rune").tooltip("\"Bok\"").build();
        this.item(ItemsPM.RUNE_PROTECT).name("Protect Rune").tooltip("\"Kor\"").build();
        this.item(ItemsPM.RUNE_SUMMON).name("Summon Rune").tooltip("\"Tir\"").build();
        this.item(ItemsPM.RUNE_AREA).name("Area Rune").tooltip("\"Rem\"").build();
        this.item(ItemsPM.RUNE_CREATURE).name("Creature Rune").tooltip("\"Tak\"").build();
        this.item(ItemsPM.RUNE_ITEM).name("Item Rune").tooltip("\"Mag\"").build();
        this.item(ItemsPM.RUNE_SELF).name("Self Rune").tooltip("\"San\"").build();
        this.item(ItemsPM.RUNE_INSIGHT).name("Insight Rune").tooltip("\"Nif\"").build();
        this.item(ItemsPM.RUNE_POWER).name("Power Rune").tooltip("\"Par\"").build();
        this.item(ItemsPM.RUNE_GRACE).name("Grace Rune").tooltip("\"Fyr\"").build();
        this.item(ItemsPM.IMBUED_WOOL_HEAD).name("Apprentice's Hood").build();
        this.item(ItemsPM.IMBUED_WOOL_CHEST).name("Apprentice's Robes").build();
        this.item(ItemsPM.IMBUED_WOOL_LEGS).name("Apprentice's Leggings").build();
        this.item(ItemsPM.IMBUED_WOOL_FEET).name("Apprentice's Slippers").build();
        this.item(ItemsPM.SPELLCLOTH_HEAD).name("Adept's Hood").build();
        this.item(ItemsPM.SPELLCLOTH_CHEST).name("Adept's Robes").build();
        this.item(ItemsPM.SPELLCLOTH_LEGS).name("Adept's Leggings").build();
        this.item(ItemsPM.SPELLCLOTH_FEET).name("Adept's Slippers").build();
        this.item(ItemsPM.HEXWEAVE_HEAD).name("Wizard's Hood").build();
        this.item(ItemsPM.HEXWEAVE_CHEST).name("Wizard's Robes").build();
        this.item(ItemsPM.HEXWEAVE_LEGS).name("Wizard's Leggings").build();
        this.item(ItemsPM.HEXWEAVE_FEET).name("Wizard's Slippers").build();
        this.item(ItemsPM.SAINTSWOOL_HEAD).name("Archmage's Hood").build();
        this.item(ItemsPM.SAINTSWOOL_CHEST).name("Archmage's Robes").build();
        this.item(ItemsPM.SAINTSWOOL_LEGS).name("Archmage's Leggings").build();
        this.item(ItemsPM.SAINTSWOOL_FEET).name("Archmage's Slippers").build();
        this.item(ItemsPM.PRIMALITE_SWORD).name("Primalite Sword").build();
        this.item(ItemsPM.PRIMALITE_TRIDENT).name("Primalite Trident").build();
        this.item(ItemsPM.PRIMALITE_BOW).name("Primalite Bow").build();
        this.item(ItemsPM.PRIMALITE_SHOVEL).name("Primalite Shovel").build();
        this.item(ItemsPM.PRIMALITE_PICKAXE).name("Primalite Pickaxe").build();
        this.item(ItemsPM.PRIMALITE_AXE).name("Primalite Axe").build();
        this.item(ItemsPM.PRIMALITE_HOE).name("Primalite Hoe").build();
        this.item(ItemsPM.PRIMALITE_FISHING_ROD).name("Primalite Fishing Rod").build();
        this.item(ItemsPM.PRIMALITE_HEAD).name("Primalite Helmet").build();
        this.item(ItemsPM.PRIMALITE_CHEST).name("Primalite Chestplate").build();
        this.item(ItemsPM.PRIMALITE_LEGS).name("Primalite Greaves").build();
        this.item(ItemsPM.PRIMALITE_FEET).name("Primalite Boots").build();
        this.item(ItemsPM.PRIMALITE_SHIELD).name("Primalite Shield")
            .coloredName(DyeColor.WHITE, "White Primalite Shield")
            .coloredName(DyeColor.ORANGE, "Orange Primalite Shield")
            .coloredName(DyeColor.MAGENTA, "Magenta Primalite Shield")
            .coloredName(DyeColor.LIGHT_BLUE, "Light Blue Primalite Shield")
            .coloredName(DyeColor.YELLOW, "Yellow Primalite Shield")
            .coloredName(DyeColor.LIME, "Lime Primalite Shield")
            .coloredName(DyeColor.PINK, "Pink Primalite Shield")
            .coloredName(DyeColor.GRAY, "Gray Primalite Shield")
            .coloredName(DyeColor.LIGHT_GRAY, "Light Gray Primalite Shield")
            .coloredName(DyeColor.CYAN, "Cyan Primalite Shield")
            .coloredName(DyeColor.PURPLE, "Purple Primalite Shield")
            .coloredName(DyeColor.BLUE, "Blue Primalite Shield")
            .coloredName(DyeColor.BROWN, "Brown Primalite Shield")
            .coloredName(DyeColor.GREEN, "Green Primalite Shield")
            .coloredName(DyeColor.RED, "Red Primalite Shield")
            .coloredName(DyeColor.BLACK, "Black Primalite Shield")
            .build();
        this.item(ItemsPM.HEXIUM_SWORD).name("Hexium Sword").build();
        this.item(ItemsPM.HEXIUM_TRIDENT).name("Hexium Trident").build();
        this.item(ItemsPM.HEXIUM_BOW).name("Hexium Bow").build();
        this.item(ItemsPM.HEXIUM_SHOVEL).name("Hexium Shovel").build();
        this.item(ItemsPM.HEXIUM_PICKAXE).name("Hexium Pickaxe").build();
        this.item(ItemsPM.HEXIUM_AXE).name("Hexium Axe").build();
        this.item(ItemsPM.HEXIUM_HOE).name("Hexium Hoe").build();
        this.item(ItemsPM.HEXIUM_FISHING_ROD).name("Hexium Fishing Rod").build();
        this.item(ItemsPM.HEXIUM_HEAD).name("Hexium Helmet").build();
        this.item(ItemsPM.HEXIUM_CHEST).name("Hexium Chestplate").build();
        this.item(ItemsPM.HEXIUM_LEGS).name("Hexium Greaves").build();
        this.item(ItemsPM.HEXIUM_FEET).name("Hexium Boots").build();
        this.item(ItemsPM.HEXIUM_SHIELD).name("Hexium Shield")
            .coloredName(DyeColor.WHITE, "White Hexium Shield")
            .coloredName(DyeColor.ORANGE, "Orange Hexium Shield")
            .coloredName(DyeColor.MAGENTA, "Magenta Hexium Shield")
            .coloredName(DyeColor.LIGHT_BLUE, "Light Blue Hexium Shield")
            .coloredName(DyeColor.YELLOW, "Yellow Hexium Shield")
            .coloredName(DyeColor.LIME, "Lime Hexium Shield")
            .coloredName(DyeColor.PINK, "Pink Hexium Shield")
            .coloredName(DyeColor.GRAY, "Gray Hexium Shield")
            .coloredName(DyeColor.LIGHT_GRAY, "Light Gray Hexium Shield")
            .coloredName(DyeColor.CYAN, "Cyan Hexium Shield")
            .coloredName(DyeColor.PURPLE, "Purple Hexium Shield")
            .coloredName(DyeColor.BLUE, "Blue Hexium Shield")
            .coloredName(DyeColor.BROWN, "Brown Hexium Shield")
            .coloredName(DyeColor.GREEN, "Green Hexium Shield")
            .coloredName(DyeColor.RED, "Red Hexium Shield")
            .coloredName(DyeColor.BLACK, "Black Hexium Shield")
            .build();
        this.item(ItemsPM.HALLOWSTEEL_SWORD).name("Hallowsteel Sword").build();
        this.item(ItemsPM.HALLOWSTEEL_TRIDENT).name("Hallowsteel Trident").build();
        this.item(ItemsPM.HALLOWSTEEL_BOW).name("Hallowsteel Bow").build();
        this.item(ItemsPM.HALLOWSTEEL_SHOVEL).name("Hallowsteel Shovel").build();
        this.item(ItemsPM.HALLOWSTEEL_PICKAXE).name("Hallowsteel Pickaxe").build();
        this.item(ItemsPM.HALLOWSTEEL_AXE).name("Hallowsteel Axe").build();
        this.item(ItemsPM.HALLOWSTEEL_HOE).name("Hallowsteel Hoe").build();
        this.item(ItemsPM.HALLOWSTEEL_FISHING_ROD).name("Hallowsteel Fishing Rod").build();
        this.item(ItemsPM.HALLOWSTEEL_HEAD).name("Hallowsteel Helmet").build();
        this.item(ItemsPM.HALLOWSTEEL_CHEST).name("Hallowsteel Chestplate").build();
        this.item(ItemsPM.HALLOWSTEEL_LEGS).name("Hallowsteel Greaves").build();
        this.item(ItemsPM.HALLOWSTEEL_FEET).name("Hallowsteel Boots").build();
        this.item(ItemsPM.HALLOWSTEEL_SHIELD).name("Hallowsteel Shield")
            .coloredName(DyeColor.WHITE, "White Hallowsteel Shield")
            .coloredName(DyeColor.ORANGE, "Orange Hallowsteel Shield")
            .coloredName(DyeColor.MAGENTA, "Magenta Hallowsteel Shield")
            .coloredName(DyeColor.LIGHT_BLUE, "Light Blue Hallowsteel Shield")
            .coloredName(DyeColor.YELLOW, "Yellow Hallowsteel Shield")
            .coloredName(DyeColor.LIME, "Lime Hallowsteel Shield")
            .coloredName(DyeColor.PINK, "Pink Hallowsteel Shield")
            .coloredName(DyeColor.GRAY, "Gray Hallowsteel Shield")
            .coloredName(DyeColor.LIGHT_GRAY, "Light Gray Hallowsteel Shield")
            .coloredName(DyeColor.CYAN, "Cyan Hallowsteel Shield")
            .coloredName(DyeColor.PURPLE, "Purple Hallowsteel Shield")
            .coloredName(DyeColor.BLUE, "Blue Hallowsteel Shield")
            .coloredName(DyeColor.BROWN, "Brown Hallowsteel Shield")
            .coloredName(DyeColor.GREEN, "Green Hallowsteel Shield")
            .coloredName(DyeColor.RED, "Red Hallowsteel Shield")
            .coloredName(DyeColor.BLACK, "Black Hallowsteel Shield")
            .build();
        this.item(ItemsPM.MAGITECH_PARTS_BASIC).name("Basic Magitech Parts").build();
        this.item(ItemsPM.MAGITECH_PARTS_ENCHANTED).name("Enchanted Magitech Parts").build();
        this.item(ItemsPM.MAGITECH_PARTS_FORBIDDEN).name("Forbidden Magitech Parts").build();
        this.item(ItemsPM.MAGITECH_PARTS_HEAVENLY).name("Heavenly Magitech Parts").build();
        this.item(ItemsPM.SEASCRIBE_PEN).name("Seascribe Pen").build();
        this.item(ItemsPM.BASIC_EARTH_AMBROSIA).name("Earth Ambrosia").build();
        this.item(ItemsPM.BASIC_SEA_AMBROSIA).name("Sea Ambrosia").build();
        this.item(ItemsPM.BASIC_SKY_AMBROSIA).name("Sky Ambrosia").build();
        this.item(ItemsPM.BASIC_SUN_AMBROSIA).name("Sun Ambrosia").build();
        this.item(ItemsPM.BASIC_MOON_AMBROSIA).name("Moon Ambrosia").build();
        this.item(ItemsPM.BASIC_BLOOD_AMBROSIA).name("Blood Ambrosia").build();
        this.item(ItemsPM.BASIC_INFERNAL_AMBROSIA).name("Infernal Ambrosia").build();
        this.item(ItemsPM.BASIC_VOID_AMBROSIA).name("Void Ambrosia").build();
        this.item(ItemsPM.BASIC_HALLOWED_AMBROSIA).name("Hallowed Ambrosia").build();
        this.item(ItemsPM.GREATER_EARTH_AMBROSIA).name("Greater Earth Ambrosia").build();
        this.item(ItemsPM.GREATER_SEA_AMBROSIA).name("Greater Sea Ambrosia").build();
        this.item(ItemsPM.GREATER_SKY_AMBROSIA).name("Greater Sky Ambrosia").build();
        this.item(ItemsPM.GREATER_SUN_AMBROSIA).name("Greater Sun Ambrosia").build();
        this.item(ItemsPM.GREATER_MOON_AMBROSIA).name("Greater Moon Ambrosia").build();
        this.item(ItemsPM.GREATER_BLOOD_AMBROSIA).name("Greater Blood Ambrosia").build();
        this.item(ItemsPM.GREATER_INFERNAL_AMBROSIA).name("Greater Infernal Ambrosia").build();
        this.item(ItemsPM.GREATER_VOID_AMBROSIA).name("Greater Void Ambrosia").build();
        this.item(ItemsPM.GREATER_HALLOWED_AMBROSIA).name("Greater Hallowed Ambrosia").build();
        this.item(ItemsPM.SUPREME_EARTH_AMBROSIA).name("Supreme Earth Ambrosia").build();
        this.item(ItemsPM.SUPREME_SEA_AMBROSIA).name("Supreme Sea Ambrosia").build();
        this.item(ItemsPM.SUPREME_SKY_AMBROSIA).name("Supreme Sky Ambrosia").build();
        this.item(ItemsPM.SUPREME_SUN_AMBROSIA).name("Supreme Sun Ambrosia").build();
        this.item(ItemsPM.SUPREME_MOON_AMBROSIA).name("Supreme Moon Ambrosia").build();
        this.item(ItemsPM.SUPREME_BLOOD_AMBROSIA).name("Supreme Blood Ambrosia").build();
        this.item(ItemsPM.SUPREME_INFERNAL_AMBROSIA).name("Supreme Infernal Ambrosia").build();
        this.item(ItemsPM.SUPREME_VOID_AMBROSIA).name("Supreme Void Ambrosia").build();
        this.item(ItemsPM.SUPREME_HALLOWED_AMBROSIA).name("Supreme Hallowed Ambrosia").build();
        this.item(ItemsPM.FLYING_CARPET).name("Flying Carpet").build();
        this.item(ItemsPM.SPELL_SCROLL_BLANK).name("Blank Spell Scroll").build();
        this.item(ItemsPM.SPELL_SCROLL_FILLED).name("Scroll of %1$s").build();
        this.item(ItemsPM.MUNDANE_WAND).name("Mundane Wand").build();
        this.item(ItemsPM.MODULAR_WAND).name("%1$s %2$s %3$s Wand").build();
        this.item(ItemsPM.MODULAR_STAFF).name("%1$s %2$s %3$s Staff").build();
        this.item(ItemsPM.HEARTWOOD_WAND_CORE_ITEM).name("Heartwood Wand Core").build();
        this.item(ItemsPM.OBSIDIAN_WAND_CORE_ITEM).name("Obsidian Wand Core").build();
        this.item(ItemsPM.CORAL_WAND_CORE_ITEM).name("Coral Wand Core").build();
        this.item(ItemsPM.BAMBOO_WAND_CORE_ITEM).name("Bamboo Wand Core").build();
        this.item(ItemsPM.SUNWOOD_WAND_CORE_ITEM).name("Sunwood Wand Core").build();
        this.item(ItemsPM.MOONWOOD_WAND_CORE_ITEM).name("Moonwood Wand Core").build();
        this.item(ItemsPM.BONE_WAND_CORE_ITEM).name("Bone Wand Core").build();
        this.item(ItemsPM.BLAZE_ROD_WAND_CORE_ITEM).name("Blaze Rod Wand Core").build();
        this.item(ItemsPM.PURPUR_WAND_CORE_ITEM).name("Purpur Wand Core").build();
        this.item(ItemsPM.PRIMAL_WAND_CORE_ITEM).name("Primal Wand Core").build();
        this.item(ItemsPM.DARK_PRIMAL_WAND_CORE_ITEM).name("Dark Primal Wand Core").build();
        this.item(ItemsPM.PURE_PRIMAL_WAND_CORE_ITEM).name("Pure Primal Wand Core").build();
        this.item(ItemsPM.IRON_WAND_CAP_ITEM).name("Iron Wand Cap").build();
        this.item(ItemsPM.GOLD_WAND_CAP_ITEM).name("Gold Wand Cap").build();
        this.item(ItemsPM.PRIMALITE_WAND_CAP_ITEM).name("Primalite Wand Cap").build();
        this.item(ItemsPM.HEXIUM_WAND_CAP_ITEM).name("Hexium Wand Cap").build();
        this.item(ItemsPM.HALLOWSTEEL_WAND_CAP_ITEM).name("Hallowsteel Wand Cap").build();
        this.item(ItemsPM.APPRENTICE_WAND_GEM_ITEM).name("Apprentice's Wand Gem").build();
        this.item(ItemsPM.ADEPT_WAND_GEM_ITEM).name("Adept's Wand Gem").build();
        this.item(ItemsPM.WIZARD_WAND_GEM_ITEM).name("Wizard's Wand Gem").build();
        this.item(ItemsPM.ARCHMAGE_WAND_GEM_ITEM).name("Archmage's Wand Gem").build();
        this.item(ItemsPM.CREATIVE_WAND_GEM_ITEM).name("Creative Wand Gem").build();
        this.item(ItemsPM.HEARTWOOD_STAFF_CORE_ITEM).name("Heartwood Staff Core").build();
        this.item(ItemsPM.OBSIDIAN_STAFF_CORE_ITEM).name("Obsidian Staff Core").build();
        this.item(ItemsPM.CORAL_STAFF_CORE_ITEM).name("Coral Staff Core").build();
        this.item(ItemsPM.BAMBOO_STAFF_CORE_ITEM).name("Bamboo Staff Core").build();
        this.item(ItemsPM.SUNWOOD_STAFF_CORE_ITEM).name("Sunwood Staff Core").build();
        this.item(ItemsPM.MOONWOOD_STAFF_CORE_ITEM).name("Moonwood Staff Core").build();
        this.item(ItemsPM.BONE_STAFF_CORE_ITEM).name("Bone Staff Core").build();
        this.item(ItemsPM.BLAZE_ROD_STAFF_CORE_ITEM).name("Blaze Rod Staff Core").build();
        this.item(ItemsPM.PURPUR_STAFF_CORE_ITEM).name("Purpur Staff Core").build();
        this.item(ItemsPM.PRIMAL_STAFF_CORE_ITEM).name("Primal Staff Core").build();
        this.item(ItemsPM.DARK_PRIMAL_STAFF_CORE_ITEM).name("Dark Primal Staff Core").build();
        this.item(ItemsPM.PURE_PRIMAL_STAFF_CORE_ITEM).name("Pure Primal Staff Core").build();
        this.item(ItemsPM.TREEFOLK_SPAWN_EGG).name("Treefolk Spawn Egg").build();
        this.item(ItemsPM.PRIMALITE_GOLEM_SPAWN_EGG).name("Primalite Golem Spawn Egg").build();
        this.item(ItemsPM.HEXIUM_GOLEM_SPAWN_EGG).name("Hexium Golem Spawn Egg").build();
        this.item(ItemsPM.HALLOWSTEEL_GOLEM_SPAWN_EGG).name("Hallowsteel Golem Spawn Egg").build();
        this.item(ItemsPM.BASIC_EARTH_PIXIE).name("Earth Pixie").build();
        this.item(ItemsPM.GRAND_EARTH_PIXIE).name("Grand Earth Pixie").build();
        this.item(ItemsPM.MAJESTIC_EARTH_PIXIE).name("Majestic Earth Pixie").build();
        this.item(ItemsPM.BASIC_SEA_PIXIE).name("Sea Pixie").build();
        this.item(ItemsPM.GRAND_SEA_PIXIE).name("Grand Sea Pixie").build();
        this.item(ItemsPM.MAJESTIC_SEA_PIXIE).name("Majestic Sea Pixie").build();
        this.item(ItemsPM.BASIC_SKY_PIXIE).name("Sky Pixie").build();
        this.item(ItemsPM.GRAND_SKY_PIXIE).name("Grand Sky Pixie").build();
        this.item(ItemsPM.MAJESTIC_SKY_PIXIE).name("Majestic Sky Pixie").build();
        this.item(ItemsPM.BASIC_SUN_PIXIE).name("Sun Pixie").build();
        this.item(ItemsPM.GRAND_SUN_PIXIE).name("Grand Sun Pixie").build();
        this.item(ItemsPM.MAJESTIC_SUN_PIXIE).name("Majestic Sun Pixie").build();
        this.item(ItemsPM.BASIC_MOON_PIXIE).name("Moon Pixie").build();
        this.item(ItemsPM.GRAND_MOON_PIXIE).name("Grand Moon Pixie").build();
        this.item(ItemsPM.MAJESTIC_MOON_PIXIE).name("Majestic Moon Pixie").build();
        this.item(ItemsPM.BASIC_BLOOD_PIXIE).name("Blood Pixie").build();
        this.item(ItemsPM.GRAND_BLOOD_PIXIE).name("Grand Blood Pixie").build();
        this.item(ItemsPM.MAJESTIC_BLOOD_PIXIE).name("Majestic Blood Pixie").build();
        this.item(ItemsPM.BASIC_INFERNAL_PIXIE).name("Infernal Pixie").build();
        this.item(ItemsPM.GRAND_INFERNAL_PIXIE).name("Grand Infernal Pixie").build();
        this.item(ItemsPM.MAJESTIC_INFERNAL_PIXIE).name("Majestic Infernal Pixie").build();
        this.item(ItemsPM.BASIC_VOID_PIXIE).name("Void Pixie").build();
        this.item(ItemsPM.GRAND_VOID_PIXIE).name("Grand Void Pixie").build();
        this.item(ItemsPM.MAJESTIC_VOID_PIXIE).name("Majestic Void Pixie").build();
        this.item(ItemsPM.BASIC_HALLOWED_PIXIE).name("Hallowed Pixie").build();
        this.item(ItemsPM.GRAND_HALLOWED_PIXIE).name("Grand Hallowed Pixie").build();
        this.item(ItemsPM.MAJESTIC_HALLOWED_PIXIE).name("Majestic Hallowed Pixie").build();
        this.item(ItemsPM.DRAINED_BASIC_EARTH_PIXIE).name("Drained Earth Pixie").build();
        this.item(ItemsPM.DRAINED_GRAND_EARTH_PIXIE).name("Drained Grand Earth Pixie").build();
        this.item(ItemsPM.DRAINED_MAJESTIC_EARTH_PIXIE).name("Drained Majestic Earth Pixie").build();
        this.item(ItemsPM.DRAINED_BASIC_SEA_PIXIE).name("Drained Sea Pixie").build();
        this.item(ItemsPM.DRAINED_GRAND_SEA_PIXIE).name("Drained Grand Sea Pixie").build();
        this.item(ItemsPM.DRAINED_MAJESTIC_SEA_PIXIE).name("Drained Majestic Sea Pixie").build();
        this.item(ItemsPM.DRAINED_BASIC_SKY_PIXIE).name("Drained Sky Pixie").build();
        this.item(ItemsPM.DRAINED_GRAND_SKY_PIXIE).name("Drained Grand Sky Pixie").build();
        this.item(ItemsPM.DRAINED_MAJESTIC_SKY_PIXIE).name("Drained Majestic Sky Pixie").build();
        this.item(ItemsPM.DRAINED_BASIC_SUN_PIXIE).name("Drained Sun Pixie").build();
        this.item(ItemsPM.DRAINED_GRAND_SUN_PIXIE).name("Drained Grand Sun Pixie").build();
        this.item(ItemsPM.DRAINED_MAJESTIC_SUN_PIXIE).name("Drained Majestic Sun Pixie").build();
        this.item(ItemsPM.DRAINED_BASIC_MOON_PIXIE).name("Drained Moon Pixie").build();
        this.item(ItemsPM.DRAINED_GRAND_MOON_PIXIE).name("Drained Grand Moon Pixie").build();
        this.item(ItemsPM.DRAINED_MAJESTIC_MOON_PIXIE).name("Drained Majestic Moon Pixie").build();
        this.item(ItemsPM.DRAINED_BASIC_BLOOD_PIXIE).name("Drained Blood Pixie").build();
        this.item(ItemsPM.DRAINED_GRAND_BLOOD_PIXIE).name("Drained Grand Blood Pixie").build();
        this.item(ItemsPM.DRAINED_MAJESTIC_BLOOD_PIXIE).name("Drained Majestic Blood Pixie").build();
        this.item(ItemsPM.DRAINED_BASIC_INFERNAL_PIXIE).name("Drained Infernal Pixie").build();
        this.item(ItemsPM.DRAINED_GRAND_INFERNAL_PIXIE).name("Drained Grand Infernal Pixie").build();
        this.item(ItemsPM.DRAINED_MAJESTIC_INFERNAL_PIXIE).name("Drained Majestic Infernal Pixie").build();
        this.item(ItemsPM.DRAINED_BASIC_VOID_PIXIE).name("Drained Void Pixie").build();
        this.item(ItemsPM.DRAINED_GRAND_VOID_PIXIE).name("Drained Grand Void Pixie").build();
        this.item(ItemsPM.DRAINED_MAJESTIC_VOID_PIXIE).name("Drained Majestic Void Pixie").build();
        this.item(ItemsPM.DRAINED_BASIC_HALLOWED_PIXIE).name("Drained Hallowed Pixie").build();
        this.item(ItemsPM.DRAINED_GRAND_HALLOWED_PIXIE).name("Drained Grand Hallowed Pixie").build();
        this.item(ItemsPM.DRAINED_MAJESTIC_HALLOWED_PIXIE).name("Drained Majestic Hallowed Pixie").build();
        this.item(ItemsPM.SANGUINE_CORE_BLANK).name("Blank Sanguine Core").build();
        this.item(ItemsPM.SANGUINE_CORE_ALLAY).name("Sanguine Core: Allay").build();
        this.item(ItemsPM.SANGUINE_CORE_AXOLOTL).name("Sanguine Core: Axolotl").build();
        this.item(ItemsPM.SANGUINE_CORE_BAT).name("Sanguine Core: Bat").build();
        this.item(ItemsPM.SANGUINE_CORE_BEE).name("Sanguine Core: Bee").build();
        this.item(ItemsPM.SANGUINE_CORE_BLAZE).name("Sanguine Core: Blaze").build();
        this.item(ItemsPM.SANGUINE_CORE_CAT).name("Sanguine Core: Cat").build();
        this.item(ItemsPM.SANGUINE_CORE_CAVE_SPIDER).name("Sanguine Core: Cave Spider").build();
        this.item(ItemsPM.SANGUINE_CORE_CHICKEN).name("Sanguine Core: Chicken").build();
        this.item(ItemsPM.SANGUINE_CORE_COD).name("Sanguine Core: Cod").build();
        this.item(ItemsPM.SANGUINE_CORE_COW).name("Sanguine Core: Cow").build();
        this.item(ItemsPM.SANGUINE_CORE_CREEPER).name("Sanguine Core: Creeper").build();
        this.item(ItemsPM.SANGUINE_CORE_DOLPHIN).name("Sanguine Core: Dolphin").build();
        this.item(ItemsPM.SANGUINE_CORE_DONKEY).name("Sanguine Core: Donkey").build();
        this.item(ItemsPM.SANGUINE_CORE_DROWNED).name("Sanguine Core: Drowned").build();
        this.item(ItemsPM.SANGUINE_CORE_ELDER_GUARDIAN).name("Sanguine Core: Elder Guardian").build();
        this.item(ItemsPM.SANGUINE_CORE_ENDERMAN).name("Sanguine Core: Enderman").build();
        this.item(ItemsPM.SANGUINE_CORE_ENDERMITE).name("Sanguine Core: Endermite").build();
        this.item(ItemsPM.SANGUINE_CORE_EVOKER).name("Sanguine Core: Evoker").build();
        this.item(ItemsPM.SANGUINE_CORE_FOX).name("Sanguine Core: Fox").build();
        this.item(ItemsPM.SANGUINE_CORE_FROG).name("Sanguine Core: Frog").build();
        this.item(ItemsPM.SANGUINE_CORE_GHAST).name("Sanguine Core: Ghast").build();
        this.item(ItemsPM.SANGUINE_CORE_GLOW_SQUID).name("Sanguine Core: Glow Squid").build();
        this.item(ItemsPM.SANGUINE_CORE_GOAT).name("Sanguine Core: Goat").build();
        this.item(ItemsPM.SANGUINE_CORE_GUARDIAN).name("Sanguine Core: Guardian").build();
        this.item(ItemsPM.SANGUINE_CORE_HOGLIN).name("Sanguine Core: Hoglin").build();
        this.item(ItemsPM.SANGUINE_CORE_HORSE).name("Sanguine Core: Horse").build();
        this.item(ItemsPM.SANGUINE_CORE_HUSK).name("Sanguine Core: Husk").build();
        this.item(ItemsPM.SANGUINE_CORE_LLAMA).name("Sanguine Core: Llama").build();
        this.item(ItemsPM.SANGUINE_CORE_MAGMA_CUBE).name("Sanguine Core: Magma Cube").build();
        this.item(ItemsPM.SANGUINE_CORE_MOOSHROOM).name("Sanguine Core: Mooshroom").build();
        this.item(ItemsPM.SANGUINE_CORE_OCELOT).name("Sanguine Core: Ocelot").build();
        this.item(ItemsPM.SANGUINE_CORE_PANDA).name("Sanguine Core: Panda").build();
        this.item(ItemsPM.SANGUINE_CORE_PARROT).name("Sanguine Core: Parrot").build();
        this.item(ItemsPM.SANGUINE_CORE_PHANTOM).name("Sanguine Core: Phantom").build();
        this.item(ItemsPM.SANGUINE_CORE_PIG).name("Sanguine Core: Pig").build();
        this.item(ItemsPM.SANGUINE_CORE_PIGLIN).name("Sanguine Core: Piglin").build();
        this.item(ItemsPM.SANGUINE_CORE_PIGLIN_BRUTE).name("Sanguine Core: Piglin Brute").build();
        this.item(ItemsPM.SANGUINE_CORE_PILLAGER).name("Sanguine Core: Pillager").build();
        this.item(ItemsPM.SANGUINE_CORE_POLAR_BEAR).name("Sanguine Core: Polar Bear").build();
        this.item(ItemsPM.SANGUINE_CORE_PUFFERFISH).name("Sanguine Core: Pufferfish").build();
        this.item(ItemsPM.SANGUINE_CORE_RABBIT).name("Sanguine Core: Rabbit").build();
        this.item(ItemsPM.SANGUINE_CORE_RAVAGER).name("Sanguine Core: Ravager").build();
        this.item(ItemsPM.SANGUINE_CORE_SALMON).name("Sanguine Core: Salmon").build();
        this.item(ItemsPM.SANGUINE_CORE_SHEEP).name("Sanguine Core: Sheep").build();
        this.item(ItemsPM.SANGUINE_CORE_SHULKER).name("Sanguine Core: Shulker").build();
        this.item(ItemsPM.SANGUINE_CORE_SILVERFISH).name("Sanguine Core: Silverfish").build();
        this.item(ItemsPM.SANGUINE_CORE_SKELETON).name("Sanguine Core: Skeleton").build();
        this.item(ItemsPM.SANGUINE_CORE_SKELETON_HORSE).name("Sanguine Core: Skeleton Horse").build();
        this.item(ItemsPM.SANGUINE_CORE_SLIME).name("Sanguine Core: Slime").build();
        this.item(ItemsPM.SANGUINE_CORE_SPIDER).name("Sanguine Core: Spider").build();
        this.item(ItemsPM.SANGUINE_CORE_SQUID).name("Sanguine Core: Squid").build();
        this.item(ItemsPM.SANGUINE_CORE_STRAY).name("Sanguine Core: Stray").build();
        this.item(ItemsPM.SANGUINE_CORE_STRIDER).name("Sanguine Core: Strider").build();
        this.item(ItemsPM.SANGUINE_CORE_TROPICAL_FISH).name("Sanguine Core: Tropical Fish").build();
        this.item(ItemsPM.SANGUINE_CORE_TURTLE).name("Sanguine Core: Turtle").build();
        this.item(ItemsPM.SANGUINE_CORE_VEX).name("Sanguine Core: Vex").build();
        this.item(ItemsPM.SANGUINE_CORE_VILLAGER).name("Sanguine Core: Villager").build();
        this.item(ItemsPM.SANGUINE_CORE_VINDICATOR).name("Sanguine Core: Vindicator").build();
        this.item(ItemsPM.SANGUINE_CORE_WITCH).name("Sanguine Core: Witch").build();
        this.item(ItemsPM.SANGUINE_CORE_WITHER_SKELETON).name("Sanguine Core: Wither Skeleton").build();
        this.item(ItemsPM.SANGUINE_CORE_WOLF).name("Sanguine Core: Wolf").build();
        this.item(ItemsPM.SANGUINE_CORE_ZOGLIN).name("Sanguine Core: Zoglin").build();
        this.item(ItemsPM.SANGUINE_CORE_ZOMBIE).name("Sanguine Core: Zombie").build();
        this.item(ItemsPM.SANGUINE_CORE_ZOMBIE_HORSE).name("Sanguine Core: Zombie Horse").build();
        this.item(ItemsPM.SANGUINE_CORE_ZOMBIE_VILLAGER).name("Sanguine Core: Zombie Villager").build();
        this.item(ItemsPM.SANGUINE_CORE_ZOMBIFIED_PIGLIN).name("Sanguine Core: Zombified Piglin").build();
        this.item(ItemsPM.SANGUINE_CORE_TREEFOLK).name("Sanguine Core: Treefolk").build();
        this.item(ItemsPM.SANGUINE_CORE_INNER_DEMON).name("Sanguine Core: Inner Demon").build();
        this.item(ItemsPM.SKYGLASS_FLASK).name("Skyglass Flask").build();
        // TODO Concoctions
        this.item(ItemsPM.BOMB_CASING).name("Bomb Casing").build();
        // TODO Alchemical bombs
        this.item(ItemsPM.PRIMAL_SHOVEL).name("Shovel of the Quaking Earth").build();
        this.item(ItemsPM.PRIMAL_FISHING_ROD).name("Fishing Rod of the Bountiful Sea").build();
        this.item(ItemsPM.PRIMAL_AXE).name("Axe of the Lightning Crash").build();
        this.item(ItemsPM.PRIMAL_HOE).name("Hoe of the Nurturing Sun").build();
        this.item(ItemsPM.PRIMAL_PICKAXE).name("Pickaxe of the Crescent Moon").build();
        this.item(ItemsPM.FORBIDDEN_TRIDENT).name("Trident of Gaping Wounds").build();
        this.item(ItemsPM.FORBIDDEN_BOW).name("Bow of Screaming Souls").build();
        this.item(ItemsPM.FORBIDDEN_SWORD).name("Sword of the Hungering Void").build();
        this.item(ItemsPM.SACRED_SHIELD).name("Shield of the Sacred Oath").build();
        this.item(ItemsPM.MANA_ARROW_EARTH).name("Earth-Tinged Arrow").build();
        this.item(ItemsPM.MANA_ARROW_SEA).name("Sea-Tinged Arrow").build();
        this.item(ItemsPM.MANA_ARROW_SKY).name("Sky-Tinged Arrow").build();
        this.item(ItemsPM.MANA_ARROW_SUN).name("Sun-Tinged Arrow").build();
        this.item(ItemsPM.MANA_ARROW_MOON).name("Moon-Tinged Arrow").build();
        this.item(ItemsPM.MANA_ARROW_BLOOD).name("Blood-Tinged Arrow").build();
        this.item(ItemsPM.MANA_ARROW_INFERNAL).name("Infernal-Tinged Arrow").build();
        this.item(ItemsPM.MANA_ARROW_VOID).name("Void-Tinged Arrow").build();
        this.item(ItemsPM.MANA_ARROW_HALLOWED).name("Hallowed-Tinged Arrow").build();
        this.item(ItemsPM.DREAM_VISION_TALISMAN).name("Dream Vision Talisman").build();
        this.item(ItemsPM.OBSERVATION_NOTES).name("Field Research Report").tooltip("Use to gain an arcane observation").build();
        this.item(ItemsPM.THEORY_NOTES).name("Arcane Thesis").tooltip("Use to gain an arcane theory").build();
        this.item(ItemsPM.MYSTICAL_RELIC).name("Mystical Relic").tooltip("Use to gain an arcane theory, or keep to trade").build();
        this.item(ItemsPM.MYSTICAL_RELIC_FRAGMENT).name("Mystical Relic Fragment").build();
        this.item(ItemsPM.BLOOD_NOTES).name("Blood-Scrawled Ravings").tooltip("Dare you decipher it?").build();
        this.item(ItemsPM.IGNYX).name("Ignyx").build();
        this.item(ItemsPM.DOWSING_ROD).name("Dowsing Rod").build();
        this.item(ItemsPM.FOUR_LEAF_CLOVER).name("Four-Leaf Clover").build();
        this.item(ItemsPM.SHEEP_TOME).name("Tome of Polymorph: Sheep").build();
        this.item(ItemsPM.HUMMING_ARTIFACT_UNATTUNED).name("Humming Artifact").build();
        this.item(ItemsPM.HUMMING_ARTIFACT_EARTH).name("Earth-Attuned Artifact").tooltip("Use to gain a point of permanent Earth attunement").build();
        this.item(ItemsPM.HUMMING_ARTIFACT_SEA).name("Sea-Attuned Artifact").tooltip("Use to gain a point of permanent Sea attunement").build();
        this.item(ItemsPM.HUMMING_ARTIFACT_SKY).name("Sky-Attuned Artifact").tooltip("Use to gain a point of permanent Sky attunement").build();
        this.item(ItemsPM.HUMMING_ARTIFACT_SUN).name("Sun-Attuned Artifact").tooltip("Use to gain a point of permanent Sun attunement").build();
        this.item(ItemsPM.HUMMING_ARTIFACT_MOON).name("Moon-Attuned Artifact").tooltip("Use to gain a point of permanent Moon attunement").build();
        this.item(ItemsPM.HUMMING_ARTIFACT_BLOOD).name("Blood-Attuned Artifact").tooltip("Use to gain a point of permanent Blood attunement").build();
        this.item(ItemsPM.HUMMING_ARTIFACT_INFERNAL).name("Infernal-Attuned Artifact").tooltip("Use to gain a point of permanent Infernal attunement").build();
        this.item(ItemsPM.HUMMING_ARTIFACT_VOID).name("Void-Attuned Artifact").tooltip("Use to gain a point of permanent Void attunement").build();
        this.item(ItemsPM.HUMMING_ARTIFACT_HALLOWED).name("Hallowed-Attuned Artifact").tooltip("Use to gain a point of permanent Hallowed attunement").build();
        this.item(ItemsPM.RECALL_STONE).name("Recall Stone").build();
        this.item(ItemsPM.TREEFOLK_SEED).name("Treefolk Seed").build();
        this.item(ItemsPM.ATTUNEMENT_SHACKLES_EARTH).name("Earth Attunement Shackles").build();
        this.item(ItemsPM.ATTUNEMENT_SHACKLES_SEA).name("Sea Attunement Shackles").build();
        this.item(ItemsPM.ATTUNEMENT_SHACKLES_SKY).name("Sky Attunement Shackles").build();
        this.item(ItemsPM.ATTUNEMENT_SHACKLES_SUN).name("Sun Attunement Shackles").build();
        this.item(ItemsPM.ATTUNEMENT_SHACKLES_MOON).name("Moon Attunement Shackles").build();
        this.item(ItemsPM.ATTUNEMENT_SHACKLES_BLOOD).name("Blood Attunement Shackles").build();
        this.item(ItemsPM.ATTUNEMENT_SHACKLES_INFERNAL).name("Infernal Attunement Shackles").build();
        this.item(ItemsPM.ATTUNEMENT_SHACKLES_VOID).name("Void Attunement Shackles").build();
        this.item(ItemsPM.ATTUNEMENT_SHACKLES_HALLOWED).name("Hallowed Attunement Shackles").build();

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
        
        // Generate status effect localizations
        this.mobEffect(EffectsPM.FLYING).name("Flying").build();
        this.mobEffect(EffectsPM.POLYMORPH).name("Polymorph").build();
        this.mobEffect(EffectsPM.BLEEDING).name("Bleeding").build();
        this.mobEffect(EffectsPM.WEAKENED_SOUL).name("Weakened Soul").build();
        this.mobEffect(EffectsPM.MANAFRUIT).name("Manafruit").build();
        this.mobEffect(EffectsPM.DRAIN_SOUL).name("Drain Soul").build();
        this.mobEffect(EffectsPM.MANA_IMPEDANCE).name("Mana Impedance").build();
        this.mobEffect(EffectsPM.ENDERLOCK).name("Enderlock").build();
        this.mobEffect(EffectsPM.SOULPIERCED).name("Soulpierced").build();
        @SuppressWarnings("removal")
        RegistryObject<MobEffect> stolenEssence = EffectsPM.STOLEN_ESSENCE;
        this.mobEffect(stolenEssence).name("Stolen Essence").build();
        
        // Generate mod enchantment localizations
        this.enchantment(EnchantmentsPM.LIFESTEAL).name("Lifesteal")
            .description("Grants a chance for the wielder to heal themselves a small amount when striking a creature.")
            .fullRuneText("The Lifesteal enchantment can be imbued through the use of Absorb, Self, and Blood runes.  It can be applied to any sword, axe, or trident.  When applied, it grants a chance for the wielder to heal themselves a small amount when striking a creature.")
            .partialRuneText("The Lifesteal enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any sword, axe, or trident.  When applied, it grants a chance for the wielder to heal themselves a small amount when striking a creature.")
            .build();
        this.enchantment(EnchantmentsPM.ENDERLOCK).name("Enderlock")
            .description("Causes creatures struck by the wielder to be unable to teleport for a short time.")
            .fullRuneText("The Enderlock enchantment can be imbued through the use of Dispel, Creature, and Void runes.  It can be applied to any sword, axe, or trident.  When applied, it causes creatures struck by the wielder to be unable to teleport for a short time.")
            .partialRuneText("The Enderlock enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any sword, axe, or trident.  When applied, it causes creatures struck by the wielder to be unable to teleport for a short time.")
            .build();
        this.enchantment(EnchantmentsPM.JUDGMENT).name("Judgment")
            .description("Increases the damage done by the wielder, with even more damage dealt to undead creatures.")
            .fullRuneText("The Judgment enchantment can be imbued through the use of Project, Item, and Hallowed runes.  It can be applied to any sword, axe, or trident.  When applied, it increases the damage done by the wielder, with even more damage dealt to undead creatures.  It is incompatible with other damage-increasing enchantments.")
            .partialRuneText("The Judgment enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any sword, axe, or trident.  When applied, it increases the damage done by the wielder, with even more damage dealt to undead creatures.  It is incompatible with other damage-increasing enchantments.")
            .build();
        this.enchantment(EnchantmentsPM.ENDERPORT).name("Enderport")
            .description("Causes the wielder to teleport to the location that any arrow they fire strikes.")
            .fullRuneText("The Enderport enchantment can be imbued through the use of Summon, Self, and Void runes.  It can be applied to any bow or crossbow.  When applied, it causes the wielder to teleport to the location that any arrow they fire strikes.")
            .partialRuneText("The Enderport enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any bow or crossbow.  When applied, it causes the wielder to teleport to the location that any arrow they fire strikes.")
            .build();
        this.enchantment(EnchantmentsPM.REGROWTH).name("Regrowth")
            .description("Causes the item to slowly repair itself while the wielder wears it or holds it in hand.")
            .fullRuneText("The Regrowth enchantment can be imbued through the use of Absorb, Item, and Hallowed runes.  It can be applied to any item that may be damaged or broken.  When applied, it causes the item to slowly repair itself while the wielder wears it or holds it in hand.")
            .partialRuneText("The Regrowth enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any item that may be damaged or broken.  When applied, it causes the item to slowly repair itself while the wielder wears it or holds it in hand.")
            .build();
        this.enchantment(EnchantmentsPM.AEGIS).name("Aegis")
            .description("Reduces all incoming damage, with greater effect for fire, explosion, projectile, or falling damage.")
            .fullRuneText("The Aegis enchantment can be imbued through the use of Protect, Self, and Hallowed runes.  It can be applied to any piece of armor.  When applied, it reduces all incoming damage, with greater effect for fire, explosion, projectile, or falling damage.  It does not, however, reduce the amount of time that the wearer is engulfed in flames, or the distance that the wearer is knocked back by explosions.  It is incompatible with other damage-reducing enchantments.")
            .partialRuneText("The Aegis enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any piece of armor.  When applied, it reduces all incoming damage, with greater effect for fire, explosion, projectile, or falling damage.  It does not, however, reduce the amount of time that the wearer is engulfed in flames, or the distance that the wearer is knocked back by explosions.  It is incompatible with other damage-reducing enchantments.")
            .build();
        this.enchantment(EnchantmentsPM.MANA_EFFICIENCY).name("Mana Efficiency")
            .description("Reduces all mana costs when crafting or casting a spell.")
            .fullRuneText("The Mana Efficiency enchantment can be imbued through the use of Dispel, Item, and Void runes.  It can be applied to any wand or staff.  When applied, it reduces all mana costs when crafting or casting a spell.")
            .partialRuneText("The Mana Efficiency enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any wand or staff.  When applied, it reduces all mana costs when crafting or casting a spell.")
            .build();
        this.enchantment(EnchantmentsPM.SPELL_POWER).name("Spell Power")
            .description("Increases the Power and Duration traits of spell payloads and mods by one per level when cast.")
            .fullRuneText("The Spell Power enchantment can be imbued through the use of Project, Item, and Void runes.  It can be applied to any wand or staff.  When applied, it increases the Power and Duration traits of spell payloads and mods by one per level when cast.")
            .partialRuneText("The Spell Power enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any wand or staff.  When applied, it increases the Power and Duration traits of spell payloads and mods by one per level when cast.")
            .build();
        this.enchantment(EnchantmentsPM.TREASURE).name("Treasure")
            .description("Increases the amount of loot dropped by creatures slain by the wielder, as well as the drops yielded by certain blocks, when using magick.")
            .fullRuneText("The Treasure enchantment can be imbued through the use of Project, Item, and Moon runes.  It can be applied to any wand or staff.  When applied, it increases the amount of loot dropped by creatures slain by the wielder, as well as the drops yielded by certain blocks, when using magick.")
            .partialRuneText("The Treasure enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any wand or staff.  When applied, it increases the amount of loot dropped by creatures slain by the wielder, as well as the drops yielded by certain blocks, when using magick.")
            .build();
        this.enchantment(EnchantmentsPM.BLUDGEONING).name("Bludgeoning")
            .description("Increases the damage done by the wielder when using the staff as a melee weapon.")
            .fullRuneText("The Bludgeoning enchantment can be imbued through the use of Project, Item, and Earth runes.  It can be applied to any staff.  When applied, it increases the damage done by the wielder when using the staff as a melee weapon.")
            .partialRuneText("The Bludgeoning enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any staff.  When applied, it increases the damage done by the wielder when using the staff as a melee weapon.")
            .build();
        this.enchantment(EnchantmentsPM.REVERBERATION).name("Reverberation")
            .description("Increases the tool's area of effect when digging.")
            .fullRuneText("The Reverberation enchantment can be imbued through the use of Project, Area, and Earth runes.  It can be applied to any axe, pickaxe, shovel, or hoe.  When applied, it increases the tool's area of effect when digging.  It is incompatible with the Disintegration enchantment.")
            .partialRuneText("The Reverberation enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any axe, pickaxe, shovel, or hoe.  When applied, it increases the tool's area of effect when digging.  It is incompatible with the Disintegration enchantment.")
            .build();
        this.enchantment(EnchantmentsPM.BOUNTY).name("Bounty")
            .description("Increases yields when fishing or harvesting crops.")
            .fullRuneText("The Bounty enchantment can be imbued through the use of Summon, Area, and Sea runes.  It can be applied to any fishing rod or hoe.  When applied, it increases yields when fishing or harvesting crops.")
            .partialRuneText("The Bounty enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any fishing rod or hoe.  When applied, it increases yields when fishing or harvesting crops.")
            .build();
        this.enchantment(EnchantmentsPM.DISINTEGRATION).name("Disintegration")
            .description("Breaks all blocks of the same type connected to the original target within a certain range.")
            .fullRuneText("The Disintegration enchantment can be imbued through the use of Project, Area, and Sky runes.  It can be applied to any axe, pickaxe, shovel, or hoe.  When applied, it causes a wave of destruction to radiate out from the broken block, breaking all blocks of the same type connected to the original.  It is incompatible with the Reverberation enchantment.")
            .partialRuneText("The Disintegration enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any axe, pickaxe, shovel, or hoe.  When applied, it causes a wave of destruction to radiate out from the broken block, breaking all blocks of the same type connected to the original.  It is incompatible with the Reverberation enchantment.")
            .build();
        this.enchantment(EnchantmentsPM.VERDANT).name("Verdant")
            .description("Stimulates rapid growth when used on a plant or fungus, similar to bone meal, at the cost of durability.")
            .fullRuneText("The Verdant enchantment can be imbued through the use of Summon, Creature, and Sun runes.  It can be applied to any hoe.  When applied, it causes the hoe to stimulate rapid growth when used on a plant or fungus, similar to bone meal, at the cost of durability.  Higher levels reduce the durability cost.")
            .partialRuneText("The Verdant enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any hoe.  When applied, it causes the hoe to stimulate rapid growth when used on a plant or fungus, similar to bone meal, at the cost of durability.  Higher levels reduce the durability cost.")
            .build();
        this.enchantment(EnchantmentsPM.LUCKY_STRIKE).name("Lucky Strike")
            .description("Yields bonus nuggets when harvesting certain valuable minerals.")
            .fullRuneText("The Lucky Strike enchantment can be imbued through the use of Summon, Item, and Moon runes.  It can be applied to any axe, pickaxe, shovel, or hoe.  When applied, it yields bonus nuggets when harvesting valuable minerals such as iron, gold, and quartz.")
            .partialRuneText("The Lucky Strike enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any axe, pickaxe, shovel, or hoe.  When applied, it yields bonus nuggets when harvesting valuable minerals such as iron, gold, and quartz.")
            .build();
        this.enchantment(EnchantmentsPM.RENDING).name("Rending")
            .description("Causes the victim to bleed for additional damage, with subsequent strikes increasing the severity of the bleed effect.")
            .fullRuneText("The Rending enchantment can be imbued through the use of Project, Creature, and Blood runes.  It can be applied to any sword, axe, or trident.  When applied, it causes the victim to bleed for additional damage, with subsequent strikes increasing the severity of the bleed effect.")
            .partialRuneText("The Rending enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any sword, axe, or trident.  When applied, it causes the victim to bleed for additional damage, with subsequent strikes increasing the severity of the bleed effect.")
            .build();
        this.enchantment(EnchantmentsPM.SOULPIERCING).name("Soulpiercing")
            .description("Causes the first strike against a target to drop soul gem slivers.")
            .fullRuneText("The Soulpiercing enchantment can be imbued through the use of Absorb, Creature, and Infernal runes.  It can be applied to any bow or crossbow.  When applied, the first strike against a target causes it to drop soul gem slivers.")
            .partialRuneText("The Soulpiercing enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any bow or crossbow.  When applied, the first strike against a target causes it to drop soul gem slivers.")
            .build();
        this.enchantment(EnchantmentsPM.ESSENCE_THIEF).name("Essence Thief")
            .description("Steals a portion of the victim's essence, causing them to drop essence dust when slain.")
            .fullRuneText("The Essence Thief enchantment can be imbued through the use of Summon, Item, and Void runes.  It can be applied to any sword, axe, trident, or wand.  When applied, it steals a portion of the victim's essence, causing them to drop essence dust when slain.")
            .partialRuneText("The Essence Thief enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any sword, axe, trident, or wand.  When applied, it steals a portion of the victim's essence, causing them to drop essence dust when slain.")
            .build();
        this.enchantment(EnchantmentsPM.BULWARK).name("Bulwark")
            .description("Causes the wielder to take less damage from all sources while blocking.")
            .fullRuneText("The Bulwark enchantment can be imbued through the use of Protect, Self, and Hallowed runes.  It can be applied to any shield.  When applied, it causes the wielder to take less damage from all sources while blocking, even sources that a shield could not normally protect against.")
            .partialRuneText("The Bulwark enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any shield.  When applied, it causes the wielder to take less damage from all sources while blocking, even sources that a shield could not normally protect against.")
            .build();
        this.enchantment(EnchantmentsPM.MAGICK_PROTECTION).name("Magick Protection")
            .description("Significantly reduces all incoming magick damage, such as from sorcerous spells.")
            .fullRuneText("The Magick Protection enchantment can be imbued through the use of Protect, Self, and Void runes.  It can be applied to any piece of armor.  When applied, it significantly reduces all incoming magick damage, such as from sorcerous spells.  It is incompatible with other Protection enchantments.")
            .partialRuneText("The Magick Protection enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any piece of armor.  When applied, it significantly reduces all incoming magick damage, such as from sorcerous spells.  It is incompatible with other Protection enchantments.")
            .build();

        // Generate vanilla enchantment localization extensions
        this.enchantment(Enchantments.ALL_DAMAGE_PROTECTION)
            .fullRuneText("The Protection enchantment can be imbued through the use of Protect, Self, and Earth runes.  It can be applied to any piece of armor.  When applied, it reduces all incoming damage.  It is incompatible with other Protection enchantments.")
            .partialRuneText("The Protection enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any piece of armor.  When applied, it reduces all incoming damage.  It is incompatible with other Protection enchantments.")
            .build();
        this.enchantment(Enchantments.FIRE_PROTECTION)
            .fullRuneText("The Fire Protection enchantment can be imbued through the use of Protect, Self, and Infernal runes.  It can be applied to any piece of armor.  When applied, it significantly reduces all incoming fire damage as well as the amount of time that the wearer is engulfed in flames.  It is incompatible with other Protection enchantments.")
            .partialRuneText("The Fire Protection enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any piece of armor.  When applied, it significantly reduces all incoming fire damage as well as the amount of time that the wearer is engulfed in flames.  It is incompatible with other Protection enchantments.")
            .build();
        this.enchantment(Enchantments.FALL_PROTECTION)
            .fullRuneText("The Feather Falling enchantment can be imbued through the use of Project, Item, and Sky runes.  It can be applied to any pair of boots.  When applied, it greatly reduces all damage sustained from falling.")
            .partialRuneText("The Feather Falling enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any pair of boots.  When applied, it greatly reduces all damage sustained from falling.")
            .build();
        this.enchantment(Enchantments.BLAST_PROTECTION)
            .fullRuneText("The Blast Protection enchantment can be imbued through the use of Protect, Self, and Moon runes.  It can be applied to any piece of armor.  When applied, it significantly reduces all incoming explosion damage as well as the distance that the wearer is knocked back by explosions.  It is incompatible with other Protection enchantments.")
            .partialRuneText("The Blast Protection enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any piece of armor.  When applied, it significantly reduces all incoming explosion damage as well as the distance that the wearer is knocked back by explosions.  It is incompatible with other Protection enchantments.")
            .build();
        this.enchantment(Enchantments.PROJECTILE_PROTECTION)
            .fullRuneText("The Projectile Protection enchantment can be imbued through the use of Protect, Self, and Sky runes.  It can be applied to any piece of armor.  When applied, it significantly reduces all incoming damage from projectiles, such as arrows.  It is incompatible with other Protection enchantments.")
            .partialRuneText("The Projectile Protection enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any piece of armor.  When applied, it significantly reduces all incoming damage from projectiles, such as arrows.  It is incompatible with other Protection enchantments.")
            .build();
        this.enchantment(Enchantments.RESPIRATION)
            .fullRuneText("The Respiration enchantment can be imbued through the use of Project, Item, and Sky runes.  It can be applied to any helmet.  When applied, it increases the amount of time the wearer can remain underwater before drowning.")
            .partialRuneText("The Respiration enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any helmet.  When applied, it increases the amount of time the wearer can remain underwater before drowning.")
            .build();
        this.enchantment(Enchantments.AQUA_AFFINITY)
            .fullRuneText("The Aqua Affinity enchantment can be imbued through the use of Project, Item, and Sea runes.  It can be applied to any helmet.  When applied, it increases the speed at which the wearer can dig while underwater.")
            .partialRuneText("The Aqua Affinity enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any helmet.  When applied, it increases the speed at which the wearer can dig while underwater.")
            .build();
        this.enchantment(Enchantments.THORNS)
            .fullRuneText("The Thorns enchantment can be imbued through the use of Project, Creature, and Blood runes.  It can be applied to any piece of armor.  When applied, it reflects a portion of incoming damage onto the attacker, at the cost of durability.")
            .partialRuneText("The Thorns enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any piece of armor.  When applied, it reflects a portion of incoming damage onto the attacker, at the cost of durability.")
            .build();
        this.enchantment(Enchantments.DEPTH_STRIDER)
            .fullRuneText("The Depth Strider enchantment can be imbued through the use of Project, Item, and Sea runes.  It can be applied to any pair of boots.  When applied, it increases the wearer's underwater movement speed.  It is incompatible with the Frost Walker enchantment.")
            .partialRuneText("The Depth Strider enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any pair of boots.  When applied, it increases the wearer's underwater movement speed.  It is incompatible with the Frost Walker enchantment.")
            .build();
        this.enchantment(Enchantments.FROST_WALKER)
            .fullRuneText("The Frost Walker enchantment can be imbued through the use of Project, Area, and Sea runes.  It can be applied to any pair of boots.  When applied, it freezes water beneath the wearer into frosted ice, allowing the wearer to walk on it.  It is incompatible with the Depth Strider enchantment.")
            .partialRuneText("The Frost Walker enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any pair of boots.  When applied, it freezes water beneath the wearer into frosted ice, allowing the wearer to walk on it.  It is incompatible with the Depth Strider enchantment.")
            .build();
        this.enchantment(Enchantments.SHARPNESS)
            .fullRuneText("The Sharpness enchantment can be imbued through the use of Project, Item, and Earth runes.  It can be applied to any sword or axe.  When applied, it increases the damage done by the wielder.  It is incompatible with other damage-increasing enchantments.")
            .partialRuneText("The Sharpness enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any sword or axe.  When applied, it increases the damage done by the wielder.  It is incompatible with other damage-increasing enchantments.")
            .build();
        this.enchantment(Enchantments.KNOCKBACK)
            .fullRuneText("The Knockback enchantment can be imbued through the use of Project, Creature, and Earth runes.  It can be applied to any sword.  When applied, it increases the distance which a creature struck by the wielder is knocked back.")
            .partialRuneText("The Knockback enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any sword.  When applied, it increases the distance which a creature struck by the wielder is knocked back.")
            .build();
        this.enchantment(Enchantments.FIRE_ASPECT)
            .fullRuneText("The Fire Aspect enchantment can be imbued through the use of Project, Item, and Infernal runes.  It can be applied to any sword.  When applied, creatures struck by the wielder will be set on fire.")
            .partialRuneText("The Fire Aspect enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any sword.  When applied, creatures struck by the wielder will be set on fire.")
            .build();
        this.enchantment(Enchantments.MOB_LOOTING)
            .fullRuneText("The Looting enchantment can be imbued through the use of Project, Item, and Moon runes.  It can be applied to any sword.  When applied, it increases the amount of loot dropped by creatures slain by the wielder.")
            .partialRuneText("The Looting enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any sword.  When applied, it increases the amount of loot dropped by creatures slain by the wielder.")
            .build();
        this.enchantment(Enchantments.SWEEPING_EDGE)
            .fullRuneText("The Sweeping Edge enchantment can be imbued through the use of Project, Area, and Sky runes.  It can be applied to any sword.  When applied, it increases the damage done by the wielder with sweep attacks.")
            .partialRuneText("The Sweeping Edge enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any sword.  When applied, it increases the damage done by the wielder with sweep attacks.")
            .build();
        this.enchantment(Enchantments.BLOCK_EFFICIENCY)
            .fullRuneText("The Efficiency enchantment can be imbued through the use of Project, Item, and Sky runes.  It can be applied to any axe, pickaxe, shears, or shovel.  When applied, it increases the speed with which the wielder may use the tool.")
            .partialRuneText("The Efficiency enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any axe, pickaxe, shears, or shovel.  When applied, it increases the speed with which the wielder may use the tool.")
            .build();
        this.enchantment(Enchantments.SILK_TOUCH)
            .fullRuneText("The Silk Touch enchantment can be imbued through the use of Project, Item, and Sea runes.  It can be applied to any axe, pickaxe, or shovel.  When applied, it causes any blocks broken by the wielder to be dropped intact and without damage.  It is incompatible with the Fortune enchantment.")
            .partialRuneText("The Silk Touch enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any axe, pickaxe, or shovel.  When applied, it causes any blocks broken by the wielder to be dropped intact and without damage.  It is incompatible with the Fortune enchantment.")
            .build();
        this.enchantment(Enchantments.UNBREAKING)
            .fullRuneText("The Unbreaking enchantment can be imbued through the use of Protect, Item, and Earth runes.  It can be applied to any item that may be damaged or broken.  When applied, it increases the durability of the item, making it harder to damage.")
            .partialRuneText("The Unbreaking enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any item that may be damaged or broken.  When applied, it increases the durability of the item, making it harder to damage.")
            .build();
        this.enchantment(Enchantments.BLOCK_FORTUNE)
            .fullRuneText("The Fortune enchantment can be imbued through the use of Project, Item, and Moon runes.  It can be applied to any axe, pickaxe, or shovel.  When applied, it increases the drops yielded by certain blocks.  It is incompatible with the Silk Touch enchantment.")
            .partialRuneText("The Fortune enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any axe, pickaxe, or shovel.  When applied, it increases the drops yielded by certain blocks.  It is incompatible with the Silk Touch enchantment.")
            .build();
        this.enchantment(Enchantments.POWER_ARROWS)
            .fullRuneText("The Power enchantment can be imbued through the use of Project, Item, and Sky runes.  It can be applied to any bow.  When applied, it increases the damage done by arrows fired by the wielder.")
            .partialRuneText("The Power enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any bow.  When applied, it increases the damage done by arrows fired by the wielder.")
            .build();
        this.enchantment(Enchantments.PUNCH_ARROWS)
            .fullRuneText("The Punch enchantment can be imbued through the use of Project, Creature, and Earth runes.  It can be applied to any bow.  When applied, it increases the distance which a creature struck by the wielder's arrows is knocked back.")
            .partialRuneText("The Punch enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any bow.  When applied, it increases the distance which a creature struck by the wielder's arrows is knocked back.")
            .build();
        this.enchantment(Enchantments.FLAMING_ARROWS)
            .fullRuneText("The Flame enchantment can be imbued through the use of Project, Item, and Infernal runes.  It can be applied to any bow.  When applied, it causes arrows fired by the wielder to set their targets on fire.")
            .partialRuneText("The Flame enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any bow.  When applied, it causes arrows fired by the wielder to set their targets on fire.")
            .build();
        this.enchantment(Enchantments.INFINITY_ARROWS)
            .fullRuneText("The Infinity enchantment can be imbued through the use of Summon, Item, and Sky runes.  It can be applied to any bow.  When applied, it prevents regular arrows from being consumed when the wielder fires.  It is incompatible with the Mending enchantment.")
            .partialRuneText("The Infinity enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any bow.  When applied, it prevents regular arrows from being consumed when the wielder fires.  It is incompatible with the Mending enchantment.")
            .build();
        this.enchantment(Enchantments.FISHING_LUCK)
            .fullRuneText("The Luck of the Sea enchantment can be imbued through the use of Project, Item, and Moon runes.  It can be applied to any fishing rod.  When applied, it increases the quality of the items that the wielder fishes up.")
            .partialRuneText("The Luck of the Sea enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any fishing rod.  When applied, it increases the quality of the items that the wielder fishes up.")
            .build();
        this.enchantment(Enchantments.FISHING_SPEED)
            .fullRuneText("The Lure enchantment can be imbued through the use of Summon, Creature, and Sea runes.  It can be applied to any fishing rod.  When applied, it decreases the time until the wielder gets a bite when fishing.")
            .partialRuneText("The Lure enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any fishing rod.  When applied, it decreases the time until the wielder gets a bite when fishing.")
            .build();
        this.enchantment(Enchantments.LOYALTY)
            .fullRuneText("The Loyalty enchantment can be imbued through the use of Summon, Item, and Sea runes.  It can be applied to any trident.  When applied, it causes the trident to return to the wielder when thrown.  It is incompatible with the Riptide enchantment.")
            .partialRuneText("The Loyalty enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any trident.  When applied, it causes the trident to return to the wielder when thrown.  It is incompatible with the Riptide enchantment.")
            .build();
        this.enchantment(Enchantments.IMPALING)
            .fullRuneText("The Impaling enchantment can be imbued through the use of Project, Item, and Sea runes.  It can be applied to any trident.  When applied, it causes the wielder to deal additional damage to oceanic creatures.")
            .partialRuneText("The Impaling enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any trident.  When applied, it causes the wielder to deal additional damage to oceanic creatures.")
            .build();
        this.enchantment(Enchantments.RIPTIDE)
            .fullRuneText("The Riptide enchantment can be imbued through the use of Summon, Self, and Sea runes.  It can be applied to any trident.  When applied, it causes the wielder to be launched along with the trident upon throwing it.  This effect only functions in water or rain.  It is incompatible with the Channeling and Loyalty enchantments.")
            .partialRuneText("The Riptide enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any trident.  When applied, it causes the wielder to be launched along with the trident upon throwing it.  This effect only functions in water or rain.  It is incompatible with the Channeling and Loyalty enchantments.")
            .build();
        this.enchantment(Enchantments.CHANNELING)
            .fullRuneText("The Channeling enchantment can be imbued through the use of Summon, Area, and Sky runes.  It can be applied to any trident.  When applied, it causes bolts of lightning to be channeled toward struck creatures.  This effect only functions during thunderstorms, when the target has a clear view of the sky.  It is incompatible with the Riptide enchantment.")
            .partialRuneText("The Channeling enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any trident.  When applied, it causes bolts of lightning to be channeled toward struck creatures.  This effect only functions during thunderstorms, when the target has a clear view of the sky.  It is incompatible with the Riptide enchantment.")
            .build();
        this.enchantment(Enchantments.MULTISHOT)
            .fullRuneText("The Multishot enchantment can be imbued through the use of Summon, Item, and Sky runes.  It can be applied to any crossbow.  When applied, it causes the wielder to fire three arrows at the cost of one, simultaneously.  It is incompatible with the Piercing enchantment.")
            .partialRuneText("The Multishot enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any crossbow.  When applied, it causes the wielder to fire three arrows at the cost of one, simultaneously.  It is incompatible with the Piercing enchantment.")
            .build();
        this.enchantment(Enchantments.QUICK_CHARGE)
            .fullRuneText("The Quick Charge enchantment can be imbued through the use of Project, Item, and Sky runes.  It can be applied to any crossbow.  When applied, it decreases the time it takes for the wielder to reload the crossbow.")
            .partialRuneText("The Quick Charge enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any crossbow.  When applied, it decreases the time it takes for the wielder to reload the crossbow.")
            .build();
        this.enchantment(Enchantments.PIERCING)
            .fullRuneText("The Piercing enchantment can be imbued through the use of Project, Item, and Earth runes.  It can be applied to any crossbow.  When applied, it causes arrows fired by the wielder to pass through multiple enemies.  It is incompatible with the Multishot enchantment.")
            .partialRuneText("The Piercing enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any crossbow.  When applied, it causes arrows fired by the wielder to pass through multiple enemies.  It is incompatible with the Multishot enchantment.")
            .build();
        this.enchantment(Enchantments.MENDING)
            .fullRuneText("The Mending enchantment can be imbued through the use of Absorb, Item, and Sun runes.  It can be applied to any item that may be damaged or broken.  When applied, it causes the item to repair itself when the wielder collects experience orbs.  It is incompatible with the Infinity enchantment.")
            .partialRuneText("The Mending enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any item that may be damaged or broken.  When applied, it causes the item to repair itself when the wielder collects experience orbs.  It is incompatible with the Infinity enchantment.")
            .build();
        this.enchantment(Enchantments.BANE_OF_ARTHROPODS)
            .fullRuneText("The Bane of Arthropods enchantment can be imbued through the use of Project, Item, and Blood runes.  It can be applied to any sword or axe.  When applied, it increases the damage done by the wielder to insect-like creatures.  It is incompatible with other damage-increasing enchantments.")
            .partialRuneText("The Bane of Arthropods enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any sword or axe.  When applied, it increases the damage done by the wielder to insect-like creatures.  It is incompatible with other damage-increasing enchantments.")
            .build();
        this.enchantment(Enchantments.SMITE)
            .fullRuneText("The Smite enchantment can be imbued through the use of Project, Creature, and Sun runes.  It can be applied to any sword or axe.  When applied, it increases the damage done by the wielder to undead creatures.  It is incompatible with other damage-increasing enchantments.")
            .partialRuneText("The Smite enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any sword or axe.  When applied, it increases the damage done by the wielder to undead creatures.  It is incompatible with other damage-increasing enchantments.")
            .build();
        this.enchantment(Enchantments.SOUL_SPEED)
            .fullRuneText("The Soul Speed enchantment can be imbued through the use of Project, Self, and Infernal runes.  It can be applied to any pair of boots.  When applied, it increases the wearer's movement speed when standing on soul sand or soul soil.")
            .partialRuneText("The Soul Speed enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any pair of boots.  When applied, it increases the wearer's movement speed when standing on soul sand or soul soil.")
            .build();
        this.enchantment(Enchantments.SWIFT_SNEAK)
            .fullRuneText("The Swift Sneak enchantment can be imbued through the use of Project, Self, and Sky runes.  It can be applied to any pair of pants.  When applied, it increases the wearer's movement speed when sneaking.")
            .partialRuneText("The Swift Sneak enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any pair of pants.  When applied, it increases the wearer's movement speed when sneaking.")
            .build();

        // Generate armor trim localizations
        this.trimMaterial("runic_trim_materials", List.of(TrimMaterialsPM.RUNE_EARTH, TrimMaterialsPM.RUNE_SEA, TrimMaterialsPM.RUNE_SKY, TrimMaterialsPM.RUNE_SUN, TrimMaterialsPM.RUNE_MOON, TrimMaterialsPM.RUNE_BLOOD, TrimMaterialsPM.RUNE_INFERNAL, TrimMaterialsPM.RUNE_VOID, TrimMaterialsPM.RUNE_HALLOWED))
            .namePattern("%s Rune Material").build();
        this.trimPattern(TrimPatternsPM.RUNIC).name("Runic Armor Trim").build();
        
        // Generate death message localizations for damage types
        this.damageType(DamageTypesPM.BLEEDING).name("%1$s bled to death").player("%1$s bled to death whilst fighting %2$s").build();
        this.damageType(DamageTypesPM.HELLISH_CHAIN).name("%1$s was killed by %2$s").item("%1$s was killed by %2$s using %3$s").build();
        this.damageType(DamageTypesPM.SORCERY_EARTH).name("%1$s was killed by %2$s using sorcery").item("%1$s was killed by %2$s using %3$s").build();
        this.damageType(DamageTypesPM.SORCERY_SEA).name("%1$s was killed by %2$s using sorcery").item("%1$s was killed by %2$s using %3$s").build();
        this.damageType(DamageTypesPM.SORCERY_SKY).name("%1$s was killed by %2$s using sorcery").item("%1$s was killed by %2$s using %3$s").build();
        this.damageType(DamageTypesPM.SORCERY_SUN).name("%1$s was killed by %2$s using sorcery").item("%1$s was killed by %2$s using %3$s").build();
        this.damageType(DamageTypesPM.SORCERY_MOON).name("%1$s was killed by %2$s using sorcery").item("%1$s was killed by %2$s using %3$s").build();
        this.damageType(DamageTypesPM.SORCERY_BLOOD).name("%1$s was killed by %2$s using sorcery").item("%1$s was killed by %2$s using %3$s").build();
        this.damageType(DamageTypesPM.SORCERY_INFERNAL).name("%1$s was killed by %2$s using sorcery").item("%1$s was killed by %2$s using %3$s").build();
        this.damageType(DamageTypesPM.SORCERY_VOID).name("%1$s was killed by %2$s using sorcery").item("%1$s was killed by %2$s using %3$s").build();
        this.damageType(DamageTypesPM.SORCERY_HALLOWED).name("%1$s was killed by %2$s using sorcery").item("%1$s was killed by %2$s using %3$s").build();
        
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
        this.grimoire("other_header").name("Other Topics").build();
        this.grimoire("stats_header").name("Statistics").build();
        this.grimoire("attunement_header").name("Attunements").build();
        this.grimoire("rune_enchantment_header").name("Rune Enchantments").build();
        this.grimoire("recipe_index_header").name("Recipes").build();
        this.grimoire("attunement_gain_header").name("Attunements Gained").build();
        this.grimoire("addendum_header").name("Addendum %1$d").build();
        this.grimoire("requirements_header").name("Requirements").build();
        this.grimoire("shapeless_arcane_recipe_header").name("Arcane Workbench recipe (Shapeless)").build();
        this.grimoire("shaped_arcane_recipe_header").name("Arcane Workbench recipe").build();
        this.grimoire("shapeless_recipe_header").name("Workbench recipe (Shapeless)").build();
        this.grimoire("shaped_recipe_header").name("Workbench recipe").build();
        this.grimoire("ritual_recipe_header").name("Ritual Altar recipe").build();
        this.grimoire("runecarving_recipe_header").name("Runecarving Table recipe").build();
        this.grimoire("concocting_recipe_header").name("Concocter recipe (Shapeless)").build();
        this.grimoire("smelting_recipe_header").name("Smelting recipe").build();
        this.grimoire("dissolution_recipe_header").name("Dissolution recipe").build();
        this.grimoire("must_obtain_header").name("Will be consumed:").build();
        this.grimoire("must_craft_header").name("Must craft:").build();
        this.grimoire("required_knowledge_header").name("Required knowledge:").build();
        this.grimoire("required_research_header").name("Must be discovered:").build();
        this.grimoire("ritual_offerings_header").name("Offerings:").build();
        this.grimoire("ritual_props_header").name("Props:").build();
        this.grimoire("complete_button").name("Complete").build();
        this.grimoire("completing_text").name("Completing...").build();
        this.grimoire("section_header").sub("new").output("New").end().build();
        this.grimoire("section_header").sub("updated").output("Updated").end().build();
        this.grimoire("section_header").sub("complete").output("Complete").end().build();
        this.grimoire("section_header").sub("in_progress").output("In Progress").end().build();
        this.grimoire("section_header").sub("available").output("Available").end().build();
        this.grimoire("section_header").sub("upcoming").output("Upcoming").end().build();
        this.grimoire("section_header").sub("unavailable").output("Unavailable").end().build();
        this.grimoire("missing_block").name("Missing block!").build();
        this.grimoire("upcoming_tooltip_header").name("Missing requirements:").build();
        this.grimoire("recipe_metadata").sub("discipline").output("Discipline:").end().build();
        this.grimoire("recipe_metadata").sub("entry").output("Research entry:").end().build();
        this.grimoire("attunement_meter").sub("tooltip").sub("header").output("%1$s attunement:").end().build();
        this.grimoire("attunement_meter").sub("tooltip").sub("permanent").output("Permanent: %1$d").end().build();
        this.grimoire("attunement_meter").sub("tooltip").sub("induced").output("Induced: %1$d").end().build();
        this.grimoire("attunement_meter").sub("tooltip").sub("temporary").output("Temporary: %1$d").end().build();
        
        // Generate JEI GUI localizations
        this.jei("ritual").sub("offerings").sub("header").output("Offerings").end().build();
        this.jei("ritual").sub("props").sub("header").output("Props").end().build();
        
        // Generate ritual feedback localizations
        this.ritual("instability").name("%1$s: %2$s").build();
        this.ritual("instability").sub("header").output("Instability").end().build();
        this.ritual("instability").sub("rating").output(0, "Trivial", "Low", "Moderate", "High", "Very High", "Extreme").end().build();
        this.ritual("prop").sub("ritual_candle").output("The ritual requires you to light a candle now!").end().build();
        this.ritual("prop").sub("incense_brazier").output("The ritual requires you to burn some incense now!").end().build();
        this.ritual("prop").sub("ritual_lectern").output("The ritual requires you to place an enchanted book now!").end().build();
        this.ritual("prop").sub("ritual_bell").output("The ritual requires you to ring a bell now!").end().build();
        this.ritual("prop").sub("bloodletter").output("The ritual requires you to shed your blood now!").end().build();
        this.ritual("prop").sub("soul_anvil").output("The ritual requires you to smash a soul gem now!").end().build();
        this.ritual("prop").sub("celestial_harp").output("The ritual requires you to play a celestial harp now!").end().build();
        this.ritual("prop").sub("entropy_sink").output("The ritual requires you to feed essence to an entropy sink now!").end().build();
        this.ritual("warning").sub("missing_offering").output("The ritual cannot find the required %1$s offering!").end().build();
        this.ritual("warning").sub("missing_offering").sub("empty").output("The ritual cannot find a required offering!").end().build();
        this.ritual("warning").sub("missing_prop").output("The ritual cannot find the required %1$s prop!").end().build();
        this.ritual("warning").sub("missing_prop").sub("empty").output("The ritual cannot find a required prop!").end().build();
        this.ritual("warning").sub("channel_interrupt").output("The channeling of the required offering %1$s has been interrupted!").end().build();
        this.ritual("warning").sub("channel_interrupt").sub("empty").output("The channeling of a required offering has been interrupted!").end().build();
        this.ritual("warning").sub("prop_interrupt").output("The activation of the required prop %1$s has been interrupted!").end().build();
        this.ritual("warning").sub("prop_interrupt").sub("empty").output("The activation of a required prop has been interrupted!").end().build();
        this.ritual("warning").sub("obstructed").output("The area above the ritual altar is obstructed").end().build();
        this.ritual("info").sub("started").output("Ritual started!").end().build();
        this.ritual("info").sub("complete").output("Ritual complete!").end().build();
        this.ritual("info").sub("canceled").output("Ritual canceled").end().build();
        
        // Generate concoction localizations
        this.concoction("doses_remaining").name("Doses: %1$d").build();
        this.concoction("charges_remaining").name("Charges: %1$d").build();
        this.concoction("fuse_length").name("Fuse: %1$s").build();
        this.concoction("fuse_set").name("Bomb fuse set to %1$s").build();
        this.concoction("fuse").sub("impact").output("Impact").end().build();
        this.concoction("fuse").sub("short").output("1s").end().build();
        this.concoction("fuse").sub("medium").output("3s").end().build();
        this.concoction("fuse").sub("long").output("5s").end().build();
        
        // Generate keybind localizations
        this.add(KeyBindings.KEY_CATEGORY, "Primal Magick");
        this.keyMapping(KeyBindings.CHANGE_SPELL_KEY, "change_spell").name("Change Active Spell").build();
        this.keyMapping(KeyBindings.CARPET_FORWARD_KEY, "carpet_forward").name("Flying Carpet Forward").build();
        this.keyMapping(KeyBindings.CARPET_BACKWARD_KEY, "carpet_backward").name("Flying Carpet Backward").build();
        this.keyMapping(KeyBindings.GRIMOIRE_NEXT_PAGE, "grimoire_next_page").name("Grimoire Next Page").build();
        this.keyMapping(KeyBindings.GRIMOIRE_PREV_PAGE, "grimoire_prev_page").name("Grimoire Previous Page").build();
        this.keyMapping(KeyBindings.GRIMOIRE_PREV_TOPIC, "grimoire_prev_topic").name("Grimoire Previous Topic").build();
        this.keyMapping(KeyBindings.VIEW_AFFINITY_KEY, "view_affinity").name("View Item Affinities").build();
        
        // Generate statistics localizations
        this.stat(StatsPM.GRIMOIRE_READ).name("Times Grimoire consulted").build();
        this.stat(StatsPM.ITEMS_ANALYZED).name("Item types analyzed").build();
        this.stat(StatsPM.ENTITIES_ANALYZED).name("Creature types analyzed").build();
        this.stat(StatsPM.MANA_SIPHONED).name("Mana siphoned from fonts").build();
        this.stat(StatsPM.OBSERVATIONS_MADE).name("Observations made").build();
        this.stat(StatsPM.THEORIES_FORMED).name("Theories formed").build();
        this.stat(StatsPM.CRAFTED_MANAWEAVING).name("Manaweaving items crafted").build();
        this.stat(StatsPM.CRAFTED_ALCHEMY).name("Alchemy items crafted").build();
        this.stat(StatsPM.CRAFTED_SORCERY).name("Sorcery items crafted").build();
        this.stat(StatsPM.CRAFTED_RUNEWORKING).name("Runeworking items crafted").build();
        this.stat(StatsPM.CRAFTED_RITUAL).name("Ritual Magick items crafted").build();
        this.stat(StatsPM.CRAFTED_MAGITECH).name("Magitech items crafted").build();
        this.stat(StatsPM.SPELLS_CAST).name("Spells cast").build();
        this.stat(StatsPM.SPELLS_CRAFTED).name("Spells crafted").build();
        this.stat(StatsPM.SPELLS_CRAFTED_MAX_COST).name("Cost of most expensive spell crafted").build();
        this.stat(StatsPM.RESEARCH_PROJECTS_COMPLETED).name("Research projects completed").build();
        this.stat(StatsPM.ITEMS_RUNESCRIBED).name("Items runescribed").build();
        this.stat(StatsPM.RITUALS_COMPLETED).name("Rituals conducted").build();
        this.stat(StatsPM.RITUAL_MISHAPS).name("Ritual mishaps endured").build();
        this.stat(StatsPM.CONCOCTIONS_USED).name("Concoctions used").build();
        this.stat(StatsPM.DISTANCE_TELEPORTED_CM).name("Distance teleported").build();
        this.stat(StatsPM.MANA_SPENT_TOTAL).name("Total mana spent").build();
        this.stat(StatsPM.MANA_SPENT_EARTH).name("Earth mana spent").build();
        this.stat(StatsPM.MANA_SPENT_SEA).name("Sea mana spent").build();
        this.stat(StatsPM.MANA_SPENT_SKY).name("Sky mana spent").build();
        this.stat(StatsPM.MANA_SPENT_SUN).name("Sun mana spent").build();
        this.stat(StatsPM.MANA_SPENT_MOON).name("Moon mana spent").build();
        this.stat(StatsPM.MANA_SPENT_BLOOD).name("Blood mana spent").build();
        this.stat(StatsPM.MANA_SPENT_INFERNAL).name("Infernal mana spent").build();
        this.stat(StatsPM.MANA_SPENT_VOID).name("Void mana spent").build();
        this.stat(StatsPM.MANA_SPENT_HALLOWED).name("Hallowed mana spent").build();
        this.stat(StatsPM.SHRINE_FOUND_EARTH).name("Earth shrines found").build();
        this.stat(StatsPM.SHRINE_FOUND_SEA).name("Sea shrines found").build();
        this.stat(StatsPM.SHRINE_FOUND_SKY).name("Sky shrines found").build();
        this.stat(StatsPM.SHRINE_FOUND_SUN).name("Sun shrines found").build();
        this.stat(StatsPM.SHRINE_FOUND_MOON).name("Moon shrines found").build();
        this.stat(StatsPM.BLOCKS_BROKEN_BAREHANDED).name("Hard blocks broken bare-handed").build();
        this.stat(StatsPM.TREANTS_NAMED).name("Treefolk driven to self-immolation").build();
        
        // Generate research project localizations
        this.researchProject("trade").name("Trade").text("Perhaps some nearby villagers have magickal knowledge to trade.  You assemble some goods they'll find valuable.").build();
        this.researchProject("mundane_tinkering").name("Mundane Tinkering").text("You spend some time working with your tools, creating paraphernalia to help with your magickal pursuits.").build();
        this.researchProject("redstone_tinkering").name("Redstone Tinkering").text("You spend some time tinkering with your redstone devices, seeing if their principles will help your magitech studies.").build();
        this.researchProject("expedition").name("Expedition").text("Who knows what arcane secrets lie hidden in the dungeons of the world?  You prepare an expedition to find some.").build();
        this.researchProject("nether_expedition").name("Nether Expedition").text("The fortresses of the Nether are surely filled with forbidden knowledge.  You prepare an expedition to find some.").build();
        this.researchProject("recuperation").name("Rest and Recuperation").text("Too much work dulls the mind.  Perhaps an idea will strike you if you take some time to stop and smell the roses.").build();
        this.researchProject("essence_analysis").name("Essence Analysis").text("Magickal essence has many amazing properties.  You study some to see how it reacts with other substances.").build();
        this.researchProject("brewing_experiments").name("Brewing Experiments").text("There are many similarities between brewing and wizardly alchemy.  Perhaps studying the former will give you insight into the latter.").build();
        this.researchProject("wand_tinkering").name("Wand Tinkering").text("You experiment with different methods and materials for constructing wands, hoping to refine your understanding of the process.").build();
        this.researchProject("spellwork").name("Spellwork").text("You devise and cast some simple spells, hoping to refine your technique.  Practice makes perfect, after all.").build();
        this.researchProject("advanced_essence_analysis").name("Adv. Essence Analysis").text("Analysis of basic magickal essence was rewarding.  It stands to reason that more advanced essence structures will teach you even more.").build();
        this.researchProject("enchanting_studies").name("Enchanting Studies").text("Enchanting and manaweaving are close siblings.  Studying their similarities and differences could be enlightening.").build();
        this.researchProject("observation_analysis").name("Observation Analysis").text("A more thorough analysis of your observations may yield surprising discoveries.").build();
        this.researchProject("hit_the_books").name("Hit the Books").text("There's bound to be something in all of this reading material that would be useful in your studies.").build();
        this.researchProject("beacon_emanations").name("Beacon Emanations").text("While they're not of the wizardly arts, Beacons are clearly more than just mundane objects.  Studying their emanations could be valuable.").build();
        this.researchProject("portal_detritus").name("Portal Detritus").text("Strange things sometimes come back through Nether portals.  You could learn much by studying them.").build();
        this.researchProject("draconic_energies").name("Draconic Energies").text("This egg seems to harmonize with ender pearls, releasing intriguing energies.  Is this really such a good idea, though?").build();
        this.researchProject("piglin_barter").name("Piglin Bartering").text("The piglins of the Nether must surely know secrets about that infernal place.  Exploit their craving for gold.").build();
        this.researchProject("apiamancy").name("Apiamancy").text("Some say you can see the future in the movement of bees.  It's probably overstated, but you might still learn something.").build();
        this.researchProject("end_expedition").name("End Expedition").text("The cities of The End reek of secrets.  You prepare an expedition to brave their dangers and bring some home.").build();
        this.researchProject("master_essence_analysis").name("Master Essence Analysis").text("Study of magickal essence has been profitable for you.  It's time to learn what the purest samples have to teach.").build();
        this.researchProject("draconic_memories").name("Draconic Memories").text("This dragon hoarded experiences, not gold.  Even in death it hungers for more.  Feed it, and see what it can teach you.").build();

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
