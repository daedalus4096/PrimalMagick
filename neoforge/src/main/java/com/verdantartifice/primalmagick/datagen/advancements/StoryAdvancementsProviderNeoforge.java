package com.verdantartifice.primalmagick.datagen.advancements;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class StoryAdvancementsProviderNeoforge extends AdvancementProvider {
    public StoryAdvancementsProviderNeoforge(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, List.of(new StoryAdvancementsPMNeoforge()));
    }

    private static final class StoryAdvancementsPMNeoforge extends StoryAdvancementsPM implements AdvancementProvider.AdvancementGenerator {
        @Override
        public void generate(HolderLookup.Provider provider, Consumer<AdvancementHolder> consumer, ExistingFileHelper existingFileHelper) {
            this.generateInner(provider, consumer);
        }
    }
}
