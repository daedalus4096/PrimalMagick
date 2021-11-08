package com.verdantartifice.primalmagick.datagen.theorycrafting;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.PrimalMagic;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public class ProjectBuilder {
    protected final ResourceLocation key;
    protected final List<IFinishedProjectMaterial> materialOptions = new ArrayList<>();
    protected final List<ResourceLocation> aidBlocks = new ArrayList<>();
    protected SimpleResearchKey requiredResearch;
    protected Optional<Integer> requiredMaterialCountOverride = Optional.empty();
    protected Optional<Double> baseSuccessChanceOverride = Optional.empty();
    protected double rewardMultiplier = 0.25D;
    
    protected ProjectBuilder(@Nonnull ResourceLocation key) {
        this.key = key;
    }
    
    public static ProjectBuilder project(@Nonnull ResourceLocation key) {
        return new ProjectBuilder(key);
    }
    
    public static ProjectBuilder project(@Nonnull String keyNamespace, @Nonnull String keyPath) {
        return project(new ResourceLocation(keyNamespace, keyPath));
    }
    
    public static ProjectBuilder project(@Nonnull String keyPath) {
        return project(PrimalMagic.MODID, keyPath);
    }
    
    public ProjectBuilder material(@Nonnull IFinishedProjectMaterial material) {
        this.materialOptions.add(material);
        return this;
    }
    
    public ProjectBuilder requiredResearch(@Nullable SimpleResearchKey key) {
        this.requiredResearch = key;
        return this;
    }
    
    public ProjectBuilder requiredResearch(@Nullable String keyStr) {
        this.requiredResearch = SimpleResearchKey.parse(keyStr);
        return this;
    }
    
    public ProjectBuilder materialCountOverride(int count) {
        this.requiredMaterialCountOverride = Optional.of(Integer.valueOf(count));
        return this;
    }
    
    public ProjectBuilder baseSuccessChanceOverride(double chance) {
        this.baseSuccessChanceOverride = Optional.of(Double.valueOf(chance));
        return this;
    }
    
    public ProjectBuilder rewardMultiplier(double multiplier) {
        this.rewardMultiplier = multiplier;
        return this;
    }
    
    public ProjectBuilder aid(@Nullable ResourceLocation block) {
        this.aidBlocks.add(block);
        return this;
    }
    
    public ProjectBuilder aid(@Nullable Block block) {
        if (block != null) {
            this.aidBlocks.add(block.getRegistryName());
        }
        return this;
    }
    
    private void validate(ResourceLocation id) {
        if (this.key == null) {
            throw new IllegalStateException("No key for theorycrafting project " + id.toString());
        }
        if (this.materialOptions.isEmpty()) {
            throw new IllegalStateException("No material options for theorycrafting project " + id.toString());
        }
        this.requiredMaterialCountOverride.ifPresent((count) -> {
            if (count <= 0) {
                throw new IllegalStateException("Invalid material count override for theorycrafting project " + id.toString());
            }
        });
        this.baseSuccessChanceOverride.ifPresent((chance) -> {
            if (chance < 0D) {
                throw new IllegalStateException("Invalid base success chance override for theorycrafting project " + id.toString());
            }
        });
        if (this.rewardMultiplier <= 0D) {
            throw new IllegalStateException("Invalid reward multiplier for theorycrafting project " + id.toString());
        }
        for (ResourceLocation aidBlock : this.aidBlocks) {
            if (!ForgeRegistries.BLOCKS.containsKey(aidBlock)) {
                throw new IllegalStateException("Unknown aid block for theorycrafting project " + id.toString());
            }
        }
    }
    
    public void build(Consumer<IFinishedProject> consumer) {
        this.build(consumer, this.key);
    }
    
    public void build(Consumer<IFinishedProject> consumer, String name) {
        this.build(consumer, new ResourceLocation(name));
    }
    
    public void build(Consumer<IFinishedProject> consumer, ResourceLocation id) {
        this.validate(id);
        consumer.accept(new ProjectBuilder.Result(this.key, this.materialOptions, this.requiredResearch, this.requiredMaterialCountOverride, this.baseSuccessChanceOverride, this.rewardMultiplier, this.aidBlocks));
    }
    
    public static class Result implements IFinishedProject {
        protected final ResourceLocation key;
        protected final List<IFinishedProjectMaterial> materialOptions;
        protected final SimpleResearchKey requiredResearch;
        protected final Optional<Integer> requiredMaterialCountOverride;
        protected final Optional<Double> baseSuccessChanceOverride;
        protected final double rewardMultiplier;
        protected final List<ResourceLocation> aidBlocks;
        
        public Result(@Nonnull ResourceLocation key, @Nonnull List<IFinishedProjectMaterial> materialOptions, @Nullable SimpleResearchKey requiredResearch, @Nonnull Optional<Integer> materialCount, 
                @Nonnull Optional<Double> successChance, double rewardMultiplier, @Nonnull List<ResourceLocation> aidBlocks) {
            this.key = key;
            this.materialOptions = materialOptions;
            this.requiredResearch = requiredResearch;
            this.requiredMaterialCountOverride = materialCount;
            this.baseSuccessChanceOverride = successChance;
            this.rewardMultiplier = rewardMultiplier;
            this.aidBlocks = aidBlocks;
        }

        @Override
        public ResourceLocation getId() {
            return this.key;
        }

        @Override
        public void serialize(JsonObject json) {
            json.addProperty("key", this.key.toString());
            if (this.requiredResearch != null) {
                json.addProperty("required_research", this.requiredResearch.toString());
            }
            this.requiredMaterialCountOverride.ifPresent((count) -> {
                json.addProperty("required_material_count_override", count);
            });
            this.baseSuccessChanceOverride.ifPresent((chance) -> {
                json.addProperty("base_success_chance_override", chance);
            });
            json.addProperty("reward_multiplier", this.rewardMultiplier);
            
            JsonArray aidsArray = new JsonArray();
            for (ResourceLocation aidBlock : this.aidBlocks) {
                aidsArray.add(aidBlock.toString());
            }
            json.add("aid_blocks", aidsArray);
            
            JsonArray materialsArray = new JsonArray();
            for (IFinishedProjectMaterial material : this.materialOptions) {
                materialsArray.add(material.getMaterialJson());
            }
            json.add("material_options", materialsArray);
        }
    }
}
