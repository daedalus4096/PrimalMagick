package com.verdantartifice.primalmagick.common.theorycrafting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.AndRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.QuorumRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.stats.StatsPM;
import com.verdantartifice.primalmagick.common.theorycrafting.materials.AbstractProjectMaterial;
import com.verdantartifice.primalmagick.common.theorycrafting.rewards.AbstractReward;
import com.verdantartifice.primalmagick.common.theorycrafting.weights.AbstractWeightFunction;
import com.verdantartifice.primalmagick.common.util.StreamCodecUtils;
import com.verdantartifice.primalmagick.common.util.WeightedRandomBag;

import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Class encapsulating a data-defined template for a theorycrafting project.  These templates determine
 * the parameters of concrete projects which are consumed by players for theory rewards.
 * 
 * @author Daedalus4096
 */
public record ProjectTemplate(List<AbstractProjectMaterial<?>> materialOptions, List<AbstractReward<?>> otherRewards, Optional<AbstractRequirement<?>> requirement,
        Optional<Integer> requiredMaterialCountOverride, Optional<Double> baseSuccessChanceOverride, double rewardMultiplier, List<ResourceLocation> aidBlocks,
        Optional<AbstractWeightFunction<?>> weightFunction) {
    public static final int MAX_MATERIALS = 4;
    
    public static Codec<ProjectTemplate> codec() {
        return RecordCodecBuilder.create(instance -> instance.group(
                AbstractProjectMaterial.dispatchCodec().listOf().fieldOf("materialOptions").forGetter(ProjectTemplate::materialOptions),
                AbstractReward.dispatchCodec().listOf().fieldOf("otherRewards").forGetter(ProjectTemplate::otherRewards),
                AbstractRequirement.dispatchCodec().optionalFieldOf("requirement").forGetter(ProjectTemplate::requirement),
                Codec.INT.optionalFieldOf("requiredMaterialCountOverride").forGetter(ProjectTemplate::requiredMaterialCountOverride),
                Codec.DOUBLE.optionalFieldOf("baseSuccessChanceOverride").forGetter(ProjectTemplate::baseSuccessChanceOverride),
                Codec.DOUBLE.fieldOf("rewardMultiplier").forGetter(ProjectTemplate::rewardMultiplier),
                ResourceLocation.CODEC.listOf().fieldOf("aidBlocks").forGetter(ProjectTemplate::aidBlocks),
                AbstractWeightFunction.dispatchCodec().optionalFieldOf("weightFunction").forGetter(ProjectTemplate::weightFunction)
            ).apply(instance, ProjectTemplate::new));
    }
    
    public static StreamCodec<RegistryFriendlyByteBuf, ProjectTemplate> streamCodec() {
        return StreamCodecUtils.composite(
                AbstractProjectMaterial.dispatchStreamCodec().apply(ByteBufCodecs.list()),
                ProjectTemplate::materialOptions,
                AbstractReward.dispatchStreamCodec().apply(ByteBufCodecs.list()),
                ProjectTemplate::otherRewards,
                ByteBufCodecs.optional(AbstractRequirement.dispatchStreamCodec()),
                ProjectTemplate::requirement,
                ByteBufCodecs.optional(ByteBufCodecs.VAR_INT),
                ProjectTemplate::requiredMaterialCountOverride,
                ByteBufCodecs.optional(ByteBufCodecs.DOUBLE),
                ProjectTemplate::baseSuccessChanceOverride,
                ByteBufCodecs.DOUBLE,
                ProjectTemplate::rewardMultiplier,
                ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list()),
                ProjectTemplate::aidBlocks,
                ByteBufCodecs.optional(AbstractWeightFunction.dispatchStreamCodec()),
                ProjectTemplate::weightFunction,
                ProjectTemplate::new);
    }
    
    public double getWeight(Player player) {
        return this.weightFunction.map(func -> func.getWeight(player)).orElse(1D);
    }
    
    @Nullable
    public Project initialize(ServerPlayer player, Set<Block> nearby) {
        if (this.requirement.isPresent() && !this.requirement.get().isMetBy(player)) {
            // Fail initialization to prevent use if the player doesn't have the right research unlocked
            return null;
        }
        
        RegistryAccess registryAccess = player.level().registryAccess();
        ResourceKey<ProjectTemplate> key = registryAccess.registryOrThrow(RegistryKeysPM.PROJECT_TEMPLATES).getResourceKey(this).orElse(null);
        if (key == null) {
            // If this isn't a registered project template, fail initialization to prevent use
            return null;
        }
        
        ResourceLocation foundAid = null;
        if (!this.aidBlocks.isEmpty()) {
            boolean found = false;
            Set<ResourceLocation> nearbyIds = nearby.stream().map(b -> ForgeRegistries.BLOCKS.getKey(b)).collect(Collectors.toUnmodifiableSet());
            for (ResourceLocation aidBlock : this.aidBlocks) {
                if (nearbyIds.contains(aidBlock)) {
                    found = true;
                    foundAid = aidBlock;
                    break;
                }
            }
            if (!found) {
                // Fail initialization to prevent use if none of the required aid blocks are nearby
                return null;
            }
        }
        
        // Randomly select materials to use from the bag of options, disallowing duplicates
        int attempts = 0;
        int maxMaterials = this.getRequiredMaterialCount(player);
        List<MaterialInstance> materials = new ArrayList<>();
        Set<AbstractProjectMaterial<?>> chosen = new HashSet<>();
        WeightedRandomBag<AbstractProjectMaterial<?>> options = this.getMaterialOptions(player);
        while (materials.size() < maxMaterials && attempts < 1000) {
            attempts++;
            AbstractProjectMaterial<?> material = options.getRandom(player.getRandom());
            if (!chosen.contains(material)) {
                chosen.add(material);
                materials.add(new MaterialInstance(material));
            }
        }
        if (materials.size() < maxMaterials) {
            // Fail initialization to prevent use if not all materials could be allocated
            return null;
        }
        
        // Create new initialized project
        return new Project(key, materials, this.otherRewards, this.getBaseSuccessChance(player), this.rewardMultiplier, Optional.ofNullable(foundAid));
    }
    
    protected int getRequiredMaterialCount(Player player) {
        return this.requiredMaterialCountOverride.orElseGet(() -> {
            // Get projects completed from stats and calculate based on that
            int completed = StatsManager.getValue(player, StatsPM.RESEARCH_PROJECTS_COMPLETED);
            return Math.min(MAX_MATERIALS, 1 + (completed / 5));
        });
    }
    
    protected double getBaseSuccessChance(Player player) {
        return this.baseSuccessChanceOverride.orElseGet(() -> {
            // Get projects completed from stats and calculate based on that
            int completed = StatsManager.getValue(player, StatsPM.RESEARCH_PROJECTS_COMPLETED);
            return Math.max(0.0D, 0.5D - (0.1D * (completed / 3)));
        });
    }
    
    @Nonnull
    protected WeightedRandomBag<AbstractProjectMaterial<?>> getMaterialOptions(ServerPlayer player) {
        WeightedRandomBag<AbstractProjectMaterial<?>> retVal = new WeightedRandomBag<>();
        for (AbstractProjectMaterial<?> material : this.materialOptions) {
            if (material.isAllowedInProject(player)) {
                retVal.add(material, material.getWeight());
            }
        }
        return retVal;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        protected final List<AbstractProjectMaterial<?>> materialOptions = new ArrayList<>();
        protected final List<AbstractReward<?>> otherRewards = new ArrayList<>();
        protected final List<AbstractRequirement<?>> requirements = new ArrayList<>();
        protected Optional<Integer> requiredMaterialCountOverride = Optional.empty();
        protected Optional<Double> baseSuccessChanceOverride = Optional.empty();
        protected double rewardMultiplier = 0.25D;
        protected final List<ResourceLocation> aidBlocks = new ArrayList<>();
        protected Optional<AbstractWeightFunction<?>> weightFunction = Optional.empty();
        
        protected Builder() {}
        
        public Builder material(AbstractProjectMaterial<?> material) {
            this.materialOptions.add(material);
            return this;
        }
        
        public Builder otherReward(AbstractReward<?> reward) {
            this.otherRewards.add(reward);
            return this;
        }
        
        public Builder requirement(AbstractRequirement<?> requirement) {
            this.requirements.add(requirement);
            return this;
        }
        
        public Builder requiredResearch(ResourceKey<ResearchEntry> rawKey) {
            return this.requirement(new ResearchRequirement(new ResearchEntryKey(rawKey)));
        }
        
        @SafeVarargs
        public final Builder quorumResearch(int count, ResourceKey<ResearchEntry>... rawKeys) {
            return this.requirement(new QuorumRequirement(count, Arrays.stream(rawKeys).map(k -> new ResearchRequirement(new ResearchEntryKey(k))).toArray(AbstractRequirement<?>[]::new)));
        }
        
        public Builder materialCountOverride(int value) {
            this.requiredMaterialCountOverride = Optional.of(value);
            return this;
        }
        
        public Builder baseSuccessChanceOverride(double value) {
            this.baseSuccessChanceOverride = Optional.of(value);
            return this;
        }
        
        public Builder rewardMultiplier(double multiplier) {
            this.rewardMultiplier = multiplier;
            return this;
        }
        
        public Builder aid(Block block) {
            this.aidBlocks.add(ForgeRegistries.BLOCKS.getKey(Preconditions.checkNotNull(block)));
            return this;
        }
        
        public Builder weightFunction(AbstractWeightFunction<?> func) {
            this.weightFunction = Optional.of(func);
            return this;
        }
        
        protected Optional<AbstractRequirement<?>> getFinalRequirement() {
            if (this.requirements.isEmpty()) {
                return Optional.empty();
            } else if (this.requirements.size() == 1) {
                return Optional.of(this.requirements.get(0));
            } else {
                return Optional.of(new AndRequirement(this.requirements));
            }
        }
        
        private void validate() {
            int maxMaterialCount = this.requiredMaterialCountOverride.orElse(MAX_MATERIALS);
            if (this.materialOptions.size() < maxMaterialCount) {
                throw new IllegalStateException("Project template must have at least " + maxMaterialCount + " material option(s)");
            } else if (this.requiredMaterialCountOverride.isPresent() && this.requiredMaterialCountOverride.get() > MAX_MATERIALS) {
                throw new IllegalStateException("Project template material override must not be greater than " + MAX_MATERIALS);
            } else if (this.requiredMaterialCountOverride.isPresent() && this.requiredMaterialCountOverride.get() <= 0) {
                throw new IllegalStateException("Project template material override must be positive");
            } else if (this.baseSuccessChanceOverride.isPresent() && (this.baseSuccessChanceOverride.get() < 0D || this.baseSuccessChanceOverride.get() > 1D)) {
                throw new IllegalStateException("Project template base success chance override must be between 0 and 1, inclusive");
            } else if (this.rewardMultiplier <= 0D) {
                throw new IllegalStateException("Project template reward multiplier must be positive");
            }
        }
        
        public ProjectTemplate build() {
            this.validate();
            return new ProjectTemplate(this.materialOptions, this.otherRewards, this.getFinalRequirement(), this.requiredMaterialCountOverride, this.baseSuccessChanceOverride,
                    this.rewardMultiplier, this.aidBlocks, this.weightFunction);
        }
    }
}
