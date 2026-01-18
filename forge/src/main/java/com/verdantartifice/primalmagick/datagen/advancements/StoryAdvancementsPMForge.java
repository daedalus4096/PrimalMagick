package com.verdantartifice.primalmagick.datagen.advancements;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class StoryAdvancementsPMForge extends StoryAdvancementsPM implements ForgeAdvancementProvider.AdvancementGenerator {
    @Override
    public void generate(@NotNull HolderLookup.Provider registries, @NotNull Consumer<AdvancementHolder> saver, @NotNull ExistingFileHelper existingFileHelper) {
        this.generateInner(registries, saver);
    }
}
