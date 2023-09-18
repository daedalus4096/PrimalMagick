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
        TipDefinition.builder("more_tips").save(consumer);
        TipDefinition.builder("earth_shrine_loc").save(consumer);
        TipDefinition.builder("sea_shrine_loc").save(consumer);
        TipDefinition.builder("sky_shrine_loc").save(consumer);
        TipDefinition.builder("sun_shrine_loc").save(consumer);
        TipDefinition.builder("moon_shrine_loc").save(consumer);
    }
    
    @Override
    public String getName() {
        return "Primal Magick Tip Definitions";
    }
}
