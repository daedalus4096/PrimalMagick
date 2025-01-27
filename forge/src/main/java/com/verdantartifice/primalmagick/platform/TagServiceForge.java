package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.platform.services.ITagService;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

public class TagServiceForge implements ITagService {
    @Override
    public String splitNamespace() {
        return "forge";
    }

    @Override
    public <T> TagKey<T> cobblestonesNormal(ResourceKey<? extends Registry<T>> registry) {
        return this.createSplit(registry, "cobblestone/normal");
    }

    @Override
    public <T> TagKey<T> cobblestonesInfested(ResourceKey<? extends Registry<T>> registry) {
        return this.createSplit(registry, "cobblestone/infested");
    }

    @Override
    public <T> TagKey<T> cobblestonesMossy(ResourceKey<? extends Registry<T>> registry) {
        return this.createSplit(registry, "cobblestone/mossy");
    }

    @Override
    public <T> TagKey<T> cobblestonesDeepslate(ResourceKey<? extends Registry<T>> registry) {
        return this.createSplit(registry, "cobblestone/deepslate");
    }

    @Override
    public <T> TagKey<T> gravels(ResourceKey<? extends Registry<T>> registry) {
        return this.createSplit(registry, "gravel");
    }

    @Override
    public <T> TagKey<T> gunpowders(ResourceKey<? extends Registry<T>> registry) {
        return this.createSplit(registry, "gunpowder");
    }

    @Override
    public <T> TagKey<T> netherracks(ResourceKey<? extends Registry<T>> registry) {
        return this.createSplit(registry, "netherrack");
    }

    @Override
    public <T> TagKey<T> sands(ResourceKey<? extends Registry<T>> registry) {
        return this.createSplit(registry, "sand");
    }

    @Override
    public <T> TagKey<T> sandsColorless(ResourceKey<? extends Registry<T>> registry) {
        return this.createSplit(registry, "sand/colorless");
    }

    @Override
    public <T> TagKey<T> sandsRed(ResourceKey<? extends Registry<T>> registry) {
        return this.createSplit(registry, "sand/red");
    }
}
