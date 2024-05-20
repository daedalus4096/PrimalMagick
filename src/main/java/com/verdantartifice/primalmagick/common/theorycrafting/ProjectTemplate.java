package com.verdantartifice.primalmagick.common.theorycrafting;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.research.IResearchKey;
import com.verdantartifice.primalmagick.common.research.ResearchKeyFactory;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.stats.StatsPM;
import com.verdantartifice.primalmagick.common.theorycrafting.materials.AbstractProjectMaterial;
import com.verdantartifice.primalmagick.common.theorycrafting.materials.IProjectMaterialSerializer;
import com.verdantartifice.primalmagick.common.theorycrafting.rewards.AbstractReward;
import com.verdantartifice.primalmagick.common.theorycrafting.rewards.IRewardSerializer;
import com.verdantartifice.primalmagick.common.theorycrafting.weights.AbstractWeightFunction;
import com.verdantartifice.primalmagick.common.theorycrafting.weights.IWeightFunctionSerializer;
import com.verdantartifice.primalmagick.common.util.CodecUtils;
import com.verdantartifice.primalmagick.common.util.WeightedRandomBag;

import net.minecraft.network.FriendlyByteBuf;
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
public record ProjectTemplate(ResourceLocation key, List<AbstractProjectMaterial<?>> materialOptions, List<AbstractReward<?>> otherRewards, Optional<AbstractRequirement<?>> requirement,
        OptionalInt requiredMaterialCountOverride, OptionalDouble baseSuccessChanceOverride, double rewardMultiplier, List<ResourceLocation> aidBlocks,
        Optional<AbstractWeightFunction<?>> weightFunction) {
    public static final Codec<ProjectTemplate> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("key").forGetter(ProjectTemplate::key),
            AbstractProjectMaterial.CODEC.listOf().fieldOf("materialOptions").forGetter(ProjectTemplate::materialOptions),
            AbstractReward.CODEC.listOf().fieldOf("otherRewards").forGetter(ProjectTemplate::otherRewards),
            AbstractRequirement.CODEC.optionalFieldOf("requirement").forGetter(ProjectTemplate::requirement),
            CodecUtils.asOptionalInt(Codec.INT.optionalFieldOf("requiredMaterialCountOverride")).forGetter(ProjectTemplate::requiredMaterialCountOverride),
            CodecUtils.asOptionalDouble(Codec.DOUBLE.optionalFieldOf("baseSuccessChanceOverride")).forGetter(ProjectTemplate::baseSuccessChanceOverride),
            Codec.DOUBLE.fieldOf("rewardMultiplier").forGetter(ProjectTemplate::rewardMultiplier),
            ResourceLocation.CODEC.listOf().fieldOf("aidBlocks").forGetter(ProjectTemplate::aidBlocks),
            AbstractWeightFunction.CODEC.optionalFieldOf("weightFunction").forGetter(ProjectTemplate::weightFunction)
        ).apply(instance, ProjectTemplate::new));
    
    public double getWeight(Player player) {
        return this.weightFunction.map(func -> func.getWeight(player)).orElse(1D);
    }
    
    @Nullable
    public Project initialize(ServerPlayer player, Set<Block> nearby) {
        if (this.requirement.isPresent() && !this.requirement.get().isMetBy(player)) {
            // Fail initialization to prevent use if the player doesn't have the right research unlocked
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
        List<AbstractProjectMaterial<?>> materials = new ArrayList<>();
        WeightedRandomBag<AbstractProjectMaterial<?>> options = this.getMaterialOptions(player);
        while (materials.size() < maxMaterials && attempts < 1000) {
            attempts++;
            AbstractProjectMaterial<?> material = options.getRandom(player.getRandom());
            if (!materials.contains(material)) {
                materials.add(material);
            }
        }
        if (materials.size() < maxMaterials) {
            // Fail initialization to prevent use if not all materials could be allocated
            return null;
        }
        
        // Create new initialized project
        return new Project(this.key, materials, this.otherRewards, this.getBaseSuccessChance(player), this.rewardMultiplier, foundAid);
    }
    
    protected int getRequiredMaterialCount(Player player) {
        return this.requiredMaterialCountOverride.orElseGet(() -> {
            // Get projects completed from stats and calculate based on that
            int completed = StatsManager.getValue(player, StatsPM.RESEARCH_PROJECTS_COMPLETED);
            return Math.min(4, 1 + (completed / 5));
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
}
