package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.platform.services.ITagService;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

public class TagServiceNeoforge implements ITagService {
    @Override
    public String splitNamespace() {
        return "c";
    }

    @Override
    public <T> TagKey<T> cobblestonesNormal(ResourceKey<? extends Registry<T>> registry) {
        return this.createSplit(registry, "cobblestones/normal");
    }

    @Override
    public <T> TagKey<T> cobblestonesInfested(ResourceKey<? extends Registry<T>> registry) {
        return this.createSplit(registry, "cobblestones/infested");
    }

    @Override
    public <T> TagKey<T> cobblestonesMossy(ResourceKey<? extends Registry<T>> registry) {
        return this.createSplit(registry, "cobblestones/mossy");
    }

    @Override
    public <T> TagKey<T> cobblestonesDeepslate(ResourceKey<? extends Registry<T>> registry) {
        return this.createSplit(registry, "cobblestones/deepslate");
    }

    @Override
    public <T> TagKey<T> gravels(ResourceKey<? extends Registry<T>> registry) {
        return this.createSplit(registry, "gravels");
    }

    @Override
    public <T> TagKey<T> gunpowders(ResourceKey<? extends Registry<T>> registry) {
        return this.createSplit(registry, "gunpowders");
    }

    @Override
    public <T> TagKey<T> netherracks(ResourceKey<? extends Registry<T>> registry) {
        return this.createSplit(registry, "netherracks");
    }

    @Override
    public <T> TagKey<T> sands(ResourceKey<? extends Registry<T>> registry) {
        return this.createSplit(registry, "sands");
    }

    @Override
    public <T> TagKey<T> sandsColorless(ResourceKey<? extends Registry<T>> registry) {
        return this.createSplit(registry, "sands/colorless");
    }

    @Override
    public <T> TagKey<T> sandsRed(ResourceKey<? extends Registry<T>> registry) {
        return this.createSplit(registry, "sands/red");
    }
}
