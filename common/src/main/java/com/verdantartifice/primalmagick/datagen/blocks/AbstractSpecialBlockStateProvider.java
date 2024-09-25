package com.verdantartifice.primalmagick.datagen.blocks;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.datagen.items.ItemModelProviderPM;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.IGeneratedBlockState;
import net.minecraftforge.common.data.ExistingFileHelper;

/**
 * Data provider for "special" block states, ones that don't necessarily come from a block.  Mimics
 * {@link net.minecraftforge.client.model.generators.BlockStateProvider} but only requires a
 * resource location for the block state's ID instead of a block.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractSpecialBlockStateProvider implements DataProvider {
    protected final Map<ResourceLocation, IGeneratedBlockState> registeredBlocks = new LinkedHashMap<>();

    private final PackOutput output;
    protected final String modid;
    private final BlockModelProviderPM blockModels;
    private final ItemModelProviderPM itemModels;
    protected final ExistingFileHelper existingFileHelper;

    public AbstractSpecialBlockStateProvider(PackOutput output, String modid, ExistingFileHelper exFileHelper) {
        this.output = output;
        this.modid = modid;
        this.existingFileHelper = exFileHelper;
        this.blockModels = new BlockModelProviderPM(output, modid, exFileHelper) {
            @Override public CompletableFuture<?> run(CachedOutput cache) { return CompletableFuture.allOf(); }

            @Override protected void registerModels() {}
        };
        // FIXME Special models generally don't need armor trims so... just don't use the lookup provider with these?  Sloppy, should pass through the real.
        this.itemModels = new ItemModelProviderPM(output, CompletableFuture.completedFuture((HolderLookup.Provider)null), this.blockModels.existingFileHelper) {
            @Override protected void registerModels() {}

            @Override public CompletableFuture<?> run(CachedOutput cache) { return CompletableFuture.allOf(); }
        };
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        this.models().clear();
        this.itemModels().clear();
        registeredBlocks.clear();
        registerStatesAndModels();
        CompletableFuture<?>[] futures = new CompletableFuture<?>[2 + this.registeredBlocks.size()];
        int i = 0;
        futures[i++] = this.models().generateAll(cache);
        futures[i++] = this.itemModels().generateAll(cache);
        for (Map.Entry<ResourceLocation, IGeneratedBlockState> entry : this.registeredBlocks.entrySet()) {
            futures[i++] = this.saveBlockState(cache, entry.getValue().toJson(), entry.getKey());
        }
        return CompletableFuture.allOf(futures);
    }

    protected abstract void registerStatesAndModels();

    public BlockModelProviderPM models() {
        return this.blockModels;
    }

    public ItemModelProviderPM itemModels() {
        return this.itemModels;
    }

    public SpecialBlockStateBuilder getSpecialBuilder(ResourceLocation loc) {
        if (this.registeredBlocks.containsKey(loc)) {
            IGeneratedBlockState old = this.registeredBlocks.get(loc);
            Preconditions.checkState(old instanceof SpecialBlockStateBuilder);
            return (SpecialBlockStateBuilder)old;
        } else {
            SpecialBlockStateBuilder ret = new SpecialBlockStateBuilder(loc);
            this.registeredBlocks.put(loc, ret);
            return ret;
        }
    }

    private CompletableFuture<?> saveBlockState(CachedOutput cache, JsonObject stateJson, ResourceLocation owner) {
        Preconditions.checkNotNull(owner);
        Path outputPath = this.output.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                .resolve(owner.getNamespace()).resolve("blockstates").resolve(owner.getPath() + ".json");
        return DataProvider.saveStable(cache, stateJson, outputPath);
    }
}
