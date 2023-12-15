package com.verdantartifice.primalmagick.common.theorycrafting;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagick.common.theorycrafting.rewards.ExperienceReward;
import com.verdantartifice.primalmagick.common.theorycrafting.rewards.IRewardSerializer;
import com.verdantartifice.primalmagick.common.theorycrafting.rewards.ItemReward;
import com.verdantartifice.primalmagick.common.theorycrafting.rewards.LootTableReward;
import com.verdantartifice.primalmagick.common.theorycrafting.weights.ConstantWeight;
import com.verdantartifice.primalmagick.common.theorycrafting.weights.IWeightFunctionSerializer;
import com.verdantartifice.primalmagick.common.theorycrafting.weights.ProgressiveWeight;
import com.verdantartifice.primalmagick.common.util.WeightedRandomBag;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Primary access point for theorycraft-related methods.  Also stores defined research projects in a
 * static registry.
 * 
 * @author Daedalus4096
 */
public class TheorycraftManager {
    public static final IProjectTemplateSerializer TEMPLATE_SERIALIZER = new ProjectTemplate.Serializer();
    protected static final Map<String, IProjectMaterialSerializer<?>> MATERIAL_SERIALIZERS = new ImmutableMap.Builder<String, IProjectMaterialSerializer<?>>()
            .put(ItemProjectMaterial.TYPE, ItemProjectMaterial.SERIALIZER)
            .put(ItemTagProjectMaterial.TYPE, ItemTagProjectMaterial.SERIALIZER)
            .put(ExperienceProjectMaterial.TYPE, ExperienceProjectMaterial.SERIALIZER)
            .put(ObservationProjectMaterial.TYPE, ObservationProjectMaterial.SERIALIZER)
            .build();
    protected static final Map<String, IRewardSerializer<?>> REWARD_SERIALIZERS = new ImmutableMap.Builder<String, IRewardSerializer<?>>()
            .put(ExperienceReward.TYPE, ExperienceReward.SERIALIZER)
            .put(ItemReward.TYPE, ItemReward.SERIALIZER)
            .put(LootTableReward.TYPE, LootTableReward.SERIALIZER)
            .build();
    protected static final Map<String, IWeightFunctionSerializer<?>> WEIGHT_SERIALIZERS = new ImmutableMap.Builder<String, IWeightFunctionSerializer<?>>()
            .put(ConstantWeight.TYPE, ConstantWeight.SERIALIZER)
            .put(ProgressiveWeight.TYPE, ProgressiveWeight.SERIALIZER)
            .build();
    protected static final Map<ResourceLocation, ProjectTemplate> TEMPLATES = new HashMap<>();
    
    @Nullable
    public static IProjectMaterialSerializer<?> getMaterialSerializer(@Nullable String type) {
        return MATERIAL_SERIALIZERS.get(type);
    }
    
    @Nullable
    public static IRewardSerializer<?> getRewardSerializer(@Nullable String type) {
        return REWARD_SERIALIZERS.get(type);
    }
    
    @Nullable
    public static IWeightFunctionSerializer<?> getWeightFunctionSerializer(@Nullable String type) {
        return WEIGHT_SERIALIZERS.get(type);
    }
    
    public static void clearAllTemplates() {
        TEMPLATES.clear();
    }
    
    public static Map<ResourceLocation, ProjectTemplate> getAllTemplates() {
        return Collections.unmodifiableMap(TEMPLATES);
    }
    
    public static boolean registerTemplate(ResourceLocation templateKey, ProjectTemplate template) {
        if (TEMPLATES.containsKey(templateKey)) {
            return false;
        } else {
            TEMPLATES.put(templateKey, template);
            return true;
        }
    }
    
    @SuppressWarnings("deprecation")
    @Nonnull
    public static Project createRandomProject(@Nonnull ServerPlayer player, @Nonnull BlockPos tablePos) {
        WeightedRandomBag<ProjectTemplate> templateBag = new WeightedRandomBag<>();
        for (ProjectTemplate template : TEMPLATES.values()) {
            templateBag.add(template, template.getWeight(player));
        }
        
        // Determine what blocks are nearby so that aid blocks can be checked
        Set<Block> nearby = new HashSet<>();
        Level level = player.level();
        if (level.isAreaLoaded(tablePos, 5)) {
            Iterable<BlockPos> positions = BlockPos.betweenClosed(tablePos.offset(-5, -5, -5), tablePos.offset(5, 5, 5));
            for (BlockPos pos : positions) {
                nearby.add(level.getBlockState(pos).getBlock());
            }
        }
        
        Project retVal = null;
        int attempts = 0;   // Don't allow an infinite loop
        while (retVal == null && attempts < 1000) {
            attempts++;
            ProjectTemplate selectedTemplate = templateBag.getRandom(player.getRandom());
            Project initializedProject = selectedTemplate.initialize(player, nearby);
            
            // Only select the project if it initializes successfully
            if (initializedProject != null) {
                retVal = initializedProject;
            }
        }
        return retVal;
    }
    
    @Nonnull
    protected static Set<ResourceLocation> getAllAidBlockIds() {
        return TEMPLATES.values().stream().flatMap(t -> t.getAidBlocks().stream()).filter(Objects::nonNull).collect(Collectors.toSet());
    }
    
    @Nonnull
    public static Set<Block> getNearbyAidBlocks(Level level, BlockPos pos) {
        Set<ResourceLocation> allAids = getAllAidBlockIds();
        return getSurroundingsInner(level, pos, b -> allAids.contains(ForgeRegistries.BLOCKS.getKey(b)));
    }
    
    @Nonnull
    public static Set<Block> getSurroundings(Level level, BlockPos pos) {
        return getSurroundingsInner(level, pos, b -> true);
    }
    
    @SuppressWarnings("deprecation")
    @Nonnull
    protected static Set<Block> getSurroundingsInner(Level level, BlockPos pos, Predicate<Block> filter) {
        Set<Block> retVal = new HashSet<>();
        
        if (level.isAreaLoaded(pos, 5)) {
            Iterable<BlockPos> positions = BlockPos.betweenClosed(pos.offset(-5, -5, -5), pos.offset(5, 5, 5));
            for (BlockPos searchPos : positions) {
                Block block = level.getBlockState(searchPos).getBlock();
                if (filter.test(block)) {
                    retVal.add(block);
                }
            }
        }
        
        return retVal;
    }
}
