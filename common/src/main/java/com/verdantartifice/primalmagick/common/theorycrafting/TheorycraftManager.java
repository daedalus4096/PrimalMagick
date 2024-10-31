package com.verdantartifice.primalmagick.common.theorycrafting;

import com.verdantartifice.primalmagick.common.util.WeightedRandomBag;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Primary access point for theorycraft-related methods.
 * 
 * @author Daedalus4096
 */
public class TheorycraftManager {
    @SuppressWarnings("deprecation")
    @Nonnull
    public static Project createRandomProject(@Nonnull ServerPlayer player, @Nonnull BlockPos tablePos) {
        WeightedRandomBag<ProjectTemplate> templateBag = new WeightedRandomBag<>();
        ProjectTemplates.stream(player.level().registryAccess()).forEach(template -> templateBag.add(template, template.getWeight(player)));
        
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
    protected static Set<ResourceLocation> getAllAidBlockIds(RegistryAccess registryAccess) {
        return ProjectTemplates.stream(registryAccess).flatMap(t -> t.aidBlocks().stream()).filter(Objects::nonNull).collect(Collectors.toSet());
    }
    
    @Nonnull
    public static Set<Block> getNearbyAidBlocks(Level level, BlockPos pos) {
        Set<ResourceLocation> allAids = getAllAidBlockIds(level.registryAccess());
        return getSurroundingsInner(level, pos, b -> allAids.contains(Services.BLOCKS.getKey(b)));
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
