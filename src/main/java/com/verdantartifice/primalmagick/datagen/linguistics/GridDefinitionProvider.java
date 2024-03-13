package com.verdantartifice.primalmagick.datagen.linguistics;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

public class GridDefinitionProvider implements DataProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    protected final PackOutput packOutput;
    
    public GridDefinitionProvider(PackOutput packOutput) {
        this.packOutput = packOutput;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput pOutput) {
        ImmutableList.Builder<CompletableFuture<?>> futuresBuilder = new ImmutableList.Builder<>();
        Map<ResourceLocation, IFinishedGrid> map = new HashMap<>();
        this.registerGrids(gridDef -> {
            if (map.put(gridDef.getId(), gridDef) != null) {
                LOGGER.debug("Duplicate linguistics grid definition in data generation: {}", gridDef.getId().toString());
            }
        });
        map.entrySet().forEach(entry -> {
            futuresBuilder.add(DataProvider.saveStable(pOutput, entry.getValue().getGridJson(), this.getPath(this.packOutput, entry.getKey())));
        });
        return CompletableFuture.allOf(futuresBuilder.build().toArray(CompletableFuture[]::new));
    }

    private Path getPath(PackOutput output, ResourceLocation entryLoc) {
        return output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(entryLoc.getNamespace()).resolve("linguistics_grids").resolve(entryLoc.getPath() + ".json");
    }
    
    protected void registerGrids(Consumer<IFinishedGrid> consumer) {
        // TODO Stub
    }
    
    @Override
    public String getName() {
        return "Primal Magick Linguistics Grids";
    }
}
