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
        // TODO Implement method stub
        this.registerBasicsEntries(consumer);
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
        // TODO Remaining research entries
    }
    
    @Override
    public String getName() {
        return "Primal Magic Grimoire Research";
    }
}
