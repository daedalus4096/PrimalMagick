package com.verdantartifice.primalmagic.datagen.research;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private static final Logger LOGGER = LogManager.getLogger();
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
                LOGGER.debug("Duplicate research entry in data generation: " + research.getId().toString());
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
            LOGGER.error("Couldn't save research entry {}", path, e);
        }
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
        ResearchEntryBuilder.entry("FIRST_STEPS", discipline)
            .stage(ResearchStageBuilder.stage().requiredCraftStack(ItemsPM.ARCANE_WORKBENCH.get()).recipe("mundane_wand").build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.MUNDANE_WAND.get()).recipe(ItemsPM.MARBLE_ENCHANTED.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("KNOWLEDGE_TYPES", discipline).parent("FIRST_STEPS")
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("THEORYCRAFTING", discipline).parent("KNOWLEDGE_TYPES")
            .stage(ResearchStageBuilder.stage().requiredItemStack(Items.STICK).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.RESEARCH_TABLE.get()).recipe(ItemsPM.ENCHANTED_INK.get()).recipe(ItemsPM.ENCHANTED_INK_AND_QUILL.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("ATTUNEMENTS", discipline).parent("FIRST_STEPS")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("UNLOCK_MANAWEAVING", discipline).parent("FIRST_STEPS")
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("UNLOCK_ALCHEMY", discipline).parent("UNLOCK_MANAWEAVING")
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("UNLOCK_SORCERY", discipline).parent("UNLOCK_MANAWEAVING")
            .stage(ResearchStageBuilder.stage().requiredItemStack(Items.STICK).build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("UNLOCK_RUNEWORKING", discipline).parent("UNLOCK_ALCHEMY")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("UNLOCK_RITUAL", discipline).parent("UNLOCK_RUNEWORKING")
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("UNLOCK_MAGITECH", discipline).parent("UNLOCK_RITUAL").parent("MANA_SALTS")
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("TERRESTRIAL_MAGIC", discipline).parent("FIRST_STEPS")
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("SOURCE_EARTH", discipline).parent("TERRESTRIAL_MAGIC")
            .stage(ResearchStageBuilder.stage()
                    .requiredItemTag("forge", "obsidian").requiredItemTag("forge", "gems/diamond")
                    .requiredKnowledge(KnowledgeType.OBSERVATION, 1)
                    .requiredResearch("m_found_shrine_earth").requiredResearch("m_env_earth").requiredResearch("t_mana_spent_earth_expert")
                    .build())
            .stage(ResearchStageBuilder.stage().attunement(Source.EARTH, 5).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SOURCE_SEA", discipline).parent("TERRESTRIAL_MAGIC")
            .stage(ResearchStageBuilder.stage()
                    .requiredItemTag(PrimalMagic.MODID, "coral_blocks").requiredItemStack(Items.ICE)
                    .requiredKnowledge(KnowledgeType.OBSERVATION, 1)
                    .requiredResearch("m_found_shrine_sea").requiredResearch("m_env_sea").requiredResearch("t_mana_spent_sea_expert")
                    .build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SEA, 5).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SOURCE_SKY", discipline).parent("TERRESTRIAL_MAGIC")
            .stage(ResearchStageBuilder.stage()
                    .requiredItemStack(Items.BAMBOO).requiredItemTag("minecraft", "leaves")
                    .requiredKnowledge(KnowledgeType.OBSERVATION, 1)
                    .requiredResearch("m_found_shrine_sky").requiredResearch("m_env_sky").requiredResearch("t_mana_spent_sky_expert")
                    .build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SKY, 5).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SOURCE_SUN", discipline).parent("TERRESTRIAL_MAGIC")
            .stage(ResearchStageBuilder.stage()
                    .requiredItemTag(PrimalMagic.MODID, "sunwood_logs").requiredItemTag("forge", "sandstone")
                    .requiredKnowledge(KnowledgeType.OBSERVATION, 1)
                    .requiredResearch("m_found_shrine_sun").requiredResearch("m_env_sun").requiredResearch("t_mana_spent_sun_expert")
                    .build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SUN, 5).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SOURCE_MOON", discipline).parent("TERRESTRIAL_MAGIC")
            .stage(ResearchStageBuilder.stage()
                    .requiredItemTag(PrimalMagic.MODID, "moonwood_logs").requiredItemTag("forge", "mushrooms")
                    .requiredKnowledge(KnowledgeType.OBSERVATION, 1)
                    .requiredResearch("m_found_shrine_moon").requiredResearch("m_env_moon").requiredResearch("t_mana_spent_moon_expert")
                    .build())
            .stage(ResearchStageBuilder.stage().attunement(Source.MOON, 5).build())
            .build(consumer);
        ResearchEntryBuilder.entry("FORBIDDEN_MAGIC", discipline).parent("TERRESTRIAL_MAGIC").parent("t_discover_forbidden")
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("SOURCE_BLOOD", discipline).parent("FORBIDDEN_MAGIC").parent("t_discover_blood")
            .stage(ResearchStageBuilder.stage()
                    .requiredItemTag("forge", "bones").requiredItemStack(ItemsPM.BLOODY_FLESH.get())
                    .requiredKnowledge(KnowledgeType.OBSERVATION, 1)
                    .requiredResearch("t_mana_spent_blood_expert")
                    .build())
            .stage(ResearchStageBuilder.stage().attunement(Source.BLOOD, 5).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SOURCE_INFERNAL", discipline).parent("FORBIDDEN_MAGIC").parent("t_discover_infernal")
            .stage(ResearchStageBuilder.stage()
                    .requiredItemTag("forge", "rods/blaze").requiredItemStack(Items.SOUL_SAND)
                    .requiredKnowledge(KnowledgeType.OBSERVATION, 1)
                    .requiredResearch("t_mana_spent_infernal_expert")
                    .build())
            .stage(ResearchStageBuilder.stage().attunement(Source.INFERNAL, 5).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SOURCE_VOID", discipline).parent("FORBIDDEN_MAGIC").parent("t_discover_void")
            .stage(ResearchStageBuilder.stage()
                    .requiredItemTag("forge", "end_stones").requiredItemTag("forge", "ender_pearls")
                    .requiredKnowledge(KnowledgeType.OBSERVATION, 1)
                    .requiredResearch("t_mana_spent_void_expert")
                    .build())
            .stage(ResearchStageBuilder.stage().attunement(Source.VOID, 5).build())
            .build(consumer);
        ResearchEntryBuilder.entry("HEAVENLY_MAGIC", discipline).parent("FORBIDDEN_MAGIC").parent("t_discover_hallowed")
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("SOURCE_HALLOWED", discipline).parent("HEAVENLY_MAGIC")
            .stage(ResearchStageBuilder.stage()
                    .requiredItemTag("forge", "nether_stars")
                    .requiredKnowledge(KnowledgeType.OBSERVATION, 1)
                    .requiredResearch("t_mana_spent_hallowed_expert")
                    .build())
            .stage(ResearchStageBuilder.stage().attunement(Source.HALLOWED, 5).build())
            .build(consumer);
    }
    
    protected void registerManaweavingEntries(Consumer<IFinishedResearchEntry> consumer) {
        String discipline = "MANAWEAVING";
        ResearchEntryBuilder.entry("BASIC_MANAWEAVING", discipline).parent("UNLOCK_MANAWEAVING")
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.MANA_PRISM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("EXPERT_MANAWEAVING", discipline).parent("BASIC_MANAWEAVING")
            .stage(ResearchStageBuilder.stage().requiredResearch("b_crafted_manaweaving_expert").build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("MASTER_MANAWEAVING", discipline).parent("EXPERT_MANAWEAVING")
            .stage(ResearchStageBuilder.stage().requiredResearch("b_crafted_manaweaving_master").build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("SUPREME_MANAWEAVING", discipline).parent("MASTER_MANAWEAVING")
            .stage(ResearchStageBuilder.stage().requiredResearch("b_crafted_manaweaving_supreme").build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_CHARGER", discipline).parent("BASIC_MANAWEAVING")
            .stage(ResearchStageBuilder.stage().requiredItemTag(PrimalMagic.MODID, "essences/terrestrial_dusts").requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.WAND_CHARGER.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("MANA_SALTS", discipline).parent("BASIC_MANAWEAVING")
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.ESSENCE_DUST_EARTH.get()).requiredItemStack(ItemsPM.ESSENCE_DUST_SEA.get()).requiredItemStack(ItemsPM.ESSENCE_DUST_SKY.get())
                    .requiredItemStack(ItemsPM.ESSENCE_DUST_SUN.get()).requiredItemStack(ItemsPM.ESSENCE_DUST_MOON.get()).requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.MANA_SALTS.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("ADVANCED_WANDMAKING", discipline).parent("BASIC_MANAWEAVING")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.WAND_ASSEMBLY_TABLE.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("STAVES", discipline).parent("EXPERT_MANAWEAVING").parent("ADVANCED_WANDMAKING")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_CORE_HEARTWOOD", discipline).parent("ADVANCED_WANDMAKING")
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.HEARTWOOD.get()).requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.HEARTWOOD_WAND_CORE_ITEM.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("STAVES").recipe(ItemsPM.HEARTWOOD_STAFF_CORE_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_CAP_IRON", discipline).parent("ADVANCED_WANDMAKING")
            .stage(ResearchStageBuilder.stage().requiredItemTag("forge", "ingots/iron").requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.IRON_WAND_CAP_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_GEM_APPRENTICE", discipline).parent("ADVANCED_WANDMAKING")
            .stage(ResearchStageBuilder.stage().requiredItemTag("forge", "gems/diamond").requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.APPRENTICE_WAND_GEM_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("EARTHSHATTER_HAMMER", discipline).parent("EXPERT_MANAWEAVING").parent("SHARD_SYNTHESIS")
            .stage(ResearchStageBuilder.stage().requiredItemTag("forge", "ores/iron").requiredItemTag("forge", "ores/gold").requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.EARTH, 2).recipe(ItemsPM.EARTHSHATTER_HAMMER.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SUNLAMP", discipline).parent("EXPERT_MANAWEAVING").parent("PRIMALITE")
            .stage(ResearchStageBuilder.stage().requiredItemStack(Items.LANTERN).requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SUN, 2).recipe(ItemsPM.SUNLAMP.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_GEM_ADEPT", discipline).parent("EXPERT_MANAWEAVING").parent("WAND_GEM_APPRENTICE")
            .stage(ResearchStageBuilder.stage().requiredItemTag(PrimalMagic.MODID, "gems/diamond").requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.ADEPT_WAND_GEM_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_GEM_WIZARD", discipline).parent("MASTER_MANAWEAVING").parent("WAND_GEM_ADEPT")
            .stage(ResearchStageBuilder.stage().requiredItemTag(PrimalMagic.MODID, "gems/diamond").requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.WIZARD_WAND_GEM_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_GEM_ARCHMAGE", discipline).parent("SUPREME_MANAWEAVING").parent("WAND_GEM_WIZARD")
            .stage(ResearchStageBuilder.stage().requiredItemTag(PrimalMagic.MODID, "gems/diamond").requiredKnowledge(KnowledgeType.THEORY, 3).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.ARCHMAGE_WAND_GEM_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_CAP_GOLD", discipline).parent("EXPERT_MANAWEAVING").parent("WAND_CAP_IRON")
            .stage(ResearchStageBuilder.stage().requiredItemTag("forge", "ingots/gold").requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.GOLD_WAND_CAP_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_CAP_PRIMALITE", discipline).parent("WAND_CAP_GOLD").parent("PRIMALITE")
            .stage(ResearchStageBuilder.stage().requiredItemTag(PrimalMagic.MODID, "ingots/primalite").requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.PRIMALITE_WAND_CAP_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_CAP_HEXIUM", discipline).parent("MASTER_MANAWEAVING").parent("WAND_CAP_PRIMALITE").parent("HEXIUM")
            .stage(ResearchStageBuilder.stage().requiredItemTag(PrimalMagic.MODID, "ingots/hexium").requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.HEXIUM_WAND_CAP_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_CAP_HALLOWSTEEL", discipline).parent("SUPREME_MANAWEAVING").parent("WAND_CAP_HEXIUM").parent("HALLOWSTEEL")
            .stage(ResearchStageBuilder.stage().requiredItemTag(PrimalMagic.MODID, "ingots/hallowsteel").requiredKnowledge(KnowledgeType.THEORY, 3).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.HALLOWSTEEL_WAND_CAP_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_CORE_OBSIDIAN", discipline).parent("EXPERT_MANAWEAVING").parent("WAND_CORE_HEARTWOOD")
            .stage(ResearchStageBuilder.stage().requiredItemTag("forge", "obsidian").requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.EARTH, 2).recipe(ItemsPM.OBSIDIAN_WAND_CORE_ITEM.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("STAVES").recipe(ItemsPM.OBSIDIAN_STAFF_CORE_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_CORE_CORAL", discipline).parent("EXPERT_MANAWEAVING").parent("WAND_CORE_HEARTWOOD")
            .stage(ResearchStageBuilder.stage().requiredItemTag(PrimalMagic.MODID, "coral_blocks").requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SEA, 2).recipe(ItemsPM.CORAL_WAND_CORE_ITEM.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("STAVES").recipe(ItemsPM.CORAL_STAFF_CORE_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_CORE_BAMBOO", discipline).parent("EXPERT_MANAWEAVING").parent("WAND_CORE_HEARTWOOD")
            .stage(ResearchStageBuilder.stage().requiredItemStack(Items.BAMBOO).requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SKY, 2).recipe(ItemsPM.BAMBOO_WAND_CORE_ITEM.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("STAVES").recipe(ItemsPM.BAMBOO_STAFF_CORE_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_CORE_SUNWOOD", discipline).parent("EXPERT_MANAWEAVING").parent("WAND_CORE_HEARTWOOD")
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.SUNWOOD_LOG.get()).requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SUN, 2).recipe(ItemsPM.SUNWOOD_WAND_CORE_ITEM.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("STAVES").recipe(ItemsPM.SUNWOOD_STAFF_CORE_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_CORE_MOONWOOD", discipline).parent("EXPERT_MANAWEAVING").parent("WAND_CORE_HEARTWOOD")
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.MOONWOOD_LOG.get()).requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.MOON, 2).recipe(ItemsPM.MOONWOOD_WAND_CORE_ITEM.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("STAVES").recipe(ItemsPM.MOONWOOD_STAFF_CORE_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_CORE_BONE", discipline).parent("MASTER_MANAWEAVING").parent("WAND_CORE_HEARTWOOD").parent("t_discover_blood")
            .stage(ResearchStageBuilder.stage().requiredItemStack(Items.BONE).requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.BLOOD, 3).recipe(ItemsPM.BONE_WAND_CORE_ITEM.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("STAVES").recipe(ItemsPM.BONE_STAFF_CORE_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_CORE_BLAZE_ROD", discipline).parent("MASTER_MANAWEAVING").parent("WAND_CORE_HEARTWOOD").parent("t_discover_infernal")
            .stage(ResearchStageBuilder.stage().requiredItemTag("forge", "rods/blaze").requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.INFERNAL, 3).recipe(ItemsPM.BLAZE_ROD_WAND_CORE_ITEM.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("STAVES").recipe(ItemsPM.BLAZE_ROD_STAFF_CORE_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_CORE_PURPUR", discipline).parent("MASTER_MANAWEAVING").parent("WAND_CORE_HEARTWOOD").parent("t_discover_void")
            .stage(ResearchStageBuilder.stage().requiredItemStack(Items.PURPUR_BLOCK).requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.VOID, 3).recipe(ItemsPM.PURPUR_WAND_CORE_ITEM.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("STAVES").recipe(ItemsPM.PURPUR_STAFF_CORE_ITEM.get()).build())
            .build(consumer);
    }
    
    protected void registerAlchemyEntries(Consumer<IFinishedResearchEntry> consumer) {
        String discipline = "ALCHEMY";
        ResearchEntryBuilder.entry("BASIC_ALCHEMY", discipline).parent("UNLOCK_ALCHEMY")
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("EXPERT_ALCHEMY", discipline).parent("BASIC_ALCHEMY")
            .stage(ResearchStageBuilder.stage().requiredResearch("b_crafted_alchemy_expert").build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("MASTER_ALCHEMY", discipline).parent("EXPERT_ALCHEMY")
            .stage(ResearchStageBuilder.stage().requiredResearch("b_crafted_alchemy_master").build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("SUPREME_ALCHEMY", discipline).parent("MASTER_ALCHEMY")
            .stage(ResearchStageBuilder.stage().requiredResearch("b_crafted_alchemy_supreme").build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("STONEMELDING", discipline).parent("BASIC_ALCHEMY")
            .stage(ResearchStageBuilder.stage().recipe(PrimalMagic.MODID, "stone_from_stonemelding").attunement(Source.EARTH, 1).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SKYGLASS", discipline).parent("BASIC_ALCHEMY")
            .stage(ResearchStageBuilder.stage().attunement(Source.SKY, 1)
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
        ResearchEntryBuilder.entry("SHARD_SYNTHESIS", discipline).parent("EXPERT_ALCHEMY")
            .stage(ResearchStageBuilder.stage()
                    .attunement(Source.EARTH, 1).attunement(Source.SEA, 1).attunement(Source.SKY, 1).attunement(Source.SUN, 1).attunement(Source.MOON, 1)
                    .recipe(PrimalMagic.MODID, "essence_shard_earth_from_dust").recipe(PrimalMagic.MODID, "essence_shard_sea_from_dust")
                    .recipe(PrimalMagic.MODID, "essence_shard_sky_from_dust").recipe(PrimalMagic.MODID, "essence_shard_sun_from_dust")
                    .recipe(PrimalMagic.MODID, "essence_shard_moon_from_dust").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_blood").attunement(Source.BLOOD, 1)
                    .recipe(PrimalMagic.MODID, "essence_shard_blood_from_dust").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_infernal").attunement(Source.INFERNAL, 1)
                    .recipe(PrimalMagic.MODID, "essence_shard_infernal_from_dust").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_void").attunement(Source.VOID, 1)
                    .recipe(PrimalMagic.MODID, "essence_shard_void_from_dust").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_hallowed").attunement(Source.HALLOWED, 1)
                    .recipe(PrimalMagic.MODID, "essence_shard_hallowed_from_dust").build())
            .build(consumer);
        ResearchEntryBuilder.entry("SHARD_DESYNTHESIS", discipline).parent("SHARD_SYNTHESIS")
            .stage(ResearchStageBuilder.stage()
                    .attunement(Source.EARTH, 1).attunement(Source.SEA, 1).attunement(Source.SKY, 1).attunement(Source.SUN, 1).attunement(Source.MOON, 1)
                    .recipe(PrimalMagic.MODID, "essence_dust_earth_from_shard").recipe(PrimalMagic.MODID, "essence_dust_sea_from_shard")
                    .recipe(PrimalMagic.MODID, "essence_dust_sky_from_shard").recipe(PrimalMagic.MODID, "essence_dust_sun_from_shard")
                    .recipe(PrimalMagic.MODID, "essence_dust_moon_from_shard").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_blood").attunement(Source.BLOOD, 1)
                    .recipe(PrimalMagic.MODID, "essence_dust_blood_from_shard").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_infernal").attunement(Source.INFERNAL, 1)
                    .recipe(PrimalMagic.MODID, "essence_dust_infernal_from_shard").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_void").attunement(Source.VOID, 1)
                    .recipe(PrimalMagic.MODID, "essence_dust_void_from_shard").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_hallowed").attunement(Source.HALLOWED, 1)
                    .recipe(PrimalMagic.MODID, "essence_dust_hallowed_from_shard").build())
            .build(consumer);
        ResearchEntryBuilder.entry("PRIMALITE", discipline).parent("EXPERT_ALCHEMY")
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.ESSENCE_DUST_EARTH.get()).requiredItemStack(ItemsPM.ESSENCE_DUST_SEA.get())
                    .requiredItemStack(ItemsPM.ESSENCE_DUST_SKY.get()).requiredItemStack(ItemsPM.ESSENCE_DUST_SUN.get()).requiredItemStack(ItemsPM.ESSENCE_DUST_MOON.get())
                    .requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.EARTH, 1).attunement(Source.SEA, 1).attunement(Source.SKY, 1)
                    .attunement(Source.SUN, 1).attunement(Source.MOON, 1).recipe(ItemsPM.PRIMALITE_INGOT.get()).recipe(ItemsPM.PRIMALITE_SWORD.get()).recipe(ItemsPM.PRIMALITE_SHOVEL.get())
                    .recipe(ItemsPM.PRIMALITE_PICKAXE.get()).recipe(ItemsPM.PRIMALITE_AXE.get()).recipe(ItemsPM.PRIMALITE_HOE.get()).recipe(ItemsPM.PRIMALITE_HEAD.get())
                    .recipe(ItemsPM.PRIMALITE_CHEST.get()).recipe(ItemsPM.PRIMALITE_LEGS.get()).recipe(ItemsPM.PRIMALITE_FEET.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("CRYSTAL_SYNTHESIS", discipline).parent("MASTER_ALCHEMY").parent("SHARD_SYNTHESIS")
            .stage(ResearchStageBuilder.stage()
                    .attunement(Source.EARTH, 2).attunement(Source.SEA, 2).attunement(Source.SKY, 2).attunement(Source.SUN, 2).attunement(Source.MOON, 2)
                    .recipe(PrimalMagic.MODID, "essence_crystal_earth_from_shard").recipe(PrimalMagic.MODID, "essence_crystal_sea_from_shard")
                    .recipe(PrimalMagic.MODID, "essence_crystal_sky_from_shard").recipe(PrimalMagic.MODID, "essence_crystal_sun_from_shard")
                    .recipe(PrimalMagic.MODID, "essence_crystal_moon_from_shard").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_blood").attunement(Source.BLOOD, 2)
                    .recipe(PrimalMagic.MODID, "essence_crystal_blood_from_shard").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_infernal").attunement(Source.INFERNAL, 2)
                    .recipe(PrimalMagic.MODID, "essence_crystal_infernal_from_shard").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_void").attunement(Source.VOID, 2)
                    .recipe(PrimalMagic.MODID, "essence_crystal_void_from_shard").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_hallowed").attunement(Source.HALLOWED, 2)
                    .recipe(PrimalMagic.MODID, "essence_crystal_hallowed_from_shard").build())
            .build(consumer);
        ResearchEntryBuilder.entry("CRYSTAL_DESYNTHESIS", discipline).parent("CRYSTAL_SYNTHESIS")
            .stage(ResearchStageBuilder.stage()
                    .attunement(Source.EARTH, 1).attunement(Source.SEA, 1).attunement(Source.SKY, 1).attunement(Source.SUN, 1).attunement(Source.MOON, 1)
                    .recipe(PrimalMagic.MODID, "essence_shard_earth_from_crystal").recipe(PrimalMagic.MODID, "essence_shard_sea_from_crystal")
                    .recipe(PrimalMagic.MODID, "essence_shard_sky_from_crystal").recipe(PrimalMagic.MODID, "essence_shard_sun_from_crystal")
                    .recipe(PrimalMagic.MODID, "essence_shard_moon_from_crystal").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_blood").attunement(Source.BLOOD, 1)
                    .recipe(PrimalMagic.MODID, "essence_shard_blood_from_crystal").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_infernal").attunement(Source.INFERNAL, 1)
                    .recipe(PrimalMagic.MODID, "essence_shard_infernal_from_crystal").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_void").attunement(Source.VOID, 1)
                    .recipe(PrimalMagic.MODID, "essence_shard_void_from_crystal").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_hallowed").attunement(Source.HALLOWED, 1)
                    .recipe(PrimalMagic.MODID, "essence_shard_hallowed_from_crystal").build())
            .build(consumer);
        ResearchEntryBuilder.entry("HEXIUM", discipline).parent("MASTER_ALCHEMY").parent("PRIMALITE").parent("SHARD_SYNTHESIS").parent("t_discover_blood")
            .parent("t_discover_infernal").parent("t_discover_void")
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.ESSENCE_SHARD_BLOOD.get()).requiredItemStack(ItemsPM.ESSENCE_SHARD_INFERNAL.get())
                    .requiredItemStack(ItemsPM.ESSENCE_SHARD_VOID.get()).requiredCraftStack(ItemsPM.PRIMALITE_INGOT.get()).requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.BLOOD, 2).attunement(Source.INFERNAL, 2).attunement(Source.VOID, 2)
                    .recipe(ItemsPM.HEXIUM_INGOT.get()).recipe(ItemsPM.HEXIUM_SWORD.get()).recipe(ItemsPM.HEXIUM_SHOVEL.get()).recipe(ItemsPM.HEXIUM_PICKAXE.get())
                    .recipe(ItemsPM.HEXIUM_AXE.get()).recipe(ItemsPM.HEXIUM_HOE.get()).recipe(ItemsPM.HEXIUM_HEAD.get()).recipe(ItemsPM.HEXIUM_CHEST.get()).recipe(ItemsPM.HEXIUM_LEGS.get())
                    .recipe(ItemsPM.HEXIUM_FEET.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("CLUSTER_SYNTHESIS", discipline).parent("SUPREME_ALCHEMY").parent("CRYSTAL_SYNTHESIS")
            .stage(ResearchStageBuilder.stage()
                    .attunement(Source.EARTH, 3).attunement(Source.SEA, 3).attunement(Source.SKY, 3).attunement(Source.SUN, 3).attunement(Source.MOON, 3)
                    .recipe(PrimalMagic.MODID, "essence_cluster_earth_from_crystal").recipe(PrimalMagic.MODID, "essence_cluster_sea_from_crystal")
                    .recipe(PrimalMagic.MODID, "essence_cluster_sky_from_crystal").recipe(PrimalMagic.MODID, "essence_cluster_sun_from_crystal")
                    .recipe(PrimalMagic.MODID, "essence_cluster_moon_from_crystal").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_blood").attunement(Source.BLOOD, 3)
                    .recipe(PrimalMagic.MODID, "essence_cluster_blood_from_crystal").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_infernal").attunement(Source.INFERNAL, 3)
                    .recipe(PrimalMagic.MODID, "essence_cluster_infernal_from_crystal").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_void").attunement(Source.VOID, 3)
                    .recipe(PrimalMagic.MODID, "essence_cluster_void_from_crystal").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_hallowed").attunement(Source.HALLOWED, 3)
                    .recipe(PrimalMagic.MODID, "essence_cluster_hallowed_from_crystal").build())
            .build(consumer);
        ResearchEntryBuilder.entry("CLUSTER_DESYNTHESIS", discipline).parent("CLUSTER_SYNTHESIS")
            .stage(ResearchStageBuilder.stage()
                    .attunement(Source.EARTH, 1).attunement(Source.SEA, 1).attunement(Source.SKY, 1).attunement(Source.SUN, 1).attunement(Source.MOON, 1)
                    .recipe(PrimalMagic.MODID, "essence_crystal_earth_from_cluster").recipe(PrimalMagic.MODID, "essence_crystal_sea_from_cluster")
                    .recipe(PrimalMagic.MODID, "essence_crystal_sky_from_cluster").recipe(PrimalMagic.MODID, "essence_crystal_sun_from_cluster")
                    .recipe(PrimalMagic.MODID, "essence_crystal_moon_from_cluster").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_blood").attunement(Source.BLOOD, 1)
                    .recipe(PrimalMagic.MODID, "essence_crystal_blood_from_cluster").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_infernal").attunement(Source.INFERNAL, 1)
                    .recipe(PrimalMagic.MODID, "essence_crystal_infernal_from_cluster").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_void").attunement(Source.VOID, 1)
                    .recipe(PrimalMagic.MODID, "essence_crystal_void_from_cluster").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("t_discover_hallowed").attunement(Source.HALLOWED, 1)
                    .recipe(PrimalMagic.MODID, "essence_crystal_hallowed_from_cluster").build())
            .build(consumer);
        ResearchEntryBuilder.entry("HALLOWSTEEL", discipline).parent("SUPREME_ALCHEMY").parent("HEXIUM").parent("CRYSTAL_SYNTHESIS").parent("t_discover_hallowed")
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.ESSENCE_CRYSTAL_HALLOWED.get()).requiredCraftStack(ItemsPM.HEXIUM_INGOT.get())
                    .requiredKnowledge(KnowledgeType.THEORY, 3).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.HALLOWED, 3).recipe(ItemsPM.HALLOWSTEEL_INGOT.get())
                    .recipe(ItemsPM.HALLOWSTEEL_SWORD.get()).recipe(ItemsPM.HALLOWSTEEL_SHOVEL.get()).recipe(ItemsPM.HALLOWSTEEL_PICKAXE.get()).recipe(ItemsPM.HALLOWSTEEL_AXE.get())
                    .recipe(ItemsPM.HALLOWSTEEL_HOE.get()).recipe(ItemsPM.HALLOWSTEEL_HEAD.get()).recipe(ItemsPM.HALLOWSTEEL_CHEST.get()).recipe(ItemsPM.HALLOWSTEEL_LEGS.get())
                    .recipe(ItemsPM.HALLOWSTEEL_FEET.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("CALCINATOR_BASIC", discipline).parent("BASIC_ALCHEMY")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).requiredCraftStack(ItemsPM.ESSENCE_DUST_EARTH.get()).requiredCraftStack(ItemsPM.ESSENCE_DUST_SEA.get())
                    .requiredCraftStack(ItemsPM.ESSENCE_DUST_SKY.get()).requiredCraftStack(ItemsPM.ESSENCE_DUST_SUN.get()).requiredCraftStack(ItemsPM.ESSENCE_DUST_MOON.get()).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.CALCINATOR_BASIC.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("CALCINATOR_ENCHANTED", discipline).parent("EXPERT_ALCHEMY").parent("SHARD_SYNTHESIS").parent("CALCINATOR_BASIC")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.CALCINATOR_ENCHANTED.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("CALCINATOR_FORBIDDEN", discipline).parent("MASTER_ALCHEMY").parent("CRYSTAL_SYNTHESIS").parent("CALCINATOR_ENCHANTED").parent(Source.BLOOD.getDiscoverKey())
            .parent(Source.INFERNAL.getDiscoverKey()).parent(Source.VOID.getDiscoverKey())
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.CALCINATOR_FORBIDDEN.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("CALCINATOR_HEAVENLY", discipline).parent("SUPREME_ALCHEMY").parent("CLUSTER_SYNTHESIS").parent("CALCINATOR_FORBIDDEN").parent(Source.HALLOWED.getDiscoverKey())
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 3).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.CALCINATOR_HEAVENLY.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("CRYOTREATMENT", discipline).parent("BASIC_ALCHEMY")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SEA, 1).recipe(PrimalMagic.MODID, "ice_from_cryotreatment").recipe(PrimalMagic.MODID, "obsidian_from_cryotreatment").build())
            .build(consumer);
    }

    protected void registerSorceryEntries(Consumer<IFinishedResearchEntry> consumer) {
        String discipline = "SORCERY";
        ResearchEntryBuilder.entry("BASIC_SORCERY", discipline).parent("UNLOCK_SORCERY")
            .stage(ResearchStageBuilder.stage().attunement(Source.EARTH, 1)
                    .recipe(ItemsPM.SPELL_SCROLL_BLANK.get()).recipe(ItemsPM.SPELLCRAFTING_ALTAR.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("EXPERT_SORCERY", discipline).parent("BASIC_SORCERY")
            .stage(ResearchStageBuilder.stage().requiredResearch("t_spells_crafted_expert").requiredResearch("t_spells_cast_expert").build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("MASTER_SORCERY", discipline).parent("EXPERT_SORCERY")
            .stage(ResearchStageBuilder.stage().requiredResearch("t_spells_cast_master").requiredResearch("t_spell_cost_master").build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("SUPREME_SORCERY", discipline).parent("MASTER_SORCERY")
            .stage(ResearchStageBuilder.stage().requiredResearch("t_spells_cast_supreme").requiredResearch("t_spell_cost_supreme").build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_INSCRIPTION", discipline).parent("BASIC_SORCERY").parent("ADVANCED_WANDMAKING")
            .stage(ResearchStageBuilder.stage().requiredResearch("t_spells_crafted_expert").requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.WAND_INSCRIPTION_TABLE.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_VEHICLE_PROJECTILE", discipline).parent("EXPERT_SORCERY")
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_VEHICLE_BOLT", discipline).parent("MASTER_SORCERY").parent("SPELL_VEHICLE_PROJECTILE")
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_FROST", discipline).parent("BASIC_SORCERY")
            .stage(ResearchStageBuilder.stage().attunement(Source.SEA, 1).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_LIGHTNING", discipline).parent("BASIC_SORCERY")
            .stage(ResearchStageBuilder.stage().attunement(Source.SKY, 1).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_SOLAR", discipline).parent("BASIC_SORCERY")
            .stage(ResearchStageBuilder.stage().attunement(Source.SUN, 1).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_LUNAR", discipline).parent("BASIC_SORCERY")
            .stage(ResearchStageBuilder.stage().attunement(Source.MOON, 1).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_BLOOD", discipline).parent("EXPERT_SORCERY").parent("t_discover_blood")
            .stage(ResearchStageBuilder.stage().attunement(Source.BLOOD, 1).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_FLAME", discipline).parent("EXPERT_SORCERY").parent("t_discover_infernal")
            .stage(ResearchStageBuilder.stage().attunement(Source.INFERNAL, 1).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_VOID", discipline).parent("EXPERT_SORCERY").parent("t_discover_void")
            .stage(ResearchStageBuilder.stage().attunement(Source.VOID, 1).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_HOLY", discipline).parent("MASTER_SORCERY").parent("t_discover_hallowed")
            .stage(ResearchStageBuilder.stage().attunement(Source.HALLOWED, 1).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_BREAK", discipline).parent("EXPERT_SORCERY")
            .stage(ResearchStageBuilder.stage().attunement(Source.EARTH, 3).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_CONJURE_WATER", discipline).parent("EXPERT_SORCERY").parent("SPELL_PAYLOAD_FROST")
            .stage(ResearchStageBuilder.stage().attunement(Source.SEA, 3).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_FLIGHT", discipline).parent("SUPREME_SORCERY").parent("SPELL_PAYLOAD_LIGHTNING")
            .stage(ResearchStageBuilder.stage().attunement(Source.SKY, 3).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_HEALING", discipline).parent("EXPERT_SORCERY").parent("SPELL_PAYLOAD_SOLAR")
            .stage(ResearchStageBuilder.stage().attunement(Source.SUN, 3).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_POLYMORPH", discipline).parent("EXPERT_SORCERY").parent("SPELL_PAYLOAD_LUNAR")
            .stage(ResearchStageBuilder.stage().attunement(Source.MOON, 3).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_CONJURE_ANIMAL", discipline).parent("MASTER_SORCERY").parent("SPELL_PAYLOAD_BLOOD")
            .stage(ResearchStageBuilder.stage().attunement(Source.BLOOD, 3).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_CONJURE_LAVA", discipline).parent("MASTER_SORCERY").parent("SPELL_PAYLOAD_CONJURE_WATER").parent("SPELL_PAYLOAD_FLAME")
            .stage(ResearchStageBuilder.stage().attunement(Source.INFERNAL, 3).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_DRAIN_SOUL", discipline).parent("MASTER_SORCERY").parent("SPELL_PAYLOAD_FLAME")
            .stage(ResearchStageBuilder.stage().attunement(Source.INFERNAL, 3).recipe(ItemsPM.SOUL_GEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_TELEPORT", discipline).parent("MASTER_SORCERY").parent("SPELL_PAYLOAD_VOID")
            .stage(ResearchStageBuilder.stage().attunement(Source.VOID, 3).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_PAYLOAD_CONSECRATE", discipline).parent("SUPREME_SORCERY").parent("SPELL_PAYLOAD_HOLY")
            .stage(ResearchStageBuilder.stage().attunement(Source.HALLOWED, 3).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_MOD_AMPLIFY", discipline).parent("EXPERT_SORCERY")
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_MOD_MINE", discipline).parent("EXPERT_SORCERY")
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_MOD_QUICKEN", discipline).parent("MASTER_SORCERY").parent("SPELL_MOD_AMPLIFY")
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_MOD_BURST", discipline).parent("MASTER_SORCERY").parent("SPELL_MOD_MINE")
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("SPELL_MOD_FORK", discipline).parent("SUPREME_SORCERY").parent("SPELL_MOD_QUICKEN").parent("SPELL_MOD_BURST")
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
    }

    protected void registerRuneworkingEntries(Consumer<IFinishedResearchEntry> consumer) {
        String discipline = "RUNEWORKING";
        ResearchEntryBuilder.entry("BASIC_RUNEWORKING", discipline).parent("UNLOCK_RUNEWORKING")
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.RUNE_UNATTUNED.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("EXPERT_RUNEWORKING", discipline).parent("BASIC_RUNEWORKING")
            .stage(ResearchStageBuilder.stage().requiredResearch("b_crafted_runeworking_expert").requiredResearch("t_items_runescribed_expert").build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("MASTER_RUNEWORKING", discipline).parent("EXPERT_RUNEWORKING")
            .stage(ResearchStageBuilder.stage().requiredResearch("b_crafted_runeworking_master").requiredResearch("t_items_runescribed_master").build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("SUPREME_RUNEWORKING", discipline).parent("MASTER_RUNEWORKING")
            .stage(ResearchStageBuilder.stage().requiredResearch("b_crafted_runeworking_supreme").requiredResearch("t_items_runescribed_supreme").build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_EARTH", discipline).parent("BASIC_RUNEWORKING")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.EARTH, 1).recipe(ItemsPM.RUNE_EARTH.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_SEA", discipline).parent("BASIC_RUNEWORKING")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SEA, 1).recipe(ItemsPM.RUNE_SEA.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_SKY", discipline).parent("BASIC_RUNEWORKING")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SKY, 1).recipe(ItemsPM.RUNE_SKY.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_SUN", discipline).parent("BASIC_RUNEWORKING")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SUN, 1).recipe(ItemsPM.RUNE_SUN.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_MOON", discipline).parent("BASIC_RUNEWORKING")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.MOON, 1).recipe(ItemsPM.RUNE_MOON.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_PROJECT", discipline).parent("BASIC_RUNEWORKING")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.RUNE_PROJECT.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_PROTECT", discipline).parent("BASIC_RUNEWORKING")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.RUNE_PROTECT.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_ITEM", discipline).parent("BASIC_RUNEWORKING")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.RUNE_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_SELF", discipline).parent("BASIC_RUNEWORKING")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.RUNE_SELF.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_BLOOD", discipline).parent("EXPERT_RUNEWORKING").parent("t_discover_blood")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.BLOOD, 1).recipe(ItemsPM.RUNE_BLOOD.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_INFERNAL", discipline).parent("EXPERT_RUNEWORKING").parent("t_discover_infernal")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.INFERNAL, 1).recipe(ItemsPM.RUNE_INFERNAL.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_VOID", discipline).parent("EXPERT_RUNEWORKING").parent("t_discover_void")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.VOID, 1).recipe(ItemsPM.RUNE_VOID.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_ABSORB", discipline).parent("EXPERT_RUNEWORKING")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.RUNE_ABSORB.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_DISPEL", discipline).parent("EXPERT_RUNEWORKING")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.RUNE_DISPEL.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_SUMMON", discipline).parent("EXPERT_RUNEWORKING")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.RUNE_SUMMON.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_AREA", discipline).parent("EXPERT_RUNEWORKING")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.RUNE_AREA.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_CREATURE", discipline).parent("EXPERT_RUNEWORKING")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.RUNE_CREATURE.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_HALLOWED", discipline).parent("MASTER_RUNEWORKING").parent("t_discover_hallowed")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.RUNE_HALLOWED.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_POWER", discipline).parent("MASTER_RUNEWORKING").parent("CRYSTAL_SYNTHESIS").parent("t_discover_blood").parent("t_discover_infernal").parent("t_discover_void")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.RUNE_POWER.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNIC_GRINDSTONE", discipline).parent("RUNE_DISPEL")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.RUNIC_GRINDSTONE.get()).build())
            .build(consumer);
    }

    protected void registerRitualEntries(Consumer<IFinishedResearchEntry> consumer) {
        String discipline = "RITUAL";
        ResearchEntryBuilder.entry("BASIC_RITUAL", discipline).parent("UNLOCK_RITUAL")
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.RITUAL_ALTAR.get()).recipe(ItemsPM.OFFERING_PEDESTAL.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("EXPERT_RITUAL", discipline).parent("BASIC_RITUAL")
            .stage(ResearchStageBuilder.stage().requiredResearch("t_rituals_completed_expert").build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("MASTER_RITUAL", discipline).parent("EXPERT_RITUAL")
            .stage(ResearchStageBuilder.stage().requiredResearch("t_rituals_completed_master").build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("SUPREME_RITUAL", discipline).parent("MASTER_RITUAL")
            .stage(ResearchStageBuilder.stage().requiredResearch("t_rituals_completed_supreme").build())
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
        ResearchEntryBuilder.entry("MANAFRUIT", discipline).parent("BASIC_RITUAL").parent("MANA_SALTS").parent("RITUAL_CANDLES")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.MANAFRUIT.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RITUAL_CANDLES", discipline).parent("BASIC_RITUAL")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SUN, 1).recipe(ItemsPM.TALLOW.get()).recipe(PrimalMagic.MODID, "ritual_candle_white_from_tallow")
                    .recipe(PrimalMagic.MODID, "ritual_candle_white_from_beeswax").recipe(ItemsPM.RITUAL_CANDLE_BLACK.get()).recipe(ItemsPM.RITUAL_CANDLE_BLUE.get())
                    .recipe(ItemsPM.RITUAL_CANDLE_BROWN.get()).recipe(ItemsPM.RITUAL_CANDLE_CYAN.get()).recipe(ItemsPM.RITUAL_CANDLE_GRAY.get()).recipe(ItemsPM.RITUAL_CANDLE_GREEN.get())
                    .recipe(ItemsPM.RITUAL_CANDLE_LIGHT_BLUE.get()).recipe(ItemsPM.RITUAL_CANDLE_LIGHT_GRAY.get()).recipe(ItemsPM.RITUAL_CANDLE_LIME.get()).recipe(ItemsPM.RITUAL_CANDLE_MAGENTA.get())
                    .recipe(ItemsPM.RITUAL_CANDLE_ORANGE.get()).recipe(ItemsPM.RITUAL_CANDLE_PINK.get()).recipe(ItemsPM.RITUAL_CANDLE_PURPLE.get()).recipe(ItemsPM.RITUAL_CANDLE_RED.get())
                    .recipe(ItemsPM.RITUAL_CANDLE_WHITE.get()).recipe(ItemsPM.RITUAL_CANDLE_WHITE.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("INCENSE_BRAZIER", discipline).parent("BASIC_RITUAL")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SKY, 1).recipe(ItemsPM.INCENSE_BRAZIER.get()).recipe(ItemsPM.INCENSE_STICK.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RITUAL_LECTERN", discipline).parent("EXPERT_RITUAL")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.MOON, 1).recipe(ItemsPM.RITUAL_LECTERN.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RITUAL_BELL", discipline).parent("EXPERT_RITUAL")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.SEA, 1).recipe(ItemsPM.RITUAL_BELL.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("BLOODLETTER", discipline).parent("MASTER_RITUAL").parent("t_discover_blood")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.BLOOD, 1).recipe(ItemsPM.BLOODLETTER.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SOUL_ANVIL", discipline).parent("MASTER_RITUAL").parent("HEXIUM").parent("SPELL_PAYLOAD_DRAIN_SOUL")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.INFERNAL, 1).recipe(ItemsPM.SOUL_ANVIL.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_CORE_PRIMAL", discipline).parent("EXPERT_RITUAL").parent("WAND_CORE_OBSIDIAN").parent("WAND_CORE_CORAL").parent("WAND_CORE_BAMBOO").parent("WAND_CORE_SUNWOOD")
            .parent("WAND_CORE_MOONWOOD").parent("MANA_SALTS").parent("RITUAL_CANDLES").parent("RITUAL_LECTERN").parent("RITUAL_BELL")
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.HEARTWOOD_WAND_CORE_ITEM.get()).requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.EARTH, 3).attunement(Source.SEA, 3).attunement(Source.SKY, 3).attunement(Source.SUN, 3).attunement(Source.MOON, 3)
                    .recipe(ItemsPM.PRIMAL_WAND_CORE_ITEM.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("STAVES").recipe(ItemsPM.PRIMAL_STAFF_CORE_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_CORE_DARK_PRIMAL", discipline).parent("MASTER_RITUAL").parent("WAND_CORE_PRIMAL").parent("WAND_CORE_BONE").parent("WAND_CORE_BLAZE_ROD")
            .parent("WAND_CORE_PURPUR").parent("BLOODLETTER").parent("SOUL_ANVIL")
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.HEARTWOOD_WAND_CORE_ITEM.get()).requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.BLOOD, 4).attunement(Source.INFERNAL, 4).attunement(Source.VOID, 4).recipe(ItemsPM.DARK_PRIMAL_WAND_CORE_ITEM.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("STAVES").recipe(ItemsPM.DARK_PRIMAL_STAFF_CORE_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("PIXIES", discipline).parent("EXPERT_RITUAL").parent("MANA_SALTS").parent("SHARD_SYNTHESIS").parent("RUNE_SUMMON").parent("RUNE_CREATURE").parent("INCENSE_BRAZIER")
            .parent("RITUAL_BELL")
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.RUNE_SUMMON.get()).requiredItemStack(ItemsPM.RUNE_CREATURE.get()).requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.EARTH, 2).attunement(Source.SEA, 2).attunement(Source.SKY, 2).attunement(Source.SUN, 2).attunement(Source.MOON, 2)
                    .recipe(ItemsPM.BASIC_EARTH_PIXIE.get()).recipe(ItemsPM.BASIC_SEA_PIXIE.get()).recipe(ItemsPM.BASIC_SKY_PIXIE.get()).recipe(ItemsPM.BASIC_SUN_PIXIE.get())
                    .recipe(ItemsPM.BASIC_MOON_PIXIE.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.BLOOD.getDiscoverKey()).attunement(Source.BLOOD, 2).recipe(ItemsPM.BASIC_BLOOD_PIXIE.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.INFERNAL.getDiscoverKey()).attunement(Source.INFERNAL, 2).recipe(ItemsPM.BASIC_INFERNAL_PIXIE.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.VOID.getDiscoverKey()).attunement(Source.VOID, 2).recipe(ItemsPM.BASIC_VOID_PIXIE.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.HALLOWED.getDiscoverKey()).attunement(Source.HALLOWED, 2).recipe(ItemsPM.BASIC_HALLOWED_PIXIE.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("GRAND_PIXIES", discipline).parent("MASTER_RITUAL").parent("PIXIES").parent("CRYSTAL_SYNTHESIS").parent("RUNE_POWER").parent("BLOODLETTER")
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.RUNE_POWER.get()).requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.EARTH, 3).attunement(Source.SEA, 3).attunement(Source.SKY, 3).attunement(Source.SUN, 3).attunement(Source.MOON, 3)
                    .recipe(ItemsPM.GRAND_EARTH_PIXIE.get()).recipe(ItemsPM.GRAND_SEA_PIXIE.get()).recipe(ItemsPM.GRAND_SKY_PIXIE.get()).recipe(ItemsPM.GRAND_SUN_PIXIE.get())
                    .recipe(ItemsPM.GRAND_MOON_PIXIE.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.BLOOD.getDiscoverKey()).attunement(Source.BLOOD, 3).recipe(ItemsPM.GRAND_BLOOD_PIXIE.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.INFERNAL.getDiscoverKey()).attunement(Source.INFERNAL, 3).recipe(ItemsPM.GRAND_INFERNAL_PIXIE.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.VOID.getDiscoverKey()).attunement(Source.VOID, 3).recipe(ItemsPM.GRAND_VOID_PIXIE.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.HALLOWED.getDiscoverKey()).attunement(Source.HALLOWED, 3).recipe(ItemsPM.GRAND_HALLOWED_PIXIE.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("MAJESTIC_PIXIES", discipline).parent("SUPREME_RITUAL").parent("GRAND_PIXIES").parent("CLUSTER_SYNTHESIS").parent("SOUL_ANVIL")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 3).build())
            .stage(ResearchStageBuilder.stage().attunement(Source.EARTH, 4).attunement(Source.SEA, 4).attunement(Source.SKY, 4).attunement(Source.SUN, 4).attunement(Source.MOON, 4)
                    .recipe(ItemsPM.MAJESTIC_EARTH_PIXIE.get()).recipe(ItemsPM.MAJESTIC_SEA_PIXIE.get()).recipe(ItemsPM.MAJESTIC_SKY_PIXIE.get()).recipe(ItemsPM.MAJESTIC_SUN_PIXIE.get())
                    .recipe(ItemsPM.MAJESTIC_MOON_PIXIE.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.BLOOD.getDiscoverKey()).attunement(Source.BLOOD, 4).recipe(ItemsPM.MAJESTIC_BLOOD_PIXIE.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.INFERNAL.getDiscoverKey()).attunement(Source.INFERNAL, 4).recipe(ItemsPM.MAJESTIC_INFERNAL_PIXIE.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.VOID.getDiscoverKey()).attunement(Source.VOID, 4).recipe(ItemsPM.MAJESTIC_VOID_PIXIE.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(Source.HALLOWED.getDiscoverKey()).attunement(Source.HALLOWED, 4).recipe(ItemsPM.MAJESTIC_HALLOWED_PIXIE.get()).build())
            .build(consumer);
    }

    protected void registerMagitechEntries(Consumer<IFinishedResearchEntry> consumer) {
        String discipline = "MAGITECH";
        ResearchEntryBuilder.entry("BASIC_MAGITECH", discipline).parent("UNLOCK_MAGITECH")
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.MAGITECH_PARTS_BASIC.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("EXPERT_MAGITECH", discipline).parent("BASIC_MAGITECH")
            .stage(ResearchStageBuilder.stage().requiredResearch("b_crafted_magitech_expert").requiredResearch("b_scan_primalite").build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.MAGITECH_PARTS_ENCHANTED.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("MASTER_MAGITECH", discipline).parent("EXPERT_MAGITECH")
            .stage(ResearchStageBuilder.stage().requiredResearch("b_crafted_magitech_master").requiredResearch("b_scan_hexium").build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.MAGITECH_PARTS_FORBIDDEN.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SUPREME_MAGITECH", discipline).parent("MASTER_MAGITECH")
            .stage(ResearchStageBuilder.stage().requiredResearch("b_crafted_magitech_supreme").requiredResearch("b_scan_hallowsteel").build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.MAGITECH_PARTS_HEAVENLY.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("HONEY_EXTRACTOR", discipline).parent("BASIC_MAGITECH")
            .stage(ResearchStageBuilder.stage().requiredItemStack(Items.HONEYCOMB).requiredItemStack(Items.HONEY_BOTTLE).requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.HONEY_EXTRACTOR.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SEASCRIBE_PEN", discipline).parent("BASIC_MAGITECH").parent("THEORYCRAFTING")
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.ENCHANTED_INK.get()).requiredResearch("t_research_projects_completed").requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.SEASCRIBE_PEN.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("ARCANOMETER", discipline).parent("EXPERT_MAGITECH")
            .stage(ResearchStageBuilder.stage().requiredResearch("t_items_analyzed").requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.ARCANOMETER.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("PRIMALITE_GOLEM", discipline).parent("EXPERT_MAGITECH")
            .stage(ResearchStageBuilder.stage().requiredResearch("t_golem").requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.PRIMALITE_GOLEM_CONTROLLER.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("HEXIUM_GOLEM", discipline).parent("MASTER_MAGITECH").parent("PRIMALITE_GOLEM")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.HEXIUM_GOLEM_CONTROLLER.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("HALLOWSTEEL_GOLEM", discipline).parent("SUPREME_MAGITECH").parent("HEXIUM_GOLEM")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 3).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.HALLOWSTEEL_GOLEM_CONTROLLER.get()).build())
            .build(consumer);
    }

    protected void registerScanEntries(Consumer<IFinishedResearchEntry> consumer) {
        String discipline = "SCANS";
        ResearchEntryBuilder.entry("RAW_MARBLE", discipline).parent("UNLOCK_SCANS")
            .stage(ResearchStageBuilder.stage().build())
            .build(consumer);
    }
    
    @Override
    public String getName() {
        return "Primal Magic Grimoire Research";
    }
}
