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
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
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
        this.registerBasicsEntries(consumer);
        this.registerManaweavingEntries(consumer);
        this.registerAlchemyEntries(consumer);
        this.registerSorceryEntries(consumer);
        this.registerRuneworkingEntries(consumer);
        this.registerRitualEntries(consumer);
        this.registerMagitechEntries(consumer);
        this.registerScanEntries(consumer);
    }

    protected void registerBasicsEntries(Consumer<IFinishedResearchEntry> consumer) {
        String discipline = "BASICS";
        ResearchEntryBuilder.entry("FIRST_STEPS", discipline).icon(ItemsPM.GRIMOIRE.get())
            .stage(ResearchStageBuilder.stage().requiredCraftStack(ItemsPM.ARCANE_WORKBENCH.get()).recipe(ItemsPM.MUNDANE_WAND.get()).build())
            .stage(ResearchStageBuilder.stage().requiredResearch("t_mana_siphoned_basics").recipe(ItemsPM.MUNDANE_WAND.get()).build())
            .stage(ResearchStageBuilder.stage().requiredResearch("t_observations_made_basics").recipe(ItemsPM.MUNDANE_WAND.get()).recipe(ItemsPM.WOOD_TABLE.get())
                    .recipe(ItemsPM.MAGNIFYING_GLASS.get()).recipe(ItemsPM.ANALYSIS_TABLE.get()).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.MUNDANE_WAND.get()).recipe(ItemsPM.WOOD_TABLE.get()).recipe(ItemsPM.MAGNIFYING_GLASS.get()).recipe(ItemsPM.ANALYSIS_TABLE.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("THEORYCRAFTING", discipline).icon("textures/research/knowledge_theory.png").parent("FIRST_STEPS")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().requiredCraftStack(ItemsPM.RESEARCH_TABLE.get()).requiredCraftStack(ItemsPM.ENCHANTED_INK_AND_QUILL.get()).recipe(ItemsPM.RESEARCH_TABLE.get())
                    .recipe(ItemsPM.ENCHANTED_INK.get()).recipe(ItemsPM.ENCHANTED_INK_AND_QUILL.get()).build())
            .stage(ResearchStageBuilder.stage().requiredResearch("t_theories_formed_basics").recipe(ItemsPM.RESEARCH_TABLE.get()).recipe(ItemsPM.ENCHANTED_INK.get())
                    .recipe(ItemsPM.ENCHANTED_INK_AND_QUILL.get()).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.RESEARCH_TABLE.get()).recipe(ItemsPM.ENCHANTED_INK.get()).recipe(ItemsPM.ENCHANTED_INK_AND_QUILL.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("ATTUNEMENTS", discipline).parent("FIRST_STEPS")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("UNLOCK_MANAWEAVING", discipline).icon("textures/research/discipline_manaweaving.png").parent("FIRST_STEPS")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("UNLOCK_ALCHEMY", discipline).icon("textures/research/discipline_alchemy.png").parent("MANA_ARROWS")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("UNLOCK_SORCERY", discipline).icon("textures/research/discipline_sorcery.png").parent("WAND_CHARGER")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("UNLOCK_RUNEWORKING", discipline).icon("textures/research/discipline_runeworking.png").parent("CALCINATOR_BASIC")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("UNLOCK_RITUAL", discipline).icon("textures/research/discipline_ritual.png").parent("WAND_INSCRIPTION").parent("RUNE_PROJECT")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("UNLOCK_MAGITECH", discipline).icon("textures/research/discipline_magitech.png").parent("MANAFRUIT").parent("MANA_SALTS")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("TERRESTRIAL_MAGICK", discipline).parent("ATTUNEMENTS")
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("SOURCE_EARTH", discipline).icon(Source.EARTH.getImage()).parent("TERRESTRIAL_MAGICK")
            .stage(ResearchStageBuilder.stage()
                    .requiredItemTag(Tags.Items.OBSIDIAN).requiredItemTag(Tags.Items.GEMS_DIAMOND)
                    .requiredKnowledge(KnowledgeType.OBSERVATION, 1)
                    .requiredResearch("m_found_shrine_earth").requiredResearch("m_env_earth").requiredResearch("t_mana_spent_earth_expert", true)
                    .build())
            .stage(ResearchStageBuilder.stage().attunement(Source.EARTH, 5).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SOURCE_SEA", discipline).icon(Source.SEA.getImage()).parent("TERRESTRIAL_MAGICK")
            .stage(ResearchStageBuilder.stage()
                    .requiredItemTag(ItemTagsPM.CORAL_BLOCKS).requiredItemStack(Items.ICE)
                    .requiredKnowledge(KnowledgeType.OBSERVATION, 1)
                    .requiredResearch("m_found_shrine_sea").requiredResearch("m_env_sea").requiredResearch("t_mana_spent_sea_expert", true)
                    .build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SEA, 5).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SOURCE_SKY", discipline).icon(Source.SKY.getImage()).parent("TERRESTRIAL_MAGICK")
            .stage(ResearchStageBuilder.stage()
                    .requiredItemStack(Items.BAMBOO).requiredItemTag(ItemTags.LEAVES)
                    .requiredKnowledge(KnowledgeType.OBSERVATION, 1)
                    .requiredResearch("m_found_shrine_sky").requiredResearch("m_env_sky").requiredResearch("t_mana_spent_sky_expert", true)
                    .build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SKY, 5).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SOURCE_SUN", discipline).icon(Source.SUN.getImage()).parent("TERRESTRIAL_MAGICK")
            .stage(ResearchStageBuilder.stage()
                    .requiredItemTag(ItemTagsPM.SUNWOOD_LOGS).requiredItemTag(Tags.Items.SANDSTONE)
                    .requiredKnowledge(KnowledgeType.OBSERVATION, 1)
                    .requiredResearch("m_found_shrine_sun").requiredResearch("m_env_sun").requiredResearch("t_mana_spent_sun_expert", true)
                    .build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SUN, 5).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SOURCE_MOON", discipline).icon(Source.MOON.getImage()).parent("TERRESTRIAL_MAGICK")
            .stage(ResearchStageBuilder.stage()
                    .requiredItemTag(ItemTagsPM.MOONWOOD_LOGS).requiredItemTag(Tags.Items.MUSHROOMS)
                    .requiredKnowledge(KnowledgeType.OBSERVATION, 1)
                    .requiredResearch("m_found_shrine_moon").requiredResearch("m_env_moon").requiredResearch("t_mana_spent_moon_expert", true)
                    .build())
            .stage(ResearchStageBuilder.stage().attunement(Source.MOON, 5).build())
            .build(consumer);
        ResearchEntryBuilder.entry("FORBIDDEN_MAGICK", discipline).parent("TERRESTRIAL_MAGICK").parent("t_discover_forbidden")
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("SOURCE_BLOOD", discipline).icon(Source.BLOOD.getImage()).parent("FORBIDDEN_MAGICK").parent(Source.BLOOD.getDiscoverKey())
            .stage(ResearchStageBuilder.stage()
                    .requiredItemTag(Tags.Items.BONES).requiredItemStack(ItemsPM.BLOODY_FLESH.get())
                    .requiredKnowledge(KnowledgeType.OBSERVATION, 1)
                    .requiredResearch("t_mana_spent_blood_expert", true)
                    .build())
            .stage(ResearchStageBuilder.stage().attunement(Source.BLOOD, 5).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SOURCE_INFERNAL", discipline).icon(Source.INFERNAL.getImage()).parent("FORBIDDEN_MAGICK").parent(Source.INFERNAL.getDiscoverKey())
            .stage(ResearchStageBuilder.stage()
                    .requiredItemTag(Tags.Items.RODS_BLAZE).requiredItemStack(Items.SOUL_SAND)
                    .requiredKnowledge(KnowledgeType.OBSERVATION, 1)
                    .requiredResearch("t_mana_spent_infernal_expert", true)
                    .build())
            .stage(ResearchStageBuilder.stage().attunement(Source.INFERNAL, 5).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SOURCE_VOID", discipline).icon(Source.VOID.getImage()).parent("FORBIDDEN_MAGICK").parent(Source.VOID.getDiscoverKey())
            .stage(ResearchStageBuilder.stage()
                    .requiredItemTag(Tags.Items.END_STONES).requiredItemTag(Tags.Items.ENDER_PEARLS)
                    .requiredKnowledge(KnowledgeType.OBSERVATION, 1)
                    .requiredResearch("t_mana_spent_void_expert", true)
                    .build())
            .stage(ResearchStageBuilder.stage().attunement(Source.VOID, 5).build())
            .build(consumer);
        ResearchEntryBuilder.entry("HEAVENLY_MAGICK", discipline).parent("FORBIDDEN_MAGICK").parent(Source.HALLOWED.getDiscoverKey())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("SOURCE_HALLOWED", discipline).icon(Source.HALLOWED.getImage()).parent("HEAVENLY_MAGICK")
            .stage(ResearchStageBuilder.stage()
                    .requiredItemTag(Tags.Items.NETHER_STARS)
                    .requiredKnowledge(KnowledgeType.OBSERVATION, 1)
                    .requiredResearch("t_mana_spent_hallowed_expert", true)
                    .build())
            .stage(ResearchStageBuilder.stage().attunement(Source.HALLOWED, 5).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SECRETS_OF_THE_UNIVERSE", discipline).hidden().icon(Source.UNKNOWN_IMAGE)
            .stage(ResearchStageBuilder.stage().requiredResearch("m_sotu_discover_blood", true).requiredResearch("m_sotu_discover_infernal", true).requiredResearch("m_sotu_discover_void", true)
                    .requiredResearch("t_sotu_research_arcanometer", true).requiredResearch("t_sotu_research_hexium", true).requiredResearch("t_sotu_research_power_rune", true)
                    .requiredResearch("t_sotu_research_sanguine_crucible", true).requiredResearch("t_sotu_research_cleansing_rite", true).requiredResearch("b_sotu_scan_hallowed_orb", true).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.HALLOWED, 4).build())
            .build(consumer);
        ResearchEntryBuilder.entry("COMPLETE_BASICS", discipline).hidden().icon(ItemsPM.GRIMOIRE.get()).finale(discipline)
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.EARTH, 1).attunement(Source.SEA, 1).attunement(Source.SKY, 1).attunement(Source.SUN, 1).attunement(Source.MOON, 1)
                    .attunement(Source.BLOOD, 1).attunement(Source.INFERNAL, 1).attunement(Source.VOID, 1).attunement(Source.HALLOWED, 1).build())
            .build(consumer);
        ResearchEntryBuilder.entry("THEORY_OF_EVERYTHING", discipline).hidden().icon(ItemsPM.GRIMOIRE.get()).finale("BASICS").finale("ALCHEMY").finale("MAGITECH").finale("MANAWEAVING")
            .finale("RITUAL").finale("RUNEWORKING").finale("SORCERY")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.EARTH, 2).attunement(Source.SEA, 2).attunement(Source.SKY, 2).attunement(Source.SUN, 2).attunement(Source.MOON, 2)
                    .attunement(Source.BLOOD, 2).attunement(Source.INFERNAL, 2).attunement(Source.VOID, 2).attunement(Source.HALLOWED, 2).build())
            .build(consumer);
    }
    
    protected void registerManaweavingEntries(Consumer<IFinishedResearchEntry> consumer) {
        String discipline = "MANAWEAVING";
        ResearchEntryBuilder.entry("BASIC_MANAWEAVING", discipline).icon("textures/research/discipline_manaweaving.png").parent("UNLOCK_MANAWEAVING")
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.MANA_PRISM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("EXPERT_MANAWEAVING", discipline).icon("textures/research/discipline_manaweaving.png").parent("MANA_ARROWS").parent("WAND_CHARGER")
            .stage(ResearchStageBuilder.stage().requiredResearch("b_crafted_manaweaving_expert").build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.MARBLE_ENCHANTED.get()).recipe(ItemsPM.MARBLE_ENCHANTED_BRICK_SLAB.get()).recipe(ItemsPM.MARBLE_ENCHANTED_BRICK_STAIRS.get())
                    .recipe(ItemsPM.MARBLE_ENCHANTED_BRICK_WALL.get()).recipe(ItemsPM.MARBLE_ENCHANTED_BRICKS.get()).recipe(ItemsPM.MARBLE_ENCHANTED_CHISELED.get())
                    .recipe(ItemsPM.MARBLE_ENCHANTED_PILLAR.get()).recipe(ItemsPM.MARBLE_ENCHANTED_RUNED.get()).recipe(ItemsPM.MARBLE_ENCHANTED_SLAB.get())
                    .recipe(ItemsPM.MARBLE_ENCHANTED_STAIRS.get()).recipe(ItemsPM.MARBLE_ENCHANTED_WALL.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("MASTER_MANAWEAVING", discipline).icon("textures/research/discipline_manaweaving.png").parent("WAND_CAP_GOLD").parent("WAND_GEM_ADEPT")
            .stage(ResearchStageBuilder.stage().requiredResearch("t_discover_forbidden").requiredResearch("b_crafted_manaweaving_master").build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.MARBLE_SMOKED.get()).recipe(ItemsPM.MARBLE_SMOKED_BRICK_SLAB.get()).recipe(ItemsPM.MARBLE_SMOKED_BRICK_STAIRS.get())
                    .recipe(ItemsPM.MARBLE_SMOKED_BRICK_WALL.get()).recipe(ItemsPM.MARBLE_SMOKED_BRICKS.get()).recipe(ItemsPM.MARBLE_SMOKED_CHISELED.get())
                    .recipe(ItemsPM.MARBLE_SMOKED_PILLAR.get()).recipe(ItemsPM.MARBLE_SMOKED_RUNED.get()).recipe(ItemsPM.MARBLE_SMOKED_SLAB.get())
                    .recipe(ItemsPM.MARBLE_SMOKED_STAIRS.get()).recipe(ItemsPM.MARBLE_SMOKED_WALL.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SUPREME_MANAWEAVING", discipline).icon("textures/research/discipline_manaweaving.png").parent("WAND_CAP_HEXIUM").parent("WAND_GEM_WIZARD")
            .stage(ResearchStageBuilder.stage().requiredResearch(Source.HALLOWED.getDiscoverKey()).requiredResearch("b_crafted_manaweaving_supreme").reveals("SECRETS_OF_THE_UNIVERSE").build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.MARBLE_HALLOWED.get()).recipe(ItemsPM.MARBLE_HALLOWED_BRICK_SLAB.get()).recipe(ItemsPM.MARBLE_HALLOWED_BRICK_STAIRS.get())
                    .recipe(ItemsPM.MARBLE_HALLOWED_BRICK_WALL.get()).recipe(ItemsPM.MARBLE_HALLOWED_BRICKS.get()).recipe(ItemsPM.MARBLE_HALLOWED_CHISELED.get())
                    .recipe(ItemsPM.MARBLE_HALLOWED_PILLAR.get()).recipe(ItemsPM.MARBLE_HALLOWED_RUNED.get()).recipe(ItemsPM.MARBLE_HALLOWED_SLAB.get())
                    .recipe(ItemsPM.MARBLE_HALLOWED_STAIRS.get()).recipe(ItemsPM.MARBLE_HALLOWED_WALL.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("COMPLETE_MANAWEAVING", discipline).hidden().icon("textures/research/discipline_manaweaving.png").finale(discipline)
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.EARTH, 1).attunement(Source.SEA, 1).attunement(Source.SKY, 1).attunement(Source.SUN, 1).attunement(Source.MOON, 1)
                    .attunement(Source.BLOOD, 1).attunement(Source.INFERNAL, 1).attunement(Source.VOID, 1).attunement(Source.HALLOWED, 1).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_CHARGER", discipline).icon(ItemsPM.WAND_CHARGER.get()).parent("BASIC_MANAWEAVING")
            .stage(ResearchStageBuilder.stage().requiredItemTag(ItemTagsPM.ESSENCES_TERRESTRIAL_DUSTS).requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.WAND_CHARGER.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("MANA_SALTS", discipline).icon(ItemsPM.MANA_SALTS.get()).parent("WAND_CHARGER")
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.ESSENCE_DUST_EARTH.get()).requiredItemStack(ItemsPM.ESSENCE_DUST_SEA.get()).requiredItemStack(ItemsPM.ESSENCE_DUST_SKY.get())
                    .requiredItemStack(ItemsPM.ESSENCE_DUST_SUN.get()).requiredItemStack(ItemsPM.ESSENCE_DUST_MOON.get()).requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.MANA_SALTS.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("ADVANCED_WANDMAKING", discipline).parent("WAND_CHARGER").parent("t_mana_spent_total_basics")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.WAND_ASSEMBLY_TABLE.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("STAVES", discipline).parent("EXPERT_MANAWEAVING").parent("WAND_GEM_ADEPT").parent("WAND_INSCRIPTION").parent("SHARD_SYNTHESIS")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_CORE_HEARTWOOD", discipline).icon(ItemsPM.HEARTWOOD_WAND_CORE_ITEM.get()).parent("ADVANCED_WANDMAKING")
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.HEARTWOOD.get()).requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.HEARTWOOD_WAND_CORE_ITEM.get()).recipe(PrimalMagick.resource("charcoal_from_smelting_heartwood")).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("STAVES").recipe(ItemsPM.HEARTWOOD_STAFF_CORE_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_CAP_IRON", discipline).icon(ItemsPM.IRON_WAND_CAP_ITEM.get()).parent("ADVANCED_WANDMAKING")
            .stage(ResearchStageBuilder.stage().requiredItemTag(Tags.Items.INGOTS_IRON).requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.IRON_WAND_CAP_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_GEM_APPRENTICE", discipline).icon(ItemsPM.APPRENTICE_WAND_GEM_ITEM.get()).parent("ADVANCED_WANDMAKING")
            .stage(ResearchStageBuilder.stage().requiredItemTag(Tags.Items.GEMS_DIAMOND).requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.APPRENTICE_WAND_GEM_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("EARTHSHATTER_HAMMER", discipline).icon(ItemsPM.EARTHSHATTER_HAMMER.get()).parent("EXPERT_MANAWEAVING").parent("SHARD_SYNTHESIS")
            .stage(ResearchStageBuilder.stage().requiredItemStack(Items.RAW_IRON).requiredItemStack(Items.RAW_GOLD).requiredItemStack(Items.RAW_COPPER).requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.EARTH, 2).recipe(ItemsPM.EARTHSHATTER_HAMMER.get()).recipe(PrimalMagick.resource("iron_grit_from_ore"))
                    .recipe(PrimalMagick.resource("iron_grit_from_raw_metal")).recipe(PrimalMagick.resource("gold_grit_from_ore"))
                    .recipe(PrimalMagick.resource("gold_grit_from_raw_metal")).recipe(PrimalMagick.resource("copper_grit_from_ore"))
                    .recipe(PrimalMagick.resource("copper_grit_from_raw_metal")).recipe(PrimalMagick.resource("iron_ingot_from_grit_smelting"))
                    .recipe(PrimalMagick.resource("gold_ingot_from_grit_smelting")).recipe(PrimalMagick.resource("copper_ingot_from_grit_smelting"))
                    .recipe(PrimalMagick.resource("cobblestone_from_earthshatter_hammer")).recipe(PrimalMagick.resource("cobbled_deepslate_from_earthshatter_hammer"))
                    .recipe(PrimalMagick.resource("gravel_from_earthshatter_hammer")).recipe(PrimalMagick.resource("sand_from_earthshatter_hammer"))
                    .recipe(PrimalMagick.resource("rock_salt_from_earthshatter_hammer")).recipe(PrimalMagick.resource("refined_salt_from_earthshatter_hammer"))
                    .recipe(PrimalMagick.resource("netherite_scrap_from_earthshatter_hammer")).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SUNLAMP", discipline).icon(ItemsPM.SUNLAMP.get()).parent("EXPERT_MANAWEAVING").parent("PRIMALITE")
            .stage(ResearchStageBuilder.stage().requiredItemStack(Items.LANTERN).requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SUN, 2).recipe(ItemsPM.SUNLAMP.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("HEXIUM").attunement(Source.INFERNAL, 2).recipe(ItemsPM.SPIRIT_LANTERN.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_GEM_ADEPT", discipline).icon(ItemsPM.ADEPT_WAND_GEM_ITEM.get()).parent("EXPERT_MANAWEAVING").parent("WAND_GEM_APPRENTICE").parent("SHARD_SYNTHESIS")
            .stage(ResearchStageBuilder.stage().requiredItemTag(Tags.Items.GEMS_DIAMOND).requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.ADEPT_WAND_GEM_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_GEM_WIZARD", discipline).icon(ItemsPM.WIZARD_WAND_GEM_ITEM.get()).parent("MASTER_MANAWEAVING").parent("WAND_GEM_ADEPT").parent("CRYSTAL_SYNTHESIS")
            .stage(ResearchStageBuilder.stage().requiredItemTag(Tags.Items.GEMS_DIAMOND).requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.WIZARD_WAND_GEM_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_GEM_ARCHMAGE", discipline).icon(ItemsPM.ARCHMAGE_WAND_GEM_ITEM.get()).parent("SUPREME_MANAWEAVING").parent("WAND_GEM_WIZARD").parent("CLUSTER_SYNTHESIS")
            .stage(ResearchStageBuilder.stage().requiredItemTag(Tags.Items.GEMS_DIAMOND).requiredKnowledge(KnowledgeType.THEORY, 3).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.ARCHMAGE_WAND_GEM_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_CAP_GOLD", discipline).icon(ItemsPM.GOLD_WAND_CAP_ITEM.get()).parent("EXPERT_MANAWEAVING").parent("WAND_CAP_IRON")
            .stage(ResearchStageBuilder.stage().requiredItemTag(Tags.Items.INGOTS_GOLD).requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.GOLD_WAND_CAP_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_CAP_PRIMALITE", discipline).icon(ItemsPM.PRIMALITE_WAND_CAP_ITEM.get()).parent("WAND_CAP_GOLD").parent("PRIMALITE")
            .stage(ResearchStageBuilder.stage().requiredItemTag(ItemTagsPM.INGOTS_PRIMALITE).requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.PRIMALITE_WAND_CAP_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_CAP_HEXIUM", discipline).icon(ItemsPM.HEXIUM_WAND_CAP_ITEM.get()).parent("MASTER_MANAWEAVING").parent("WAND_CAP_PRIMALITE").parent("HEXIUM")
            .stage(ResearchStageBuilder.stage().requiredItemTag(ItemTagsPM.INGOTS_HEXIUM).requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.HEXIUM_WAND_CAP_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_CAP_HALLOWSTEEL", discipline).icon(ItemsPM.HALLOWSTEEL_WAND_CAP_ITEM.get()).parent("SUPREME_MANAWEAVING").parent("WAND_CAP_HEXIUM").parent("HALLOWSTEEL")
            .stage(ResearchStageBuilder.stage().requiredItemTag(ItemTagsPM.INGOTS_HALLOWSTEEL).requiredKnowledge(KnowledgeType.THEORY, 3).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.HALLOWSTEEL_WAND_CAP_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_CORE_OBSIDIAN", discipline).icon(ItemsPM.OBSIDIAN_WAND_CORE_ITEM.get()).parent("EXPERT_MANAWEAVING").parent("WAND_CORE_HEARTWOOD")
            .stage(ResearchStageBuilder.stage().requiredItemTag(Tags.Items.OBSIDIAN).requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.EARTH, 2).recipe(ItemsPM.OBSIDIAN_WAND_CORE_ITEM.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("STAVES").recipe(ItemsPM.OBSIDIAN_STAFF_CORE_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_CORE_CORAL", discipline).icon(ItemsPM.CORAL_WAND_CORE_ITEM.get()).parent("EXPERT_MANAWEAVING").parent("WAND_CORE_OBSIDIAN")
            .stage(ResearchStageBuilder.stage().requiredItemTag(ItemTagsPM.CORAL_BLOCKS).requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SEA, 2).recipe(ItemsPM.CORAL_WAND_CORE_ITEM.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("STAVES").recipe(ItemsPM.CORAL_STAFF_CORE_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_CORE_BAMBOO", discipline).icon(ItemsPM.BAMBOO_WAND_CORE_ITEM.get()).parent("EXPERT_MANAWEAVING").parent("WAND_CORE_OBSIDIAN")
            .stage(ResearchStageBuilder.stage().requiredItemStack(Items.BAMBOO).requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SKY, 2).recipe(ItemsPM.BAMBOO_WAND_CORE_ITEM.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("STAVES").recipe(ItemsPM.BAMBOO_STAFF_CORE_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_CORE_SUNWOOD", discipline).icon(ItemsPM.SUNWOOD_WAND_CORE_ITEM.get()).parent("EXPERT_MANAWEAVING").parent("WAND_CORE_OBSIDIAN")
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.SUNWOOD_LOG.get()).requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SUN, 2).recipe(ItemsPM.SUNWOOD_WAND_CORE_ITEM.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("STAVES").recipe(ItemsPM.SUNWOOD_STAFF_CORE_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_CORE_MOONWOOD", discipline).icon(ItemsPM.MOONWOOD_WAND_CORE_ITEM.get()).parent("EXPERT_MANAWEAVING").parent("WAND_CORE_OBSIDIAN")
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.MOONWOOD_LOG.get()).requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.MOON, 2).recipe(ItemsPM.MOONWOOD_WAND_CORE_ITEM.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("STAVES").recipe(ItemsPM.MOONWOOD_STAFF_CORE_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_CORE_BONE", discipline).icon(ItemsPM.BONE_WAND_CORE_ITEM.get()).parent("MASTER_MANAWEAVING").parent("WAND_CORE_HEARTWOOD")
            .parent(Source.BLOOD.getDiscoverKey())
            .stage(ResearchStageBuilder.stage().requiredItemStack(Items.BONE).requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.BLOOD, 3).recipe(ItemsPM.BONE_WAND_CORE_ITEM.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("STAVES").recipe(ItemsPM.BONE_STAFF_CORE_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_CORE_BLAZE_ROD", discipline).icon(ItemsPM.BLAZE_ROD_WAND_CORE_ITEM.get()).parent("MASTER_MANAWEAVING").parent("WAND_CORE_HEARTWOOD")
            .parent(Source.INFERNAL.getDiscoverKey())
            .stage(ResearchStageBuilder.stage().requiredItemTag(Tags.Items.RODS_BLAZE).requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.INFERNAL, 3).recipe(ItemsPM.BLAZE_ROD_WAND_CORE_ITEM.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("STAVES").recipe(ItemsPM.BLAZE_ROD_STAFF_CORE_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_CORE_PURPUR", discipline).icon(ItemsPM.PURPUR_WAND_CORE_ITEM.get()).parent("MASTER_MANAWEAVING").parent("WAND_CORE_HEARTWOOD")
            .parent(Source.VOID.getDiscoverKey())
            .stage(ResearchStageBuilder.stage().requiredItemStack(Items.PURPUR_BLOCK).requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.VOID, 3).recipe(ItemsPM.PURPUR_WAND_CORE_ITEM.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("STAVES").recipe(ItemsPM.PURPUR_STAFF_CORE_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("IMBUED_WOOL", discipline).icon(ItemsPM.IMBUED_WOOL_HEAD.get()).parent("MANA_ARROWS")
            .stage(ResearchStageBuilder.stage().requiredItemTag(ItemTags.WOOL).requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.IMBUED_WOOL_HEAD.get()).recipe(ItemsPM.IMBUED_WOOL_CHEST.get()).recipe(ItemsPM.IMBUED_WOOL_LEGS.get())
                    .recipe(ItemsPM.IMBUED_WOOL_FEET.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELLCLOTH", discipline).icon(ItemsPM.SPELLCLOTH_HEAD.get()).parent("EXPERT_MANAWEAVING").parent("IMBUED_WOOL").parent("EARTHSHATTER_HAMMER")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.SPELLCLOTH.get()).recipe(ItemsPM.SPELLCLOTH_HEAD.get()).recipe(ItemsPM.SPELLCLOTH_CHEST.get())
                    .recipe(ItemsPM.SPELLCLOTH_LEGS.get()).recipe(ItemsPM.SPELLCLOTH_FEET.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("HEXWEAVE", discipline).icon(ItemsPM.HEXWEAVE_HEAD.get()).parent("MASTER_MANAWEAVING").parent("SPELLCLOTH").parent("SHARD_SYNTHESIS")
            .parent(Source.BLOOD.getDiscoverKey()).parent(Source.INFERNAL.getDiscoverKey()).parent(Source.VOID.getDiscoverKey())
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.HEXWEAVE.get()).recipe(ItemsPM.HEXWEAVE_HEAD.get()).recipe(ItemsPM.HEXWEAVE_CHEST.get())
                    .recipe(ItemsPM.HEXWEAVE_LEGS.get()).recipe(ItemsPM.HEXWEAVE_FEET.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SAINTSWOOL", discipline).icon(ItemsPM.SAINTSWOOL_HEAD.get()).parent("SUPREME_MANAWEAVING").parent("HEXWEAVE").parent("CRYSTAL_SYNTHESIS")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 3).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.SAINTSWOOL.get()).recipe(ItemsPM.SAINTSWOOL_HEAD.get()).recipe(ItemsPM.SAINTSWOOL_CHEST.get())
                    .recipe(ItemsPM.SAINTSWOOL_LEGS.get()).recipe(ItemsPM.SAINTSWOOL_FEET.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("ARTIFICIAL_MANA_FONTS", discipline).icon(ItemsPM.ARTIFICIAL_FONT_EARTH.get()).parent("EXPERT_MANAWEAVING").parent("SHARD_SYNTHESIS")
            .parent("PRIMALITE").parent("SUNLAMP")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).requiredResearch("t_mana_siphoned_expert").build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.ARTIFICIAL_FONT_EARTH.get()).recipe(ItemsPM.ARTIFICIAL_FONT_SEA.get()).recipe(ItemsPM.ARTIFICIAL_FONT_SKY.get())
                    .recipe(ItemsPM.ARTIFICIAL_FONT_SUN.get()).recipe(ItemsPM.ARTIFICIAL_FONT_MOON.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.BLOOD.getDiscoverKey()).recipe(ItemsPM.ARTIFICIAL_FONT_BLOOD.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.INFERNAL.getDiscoverKey()).recipe(ItemsPM.ARTIFICIAL_FONT_INFERNAL.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.VOID.getDiscoverKey()).recipe(ItemsPM.ARTIFICIAL_FONT_VOID.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.HALLOWED.getDiscoverKey()).recipe(ItemsPM.ARTIFICIAL_FONT_HALLOWED.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("FORBIDDEN_MANA_FONTS", discipline).icon(ItemsPM.FORBIDDEN_FONT_EARTH.get()).parent("ARTIFICIAL_MANA_FONTS").parent("MASTER_MANAWEAVING")
            .parent("CRYSTAL_SYNTHESIS").parent("HEXIUM")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.FORBIDDEN_FONT_EARTH.get()).recipe(ItemsPM.FORBIDDEN_FONT_SEA.get()).recipe(ItemsPM.FORBIDDEN_FONT_SKY.get())
                    .recipe(ItemsPM.FORBIDDEN_FONT_SUN.get()).recipe(ItemsPM.FORBIDDEN_FONT_MOON.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.BLOOD.getDiscoverKey()).recipe(ItemsPM.FORBIDDEN_FONT_BLOOD.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.INFERNAL.getDiscoverKey()).recipe(ItemsPM.FORBIDDEN_FONT_INFERNAL.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.VOID.getDiscoverKey()).recipe(ItemsPM.FORBIDDEN_FONT_VOID.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.HALLOWED.getDiscoverKey()).recipe(ItemsPM.FORBIDDEN_FONT_HALLOWED.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("HEAVENLY_MANA_FONTS", discipline).icon(ItemsPM.HEAVENLY_FONT_EARTH.get()).parent("FORBIDDEN_MANA_FONTS").parent("SUPREME_MANAWEAVING")
            .parent("CLUSTER_SYNTHESIS").parent("HALLOWSTEEL")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 3).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.HEAVENLY_FONT_EARTH.get()).recipe(ItemsPM.HEAVENLY_FONT_SEA.get()).recipe(ItemsPM.HEAVENLY_FONT_SKY.get())
                    .recipe(ItemsPM.HEAVENLY_FONT_SUN.get()).recipe(ItemsPM.HEAVENLY_FONT_MOON.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.BLOOD.getDiscoverKey()).recipe(ItemsPM.HEAVENLY_FONT_BLOOD.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.INFERNAL.getDiscoverKey()).recipe(ItemsPM.HEAVENLY_FONT_INFERNAL.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.VOID.getDiscoverKey()).recipe(ItemsPM.HEAVENLY_FONT_VOID.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.HALLOWED.getDiscoverKey()).recipe(ItemsPM.HEAVENLY_FONT_HALLOWED.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("MANA_ARROWS", discipline).icon(ItemsPM.MANA_ARROW_EARTH.get()).parent("BASIC_MANAWEAVING")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).requiredCraftStack(Items.ARROW).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.MANA_ARROW_EARTH.get()).recipe(ItemsPM.MANA_ARROW_SEA.get()).recipe(ItemsPM.MANA_ARROW_SKY.get()).recipe(ItemsPM.MANA_ARROW_SUN.get())
                    .recipe(ItemsPM.MANA_ARROW_MOON.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.BLOOD.getDiscoverKey()).recipe(ItemsPM.MANA_ARROW_BLOOD.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.INFERNAL.getDiscoverKey()).recipe(ItemsPM.MANA_ARROW_INFERNAL.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.VOID.getDiscoverKey()).recipe(ItemsPM.MANA_ARROW_VOID.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.HALLOWED.getDiscoverKey()).recipe(ItemsPM.MANA_ARROW_HALLOWED.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("ESSENCE_CASK_ENCHANTED", discipline).icon(ItemsPM.ESSENCE_CASK_ENCHANTED.get()).parent("EXPERT_MANAWEAVING").parent("WAND_CORE_HEARTWOOD")
            .parent("SHARD_SYNTHESIS").parent("PRIMALITE")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.ESSENCE_CASK_ENCHANTED.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("ESSENCE_CASK_FORBIDDEN", discipline).icon(ItemsPM.ESSENCE_CASK_FORBIDDEN.get()).parent("MASTER_MANAWEAVING").parent("ESSENCE_CASK_ENCHANTED")
            .parent("CRYSTAL_SYNTHESIS").parent("HEXIUM")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.ESSENCE_CASK_FORBIDDEN.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("ESSENCE_CASK_HEAVENLY", discipline).icon(ItemsPM.ESSENCE_CASK_HEAVENLY.get()).parent("SUPREME_MANAWEAVING").parent("ESSENCE_CASK_FORBIDDEN")
            .parent("CLUSTER_SYNTHESIS").parent("HALLOWSTEEL")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 3).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.ESSENCE_CASK_HEAVENLY.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_GLAMOUR_TABLE", discipline).icon(ItemsPM.WAND_GLAMOUR_TABLE.get()).parent("EXPERT_MANAWEAVING").parent("WAND_CORE_HEARTWOOD")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.MOON, 2).recipe(ItemsPM.WAND_GLAMOUR_TABLE.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("ATTUNEMENT_SHACKLES", discipline).icon(ItemsPM.ATTUNEMENT_SHACKLES_EARTH.get()).parent("EXPERT_MANAWEAVING").parent("SHARD_SYNTHESIS")
            .parent("ATTUNEMENTS")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.ATTUNEMENT_SHACKLES_EARTH.get()).recipe(ItemsPM.ATTUNEMENT_SHACKLES_SEA.get())
                    .recipe(ItemsPM.ATTUNEMENT_SHACKLES_SKY.get()).recipe(ItemsPM.ATTUNEMENT_SHACKLES_SUN.get()).recipe(ItemsPM.ATTUNEMENT_SHACKLES_MOON.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.BLOOD.getDiscoverKey()).recipe(ItemsPM.ATTUNEMENT_SHACKLES_BLOOD.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.INFERNAL.getDiscoverKey()).recipe(ItemsPM.ATTUNEMENT_SHACKLES_INFERNAL.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.VOID.getDiscoverKey()).recipe(ItemsPM.ATTUNEMENT_SHACKLES_VOID.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.HALLOWED.getDiscoverKey()).recipe(ItemsPM.ATTUNEMENT_SHACKLES_HALLOWED.get()).build())
            .build(consumer);
    }
    
    protected void registerAlchemyEntries(Consumer<IFinishedResearchEntry> consumer) {
        String discipline = "ALCHEMY";
        ResearchEntryBuilder.entry("BASIC_ALCHEMY", discipline).icon("textures/research/discipline_alchemy.png").parent("UNLOCK_ALCHEMY")
            .stage(ResearchStageBuilder.stage().requiredCraftStack(ItemsPM.ESSENCE_FURNACE.get()).build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("EXPERT_ALCHEMY", discipline).icon("textures/research/discipline_alchemy.png").parent("CALCINATOR_BASIC").parent("STONEMELDING")
            .stage(ResearchStageBuilder.stage().requiredResearch("b_crafted_alchemy_expert").build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("MASTER_ALCHEMY", discipline).icon("textures/research/discipline_alchemy.png").parent("CALCINATOR_ENCHANTED").parent("PRIMALITE")
            .stage(ResearchStageBuilder.stage().requiredResearch("t_discover_forbidden").requiredResearch("b_crafted_alchemy_master").build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("SUPREME_ALCHEMY", discipline).icon("textures/research/discipline_alchemy.png").parent("CALCINATOR_FORBIDDEN").parent("HEXIUM")
            .stage(ResearchStageBuilder.stage().requiredResearch(Source.HALLOWED.getDiscoverKey()).requiredResearch("b_crafted_alchemy_supreme").reveals("SECRETS_OF_THE_UNIVERSE").build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("COMPLETE_ALCHEMY", discipline).hidden().icon("textures/research/discipline_alchemy.png").finale(discipline)
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.EARTH, 1).attunement(Source.SEA, 1).attunement(Source.SKY, 1).attunement(Source.SUN, 1).attunement(Source.MOON, 1)
                    .attunement(Source.BLOOD, 1).attunement(Source.INFERNAL, 1).attunement(Source.VOID, 1).attunement(Source.HALLOWED, 1).build())
            .build(consumer);
        ResearchEntryBuilder.entry("STONEMELDING", discipline).icon(Items.STONE).parent("BASIC_ALCHEMY")
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.ESSENCE_DUST_EARTH.get()).requiredCraftStack(Items.STONE).requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.EARTH, 1).recipe(PrimalMagick.MODID, "stone_from_stonemelding").recipe(PrimalMagick.MODID, "deepslate_from_stonemelding")
                    .recipe(PrimalMagick.MODID, "cobblestone_from_stonemelding").recipe(PrimalMagick.MODID, "gravel_from_stonemelding").build())
            .build(consumer);
        ResearchEntryBuilder.entry("SKYGLASS", discipline).icon(ItemsPM.SKYGLASS.get()).parent("STONEMELDING")
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.ESSENCE_DUST_SKY.get()).requiredCraftStack(Items.GLASS).requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SKY, 1)
                    .recipe(ItemsPM.SKYGLASS.get()).recipe(ItemsPM.SKYGLASS_PANE.get())
                    .recipe(ItemsPM.STAINED_SKYGLASS_BLACK.get()).recipe(ItemsPM.STAINED_SKYGLASS_BLUE.get()).recipe(ItemsPM.STAINED_SKYGLASS_BROWN.get()).recipe(ItemsPM.STAINED_SKYGLASS_CYAN.get())
                    .recipe(ItemsPM.STAINED_SKYGLASS_GRAY.get()).recipe(ItemsPM.STAINED_SKYGLASS_GREEN.get()).recipe(ItemsPM.STAINED_SKYGLASS_LIGHT_BLUE.get()).recipe(ItemsPM.STAINED_SKYGLASS_LIGHT_GRAY.get())
                    .recipe(ItemsPM.STAINED_SKYGLASS_LIME.get()).recipe(ItemsPM.STAINED_SKYGLASS_MAGENTA.get()).recipe(ItemsPM.STAINED_SKYGLASS_ORANGE.get()).recipe(ItemsPM.STAINED_SKYGLASS_PINK.get())
                    .recipe(ItemsPM.STAINED_SKYGLASS_PURPLE.get()).recipe(ItemsPM.STAINED_SKYGLASS_RED.get()).recipe(ItemsPM.STAINED_SKYGLASS_WHITE.get()).recipe(ItemsPM.STAINED_SKYGLASS_YELLOW.get())
                    .recipe(PrimalMagick.MODID, "stained_skyglass_pane_black_from_blocks").recipe(PrimalMagick.MODID, "stained_skyglass_pane_black_from_panes")
                    .recipe(PrimalMagick.MODID, "stained_skyglass_pane_blue_from_blocks").recipe(PrimalMagick.MODID, "stained_skyglass_pane_blue_from_panes")
                    .recipe(PrimalMagick.MODID, "stained_skyglass_pane_brown_from_blocks").recipe(PrimalMagick.MODID, "stained_skyglass_pane_brown_from_panes")
                    .recipe(PrimalMagick.MODID, "stained_skyglass_pane_cyan_from_blocks").recipe(PrimalMagick.MODID, "stained_skyglass_pane_cyan_from_panes")
                    .recipe(PrimalMagick.MODID, "stained_skyglass_pane_gray_from_blocks").recipe(PrimalMagick.MODID, "stained_skyglass_pane_gray_from_panes")
                    .recipe(PrimalMagick.MODID, "stained_skyglass_pane_green_from_blocks").recipe(PrimalMagick.MODID, "stained_skyglass_pane_green_from_panes")
                    .recipe(PrimalMagick.MODID, "stained_skyglass_pane_light_blue_from_blocks").recipe(PrimalMagick.MODID, "stained_skyglass_pane_light_blue_from_panes")
                    .recipe(PrimalMagick.MODID, "stained_skyglass_pane_light_gray_from_blocks").recipe(PrimalMagick.MODID, "stained_skyglass_pane_light_gray_from_panes")
                    .recipe(PrimalMagick.MODID, "stained_skyglass_pane_lime_from_blocks").recipe(PrimalMagick.MODID, "stained_skyglass_pane_lime_from_panes")
                    .recipe(PrimalMagick.MODID, "stained_skyglass_pane_magenta_from_blocks").recipe(PrimalMagick.MODID, "stained_skyglass_pane_magenta_from_panes")
                    .recipe(PrimalMagick.MODID, "stained_skyglass_pane_orange_from_blocks").recipe(PrimalMagick.MODID, "stained_skyglass_pane_orange_from_panes")
                    .recipe(PrimalMagick.MODID, "stained_skyglass_pane_pink_from_blocks").recipe(PrimalMagick.MODID, "stained_skyglass_pane_pink_from_panes")
                    .recipe(PrimalMagick.MODID, "stained_skyglass_pane_purple_from_blocks").recipe(PrimalMagick.MODID, "stained_skyglass_pane_purple_from_panes")
                    .recipe(PrimalMagick.MODID, "stained_skyglass_pane_red_from_blocks").recipe(PrimalMagick.MODID, "stained_skyglass_pane_red_from_panes")
                    .recipe(PrimalMagick.MODID, "stained_skyglass_pane_white_from_blocks").recipe(PrimalMagick.MODID, "stained_skyglass_pane_white_from_panes")
                    .recipe(PrimalMagick.MODID, "stained_skyglass_pane_yellow_from_blocks").recipe(PrimalMagick.MODID, "stained_skyglass_pane_yellow_from_panes")
                    .build())
            .build(consumer);
        ResearchEntryBuilder.entry("SHARD_SYNTHESIS", discipline).icon(ItemsPM.ESSENCE_SHARD_EARTH.get()).parent("EXPERT_ALCHEMY")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage()
                    .attunement(Source.EARTH, 1).attunement(Source.SEA, 1).attunement(Source.SKY, 1).attunement(Source.SUN, 1).attunement(Source.MOON, 1)
                    .recipe(PrimalMagick.MODID, "essence_shard_earth_from_dust").recipe(PrimalMagick.MODID, "essence_shard_sea_from_dust")
                    .recipe(PrimalMagick.MODID, "essence_shard_sky_from_dust").recipe(PrimalMagick.MODID, "essence_shard_sun_from_dust")
                    .recipe(PrimalMagick.MODID, "essence_shard_moon_from_dust").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_blood").attunement(Source.BLOOD, 1)
                    .recipe(PrimalMagick.MODID, "essence_shard_blood_from_dust").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_infernal").attunement(Source.INFERNAL, 1)
                    .recipe(PrimalMagick.MODID, "essence_shard_infernal_from_dust").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_void").attunement(Source.VOID, 1)
                    .recipe(PrimalMagick.MODID, "essence_shard_void_from_dust").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_hallowed").attunement(Source.HALLOWED, 1)
                    .recipe(PrimalMagick.MODID, "essence_shard_hallowed_from_dust").build())
            .build(consumer);
        ResearchEntryBuilder.entry("SHARD_DESYNTHESIS", discipline).icon(ItemsPM.ESSENCE_SHARD_EARTH.get()).parent("SHARD_SYNTHESIS")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage()
                    .attunement(Source.EARTH, 1).attunement(Source.SEA, 1).attunement(Source.SKY, 1).attunement(Source.SUN, 1).attunement(Source.MOON, 1)
                    .recipe(PrimalMagick.MODID, "essence_dust_earth_from_shard").recipe(PrimalMagick.MODID, "essence_dust_sea_from_shard")
                    .recipe(PrimalMagick.MODID, "essence_dust_sky_from_shard").recipe(PrimalMagick.MODID, "essence_dust_sun_from_shard")
                    .recipe(PrimalMagick.MODID, "essence_dust_moon_from_shard").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_blood").attunement(Source.BLOOD, 1)
                    .recipe(PrimalMagick.MODID, "essence_dust_blood_from_shard").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_infernal").attunement(Source.INFERNAL, 1)
                    .recipe(PrimalMagick.MODID, "essence_dust_infernal_from_shard").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_void").attunement(Source.VOID, 1)
                    .recipe(PrimalMagick.MODID, "essence_dust_void_from_shard").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_hallowed").attunement(Source.HALLOWED, 1)
                    .recipe(PrimalMagick.MODID, "essence_dust_hallowed_from_shard").build())
            .build(consumer);
        ResearchEntryBuilder.entry("PRIMALITE", discipline).icon(ItemsPM.PRIMALITE_INGOT.get()).parent("EXPERT_ALCHEMY")
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.ESSENCE_DUST_EARTH.get()).requiredItemStack(ItemsPM.ESSENCE_DUST_SEA.get())
                    .requiredItemStack(ItemsPM.ESSENCE_DUST_SKY.get()).requiredItemStack(ItemsPM.ESSENCE_DUST_SUN.get()).requiredItemStack(ItemsPM.ESSENCE_DUST_MOON.get())
                    .requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.EARTH, 1).attunement(Source.SEA, 1).attunement(Source.SKY, 1)
                    .attunement(Source.SUN, 1).attunement(Source.MOON, 1).recipe(ItemsPM.PRIMALITE_INGOT.get()).recipe(ItemsPM.PRIMALITE_NUGGET.get())
                    .recipe(PrimalMagick.resource("primalite_ingot_from_nuggets")).recipe(ItemsPM.PRIMALITE_BLOCK.get())
                    .recipe(PrimalMagick.resource("primalite_ingots_from_block")).recipe(ItemsPM.PRIMALITE_SWORD.get()).recipe(ItemsPM.PRIMALITE_TRIDENT.get())
                    .recipe(ItemsPM.PRIMALITE_BOW.get()).recipe(ItemsPM.PRIMALITE_SHOVEL.get()).recipe(ItemsPM.PRIMALITE_PICKAXE.get()).recipe(ItemsPM.PRIMALITE_AXE.get())
                    .recipe(ItemsPM.PRIMALITE_HOE.get()).recipe(ItemsPM.PRIMALITE_FISHING_ROD.get()).recipe(ItemsPM.PRIMALITE_HEAD.get()).recipe(ItemsPM.PRIMALITE_CHEST.get())
                    .recipe(ItemsPM.PRIMALITE_LEGS.get()).recipe(ItemsPM.PRIMALITE_FEET.get()).recipe(ItemsPM.PRIMALITE_SHIELD.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("CRYSTAL_SYNTHESIS", discipline).icon(ItemsPM.ESSENCE_CRYSTAL_EARTH.get()).parent("MASTER_ALCHEMY").parent("SHARD_SYNTHESIS")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage()
                    .attunement(Source.EARTH, 2).attunement(Source.SEA, 2).attunement(Source.SKY, 2).attunement(Source.SUN, 2).attunement(Source.MOON, 2)
                    .recipe(PrimalMagick.MODID, "essence_crystal_earth_from_shard").recipe(PrimalMagick.MODID, "essence_crystal_sea_from_shard")
                    .recipe(PrimalMagick.MODID, "essence_crystal_sky_from_shard").recipe(PrimalMagick.MODID, "essence_crystal_sun_from_shard")
                    .recipe(PrimalMagick.MODID, "essence_crystal_moon_from_shard").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_blood").attunement(Source.BLOOD, 2)
                    .recipe(PrimalMagick.MODID, "essence_crystal_blood_from_shard").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_infernal").attunement(Source.INFERNAL, 2)
                    .recipe(PrimalMagick.MODID, "essence_crystal_infernal_from_shard").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_void").attunement(Source.VOID, 2)
                    .recipe(PrimalMagick.MODID, "essence_crystal_void_from_shard").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_hallowed").attunement(Source.HALLOWED, 2)
                    .recipe(PrimalMagick.MODID, "essence_crystal_hallowed_from_shard").build())
            .build(consumer);
        ResearchEntryBuilder.entry("CRYSTAL_DESYNTHESIS", discipline).icon(ItemsPM.ESSENCE_CRYSTAL_EARTH.get()).parent("CRYSTAL_SYNTHESIS")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage()
                    .attunement(Source.EARTH, 1).attunement(Source.SEA, 1).attunement(Source.SKY, 1).attunement(Source.SUN, 1).attunement(Source.MOON, 1)
                    .recipe(PrimalMagick.MODID, "essence_shard_earth_from_crystal").recipe(PrimalMagick.MODID, "essence_shard_sea_from_crystal")
                    .recipe(PrimalMagick.MODID, "essence_shard_sky_from_crystal").recipe(PrimalMagick.MODID, "essence_shard_sun_from_crystal")
                    .recipe(PrimalMagick.MODID, "essence_shard_moon_from_crystal").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_blood").attunement(Source.BLOOD, 1)
                    .recipe(PrimalMagick.MODID, "essence_shard_blood_from_crystal").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_infernal").attunement(Source.INFERNAL, 1)
                    .recipe(PrimalMagick.MODID, "essence_shard_infernal_from_crystal").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_void").attunement(Source.VOID, 1)
                    .recipe(PrimalMagick.MODID, "essence_shard_void_from_crystal").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_hallowed").attunement(Source.HALLOWED, 1)
                    .recipe(PrimalMagick.MODID, "essence_shard_hallowed_from_crystal").build())
            .build(consumer);
        ResearchEntryBuilder.entry("HEXIUM", discipline).icon(ItemsPM.HEXIUM_INGOT.get()).parent("MASTER_ALCHEMY").parent("PRIMALITE").parent("SHARD_SYNTHESIS")
            .parent(Source.BLOOD.getDiscoverKey()).parent(Source.INFERNAL.getDiscoverKey()).parent(Source.VOID.getDiscoverKey())
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.ESSENCE_SHARD_BLOOD.get()).requiredItemStack(ItemsPM.ESSENCE_SHARD_INFERNAL.get())
                    .requiredItemStack(ItemsPM.ESSENCE_SHARD_VOID.get()).requiredCraftStack(ItemsPM.PRIMALITE_INGOT.get()).requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.BLOOD, 2).attunement(Source.INFERNAL, 2).attunement(Source.VOID, 2).sibling("t_sotu_research_hexium")
                    .recipe(ItemsPM.HEXIUM_INGOT.get()).recipe(ItemsPM.HEXIUM_NUGGET.get()).recipe(PrimalMagick.resource("hexium_ingot_from_nuggets"))
                    .recipe(ItemsPM.HEXIUM_BLOCK.get()).recipe(PrimalMagick.resource("hexium_ingots_from_block"))
                    .recipe(ItemsPM.HEXIUM_SWORD.get()).recipe(ItemsPM.HEXIUM_TRIDENT.get()).recipe(ItemsPM.HEXIUM_BOW.get()).recipe(ItemsPM.HEXIUM_SHOVEL.get())
                    .recipe(ItemsPM.HEXIUM_PICKAXE.get()).recipe(ItemsPM.HEXIUM_AXE.get()).recipe(ItemsPM.HEXIUM_HOE.get()).recipe(ItemsPM.HEXIUM_FISHING_ROD.get())
                    .recipe(ItemsPM.HEXIUM_HEAD.get()).recipe(ItemsPM.HEXIUM_CHEST.get()).recipe(ItemsPM.HEXIUM_LEGS.get()).recipe(ItemsPM.HEXIUM_FEET.get())
                    .recipe(ItemsPM.HEXIUM_SHIELD.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("CLUSTER_SYNTHESIS", discipline).icon(ItemsPM.ESSENCE_CLUSTER_EARTH.get()).parent("SUPREME_ALCHEMY").parent("CRYSTAL_SYNTHESIS")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 3).build())
            .stage(ResearchStageBuilder.stage()
                    .attunement(Source.EARTH, 3).attunement(Source.SEA, 3).attunement(Source.SKY, 3).attunement(Source.SUN, 3).attunement(Source.MOON, 3)
                    .recipe(PrimalMagick.MODID, "essence_cluster_earth_from_crystal").recipe(PrimalMagick.MODID, "essence_cluster_sea_from_crystal")
                    .recipe(PrimalMagick.MODID, "essence_cluster_sky_from_crystal").recipe(PrimalMagick.MODID, "essence_cluster_sun_from_crystal")
                    .recipe(PrimalMagick.MODID, "essence_cluster_moon_from_crystal").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_blood").attunement(Source.BLOOD, 3)
                    .recipe(PrimalMagick.MODID, "essence_cluster_blood_from_crystal").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_infernal").attunement(Source.INFERNAL, 3)
                    .recipe(PrimalMagick.MODID, "essence_cluster_infernal_from_crystal").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_void").attunement(Source.VOID, 3)
                    .recipe(PrimalMagick.MODID, "essence_cluster_void_from_crystal").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_hallowed").attunement(Source.HALLOWED, 3)
                    .recipe(PrimalMagick.MODID, "essence_cluster_hallowed_from_crystal").build())
            .build(consumer);
        ResearchEntryBuilder.entry("CLUSTER_DESYNTHESIS", discipline).icon(ItemsPM.ESSENCE_CLUSTER_EARTH.get()).parent("CLUSTER_SYNTHESIS")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage()
                    .attunement(Source.EARTH, 1).attunement(Source.SEA, 1).attunement(Source.SKY, 1).attunement(Source.SUN, 1).attunement(Source.MOON, 1)
                    .recipe(PrimalMagick.MODID, "essence_crystal_earth_from_cluster").recipe(PrimalMagick.MODID, "essence_crystal_sea_from_cluster")
                    .recipe(PrimalMagick.MODID, "essence_crystal_sky_from_cluster").recipe(PrimalMagick.MODID, "essence_crystal_sun_from_cluster")
                    .recipe(PrimalMagick.MODID, "essence_crystal_moon_from_cluster").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_blood").attunement(Source.BLOOD, 1)
                    .recipe(PrimalMagick.MODID, "essence_crystal_blood_from_cluster").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_infernal").attunement(Source.INFERNAL, 1)
                    .recipe(PrimalMagick.MODID, "essence_crystal_infernal_from_cluster").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_void").attunement(Source.VOID, 1)
                    .recipe(PrimalMagick.MODID, "essence_crystal_void_from_cluster").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_hallowed").attunement(Source.HALLOWED, 1)
                    .recipe(PrimalMagick.MODID, "essence_crystal_hallowed_from_cluster").build())
            .build(consumer);
        ResearchEntryBuilder.entry("HALLOWSTEEL", discipline).icon(ItemsPM.HALLOWSTEEL_INGOT.get()).parent("SUPREME_ALCHEMY").parent("HEXIUM").parent("CRYSTAL_SYNTHESIS")
            .parent(Source.HALLOWED.getDiscoverKey())
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.ESSENCE_CRYSTAL_HALLOWED.get()).requiredCraftStack(ItemsPM.HEXIUM_INGOT.get())
                    .requiredKnowledge(KnowledgeType.THEORY, 3).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.HALLOWED, 3).recipe(ItemsPM.HALLOWSTEEL_INGOT.get()).recipe(ItemsPM.HALLOWSTEEL_NUGGET.get())
                    .recipe(PrimalMagick.resource("hallowsteel_ingot_from_nuggets")).recipe(ItemsPM.HALLOWSTEEL_BLOCK.get())
                    .recipe(PrimalMagick.resource("hallowsteel_ingots_from_block")).recipe(ItemsPM.HALLOWSTEEL_SWORD.get())
                    .recipe(ItemsPM.HALLOWSTEEL_TRIDENT.get()).recipe(ItemsPM.HALLOWSTEEL_BOW.get()).recipe(ItemsPM.HALLOWSTEEL_SHOVEL.get()).recipe(ItemsPM.HALLOWSTEEL_PICKAXE.get())
                    .recipe(ItemsPM.HALLOWSTEEL_AXE.get()).recipe(ItemsPM.HALLOWSTEEL_HOE.get()).recipe(ItemsPM.HALLOWSTEEL_FISHING_ROD.get()).recipe(ItemsPM.HALLOWSTEEL_HEAD.get())
                    .recipe(ItemsPM.HALLOWSTEEL_CHEST.get()).recipe(ItemsPM.HALLOWSTEEL_LEGS.get()).recipe(ItemsPM.HALLOWSTEEL_FEET.get()).recipe(ItemsPM.HALLOWSTEEL_SHIELD.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("CALCINATOR_BASIC", discipline).icon(ItemsPM.CALCINATOR_BASIC.get()).parent("BASIC_ALCHEMY")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).requiredCraftStack(ItemsPM.ESSENCE_DUST_EARTH.get()).requiredCraftStack(ItemsPM.ESSENCE_DUST_SEA.get())
                    .requiredCraftStack(ItemsPM.ESSENCE_DUST_SKY.get()).requiredCraftStack(ItemsPM.ESSENCE_DUST_SUN.get()).requiredCraftStack(ItemsPM.ESSENCE_DUST_MOON.get()).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.CALCINATOR_BASIC.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("CALCINATOR_ENCHANTED", discipline).icon(ItemsPM.CALCINATOR_ENCHANTED.get()).parent("EXPERT_ALCHEMY").parent("EXPERT_MANAWEAVING")
            .parent("SHARD_SYNTHESIS").parent("CALCINATOR_BASIC")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.CALCINATOR_ENCHANTED.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("CALCINATOR_FORBIDDEN", discipline).icon(ItemsPM.CALCINATOR_FORBIDDEN.get()).parent("MASTER_ALCHEMY").parent("MASTER_MANAWEAVING")
            .parent("CRYSTAL_SYNTHESIS").parent("CALCINATOR_ENCHANTED") .parent(Source.BLOOD.getDiscoverKey()).parent(Source.INFERNAL.getDiscoverKey()).parent(Source.VOID.getDiscoverKey())
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.CALCINATOR_FORBIDDEN.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("CALCINATOR_HEAVENLY", discipline).icon(ItemsPM.CALCINATOR_HEAVENLY.get()).parent("SUPREME_ALCHEMY").parent("SUPREME_MANAWEAVING")
            .parent("CLUSTER_SYNTHESIS").parent("CALCINATOR_FORBIDDEN").parent(Source.HALLOWED.getDiscoverKey())
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 3).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.CALCINATOR_HEAVENLY.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("CRYOTREATMENT", discipline).icon(Items.ICE).parent("STONEMELDING")
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.ESSENCE_DUST_SEA.get()).requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SEA, 1).recipe(PrimalMagick.MODID, "ice_from_cryotreatment").recipe(PrimalMagick.MODID, "obsidian_from_cryotreatment")
                    .recipe(PrimalMagick.resource("slime_ball_from_cryotreatment")).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SANGUINE_CRUCIBLE", discipline).icon(ItemsPM.SANGUINE_CRUCIBLE.get()).parent("MASTER_ALCHEMY").parent("HEXIUM").parent("CRYSTAL_SYNTHESIS")
            .parent("SPELL_PAYLOAD_CONJURE_ANIMAL").parent("SPELL_PAYLOAD_DRAIN_SOUL")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.BLOOD, 3).attunement(Source.INFERNAL, 3).sibling("t_sotu_research_sanguine_crucible")
                    .recipe(ItemsPM.SANGUINE_CRUCIBLE.get()).recipe(ItemsPM.SANGUINE_CORE_BLANK.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SANGUINE_CORE_LAND_ANIMALS", discipline).icon(ItemsPM.SANGUINE_CORE_BLANK.get()).parent("SANGUINE_CRUCIBLE")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.EARTH, 2).recipe(ItemsPM.SANGUINE_CORE_CAT.get()).recipe(ItemsPM.SANGUINE_CORE_CAMEL.get()).recipe(ItemsPM.SANGUINE_CORE_CAVE_SPIDER.get())
                    .recipe(ItemsPM.SANGUINE_CORE_COW.get()) .recipe(ItemsPM.SANGUINE_CORE_DONKEY.get()).recipe(ItemsPM.SANGUINE_CORE_FOX.get()).recipe(ItemsPM.SANGUINE_CORE_GOAT.get())
                    .recipe(ItemsPM.SANGUINE_CORE_HORSE.get()) .recipe(ItemsPM.SANGUINE_CORE_LLAMA.get()).recipe(ItemsPM.SANGUINE_CORE_OCELOT.get()).recipe(ItemsPM.SANGUINE_CORE_PANDA.get())
                    .recipe(ItemsPM.SANGUINE_CORE_PIG.get()).recipe(ItemsPM.SANGUINE_CORE_RABBIT.get()).recipe(ItemsPM.SANGUINE_CORE_RAVAGER.get()).recipe(ItemsPM.SANGUINE_CORE_SHEEP.get())
                    .recipe(ItemsPM.SANGUINE_CORE_SILVERFISH.get()).recipe(ItemsPM.SANGUINE_CORE_SLIME.get()).recipe(ItemsPM.SANGUINE_CORE_SNIFFER.get()).recipe(ItemsPM.SANGUINE_CORE_SPIDER.get())
                    .recipe(ItemsPM.SANGUINE_CORE_WOLF.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SANGUINE_CORE_SEA_CREATURES", discipline).icon(ItemsPM.SANGUINE_CORE_BLANK.get()).parent("SANGUINE_CRUCIBLE")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SEA, 2).recipe(ItemsPM.SANGUINE_CORE_AXOLOTL.get()).recipe(ItemsPM.SANGUINE_CORE_COD.get()).recipe(ItemsPM.SANGUINE_CORE_DOLPHIN.get())
                    .recipe(ItemsPM.SANGUINE_CORE_ELDER_GUARDIAN.get()).recipe(ItemsPM.SANGUINE_CORE_GLOW_SQUID.get()).recipe(ItemsPM.SANGUINE_CORE_GUARDIAN.get()).recipe(ItemsPM.SANGUINE_CORE_POLAR_BEAR.get())
                    .recipe(ItemsPM.SANGUINE_CORE_PUFFERFISH.get()).recipe(ItemsPM.SANGUINE_CORE_SALMON.get()).recipe(ItemsPM.SANGUINE_CORE_SQUID.get()).recipe(ItemsPM.SANGUINE_CORE_TROPICAL_FISH.get())
                    .recipe(ItemsPM.SANGUINE_CORE_TURTLE.get()).recipe(ItemsPM.SANGUINE_CORE_FROG.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SANGUINE_CORE_FLYING_CREATURES", discipline).icon(ItemsPM.SANGUINE_CORE_BLANK.get()).parent("SANGUINE_CRUCIBLE")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SKY, 2).recipe(ItemsPM.SANGUINE_CORE_BAT.get()).recipe(ItemsPM.SANGUINE_CORE_BEE.get()).recipe(ItemsPM.SANGUINE_CORE_CHICKEN.get())
                    .recipe(ItemsPM.SANGUINE_CORE_PARROT.get()).recipe(ItemsPM.SANGUINE_CORE_VEX.get()).recipe(ItemsPM.SANGUINE_CORE_ALLAY.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SANGUINE_CORE_PLANTS", discipline).icon(ItemsPM.SANGUINE_CORE_BLANK.get()).parent("SANGUINE_CRUCIBLE")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SUN, 2).recipe(ItemsPM.SANGUINE_CORE_CREEPER.get()).recipe(ItemsPM.SANGUINE_CORE_MOOSHROOM.get())
                    .recipe(ItemsPM.SANGUINE_CORE_TREEFOLK.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SANGUINE_CORE_UNDEAD", discipline).icon(ItemsPM.SANGUINE_CORE_BLANK.get()).parent("SANGUINE_CRUCIBLE")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.MOON, 2).recipe(ItemsPM.SANGUINE_CORE_DROWNED.get()).recipe(ItemsPM.SANGUINE_CORE_HUSK.get()).recipe(ItemsPM.SANGUINE_CORE_PHANTOM.get())
                    .recipe(ItemsPM.SANGUINE_CORE_SKELETON.get()).recipe(ItemsPM.SANGUINE_CORE_SKELETON_HORSE.get()).recipe(ItemsPM.SANGUINE_CORE_STRAY.get()).recipe(ItemsPM.SANGUINE_CORE_WITHER_SKELETON.get())
                    .recipe(ItemsPM.SANGUINE_CORE_ZOGLIN.get()).recipe(ItemsPM.SANGUINE_CORE_ZOMBIE.get()).recipe(ItemsPM.SANGUINE_CORE_ZOMBIE_HORSE.get()).recipe(ItemsPM.SANGUINE_CORE_ZOMBIE_VILLAGER.get())
                    .recipe(ItemsPM.SANGUINE_CORE_ZOMBIFIED_PIGLIN.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SANGUINE_CORE_SAPIENTS", discipline).icon(ItemsPM.SANGUINE_CORE_BLANK.get()).parent("SANGUINE_CRUCIBLE")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.BLOOD, 2).recipe(ItemsPM.SANGUINE_CORE_EVOKER.get()).recipe(ItemsPM.SANGUINE_CORE_PILLAGER.get()).recipe(ItemsPM.SANGUINE_CORE_VILLAGER.get())
                    .recipe(ItemsPM.SANGUINE_CORE_VINDICATOR.get()).recipe(ItemsPM.SANGUINE_CORE_WITCH.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SANGUINE_CORE_DEMONS", discipline).icon(ItemsPM.SANGUINE_CORE_BLANK.get()).parent("SANGUINE_CRUCIBLE")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.INFERNAL, 2).recipe(ItemsPM.SANGUINE_CORE_BLAZE.get()).recipe(ItemsPM.SANGUINE_CORE_GHAST.get()).recipe(ItemsPM.SANGUINE_CORE_HOGLIN.get())
                    .recipe(ItemsPM.SANGUINE_CORE_MAGMA_CUBE.get()).recipe(ItemsPM.SANGUINE_CORE_PIGLIN.get()).recipe(ItemsPM.SANGUINE_CORE_PIGLIN_BRUTE.get()).recipe(ItemsPM.SANGUINE_CORE_STRIDER.get())
                    .build())
            .build(consumer);
        ResearchEntryBuilder.entry("SANGUINE_CORE_ALIENS", discipline).icon(ItemsPM.SANGUINE_CORE_BLANK.get()).parent("SANGUINE_CRUCIBLE")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.VOID, 2).recipe(ItemsPM.SANGUINE_CORE_ENDERMAN.get()).recipe(ItemsPM.SANGUINE_CORE_ENDERMITE.get()).recipe(ItemsPM.SANGUINE_CORE_SHULKER.get())
                    .build())
            .build(consumer);
        ResearchEntryBuilder.entry("IGNYX", discipline).icon(ItemsPM.IGNYX.get()).parent("EXPERT_ALCHEMY").parent("CALCINATOR_BASIC").parent("STONEMELDING")
            .parent(Source.INFERNAL.getDiscoverKey())
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).requiredItemTag(ItemTags.COALS).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.INFERNAL, 2).recipe(ItemsPM.IGNYX.get()).recipe(ItemsPM.IGNYX_BLOCK.get())
                    .recipe(PrimalMagick.resource("ignyx_from_storage_block")).recipe(PrimalMagick.resource("torch_from_ignyx")).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SYNTHETIC_GEM_BUDS", discipline).icon(Items.AMETHYST_CLUSTER).parent("MASTER_ALCHEMY").parent("SHARD_SYNTHESIS").parent("STONEMELDING")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).requiredItemTag(Tags.Items.GEMS_AMETHYST).build())
            .stage(ResearchStageBuilder.stage().attunement(SourceList.builder().withEarth(3).withSun(3).build()).recipe(ItemsPM.ENERGIZED_AMETHYST.get()).recipe(ItemsPM.DAMAGED_BUDDING_AMETHYST_BLOCK.get())
                    .recipe(ItemsPM.CHIPPED_BUDDING_AMETHYST_BLOCK.get()).recipe(ItemsPM.FLAWED_BUDDING_AMETHYST_BLOCK.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.HALLOWED.getDiscoverKey()).attunement(Source.HALLOWED, 3).recipe(ItemsPM.ENERGIZED_DIAMOND.get())
                    .recipe(ItemsPM.DAMAGED_BUDDING_DIAMOND_BLOCK.get()).recipe(ItemsPM.CHIPPED_BUDDING_DIAMOND_BLOCK.get()).recipe(ItemsPM.FLAWED_BUDDING_DIAMOND_BLOCK.get())
                    .recipe(ItemsPM.ENERGIZED_EMERALD.get()).recipe(ItemsPM.DAMAGED_BUDDING_EMERALD_BLOCK.get()).recipe(ItemsPM.CHIPPED_BUDDING_EMERALD_BLOCK.get())
                    .recipe(ItemsPM.FLAWED_BUDDING_EMERALD_BLOCK.get()).recipe(ItemsPM.ENERGIZED_QUARTZ.get()).recipe(ItemsPM.DAMAGED_BUDDING_QUARTZ_BLOCK.get())
                    .recipe(ItemsPM.CHIPPED_BUDDING_QUARTZ_BLOCK.get()).recipe(ItemsPM.FLAWED_BUDDING_QUARTZ_BLOCK.get()).build())
            .build(consumer);
    }

    protected void registerSorceryEntries(Consumer<IFinishedResearchEntry> consumer) {
        String discipline = "SORCERY";
        ResearchEntryBuilder.entry("BASIC_SORCERY", discipline).icon("textures/research/discipline_sorcery.png").parent("UNLOCK_SORCERY")
            .stage(ResearchStageBuilder.stage().attunement(Source.EARTH, 1)
                    .recipe(ItemsPM.SPELL_SCROLL_BLANK.get()).recipe(ItemsPM.SPELLCRAFTING_ALTAR.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("EXPERT_SORCERY", discipline).icon("textures/research/discipline_sorcery.png").parent("SPELL_PAYLOAD_LIGHTNING").parent("SPELL_PAYLOAD_FROST")
            .stage(ResearchStageBuilder.stage().requiredResearch("t_spells_crafted_expert").requiredResearch("t_spells_cast_expert").build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("MASTER_SORCERY", discipline).icon("textures/research/discipline_sorcery.png").parent("SPELL_VEHICLE_PROJECTILE").parent("SPELL_MOD_AMPLIFY")
            .stage(ResearchStageBuilder.stage().requiredResearch("t_discover_forbidden").requiredResearch("t_spells_cast_master").requiredResearch("t_spell_cost_master").build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("SUPREME_SORCERY", discipline).icon("textures/research/discipline_sorcery.png").parent("SPELL_VEHICLE_BOLT").parent("SPELL_MOD_QUICKEN")
            .stage(ResearchStageBuilder.stage().requiredResearch(Source.HALLOWED.getDiscoverKey()).requiredResearch("t_spells_cast_supreme").requiredResearch("t_spell_cost_supreme")
                    .reveals("SECRETS_OF_THE_UNIVERSE").build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("COMPLETE_SORCERY", discipline).hidden().icon("textures/research/discipline_sorcery.png").finale(discipline)
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.EARTH, 1).attunement(Source.SEA, 1).attunement(Source.SKY, 1).attunement(Source.SUN, 1).attunement(Source.MOON, 1)
                    .attunement(Source.BLOOD, 1).attunement(Source.INFERNAL, 1).attunement(Source.VOID, 1).attunement(Source.HALLOWED, 1).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_INSCRIPTION", discipline).icon(ItemsPM.WAND_INSCRIPTION_TABLE.get()).parent("BASIC_SORCERY").parent("ADVANCED_WANDMAKING")
            .stage(ResearchStageBuilder.stage().requiredResearch("t_spells_crafted_expert").requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.WAND_INSCRIPTION_TABLE.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_VEHICLE_PROJECTILE", discipline).parent("EXPERT_SORCERY")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_VEHICLE_BOLT", discipline).parent("MASTER_SORCERY").parent("SPELL_VEHICLE_PROJECTILE")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_FROST", discipline).icon(Source.SEA.getImage()).parent("BASIC_SORCERY")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SEA, 1).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_LIGHTNING", discipline).icon(Source.SKY.getImage()).parent("BASIC_SORCERY")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SKY, 1).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_SOLAR", discipline).icon(Source.SUN.getImage()).parent("SPELL_PAYLOAD_LIGHTNING")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SUN, 1).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_LUNAR", discipline).icon(Source.MOON.getImage()).parent("SPELL_PAYLOAD_FROST")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.MOON, 1).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_BLOOD", discipline).icon(Source.BLOOD.getImage()).parent("EXPERT_SORCERY").parent(Source.BLOOD.getDiscoverKey())
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.BLOOD, 1).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_FLAME", discipline).icon(Source.INFERNAL.getImage()).parent("EXPERT_SORCERY").parent(Source.INFERNAL.getDiscoverKey())
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.INFERNAL, 1).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_VOID", discipline).icon(Source.VOID.getImage()).parent("EXPERT_SORCERY").parent(Source.VOID.getDiscoverKey())
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.VOID, 1).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_HOLY", discipline).icon(Source.HALLOWED.getImage()).parent("MASTER_SORCERY").parent(Source.HALLOWED.getDiscoverKey())
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.HALLOWED, 1).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_BREAK", discipline).icon(Source.EARTH.getImage()).parent("EXPERT_SORCERY")
            .stage(ResearchStageBuilder.stage().requiredResearch("m_blocks_broken_barehanded_expert").requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.EARTH, 3).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_CONJURE_STONE", discipline).icon(Source.EARTH.getImage()).parent("EXPERT_SORCERY")
            .stage(ResearchStageBuilder.stage().requiredResearch("b_place_stone_expert").requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.EARTH, 3).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_CONJURE_WATER", discipline).icon(Source.SEA.getImage()).parent("EXPERT_SORCERY").parent("SPELL_PAYLOAD_FROST")
            .stage(ResearchStageBuilder.stage().requiredResearch("m_drown_a_little", true).requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SEA, 3).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_SHEAR", discipline).icon(Source.SKY.getImage()).parent("EXPERT_SORCERY").parent("SPELL_PAYLOAD_LIGHTNING").parent("SPELL_PAYLOAD_BREAK")
            .stage(ResearchStageBuilder.stage().requiredResearch("b_shears_used_expert").requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SKY, 3).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_FLIGHT", discipline).icon(Source.SKY.getImage()).parent("SUPREME_SORCERY").parent("SPELL_PAYLOAD_LIGHTNING")
            .stage(ResearchStageBuilder.stage().requiredResearch("m_fly_elytra").requiredKnowledge(KnowledgeType.THEORY, 3).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SKY, 3).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_CONJURE_LIGHT", discipline).icon(Source.SUN.getImage()).parent("EXPERT_SORCERY").parent("SPELL_PAYLOAD_SOLAR")
            .stage(ResearchStageBuilder.stage().requiredResearch("b_place_torch_expert").requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SUN, 3).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_HEALING", discipline).icon(Source.SUN.getImage()).parent("EXPERT_SORCERY").parent("SPELL_PAYLOAD_SOLAR")
            .stage(ResearchStageBuilder.stage().requiredResearch("m_near_death_experience", true).requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SUN, 3).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_POLYMORPH", discipline).icon(Source.MOON.getImage()).parent("EXPERT_SORCERY").parent("SPELL_PAYLOAD_LUNAR")
            .stage(ResearchStageBuilder.stage().requiredResearch("m_furry_friend", true).requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.MOON, 3).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_POLYMORPH_SHEEP", discipline).hidden().finaleExempt().icon(Source.MOON.getImage())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_CONJURE_ANIMAL", discipline).icon(Source.BLOOD.getImage()).parent("MASTER_SORCERY").parent("SPELL_PAYLOAD_BLOOD")
            .stage(ResearchStageBuilder.stage().requiredResearch("m_breed_animal", true).requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.BLOOD, 3).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_CONJURE_LAVA", discipline).icon(Source.INFERNAL.getImage()).parent("MASTER_SORCERY").parent("SPELL_PAYLOAD_CONJURE_WATER").parent("SPELL_PAYLOAD_FLAME")
            .stage(ResearchStageBuilder.stage().requiredResearch("m_feel_the_burn", true).requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.INFERNAL, 3).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_DRAIN_SOUL", discipline).icon(Source.INFERNAL.getImage()).parent("MASTER_SORCERY").parent("SPELL_PAYLOAD_FLAME")
            .stage(ResearchStageBuilder.stage().requiredItemStack(Items.SOUL_SAND).requiredItemStack(Items.SOUL_SOIL).requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.INFERNAL, 3).recipe(ItemsPM.SOUL_GEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_TELEPORT", discipline).icon(Source.VOID.getImage()).parent("MASTER_SORCERY").parent("SPELL_PAYLOAD_VOID")
            .stage(ResearchStageBuilder.stage().requiredResearch("m_teleport_a_lot").requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.VOID, 3).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_CONSECRATE", discipline).icon(Source.HALLOWED.getImage()).parent("SUPREME_SORCERY").parent("SPELL_PAYLOAD_HOLY")
            .stage(ResearchStageBuilder.stage().requiredResearch("b_scan_nether_star", true).requiredKnowledge(KnowledgeType.THEORY, 3).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.HALLOWED, 3).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_MOD_AMPLIFY", discipline).parent("EXPERT_SORCERY")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_MOD_MINE", discipline).parent("EXPERT_SORCERY")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_MOD_QUICKEN", discipline).parent("MASTER_SORCERY").parent("SPELL_MOD_AMPLIFY")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_MOD_BURST", discipline).parent("MASTER_SORCERY").parent("SPELL_MOD_MINE")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_MOD_FORK", discipline).parent("SUPREME_SORCERY").parent("SPELL_MOD_QUICKEN").parent("SPELL_MOD_BURST")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 3).build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
    }

    protected void registerRuneworkingEntries(Consumer<IFinishedResearchEntry> consumer) {
        String discipline = "RUNEWORKING";
        ResearchEntryBuilder.entry("BASIC_RUNEWORKING", discipline).icon("textures/research/discipline_runeworking.png").parent("UNLOCK_RUNEWORKING")
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.RUNECARVING_TABLE.get()).recipe(ItemsPM.RUNE_UNATTUNED.get()).recipe(ItemsPM.RUNESCRIBING_ALTAR_BASIC.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("EXPERT_RUNEWORKING", discipline).icon("textures/research/discipline_runeworking.png").parent("RUNE_EARTH").parent("RUNE_PROJECT").parent("RUNE_ITEM")
            .stage(ResearchStageBuilder.stage().requiredResearch("b_crafted_runeworking_expert").requiredResearch("t_items_runescribed_expert").build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.RUNESCRIBING_ALTAR_ENCHANTED.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("MASTER_RUNEWORKING", discipline).icon("textures/research/discipline_runeworking.png").parent("RUNE_ABSORB").parent("RUNE_CREATURE")
            .stage(ResearchStageBuilder.stage().requiredResearch("t_discover_forbidden").requiredResearch("b_crafted_runeworking_master").requiredResearch("t_items_runescribed_master").build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.RUNESCRIBING_ALTAR_FORBIDDEN.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SUPREME_RUNEWORKING", discipline).icon("textures/research/discipline_runeworking.png").parent("RUNE_POWER")
            .stage(ResearchStageBuilder.stage().requiredResearch(Source.HALLOWED.getDiscoverKey()).requiredResearch("b_crafted_runeworking_supreme").requiredResearch("t_items_runescribed_supreme")
                    .reveals("SECRETS_OF_THE_UNIVERSE").build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.RUNESCRIBING_ALTAR_HEAVENLY.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("COMPLETE_RUNEWORKING", discipline).hidden().icon("textures/research/discipline_runeworking.png").finale(discipline)
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.EARTH, 1).attunement(Source.SEA, 1).attunement(Source.SKY, 1).attunement(Source.SUN, 1).attunement(Source.MOON, 1)
                    .attunement(Source.BLOOD, 1).attunement(Source.INFERNAL, 1).attunement(Source.VOID, 1).attunement(Source.HALLOWED, 1).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_EARTH", discipline).icon(ItemsPM.RUNE_EARTH.get()).parent("BASIC_RUNEWORKING")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.EARTH, 1).recipe(ItemsPM.RUNE_EARTH.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_SEA", discipline).icon(ItemsPM.RUNE_SEA.get()).parent("RUNE_EARTH")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SEA, 1).recipe(ItemsPM.RUNE_SEA.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_SKY", discipline).icon(ItemsPM.RUNE_SKY.get()).parent("RUNE_EARTH")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SKY, 1).recipe(ItemsPM.RUNE_SKY.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_SUN", discipline).icon(ItemsPM.RUNE_SUN.get()).parent("RUNE_SKY")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SUN, 1).recipe(ItemsPM.RUNE_SUN.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_MOON", discipline).icon(ItemsPM.RUNE_MOON.get()).parent("RUNE_SEA")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.MOON, 1).recipe(ItemsPM.RUNE_MOON.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_PROJECT", discipline).icon(ItemsPM.RUNE_PROJECT.get()).parent("BASIC_RUNEWORKING")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.RUNE_PROJECT.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_PROTECT", discipline).icon(ItemsPM.RUNE_PROTECT.get()).parent("RUNE_PROJECT")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.RUNE_PROTECT.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_ITEM", discipline).icon(ItemsPM.RUNE_ITEM.get()).parent("BASIC_RUNEWORKING")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.RUNE_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_SELF", discipline).icon(ItemsPM.RUNE_SELF.get()).parent("RUNE_ITEM")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.RUNE_SELF.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_BLOOD", discipline).icon(ItemsPM.RUNE_BLOOD.get()).parent("EXPERT_RUNEWORKING").parent(Source.BLOOD.getDiscoverKey())
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.BLOOD, 1).recipe(ItemsPM.RUNE_BLOOD.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_INFERNAL", discipline).icon(ItemsPM.RUNE_INFERNAL.get()).parent("EXPERT_RUNEWORKING").parent(Source.INFERNAL.getDiscoverKey())
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.INFERNAL, 1).recipe(ItemsPM.RUNE_INFERNAL.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_VOID", discipline).icon(ItemsPM.RUNE_VOID.get()).parent("EXPERT_RUNEWORKING").parent(Source.VOID.getDiscoverKey())
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.VOID, 1).recipe(ItemsPM.RUNE_VOID.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_ABSORB", discipline).icon(ItemsPM.RUNE_ABSORB.get()).parent("EXPERT_RUNEWORKING")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.RUNE_ABSORB.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_DISPEL", discipline).icon(ItemsPM.RUNE_DISPEL.get()).parent("RUNE_ABSORB")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.RUNE_DISPEL.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_SUMMON", discipline).icon(ItemsPM.RUNE_SUMMON.get()).parent("RUNE_ABSORB")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.RUNE_SUMMON.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_AREA", discipline).icon(ItemsPM.RUNE_AREA.get()).parent("RUNE_CREATURE")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.RUNE_AREA.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_CREATURE", discipline).icon(ItemsPM.RUNE_CREATURE.get()).parent("EXPERT_RUNEWORKING")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.RUNE_CREATURE.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_HALLOWED", discipline).icon(ItemsPM.RUNE_HALLOWED.get()).parent("MASTER_RUNEWORKING").parent(Source.HALLOWED.getDiscoverKey())
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.RUNE_HALLOWED.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_INSIGHT", discipline).icon(ItemsPM.RUNE_INSIGHT.get()).parent("EXPERT_RUNEWORKING").parent("SHARD_SYNTHESIS")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.RUNE_INSIGHT.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_POWER", discipline).icon(ItemsPM.RUNE_POWER.get()).parent("MASTER_RUNEWORKING").parent("CRYSTAL_SYNTHESIS").parent(Source.BLOOD.getDiscoverKey())
            .parent(Source.INFERNAL.getDiscoverKey()).parent(Source.VOID.getDiscoverKey()).parent("RUNE_INSIGHT")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().sibling("t_sotu_research_power_rune").recipe(ItemsPM.RUNE_POWER.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_GRACE", discipline).icon(ItemsPM.RUNE_GRACE.get()).parent("SUPREME_RUNEWORKING").parent("CLUSTER_SYNTHESIS").parent(Source.HALLOWED.getDiscoverKey())
            .parent("RUNE_POWER")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 3).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.RUNE_GRACE.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNIC_GRINDSTONE", discipline).icon(ItemsPM.RUNIC_GRINDSTONE.get()).parent("RUNE_DISPEL")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.RUNIC_GRINDSTONE.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RECALL_STONE", discipline).icon(ItemsPM.RECALL_STONE.get()).parent("EXPERT_RUNEWORKING").parent("RUNE_SUMMON").parent("RUNE_SELF")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).requiredCraftTag(ItemTags.BEDS).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.RECALL_STONE.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNIC_TRIM", discipline).icon(ItemsPM.RUNIC_ARMOR_TRIM_SMITHING_TEMPLATE.get()).parent("EXPERT_RUNEWORKING").parent("IMBUED_WOOL").parent("RUNE_EARTH")
            .parent("RUNE_SEA").parent("RUNE_SKY").parent("RUNE_SUN").parent("RUNE_MOON")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).requiredItemStack(ItemsPM.RUNE_EARTH.get()).requiredItemStack(ItemsPM.RUNE_SEA.get())
                    .requiredItemStack(ItemsPM.RUNE_SKY.get()).requiredItemStack(ItemsPM.RUNE_SUN.get()).requiredItemStack(ItemsPM.RUNE_MOON.get()).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.RUNIC_ARMOR_TRIM_SMITHING_TEMPLATE.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("ENDERWARD", discipline).icon(ItemsPM.ENDERWARD.get()).parent("MASTER_RUNEWORKING").parent("RUNE_DISPEL").parent(Source.VOID.getDiscoverKey())
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.VOID, 3).recipe(ItemsPM.ENDERWARD.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("RECALL_STONE").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("SPELL_PAYLOAD_TELEPORT").build())
            .build(consumer);
    }

    protected void registerRitualEntries(Consumer<IFinishedResearchEntry> consumer) {
        String discipline = "RITUAL";
        ResearchEntryBuilder.entry("BASIC_RITUAL", discipline).icon("textures/research/discipline_ritual.png").parent("UNLOCK_RITUAL")
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.RITUAL_ALTAR.get()).recipe(ItemsPM.OFFERING_PEDESTAL.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("EXPERT_RITUAL", discipline).icon("textures/research/discipline_ritual.png").parent("RITUAL_CANDLES").parent("INCENSE_BRAZIER")
            .stage(ResearchStageBuilder.stage().requiredResearch("t_rituals_completed_expert").build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("MASTER_RITUAL", discipline).icon("textures/research/discipline_ritual.png").parent("RITUAL_LECTERN").parent("RITUAL_BELL")
            .stage(ResearchStageBuilder.stage().requiredResearch("t_discover_forbidden").requiredResearch("t_rituals_completed_master").build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("SUPREME_RITUAL", discipline).icon("textures/research/discipline_ritual.png").parent("BLOODLETTER").parent("SOUL_ANVIL")
            .stage(ResearchStageBuilder.stage().requiredResearch(Source.HALLOWED.getDiscoverKey()).requiredResearch("t_rituals_completed_supreme").reveals("SECRETS_OF_THE_UNIVERSE").build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("COMPLETE_RITUAL", discipline).hidden().icon("textures/research/discipline_ritual.png").finale(discipline)
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.EARTH, 1).attunement(Source.SEA, 1).attunement(Source.SKY, 1).attunement(Source.SUN, 1).attunement(Source.MOON, 1)
                    .attunement(Source.BLOOD, 1).attunement(Source.INFERNAL, 1).attunement(Source.VOID, 1).attunement(Source.HALLOWED, 1).build())
            .build(consumer);
        ResearchEntryBuilder.entry("MANAFRUIT", discipline).icon(ItemsPM.MANAFRUIT.get()).parent("BASIC_RITUAL").parent("MANA_SALTS").parent("RITUAL_CANDLES")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.MANAFRUIT.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RITUAL_CANDLES", discipline).icon(ItemsPM.RITUAL_CANDLE_WHITE.get()).parent("BASIC_RITUAL")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SUN, 1).recipe(ItemsPM.TALLOW.get()).recipe(PrimalMagick.MODID, "ritual_candle_white_from_tallow")
                    .recipe(ItemsPM.RITUAL_CANDLE_BLACK.get()).recipe(ItemsPM.RITUAL_CANDLE_BLUE.get()).recipe(ItemsPM.RITUAL_CANDLE_BROWN.get()).recipe(ItemsPM.RITUAL_CANDLE_CYAN.get())
                    .recipe(ItemsPM.RITUAL_CANDLE_GRAY.get()).recipe(ItemsPM.RITUAL_CANDLE_GREEN.get()).recipe(ItemsPM.RITUAL_CANDLE_LIGHT_BLUE.get()).recipe(ItemsPM.RITUAL_CANDLE_LIGHT_GRAY.get())
                    .recipe(ItemsPM.RITUAL_CANDLE_LIME.get()).recipe(ItemsPM.RITUAL_CANDLE_MAGENTA.get()).recipe(ItemsPM.RITUAL_CANDLE_ORANGE.get()).recipe(ItemsPM.RITUAL_CANDLE_PINK.get())
                    .recipe(ItemsPM.RITUAL_CANDLE_PURPLE.get()).recipe(ItemsPM.RITUAL_CANDLE_RED.get()).recipe(ItemsPM.RITUAL_CANDLE_WHITE.get()).recipe(ItemsPM.RITUAL_CANDLE_YELLOW.get())
                    .build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("HONEY_EXTRACTOR").recipe(PrimalMagick.MODID, "ritual_candle_white_from_beeswax").build())
            .build(consumer);
        ResearchEntryBuilder.entry("INCENSE_BRAZIER", discipline).icon(ItemsPM.INCENSE_BRAZIER.get()).parent("BASIC_RITUAL")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SKY, 1).recipe(ItemsPM.INCENSE_BRAZIER.get()).recipe(ItemsPM.INCENSE_STICK.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RITUAL_LECTERN", discipline).icon(ItemsPM.RITUAL_LECTERN.get()).parent("EXPERT_RITUAL")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.MOON, 1).recipe(ItemsPM.RITUAL_LECTERN.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RITUAL_BELL", discipline).icon(ItemsPM.RITUAL_BELL.get()).parent("EXPERT_RITUAL")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SEA, 1).recipe(ItemsPM.RITUAL_BELL.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("BLOODLETTER", discipline).icon(ItemsPM.BLOODLETTER.get()).parent("MASTER_RITUAL").parent("t_discover_blood")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.BLOOD, 1).recipe(ItemsPM.BLOODLETTER.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SOUL_ANVIL", discipline).icon(ItemsPM.SOUL_ANVIL.get()).parent("MASTER_RITUAL").parent("HEXIUM").parent("SPELL_PAYLOAD_DRAIN_SOUL")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.INFERNAL, 1).recipe(ItemsPM.SOUL_ANVIL.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("CELESTIAL_HARP", discipline).icon(ItemsPM.CELESTIAL_HARP.get()).parent("SUPREME_RITUAL").parent(Source.HALLOWED.getDiscoverKey())
            .stage(ResearchStageBuilder.stage().requiredItemStack(Items.NOTE_BLOCK).requiredItemStack(Items.JUKEBOX).requiredKnowledge(KnowledgeType.THEORY, 3).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.HALLOWED, 1).recipe(ItemsPM.CELESTIAL_HARP.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_CORE_PRIMAL", discipline).icon(ItemsPM.PRIMAL_WAND_CORE_ITEM.get()).parent("EXPERT_RITUAL").parent("WAND_CORE_OBSIDIAN")
            .parent("WAND_CORE_CORAL").parent("WAND_CORE_BAMBOO").parent("WAND_CORE_SUNWOOD").parent("WAND_CORE_MOONWOOD").parent("MANA_SALTS").parent("RITUAL_CANDLES")
            .parent("RITUAL_LECTERN").parent("RITUAL_BELL")
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.HEARTWOOD_WAND_CORE_ITEM.get()).requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.EARTH, 3).attunement(Source.SEA, 3).attunement(Source.SKY, 3).attunement(Source.SUN, 3).attunement(Source.MOON, 3)
                    .recipe(ItemsPM.PRIMAL_WAND_CORE_ITEM.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("STAVES").recipe(ItemsPM.PRIMAL_STAFF_CORE_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_CORE_DARK_PRIMAL", discipline).icon(ItemsPM.DARK_PRIMAL_WAND_CORE_ITEM.get()).parent("MASTER_RITUAL").parent("WAND_CORE_PRIMAL")
            .parent("WAND_CORE_BONE").parent("WAND_CORE_BLAZE_ROD").parent("WAND_CORE_PURPUR").parent("BLOODLETTER").parent("SOUL_ANVIL")
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.HEARTWOOD_WAND_CORE_ITEM.get()).requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.BLOOD, 4).attunement(Source.INFERNAL, 4).attunement(Source.VOID, 4).recipe(ItemsPM.DARK_PRIMAL_WAND_CORE_ITEM.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("STAVES").recipe(ItemsPM.DARK_PRIMAL_STAFF_CORE_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_CORE_PURE_PRIMAL", discipline).icon(ItemsPM.PURE_PRIMAL_WAND_CORE_ITEM.get()).parent("SUPREME_RITUAL").parent("WAND_CORE_DARK_PRIMAL")
            .parent("CELESTIAL_HARP")
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.HEARTWOOD_WAND_CORE_ITEM.get()).requiredKnowledge(KnowledgeType.THEORY, 3).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.HALLOWED, 5).recipe(ItemsPM.PURE_PRIMAL_WAND_CORE_ITEM.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("STAVES").recipe(ItemsPM.PURE_PRIMAL_STAFF_CORE_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("PIXIES", discipline).icon(ItemsPM.BASIC_EARTH_PIXIE.get()).parent("EXPERT_RITUAL").parent("MANA_SALTS").parent("SHARD_SYNTHESIS")
            .parent("RUNE_SUMMON").parent("RUNE_CREATURE").parent("INCENSE_BRAZIER").parent("RITUAL_BELL")
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.RUNE_SUMMON.get()).requiredItemStack(ItemsPM.RUNE_CREATURE.get()).requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.EARTH, 2).attunement(Source.SEA, 2).attunement(Source.SKY, 2).attunement(Source.SUN, 2).attunement(Source.MOON, 2)
                    .recipe(ItemsPM.BASIC_EARTH_PIXIE.get()).recipe(PrimalMagick.resource("pixie_basic_earth_revive"))
                    .recipe(ItemsPM.BASIC_SEA_PIXIE.get()).recipe(PrimalMagick.resource("pixie_basic_sea_revive"))
                    .recipe(ItemsPM.BASIC_SKY_PIXIE.get()).recipe(PrimalMagick.resource("pixie_basic_sky_revive"))
                    .recipe(ItemsPM.BASIC_SUN_PIXIE.get()).recipe(PrimalMagick.resource("pixie_basic_sun_revive"))
                    .recipe(ItemsPM.BASIC_MOON_PIXIE.get()).recipe(PrimalMagick.resource("pixie_basic_moon_revive")).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.BLOOD.getDiscoverKey()).attunement(Source.BLOOD, 2).recipe(ItemsPM.BASIC_BLOOD_PIXIE.get())
                    .recipe(PrimalMagick.resource("pixie_basic_blood_revive")).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.INFERNAL.getDiscoverKey()).attunement(Source.INFERNAL, 2).recipe(ItemsPM.BASIC_INFERNAL_PIXIE.get())
                    .recipe(PrimalMagick.resource("pixie_basic_infernal_revive")).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.VOID.getDiscoverKey()).attunement(Source.VOID, 2).recipe(ItemsPM.BASIC_VOID_PIXIE.get())
                    .recipe(PrimalMagick.resource("pixie_basic_void_revive")).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.HALLOWED.getDiscoverKey()).attunement(Source.HALLOWED, 2).recipe(ItemsPM.BASIC_HALLOWED_PIXIE.get())
                    .recipe(PrimalMagick.resource("pixie_basic_hallowed_revive")).build())
            .build(consumer);
        ResearchEntryBuilder.entry("GRAND_PIXIES", discipline).icon(ItemsPM.GRAND_EARTH_PIXIE.get()).parent("MASTER_RITUAL").parent("PIXIES").parent("CRYSTAL_SYNTHESIS")
            .parent("RUNE_POWER").parent("SOUL_ANVIL")
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.RUNE_INSIGHT.get()).requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.EARTH, 3).attunement(Source.SEA, 3).attunement(Source.SKY, 3).attunement(Source.SUN, 3).attunement(Source.MOON, 3)
                    .recipe(ItemsPM.GRAND_EARTH_PIXIE.get()).recipe(PrimalMagick.resource("pixie_grand_earth_revive"))
                    .recipe(ItemsPM.GRAND_SEA_PIXIE.get()).recipe(PrimalMagick.resource("pixie_grand_sea_revive"))
                    .recipe(ItemsPM.GRAND_SKY_PIXIE.get()).recipe(PrimalMagick.resource("pixie_grand_sky_revive"))
                    .recipe(ItemsPM.GRAND_SUN_PIXIE.get()).recipe(PrimalMagick.resource("pixie_grand_sun_revive"))
                    .recipe(ItemsPM.GRAND_MOON_PIXIE.get()).recipe(PrimalMagick.resource("pixie_grand_moon_revive")).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.BLOOD.getDiscoverKey()).attunement(Source.BLOOD, 3).recipe(ItemsPM.GRAND_BLOOD_PIXIE.get())
                    .recipe(PrimalMagick.resource("pixie_grand_blood_revive")).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.INFERNAL.getDiscoverKey()).attunement(Source.INFERNAL, 3).recipe(ItemsPM.GRAND_INFERNAL_PIXIE.get())
                    .recipe(PrimalMagick.resource("pixie_grand_infernal_revive")).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.VOID.getDiscoverKey()).attunement(Source.VOID, 3).recipe(ItemsPM.GRAND_VOID_PIXIE.get())
                    .recipe(PrimalMagick.resource("pixie_grand_void_revive")).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.HALLOWED.getDiscoverKey()).attunement(Source.HALLOWED, 3).recipe(ItemsPM.GRAND_HALLOWED_PIXIE.get())
                    .recipe(PrimalMagick.resource("pixie_grand_hallowed_revive")).build())
            .build(consumer);
        ResearchEntryBuilder.entry("MAJESTIC_PIXIES", discipline).icon(ItemsPM.MAJESTIC_EARTH_PIXIE.get()).parent("SUPREME_RITUAL").parent("GRAND_PIXIES").parent("CLUSTER_SYNTHESIS")
            .parent("CELESTIAL_HARP")
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.RUNE_POWER.get()).requiredKnowledge(KnowledgeType.THEORY, 3).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.EARTH, 4).attunement(Source.SEA, 4).attunement(Source.SKY, 4).attunement(Source.SUN, 4).attunement(Source.MOON, 4)
                    .recipe(ItemsPM.MAJESTIC_EARTH_PIXIE.get()).recipe(PrimalMagick.resource("pixie_majestic_earth_revive"))
                    .recipe(ItemsPM.MAJESTIC_SEA_PIXIE.get()).recipe(PrimalMagick.resource("pixie_majestic_sea_revive"))
                    .recipe(ItemsPM.MAJESTIC_SKY_PIXIE.get()).recipe(PrimalMagick.resource("pixie_majestic_sky_revive"))
                    .recipe(ItemsPM.MAJESTIC_SUN_PIXIE.get()).recipe(PrimalMagick.resource("pixie_majestic_sun_revive"))
                    .recipe(ItemsPM.MAJESTIC_MOON_PIXIE.get()).recipe(PrimalMagick.resource("pixie_majestic_moon_revive")).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.BLOOD.getDiscoverKey()).attunement(Source.BLOOD, 4).recipe(ItemsPM.MAJESTIC_BLOOD_PIXIE.get())
                    .recipe(PrimalMagick.resource("pixie_majestic_blood_revive")).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.INFERNAL.getDiscoverKey()).attunement(Source.INFERNAL, 4).recipe(ItemsPM.MAJESTIC_INFERNAL_PIXIE.get())
                    .recipe(PrimalMagick.resource("pixie_majestic_infernal_revive")).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.VOID.getDiscoverKey()).attunement(Source.VOID, 4).recipe(ItemsPM.MAJESTIC_VOID_PIXIE.get())
                    .recipe(PrimalMagick.resource("pixie_majestic_void_revive")).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.HALLOWED.getDiscoverKey()).attunement(Source.HALLOWED, 4).recipe(ItemsPM.MAJESTIC_HALLOWED_PIXIE.get())
                    .recipe(PrimalMagick.resource("pixie_majestic_hallowed_revive")).build())
            .build(consumer);
        ResearchEntryBuilder.entry("AMBROSIA", discipline).icon(ItemsPM.BASIC_EARTH_AMBROSIA.get()).parent("EXPERT_RITUAL").parent("ATTUNEMENTS").parent("MANAFRUIT")
            .parent("SHARD_SYNTHESIS").parent("RUNE_ABSORB").parent("RUNE_SELF").parent("RITUAL_LECTERN")
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.RUNE_ABSORB.get()).requiredItemStack(ItemsPM.RUNE_SELF.get()).requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.EARTH, 2).attunement(Source.SEA, 2).attunement(Source.SKY, 2).attunement(Source.SUN, 2).attunement(Source.MOON, 2)
                    .recipe(ItemsPM.BASIC_EARTH_AMBROSIA.get()).recipe(ItemsPM.BASIC_SEA_AMBROSIA.get()).recipe(ItemsPM.BASIC_SKY_AMBROSIA.get()).recipe(ItemsPM.BASIC_SUN_AMBROSIA.get())
                    .recipe(ItemsPM.BASIC_MOON_AMBROSIA.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.BLOOD.getDiscoverKey()).attunement(Source.BLOOD, 2).recipe(ItemsPM.BASIC_BLOOD_AMBROSIA.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.INFERNAL.getDiscoverKey()).attunement(Source.INFERNAL, 2).recipe(ItemsPM.BASIC_INFERNAL_AMBROSIA.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.VOID.getDiscoverKey()).attunement(Source.VOID, 2).recipe(ItemsPM.BASIC_VOID_AMBROSIA.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.HALLOWED.getDiscoverKey()).attunement(Source.HALLOWED, 2).recipe(ItemsPM.BASIC_HALLOWED_AMBROSIA.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("GREATER_AMBROSIA", discipline).icon(ItemsPM.GREATER_EARTH_AMBROSIA.get()).parent("MASTER_RITUAL").parent("AMBROSIA").parent("CRYSTAL_SYNTHESIS")
            .parent("RUNE_POWER").parent("BLOODLETTER")
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.RUNE_INSIGHT.get()).requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.EARTH, 3).attunement(Source.SEA, 3).attunement(Source.SKY, 3).attunement(Source.SUN, 3).attunement(Source.MOON, 3)
                    .recipe(ItemsPM.GREATER_EARTH_AMBROSIA.get()).recipe(ItemsPM.GREATER_SEA_AMBROSIA.get()).recipe(ItemsPM.GREATER_SKY_AMBROSIA.get()).recipe(ItemsPM.GREATER_SUN_AMBROSIA.get())
                    .recipe(ItemsPM.GREATER_MOON_AMBROSIA.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.BLOOD.getDiscoverKey()).attunement(Source.BLOOD, 3).recipe(ItemsPM.GREATER_BLOOD_AMBROSIA.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.INFERNAL.getDiscoverKey()).attunement(Source.INFERNAL, 3).recipe(ItemsPM.GREATER_INFERNAL_AMBROSIA.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.VOID.getDiscoverKey()).attunement(Source.VOID, 3).recipe(ItemsPM.GREATER_VOID_AMBROSIA.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.HALLOWED.getDiscoverKey()).attunement(Source.HALLOWED, 3).recipe(ItemsPM.GREATER_HALLOWED_AMBROSIA.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SUPREME_AMBROSIA", discipline).icon(ItemsPM.SUPREME_EARTH_AMBROSIA.get()).parent("SUPREME_RITUAL").parent("GREATER_AMBROSIA").parent("CLUSTER_SYNTHESIS")
            .parent("CELESTIAL_HARP")
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.RUNE_POWER.get()).requiredKnowledge(KnowledgeType.THEORY, 3).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.EARTH, 4).attunement(Source.SEA, 4).attunement(Source.SKY, 4).attunement(Source.SUN, 4).attunement(Source.MOON, 4)
                    .recipe(ItemsPM.SUPREME_EARTH_AMBROSIA.get()).recipe(ItemsPM.SUPREME_SEA_AMBROSIA.get()).recipe(ItemsPM.SUPREME_SKY_AMBROSIA.get()).recipe(ItemsPM.SUPREME_SUN_AMBROSIA.get())
                    .recipe(ItemsPM.SUPREME_MOON_AMBROSIA.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.BLOOD.getDiscoverKey()).attunement(Source.BLOOD, 4).recipe(ItemsPM.SUPREME_BLOOD_AMBROSIA.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.INFERNAL.getDiscoverKey()).attunement(Source.INFERNAL, 4).recipe(ItemsPM.SUPREME_INFERNAL_AMBROSIA.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.VOID.getDiscoverKey()).attunement(Source.VOID, 4).recipe(ItemsPM.SUPREME_VOID_AMBROSIA.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.HALLOWED.getDiscoverKey()).attunement(Source.HALLOWED, 4).recipe(ItemsPM.SUPREME_HALLOWED_AMBROSIA.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("FLYING_CARPET", discipline).icon(ItemsPM.FLYING_CARPET.get()).parent("MASTER_RITUAL").parent("CRYSTAL_SYNTHESIS").parent("MANA_SALTS").parent("RUNE_PROJECT")
            .parent("RUNE_ITEM").parent("RUNE_POWER").parent("INCENSE_BRAZIER").parent("RITUAL_LECTERN").parent("RITUAL_BELL")
            .stage(ResearchStageBuilder.stage().requiredResearch("t_flying_creature").requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SKY, 3).recipe(ItemsPM.FLYING_CARPET.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("CLEANSING_RITE", discipline).icon(ItemsPM.SANGUINE_CORE_BLANK.get()).parent("MASTER_RITUAL").parent("SANGUINE_CRUCIBLE").parent("RUNE_SUMMON")
            .parent("RUNE_SELF").parent("RUNE_POWER").parent("RITUAL_CANDLES").parent("RITUAL_BELL").parent("RITUAL_LECTERN").parent("BLOODLETTER").parent("SOUL_ANVIL")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.SANGUINE_CORE_INNER_DEMON.get()).sibling("t_sotu_research_cleansing_rite").build())
            .build(consumer);
        ResearchEntryBuilder.entry("PRIMAL_SHOVEL", discipline).icon(ItemsPM.PRIMAL_SHOVEL.get()).parent("EXPERT_RITUAL").parent("PRIMALITE").parent("SHARD_SYNTHESIS").parent("MANA_SALTS")
            .parent("RUNE_EARTH").parent("RITUAL_CANDLES").parent("RITUAL_BELL")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.PRIMAL_SHOVEL.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("MASTER_RUNEWORKING").requiredResearch("RUNE_PROJECT").requiredResearch("RUNE_AREA").requiredResearch("RUNE_EARTH")
                    .sibling(SimpleResearchKey.parseRuneEnchantment(EnchantmentsPM.REVERBERATION.get())).sibling("UNLOCK_RUNE_ENCHANTMENTS").build())
            .build(consumer);
        ResearchEntryBuilder.entry("PRIMAL_FISHING_ROD", discipline).icon(ItemsPM.PRIMAL_FISHING_ROD.get()).parent("EXPERT_RITUAL").parent("PRIMALITE").parent("SHARD_SYNTHESIS")
            .parent("MANA_SALTS").parent("RUNE_SEA").parent("RITUAL_BELL").parent("RITUAL_LECTERN")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.PRIMAL_FISHING_ROD.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("MASTER_RUNEWORKING").requiredResearch("RUNE_SUMMON").requiredResearch("RUNE_AREA").requiredResearch("RUNE_SEA")
                    .sibling(SimpleResearchKey.parseRuneEnchantment(EnchantmentsPM.BOUNTY.get())).sibling("UNLOCK_RUNE_ENCHANTMENTS").build())
            .build(consumer);
        ResearchEntryBuilder.entry("PRIMAL_AXE", discipline).icon(ItemsPM.PRIMAL_AXE.get()).parent("EXPERT_RITUAL").parent("PRIMALITE").parent("SHARD_SYNTHESIS").parent("MANA_SALTS")
            .parent("RUNE_SKY").parent("RITUAL_BELL").parent("INCENSE_BRAZIER")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.PRIMAL_AXE.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("MASTER_RUNEWORKING").requiredResearch("RUNE_PROJECT").requiredResearch("RUNE_AREA").requiredResearch("RUNE_SKY")
                    .sibling(SimpleResearchKey.parseRuneEnchantment(EnchantmentsPM.DISINTEGRATION.get())).sibling("UNLOCK_RUNE_ENCHANTMENTS").build())
            .build(consumer);
        ResearchEntryBuilder.entry("PRIMAL_HOE", discipline).icon(ItemsPM.PRIMAL_HOE.get()).parent("EXPERT_RITUAL").parent("PRIMALITE").parent("SHARD_SYNTHESIS").parent("MANA_SALTS")
            .parent("RUNE_SUN").parent("RITUAL_CANDLES").parent("INCENSE_BRAZIER")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.PRIMAL_HOE.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("MASTER_RUNEWORKING").requiredResearch("RUNE_SUMMON").requiredResearch("RUNE_CREATURE").requiredResearch("RUNE_SUN")
                    .sibling(SimpleResearchKey.parseRuneEnchantment(EnchantmentsPM.VERDANT.get())).sibling("UNLOCK_RUNE_ENCHANTMENTS").build())
            .build(consumer);
        ResearchEntryBuilder.entry("PRIMAL_PICKAXE", discipline).icon(ItemsPM.PRIMAL_PICKAXE.get()).parent("EXPERT_RITUAL").parent("PRIMALITE").parent("SHARD_SYNTHESIS").parent("MANA_SALTS")
            .parent("RUNE_MOON").parent("RITUAL_LECTERN").parent("INCENSE_BRAZIER")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.PRIMAL_PICKAXE.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("MASTER_RUNEWORKING").requiredResearch("RUNE_SUMMON").requiredResearch("RUNE_ITEM").requiredResearch("RUNE_MOON")
                    .sibling(SimpleResearchKey.parseRuneEnchantment(EnchantmentsPM.LUCKY_STRIKE.get())).sibling("UNLOCK_RUNE_ENCHANTMENTS").build())
            .build(consumer);
        ResearchEntryBuilder.entry("FORBIDDEN_TRIDENT", discipline).icon(ItemsPM.FORBIDDEN_TRIDENT.get()).parent(Source.BLOOD.getDiscoverKey()).parent("MASTER_RITUAL").parent("HEXIUM")
            .parent("SHARD_SYNTHESIS").parent("MANA_SALTS").parent("RUNE_BLOOD").parent("BLOODLETTER")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.FORBIDDEN_TRIDENT.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("MASTER_RUNEWORKING").requiredResearch("RUNE_PROJECT").requiredResearch("RUNE_CREATURE").requiredResearch("RUNE_BLOOD")
                    .sibling(SimpleResearchKey.parseRuneEnchantment(EnchantmentsPM.RENDING.get())).sibling("UNLOCK_RUNE_ENCHANTMENTS").build())
            .build(consumer);
        ResearchEntryBuilder.entry("FORBIDDEN_BOW", discipline).icon(ItemsPM.FORBIDDEN_BOW.get()).parent(Source.INFERNAL.getDiscoverKey()).parent("MASTER_RITUAL").parent("HEXIUM")
            .parent("SHARD_SYNTHESIS").parent("MANA_SALTS").parent("RUNE_INFERNAL").parent("SOUL_ANVIL")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.FORBIDDEN_BOW.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("MASTER_RUNEWORKING").requiredResearch("RUNE_ABSORB").requiredResearch("RUNE_CREATURE").requiredResearch("RUNE_INFERNAL")
                    .sibling(SimpleResearchKey.parseRuneEnchantment(EnchantmentsPM.SOULPIERCING.get())).sibling("UNLOCK_RUNE_ENCHANTMENTS").build())
            .build(consumer);
        ResearchEntryBuilder.entry("FORBIDDEN_SWORD", discipline).icon(ItemsPM.FORBIDDEN_SWORD.get()).parent(Source.VOID.getDiscoverKey()).parent("MASTER_RITUAL").parent("HEXIUM")
            .parent("SHARD_SYNTHESIS").parent("MANA_SALTS").parent("RUNE_VOID").parent("BLOODLETTER").parent("SOUL_ANVIL")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.FORBIDDEN_SWORD.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("MASTER_RUNEWORKING").requiredResearch("RUNE_SUMMON").requiredResearch("RUNE_ITEM").requiredResearch("RUNE_VOID")
                    .sibling(SimpleResearchKey.parseRuneEnchantment(EnchantmentsPM.ESSENCE_THIEF.get())).sibling("UNLOCK_RUNE_ENCHANTMENTS").build())
            .build(consumer);
        ResearchEntryBuilder.entry("SACRED_SHIELD", discipline).icon(ItemsPM.SACRED_SHIELD.get()).parent(Source.HALLOWED.getDiscoverKey()).parent("SUPREME_RITUAL").parent("HALLOWSTEEL")
            .parent("SHARD_SYNTHESIS").parent("MANA_SALTS").parent("RUNE_HALLOWED").parent("CELESTIAL_HARP")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 3).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.SACRED_SHIELD.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("MASTER_RUNEWORKING").requiredResearch("RUNE_PROTECT").requiredResearch("RUNE_SELF").requiredResearch("RUNE_HALLOWED")
                    .sibling(SimpleResearchKey.parseRuneEnchantment(EnchantmentsPM.BULWARK.get())).sibling("UNLOCK_RUNE_ENCHANTMENTS").build())
            .build(consumer);
        ResearchEntryBuilder.entry("DREAM_VISION_TALISMAN", discipline).icon(ItemsPM.DREAM_VISION_TALISMAN.get()).parent("EXPERT_RITUAL").parent("RITUAL_CANDLES").parent("INCENSE_BRAZIER")
            .parent("RITUAL_LECTERN")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).requiredResearch("t_observations_made_expert").build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.DREAM_VISION_TALISMAN.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("DOWSING_ROD", discipline).icon(ItemsPM.DOWSING_ROD.get()).parent("BASIC_RITUAL")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.DOWSING_ROD.get()).build())
            .build(consumer);
        // TODO Re-add Rosetta Arcana research once the book project is ready to deploy
        ResearchEntryBuilder.entry("HYDROMELON", discipline).icon(ItemsPM.HYDROMELON_SLICE.get()).parent("EXPERT_RITUAL").parent("RITUAL_CANDLES").parent("RITUAL_BELL").parent("RUNE_SEA")
            .parent("SHARD_SYNTHESIS")
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.SUNWOOD_SAPLING.get()).requiredItemStack(ItemsPM.MOONWOOD_SAPLING.get()).requiredItemStack(Items.MELON)
                    .requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SEA, 2).recipe(PrimalMagick.resource("hydromelon_seeds_from_ritual")).recipe(ItemsPM.HYDROMELON.get())
                    .recipe(ItemsPM.HYDROMELON_SEEDS.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("BLOOD_ROSE", discipline).icon(ItemsPM.BLOOD_ROSE.get()).parent(Source.BLOOD.getDiscoverKey()).parent("EXPERT_RITUAL").parent("HYDROMELON").parent("BLOODLETTER")
            .parent("RUNE_BLOOD")
            .stage(ResearchStageBuilder.stage().requiredItemStack(Items.ROSE_BUSH).requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.BLOOD, 2).recipe(ItemsPM.BLOOD_ROSE.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("EMBERFLOWER", discipline).icon(ItemsPM.EMBERFLOWER.get()).parent(Source.INFERNAL.getDiscoverKey()).parent("EXPERT_RITUAL").parent("HYDROMELON").parent("BLOODLETTER")
            .parent("RUNE_INFERNAL")
            .stage(ResearchStageBuilder.stage().requiredItemStack(Items.SUNFLOWER).requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.INFERNAL, 2).recipe(ItemsPM.EMBERFLOWER.get()).build())
            .build(consumer);
    }

    protected void registerMagitechEntries(Consumer<IFinishedResearchEntry> consumer) {
        String discipline = "MAGITECH";
        ResearchEntryBuilder.entry("BASIC_MAGITECH", discipline).icon("textures/research/discipline_magitech.png").parent("UNLOCK_MAGITECH")
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.MAGITECH_PARTS_BASIC.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("EXPERT_MAGITECH", discipline).icon("textures/research/discipline_magitech.png").parent("HONEY_EXTRACTOR").parent("SEASCRIBE_PEN")
            .stage(ResearchStageBuilder.stage().requiredResearch("b_crafted_magitech_expert").requiredResearch("b_scan_primalite", true).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.MAGITECH_PARTS_ENCHANTED.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("MASTER_MAGITECH", discipline).icon("textures/research/discipline_magitech.png").parent("ARCANOMETER").parent("PRIMALITE_GOLEM")
            .stage(ResearchStageBuilder.stage().requiredResearch("t_discover_forbidden").requiredResearch("b_crafted_magitech_master").requiredResearch("b_scan_hexium", true).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.MAGITECH_PARTS_FORBIDDEN.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SUPREME_MAGITECH", discipline).icon("textures/research/discipline_magitech.png").parent("HEXIUM_GOLEM")
            .stage(ResearchStageBuilder.stage().requiredResearch(Source.HALLOWED.getDiscoverKey()).requiredResearch("b_crafted_magitech_supreme").requiredResearch("b_scan_hallowsteel", true)
                    .reveals("SECRETS_OF_THE_UNIVERSE").build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.MAGITECH_PARTS_HEAVENLY.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("COMPLETE_MAGITECH", discipline).hidden().icon("textures/research/discipline_magitech.png").finale(discipline)
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.EARTH, 1).attunement(Source.SEA, 1).attunement(Source.SKY, 1).attunement(Source.SUN, 1).attunement(Source.MOON, 1)
                    .attunement(Source.BLOOD, 1).attunement(Source.INFERNAL, 1).attunement(Source.VOID, 1).attunement(Source.HALLOWED, 1).build())
            .build(consumer);
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
        ResearchEntryBuilder.entry("CONCOCTING_TINCTURES", discipline).icon(ItemsPM.CONCOCTER.get()).parent("EXPERT_MAGITECH").parent("SKYGLASS").parent(Source.INFERNAL.getDiscoverKey())
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
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.BLOOD.getDiscoverKey()).recipe(PrimalMagick.resource("healing_tincture"))
                    .recipe(PrimalMagick.resource("strong_healing_tincture")).recipe(PrimalMagick.resource("regeneration_tincture"))
                    .recipe(PrimalMagick.resource("long_regeneration_tincture")).recipe(PrimalMagick.resource("strong_regeneration_tincture")).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.INFERNAL.getDiscoverKey()).recipe(PrimalMagick.resource("fire_resistance_tincture"))
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
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.BLOOD.getDiscoverKey()).recipe(PrimalMagick.resource("healing_philter"))
                    .recipe(PrimalMagick.resource("strong_healing_philter")).recipe(PrimalMagick.resource("regeneration_philter"))
                    .recipe(PrimalMagick.resource("long_regeneration_philter")).recipe(PrimalMagick.resource("strong_regeneration_philter")).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.INFERNAL.getDiscoverKey()).recipe(PrimalMagick.resource("fire_resistance_philter"))
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
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.BLOOD.getDiscoverKey()).recipe(PrimalMagick.resource("healing_elixir"))
                    .recipe(PrimalMagick.resource("strong_healing_elixir")).recipe(PrimalMagick.resource("regeneration_elixir"))
                    .recipe(PrimalMagick.resource("long_regeneration_elixir")).recipe(PrimalMagick.resource("strong_regeneration_elixir")).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.INFERNAL.getDiscoverKey()).recipe(PrimalMagick.resource("fire_resistance_elixir"))
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
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.BLOOD.getDiscoverKey()).recipe(PrimalMagick.resource("healing_bomb"))
                    .recipe(PrimalMagick.resource("strong_healing_bomb")).recipe(PrimalMagick.resource("regeneration_bomb"))
                    .recipe(PrimalMagick.resource("long_regeneration_bomb")).recipe(PrimalMagick.resource("strong_regeneration_bomb"))
                    .recipe(PrimalMagick.resource("harming_bomb")).recipe(PrimalMagick.resource("strong_harming_bomb"))
                    .recipe(PrimalMagick.resource("poison_bomb")).recipe(PrimalMagick.resource("long_poison_bomb"))
                    .recipe(PrimalMagick.resource("strong_poison_bomb")).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.INFERNAL.getDiscoverKey()).recipe(PrimalMagick.resource("fire_resistance_bomb"))
                    .recipe(PrimalMagick.resource("long_fire_resistance_bomb")).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.VOID.getDiscoverKey()).recipe(PrimalMagick.resource("slowness_bomb"))
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
            .stage(ResearchStageBuilder.stage().attunement(Source.EARTH, 3).recipe(ItemsPM.DISSOLUTION_CHAMBER.get()).recipe(PrimalMagick.resource("iron_grit_from_dissolving_ore"))
                    .recipe(PrimalMagick.resource("iron_grit_from_dissolving_raw_metal")).recipe(PrimalMagick.resource("gold_grit_from_dissolving_ore"))
                    .recipe(PrimalMagick.resource("gold_grit_from_dissolving_raw_metal")).recipe(PrimalMagick.resource("copper_grit_from_dissolving_ore"))
                    .recipe(PrimalMagick.resource("copper_grit_from_dissolving_raw_metal")).recipe(PrimalMagick.resource("cobblestone_from_dissolving_surface_stone"))
                    .recipe(PrimalMagick.resource("cobbled_deepslate_from_dissolving_deep_stone")).recipe(PrimalMagick.resource("gravel_from_dissolving_cobblestone"))
                    .recipe(PrimalMagick.resource("sand_from_dissolving_gravel")).recipe(PrimalMagick.resource("bone_meal_from_dissolving_bone"))
                    .recipe(PrimalMagick.resource("blaze_powder_from_dissolving_blaze_rod")).recipe(PrimalMagick.resource("string_from_dissolving_wool"))
                    .recipe(PrimalMagick.resource("quartz_from_dissolving_quartz_block")).recipe(PrimalMagick.resource("glowstone_dust_from_dissolving_glowstone_block"))
                    .recipe(PrimalMagick.resource("rock_salt_from_dissolving_rock_salt_ore")).recipe(PrimalMagick.resource("refined_salt_from_dissolving_rock_salt"))
                    .recipe(PrimalMagick.resource("netherite_scrap_from_dissolving_ancient_debris")).build())
            .build(consumer);
        ResearchEntryBuilder.entry("ZEPHYR_ENGINE", discipline).icon(ItemsPM.ZEPHYR_ENGINE.get()).parent("EXPERT_MAGITECH").parent("PRIMALITE").parent("SHARD_SYNTHESIS")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).requiredItemStack(Items.PISTON).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SKY, 2).recipe(ItemsPM.ZEPHYR_ENGINE.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("VOID_TURBINE", discipline).icon(ItemsPM.VOID_TURBINE.get()).parent("MASTER_MAGITECH").parent("HEXIUM").parent("SHARD_SYNTHESIS")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).requiredItemStack(Items.STICKY_PISTON).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.VOID, 3).recipe(ItemsPM.VOID_TURBINE.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("INFERNAL_FURNACE", discipline).icon(ItemsPM.INFERNAL_FURNACE.get()).parent("MASTER_MAGITECH").parent("HEXIUM").parent("CRYSTAL_SYNTHESIS")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.INFERNAL, 3).recipe(ItemsPM.INFERNAL_FURNACE.get()).build())
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

    protected void registerScanEntries(Consumer<IFinishedResearchEntry> consumer) {
        String discipline = "SCANS";
        ResearchEntryBuilder.entry("RAW_MARBLE", discipline).hidden().icon(ItemsPM.MARBLE_RAW.get()).parent("UNLOCK_SCANS")
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.MARBLE_SLAB.get()).recipe(ItemsPM.MARBLE_STAIRS.get()).recipe(ItemsPM.MARBLE_WALL.get()).recipe(ItemsPM.MARBLE_BRICKS.get())
                    .recipe(ItemsPM.MARBLE_BRICK_SLAB.get()).recipe(ItemsPM.MARBLE_BRICK_STAIRS.get()).recipe(ItemsPM.MARBLE_BRICK_WALL.get()).recipe(ItemsPM.MARBLE_PILLAR.get())
                    .recipe(ItemsPM.MARBLE_CHISELED.get()).recipe(ItemsPM.MARBLE_TILES.get()).recipe(ItemsPM.MARBLE_RUNED.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("HALLOWED_ORB", discipline).hidden().icon(ItemsPM.HALLOWED_ORB.get()).parent("UNLOCK_SCANS")
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.HALLOWOOD_SAPLING.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("HALLOWOOD_TREES", discipline).hidden().icon(ItemsPM.HALLOWOOD_SAPLING.get()).parent("UNLOCK_SCANS")
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.HALLOWOOD_WOOD.get()).recipe(ItemsPM.STRIPPED_HALLOWOOD_WOOD.get()).recipe(ItemsPM.HALLOWOOD_PLANKS.get())
                    .recipe(ItemsPM.HALLOWOOD_SLAB.get()).recipe(ItemsPM.HALLOWOOD_STAIRS.get()).recipe(ItemsPM.HALLOWOOD_PILLAR.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SUNWOOD_TREES", discipline).hidden().icon(ItemsPM.SUNWOOD_SAPLING.get()).parent("UNLOCK_SCANS")
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.SUNWOOD_WOOD.get()).recipe(ItemsPM.STRIPPED_SUNWOOD_WOOD.get()).recipe(ItemsPM.SUNWOOD_PLANKS.get())
                    .recipe(ItemsPM.SUNWOOD_SLAB.get()).recipe(ItemsPM.SUNWOOD_STAIRS.get()).recipe(ItemsPM.SUNWOOD_PILLAR.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("MOONWOOD_TREES", discipline).hidden().icon(ItemsPM.MOONWOOD_SAPLING.get()).parent("UNLOCK_SCANS")
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.MOONWOOD_WOOD.get()).recipe(ItemsPM.STRIPPED_MOONWOOD_WOOD.get()).recipe(ItemsPM.MOONWOOD_PLANKS.get())
                    .recipe(ItemsPM.MOONWOOD_SLAB.get()).recipe(ItemsPM.MOONWOOD_STAIRS.get()).recipe(ItemsPM.MOONWOOD_PILLAR.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("ROCK_SALT", discipline).hidden().icon(ItemsPM.REFINED_SALT.get()).parent("UNLOCK_SCANS")
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.REFINED_SALT.get()).recipe(PrimalMagick.resource("rock_salt_from_smelting"))
                    .recipe(ItemsPM.SALT_BLOCK.get()).recipe(PrimalMagick.resource("refined_salt_from_salt_block"))
                    .recipe(ItemsPM.SALTED_BAKED_POTATO.get()).recipe(ItemsPM.SALTED_BEETROOT_SOUP.get()).recipe(ItemsPM.SALTED_COOKED_BEEF.get())
                    .recipe(ItemsPM.SALTED_COOKED_CHICKEN.get()).recipe(ItemsPM.SALTED_COOKED_COD.get()).recipe(ItemsPM.SALTED_COOKED_MUTTON.get()).recipe(ItemsPM.SALTED_COOKED_PORKCHOP.get())
                    .recipe(ItemsPM.SALTED_COOKED_RABBIT.get()).recipe(ItemsPM.SALTED_COOKED_SALMON.get()).recipe(ItemsPM.SALTED_MUSHROOM_STEW.get()).recipe(ItemsPM.SALTED_RABBIT_STEW.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("ALCHEMICAL_WASTE", discipline).hidden().icon(ItemsPM.ALCHEMICAL_WASTE.get()).parent("UNLOCK_SCANS")
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("QUARTZ", discipline).hidden().icon(Items.QUARTZ).parent("UNLOCK_SCANS")
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.QUARTZ_NUGGET.get()).recipe(PrimalMagick.resource("quartz_from_nuggets"))
                    .recipe(PrimalMagick.resource("quartz_from_smelting")).build())
            .build(consumer);
        ResearchEntryBuilder.entry("INNER_DEMON", discipline).hidden().icon(ItemsPM.SANGUINE_CORE_BLANK.get()).parent("UNLOCK_SCANS")
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("BOOKSHELF", discipline).hidden().icon(Items.BOOKSHELF).parent("UNLOCK_SCANS")
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("BEEHIVE", discipline).hidden().icon(Items.BEEHIVE).parent("UNLOCK_SCANS")
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("BEACON", discipline).hidden().icon(Items.BEACON).parent("UNLOCK_SCANS")
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("DRAGON_EGG", discipline).hidden().icon(Items.DRAGON_EGG).parent("UNLOCK_SCANS")
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("DRAGON_HEAD", discipline).hidden().icon(Items.DRAGON_HEAD).parent("UNLOCK_SCANS")
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("MYSTICAL_RELIC", discipline).hidden().icon(ItemsPM.MYSTICAL_RELIC.get()).parent("UNLOCK_SCANS")
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.MYSTICAL_RELIC.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("HUMMING_ARTIFACT", discipline).hidden().icon(ItemsPM.HUMMING_ARTIFACT_UNATTUNED.get()).parent("UNLOCK_SCANS")
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.HUMMING_ARTIFACT_EARTH.get()).recipe(ItemsPM.HUMMING_ARTIFACT_SEA.get()).recipe(ItemsPM.HUMMING_ARTIFACT_SKY.get())
                    .recipe(ItemsPM.HUMMING_ARTIFACT_SUN.get()).recipe(ItemsPM.HUMMING_ARTIFACT_MOON.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.BLOOD.getDiscoverKey()).recipe(ItemsPM.HUMMING_ARTIFACT_BLOOD.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.INFERNAL.getDiscoverKey()).recipe(ItemsPM.HUMMING_ARTIFACT_INFERNAL.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.VOID.getDiscoverKey()).recipe(ItemsPM.HUMMING_ARTIFACT_VOID.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.HALLOWED.getDiscoverKey()).recipe(ItemsPM.HUMMING_ARTIFACT_HALLOWED.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("TREEFOLK", discipline).hidden().icon(ItemsPM.HEARTWOOD.get()).parent("UNLOCK_SCANS")
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
    }
    
    @Override
    public String getName() {
        return "Primal Magick Grimoire Research";
    }
}
