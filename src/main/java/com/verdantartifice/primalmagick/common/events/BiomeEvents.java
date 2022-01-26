package com.verdantartifice.primalmagick.common.events;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.config.Config;
import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.worldgen.features.OrePlacementsPM;
import com.verdantartifice.primalmagick.common.worldgen.features.VegetationPlacementsPM;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handlers for biome related events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagick.MODID)
public class BiomeEvents {
    @SubscribeEvent(priority=EventPriority.HIGH)
    public static void loadBiome(BiomeLoadingEvent event) {
        Biome.BiomeCategory cat = event.getCategory();
        
        // Add raw marble, rock salt, and quartz seams to all non-Nether, non-End biomes
        if (isOverworldBiome(event.getName(), cat)) {
            if (Config.GENERATE_MARBLE.get()) {
                event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacementsPM.ORE_MARBLE_RAW_UPPER);
                event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacementsPM.ORE_MARBLE_RAW_LOWER);
            }
            if (Config.GENERATE_ROCK_SALT.get()) {
                event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacementsPM.ORE_ROCK_SALT_UPPER);
                event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacementsPM.ORE_ROCK_SALT_LOWER);
            }
            if (Config.GENERATE_QUARTZ.get()) {
                event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacementsPM.ORE_QUARTZ);
            }
        }
        
        // Add treefolk spawns and sunwood/moonwood trees to forests
        if (Biome.BiomeCategory.FOREST.equals(cat)) {
            event.getSpawns().getSpawner(MobCategory.CREATURE).add(new MobSpawnSettings.SpawnerData(EntityTypesPM.TREEFOLK.get(), 100, 1, 3));
            // TODO Phase sunwood and moonwood trees appropriately
            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacementsPM.TREES_WILD_SUNWOOD);
            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacementsPM.TREES_WILD_MOONWOOD);
        }
    }

    private static boolean isOverworldBiome(@Nonnull ResourceLocation biomeName, @Nonnull Biome.BiomeCategory biomeCategory) {
        if (biomeName.equals(Biomes.STONY_SHORE.getRegistryName())) {
            // Stony Shore has a category of None, but it still exists in the Overworld
            return true;
        } else {
            return !Biome.BiomeCategory.NONE.equals(biomeCategory) && !Biome.BiomeCategory.NETHER.equals(biomeCategory) && !Biome.BiomeCategory.THEEND.equals(biomeCategory);
        }
    }
}
