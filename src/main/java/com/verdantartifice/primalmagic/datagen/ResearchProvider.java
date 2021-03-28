package com.verdantartifice.primalmagic.datagen;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge.KnowledgeType;
import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

public class ResearchProvider implements IDataProvider {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    protected final DataGenerator generator;
    
    public ResearchProvider(DataGenerator generator) {
        this.generator = generator;
    }

    @Override
    public void act(DirectoryCache cache) throws IOException {
        Path path = this.generator.getOutputFolder();
        Map<ResourceLocation, IFinishedResearchEntry> map = new HashMap<>();
        this.registerEntries((research) -> {
            if (map.put(research.getId(), research) != null) {
                PrimalMagic.LOGGER.debug("Duplicate research entry in data generation: " + research.getId().toString());
            }
        });
        for (Map.Entry<ResourceLocation, IFinishedResearchEntry> entry : map.entrySet()) {
            IFinishedResearchEntry research = entry.getValue();
            this.saveEntry(cache, research.getEntryJson(), path.resolve("data/" + entry.getKey().getNamespace() + "/grimoire/" + entry.getKey().getPath() + ".json"));
        }
    }

    private void saveEntry(DirectoryCache cache, JsonObject json, Path path) {
        try {
            String jsonStr = GSON.toJson((JsonElement)json);
            String hash = HASH_FUNCTION.hashUnencodedChars(jsonStr).toString();
            if (!Objects.equals(cache.getPreviousHash(path), hash) || !Files.exists(path)) {
                Files.createDirectories(path.getParent());
                try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                    writer.write(jsonStr);
                }
            }
            cache.recordHash(path, hash);
        } catch (IOException e) {
            PrimalMagic.LOGGER.error("Couldn't save research entry {}", path, e);
        }
    }
    
    protected void registerEntries(Consumer<IFinishedResearchEntry> consumer) {
        this.registerBasicsEntries(consumer);
        this.registerAlchemyEntries(consumer);
        this.registerSorceryEntries(consumer);
        this.registerRuneworkingEntries(consumer);
        this.registerRitualEntries(consumer);
        this.registerMagitechEntries(consumer);
        this.registerScanEntries(consumer);
    }

    protected void registerBasicsEntries(Consumer<IFinishedResearchEntry> consumer) {
        ResearchEntryBuilder.entry("FIRST_STEPS", "primalmagic.research.first_steps.title", "BASICS")
            .stage(ResearchStageBuilder.stage("primalmagic.research.first_steps.text.stage.1").requiredCraftStack(ItemsPM.ARCANE_WORKBENCH.get()).recipe("mundane_wand").build())
            .stage(ResearchStageBuilder.stage("primalmagic.research.first_steps.text.stage.2").recipe(ItemsPM.MUNDANE_WAND.get()).recipe(ItemsPM.MARBLE_ENCHANTED.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("KNOWLEDGE_TYPES", "primalmagic.research.knowledge_types.title", "BASICS").parent("FIRST_STEPS")
            .stage(ResearchStageBuilder.stage("primalmagic.research.knowledge_types.text.stage.1").build())
            .build(consumer);
        ResearchEntryBuilder.entry("THEORYCRAFTING", "primalmagic.research.theorycrafting.title", "BASICS").parent("KNOWLEDGE_TYPES")
            .stage(ResearchStageBuilder.stage("primalmagic.research.theorycrafting.text.stage.1").requiredItemStack(Items.STICK).build())
            .stage(ResearchStageBuilder.stage("primalmagic.research.theorycrafting.text.stage.2").recipe(ItemsPM.RESEARCH_TABLE.get()).recipe(ItemsPM.ENCHANTED_INK.get()).recipe(ItemsPM.ENCHANTED_INK_AND_QUILL.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("ATTUNEMENTS", "primalmagic.research.attunements.title", "BASICS").parent("FIRST_STEPS")
            .stage(ResearchStageBuilder.stage("primalmagic.research.attunements.text.stage.1").requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage("primalmagic.research.attunements.text.stage.2").build())
            .build(consumer);
        ResearchEntryBuilder.entry("UNLOCK_MANAWEAVING", "primalmagic.research.unlock_manaweaving.title", "BASICS").parent("FIRST_STEPS")
            .stage(ResearchStageBuilder.stage("primalmagic.research.unlock_manaweaving.text.stage.1").build())
            .build(consumer);
        ResearchEntryBuilder.entry("UNLOCK_ALCHEMY", "primalmagic.research.unlock_alchemy.title", "BASICS").parent("UNLOCK_MANAWEAVING")
            .stage(ResearchStageBuilder.stage("primalmagic.research.unlock_alchemy.text.stage.1").build())
            .build(consumer);
        ResearchEntryBuilder.entry("UNLOCK_SORCERY", "primalmagic.research.unlock_sorcery.title", "BASICS").parent("UNLOCK_MANAWEAVING")
            .stage(ResearchStageBuilder.stage("primalmagic.research.unlock_sorcery.text.stage.1").requiredItemStack(Items.STICK).build())
            .stage(ResearchStageBuilder.stage("primalmagic.research.unlock_sorcery.text.stage.2").build())
            .build(consumer);
        ResearchEntryBuilder.entry("UNLOCK_RUNEWORKING", "primalmagic.research.unlock_runeworking.title", "BASICS").parent("UNLOCK_ALCHEMY")
            .stage(ResearchStageBuilder.stage("primalmagic.research.unlock_runeworking.text.stage.1").requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage("primalmagic.research.unlock_runeworking.text.stage.2").build())
            .build(consumer);
        ResearchEntryBuilder.entry("UNLOCK_RITUAL", "primalmagic.research.unlock_ritual.title", "BASICS").parent("UNLOCK_RUNEWORKING")
            .stage(ResearchStageBuilder.stage("primalmagic.research.unlock_ritual.text.stage.1").build())
            .build(consumer);
        ResearchEntryBuilder.entry("UNLOCK_MAGITECH", "primalmagic.research.unlock_magitech.title", "BASICS").parent("UNLOCK_RITUAL")
            .stage(ResearchStageBuilder.stage("primalmagic.research.unlock_magitech.text.stage.1").build())
            .build(consumer);
        ResearchEntryBuilder.entry("TERRESTRIAL_MAGIC", "primalmagic.research.terrestrial_magic.title", "BASICS").parent("FIRST_STEPS")
            .stage(ResearchStageBuilder.stage("primalmagic.research.terrestrial_magic.text.stage.1").build())
            .build(consumer);
        ResearchEntryBuilder.entry("SOURCE_EARTH", "primalmagic.research.source_earth.title", "BASICS").parent("TERRESTRIAL_MAGIC")
            .stage(ResearchStageBuilder.stage("primalmagic.research.source_earth.text.stage.1")
                    .requiredItemTag("forge", "obsidian").requiredItemTag("forge", "gems/diamond")
                    .requiredKnowledge(KnowledgeType.OBSERVATION, 1)
                    .requiredResearch("m_found_shrine_earth").requiredResearch("m_env_earth").requiredResearch("t_mana_spent_earth_expert")
                    .build())
            .stage(ResearchStageBuilder.stage("primalmagic.research.source_earth.text.stage.2").attunement(Source.EARTH, 5).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SOURCE_SEA", "primalmagic.research.source_sea.title", "BASICS").parent("TERRESTRIAL_MAGIC")
            .stage(ResearchStageBuilder.stage("primalmagic.research.source_sea.text.stage.1")
                    .requiredItemTag(PrimalMagic.MODID, "coral_blocks").requiredItemStack(Items.ICE)
                    .requiredKnowledge(KnowledgeType.OBSERVATION, 1)
                    .requiredResearch("m_found_shrine_sea").requiredResearch("m_env_sea").requiredResearch("t_mana_spent_sea_expert")
                    .build())
            .stage(ResearchStageBuilder.stage("primalmagic.research.source_sea.text.stage.2").attunement(Source.SEA, 5).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SOURCE_SKY", "primalmagic.research.source_sky.title", "BASICS").parent("TERRESTRIAL_MAGIC")
            .stage(ResearchStageBuilder.stage("primalmagic.research.source_sky.text.stage.1")
                    .requiredItemStack(Items.BAMBOO).requiredItemTag("minecraft", "leaves")
                    .requiredKnowledge(KnowledgeType.OBSERVATION, 1)
                    .requiredResearch("m_found_shrine_sky").requiredResearch("m_env_sky").requiredResearch("t_mana_spent_sky_expert")
                    .build())
            .stage(ResearchStageBuilder.stage("primalmagic.research.source_sky.text.stage.2").attunement(Source.SKY, 5).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SOURCE_SUN", "primalmagic.research.source_sun.title", "BASICS").parent("TERRESTRIAL_MAGIC")
            .stage(ResearchStageBuilder.stage("primalmagic.research.source_sun.text.stage.1")
                    .requiredItemTag(PrimalMagic.MODID, "sunwood_logs").requiredItemTag("forge", "sandstone")
                    .requiredKnowledge(KnowledgeType.OBSERVATION, 1)
                    .requiredResearch("m_found_shrine_sun").requiredResearch("m_env_sun").requiredResearch("t_mana_spent_sun_expert")
                    .build())
            .stage(ResearchStageBuilder.stage("primalmagic.research.source_sun.text.stage.2").attunement(Source.SUN, 5).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SOURCE_MOON", "primalmagic.research.source_moon.title", "BASICS").parent("TERRESTRIAL_MAGIC")
            .stage(ResearchStageBuilder.stage("primalmagic.research.source_moon.text.stage.1")
                    .requiredItemTag(PrimalMagic.MODID, "moonwood_logs").requiredItemTag("forge", "mushrooms")
                    .requiredKnowledge(KnowledgeType.OBSERVATION, 1)
                    .requiredResearch("m_found_shrine_moon").requiredResearch("m_env_moon").requiredResearch("t_mana_spent_moon_expert")
                    .build())
            .stage(ResearchStageBuilder.stage("primalmagic.research.source_moon.text.stage.2").attunement(Source.MOON, 5).build())
            .build(consumer);
        ResearchEntryBuilder.entry("FORBIDDEN_MAGIC", "primalmagic.research.forbidden_magic.title", "BASICS").parent("TERRESTRIAL_MAGIC").parent("t_discover_forbidden")
            .stage(ResearchStageBuilder.stage("primalmagic.research.forbidden_magic.text.stage.1").build())
            .build(consumer);
        ResearchEntryBuilder.entry("SOURCE_BLOOD", "primalmagic.research.source_blood.title", "BASICS").parent("FORBIDDEN_MAGIC").parent("t_discover_blood")
            .stage(ResearchStageBuilder.stage("primalmagic.research.source_blood.text.stage.1")
                    .requiredItemTag("forge", "bones").requiredItemStack(ItemsPM.BLOODY_FLESH.get())
                    .requiredKnowledge(KnowledgeType.OBSERVATION, 1)
                    .requiredResearch("t_mana_spent_blood_expert")
                    .build())
            .stage(ResearchStageBuilder.stage("primalmagic.research.source_blood.text.stage.2").attunement(Source.BLOOD, 5).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SOURCE_INFERNAL", "primalmagic.research.source_infernal.title", "BASICS").parent("FORBIDDEN_MAGIC").parent("t_discover_infernal")
            .stage(ResearchStageBuilder.stage("primalmagic.research.source_infernal.text.stage.1")
                    .requiredItemTag("forge", "rods/blaze").requiredItemStack(Items.SOUL_SAND)
                    .requiredKnowledge(KnowledgeType.OBSERVATION, 1)
                    .requiredResearch("t_mana_spent_infernal_expert")
                    .build())
            .stage(ResearchStageBuilder.stage("primalmagic.research.source_infernal.text.stage.2").attunement(Source.INFERNAL, 5).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SOURCE_VOID", "primalmagic.research.source_void.title", "BASICS").parent("FORBIDDEN_MAGIC").parent("t_discover_void")
            .stage(ResearchStageBuilder.stage("primalmagic.research.source_void.text.stage.1")
                    .requiredItemTag("forge", "end_stones").requiredItemTag("forge", "ender_pearls")
                    .requiredKnowledge(KnowledgeType.OBSERVATION, 1)
                    .requiredResearch("t_mana_spent_void_expert")
                    .build())
            .stage(ResearchStageBuilder.stage("primalmagic.research.source_void.text.stage.2").attunement(Source.VOID, 5).build())
            .build(consumer);
        ResearchEntryBuilder.entry("HEAVENLY_MAGIC", "primalmagic.research.heavenly_magic.title", "BASICS").parent("FORBIDDEN_MAGIC").parent("t_discover_hallowed")
            .stage(ResearchStageBuilder.stage("primalmagic.research.heavenly_magic.text.stage.1").build())
            .build(consumer);
        ResearchEntryBuilder.entry("SOURCE_HALLOWED", "primalmagic.research.source_hallowed.title", "BASICS").parent("HEAVENLY_MAGIC")
            .stage(ResearchStageBuilder.stage("primalmagic.research.source_hallowed.text.stage.1")
                    .requiredItemTag("forge", "nether_stars")
                    .requiredKnowledge(KnowledgeType.OBSERVATION, 1)
                    .requiredResearch("t_mana_spent_hallowed_expert")
                    .build())
            .stage(ResearchStageBuilder.stage("primalmagic.research.source_hallowed.text.stage.2").attunement(Source.HALLOWED, 5).build())
            .build(consumer);
    }
    
    protected void registerAlchemyEntries(Consumer<IFinishedResearchEntry> consumer) {
        ResearchEntryBuilder.entry("BASIC_ALCHEMY", "primalmagic.research.basic_alchemy.title", "ALCHEMY").parent("UNLOCK_ALCHEMY")
            .stage(ResearchStageBuilder.stage("primalmagic.research.basic_alchemy.text.stage.1").build())
            .build(consumer);
        ResearchEntryBuilder.entry("EXPERT_ALCHEMY", "primalmagic.research.expert_alchemy.title", "ALCHEMY").parent("BASIC_ALCHEMY")
            .stage(ResearchStageBuilder.stage("primalmagic.research.expert_alchemy.text.stage.1").requiredResearch("b_crafted_alchemy_expert").build())
            .stage(ResearchStageBuilder.stage("primalmagic.research.expert_alchemy.text.stage.2").build())
            .build(consumer);
        ResearchEntryBuilder.entry("MASTER_ALCHEMY", "primalmagic.research.master_alchemy.title", "ALCHEMY").parent("EXPERT_ALCHEMY")
            .stage(ResearchStageBuilder.stage("primalmagic.research.master_alchemy.text.stage.1").requiredResearch("b_crafted_alchemy_master").build())
            .stage(ResearchStageBuilder.stage("primalmagic.research.master_alchemy.text.stage.2").build())
            .build(consumer);
        ResearchEntryBuilder.entry("SUPREME_ALCHEMY", "primalmagic.research.supreme_alchemy.title", "ALCHEMY").parent("MASTER_ALCHEMY")
            .stage(ResearchStageBuilder.stage("primalmagic.research.supreme_alchemy.text.stage.1").requiredResearch("b_crafted_alchemy_supreme").build())
            .stage(ResearchStageBuilder.stage("primalmagic.research.supreme_alchemy.text.stage.2").build())
            .build(consumer);
        ResearchEntryBuilder.entry("STONEMELDING", "primalmagic.research.stonemelding.title", "ALCHEMY").parent("BASIC_ALCHEMY")
            .stage(ResearchStageBuilder.stage("primalmagic.research.stonemelding.text.stage.1").recipe(PrimalMagic.MODID, "stone_from_stonemelding").attunement(Source.EARTH, 1).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SKYGLASS", "primalmagic.research.skyglass.title", "ALCHEMY").parent("BASIC_ALCHEMY")
            .stage(ResearchStageBuilder.stage("primalmagic.research.skyglass.text.stage.1").attunement(Source.SKY, 1)
                    .recipe(ItemsPM.SKYGLASS.get()).recipe(ItemsPM.SKYGLASS_PANE.get())
                    .recipe(ItemsPM.STAINED_SKYGLASS_BLACK.get()).recipe(ItemsPM.STAINED_SKYGLASS_BLUE.get()).recipe(ItemsPM.STAINED_SKYGLASS_BROWN.get()).recipe(ItemsPM.STAINED_SKYGLASS_CYAN.get())
                    .recipe(ItemsPM.STAINED_SKYGLASS_GRAY.get()).recipe(ItemsPM.STAINED_SKYGLASS_GREEN.get()).recipe(ItemsPM.STAINED_SKYGLASS_LIGHT_BLUE.get()).recipe(ItemsPM.STAINED_SKYGLASS_LIGHT_GRAY.get())
                    .recipe(ItemsPM.STAINED_SKYGLASS_LIME.get()).recipe(ItemsPM.STAINED_SKYGLASS_MAGENTA.get()).recipe(ItemsPM.STAINED_SKYGLASS_ORANGE.get()).recipe(ItemsPM.STAINED_SKYGLASS_PINK.get())
                    .recipe(ItemsPM.STAINED_SKYGLASS_PURPLE.get()).recipe(ItemsPM.STAINED_SKYGLASS_RED.get()).recipe(ItemsPM.STAINED_SKYGLASS_WHITE.get()).recipe(ItemsPM.STAINED_SKYGLASS_YELLOW.get())
                    .recipe(PrimalMagic.MODID, "stained_skyglass_pane_black_from_blocks").recipe(PrimalMagic.MODID, "stained_skyglass_pane_black_from_panes")
                    .recipe(PrimalMagic.MODID, "stained_skyglass_pane_blue_from_blocks").recipe(PrimalMagic.MODID, "stained_skyglass_pane_blue_from_panes")
                    .recipe(PrimalMagic.MODID, "stained_skyglass_pane_brown_from_blocks").recipe(PrimalMagic.MODID, "stained_skyglass_pane_brown_from_panes")
                    .recipe(PrimalMagic.MODID, "stained_skyglass_pane_cyan_from_blocks").recipe(PrimalMagic.MODID, "stained_skyglass_pane_cyan_from_panes")
                    .recipe(PrimalMagic.MODID, "stained_skyglass_pane_gray_from_blocks").recipe(PrimalMagic.MODID, "stained_skyglass_pane_gray_from_panes")
                    .recipe(PrimalMagic.MODID, "stained_skyglass_pane_green_from_blocks").recipe(PrimalMagic.MODID, "stained_skyglass_pane_green_from_panes")
                    .recipe(PrimalMagic.MODID, "stained_skyglass_pane_light_blue_from_blocks").recipe(PrimalMagic.MODID, "stained_skyglass_pane_light_blue_from_panes")
                    .recipe(PrimalMagic.MODID, "stained_skyglass_pane_light_gray_from_blocks").recipe(PrimalMagic.MODID, "stained_skyglass_pane_light_gray_from_panes")
                    .recipe(PrimalMagic.MODID, "stained_skyglass_pane_lime_from_blocks").recipe(PrimalMagic.MODID, "stained_skyglass_pane_lime_from_panes")
                    .recipe(PrimalMagic.MODID, "stained_skyglass_pane_magenta_from_blocks").recipe(PrimalMagic.MODID, "stained_skyglass_pane_magenta_from_panes")
                    .recipe(PrimalMagic.MODID, "stained_skyglass_pane_orange_from_blocks").recipe(PrimalMagic.MODID, "stained_skyglass_pane_orange_from_panes")
                    .recipe(PrimalMagic.MODID, "stained_skyglass_pane_pink_from_blocks").recipe(PrimalMagic.MODID, "stained_skyglass_pane_pink_from_panes")
                    .recipe(PrimalMagic.MODID, "stained_skyglass_pane_purple_from_blocks").recipe(PrimalMagic.MODID, "stained_skyglass_pane_purple_from_panes")
                    .recipe(PrimalMagic.MODID, "stained_skyglass_pane_red_from_blocks").recipe(PrimalMagic.MODID, "stained_skyglass_pane_red_from_panes")
                    .recipe(PrimalMagic.MODID, "stained_skyglass_pane_white_from_blocks").recipe(PrimalMagic.MODID, "stained_skyglass_pane_white_from_panes")
                    .recipe(PrimalMagic.MODID, "stained_skyglass_pane_yellow_from_blocks").recipe(PrimalMagic.MODID, "stained_skyglass_pane_yellow_from_panes")
                    .build())
            .build(consumer);
        ResearchEntryBuilder.entry("SHARD_SYNTHESIS", "primalmagic.research.shard_synthesis.title", "ALCHEMY").parent("EXPERT_ALCHEMY")
            .stage(ResearchStageBuilder.stage("primalmagic.research.shard_synthesis.text.stage.1")
                    .attunement(Source.EARTH, 1).attunement(Source.SEA, 1).attunement(Source.SKY, 1).attunement(Source.SUN, 1).attunement(Source.MOON, 1)
                    .recipe(PrimalMagic.MODID, "essence_shard_earth_from_dust").recipe(PrimalMagic.MODID, "essence_shard_sea_from_dust")
                    .recipe(PrimalMagic.MODID, "essence_shard_sky_from_dust").recipe(PrimalMagic.MODID, "essence_shard_sun_from_dust")
                    .recipe(PrimalMagic.MODID, "essence_shard_moon_from_dust").build())
            .addendum(ResearchAddendumBuilder.addendum("primalmagic.research.shard_synthesis.text.addenda.1").requiredResearch("t_discover_blood").attunement(Source.BLOOD, 1)
                    .recipe(PrimalMagic.MODID, "essence_shard_blood_from_dust").build())
            .addendum(ResearchAddendumBuilder.addendum("primalmagic.research.shard_synthesis.text.addenda.2").requiredResearch("t_discover_infernal").attunement(Source.INFERNAL, 1)
                    .recipe(PrimalMagic.MODID, "essence_shard_infernal_from_dust").build())
            .addendum(ResearchAddendumBuilder.addendum("primalmagic.research.shard_synthesis.text.addenda.3").requiredResearch("t_discover_void").attunement(Source.VOID, 1)
                    .recipe(PrimalMagic.MODID, "essence_shard_void_from_dust").build())
            .addendum(ResearchAddendumBuilder.addendum("primalmagic.research.shard_synthesis.text.addenda.4").requiredResearch("t_discover_hallowed").attunement(Source.HALLOWED, 1)
                    .recipe(PrimalMagic.MODID, "essence_shard_hallowed_from_dust").build())
            .build(consumer);
        ResearchEntryBuilder.entry("SHARD_DESYNTHESIS", "primalmagic.research.shard_desynthesis.title", "ALCHEMY").parent("SHARD_SYNTHESIS")
            .stage(ResearchStageBuilder.stage("primalmagic.research.shard_desynthesis.text.stage.1")
                    .attunement(Source.EARTH, 1).attunement(Source.SEA, 1).attunement(Source.SKY, 1).attunement(Source.SUN, 1).attunement(Source.MOON, 1)
                    .recipe(PrimalMagic.MODID, "essence_dust_earth_from_shard").recipe(PrimalMagic.MODID, "essence_dust_sea_from_shard")
                    .recipe(PrimalMagic.MODID, "essence_dust_sky_from_shard").recipe(PrimalMagic.MODID, "essence_dust_sun_from_shard")
                    .recipe(PrimalMagic.MODID, "essence_dust_moon_from_shard").build())
            .addendum(ResearchAddendumBuilder.addendum("primalmagic.research.shard_desynthesis.text.addenda.1").requiredResearch("t_discover_blood").attunement(Source.BLOOD, 1)
                    .recipe(PrimalMagic.MODID, "essence_dust_blood_from_shard").build())
            .addendum(ResearchAddendumBuilder.addendum("primalmagic.research.shard_desynthesis.text.addenda.2").requiredResearch("t_discover_infernal").attunement(Source.INFERNAL, 1)
                    .recipe(PrimalMagic.MODID, "essence_dust_infernal_from_shard").build())
            .addendum(ResearchAddendumBuilder.addendum("primalmagic.research.shard_desynthesis.text.addenda.3").requiredResearch("t_discover_void").attunement(Source.VOID, 1)
                    .recipe(PrimalMagic.MODID, "essence_dust_void_from_shard").build())
            .addendum(ResearchAddendumBuilder.addendum("primalmagic.research.shard_desynthesis.text.addenda.4").requiredResearch("t_discover_hallowed").attunement(Source.HALLOWED, 1)
                    .recipe(PrimalMagic.MODID, "essence_dust_hallowed_from_shard").build())
            .build(consumer);
        ResearchEntryBuilder.entry("PRIMALITE", "primalmagic.research.primalite.title", "ALCHEMY").parent("EXPERT_ALCHEMY")
            .stage(ResearchStageBuilder.stage("primalmagic.research.primalite.text.stage.1").requiredItemStack(ItemsPM.ESSENCE_DUST_EARTH.get()).requiredItemStack(ItemsPM.ESSENCE_DUST_SEA.get())
                    .requiredItemStack(ItemsPM.ESSENCE_DUST_SKY.get()).requiredItemStack(ItemsPM.ESSENCE_DUST_SUN.get()).requiredItemStack(ItemsPM.ESSENCE_DUST_MOON.get())
                    .requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage("primalmagic.research.primalite.text.stage.2").attunement(Source.EARTH, 1).attunement(Source.SEA, 1).attunement(Source.SKY, 1)
                    .attunement(Source.SUN, 1).attunement(Source.MOON, 1).recipe(ItemsPM.PRIMALITE_INGOT.get()).recipe(ItemsPM.PRIMALITE_SWORD.get()).recipe(ItemsPM.PRIMALITE_SHOVEL.get())
                    .recipe(ItemsPM.PRIMALITE_PICKAXE.get()).recipe(ItemsPM.PRIMALITE_AXE.get()).recipe(ItemsPM.PRIMALITE_HOE.get()).recipe(ItemsPM.PRIMALITE_HEAD.get())
                    .recipe(ItemsPM.PRIMALITE_CHEST.get()).recipe(ItemsPM.PRIMALITE_LEGS.get()).recipe(ItemsPM.PRIMALITE_FEET.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("CRYSTAL_SYNTHESIS", "primalmagic.research.crystal_synthesis.title", "ALCHEMY").parent("MASTER_ALCHEMY").parent("SHARD_SYNTHESIS")
            .stage(ResearchStageBuilder.stage("primalmagic.research.crystal_synthesis.text.stage.1")
                    .attunement(Source.EARTH, 2).attunement(Source.SEA, 2).attunement(Source.SKY, 2).attunement(Source.SUN, 2).attunement(Source.MOON, 2)
                    .recipe(PrimalMagic.MODID, "essence_crystal_earth_from_shard").recipe(PrimalMagic.MODID, "essence_crystal_sea_from_shard")
                    .recipe(PrimalMagic.MODID, "essence_crystal_sky_from_shard").recipe(PrimalMagic.MODID, "essence_crystal_sun_from_shard")
                    .recipe(PrimalMagic.MODID, "essence_crystal_moon_from_shard").build())
            .addendum(ResearchAddendumBuilder.addendum("primalmagic.research.crystal_synthesis.text.addenda.1").requiredResearch("t_discover_blood").attunement(Source.BLOOD, 2)
                    .recipe(PrimalMagic.MODID, "essence_crystal_blood_from_shard").build())
            .addendum(ResearchAddendumBuilder.addendum("primalmagic.research.crystal_synthesis.text.addenda.2").requiredResearch("t_discover_infernal").attunement(Source.INFERNAL, 2)
                    .recipe(PrimalMagic.MODID, "essence_crystal_infernal_from_shard").build())
            .addendum(ResearchAddendumBuilder.addendum("primalmagic.research.crystal_synthesis.text.addenda.3").requiredResearch("t_discover_void").attunement(Source.VOID, 2)
                    .recipe(PrimalMagic.MODID, "essence_crystal_void_from_shard").build())
            .addendum(ResearchAddendumBuilder.addendum("primalmagic.research.crystal_synthesis.text.addenda.4").requiredResearch("t_discover_hallowed").attunement(Source.HALLOWED, 2)
                    .recipe(PrimalMagic.MODID, "essence_crystal_hallowed_from_shard").build())
            .build(consumer);
        ResearchEntryBuilder.entry("CRYSTAL_DESYNTHESIS", "primalmagic.research.crystal_desynthesis.title", "ALCHEMY").parent("CRYSTAL_SYNTHESIS")
            .stage(ResearchStageBuilder.stage("primalmagic.research.crystal_desynthesis.text.stage.1")
                    .attunement(Source.EARTH, 1).attunement(Source.SEA, 1).attunement(Source.SKY, 1).attunement(Source.SUN, 1).attunement(Source.MOON, 1)
                    .recipe(PrimalMagic.MODID, "essence_shard_earth_from_crystal").recipe(PrimalMagic.MODID, "essence_shard_sea_from_crystal")
                    .recipe(PrimalMagic.MODID, "essence_shard_sky_from_crystal").recipe(PrimalMagic.MODID, "essence_shard_sun_from_crystal")
                    .recipe(PrimalMagic.MODID, "essence_shard_moon_from_crystal").build())
            .addendum(ResearchAddendumBuilder.addendum("primalmagic.research.crystal_desynthesis.text.addenda.1").requiredResearch("t_discover_blood").attunement(Source.BLOOD, 1)
                    .recipe(PrimalMagic.MODID, "essence_shard_blood_from_crystal").build())
            .addendum(ResearchAddendumBuilder.addendum("primalmagic.research.crystal_desynthesis.text.addenda.2").requiredResearch("t_discover_infernal").attunement(Source.INFERNAL, 1)
                    .recipe(PrimalMagic.MODID, "essence_shard_infernal_from_crystal").build())
            .addendum(ResearchAddendumBuilder.addendum("primalmagic.research.crystal_desynthesis.text.addenda.3").requiredResearch("t_discover_void").attunement(Source.VOID, 1)
                    .recipe(PrimalMagic.MODID, "essence_shard_void_from_crystal").build())
            .addendum(ResearchAddendumBuilder.addendum("primalmagic.research.crystal_desynthesis.text.addenda.4").requiredResearch("t_discover_hallowed").attunement(Source.HALLOWED, 1)
                    .recipe(PrimalMagic.MODID, "essence_shard_hallowed_from_crystal").build())
            .build(consumer);
        ResearchEntryBuilder.entry("HEXIUM", "primalmagic.research.hexium.title", "ALCHEMY").parent("MASTER_ALCHEMY").parent("PRIMALITE").parent("SHARD_SYNTHESIS").parent("t_discover_blood")
            .parent("t_discover_infernal").parent("t_discover_void")
            .stage(ResearchStageBuilder.stage("primalmagic.research.hexium.text.stage.1").requiredItemStack(ItemsPM.ESSENCE_SHARD_BLOOD.get()).requiredItemStack(ItemsPM.ESSENCE_SHARD_INFERNAL.get())
                    .requiredItemStack(ItemsPM.ESSENCE_SHARD_VOID.get()).requiredCraftStack(ItemsPM.PRIMALITE_INGOT.get()).requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage("primalmagic.research.hexium.text.stage.2").attunement(Source.BLOOD, 2).attunement(Source.INFERNAL, 2).attunement(Source.VOID, 2)
                    .recipe(ItemsPM.HEXIUM_INGOT.get()).recipe(ItemsPM.HEXIUM_SWORD.get()).recipe(ItemsPM.HEXIUM_SHOVEL.get()).recipe(ItemsPM.HEXIUM_PICKAXE.get())
                    .recipe(ItemsPM.HEXIUM_AXE.get()).recipe(ItemsPM.HEXIUM_HOE.get()).recipe(ItemsPM.HEXIUM_HEAD.get()).recipe(ItemsPM.HEXIUM_CHEST.get()).recipe(ItemsPM.HEXIUM_LEGS.get())
                    .recipe(ItemsPM.HEXIUM_FEET.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("CLUSTER_SYNTHESIS", "primalmagic.research.cluster_synthesis.title", "ALCHEMY").parent("SUPREME_ALCHEMY").parent("CRYSTAL_SYNTHESIS")
            .stage(ResearchStageBuilder.stage("primalmagic.research.cluster_synthesis.text.stage.1")
                    .attunement(Source.EARTH, 3).attunement(Source.SEA, 3).attunement(Source.SKY, 3).attunement(Source.SUN, 3).attunement(Source.MOON, 3)
                    .recipe(PrimalMagic.MODID, "essence_cluster_earth_from_crystal").recipe(PrimalMagic.MODID, "essence_cluster_sea_from_crystal")
                    .recipe(PrimalMagic.MODID, "essence_cluster_sky_from_crystal").recipe(PrimalMagic.MODID, "essence_cluster_sun_from_crystal")
                    .recipe(PrimalMagic.MODID, "essence_cluster_moon_from_crystal").build())
            .addendum(ResearchAddendumBuilder.addendum("primalmagic.research.cluster_synthesis.text.addenda.1").requiredResearch("t_discover_blood").attunement(Source.BLOOD, 3)
                    .recipe(PrimalMagic.MODID, "essence_cluster_blood_from_crystal").build())
            .addendum(ResearchAddendumBuilder.addendum("primalmagic.research.cluster_synthesis.text.addenda.2").requiredResearch("t_discover_infernal").attunement(Source.INFERNAL, 3)
                    .recipe(PrimalMagic.MODID, "essence_cluster_infernal_from_crystal").build())
            .addendum(ResearchAddendumBuilder.addendum("primalmagic.research.cluster_synthesis.text.addenda.3").requiredResearch("t_discover_void").attunement(Source.VOID, 3)
                    .recipe(PrimalMagic.MODID, "essence_cluster_void_from_crystal").build())
            .addendum(ResearchAddendumBuilder.addendum("primalmagic.research.cluster_synthesis.text.addenda.4").requiredResearch("t_discover_hallowed").attunement(Source.HALLOWED, 3)
                    .recipe(PrimalMagic.MODID, "essence_cluster_hallowed_from_crystal").build())
            .build(consumer);
        ResearchEntryBuilder.entry("CLUSTER_DESYNTHESIS", "primalmagic.research.cluster_desynthesis.title", "ALCHEMY").parent("CLUSTER_SYNTHESIS")
            .stage(ResearchStageBuilder.stage("primalmagic.research.cluster_desynthesis.text.stage.1")
                    .attunement(Source.EARTH, 1).attunement(Source.SEA, 1).attunement(Source.SKY, 1).attunement(Source.SUN, 1).attunement(Source.MOON, 1)
                    .recipe(PrimalMagic.MODID, "essence_crystal_earth_from_cluster").recipe(PrimalMagic.MODID, "essence_crystal_sea_from_cluster")
                    .recipe(PrimalMagic.MODID, "essence_crystal_sky_from_cluster").recipe(PrimalMagic.MODID, "essence_crystal_sun_from_cluster")
                    .recipe(PrimalMagic.MODID, "essence_crystal_moon_from_cluster").build())
            .addendum(ResearchAddendumBuilder.addendum("primalmagic.research.cluster_desynthesis.text.addenda.1").requiredResearch("t_discover_blood").attunement(Source.BLOOD, 1)
                    .recipe(PrimalMagic.MODID, "essence_crystal_blood_from_cluster").build())
            .addendum(ResearchAddendumBuilder.addendum("primalmagic.research.cluster_desynthesis.text.addenda.2").requiredResearch("t_discover_infernal").attunement(Source.INFERNAL, 1)
                    .recipe(PrimalMagic.MODID, "essence_crystal_infernal_from_cluster").build())
            .addendum(ResearchAddendumBuilder.addendum("primalmagic.research.cluster_desynthesis.text.addenda.3").requiredResearch("t_discover_void").attunement(Source.VOID, 1)
                    .recipe(PrimalMagic.MODID, "essence_crystal_void_from_cluster").build())
            .addendum(ResearchAddendumBuilder.addendum("primalmagic.research.cluster_desynthesis.text.addenda.4").requiredResearch("t_discover_hallowed").attunement(Source.HALLOWED, 1)
                    .recipe(PrimalMagic.MODID, "essence_crystal_hallowed_from_cluster").build())
            .build(consumer);
        ResearchEntryBuilder.entry("HALLOWSTEEL", "primalmagic.research.hallowsteel.title", "ALCHEMY").parent("SUPREME_ALCHEMY").parent("HEXIUM").parent("CRYSTAL_SYNTHESIS").parent("t_discover_hallowed")
            .stage(ResearchStageBuilder.stage("primalmagic.research.hallowsteel.text.stage.1").requiredItemStack(ItemsPM.ESSENCE_CRYSTAL_HALLOWED.get()).requiredCraftStack(ItemsPM.HEXIUM_INGOT.get())
                    .requiredKnowledge(KnowledgeType.THEORY, 3).build())
            .stage(ResearchStageBuilder.stage("primalmagic.research.hallowsteel.text.stage.2").attunement(Source.HALLOWED, 3).recipe(ItemsPM.HALLOWSTEEL_INGOT.get())
                    .recipe(ItemsPM.HALLOWSTEEL_SWORD.get()).recipe(ItemsPM.HALLOWSTEEL_SHOVEL.get()).recipe(ItemsPM.HALLOWSTEEL_PICKAXE.get()).recipe(ItemsPM.HALLOWSTEEL_AXE.get())
                    .recipe(ItemsPM.HALLOWSTEEL_HOE.get()).recipe(ItemsPM.HALLOWSTEEL_HEAD.get()).recipe(ItemsPM.HALLOWSTEEL_CHEST.get()).recipe(ItemsPM.HALLOWSTEEL_LEGS.get())
                    .recipe(ItemsPM.HALLOWSTEEL_FEET.get()).build())
            .build(consumer);
    }

    protected void registerSorceryEntries(Consumer<IFinishedResearchEntry> consumer) {
        // TODO Auto-generated method stub
        ResearchEntryBuilder.entry("BASIC_SORCERY", "primalmagic.research.basic_sorcery.title", "SORCERY").parent("UNLOCK_SORCERY")
            .stage(ResearchStageBuilder.stage("primalmagic.research.basic_sorcery.text.stage.1").attunement(Source.EARTH, 1)
                    .recipe(ItemsPM.SPELL_SCROLL_BLANK.get()).recipe(ItemsPM.SPELLCRAFTING_ALTAR.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("EXPERT_SORCERY", "primalmagic.research.expert_sorcery.title", "SORCERY").parent("BASIC_SORCERY")
            .stage(ResearchStageBuilder.stage("primalmagic.research.expert_sorcery.text.stage.1").requiredResearch("t_spells_crafted_expert").requiredResearch("t_spells_cast_expert").build())
            .stage(ResearchStageBuilder.stage("primalmagic.research.expert_sorcery.text.stage.2").build())
            .build(consumer);
        ResearchEntryBuilder.entry("MASTER_SORCERY", "primalmagic.research.master_sorcery.title", "SORCERY").parent("EXPERT_SORCERY")
            .stage(ResearchStageBuilder.stage("primalmagic.research.master_sorcery.text.stage.1").requiredResearch("t_spells_cast_master").requiredResearch("t_spell_cost_master").build())
            .stage(ResearchStageBuilder.stage("primalmagic.research.master_sorcery.text.stage.2").build())
            .build(consumer);
        ResearchEntryBuilder.entry("SUPREME_SORCERY", "primalmagic.research.supreme_sorcery.title", "SORCERY").parent("MASTER_SORCERY")
            .stage(ResearchStageBuilder.stage("primalmagic.research.supreme_sorcery.text.stage.1").requiredResearch("t_spells_cast_supreme").requiredResearch("t_spell_cost_supreme").build())
            .stage(ResearchStageBuilder.stage("primalmagic.research.supreme_sorcery.text.stage.2").build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_INSCRIPTION", "primalmagic.research.wand_inscription.title", "SORCERY").parent("BASIC_SORCERY").parent("ADVANCED_WANDMAKING")
            .stage(ResearchStageBuilder.stage("primalmagic.research.wand_inscription.text.stage.1").requiredResearch("t_spells_crafted_expert").requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage("primalmagic.research.wand_inscription.text.stage.2").recipe(ItemsPM.WAND_INSCRIPTION_TABLE.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_VEHICLE_PROJECTILE", "primalmagic.research.spell_vehicle_projectile.title", "SORCERY").parent("EXPERT_SORCERY")
            .stage(ResearchStageBuilder.stage("primalmagic.research.spell_vehicle_projectile.text.stage.1").build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_VEHICLE_BOLT", "primalmagic.research.spell_vehicle_bolt.title", "SORCERY").parent("MASTER_SORCERY").parent("SPELL_VEHICLE_PROJECTILE")
            .stage(ResearchStageBuilder.stage("primalmagic.research.spell_vehicle_bolt.text.stage.1").build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_FROST", "primalmagic.research.spell_payload_frost.title", "SORCERY").parent("BASIC_SORCERY")
            .stage(ResearchStageBuilder.stage("primalmagic.research.spell_payload_frost.text.stage.1").attunement(Source.SEA, 1).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_LIGHTNING", "primalmagic.research.spell_payload_lightning.title", "SORCERY").parent("BASIC_SORCERY")
            .stage(ResearchStageBuilder.stage("primalmagic.research.spell_payload_lightning.text.stage.1").attunement(Source.SKY, 1).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_SOLAR", "primalmagic.research.spell_payload_solar.title", "SORCERY").parent("BASIC_SORCERY")
            .stage(ResearchStageBuilder.stage("primalmagic.research.spell_payload_solar.text.stage.1").attunement(Source.SUN, 1).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_LUNAR", "primalmagic.research.spell_payload_lunar.title", "SORCERY").parent("BASIC_SORCERY")
            .stage(ResearchStageBuilder.stage("primalmagic.research.spell_payload_lunar.text.stage.1").attunement(Source.MOON, 1).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_BLOOD", "primalmagic.research.spell_payload_blood.title", "SORCERY").parent("EXPERT_SORCERY").parent("t_discover_blood")
            .stage(ResearchStageBuilder.stage("primalmagic.research.spell_payload_blood.text.stage.1").attunement(Source.BLOOD, 1).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_FLAME", "primalmagic.research.spell_payload_flame.title", "SORCERY").parent("EXPERT_SORCERY").parent("t_discover_infernal")
            .stage(ResearchStageBuilder.stage("primalmagic.research.spell_payload_flame.text.stage.1").attunement(Source.INFERNAL, 1).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_VOID", "primalmagic.research.spell_payload_void.title", "SORCERY").parent("EXPERT_SORCERY").parent("t_discover_void")
            .stage(ResearchStageBuilder.stage("primalmagic.research.spell_payload_void.text.stage.1").attunement(Source.VOID, 1).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_HOLY", "primalmagic.research.spell_payload_holy.title", "SORCERY").parent("MASTER_SORCERY").parent("t_discover_hallowed")
            .stage(ResearchStageBuilder.stage("primalmagic.research.spell_payload_holy.text.stage.1").attunement(Source.HALLOWED, 1).build())
            .build(consumer);
        
    }

    protected void registerRuneworkingEntries(Consumer<IFinishedResearchEntry> consumer) {
        // TODO Auto-generated method stub
        
    }

    protected void registerRitualEntries(Consumer<IFinishedResearchEntry> consumer) {
        // TODO Auto-generated method stub
        
    }

    protected void registerMagitechEntries(Consumer<IFinishedResearchEntry> consumer) {
        // TODO Auto-generated method stub
        
    }

    protected void registerScanEntries(Consumer<IFinishedResearchEntry> consumer) {
        ResearchEntryBuilder.entry("RAW_MARBLE", "primalmagic.research.raw_marble.title", "SCANS").parent("UNLOCK_SCANS")
            .stage(ResearchStageBuilder.stage("primalmagic.research.raw_marble.text.stage.1").build())
            .build(consumer);
    }
    
    @Override
    public String getName() {
        return "Primal Magic Grimoire Research";
    }
}
