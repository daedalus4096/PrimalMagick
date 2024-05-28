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
        this.registerRuneworkingEntries(consumer);
        this.registerRitualEntries(consumer);
        this.registerMagitechEntries(consumer);
    }

    protected void registerRuneworkingEntries(Consumer<IFinishedResearchEntry> consumer) {
        String discipline = "RUNEWORKING";
        ResearchEntryBuilder.entry("RUNE_EARTH", discipline).icon(ItemsPM.RUNE_EARTH.get()).parent("BASIC_RUNEWORKING")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Sources.EARTH, 1).recipe(ItemsPM.RUNE_EARTH.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_SEA", discipline).icon(ItemsPM.RUNE_SEA.get()).parent("RUNE_EARTH")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Sources.SEA, 1).recipe(ItemsPM.RUNE_SEA.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_SKY", discipline).icon(ItemsPM.RUNE_SKY.get()).parent("RUNE_EARTH")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Sources.SKY, 1).recipe(ItemsPM.RUNE_SKY.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_SUN", discipline).icon(ItemsPM.RUNE_SUN.get()).parent("RUNE_SKY")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Sources.SUN, 1).recipe(ItemsPM.RUNE_SUN.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_MOON", discipline).icon(ItemsPM.RUNE_MOON.get()).parent("RUNE_SEA")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Sources.MOON, 1).recipe(ItemsPM.RUNE_MOON.get()).build())
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
        ResearchEntryBuilder.entry("RUNE_BLOOD", discipline).icon(ItemsPM.RUNE_BLOOD.get()).parent("EXPERT_RUNEWORKING").parent(ResearchEntries.DISCOVER_BLOOD)
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Sources.BLOOD, 1).recipe(ItemsPM.RUNE_BLOOD.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_INFERNAL", discipline).icon(ItemsPM.RUNE_INFERNAL.get()).parent("EXPERT_RUNEWORKING").parent(ResearchEntries.DISCOVER_INFERNAL)
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Sources.INFERNAL, 1).recipe(ItemsPM.RUNE_INFERNAL.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_VOID", discipline).icon(ItemsPM.RUNE_VOID.get()).parent("EXPERT_RUNEWORKING").parent(ResearchEntries.DISCOVER_VOID)
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Sources.VOID, 1).recipe(ItemsPM.RUNE_VOID.get()).build())
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
        ResearchEntryBuilder.entry("RUNE_HALLOWED", discipline).icon(ItemsPM.RUNE_HALLOWED.get()).parent("MASTER_RUNEWORKING").parent(ResearchEntries.DISCOVER_HALLOWED)
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.RUNE_HALLOWED.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_INSIGHT", discipline).icon(ItemsPM.RUNE_INSIGHT.get()).parent("EXPERT_RUNEWORKING").parent("SHARD_SYNTHESIS")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.RUNE_INSIGHT.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_POWER", discipline).icon(ItemsPM.RUNE_POWER.get()).parent("MASTER_RUNEWORKING").parent("CRYSTAL_SYNTHESIS").parent(ResearchEntries.DISCOVER_BLOOD)
            .parent(ResearchEntries.DISCOVER_INFERNAL).parent(ResearchEntries.DISCOVER_VOID).parent("RUNE_INSIGHT")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().sibling("t_sotu_research_power_rune").recipe(ItemsPM.RUNE_POWER.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RUNE_GRACE", discipline).icon(ItemsPM.RUNE_GRACE.get()).parent("SUPREME_RUNEWORKING").parent("CLUSTER_SYNTHESIS").parent(ResearchEntries.DISCOVER_HALLOWED)
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
        ResearchEntryBuilder.entry("ENDERWARD", discipline).icon(ItemsPM.ENDERWARD.get()).parent("MASTER_RUNEWORKING").parent("RUNE_DISPEL").parent(ResearchEntries.DISCOVER_VOID)
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().attunement(Sources.VOID, 3).recipe(ItemsPM.ENDERWARD.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("RECALL_STONE").build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("SPELL_PAYLOAD_TELEPORT").build())
            .build(consumer);
    }

    protected void registerRitualEntries(Consumer<IFinishedResearchEntry> consumer) {
        String discipline = "RITUAL";
        ResearchEntryBuilder.entry("MANAFRUIT", discipline).icon(ItemsPM.MANAFRUIT.get()).parent("BASIC_RITUAL").parent("MANA_SALTS").parent("RITUAL_CANDLES")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.MANAFRUIT.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RITUAL_CANDLES", discipline).icon(ItemsPM.RITUAL_CANDLE_WHITE.get()).parent("BASIC_RITUAL")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Sources.SUN, 1).recipe(ItemsPM.TALLOW.get()).recipe(PrimalMagick.MODID, "ritual_candle_white_from_tallow")
                    .recipe(ItemsPM.RITUAL_CANDLE_BLACK.get()).recipe(ItemsPM.RITUAL_CANDLE_BLUE.get()).recipe(ItemsPM.RITUAL_CANDLE_BROWN.get()).recipe(ItemsPM.RITUAL_CANDLE_CYAN.get())
                    .recipe(ItemsPM.RITUAL_CANDLE_GRAY.get()).recipe(ItemsPM.RITUAL_CANDLE_GREEN.get()).recipe(ItemsPM.RITUAL_CANDLE_LIGHT_BLUE.get()).recipe(ItemsPM.RITUAL_CANDLE_LIGHT_GRAY.get())
                    .recipe(ItemsPM.RITUAL_CANDLE_LIME.get()).recipe(ItemsPM.RITUAL_CANDLE_MAGENTA.get()).recipe(ItemsPM.RITUAL_CANDLE_ORANGE.get()).recipe(ItemsPM.RITUAL_CANDLE_PINK.get())
                    .recipe(ItemsPM.RITUAL_CANDLE_PURPLE.get()).recipe(ItemsPM.RITUAL_CANDLE_RED.get()).recipe(ItemsPM.RITUAL_CANDLE_WHITE.get()).recipe(ItemsPM.RITUAL_CANDLE_YELLOW.get())
                    .build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("HONEY_EXTRACTOR").recipe(PrimalMagick.MODID, "ritual_candle_white_from_beeswax").build())
            .build(consumer);
        ResearchEntryBuilder.entry("INCENSE_BRAZIER", discipline).icon(ItemsPM.INCENSE_BRAZIER.get()).parent("BASIC_RITUAL")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Sources.SKY, 1).recipe(ItemsPM.INCENSE_BRAZIER.get()).recipe(ItemsPM.INCENSE_STICK.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RITUAL_LECTERN", discipline).icon(ItemsPM.RITUAL_LECTERN.get()).parent("EXPERT_RITUAL")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Sources.MOON, 1).recipe(ItemsPM.RITUAL_LECTERN.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("RITUAL_BELL", discipline).icon(ItemsPM.RITUAL_BELL.get()).parent("EXPERT_RITUAL")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Sources.SEA, 1).recipe(ItemsPM.RITUAL_BELL.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("BLOODLETTER", discipline).icon(ItemsPM.BLOODLETTER.get()).parent("MASTER_RITUAL").parent("t_discover_blood")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().attunement(Sources.BLOOD, 1).recipe(ItemsPM.BLOODLETTER.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SOUL_ANVIL", discipline).icon(ItemsPM.SOUL_ANVIL.get()).parent("MASTER_RITUAL").parent("HEXIUM").parent("SPELL_PAYLOAD_DRAIN_SOUL")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().attunement(Sources.INFERNAL, 1).recipe(ItemsPM.SOUL_ANVIL.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("CELESTIAL_HARP", discipline).icon(ItemsPM.CELESTIAL_HARP.get()).parent("SUPREME_RITUAL").parent(ResearchEntries.DISCOVER_HALLOWED)
            .stage(ResearchStageBuilder.stage().requiredItemStack(Items.NOTE_BLOCK).requiredItemStack(Items.JUKEBOX).requiredKnowledge(KnowledgeType.THEORY, 3).build())
            .stage(ResearchStageBuilder.stage().attunement(Sources.HALLOWED, 1).recipe(ItemsPM.CELESTIAL_HARP.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_CORE_PRIMAL", discipline).icon(ItemsPM.PRIMAL_WAND_CORE_ITEM.get()).parent("EXPERT_RITUAL").parent("WAND_CORE_OBSIDIAN")
            .parent("WAND_CORE_CORAL").parent("WAND_CORE_BAMBOO").parent("WAND_CORE_SUNWOOD").parent("WAND_CORE_MOONWOOD").parent("MANA_SALTS").parent("RITUAL_CANDLES")
            .parent("RITUAL_LECTERN").parent("RITUAL_BELL")
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.HEARTWOOD_WAND_CORE_ITEM.get()).requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Sources.EARTH, 3).attunement(Sources.SEA, 3).attunement(Sources.SKY, 3).attunement(Sources.SUN, 3).attunement(Sources.MOON, 3)
                    .recipe(ItemsPM.PRIMAL_WAND_CORE_ITEM.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("STAVES").recipe(ItemsPM.PRIMAL_STAFF_CORE_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_CORE_DARK_PRIMAL", discipline).icon(ItemsPM.DARK_PRIMAL_WAND_CORE_ITEM.get()).parent("MASTER_RITUAL").parent("WAND_CORE_PRIMAL")
            .parent("WAND_CORE_BONE").parent("WAND_CORE_BLAZE_ROD").parent("WAND_CORE_PURPUR").parent("BLOODLETTER").parent("SOUL_ANVIL")
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.HEARTWOOD_WAND_CORE_ITEM.get()).requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().attunement(Sources.BLOOD, 4).attunement(Sources.INFERNAL, 4).attunement(Sources.VOID, 4).recipe(ItemsPM.DARK_PRIMAL_WAND_CORE_ITEM.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("STAVES").recipe(ItemsPM.DARK_PRIMAL_STAFF_CORE_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("WAND_CORE_PURE_PRIMAL", discipline).icon(ItemsPM.PURE_PRIMAL_WAND_CORE_ITEM.get()).parent("SUPREME_RITUAL").parent("WAND_CORE_DARK_PRIMAL")
            .parent("CELESTIAL_HARP")
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.HEARTWOOD_WAND_CORE_ITEM.get()).requiredKnowledge(KnowledgeType.THEORY, 3).build())
            .stage(ResearchStageBuilder.stage().attunement(Sources.HALLOWED, 5).recipe(ItemsPM.PURE_PRIMAL_WAND_CORE_ITEM.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("STAVES").recipe(ItemsPM.PURE_PRIMAL_STAFF_CORE_ITEM.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("PIXIES", discipline).icon(ItemsPM.BASIC_EARTH_PIXIE.get()).parent("EXPERT_RITUAL").parent("MANA_SALTS").parent("SHARD_SYNTHESIS")
            .parent("RUNE_SUMMON").parent("RUNE_CREATURE").parent("INCENSE_BRAZIER").parent("RITUAL_BELL")
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.RUNE_SUMMON.get()).requiredItemStack(ItemsPM.RUNE_CREATURE.get()).requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Sources.EARTH, 2).attunement(Sources.SEA, 2).attunement(Sources.SKY, 2).attunement(Sources.SUN, 2).attunement(Sources.MOON, 2)
                    .recipe(ItemsPM.BASIC_EARTH_PIXIE.get()).recipe(PrimalMagick.resource("pixie_basic_earth_revive"))
                    .recipe(ItemsPM.BASIC_SEA_PIXIE.get()).recipe(PrimalMagick.resource("pixie_basic_sea_revive"))
                    .recipe(ItemsPM.BASIC_SKY_PIXIE.get()).recipe(PrimalMagick.resource("pixie_basic_sky_revive"))
                    .recipe(ItemsPM.BASIC_SUN_PIXIE.get()).recipe(PrimalMagick.resource("pixie_basic_sun_revive"))
                    .recipe(ItemsPM.BASIC_MOON_PIXIE.get()).recipe(PrimalMagick.resource("pixie_basic_moon_revive")).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(ResearchEntries.DISCOVER_BLOOD).attunement(Sources.BLOOD, 2).recipe(ItemsPM.BASIC_BLOOD_PIXIE.get())
                    .recipe(PrimalMagick.resource("pixie_basic_blood_revive")).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(ResearchEntries.DISCOVER_INFERNAL).attunement(Sources.INFERNAL, 2).recipe(ItemsPM.BASIC_INFERNAL_PIXIE.get())
                    .recipe(PrimalMagick.resource("pixie_basic_infernal_revive")).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(ResearchEntries.DISCOVER_VOID).attunement(Sources.VOID, 2).recipe(ItemsPM.BASIC_VOID_PIXIE.get())
                    .recipe(PrimalMagick.resource("pixie_basic_void_revive")).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(ResearchEntries.DISCOVER_HALLOWED).attunement(Sources.HALLOWED, 2).recipe(ItemsPM.BASIC_HALLOWED_PIXIE.get())
                    .recipe(PrimalMagick.resource("pixie_basic_hallowed_revive")).build())
            .build(consumer);
        ResearchEntryBuilder.entry("GRAND_PIXIES", discipline).icon(ItemsPM.GRAND_EARTH_PIXIE.get()).parent("MASTER_RITUAL").parent("PIXIES").parent("CRYSTAL_SYNTHESIS")
            .parent("RUNE_POWER").parent("SOUL_ANVIL")
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.RUNE_INSIGHT.get()).requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().attunement(Sources.EARTH, 3).attunement(Sources.SEA, 3).attunement(Sources.SKY, 3).attunement(Sources.SUN, 3).attunement(Sources.MOON, 3)
                    .recipe(ItemsPM.GRAND_EARTH_PIXIE.get()).recipe(PrimalMagick.resource("pixie_grand_earth_revive"))
                    .recipe(ItemsPM.GRAND_SEA_PIXIE.get()).recipe(PrimalMagick.resource("pixie_grand_sea_revive"))
                    .recipe(ItemsPM.GRAND_SKY_PIXIE.get()).recipe(PrimalMagick.resource("pixie_grand_sky_revive"))
                    .recipe(ItemsPM.GRAND_SUN_PIXIE.get()).recipe(PrimalMagick.resource("pixie_grand_sun_revive"))
                    .recipe(ItemsPM.GRAND_MOON_PIXIE.get()).recipe(PrimalMagick.resource("pixie_grand_moon_revive")).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(ResearchEntries.DISCOVER_BLOOD).attunement(Sources.BLOOD, 3).recipe(ItemsPM.GRAND_BLOOD_PIXIE.get())
                    .recipe(PrimalMagick.resource("pixie_grand_blood_revive")).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(ResearchEntries.DISCOVER_INFERNAL).attunement(Sources.INFERNAL, 3).recipe(ItemsPM.GRAND_INFERNAL_PIXIE.get())
                    .recipe(PrimalMagick.resource("pixie_grand_infernal_revive")).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(ResearchEntries.DISCOVER_VOID).attunement(Sources.VOID, 3).recipe(ItemsPM.GRAND_VOID_PIXIE.get())
                    .recipe(PrimalMagick.resource("pixie_grand_void_revive")).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(ResearchEntries.DISCOVER_HALLOWED).attunement(Sources.HALLOWED, 3).recipe(ItemsPM.GRAND_HALLOWED_PIXIE.get())
                    .recipe(PrimalMagick.resource("pixie_grand_hallowed_revive")).build())
            .build(consumer);
        ResearchEntryBuilder.entry("MAJESTIC_PIXIES", discipline).icon(ItemsPM.MAJESTIC_EARTH_PIXIE.get()).parent("SUPREME_RITUAL").parent("GRAND_PIXIES").parent("CLUSTER_SYNTHESIS")
            .parent("CELESTIAL_HARP")
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.RUNE_POWER.get()).requiredKnowledge(KnowledgeType.THEORY, 3).build())
            .stage(ResearchStageBuilder.stage().attunement(Sources.EARTH, 4).attunement(Sources.SEA, 4).attunement(Sources.SKY, 4).attunement(Sources.SUN, 4).attunement(Sources.MOON, 4)
                    .recipe(ItemsPM.MAJESTIC_EARTH_PIXIE.get()).recipe(PrimalMagick.resource("pixie_majestic_earth_revive"))
                    .recipe(ItemsPM.MAJESTIC_SEA_PIXIE.get()).recipe(PrimalMagick.resource("pixie_majestic_sea_revive"))
                    .recipe(ItemsPM.MAJESTIC_SKY_PIXIE.get()).recipe(PrimalMagick.resource("pixie_majestic_sky_revive"))
                    .recipe(ItemsPM.MAJESTIC_SUN_PIXIE.get()).recipe(PrimalMagick.resource("pixie_majestic_sun_revive"))
                    .recipe(ItemsPM.MAJESTIC_MOON_PIXIE.get()).recipe(PrimalMagick.resource("pixie_majestic_moon_revive")).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(ResearchEntries.DISCOVER_BLOOD).attunement(Sources.BLOOD, 4).recipe(ItemsPM.MAJESTIC_BLOOD_PIXIE.get())
                    .recipe(PrimalMagick.resource("pixie_majestic_blood_revive")).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(ResearchEntries.DISCOVER_INFERNAL).attunement(Sources.INFERNAL, 4).recipe(ItemsPM.MAJESTIC_INFERNAL_PIXIE.get())
                    .recipe(PrimalMagick.resource("pixie_majestic_infernal_revive")).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(ResearchEntries.DISCOVER_VOID).attunement(Sources.VOID, 4).recipe(ItemsPM.MAJESTIC_VOID_PIXIE.get())
                    .recipe(PrimalMagick.resource("pixie_majestic_void_revive")).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(ResearchEntries.DISCOVER_HALLOWED).attunement(Sources.HALLOWED, 4).recipe(ItemsPM.MAJESTIC_HALLOWED_PIXIE.get())
                    .recipe(PrimalMagick.resource("pixie_majestic_hallowed_revive")).build())
            .build(consumer);
        ResearchEntryBuilder.entry("AMBROSIA", discipline).icon(ItemsPM.BASIC_EARTH_AMBROSIA.get()).parent("EXPERT_RITUAL").parent("ATTUNEMENTS").parent("MANAFRUIT")
            .parent("SHARD_SYNTHESIS").parent("RUNE_ABSORB").parent("RUNE_SELF").parent("RITUAL_LECTERN")
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.RUNE_ABSORB.get()).requiredItemStack(ItemsPM.RUNE_SELF.get()).requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Sources.EARTH, 2).attunement(Sources.SEA, 2).attunement(Sources.SKY, 2).attunement(Sources.SUN, 2).attunement(Sources.MOON, 2)
                    .recipe(ItemsPM.BASIC_EARTH_AMBROSIA.get()).recipe(ItemsPM.BASIC_SEA_AMBROSIA.get()).recipe(ItemsPM.BASIC_SKY_AMBROSIA.get()).recipe(ItemsPM.BASIC_SUN_AMBROSIA.get())
                    .recipe(ItemsPM.BASIC_MOON_AMBROSIA.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(ResearchEntries.DISCOVER_BLOOD).attunement(Sources.BLOOD, 2).recipe(ItemsPM.BASIC_BLOOD_AMBROSIA.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(ResearchEntries.DISCOVER_INFERNAL).attunement(Sources.INFERNAL, 2).recipe(ItemsPM.BASIC_INFERNAL_AMBROSIA.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(ResearchEntries.DISCOVER_VOID).attunement(Sources.VOID, 2).recipe(ItemsPM.BASIC_VOID_AMBROSIA.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(ResearchEntries.DISCOVER_HALLOWED).attunement(Sources.HALLOWED, 2).recipe(ItemsPM.BASIC_HALLOWED_AMBROSIA.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("GREATER_AMBROSIA", discipline).icon(ItemsPM.GREATER_EARTH_AMBROSIA.get()).parent("MASTER_RITUAL").parent("AMBROSIA").parent("CRYSTAL_SYNTHESIS")
            .parent("RUNE_POWER").parent("BLOODLETTER")
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.RUNE_INSIGHT.get()).requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().attunement(Sources.EARTH, 3).attunement(Sources.SEA, 3).attunement(Sources.SKY, 3).attunement(Sources.SUN, 3).attunement(Sources.MOON, 3)
                    .recipe(ItemsPM.GREATER_EARTH_AMBROSIA.get()).recipe(ItemsPM.GREATER_SEA_AMBROSIA.get()).recipe(ItemsPM.GREATER_SKY_AMBROSIA.get()).recipe(ItemsPM.GREATER_SUN_AMBROSIA.get())
                    .recipe(ItemsPM.GREATER_MOON_AMBROSIA.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(ResearchEntries.DISCOVER_BLOOD).attunement(Sources.BLOOD, 3).recipe(ItemsPM.GREATER_BLOOD_AMBROSIA.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(ResearchEntries.DISCOVER_INFERNAL).attunement(Sources.INFERNAL, 3).recipe(ItemsPM.GREATER_INFERNAL_AMBROSIA.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(ResearchEntries.DISCOVER_VOID).attunement(Sources.VOID, 3).recipe(ItemsPM.GREATER_VOID_AMBROSIA.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(ResearchEntries.DISCOVER_HALLOWED).attunement(Sources.HALLOWED, 3).recipe(ItemsPM.GREATER_HALLOWED_AMBROSIA.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("SUPREME_AMBROSIA", discipline).icon(ItemsPM.SUPREME_EARTH_AMBROSIA.get()).parent("SUPREME_RITUAL").parent("GREATER_AMBROSIA").parent("CLUSTER_SYNTHESIS")
            .parent("CELESTIAL_HARP")
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.RUNE_POWER.get()).requiredKnowledge(KnowledgeType.THEORY, 3).build())
            .stage(ResearchStageBuilder.stage().attunement(Sources.EARTH, 4).attunement(Sources.SEA, 4).attunement(Sources.SKY, 4).attunement(Sources.SUN, 4).attunement(Sources.MOON, 4)
                    .recipe(ItemsPM.SUPREME_EARTH_AMBROSIA.get()).recipe(ItemsPM.SUPREME_SEA_AMBROSIA.get()).recipe(ItemsPM.SUPREME_SKY_AMBROSIA.get()).recipe(ItemsPM.SUPREME_SUN_AMBROSIA.get())
                    .recipe(ItemsPM.SUPREME_MOON_AMBROSIA.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(ResearchEntries.DISCOVER_BLOOD).attunement(Sources.BLOOD, 4).recipe(ItemsPM.SUPREME_BLOOD_AMBROSIA.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(ResearchEntries.DISCOVER_INFERNAL).attunement(Sources.INFERNAL, 4).recipe(ItemsPM.SUPREME_INFERNAL_AMBROSIA.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(ResearchEntries.DISCOVER_VOID).attunement(Sources.VOID, 4).recipe(ItemsPM.SUPREME_VOID_AMBROSIA.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch(ResearchEntries.DISCOVER_HALLOWED).attunement(Sources.HALLOWED, 4).recipe(ItemsPM.SUPREME_HALLOWED_AMBROSIA.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("FLYING_CARPET", discipline).icon(ItemsPM.FLYING_CARPET.get()).parent("MASTER_RITUAL").parent("CRYSTAL_SYNTHESIS").parent("MANA_SALTS").parent("RUNE_PROJECT")
            .parent("RUNE_ITEM").parent("RUNE_POWER").parent("INCENSE_BRAZIER").parent("RITUAL_LECTERN").parent("RITUAL_BELL")
            .stage(ResearchStageBuilder.stage().requiredResearch("t_flying_creature").requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().attunement(Sources.SKY, 3).recipe(ItemsPM.FLYING_CARPET.get()).build())
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
        ResearchEntryBuilder.entry("FORBIDDEN_TRIDENT", discipline).icon(ItemsPM.FORBIDDEN_TRIDENT.get()).parent(ResearchEntries.DISCOVER_BLOOD).parent("MASTER_RITUAL").parent("HEXIUM")
            .parent("SHARD_SYNTHESIS").parent("MANA_SALTS").parent("RUNE_BLOOD").parent("BLOODLETTER")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.FORBIDDEN_TRIDENT.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("MASTER_RUNEWORKING").requiredResearch("RUNE_PROJECT").requiredResearch("RUNE_CREATURE").requiredResearch("RUNE_BLOOD")
                    .sibling(SimpleResearchKey.parseRuneEnchantment(EnchantmentsPM.RENDING.get())).sibling("UNLOCK_RUNE_ENCHANTMENTS").build())
            .build(consumer);
        ResearchEntryBuilder.entry("FORBIDDEN_BOW", discipline).icon(ItemsPM.FORBIDDEN_BOW.get()).parent(ResearchEntries.DISCOVER_INFERNAL).parent("MASTER_RITUAL").parent("HEXIUM")
            .parent("SHARD_SYNTHESIS").parent("MANA_SALTS").parent("RUNE_INFERNAL").parent("SOUL_ANVIL")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.FORBIDDEN_BOW.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("MASTER_RUNEWORKING").requiredResearch("RUNE_ABSORB").requiredResearch("RUNE_CREATURE").requiredResearch("RUNE_INFERNAL")
                    .sibling(SimpleResearchKey.parseRuneEnchantment(EnchantmentsPM.SOULPIERCING.get())).sibling("UNLOCK_RUNE_ENCHANTMENTS").build())
            .build(consumer);
        ResearchEntryBuilder.entry("FORBIDDEN_SWORD", discipline).icon(ItemsPM.FORBIDDEN_SWORD.get()).parent(ResearchEntries.DISCOVER_VOID).parent("MASTER_RITUAL").parent("HEXIUM")
            .parent("SHARD_SYNTHESIS").parent("MANA_SALTS").parent("RUNE_VOID").parent("BLOODLETTER").parent("SOUL_ANVIL")
            .stage(ResearchStageBuilder.stage().requiredKnowledge(KnowledgeType.THEORY, 2).build())
            .stage(ResearchStageBuilder.stage().recipe(ItemsPM.FORBIDDEN_SWORD.get()).build())
            .addendum(ResearchAddendumBuilder.addendum().requiredResearch("MASTER_RUNEWORKING").requiredResearch("RUNE_SUMMON").requiredResearch("RUNE_ITEM").requiredResearch("RUNE_VOID")
                    .sibling(SimpleResearchKey.parseRuneEnchantment(EnchantmentsPM.ESSENCE_THIEF.get())).sibling("UNLOCK_RUNE_ENCHANTMENTS").build())
            .build(consumer);
        ResearchEntryBuilder.entry("SACRED_SHIELD", discipline).icon(ItemsPM.SACRED_SHIELD.get()).parent(ResearchEntries.DISCOVER_HALLOWED).parent("SUPREME_RITUAL").parent("HALLOWSTEEL")
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
        ResearchEntryBuilder.entry("HYDROMELON", discipline).icon(ItemsPM.HYDROMELON_SLICE.get()).parent("EXPERT_RITUAL").parent("RITUAL_CANDLES").parent("RITUAL_BELL").parent("RUNE_SEA")
            .parent("SHARD_SYNTHESIS")
            .stage(ResearchStageBuilder.stage().requiredItemStack(ItemsPM.SUNWOOD_SAPLING.get()).requiredItemStack(ItemsPM.MOONWOOD_SAPLING.get()).requiredItemStack(Items.MELON)
                    .requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Sources.SEA, 2).recipe(PrimalMagick.resource("hydromelon_seeds_from_ritual")).recipe(ItemsPM.HYDROMELON.get())
                    .recipe(ItemsPM.HYDROMELON_SEEDS.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("BLOOD_ROSE", discipline).icon(ItemsPM.BLOOD_ROSE.get()).parent(ResearchEntries.DISCOVER_BLOOD).parent("EXPERT_RITUAL").parent("HYDROMELON").parent("BLOODLETTER")
            .parent("RUNE_BLOOD")
            .stage(ResearchStageBuilder.stage().requiredItemStack(Items.ROSE_BUSH).requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Sources.BLOOD, 2).recipe(ItemsPM.BLOOD_ROSE.get()).build())
            .build(consumer);
        ResearchEntryBuilder.entry("EMBERFLOWER", discipline).icon(ItemsPM.EMBERFLOWER.get()).parent(ResearchEntries.DISCOVER_INFERNAL).parent("EXPERT_RITUAL").parent("HYDROMELON").parent("BLOODLETTER")
            .parent("RUNE_INFERNAL")
            .stage(ResearchStageBuilder.stage().requiredItemStack(Items.SUNFLOWER).requiredKnowledge(KnowledgeType.THEORY, 1).build())
            .stage(ResearchStageBuilder.stage().attunement(Sources.INFERNAL, 2).recipe(ItemsPM.EMBERFLOWER.get()).build())
            .build(consumer);
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
