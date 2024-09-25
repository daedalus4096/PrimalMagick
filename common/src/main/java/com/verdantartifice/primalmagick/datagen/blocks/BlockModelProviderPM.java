package com.verdantartifice.primalmagick.datagen.blocks;

import java.util.concurrent.CompletableFuture;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

/**
 * Extension of {@link net.minecraftforge.client.model.generators.BlockModelProvider} that publicly
 * exposes two methods needed by {@link AbstractSpecialBlockStateProvider}.
 * 
 * @author Daedalus4096
 */
public abstract class BlockModelProviderPM extends BlockModelProvider {
    public BlockModelProviderPM(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
    }

    @Override
    public void clear() {
        // Expose publicly is all
        super.clear();
    }

    @Override
    public CompletableFuture<?> generateAll(CachedOutput cache) {
        // Expose publicly is all
        return super.generateAll(cache);
    }
}
