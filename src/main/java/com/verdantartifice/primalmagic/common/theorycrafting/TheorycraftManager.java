package com.verdantartifice.primalmagic.common.theorycrafting;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagic.common.util.WeightedRandomBag;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

/**
 * Primary access point for theorycraft-related methods.  Also stores defined research projects in a
 * static registry.
 * 
 * @author Daedalus4096
 */
public class TheorycraftManager {
    protected static final Map<String, Supplier<AbstractProject>> PROJECT_SUPPLIERS = new HashMap<>();
    protected static final Map<String, IProjectMaterialSerializer<?>> MATERIAL_SERIALIZERS = new ImmutableMap.Builder<String, IProjectMaterialSerializer<?>>()
            .put(ItemProjectMaterial.TYPE, ItemProjectMaterial.SERIALIZER)
            .put(ItemTagProjectMaterial.TYPE, ItemTagProjectMaterial.SERIALIZER)
            .put(ExperienceProjectMaterial.TYPE, ExperienceProjectMaterial.SERIALIZER)
            .put(ObservationProjectMaterial.TYPE, ObservationProjectMaterial.SERIALIZER)
            .build();
    
    public static void registerProjectType(@Nullable String type, @Nullable Supplier<AbstractProject> supplier) {
        // Don't allow null or empty data in the project registry
        if (type != null && !type.isEmpty() && supplier != null) {
            PROJECT_SUPPLIERS.put(type, supplier);
        }
    }
    
    @Nullable
    public static Supplier<AbstractProject> getProjectSupplier(@Nullable String type) {
        return PROJECT_SUPPLIERS.get(type);
    }
    
    @Nullable
    public static IProjectMaterialSerializer<?> getMaterialSerializer(@Nullable String type) {
        return MATERIAL_SERIALIZERS.get(type);
    }
    
    @Nonnull
    public static AbstractProject createRandomProject(@Nonnull PlayerEntity player, @Nonnull BlockPos tablePos) {
        WeightedRandomBag<String> typeBag = new WeightedRandomBag<>();
        for (String typeStr : PROJECT_SUPPLIERS.keySet()) {
            typeBag.add(typeStr, 1);
        }
        
        // Determine what blocks are nearby so that aid blocks can be checked
        Set<Block> nearby = new HashSet<>();
        if (player.world.isAreaLoaded(tablePos, 5)) {
            Iterable<BlockPos> positions = BlockPos.getAllInBoxMutable(tablePos.add(-5, -2, -5), tablePos.add(5, 2, 5));
            for (BlockPos pos : positions) {
                nearby.add(player.world.getBlockState(pos).getBlock());
            }
        }
        
        AbstractProject retVal = null;
        int attempts = 0;   // Don't allow an infinite loop
        while (retVal == null && attempts < 1000) {
            attempts++;
            String selectedType = typeBag.getRandom(player.getRNG());
            AbstractProject tempProject = ProjectFactory.getProjectFromType(selectedType);
            // Only select the project if it initializes successfully and any required aid blocks are nearby
            if (tempProject != null && tempProject.initialize(player) && (tempProject.getAidBlock() == null || nearby.contains(tempProject.getAidBlock()))) {
                retVal = tempProject;
            }
        }
        return retVal;
    }
}
