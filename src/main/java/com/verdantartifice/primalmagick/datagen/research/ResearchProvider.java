package com.verdantartifice.primalmagick.datagen.research;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.research.KnowledgeType;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

public class ResearchProvider implements DataProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    protected final PackOutput packOutput;
    
    public ResearchProvider(PackOutput packOutput) {
        this.packOutput = packOutput;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        ImmutableList.Builder<CompletableFuture<?>> futuresBuilder = new ImmutableList.Builder<>();
        Map<ResourceLocation, IFinishedResearchEntry> map = new HashMap<>();
        this.registerEntries((research) -> {
            if (map.put(research.getId(), research) != null) {
                LOGGER.debug("Duplicate research entry in data generation: " + research.getId().toString());
            }
        });
        map.entrySet().forEach(entry -> {
            futuresBuilder.add(DataProvider.saveStable(cache, entry.getValue().getEntryJson(), this.getPath(this.packOutput, entry.getKey())));
        });
        return CompletableFuture.allOf(futuresBuilder.build().toArray(CompletableFuture[]::new));
    }

    private Path getPath(PackOutput output, ResourceLocation entryLoc) {
        return output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(entryLoc.getNamespace()).resolve("grimoire").resolve(entryLoc.getPath() + ".json");
    }
    
    protected void registerEntries(Consumer<IFinishedResearchEntry> consumer) {
        this.registerMagitechEntries(consumer);
    }

    protected void registerMagitechEntries(Consumer<IFinishedResearchEntry> consumer) {
        String discipline = "MAGITECH";
        ResearchEntryBuilder.entry("HONEY_EXTRACTOR", discipline).icon(ItemsPM.HONEY_EXTRACTOR.get()).parent("BASIC_MAGITECH")
            .stage(ResearchStageBuilder.stage().requiredItemStack(Items.HONEYCOMB).requiredItemStack(Items.HONEY_BOTTLE).requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.HONEY_EXTRACTOR.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SEASCRIBE_PEN", discipline).icon(ItemsPM.SEASCRIBE_PEN.get()).parent("BASIC_MAGITECH").parent("THEORYCRAFTING")
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.ENCHANTED_INK.get()).requiredResearch("t_research_projects_completed").requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.SEASCRIBE_PEN.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("ARCANOMETER", discipline).icon(ItemsPM.ARCANOMETER.get()).parent("EXPERT_MAGITECH")
            .stage(ResearchStageBuilder.stage().requiredResearch("t_items_analyzed").requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().sibling("t_sotu_research_arcanometer").recipe(ItemsPM.ARCANOMETER.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("PRIMALITE_GOLEM", discipline).icon(ItemsPM.PRIMALITE_GOLEM_CONTROLLER.get()).parent("EXPERT_MAGITECH").parent("PRIMALITE")
            .stage(ResearchStageBuilder.stage().requiredResearch("t_golem").requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.PRIMALITE_GOLEM_CONTROLLER.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("HEXIUM_GOLEM", discipline).icon(ItemsPM.HEXIUM_GOLEM_CONTROLLER.get()).parent("MASTER_MAGITECH").parent("PRIMALITE_GOLEM").parent("HEXIUM")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.HEXIUM_GOLEM_CONTROLLER.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("HALLOWSTEEL_GOLEM", discipline).icon(ItemsPM.HALLOWSTEEL_GOLEM_CONTROLLER.get()).parent("SUPREME_MAGITECH").parent("HEXIUM_GOLEM").parent("HALLOWSTEEL")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 3).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.HALLOWSTEEL_GOLEM_CONTROLLER.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("CONCOCTING_TINCTURES", discipline).icon(ItemsPM.CONCOCTER.get()).parent("EXPERT_MAGITECH").parent("SKYGLASS").parent(ResearchEntries.DISCOVER_INFERNAL)
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.SKYGLASS_FLASK.get()).recipe(ItemsPM.CONCOCTER.get()).recipe(PrimalMagick.resource("night_vision_tincture"))
                    .recipe(PrimalMagick.resource("long_night_vision_tincture")).recipe(PrimalMagick.resource("invisibility_tincture"))
                    .recipe(PrimalMagick.resource("long_invisibility_tincture")).recipe(PrimalMagick.resource("leaping_tincture"))
                    .recipe(PrimalMagick.resource("long_leaping_tincture")).recipe(PrimalMagick.resource("strong_leaping_tincture"))
                    .recipe(PrimalMagick.resource("swiftness_tincture")).recipe(PrimalMagick.resource("long_swiftness_tincture"))
                    .recipe(PrimalMagick.resource("strong_swiftness_tincture")).recipe(PrimalMagick.resource("turtle_master_tincture"))
                    .recipe(PrimalMagick.resource("long_turtle_master_tincture")).recipe(PrimalMagick.resource("strong_turtle_master_tincture"))
                    .recipe(PrimalMagick.resource("water_breathing_tincture")).recipe(PrimalMagick.resource("long_water_breathing_tincture"))
                    .recipe(PrimalMagick.resource("strength_tincture")).recipe(PrimalMagick.resource("long_strength_tincture"))
                    .recipe(PrimalMagick.resource("strong_strength_tincture")).recipe(PrimalMagick.resource("slow_falling_tincture"))
                    .recipe(PrimalMagick.resource("long_slow_falling_tincture")).recipe(PrimalMagick.resource("luck_tincture")).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(ResearchEntries.DISCOVER_BLOOD).recipe(PrimalMagick.resource("healing_tincture"))
                    .recipe(PrimalMagick.resource("strong_healing_tincture")).recipe(PrimalMagick.resource("regeneration_tincture"))
                    .recipe(PrimalMagick.resource("long_regeneration_tincture")).recipe(PrimalMagick.resource("strong_regeneration_tincture")).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(ResearchEntries.DISCOVER_INFERNAL).recipe(PrimalMagick.resource("fire_resistance_tincture"))
                    .recipe(PrimalMagick.resource("long_fire_resistance_tincture")).build())
            .build(consumer);
        ResearchEntryBuilder.entry("CONCOCTING_PHILTERS", discipline).icon(ItemsPM.CONCOCTER.get()).parent("MASTER_MAGITECH").parent("CONCOCTING_TINCTURES").parent("SHARD_SYNTHESIS")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().recipe(PrimalMagick.resource("night_vision_philter"))
                    .recipe(PrimalMagick.resource("long_night_vision_philter")).recipe(PrimalMagick.resource("invisibility_philter"))
                    .recipe(PrimalMagick.resource("long_invisibility_philter")).recipe(PrimalMagick.resource("leaping_philter"))
                    .recipe(PrimalMagick.resource("long_leaping_philter")).recipe(PrimalMagick.resource("strong_leaping_philter"))
                    .recipe(PrimalMagick.resource("swiftness_philter")).recipe(PrimalMagick.resource("long_swiftness_philter"))
                    .recipe(PrimalMagick.resource("strong_swiftness_philter")).recipe(PrimalMagick.resource("turtle_master_philter"))
                    .recipe(PrimalMagick.resource("long_turtle_master_philter")).recipe(PrimalMagick.resource("strong_turtle_master_philter"))
                    .recipe(PrimalMagick.resource("water_breathing_philter")).recipe(PrimalMagick.resource("long_water_breathing_philter"))
                    .recipe(PrimalMagick.resource("strength_philter")).recipe(PrimalMagick.resource("long_strength_philter"))
                    .recipe(PrimalMagick.resource("strong_strength_philter")).recipe(PrimalMagick.resource("slow_falling_philter"))
                    .recipe(PrimalMagick.resource("long_slow_falling_philter")).recipe(PrimalMagick.resource("luck_philter")).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(ResearchEntries.DISCOVER_BLOOD).recipe(PrimalMagick.resource("healing_philter"))
                    .recipe(PrimalMagick.resource("strong_healing_philter")).recipe(PrimalMagick.resource("regeneration_philter"))
                    .recipe(PrimalMagick.resource("long_regeneration_philter")).recipe(PrimalMagick.resource("strong_regeneration_philter")).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(ResearchEntries.DISCOVER_INFERNAL).recipe(PrimalMagick.resource("fire_resistance_philter"))
                    .recipe(PrimalMagick.resource("long_fire_resistance_philter")).build())
            .build(consumer);
        ResearchEntryBuilder.entry("CONCOCTING_ELIXIRS", discipline).icon(ItemsPM.CONCOCTER.get()).parent("SUPREME_MAGITECH").parent("CONCOCTING_PHILTERS").parent("CRYSTAL_SYNTHESIS")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 3).build())
            .stage(ResearchStageBuilder.stage().recipe(PrimalMagick.resource("night_vision_elixir"))
                    .recipe(PrimalMagick.resource("long_night_vision_elixir")).recipe(PrimalMagick.resource("invisibility_elixir"))
                    .recipe(PrimalMagick.resource("long_invisibility_elixir")).recipe(PrimalMagick.resource("leaping_elixir"))
                    .recipe(PrimalMagick.resource("long_leaping_elixir")).recipe(PrimalMagick.resource("strong_leaping_elixir"))
                    .recipe(PrimalMagick.resource("swiftness_elixir")).recipe(PrimalMagick.resource("long_swiftness_elixir"))
                    .recipe(PrimalMagick.resource("strong_swiftness_elixir")).recipe(PrimalMagick.resource("turtle_master_elixir"))
                    .recipe(PrimalMagick.resource("long_turtle_master_elixir")).recipe(PrimalMagick.resource("strong_turtle_master_elixir"))
                    .recipe(PrimalMagick.resource("water_breathing_elixir")).recipe(PrimalMagick.resource("long_water_breathing_elixir"))
                    .recipe(PrimalMagick.resource("strength_elixir")).recipe(PrimalMagick.resource("long_strength_elixir"))
                    .recipe(PrimalMagick.resource("strong_strength_elixir")).recipe(PrimalMagick.resource("slow_falling_elixir"))
                    .recipe(PrimalMagick.resource("long_slow_falling_elixir")).recipe(PrimalMagick.resource("luck_elixir")).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(ResearchEntries.DISCOVER_BLOOD).recipe(PrimalMagick.resource("healing_elixir"))
                    .recipe(PrimalMagick.resource("strong_healing_elixir")).recipe(PrimalMagick.resource("regeneration_elixir"))
                    .recipe(PrimalMagick.resource("long_regeneration_elixir")).recipe(PrimalMagick.resource("strong_regeneration_elixir")).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(ResearchEntries.DISCOVER_INFERNAL).recipe(PrimalMagick.resource("fire_resistance_elixir"))
                    .recipe(PrimalMagick.resource("long_fire_resistance_elixir")).build())
            .build(consumer);
        ResearchEntryBuilder.entry("CONCOCTING_BOMBS", discipline).icon(ItemsPM.CONCOCTER.get()).parent("MASTER_MAGITECH").parent("CONCOCTING_TINCTURES").parent("SHARD_SYNTHESIS")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.BOMB_CASING.get()).recipe(PrimalMagick.resource("night_vision_bomb"))
                    .recipe(PrimalMagick.resource("long_night_vision_bomb")).recipe(PrimalMagick.resource("invisibility_bomb"))
                    .recipe(PrimalMagick.resource("long_invisibility_bomb")).recipe(PrimalMagick.resource("leaping_bomb"))
                    .recipe(PrimalMagick.resource("long_leaping_bomb")).recipe(PrimalMagick.resource("strong_leaping_bomb"))
                    .recipe(PrimalMagick.resource("swiftness_bomb")).recipe(PrimalMagick.resource("long_swiftness_bomb"))
                    .recipe(PrimalMagick.resource("strong_swiftness_bomb")).recipe(PrimalMagick.resource("turtle_master_bomb"))
                    .recipe(PrimalMagick.resource("long_turtle_master_bomb")).recipe(PrimalMagick.resource("strong_turtle_master_bomb"))
                    .recipe(PrimalMagick.resource("water_breathing_bomb")).recipe(PrimalMagick.resource("long_water_breathing_bomb"))
                    .recipe(PrimalMagick.resource("strength_bomb")).recipe(PrimalMagick.resource("long_strength_bomb"))
                    .recipe(PrimalMagick.resource("strong_strength_bomb")).recipe(PrimalMagick.resource("slow_falling_bomb"))
                    .recipe(PrimalMagick.resource("long_slow_falling_bomb")).recipe(PrimalMagick.resource("luck_bomb")).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(ResearchEntries.DISCOVER_BLOOD).recipe(PrimalMagick.resource("healing_bomb"))
                    .recipe(PrimalMagick.resource("strong_healing_bomb")).recipe(PrimalMagick.resource("regeneration_bomb"))
                    .recipe(PrimalMagick.resource("long_regeneration_bomb")).recipe(PrimalMagick.resource("strong_regeneration_bomb"))
                    .recipe(PrimalMagick.resource("harming_bomb")).recipe(PrimalMagick.resource("strong_harming_bomb"))
                    .recipe(PrimalMagick.resource("poison_bomb")).recipe(PrimalMagick.resource("long_poison_bomb"))
                    .recipe(PrimalMagick.resource("strong_poison_bomb")).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(ResearchEntries.DISCOVER_INFERNAL).recipe(PrimalMagick.resource("fire_resistance_bomb"))
                    .recipe(PrimalMagick.resource("long_fire_resistance_bomb")).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(ResearchEntries.DISCOVER_VOID).recipe(PrimalMagick.resource("slowness_bomb"))
                    .recipe(PrimalMagick.resource("long_slowness_bomb")).recipe(PrimalMagick.resource("strong_slowness_bomb"))
                    .recipe(PrimalMagick.resource("weakness_bomb")).recipe(PrimalMagick.resource("long_weakness_bomb")).build())
            .build(consumer);
        ResearchEntryBuilder.entry("ENTROPY_SINK", discipline).icon(ItemsPM.ENTROPY_SINK.get()).parent("EXPERT_MAGITECH").parent("EXPERT_MANAWEAVING").parent("MANAFRUIT")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).requiredResearch("t_ritual_mishaps_basic").build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.ENTROPY_SINK.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("AUTO_CHARGER", discipline).icon(ItemsPM.AUTO_CHARGER.get()).parent("EXPERT_MAGITECH").parent("EXPERT_MANAWEAVING").parent("WAND_CHARGER")
            .parent("ARTIFICIAL_MANA_FONTS")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.AUTO_CHARGER.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("ESSENCE_TRANSMUTER", discipline).icon(ItemsPM.ESSENCE_TRANSMUTER.get()).parent("EXPERT_MAGITECH").parent("EXPERT_MANAWEAVING")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.ESSENCE_TRANSMUTER.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("DISSOLUTION_CHAMBER", discipline).icon(ItemsPM.DISSOLUTION_CHAMBER.get()).parent("MASTER_MAGITECH").parent("MASTER_MANAWEAVING")
            .parent("EARTHSHATTER_HAMMER")
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.IRON_GRIT.get(), 20).requiredItemStack(ItemsPM.COPPER_GRIT.get(), 20).requiredItemStack(ItemsPM.GOLD_GRIT.get(), 10)
                    .requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().attunement(Sources.EARTH, 3).recipe(ItemsPM.DISSOLUTION_CHAMBER.get()).recipe(PrimalMagick.resource("iron_grit_from_dissolving_ore"))
                    .recipe(PrimalMagick.resource("iron_grit_from_dissolving_raw_metal")).recipe(PrimalMagick.resource("gold_grit_from_dissolving_ore"))
                    .recipe(PrimalMagick.resource("gold_grit_from_dissolving_raw_metal")).recipe(PrimalMagick.resource("copper_grit_from_dissolving_ore"))
                    .recipe(PrimalMagick.resource("copper_grit_from_dissolving_raw_metal")).recipe(PrimalMagick.resource("tin_dust_from_dissolving_ore"))
                    .recipe(PrimalMagick.resource("tin_dust_from_dissolving_raw_metal")).recipe(PrimalMagick.resource("lead_dust_from_dissolving_ore"))
                    .recipe(PrimalMagick.resource("lead_dust_from_dissolving_raw_metal")).recipe(PrimalMagick.resource("silver_dust_from_dissolving_ore"))
                    .recipe(PrimalMagick.resource("silver_dust_from_dissolving_raw_metal")).recipe(PrimalMagick.resource("uranium_dust_from_dissolving_ore"))
                    .recipe(PrimalMagick.resource("uranium_dust_from_dissolving_raw_metal")).recipe(PrimalMagick.resource("cobblestone_from_dissolving_surface_stone"))
                    .recipe(PrimalMagick.resource("cobbled_deepslate_from_dissolving_deep_stone")).recipe(PrimalMagick.resource("gravel_from_dissolving_cobblestone"))
                    .recipe(PrimalMagick.resource("sand_from_dissolving_gravel")).recipe(PrimalMagick.resource("bone_meal_from_dissolving_bone"))
                    .recipe(PrimalMagick.resource("blaze_powder_from_dissolving_blaze_rod")).recipe(PrimalMagick.resource("string_from_dissolving_wool"))
                    .recipe(PrimalMagick.resource("quartz_from_dissolving_quartz_block")).recipe(PrimalMagick.resource("glowstone_dust_from_dissolving_glowstone_block"))
                    .recipe(PrimalMagick.resource("rock_salt_from_dissolving_rock_salt_ore")).recipe(PrimalMagick.resource("refined_salt_from_dissolving_rock_salt"))
                    .recipe(PrimalMagick.resource("netherite_scrap_from_dissolving_ancient_debris")).build())
            .build(consumer);
        ResearchEntryBuilder.entry("ZEPHYR_ENGINE", discipline).icon(ItemsPM.ZEPHYR_ENGINE.get()).parent("EXPERT_MAGITECH").parent("PRIMALITE").parent("SHARD_SYNTHESIS")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).requiredItemStack(Items.PISTON).build())
            .stage(ResearchStageBuilder.stage().attunement(Sources.SKY, 2).recipe(ItemsPM.ZEPHYR_ENGINE.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("VOID_TURBINE", discipline).icon(ItemsPM.VOID_TURBINE.get()).parent("MASTER_MAGITECH").parent("HEXIUM").parent("SHARD_SYNTHESIS")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).requiredItemStack(Items.STICKY_PISTON).build())
            .stage(ResearchStageBuilder.stage().attunement(Sources.VOID, 3).recipe(ItemsPM.VOID_TURBINE.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("INFERNAL_FURNACE", discipline).icon(ItemsPM.INFERNAL_FURNACE.get()).parent("MASTER_MAGITECH").parent("HEXIUM").parent("CRYSTAL_SYNTHESIS")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().attunement(Sources.INFERNAL, 3).recipe(ItemsPM.INFERNAL_FURNACE.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("IGNYX").build())
            .build(consumer);
        ResearchEntryBuilder.entry("MANA_NEXUS", discipline).icon(ItemsPM.MANA_NEXUS.get()).parent("MASTER_MAGITECH").parent("AUTO_CHARGER").parent("HEXIUM").parent("WAND_GEM_WIZARD")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.MANA_NEXUS.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("MANA_SINGULARITY", discipline).icon(ItemsPM.MANA_SINGULARITY.get()).parent("SUPREME_MAGITECH").parent("MANA_NEXUS").parent("HALLOWSTEEL").parent("WAND_GEM_ARCHMAGE")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 3).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.MANA_SINGULARITY.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WARDING_MODULE", discipline).icon(ItemsPM.BASIC_WARDING_MODULE.get()).parent("EXPERT_MAGITECH").parent("RUNE_PROTECT").parent("RUNE_SELF").parent("RUNE_INSIGHT")
            .parent("PRIMALITE").parent("WAND_CHARGER")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.BASIC_WARDING_MODULE.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("GREATER_WARDING_MODULE", discipline).icon(ItemsPM.GREATER_WARDING_MODULE.get()).parent("MASTER_MAGITECH").parent("RUNE_POWER").parent("WARDING_MODULE")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.GREATER_WARDING_MODULE.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SUPREME_WARDING_MODULE", discipline).icon(ItemsPM.SUPREME_WARDING_MODULE.get()).parent("SUPREME_MAGITECH").parent("RUNE_GRACE").parent("GREATER_WARDING_MODULE")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 3).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.SUPREME_WARDING_MODULE.get()).build())
            .build(consumer);
    }

    @Override
    public String getName() {
        return "Primal Magick Grimoire Research";
    }
}
