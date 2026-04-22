package com.verdantartifice.primalmagick.datagen.advancements;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.advancements.AdvancementSubProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class StoryAdvancementsProviderNeoforge extends AdvancementProvider {
    public StoryAdvancementsProviderNeoforge(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, List.of(new StoryAdvancementsPMNeoforge()));
    }

    private static final class StoryAdvancementsPMNeoforge extends StoryAdvancementsPM implements AdvancementSubProvider {
        @Override
        public void generate(@NotNull HolderLookup.Provider provider, @NotNull Consumer<AdvancementHolder> consumer) {
            this.generateInner(provider, consumer);
        }
    }
}
