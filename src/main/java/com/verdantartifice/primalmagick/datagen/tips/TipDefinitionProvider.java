package com.verdantartifice.primalmagick.datagen.tips;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.JsonOps;
import com.verdantartifice.primalmagick.client.tips.TipDefinition;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

public class TipDefinitionProvider implements DataProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    protected final PackOutput packOutput;
    
    public TipDefinitionProvider(PackOutput packOutput) {
        this.packOutput = packOutput;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput pOutput) {
        ImmutableList.Builder<CompletableFuture<?>> futuresBuilder = new ImmutableList.Builder<>();
        Map<ResourceLocation, TipDefinition> map = new HashMap<>();
        this.registerTipDefinitions((id, tip) -> {
            if (map.put(id, tip) != null) {
                LOGGER.warn("Duplicate tip definition in data generation: {}", id.toString());
            }
        });
        map.entrySet().forEach(entry -> {
            TipDefinition.CODEC.encodeStart(JsonOps.INSTANCE, entry.getValue())
                    .resultOrPartial(LOGGER::error)
                    .ifPresent(json -> futuresBuilder.add(DataProvider.saveStable(pOutput, json, this.getPath(this.packOutput, entry.getKey()))));
        });
        return CompletableFuture.allOf(futuresBuilder.build().toArray(CompletableFuture[]::new));
    }

    private Path getPath(PackOutput output, ResourceLocation entryLoc) {
        return output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(entryLoc.getNamespace()).resolve("tips").resolve(entryLoc.getPath() + ".json");
    }
    
    protected void registerTipDefinitions(BiConsumer<ResourceLocation, TipDefinition> consumer) {
        TipDefinition.builder("thanks").save(consumer);
        TipDefinition.builder("discord").save(consumer);
        TipDefinition.builder("more_tips").save(consumer);
        TipDefinition.builder("earth_shrine_loc").save(consumer);
        TipDefinition.builder("sea_shrine_loc").save(consumer);
        TipDefinition.builder("sky_shrine_loc").save(consumer);
        TipDefinition.builder("sun_shrine_loc").save(consumer);
        TipDefinition.builder("moon_shrine_loc").save(consumer);
        TipDefinition.builder("no_blood_shrines").requiredResearch(CompoundResearchKey.from(Source.BLOOD.getDiscoverKey())).save(consumer);
        TipDefinition.builder("no_infernal_shrines").requiredResearch(CompoundResearchKey.from(Source.INFERNAL.getDiscoverKey())).save(consumer);
        TipDefinition.builder("no_void_shrines").requiredResearch(CompoundResearchKey.from(Source.VOID.getDiscoverKey())).save(consumer);
        TipDefinition.builder("no_hallowed_shrines").requiredResearch(CompoundResearchKey.from(Source.HALLOWED.getDiscoverKey())).save(consumer);
        TipDefinition.builder("go_explore").save(consumer);
        TipDefinition.builder("new_disciplines").save(consumer);
        TipDefinition.builder("salt").save(consumer);
        TipDefinition.builder("treefolk").save(consumer);
        TipDefinition.builder("view_affinities").requiredResearch(CompoundResearchKey.from(SimpleResearchKey.FIRST_STEPS)).save(consumer);
        TipDefinition.builder("analysis").requiredResearch(CompoundResearchKey.from(SimpleResearchKey.FIRST_STEPS)).save(consumer);
        TipDefinition.builder("no_affinities").requiredResearch(CompoundResearchKey.from(SimpleResearchKey.FIRST_STEPS)).save(consumer);
        TipDefinition.builder("research_table").requiredResearch(CompoundResearchKey.parse("THEORYCRAFTING")).save(consumer);
        TipDefinition.builder("project_success").requiredResearch(CompoundResearchKey.parse("THEORYCRAFTING")).save(consumer);
        TipDefinition.builder("research_boldness").requiredResearch(CompoundResearchKey.parse("THEORYCRAFTING")).save(consumer);
        TipDefinition.builder("research_aids").requiredResearch(CompoundResearchKey.parse("THEORYCRAFTING")).save(consumer);
        TipDefinition.builder("permanent_attunement").requiredResearch(CompoundResearchKey.parse("ATTUNEMENTS")).save(consumer);
        TipDefinition.builder("temporary_attunement").requiredResearch(CompoundResearchKey.parse("ATTUNEMENTS")).save(consumer);
        TipDefinition.builder("better_analysis").requiredResearch(CompoundResearchKey.parse("UNLOCK_MAGITECH")).save(consumer);
        TipDefinition.builder("spending_mana").requiredResearch(CompoundResearchKey.parse("TERRESTRIAL_MAGICK")).save(consumer);
        TipDefinition.builder("sotu").requiredResearch(CompoundResearchKey.parse("SECRETS_OF_THE_UNIVERSE@0")).save(consumer);
        TipDefinition.builder("mana_charger").requiredResearch(CompoundResearchKey.parse("WAND_CHARGER")).save(consumer);
        TipDefinition.builder("staves").requiredResearch(CompoundResearchKey.parse("STAVES")).save(consumer);
        TipDefinition.builder("robes").requiredResearch(CompoundResearchKey.parse("IMBUED_WOOL")).save(consumer);
        TipDefinition.builder("ritual_symmetry").requiredResearch(CompoundResearchKey.parse("BASIC_RITUAL")).save(consumer);
        TipDefinition.builder("induced_attunement").requiredResearch(CompoundResearchKey.parse("AMBROSIA")).save(consumer);
        TipDefinition.builder("ambrosia_cap").requiredResearch(CompoundResearchKey.parse("AMBROSIA")).save(consumer);
        TipDefinition.builder("power_runes").requiredResearch(CompoundResearchKey.parse("BASIC_RUNEWORKING")).save(consumer);
        TipDefinition.builder("rune_hints").requiredResearch(CompoundResearchKey.parse("BASIC_RUNEWORKING")).save(consumer);
    }
    
    @Override
    public String getName() {
        return "Primal Magick Tip Definitions";
    }
}
