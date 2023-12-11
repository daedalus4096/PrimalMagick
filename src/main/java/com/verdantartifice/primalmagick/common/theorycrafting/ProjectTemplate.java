package com.verdantartifice.primalmagick.common.theorycrafting;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import com.verdantartifice.primalmagick.common.research.IResearchKey;
import com.verdantartifice.primalmagick.common.research.ResearchKeyFactory;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.stats.StatsPM;
import com.verdantartifice.primalmagick.common.theorycrafting.rewards.AbstractReward;
import com.verdantartifice.primalmagick.common.theorycrafting.rewards.IRewardSerializer;
import com.verdantartifice.primalmagick.common.theorycrafting.weights.IWeightFunction;
import com.verdantartifice.primalmagick.common.theorycrafting.weights.IWeightFunctionSerializer;
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
public class ProjectTemplate {
    protected ResourceLocation key;
    protected List<AbstractProjectMaterial> materialOptions = new ArrayList<>();
    protected List<AbstractReward> otherRewards = new ArrayList<>();
    protected IResearchKey requiredResearch;
    protected Optional<Integer> requiredMaterialCountOverride = Optional.empty();
    protected Optional<Double> baseSuccessChanceOverride = Optional.empty();
    protected double rewardMultiplier = 0.25D;
    protected List<ResourceLocation> aidBlocks = new ArrayList<>();
    protected Optional<IWeightFunction> weightFunction = Optional.empty();
    
    protected ProjectTemplate(@Nonnull ResourceLocation key, @Nonnull List<AbstractProjectMaterial> materialOptions, @Nonnull List<AbstractReward> otherRewards, @Nullable IResearchKey requiredResearch,
            @Nonnull Optional<Integer> requiredMaterialCountOverride, @Nonnull Optional<Double> baseSuccessChanceOverride, double rewardMultiplier, @Nonnull List<ResourceLocation> aidBlocks,
            @Nonnull Optional<IWeightFunction> weightFunction) {
        this.key = key;
        this.materialOptions = materialOptions;
        this.otherRewards = otherRewards;
        this.requiredResearch = requiredResearch;
        this.requiredMaterialCountOverride = requiredMaterialCountOverride;
        this.baseSuccessChanceOverride = baseSuccessChanceOverride;
        this.rewardMultiplier = rewardMultiplier;
        this.aidBlocks = aidBlocks;
        this.weightFunction = weightFunction;
    }

    public ResourceLocation getKey() {
        return this.key;
    }
    
    @Nullable
    public List<ResourceLocation> getAidBlocks() {
        return this.aidBlocks;
    }
    
    public double getWeight(Player player) {
        if (this.weightFunction.isPresent()) {
            return this.weightFunction.get().getWeight(player);
        } else {
            return 1D;
        }
    }
    
    @Nullable
    public Project initialize(ServerPlayer player, Set<Block> nearby) {
        if (this.requiredResearch != null && !this.requiredResearch.isKnownByStrict(player)) {
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
        List<AbstractProjectMaterial> materials = new ArrayList<>();
        WeightedRandomBag<AbstractProjectMaterial> options = this.getMaterialOptions(player);
        while (materials.size() < maxMaterials && attempts < 1000) {
            attempts++;
            AbstractProjectMaterial material = options.getRandom(player.getRandom()).copy();
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
    protected WeightedRandomBag<AbstractProjectMaterial> getMaterialOptions(ServerPlayer player) {
        WeightedRandomBag<AbstractProjectMaterial> retVal = new WeightedRandomBag<>();
        for (AbstractProjectMaterial material : this.materialOptions) {
            if (material.isAllowedInProject(player)) {
                retVal.add(material, material.getWeight());
            }
        }
        return retVal;
    }
    
    public static class Serializer implements IProjectTemplateSerializer {
        private static final Logger LOGGER = LogManager.getLogger();
        
        @Override
        public ProjectTemplate read(ResourceLocation templateId, JsonObject json) {
            String keyStr = json.getAsJsonPrimitive("key").getAsString();
            if (keyStr == null) {
                throw new JsonSyntaxException("Illegal key in project template JSON for " + templateId.toString());
            }
            ResourceLocation key = new ResourceLocation(keyStr);
            
            IResearchKey requiredResearch = null;
            if (json.has("required_research")) {
                requiredResearch = ResearchKeyFactory.parse(json.getAsJsonPrimitive("required_research").getAsString());
            }
            
            Optional<Integer> materialCountOverride = Optional.empty();
            if (json.has("required_material_count_override")) {
                materialCountOverride = Optional.of(Integer.valueOf(json.getAsJsonPrimitive("required_material_count_override").getAsInt()));
            }
            
            Optional<Double> baseSuccessChanceOverride = Optional.empty();
            if (json.has("base_success_chance_override")) {
                baseSuccessChanceOverride = Optional.of(Double.valueOf(json.getAsJsonPrimitive("base_success_chance_override").getAsDouble()));
            }
            
            double rewardMultiplier = json.getAsJsonPrimitive("reward_multiplier").getAsDouble();
            
            List<ResourceLocation> aidBlocks = new ArrayList<>();
            JsonArray aidsArray = json.getAsJsonArray("aid_blocks");
            for (JsonElement aidElement : aidsArray) {
                ResourceLocation aidBlock;
                try {
                    aidBlock = new ResourceLocation(aidElement.getAsString());
                }
                catch (Exception e) {
                    throw new JsonSyntaxException("Invalid aid block in project template JSON for " + templateId.toString());
                }
                if (!ForgeRegistries.BLOCKS.containsKey(aidBlock)) {
                    throw new JsonSyntaxException("Invalid aid block in project template JSON for " + templateId.toString());
                }
                aidBlocks.add(aidBlock);
            }
            
            List<AbstractProjectMaterial> materials = new ArrayList<>();
            JsonArray materialsArray = json.getAsJsonArray("material_options");
            for (JsonElement materialElement : materialsArray) {
                try {
                    JsonObject materialObj = materialElement.getAsJsonObject();
                    IProjectMaterialSerializer<?> materialSerializer = TheorycraftManager.getMaterialSerializer(materialObj.getAsJsonPrimitive("type").getAsString());
                    materials.add(materialSerializer.read(templateId, materialObj));
                }
                catch (Exception e) {
                    throw new JsonSyntaxException("Invalid material in project template JSON for " + templateId.toString(), e);
                }
            }
            
            List<AbstractReward> otherRewards = new ArrayList<>();
            if (json.has("other_rewards")) {
                JsonArray rewardsArray = json.getAsJsonArray("other_rewards");
                for (JsonElement rewardElement : rewardsArray) {
                    try {
                        JsonObject rewardObj = rewardElement.getAsJsonObject();
                        IRewardSerializer<?> rewardSerializer = TheorycraftManager.getRewardSerializer(rewardObj.getAsJsonPrimitive("type").getAsString());
                        otherRewards.add(rewardSerializer.read(templateId, rewardObj));
                    } catch (Exception e) {
                        throw new JsonSyntaxException("Invalid reward in project template JSON for " + templateId.toString(), e);
                    }
                }
            }
            
            Optional<IWeightFunction> weightOpt = Optional.empty();
            if (json.has("weight_function")) {
                JsonObject weightObj = json.getAsJsonObject("weight_function");
                String functionType = weightObj.getAsJsonPrimitive("type").getAsString();
                IWeightFunctionSerializer<?> serializer = TheorycraftManager.getWeightFunctionSerializer(functionType);
                if (serializer != null) {
                    weightOpt = Optional.ofNullable(serializer.read(templateId, weightObj));
                } else {
                    LOGGER.warn("Unknown weight function type {} in JSON for project template {}", functionType, templateId.toString());
                    weightOpt = Optional.empty();
                }
            }
            
            return new ProjectTemplate(key, materials, otherRewards, requiredResearch, materialCountOverride, baseSuccessChanceOverride, rewardMultiplier, aidBlocks, weightOpt);
        }

        @Override
        public ProjectTemplate fromNetwork(FriendlyByteBuf buf) {
            ResourceLocation key = buf.readResourceLocation();
            IResearchKey requiredResearch = buf.readBoolean() ? ResearchKeyFactory.parse(buf.readUtf()) : null;
            Optional<Integer> materialCountOverride = buf.readBoolean() ? Optional.of(buf.readVarInt()) : Optional.empty();
            Optional<Double> baseSuccessChanceOverride = buf.readBoolean() ? Optional.of(buf.readDouble()) : Optional.empty();
            double rewardMultiplier = buf.readDouble();
            
            List<ResourceLocation> aidBlocks = new ArrayList<>();
            int aidCount = buf.readVarInt();
            for (int index = 0; index < aidCount; index++) {
                aidBlocks.add(buf.readResourceLocation());
            }
            
            List<AbstractProjectMaterial> materials = new ArrayList<>();
            int materialCount = buf.readVarInt();
            for (int index = 0; index < materialCount; index++) {
                String materialType = buf.readUtf();
                IProjectMaterialSerializer<?> serializer = TheorycraftManager.getMaterialSerializer(materialType);
                if (serializer != null) {
                    materials.add(serializer.fromNetwork(buf));
                } else {
                    throw new IllegalArgumentException("Unknown theorycrafting project material type " + materialType);
                }
            }
            
            List<AbstractReward> rewards = new ArrayList<>();
            int rewardCount = buf.readVarInt();
            for (int index = 0; index < rewardCount; index++) {
                String rewardType = buf.readUtf();
                IRewardSerializer<?> serializer = TheorycraftManager.getRewardSerializer(rewardType);
                if (serializer != null) {
                    rewards.add(serializer.fromNetwork(buf));
                } else {
                    throw new IllegalArgumentException("Unknown theorycrafting project reward type " + rewardType);
                }
            }
            
            Optional<IWeightFunction> weightOpt;
            if (buf.readBoolean()) {
                String functionType = buf.readUtf();
                IWeightFunctionSerializer<?> serializer = TheorycraftManager.getWeightFunctionSerializer(functionType);
                if (serializer != null) {
                    weightOpt = Optional.ofNullable(serializer.fromNetwork(buf));
                } else {
                    LOGGER.warn("Unknown weight function type {} from network for project template {}", functionType, key.toString());
                    weightOpt = Optional.empty();
                }
            } else {
                weightOpt = Optional.empty();
            }
            
            return new ProjectTemplate(key, materials, rewards, requiredResearch, materialCountOverride, baseSuccessChanceOverride, rewardMultiplier, aidBlocks, weightOpt);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ProjectTemplate template) {
            buf.writeResourceLocation(template.key);
            if (template.requiredResearch != null) {
                buf.writeBoolean(true);
                buf.writeUtf(template.requiredResearch.toString());
            } else {
                buf.writeBoolean(false);
            }
            template.requiredMaterialCountOverride.ifPresentOrElse((val) -> {
                buf.writeBoolean(true);
                buf.writeVarInt(val);
            }, () -> {
                buf.writeBoolean(false);
            });
            template.baseSuccessChanceOverride.ifPresentOrElse((val) -> {
                buf.writeBoolean(true);
                buf.writeDouble(val);
            }, () -> {
                buf.writeBoolean(false);
            });
            buf.writeDouble(template.rewardMultiplier);
            buf.writeVarInt(template.aidBlocks.size());
            for (ResourceLocation aidBlock : template.aidBlocks) {
                buf.writeResourceLocation(aidBlock);
            }
            buf.writeVarInt(template.materialOptions.size());
            for (AbstractProjectMaterial material : template.materialOptions) {
                buf.writeUtf(material.getMaterialType());
                material.toNetwork(buf);
            }
            buf.writeVarInt(template.otherRewards.size());
            for (AbstractReward reward : template.otherRewards) {
                buf.writeUtf(reward.getRewardType());
                reward.getSerializer().toNetwork(buf, reward);
            }
            template.weightFunction.ifPresentOrElse(weight -> {
                buf.writeBoolean(true);
                buf.writeUtf(weight.getFunctionType());
                weight.getSerializer().toNetwork(buf, weight);
            }, () -> {
                buf.writeBoolean(false);
            });
        }
    }
}
